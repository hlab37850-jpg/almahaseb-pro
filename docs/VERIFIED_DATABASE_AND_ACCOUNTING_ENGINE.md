# VERIFIED DATABASE AND ACCOUNTING ENGINE EVIDENCE\n\nStatus: ANALYSIS ONLY. No application code or UI implementation is authorized by this document.\n\nSource of truth:\n- APK analysis repository: hlab37850-jpg/almahaseb-pro-apk-analysis\n- Primary schema: resources/res/raw/sql.xml\n- Migration/update layer: resources/res/raw/all.xml\n- Runtime database access is through decompiled class defpackage.Z00.\n\n## 1. Core reference tables\n\ntran_type mature IDs:\n0 افتتاحي; 1 بيع; 2 شراء; 3 تحويل مخزني; 4 تسوية مخزنية; 5 قبض; 6 صرف; 7 جرد; 8 عرض سعر; 9 طلب شراء; 10 بيع عملة; 11 صرف مخزني; 13 شراء عملة; 21 توريد مخزني; -1 قيد افتتاحي; -2 قيود يومية; -3 سندات; -4 ضريبة; -5 رسوم; -6 عملات.\n\nbill_type: 0 حركة, 1 نقد, 2 آجل.\n\ncus_type: 0 عملاء, 1 موردون, 2 مبيعات ومشتريات, 4 نقدية, 5 مصروفات, 6 إيرادات, 7 أخرى.\n\nbranches: id, name, date_, IS_ACTIVE, ADDRESS, gsm, remarks, param1, param2. Seed branch: id 0 = المخزن الرئيسي.\ngroups: id, name, param1, param2. Seed id 0 = عام.\ncurrency: id, name, curr_type, param1, param2; mature migrations add fils_name and code_name. Seed: 0 محلي (YR), 1 دولار (USD).\nitem_type: id, name, remarks, param1, param2. Seed 0 = عام.\nitem_type_: separate classification: 0 سلعة, 1 خدمة.\n\n## 2. Customers and account-like entities\n\ncustomers mature fields: id, name, gsm, g_id, cus_type_id, date_, ADDRESS, remarks, param1, param2, f1, f2, f3. Mature migrations also reference vat_no and acc_p_id.\n\nVerified special negative IDs: -1 مبيعات آجل; -2 مشتريات آجل; -3 الصندوق; -5 مبيعات نقدي; -6 مشتريات نقدي; -7 الخصم المكتسب; -8 الخصم المسموح به; -9 مردودات مبيعات آجل; -10 مردودات مشتريات آجل; -11 مردودات مبيعات نقدي; -12 مردودات مشتريات نقدي; -13 رأس المال; -14 بضاعة أول المدة; -15 عجز و زيادة البضاعة; -16 البضاعة التالفة; -17 ح/الارباح والخسائر; -18 ضريبة القيمة المضافة; -30 تسوية المخزون-صرف وتوريد.\nThese are seeded database entities, not permission to create demo transactions in the reconstruction.\n\n## 3. Items and pricing\n\nitems mature fields: id, name, item_type_id, date_, IS_ACTIVE, o_qty, o_cost, o_br_id, o_date, e_date, pic, remarks, qty, price, curr_id, curr_price, param1, param2. Migration adds item_type_id_.\nitems_temp is a temporary editing table. Bill_edit and Bill_inv use it while editing line items.\nitem_price stores item/currency selling price and later unit_id; mature unique index is item_id,curr_id,unit_id.\nitem_price_history records price history. Trigger item_price_update inserts history after item_price insertion.\n\n## 4. Bills and bill lines\n\nbills mature fields: id, date_, remarks, bill_no, curr_id, bill_type, tr_type, is_back, br_id, cus_id, tran_status, to_br_id, amount, d_amount, tax_amount, param1, param2, param3, curr_price, bill_no2, cash_id.\nMigrations add discount_id, d_val, paid_amount, adj_id, adj_acc, curr_mod.\nbill_transactions fields: bill_id, item_id, item_type_id, curr_id, qty, cost_price, sls_u_price, d_amount, param1, param2, param3, cash_id, remark.\n\n## 5. Proven accounting side effects\n\nbills_delete deletes transactions whose bill_id equals the deleted bill.\n\nCredit purchase: tr_type=2, bill_type=2. Normal branch posts a transaction using -2 (مشتريات آجل) against the selected customer. Non-zero paid_amount creates a separate customer/cash posting.\n\nCredit sale: tr_type=1, bill_type=2. Normal branch posts customer against -1 (مبيعات آجل). Non-zero paid_amount creates a separate customer/cash posting.\n\nCash sale: tr_type=1, bill_type=1. Posting uses -3 (الصندوق) and -5 (مبيعات نقدي). Remarks include customer name, bill remarks and bill number.\n\nCash purchase: tr_type=2, bill_type=1. Posting uses -6 (مشتريات نقدي) and -3 (الصندوق).\n\nTax/other-cost branches split the posting into multiple transaction rows and use -18 for VAT. paid_amount remains a separate posting when non-zero.\n\nPurchase returns use -10 (مردودات مشتريات آجل) for the credit-return branch; mature triggers also have tax/cost and paid-amount variants.\nSales returns have a parallel mature trigger family. Their exact conditions must be preserved from the source instead of replaced by a generic return formula.\n\n## 6. Opening inventory accounting\n\nTrigger items_OP_insert proves that non-zero opening quantity and cost create a transaction: cus_id=-14, t_cus_id=-13, in=1, out=o_qty*o_cost, curr_id=item currency, bill_id=-4, item_id=item id, date=o_date. The remark contains the opening transaction type and item name.\nitems_OP_update removes the item-linked posting and recreates it when the closing-year condition allows editing.\nTherefore opening inventory is an accounting posting, not only a display field.\n\n## 7. Manual transaction normalization\n\ntransactions_cr_insert: when in=-1, t_cus_id is null and bill_id=0, the row is changed to in=1, t_cus_id=cus_id and cus_id=-4.\ntransactions_db_insert: when in=1, t_cus_id is null and bill_id=0, t_cus_id becomes -3.\nDo not assume every transaction caller supplies final normalized counterparty fields.\n\n## 8. Inventory calculation views\n\nitem_avg_cost calculates, by item/date/currency/branch: incoming quantity, net quantity, sales quantity, issue quantity, weighted average purchase cost, sales amount, purchase amount and discount amount. It also unions opening-item values from items.\nitem_avg_cost2 groups purchase quantity/cost totals and unions opening inventory.\nitem_avg_cost4 represents sales, purchases, transfers and opening inventory as item movement rows; transfer direction is represented by positive/negative transaction-type markers.\n\n## 9. Customer transaction view\n\ncus_tr_curr_view combines transactions where the customer is transactions.cus_id or transactions.t_cus_id and joins groups, customers, currencies and customer types. This proves balance/movement logic uses both sides of a transaction relationship.\n\n## 10. Closing-year structures\n\nVerified tables: items_closing_balance, current_closing_balance, closing_year.\ncurrent_closing_balance is seeded with id 0 and a date one year before the current database date.\nOpening-item update logic explicitly checks closing_year, so annual closing affects editability of opening data.\n\n## 11. Permissions and screens database\n\nscreens table is data-driven and uses parent IDs. Verified IDs: 1 عمليات مخزنية; 2 قيود وحسابات; 3 أصناف; 4 العملات; 5 التقارير; 6 تحويل مخزني; 7 تسوية مخزنية; 8 إضافة مخزن; 9 قيد يومي; 10 قيد إفتتاحي; 11 إضافة حساب; 12 حركة الصندوق; 13 دليل الحسابات; 14 إقفال سنوي; 15 الأصناف; 16 أسعار البيع; 17 المخزون; 18 أرباح الأصناف; 19 إضافة عملة; 20 سعر العملات; 21 حركة الأصناف; 22 المخزون; 23 ميزان المراجعة; 24 قائمة الدخل; 25 المركز المالي; 26 تقارير أخرى.\nuser_priv is queried against screens, so permissions are data-driven rather than only visual hiding.\n\n## 12. Other verified support tables\n\nvalid(imei, imei_code); act_req(imei, aid, name, phone, country, email, status, date_, reply, user_reply, reply_from); notifications(id, name, date_, status, param1, param2); days_(id,name); trigger_flags(is_active); adj_type(id,name,acc_id,in_); tr_p_temps; currency_price_temp; currency_price; discount_type.\nadj_type verified: 1 عجز -> -15; 2 زيادة -> -15; 3 تالف -> -16; 4 افتتاحي -> -14.\ndiscount_type: 0 نسبة مئوية; 1 مبلغ.\n\n## 13. Migration conclusion\n\nall.xml is a mature migration layer, not merely an initial schema. It adds cash/fund fields, transaction types, discounts, paid_amount, VAT-related fields, item classifications, currency metadata, bill numbering, adjustment fields and currency modification fields.\nTherefore reconstruction must follow the mature schema and triggers, not only the earliest CREATE TABLE statements in sql.xml.\n\n## 14. Mandatory reconstruction rule\n\nBefore implementation: reproduce the mature data model; preserve transaction IDs; preserve special negative account/customer IDs and roles; reproduce trigger-equivalent posting; preserve opening inventory accounting; preserve returns/tax/discount/paid-amount/other-cost branches; preserve closing-year restrictions; preserve data-driven permissions; and never add invented demo business records.\n\nRemaining analysis is still required before implementation: complete migration coverage, exact report SQL/formulas, adjustment/transfer triggers, printing/export paths, activation/online behavior, and every remaining screen/dialog. No UI or application code should be written until those evidence gaps are closed.

