# Almahaseb Pro — Verified Screen & Navigation Map

Status: ANALYSIS ONLY. No application code or UI has been added.

This document records findings verified directly against the decompiled APK repository `hlab37850-jpg/almahaseb-pro-apk-analysis`. The reconstruction repository `hlab37850-jpg/almahaseb-pro` is documentation-only at this stage.

## 1. Main architecture — corrected finding

The launcher/main shell is:

`FragmentStatePagerSupport_Main`
→ `activity_main_tmp2.xml`
→ includes `customer_det_pager.xml`
→ includes `fragment_pager.xml`
→ ViewPager id `pager`.

The main Activity creates `JC` with mode `4` and the loaded customer/group list, then assigns it to the ViewPager.

Verified code path:

- `onCreate()` loads `activity_main_tmp2`.
- `JC(getSupportFragmentManager(), this.i, 4, this)` is created.
- `pager.setAdapter(this.c)`.
- `pager.setOffscreenPageLimit(1)`.
- page selection calls `h0(position)`.

The group loader is `C0943Tq`. It reads group records from the database and creates `C0917Sq(id, g_name, curr_name)`. Thus the ViewPager page count is data-driven, not a hardcoded four-screen carousel.

### Important correction to earlier assumptions

`content_customer_main3.xml` is a real layout, but it is NOT sufficient evidence for the actual default main page.

For ViewPager mode `4`, adapter `JC.getItem()` calls `d.w(...)`, where Activity/Fragment `d` receives:

- `cus_id`
- `cus_name`
- position
- page count
- `g_name`
- `curr_name`

Fragment `d` then inflates **`content_customer_main4.xml`**.

Therefore the primary main-page reconstruction must use `content_customer_main4.xml` and Fragment `d` behavior, while `content_customer_main3.xml` remains a separate verified layout used elsewhere.

## 2. Main page — actual ViewPager page

Source:

`sources/info/aalmoghalis/inventory/activity/d.java`

Layout:

`resources/res/layout/content_customer_main4.xml`

### Page structure

- RelativeLayout root.
- CoordinatorLayout.
- Main vertical container.
- Main content area.
- GridView id `gridview`, two columns.
- ExpandableListView id `list_report`.
- Footer area including `footer_bal_m`.

Fragment `d` initializes the page from database-backed group/customer context.

It obtains `titles4` and `icons4` when:

- page/group parameter `p == "0"`, OR
- multi-user condition `C2160f10.h > 0`.

For that primary condition, the verified four main actions are:

1. قبض/صرف — `@drawable/amount_`
2. المبيعات — `@drawable/sales`
3. الحسابات — `@drawable/accounts`
4. المشتريات — `@drawable/purchase`

The alternative restricted array `titles4_` contains:

1. المشتريات
2. المبيعات
3. عملية مخزن
4. المخزون
5. أرباح الأصناف
6. حركة الصنف

with `icons4_`:

1. purchase
2. sales
3. issues
4. stock
5. reports
6. reports

The restricted array is selected when `p != "0"` and the user is not in the special multi-user condition. It is therefore not interchangeable with the primary four-action page.

## 3. Secondary action set in the main-page footer

Fragment `d` also constructs a second action list from `titles3_main` when `p == "0"`:

1. تحويل مخزني
2. شراء
3. بيع
4. أصنـاف
5. الأسعار
6. سداد
7. مخازن
8. إفتتاحي
9. حسابات

Verified corresponding icons:

1. issues2
2. purchases2
3. sales2
4. items2
5. item_price2
6. amount2
7. items2
8. amount2
9. items2

This list is passed to adapter `C3981tg`. It must be analyzed with that adapter and its click handler before being implemented.

## 4. Main footer — verified controls

Layout:

`resources/res/layout/footer_bal_m.xml`

Verified controls:

- `img_help`: help icon, calls `help_btn`.
- `move_left`: previous group/page control.
- `footer_sum`: centered value/status text; initial XML text is `...`.
- `move_right`: next group/page control.
- `img_add_amount`: hidden by default in XML; source `s_currency4`; calls `amount_add_btn`.
- `img_move_tr`: transfer icon; calls `move_tr_btn`.
- `footer_ads`: white area, hidden by default.

The footer background is `table_row_bg_m`, not a generic modern navigation bar.

Fragment `d` additionally hides both left/right movement controls when the total ViewPager group count is <= 1.

## 5. Header / drawer

Layout:

`resources/res/layout/header_logo.xml`

Verified:

- vertical layout.
- background `table_row_bg_m`.
- logo ImageView uses `@drawable/logo`.
- title TextView id `logo_title`.
- title size 20dp in recovered XML, bold, white, centered.
- title defaults to `@string/app_name`.
- user row `ll_logo_user` is hidden by default.
- user text `logo_user` is white and small.

Main drawer layout:

`activity_main_tmp2.xml`

- header: 250dp × 100dp.
- drawer ListView: 250dp wide.
- white background.
- transparent divider.
- divider height 0dp.
- singleChoice mode.

## 6. Drawer menu — verified labels and destinations

Array `titles`:

