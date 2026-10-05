# Almahaseb Pro — Original APK UI Reconstruction Specification

Status: ANALYSIS ONLY — NO APPLICATION CODE OR UI IMPLEMENTATION HAS BEEN ADDED YET.

## Repositories

- APK analysis source: https://github.com/hlab37850-jpg/almahaseb-pro-apk-analysis
- Target reconstruction repository: https://github.com/hlab37850-jpg/almahaseb-pro

The target repository was checked before writing this document and is currently empty (0 KB, default branch main). Therefore there is no existing implementation to overwrite or reinterpret.

## Analysis rules

1. Do not implement a screen until its corresponding original APK evidence has been reviewed.
2. Treat XML resources, strings, dimensions, drawables, Activity/Fragment code and navigation code as the source of truth.
3. Do not invent buttons, tabs, cards, colors, navigation or business flows when the original evidence does not support them.
4. Keep Arabic text and RTL behavior faithful to the original where recoverable.
5. Separate confirmed facts from inferred behavior.
6. Do not populate the reconstruction with fake/demo accounting records.
7. This document is a specification artifact only; implementation starts only after the relevant screen has been completely reviewed.

## Confirmed platform/UI architecture

The APK is a traditional Android View/XML application. The analyzed package is:

info.aalmoghalis.inventory

Evidence includes Android Activities, XML layouts, ListView, GridView, DrawerLayout, Toolbar, AppBarLayout, ViewPager, TableLayout/TableRow, AutoCompleteTextView, EditText, RadioGroup and other classic Android widgets.

No Flutter asset/runtime marker was found in the analyzed APK, and the application package did not contain actual @Composable screen implementations. Compose-related dependency metadata exists in resources but is not evidence that the application's visible screens were implemented with Compose.

## Global visual system recovered from resources

Primary application color:
- colorPrimary = #0a6ea9
- colorPrimaryDark = #1b5088
- gold = #0a6ea9

Additional application colors:
- bk_color = #0a6ea9
- bk_color2 = #a8cef4
- bk_color3 = #168ad0
- colorAccent = #ff4081
- colorAccent2 = #ff9081

Important observation:
The resource name "gold" does NOT mean the rendered color is gold in this APK. Its actual value is #0a6ea9. Any reconstruction must use the actual value, not the resource name.

Global dimensions confirmed:
- activity_horizontal_margin = 10dp
- activity_vertical_margin = 10dp
- action bar sizing follows the Android actionBarSize attribute.
- main drawer width = 250dp
- drawer header height = 100dp
- main GridView horizontal spacing = 10dp
- main GridView vertical spacing = 10dp
- main item editing layout has 5dp outer padding.

Typography/assets:
The APK resources contain Arabic-capable fonts including Noto Naskh Arabic, Noto Naskh Arabic UI, GE SS TEXT LIGHT and GE SS TWO MEDIUM, in addition to common Latin fonts. Exact font selection must be traced screen-by-screen before implementation.

## Screen 01 — Login

Source layout:
resources/res/layout/activity_login.xml

Confirmed structure:
- Vertical root LinearLayout.
- ScrollView containing the login form.
- Centered application icon.
- Icon dimensions: 100dp x 100dp.
- Email field exists in the resource but is gone/hidden.
- Password EditText is centered.
- Password field height: 40dp.
- Password uses numeric/password input behavior.
- Password background references table_row_bg.
- Sign-in button is bold and has a 16dp top margin.
- Fingerprint section/title exists.
- Error message TextViews exist.
- Forgot-password text exists.

Do not implement the login screen as a modern invented Material login page. The recovered structure is compact and classic.

## Screen 02 — Main shell

Primary layout:
resources/res/layout/activity_main_tmp2.xml

Root:
DrawerLayout

Main content:
- FrameLayout
- vertical LinearLayout
- AppBarLayout
- Toolbar
- included customer/detail pager content

Toolbar:
- id toolbar2
- background = @drawable/table_row_bg_m
- height = ?attr/actionBarSize
- centered gravity in the recovered XML.