## 15. Mature schema inventory — directly extracted from all.xml

Direct parsing of the recovered migration file found 68 table-creation statements, 16 view-creation statements, 102 trigger-creation statements, and 298 ALTER TABLE ... ADD statements. These are source inventory counts; replacement/drop statements are historical migration steps.

### Table names
`discount_type, tmp_real, tran_type, bill_type, cus_type, branches, customers, transactions, valid, groups, currency, item_type, item_type_, items, items_temp, item_price, item_price_history, currency_price_temp, currency_price, bills, bill_transactions, trigger_flags, revenue_type, account_tree, account_tree_type, items_closing_balance, current_closing_balance, act_req, days_, tr_p_temps, adj_type, units, unit_item, bills2, bill_transactions2, items_cost_calc, error_exception, screens, sys_conf, closing_year, notifications, chat_rooms, messages, requests, requests_bills, requests_bills_det, requests_out, doc_hdr, doc_det, requests_notify, requests_items, requests_items_unit, requests_items_price, requests_names, requests_curr, requests_curr_price, tax_type, tax, cus_limit, cus_limit_h, reminders, users, user_priv, bills_log, transactions_issue, z_units, z_status, app_ver`.

### View names
`item_avg_cost2, item_avg_cost4, transactions_v, bills_v, bills2_v, bill_transactions_v, items_v, units_v, currency_price_v, cus_tr_curr_view, bill_transactions2_v, profit_loss_vw, items_cost_calc_end_yr, transactions_tot_v, cus_curr`. One parser token named `if` was a SQL false positive and is not treated as a real object.

