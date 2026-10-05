# Verified Transaction, Billing and Settings Analysis

Status: ANALYSIS ONLY. No application implementation has been added.

## A. Transaction/bill architecture

The original application separates three major document families:

### A1. Commercial bills — Bill_edit

Activity:
`info.aalmoghalis.inventory.activity.Bill_edit`

New document layout:
`content_bill_edit.xml`

Read-only/edit layout:
`content_bill_det.xml`

Both have the same overall vertical composition:

1. bill header
2. item-add header
3. item table header
4. weighted ListView of line items
5. bill summary/footer

The new/edit screen uses:
- `content_bill_edit_p1`
- `content_bill_item_add_hdr`
- `bill_item_det_row_hdr`
- `content_bill_summary`

### A2. Inventory documents — Bill_inv

Activity:
`Bill_inv`

New layout:
`content_bill_inv`

Detail/edit layout:
`content_inv_det`

The inventory document differs from a commercial bill at the header:
- branch/date fields are used.
- item list header uses `move_item_det_row_hdr`.
- totals show quantity/total fields.

Database evidence from the Activity is explicit:
- table `bills2`
- table `bill_transactions2`
- temporary editing table `items_temp`.

### A3. Inventory transfer — Bill_move

Activity:
`Bill_move`

Layout:
`content_bill_move`

Detail layout:
`content_move_det`

Header fields are explicitly:
- date
- from branch
- to branch
- remarks
- bill number

The line-item and total structure is otherwise similar to inventory documents.

## B. Commercial bill header — exact recovered fields

`content_bill_edit_p1.xml` contains:

- `include_bill_status`
- `chk_back`: bill-back checkbox, default unchecked.
- `tr_branch`: branch selector.
- `item_status_hdr`: cash/post label, default `bill_cash`.
- `switch_compat`: cash/post switch, default unchecked.
- `img_u_currency`: currency icon, 28dp.
- `tv_curr`: currency selector, with list icon.
- `tv_cash`: cash-account selector, with list icon.
- `tr_date`: date.
- `tr_name`: customer/party AutoCompleteTextView.
- `btn_attach`: camera/attachment control, initially gone.
- `tr_remaks`: remarks EditText.
- `tr_bill_no`: numeric bill number EditText.
- `tr_price_diff`: hidden decimal price-difference field.

The screen uses `table_row_bg` as its header background.

## C. Commercial bill item entry

`content_bill_item_add_hdr.xml`:

- add-item ImageView `tr_item_add`
- item-name AutoCompleteTextView
- hidden barcode ImageView `tr_item_barcode`

The expanded item editor `content_bill_item_add_view.xml` has verified fields:

- item name
- optional barcode
- quantity
- price
- total
- remarks
- optional unit
- optional date

The numeric fields use decimal input. The quantity and price fields have explicit keyboard focus order.

The item editor contains an add/hide interaction rather than a permanently expanded modern form.

## D. Bill list columns

`bill_item_det_row_hdr.xml` confirms these displayed columns:

- weight
- price
- quantity
- item

There is also a hidden 1dp ID field.

The header is white, 8dp padded, with a one-pixel darker-gray separator.

## E. Bill totals — verified capabilities

The recovered summary layouts explicitly support:

- total with discount
- discount as amount or percentage
- tax amount
- tax value
- other cost
- cost account
- paid amount
- remaining amount

`content_bill_total2.xml`:
- RadioButton `نسبة`
- RadioButton `مبلغ`
- amount input
- default selection = `مبلغ`

`content_bill_total3.xml`:
- cost account AutoCompleteTextView
- other-cost decimal input

`content_bill_total_all.xml` combines:
- discount
- tax
- other cost
- paid amount

Paid amount input is represented by a decimal EditText.

## F. Bill transaction persistence — direct SQL evidence

Bill_edit contains direct SQL statements proving these persistent structures:

### Header table

`bills`

Fields observed in INSERT/UPDATE statements include:

- id
- tr_type
- date_
- amount
- d_amount
- cus_id
- tran_status
- bill_type
- bill_no2
- remarks
- curr_id
- br_id
- cash_id
- is_back
- bill_no
- param1
- online
- t_val
- tax_amount
- time_
- paid_amount
- user_id
- id2

### Line table

`bill_transactions`

Observed fields include:

- bill_id
- item_id
- item_type_id
- qty
- qty_t
- cost_price OR sls_u_price depending on transaction
- curr_id
- d_amount
- remark
- unit_id
- u_val
- base_unit
- qty_pr
- e_date