1. حفـظ نسخة إحتياطية
2. إسترجاع قاعدة البيانات
3. جوجل درايف
4. دليل الحسابات
5. إعـدادات
6. للتــواصـل والـدعـم
7. حــول البـرنـامج
8. خروج

The main Activity contains explicit routing for backup, restore, Google Drive, settings, support/contact, help/about and process exit.

Other main navigation paths verified in the Activity include:

- sales/purchase/expense entry.
- reports.
- transaction reopening.
- Bill_edit for transaction types 1/2.
- Bill_move for transaction type 3.
- help via `assets/help.html`.

## 7. Reports screen — verified

Activity:

`Reports_list`

Layout:

`data_listview3.xml`

The screen is a vertical container with a TableLayout and a ListView `list_items`.

The report adapter uses `C4062ug`, which by default inflates `list_item2.xml`.

### Report labels

Array `titles2`:

1. تقـرير-مخزون الأصناف
2. تقـرير-حسابات الموردين
3. تقـرير-إجمالي المشتريات
4. تقـرير-إجمالي المبيعات
5. تقـرير-الصندوق
6. تقـرير-النفقات والإيرادات

### Exact routing

Index 0 → `Report8_item_movement`

Index 1 → `Report2_supp_balance`

Index 2 → `Report4_purchases` with `TR_TYPE = "2"`

Index 3 → `Report4_purchases` with `TR_TYPE = "1"`

Index 4 → `Report9_expenses_balance_daily`

Index 5 → `Report6_expenses` with `TR_TYPE = "3"`

This is the verified first-level Reports menu. The larger set of report Activities is separate and must be traced individually.

## 8. List-item visual component

`list_item2.xml` is used by the generic `C4062ug` adapter.

Verified visual structure:

- vertical root.
- horizontal centered row.
- 4dp padding.
- title TextView:
  - 17sp
  - bold
  - black
  - centered
  - weight 3.
- icon ImageView:
  - 32dp × 32dp
  - 10dp left/right margins
  - weight 1.
- one-pixel darker-gray separator.

Therefore report/menu rows are not Material Cards.

## 9. Account tree — verified entry points

Activities:

- `Account_Tree_Main`
- `Account_Tree_Det`

Layouts:

- `data_listview_bill6_`
- `data_listview_bill5_2`

Both use ListView-based structures with a header, data list and footer/action area.

The account edit flow uses `dialog_customer5` and contains:

- account name EditText.
- account type SwitchCompat.
- account parent AutoCompleteTextView.
- account/category selector data.
- account-type icon control.

Account tree implementation must therefore preserve a hierarchical/account-parent concept; it is not simply a flat customer list.

## 10. Main customer/group page data model evidence

The group loader creates:

`C0917Sq(id, g_name, curr_name)`

The page factory `d.w()` maps those values into fragment arguments:

- `cus_id` ← group id
- `cus_name` ← group name
- `g_name` ← group name argument
- `curr_name` ← currency name
- position
- page count

This is strong evidence that the main ViewPager is tied to database-defined group/customer contexts and currency context.

## 11. Secondary generic action grid — separate screen

Layout:

`content_customer_main3.xml`

Verified GridView:

- two columns.
- 10dp horizontal spacing.
- 10dp vertical spacing.
- centered.
- separate footer container.

Array `titles3`:

1. بيع
2. شراء
3. تحويل مخزني
4. أصنـاف
5. الأسعار
6. سداد

Icons:

1. sales2
2. purchases2
3. issues2
4. items2
5. item_price2
6. amount2

This screen should not be merged with the ViewPager primary page until its actual callers are fully traced.

## 12. Current reconstruction boundary

Completed analysis evidence is now sufficient to establish:

- application architecture.
- main shell.
- actual ViewPager mechanism.
- database-driven group/page model.
- primary main page layout.
- primary four actions.
- secondary action set.
- footer controls.
- drawer structure.
- reports first-level menu and routing.
- account-tree entry points.
- generic list-row visual language.

Still **not approved for implementation**:

- complete customer/group page click behavior.
- exact `C3981tg` secondary action routing.
- ExpandableListView report section and its child/group data.
- complete Bill_edit/Bill_inv/Bill_move screens and posting logic.
- complete customer transaction editing.
- settings/preferences screen details.
- all report layouts and filters.
- database schema and accounting calculations.
- permissions/activation/multi-user behavior.
- exact typography per screen.
- remaining drawables/backgrounds and dimensions.

## 13. Implementation gate

No application code or UI should be added to `hlab37850-jpg/almahaseb-pro` until the remaining high-risk navigation and transaction screens above have been traced.

Next analysis sequence:

1. `C3981tg` + exact click destinations from Fragment `d`.
2. ExpandableListView adapter/data model used by `list_report`.
3. `Bill_edit`, `Bill_inv`, `Bill_move` with every included XML.
4. `Customer_Det_List_edit*` family.
5. `SettingsActivity` and preference XML/resources.
6. all report layouts and date/filter dialogs.
7. database schema + accounting transaction relationships.
8. final screen/navigation/state matrix.

Only after that matrix is complete should implementation begin.