## 16. Mature-schema expansion

The migration adds fields far beyond the initial CREATE TABLE blocks. High-impact additions include:

- transactions: bill/item references, purchase references, cash/fund, transaction type, discounts, currency prices/modifiers, current-currency fields, user/online fields and audit fields.
- bills: cash account, currency price, bill sequence, adjustment IDs/accounts, discount ID/value, paid amount, online metadata, tax ID/value, time, other-cost fields, cost account, return-cost, user/audit fields, QR/ZATCA fields and SAR currency-price.
- items: opening-cost/expiry/picture/remarks, classification, unit/base-unit/value, barcode, online fields and second expiry field.
- bill_transactions: discount/remark/cash/unit/value/base-unit/quantity-conversion/expiry/allocated-other-cost fields.
- customers: customer type/address/VAT/account-parent/online/SMS/WhatsApp/commercial-registration fields.
- screens: is_active/new/edit/view/del permission columns.
- users/user_priv and many master tables: online synchronization identifiers and audit fields.
- currency/tax/unit/item-price structures: currency metadata, tax controls, unit conversion, price-history and online fields.
- ZATCA structures: company/branch/unit/status and bill QR/hash/UUID/signing metadata.

A reconstruction based only on the first `sql.xml` CREATE TABLE definitions would therefore be incomplete.

