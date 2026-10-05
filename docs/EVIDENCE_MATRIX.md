# Evidence Matrix — Main Actions, Reports, Settings and Transaction Types

Status: ANALYSIS ONLY. This is a source-backed matrix; unresolved cells are intentionally marked unresolved.

## 1. Primary main-page grid — exact click mapping

Source: `Fragment/d.java`, method `onItemClick`.

### Restricted/group page branch

When `p != "0"` and the multi-user condition does not apply:

| Grid index | Actual title source | Destination / operation |
|---|---|---|
| 0 | titles4_[0] | Bills2, TR_TYPE=2 |
| 1 | titles4_[1] | Bills2, TR_TYPE=1 |
| 2 | titles4_[2] | `Z00.y(activity,p,q,true)` |
| 3 | titles4_[3] | Frag_Report3_item_balance |
| 4 | titles4_[4] | Revenue_item_Report, TR_TYPE=index (4) |
| 5 | titles4_[5] | FragmentStatePagerSupport, screen_no=1 |

### Primary/root page branch

For the root page / special multi-user branch:

| Grid index | Actual title | Destination |
|---|---|---|
| 0 | title4[0] | Daily_Doc_Report |
| 1 | title4[1] | Bills2, TR_TYPE=1 |
| 2 | title4[2] | FragmentStatePagerSupport, screen_no=2 |
| 3 | title4[3] | Frag_Report3_item_balance |
| 4 | title4[4] | Info_edit3, action_type=1 |
| 5 | title4[5] | Z00.y(activity,p,q,true) |
| 6 | title4[6] | item_price_exp, action_id=2 |
| 7 | title4[7] | Frag_Report3_item_balance |
| 8 | title4[8] | Action_list, action_id=3 |

The exact displayed title text must always come from `titles4` / `titles4_`; the source does not justify substituting modern labels.

## 2. Secondary action list

Adapter: `C3981tg`.

Important: the adapter itself only renders `list_item5.xml`; it contains no routing logic.

The routing therefore lives in Fragment `d` and is not safely inferable from the adapter alone.

The source shows action IDs 6,7,28,29,30,8,9,10,11,12,13,31,32,14,15,16,27,17,22,18,19,20,21,23,24,25,26 and -3/-2 branches. These are a larger action catalogue than the visible nine `titles3_main` rows. They are used by other action/report paths and must not be treated as one-to-one indices without tracing the action-list data source.

## 3. Expandable report tree

The root groups are loaded by:

`n(Cursor)`

Each row becomes:

`C1896ch(_id, name, "", "0", 0, "", "")`

The adapter is `SC`.

Group click:
- calculates child data via `r(group_id)`
- stores it in the group
- refreshes adapter.

Child click:
- reads `C1815bh`
- extracts action id from `c()`
- extracts display/action text from `e()`
- calls `t(actionId, actionText)`.

Therefore the report/action tree is a database-backed action hierarchy. The visible group names and child names must come from the database/action tables, not hardcoded UI assumptions.

## 4. Verified action destinations from t(actionId,text)

| Action ID | Verified destination/operation |
|---:|---|
| -3 | Adj2, TR_TYPE=11 |
| 6 | Moves2, TR_TYPE=3 |
| 7 | Moves2, TR_TYPE=4 |
| 28 | Invs3, TR_TYPE=7 |
| 29 | Offers, TR_TYPE=8 |
| 30 | Orders, TR_TYPE=9 |
| 8 | BranchList_edit |
| 9 | Customer_Det_List_edit4 |
| 10 | Customer_Det_List_edit3 |
| 11 | FragmentStatePagerSupport, screen_no=9 |
| 12 | FragmentStatePagerSupport, screen_no=11 + customer/date extras |
| 13 | Account_Tree_Main |
| 31 | CusLimit_edit |
| 32 | Daily_Curr_Report |
| 14 | year-close operation |
| 15 | Info_edit3, action_type=1 |
| 16 | item_price_exp, action_id=2 |
| 27 | Unit_item_edit |
| 17 / 22 | Frag_Report3_item_balance |
| 18 | Revenue_item_Report, TR_TYPE=2 |
| 19 | CurrList_edit |
| 20 | currency_price_exp |
| 21 | FragmentStatePagerSupport, screen_no=1 |
| 23 | Trail_Balance_Report |
| 24 | Accounts_Total_Report |
| 25 | Balance_Sheet_Report |
| 26 | Action_list, action_id=3 |
| 21 (later fallback path) | Adj2, TR_TYPE=21 |

The source contains duplicated/decompiled control flow around action 21; this is explicitly flagged rather than silently normalized.

## 5. Permission behavior

For multi-user mode (`C2160f10.h > 0`), several actions check a permission key/action code using:

`new C2160f10(db).l(code, operation)`

When denied, the app displays `no_priv`.

