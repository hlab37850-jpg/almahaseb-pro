# Verified Platform, Security, Settings and Customer-Transaction Evidence

Status: ANALYSIS ONLY.

## 1. APK identity

Manifest evidence:

- package: `info.aalmoghalis.inventory`
- versionCode: 170
- versionName: `Inv1.170`
- minSdkVersion: 21
- targetSdkVersion: 35
- compileSdkVersion: 35
- launcher Activity: `FragmentStatePagerSupport_Main`
- application class: `info.aalmoghalis.inventory.lang.App`
- application theme: `@style/AppTheme`
- RTL manifest flag: `supportsRtl="false"`
- cleartext traffic: enabled
- network security config: `@xml/network_security_config`
- backup agent: `MyBackupAgent`
- allowBackup: true
- restoreAnyVersion: true

These values are observations from the recovered manifest, not reconstruction recommendations.

## 2. Declared platform capabilities

The manifest declares permissions/features for:

- Internet/network state/Wi-Fi
- notifications
- legacy external storage read/write
- phone state
- wake lock
- boot receiver
- contacts read/write
- selected visual media
- Bluetooth classic and modern scan/connect/advertise
- fingerprint
- synchronization settings/statistics
- authenticator account
- Google Play billing
- Firebase/FCM receive
- foreground service
- camera.

Camera hardware is explicitly optional.

The APK therefore has substantial peripheral/integration capability beyond accounting UI.

## 3. Customer transaction screen family

The recovered layouts prove multiple distinct variants.

### `content_customer_det_edit.xml`

Verified controls:

- cash account
- current currency price area
- calculator
- amount
- customer AutoCompleteTextView
- discount area
- date
- two-way in/out RadioGroup
- edit/save action
- attachment
- remarks
- second date/remarks variant
- currency RadioGroup
- two additional add actions
- transaction ListView
- empty state.

### `content_customer_det_edit2.xml`

Same structural family but uses a fund-account label rather than the cash-account label.

### `content_customer_det_edit5.xml`

Adds explicit:
- currency
- cash/account status switch
- document number
- incoming-document label.

### `content_customer_det_move3.xml` / `_rv`

These are list-only transaction movement variants with an empty state.

### Transaction row layouts

`customer_det_row_all.xml`:
- hidden transaction ID
- remarks
- amount
- customer name
- date
- image/type control.

`card_customer_det_row3.xml`:
- CardView
- balance
- transaction image/type
- transaction ID
- remarks
- amount
- date
- time.

`customer_det_view.xml`:
- amount
- transaction type
- currency
- date
- time
- remarks
- optional attachment
- optional weight.

## 4. Customer edit evidence

The Activity source explicitly constructs a customer-edit dialog using `dialog_customer4.xml`.

Observed fields:

- customer name
- phone
- address
- call action
- classification/group action
- customer type action
- customer group AutoCompleteTextView
- account type AutoCompleteTextView.

The source explicitly performs SQL updates/inserts against `customers`, including:
- name
- gsm
- address
- account parent
- group.

Therefore the customer form is a database-backed master record, not just a display screen.

## 5. Account hierarchy evidence

`dialog_customer5.xml` explicitly contains:

- account name
- account type SwitchCompat
- account parent AutoCompleteTextView
- account-type image/action.

The Activities `Account_Tree_Main` and `Account_Tree_Det` use these controls.

This establishes a parent/account-type hierarchy in the original data model.

## 6. Customer transaction persistence evidence

Customer transaction Activities explicitly generate SQL involving `transactions`.

Observed fields include:

- cus_id
- out
- in
- date_
- remarks
- now_
- param1/param2
- fund_id
- curr_id
- bill_id
- user_id.

The recovered source also loads transaction rows containing:
- amount
- direction
- customer
- bill
- currency
- fund
- other account.

## 7. Report input components

The original app has reusable report-input layouts rather than one universal filter.

### content_report_input.xml

Verified:
- show-all checkbox
- switch/status
- from date
- to date
- customer/supplier selector
- add selector action
- name header.

### content_report_input2.xml

Verified:
- payment type Spinner
- from/to dates
- customer/supplier selector.

### content_report_input3.xml

Verified:
- from date
- to date.

### content_report_input4.xml

Verified:
- from date
- to date
- currency selector.

### content_report_input8.xml

Verified:
- from date
- to date
- radio group for report type
- quantity/weight options.

## 8. Report screen layout families

Verified Activity → layout mapping:

- Report1_item_balance → `data_listview`
- Report2_supp_balance → `data_listview_with_inp9`
- Report3_expense_balance → `data_listview`
- Report4_purchases → `data_listview_with_inp`
- Report6_expenses → `data_listview_with_inp2`
- Report7_profit_loss → `data_listview_with_inp6`
- Report8_item_movement → `data_listview_with_inp8`
- Report8_item_movement_daily → `data_listview4`
- Report9_expenses_balance_daily → `data_listview_with_inp12`
- Report9_expenses_balance_daily_det → `data_listview_rep4`

Several layouts contain explicit empty states and/or balance/total fields.

## 9. Report behavior confirmed from source

Report4_purchases:
- has customer/supplier AutoCompleteTextView.
- has show-all/status controls.
- has date range.
- accepts `TR_TYPE`.
- Report menu sends `TR_TYPE=2` for purchases and `TR_TYPE=1` for sales.

Report6_expenses:
- has customer/account AutoCompleteTextView.
- has payment-type Spinner.
- has date range.
- accepts `TR_TYPE=3`.

Report8_item_movement:
- has date range.
- has a report-type RadioGroup.
- result list is data-driven.

Report9_expenses_balance_daily:
- has date range.
- list plus explicit balance field.
- clicking a result opens `Customer_Det_List_edit`.

## 10. Customer transaction direction

The layouts contain RadioButtons whose string resources are `radio1` and `radio2`, and the source persists `out`/`in` fields.

The exact human-readable labels and every business rule associated with each direction must be taken from the corresponding string resources/source branches; they are not being invented here.

## 11. Visual-language evidence

Across these screens the APK repeatedly uses:

- LinearLayout
- TableLayout/TableRow
- ListView
- AutoCompleteTextView
- EditText
- TextView
- ImageView
- RadioGroup/RadioButton
- SwitchCompat
- CardView only in selected transaction-row variants.

The dominant visual language is compact table/list forms with background drawables and separators, not a uniform card-based redesign.

## 12. Important security/compatibility observation

The original APK declares `supportsRtl=false` despite visibly using Arabic layouts/text and right gravity extensively. Therefore RTL behavior must be reproduced from actual layout gravity/alignment and not assumed from the manifest flag.

## 13. Remaining evidence required before implementation

Still not declared final:

- exact C3981tg click mapping.
- full secondary action behavior.
- complete ExpandableListView group/child report data.
- complete Bill_edit save-validation state machine.
- complete Bill_inv/Bill_move state machines.
- exact SQL schema creation/migration source.
- all report query formulas.
- all account/customer permissions.
- activation and subscription gates.
- printer/export/share flows.
- remaining screens and dialogs.

No code is approved until these are traced.