## 17. Accounting-trigger families verified

The migration contains trigger families for opening inventory, credit/cash sales and purchases, returns, tax/other-cost branches, inventory-in/out documents, manual debit/credit normalization, opening/discount postings, currency-price propagation, unit validation, closing-year restrictions, bill-line validation, tax defaults/protection, customer credit limits, user privilege protection, bill user/audit stamping and online synchronization.

The original architecture consequently relies heavily on SQLite triggers for accounting integrity. Copying only Activity-level INSERT statements would not reproduce equivalent behavior.

## 18. Account hierarchy seed mapping

Migration statements assign parent account IDs by customer class:

- type 0 customers → parent 123
- type 1 suppliers → parent 221
- type 4 cash → parent 121
- type 5 expenses → parent 322
- type 6 income → parent 42
- type 7 other → parent 124

Special negative entities are also mapped to dedicated parents, including capital, opening inventory, discounts, cash/credit sales and purchases, returns, VAT and other costs.

`account_tree` fields: id, name, parent_id, p, cus_id, admin, unique(name).
`account_tree_type`: 0 = فرعي, 1 = رئيسي.

## 19. Configuration seeds verified

`sys_conf` initializes:
- 1 Account Cash = -3
- 2 F-DATE = now
- 3 CURR = 0
- 4 Fund Cash = -13
- 5 VAT Acc = -18
- 6 VAT enable = 0
- 8 share_type = 0
- 9 curr_diff_id = -27
- 10 show_end_date = 0
- 100 dev_id = empty
- 101 online2 = 0

These are database facts, not invented UI defaults.

## 20. Permission enforcement

`screens` is seeded with parent/child IDs and extended with is_active/new/edit/view/del. `user_priv` stores per-user CRUD permissions with a unique(user_id,screen_id) constraint. Protective triggers include `prevent_invalid_priv`, `prevent_stop_admin`, `prevent_add_user` and `prevent_update_admin_br_cash`. Permission enforcement is therefore database-backed, not merely visual.

## 21. Migration precision rule

The migration file contains historical definitions followed by drops/replacements. The **last effective definition in migration order** is the required reconstruction reference.


## 22. Verified reporting views and formulas

### cus_tr_curr_view
The mature customer transaction view joins groups, customers, transactions, currency, customer type and account tree, and UNIONs both transaction sides: rows where transactions.cus_id equals the customer and rows where transactions.t_cus_id equals the customer. It exposes converted OUT amounts using transaction/current currency prices plus group/type/account/user/address fields.

### transactions_tot_v
Groups transactions by currency, source customer, target customer and in, exposing summed OUT, latest normalized date, count and latest transaction ID.

### cus_curr
Builds customer balances from transactions_tot_v and customers_tree_v. It separately derives credit/debit totals, computes balance as `sum(case when t_cus_id=customer then -out else in*out end)`, tracks last date, days late and transaction count, and UNIONs customers with no transactions as zero-balance rows.

### item_avg_cost2
Groups purchase quantities and cost totals by item/currency/unit price/date, then UNIONs opening inventory quantities/costs.

### item_avg_cost4
Produces item movement rows for sales/purchases, stock transfers and opening inventory. Returns negate the transaction type. A transfer is represented once for the source branch and once for the destination branch with opposite transaction sign.

### profit_loss_vw
The mature definition derives daily profit/loss components from cus_tr_curr_view and items_cost_calc: Net Sales, Cost Sales, Discount.IN, Income and Outcome. Sales/discount calculations use transaction-side direction and special negative customer IDs. Cost of sales comes from items_cost_calc using purchase/opening/return quantity-cost calculations. Income and Outcome are filtered by account-tree ID prefixes (4xx and 3xx respectively) with explicit exclusions.

## 23. Balance semantics that must not be simplified

The database does not treat `in=1` as a universal debit/credit meaning. Balance depends on whether the customer is the transaction target (`t_cus_id`) or source (`cus_id`). Currency conversion, account-tree membership, special negative IDs, branch direction, return sign and opening inventory all participate in reporting.

## 24. Reconstruction consequence

Report screens cannot be implemented accurately from labels/layouts alone. Their query layer and database views are part of the required reconstruction contract. No application/UI code is authorized yet.