Verified examples:
- action 6: view
- action 7: view
- action 28: view
- action 9: new
- action 10: new
- main sales/purchase routes also check `-6`, `-7`, or `6`.

This permission system is part of behavior and must be modeled rather than bypassed.

## 6. Footer controls

### help
Calls `help_btn`.

### transfer
Calls `move_tr_btn`:
- opens `Daily_Trans_Report`
- passes group id/name
- title = `add_tr_account`.

### add amount/currency
For root page:
- opens `Daily_Curr_Report`.

For non-root page:
- uses alternate footer icon/state.

### previous/next
The main Activity handles `move_left` and `move_right`; the Fragment only exposes the controls and hides both if total page count <= 1.

## 7. Transaction-type taxonomy

Resource `titles_bills`:

1 بيع
2 شراء
3 تحويل مخزني
4 تسوية مخزنية
5 جرد مخزني
6 عرض سعر
7 طلب شراء
8 توريد مخزني
9 صرف مخزني

Additional source branches explicitly use:
- 11 adjustment
- 21 adjustment
- other internal transaction values.

These extra IDs are source-level values and should not be collapsed into the nine visible labels.

## 8. Settings — exact preference architecture

Settings are AndroidX Preference screens.

### General
Keys:
- prefUsername
- prefAddress
- prefPhone
- prefLogo
- prefSign
- prefUsername_en
- prefAddress_en
- prefTax_no
- prefApproval

### Printing/data sync
Keys include:
- prefPrintUser
- pref_show_qr_code
- prefBlankLines
- prefPrintSort
- prefPrintDate
- pref_print_all_curr
- pref_debit
- pref_credit
- pref_print_remarks
- pref_bill_remarks
- pref_print_balance
- pref_num_words
- pref_hide_doc_col
- prefOthers_footer2_total
- prefOthers_print_end_date
- prefOthers_print_bill_pages
- pref_print_bill_sign

### Security
- prefLogin
- prefPassword

### Other
- prefOthers_sales_out_qty
- prefOthers_trans_diff_dates
- pref_cash_id
- pref_cost_avg
- prefOthers_closed_year
- prefOthers_whats_bus
- prefOthers_activation
- prefOthers_barcode
- pref_barcode_type
- prefOthers_item_price
- prefOthers_item_qty
- prefOthers_update_item_price
- prefOthers_purchases_tax
- prefOthers_sales_tax
- prefOthers_tr_time
- prefOthers_item_end_date

### Backup
- prefAutoBackup = true
- prefBackup_path
- prefBackup_pic_path
- prefBackup_time_h
- PREF_ACCOUNT_NAME
- google_drive_folder (disabled in XML)
- prefAutoNotify = true

### Thermal
- prefPaperSize
- prefThermalBluetooth = true
- prefThermalType
- pref_show_qr_code
- pref_thermal_remarks
- prefFontSize
- prefNoPrinted
- pref_thermal_balance
- pref_codetable
- pref_thermal_qty

### Barcode
- name_status
- item_price_status
- item_unit_status
- item_barcode_curr_id
- item_barcode_col_cnt2
- item_barcode_img_h2
- item_barcode_img_w2
- item_barcode_img_cnt2
- item_barcode_img_m2
- hide_barcode2
- item_barcode_col_cnt
- item_barcode_img_cnt

### Currency/group
- pref_curr_list → old.CurrList_edit
- pref_group_curr_list → old.GroupList_edit

### SMS/WhatsApp
- prefSMS_header
- prefSMS_footer
- prefSMS_debit
- prefSMS_credit
- prefAutoNotify_sms_header
- prefOthers_notify_image
- prefOthers_notify_bill_items
- prefAutoSMS_date
- pref_notify_bill_type
- prefAutoSMS_wa
- prefAutoSMS

## 9. Explicit defaults verified from XML

Notable defaults:
- login = false
- automatic backup = true
- automatic notification = true
- thermal Bluetooth = true
- print sort = true
- print date = true
- hide document column = true
- update item price = true
- transaction time = true
- most optional tax/barcode/WhatsApp features = false.

Defaults are source facts, not recommendations.

## 10. Remaining implementation blockers

Before any UI/code implementation, still require:

1. Exact `titles4`, `titles4_`, `titles3_main`, `titles3_`, `icons*` resource contents and caller context.
2. Full `SC` adapter + action-table query method `r()`.
3. Full SQL schema creation/migration and views.
4. Full Bill_edit validation and save method, including every transaction posting side effect.
5. Bill_inv and Bill_move save/validation side effects.
6. Customer transaction save/delete/update side effects.
7. Account-tree creation/deletion rules.
8. Every report's SQL query and result columns.
9. Print/PDF/export workflows.
10. Activation/online/multi-user gating.
11. Remaining activities/layouts/dialogs.
12. A final source-to-screen evidence matrix.

No implementation is approved until these blockers are closed.