### Temporary line-edit table

`items_temp`

Observed fields include:

- item_id
- item_type_id
- qty
- qty_t
- price
- curr_id
- remark
- unit_id
- u_val
- base_unit
- qty_pr
- e_date

The Activity deletes/repopulates `items_temp` while opening/editing bills. This is not an assumption; it is explicit source behavior.

## G. Bill types

Resource array `titles_bills` is exact:

1. بيع
2. شراء
3. تحويل مخزني
4. تسوية مخزنية
5. جرد مخزني
6. عرض سعر
7. طلب شراء
8. توريد مخزني
9. صرف مخزني

The Activity indexes this array from `TR_TYPE`, proving that transaction type is a central document discriminator.

## H. Inventory persistence

Bill_inv explicitly uses:

- `bills2`
- `bill_transactions2`
- `items_temp`

For inventory editing, the Activity copies existing rows from `bill_transactions2` into `items_temp`, edits the temporary set, then deletes/reinserts the bill line rows.

The inventory quantity calculation code also explicitly derives quantities from `items_cost_calc` according to transaction type, back-state and adjustment type. Therefore stock balance is transaction-derived rather than a static item quantity field.

## I. Customer transaction editing

The Customer_Det_List_edit family is not one screen:

- `Customer_Det_List_edit`
- `Customer_Det_List_edit2`
- `Customer_Det_List_edit3`
- `Customer_Det_List_edit4`
- `Customer_Det_List_edit5`
- `Customer_Det_List_edit6`

They use separate XML families for transaction/detail variants.

Observed customer transaction data includes:
- transaction ID
- date
- amount
- remarks
- in/out direction
- customer name
- bill ID
- currency
- parameter
- fund name
- other account

The family also contains:
- calculator activity
- phone/contact picker
- camera
- file/image selection
- customer edit dialogs
- currency selection
- transaction type handling
- contextual multi-select operations.

Customer records can be created/updated with:
- name
- GSM/phone
- address
- account parent
- group.

## J. Settings architecture

Settings are Android Preference-based, not a custom dashboard.

Root:
`activity_settings.xml`

Header source:
`pref_headers.xml`

Verified settings sections:

1. General
2. Data synchronization / printing
3. Security
4. Users
5. Group/currency management
6. Item types
7. Units
8. Backup
9. Thermal printing
10. Tax
11. ZATCA
12. Barcode
13. SMS/WhatsApp
14. Other options
15. Activation
16. Online status

### General settings

`pref_general.xml` explicitly provides:

- user name
- address
- phone
- logo
- signature
- English name
- English address
- tax number
- approval/signature change

### Security

`pref_security.xml`:
- login enable switch, default false
- password preference

### Backup

`pref_backup.xml`:
- automatic backup, default true
- backup path
- picture backup path
- backup time
- account/email setting
- Google Drive folder preference (disabled in XML)
- automatic notification, default true

### Thermal printing

`pref_thermal.xml` explicitly supports:
- paper size
- Bluetooth
- printer type
- QR code
- thermal remarks
- font size
- number of printed copies
- thermal balance
- code page
- thermal quantity.

### Barcode

`pref_barcode.xml` supports:
- print user name
- print price
- print base unit
- barcode currency
- thermal barcode dimensions/count/margins
- hide barcode
- thermal printer type
- A4 barcode rows/count.

## K. No assumptions rule

The following are still deliberately not declared as final implementation behavior:

- exact accounting posting formulas for every transaction type
- exact tax/discount calculation order in all cases
- permissions matrix
- every menu item's click destination
- complete database creation/migration source
- all report filter combinations
- all print formats
- all dialogs and validation messages.

Those require direct tracing of the corresponding methods/resources before implementation.

## L. Implementation gate

The reconstruction must not begin from these observations alone.

Next required evidence pass:

1. C3981tg and its exact click route.
2. ExpandableListView adapter and report groups.
3. every `content_bill_*` include and its click listener.
4. Bill_edit save/validation path from `checkValidation_save` through final SQL.
5. Bill_inv/Bill_move save paths.
6. Customer_Det_List_edit family screen-by-screen.
7. all report Activity/layout pairs.
8. all database-related SQL query methods and resource strings.
9. permissions/activation/online behavior.
10. final evidence matrix with source path + exact behavior before coding.