Drawer:
- start/gravity side drawer.
- width = 250dp.
- header = 250dp x 100dp.
- header background = @color/gold, whose actual color is #0a6ea9.
- header includes header_logo.
- ListView width = 250dp.
- white background.
- transparent divider.
- divider height = 0dp.
- single-choice mode.

## Screen 03 — Main action grid

Primary layout:
resources/res/layout/content_customer_main3.xml

Confirmed:
- Full-screen RelativeLayout.
- Vertical content container.
- Centered GridView.
- GridView uses 2 columns.
- horizontal spacing = 10dp.
- vertical spacing = 10dp.
- stretchMode = columnWidth.
- footer is a separate lower container.
- footer container background = @color/gold (#0a6ea9).
- footer includes footer_bal_m.

Main action array titles3:
1. بيع
2. شراء
3. تحويل مخزني
4. أصنـاف
5. الأسعار
6. سداد

Corresponding icons3:
1. @drawable/sales2
2. @drawable/purchases2
3. @drawable/issues2
4. @drawable/items2
5. @drawable/item_price2
6. @drawable/amount2

This is a confirmed two-column action grid, not a bottom-navigation screen.

## Screen 04 — Alternate main action configuration

Resource array:
titles3_main

Order:
1. تحويل مخزني
2. شراء
3. بيع
4. أصنـاف
5. الأسعار
6. سداد
7. مخازن
8. إفتتاحي
9. حسابات

This array must not automatically be treated as the default visible grid. Its use must be traced in the corresponding Activity/Fragment before implementation.

## Screen 05 — Main drawer menu

Resource array:
titles

Exact recovered labels:
1. حفـظ نسخة إحتياطية
2. إسترجاع قاعدة البيانات
3. جوجل درايف
4. دليل الحسابات
5. إعـدادات
6. للتــواصـل والـدعـم
7. حــول البـرنـامج
8. خروج

Additional navigation evidence from Main.java confirms drawer actions for:
- sales
- purchases
- expenses
- inventory-related transaction
- reports
- backup
- restore
- Google Drive
- settings
- contact/support
- help/about
- exit

The drawer is therefore a functional navigation surface, not a decorative menu.

## Screen 06 — Customer list

Layout:
resources/res/layout/content_customer_main.xml

Confirmed:
- full-height vertical customer list.
- ListView id list_data.
- thin darker-gray separator view, 2dp.
- empty state TextView id emptyResults.
- empty-state text size = 18sp.
- empty-state text color = #536fd2.
- empty-state text references @string/empty_list.

Do not replace this with a card-based customer dashboard without evidence.

## Screen 07 — Customer transaction/detail editing

Layout:
resources/res/layout/content_customer_det_edit_rv.xml

Confirmed top structure:
- RelativeLayout.
- vertical content container.
- small 2dp start/end padding.
- centered cash-account row.
- separator line, 1dp, darker gray.
- transaction entry row with:
  - calculator ImageView
  - amount EditText
  - customer AutoCompleteTextView
- discount section exists but is initially gone.
- date row:
  - clickable date TextView
  - horizontal RadioGroup with two RadioButtons.
- additional edit/save/attachment/remarks sections exist further down and must be analyzed before implementation.
- currency-related controls and other transaction fields are present later in the layout.

Important: this screen is transaction-oriented and must not be simplified to a generic customer form.

## Screen 08 — Item edit

Layout:
resources/res/layout/content_item_edit.xml

Confirmed:
- vertical LinearLayout with 5dp padding.
- TableLayout id bill_hdr.
- TableLayout background = @drawable/table_row_bg.
- 5dp TableLayout padding.
- show-all checkbox.
- SwitchCompat.
- item status label.
- item type area with RadioGroup.
- item name AutoCompleteTextView.
- weight EditText, numeric decimal.
- quantity EditText, numeric decimal.
- Add button id bill_save, width 150dp.
- keyboard focus order is explicitly wired between quantity and weight fields.

This confirms a compact form/table visual language rather than a modern floating-card form.

## Screen 09 — Reports

Confirmed report resource array titles2:
1. تقـرير-مخزون الأصناف
2. تقـرير-حسابات الموردين
3. تقـرير-إجمالي المشتريات
4. تقـرير-إجمالي المبيعات
5. تقـرير-الصندوق
6. تقـرير-النفقات والإيرادات

Recovered Activity classes include multiple additional reports, including:
- Report1_item_balance
- Report2_supp_balance
- Report3_expense_balance
- Report4_purchases
- Report6_expenses
- Report7_profit_loss
- Report8_item_movement
- Report8_item_movement_daily
- Report9_expenses_balance_daily
- Report9_expenses_balance_daily_det
- Account_Balance_Report
- Accounts_Total_Report
- Balance_Sheet_Report
- Trail_Balance_Report
- Money_Balance_Report
- Revaluation_Currency_Report
- Daily_Doc_Report
- Daily_Curr_Report
- Revenue_customer_Report
- Revenue_item_Report
- Revenue_item_Report_Det
- Bills_customer_Report
- Bills_customer_Report_bk

Every report screen must be traced to its XML layout and Activity behavior before implementation.

## Screen 10 — Item/transaction workflow

Recovered Activities establish separate screens for:
- Bill_inv / Bill_edit
- Bill_inv_check
- Bill_adj / Bill_adj2
- Bill_move
- Bills2
- Orders / Order_edit
- Expenses
- Offers
- item editing and item movement
- customer transaction editing.

The original application is therefore a large multi-screen accounting/inventory application, not merely a six-button dashboard.

## Navigation evidence

Main.java contains explicit Intent routing.

Confirmed examples:
- transaction type 1/2 -> Bill_edit or Bills2 depending on entry point.
- expense route -> Expenses.
- report route -> Reports_list.
- backup -> database export.
- restore -> database file selector.
- Google Drive -> Google_drive_list.
- settings -> SettingsActivity.
- help -> WebView loading assets/help.html.
- transaction records can reopen Bill_edit or Bill_move based on stored transaction type.

Menu_AppCompatActivity also provides:
- Add action dialog based on titles3/icons3.
- Bills and purchases list routes.
- Expenses route.
- multiple report routes.
- back navigation to FragmentStatePagerSupport_Main.

## Required reconstruction methodology

For each screen, the implementation team must complete this evidence chain first:

1. Activity/Fragment source.
2. Exact layout XML.
3. Included layouts.
4. String resources used by the layout.
5. Color resources.
6. Dimension resources.
7. Drawable/background resources.
8. Array/icon resources.
9. Click listeners and Intent destinations.
10. Visibility/default-state behavior.
11. Empty/error states.
12. Only then implement the screen.

## Current implementation gate

NO CODE has been added to the target repository yet.

Before implementation of any screen:
- finish its evidence extraction;
- document confirmed geometry and navigation;
- document unresolved points;
- then implement only that screen;
- validate against the recovered resource structure;
- proceed to the next screen.

## Next analysis targets

Priority order:
1. FragmentStatePagerSupport_Main + customer_det_pager.
2. The actual pager page layouts and their adapter/page titles.
3. Grid item layout used by titles3/icons3.
4. footer_bal_m exact structure and behavior.
5. header_logo exact structure.
6. Reports_list + report item layout.
7. Account_Tree_Main / Account_Tree_Det.
8. Customer_Det_List_edit family.
9. Bill_edit / Bill_inv / Bill_move.
10. SettingsActivity and settings layout.
11. All remaining report layouts.
12. Assets/fonts/drawables used by visible UI.
13. Only after the complete screen map is stable: implementation in almahaseb-pro.

## Source integrity note

This specification is based on the decompiled APK analysis repository and is intended as a reconstruction reference. Any implementation should be used only where the user has the necessary rights/authorization to reproduce the application's design and behavior.
