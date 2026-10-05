# VERIFIED_PRINT_ZATCA_INTEGRATION.md

Status: ANALYSIS ONLY. No reconstruction code is authorized yet.

## 1. Printing architecture

The APK contains three distinct printing layers:

1. Android PrintService package: `info.aalmoghalis.inventory.printservice`
2. Bluetooth thermal/Zebra package: `info.aalmoghalis.inventory.printBT`
3. PDF preview/export path using an embedded PDF viewer.

### Android PrintService
Verified classes:
- `printservice.MainActivity`
- `printservice.PrintPreview`
- `printservice.SettingsActivity`
- `printservice.ThermalPrintService`

`MainActivity` uses Bluetooth devices and displays paired printer names/addresses. The UI is `activity_main.xml`; it exposes a floating action button and a printer-selection dialog. If no printers are found it explicitly displays `No printers`.

`PrintPreview` extends `net.sf.andpdf.pdfviewer.PdfViewerActivity` and uses the APK resource `pdf_file_password` for the password UI.

`ThermalPrintService` is an Android `PrintService`. A queued print job is started, its PDF stream is copied to an app-private PDF file, and an Intent starts the thermal-print activity with the file path. The discovered printer is named `MiNiPrinter`.

## 2. Bluetooth thermal/Zebra printing

`printBT.MainActivity` proves two printer paths:

- Generic Bluetooth socket/ESC-POS-style byte output.
- Zebra SDK path using `com.zebra.sdk.comm.BluetoothConnection` and `ZebraPrinter`.

The class:
- requests BLUETOOTH_CONNECT and BLUETOOTH_SCAN on Android 12+ when needed;
- enumerates bonded devices;
- can launch `DeviceListActivity`;
- can request Bluetooth adapter enablement;
- stores/uses a selected device address;
- supports a configurable thermal printer type through `prefThermalType`;
- reads `prefNoPrinted`;
- generates/uses `print.jpg` and `print2.jpg`;
- has explicit thermal image-print routines;
- has a Zebra image-print path.

The original app therefore does not simply print a Flutter/Android screen. It first builds printable content/images and sends them through printer-specific paths.

## 3. Bill PDF/share path

`Bill_edit`, `Bill_inv`, and `Bill_move` all expose menu actions for printing and sharing on persisted documents. Their print/share actions are hidden when the transaction ID is zero (new unsaved document).

`Bill_edit` calls PDF/content helpers in `Z00` and creates a `.pdf` path. It also exposes share menus backed by string arrays `titles_share_bill2` and `titles_share_tr`.

The exact share menu labels and each branch destination must be preserved from the corresponding string arrays/source; no label normalization is permitted.

The bill flow also supports:
- standard print;
- alternate printing path;
- PDF preview;
- Android share/chooser;
- thermal printing;
- optional balance/footer/remarks/date fields controlled by preferences.

## 4. Printing preferences

Verified preference keys affecting output include:

- `prefPrintUser`
- `pref_show_qr_code`
- `prefBlankLines`
- `prefPrintSort`
- `prefPrintDate`
- `pref_print_all_curr`
- `pref_debit`
- `pref_credit`
- `pref_print_remarks`
- `pref_bill_remarks`
- `pref_print_balance`
- `pref_num_words`
- `pref_hide_doc_col`
- `prefOthers_footer2_total`
- `prefOthers_print_end_date`
- `prefOthers_print_bill_pages`
- `pref_print_bill_sign`
- thermal preferences including `prefPaperSize`, `prefThermalBluetooth`, `prefThermalType`, `pref_thermal_remarks`, `prefFontSize`, `prefNoPrinted`, `pref_thermal_balance`, `pref_codetable`, `pref_thermal_qty`.

## 5. ZATCA module

The APK has a dedicated ZATCA integration package:
- `ZatcaSettingsActivity`
- helper classes `a,b,c,d,e` under `zatca`
- dedicated `activity_zatca_settings.xml`.

The settings screen is a ScrollView with white elevated cards on `#f5f7fa`, 16dp outer/content padding.

### Branch/environment
Verified controls:
- branch Spinner
- environment Spinner
- device email display.

### Existing device card
Initially hidden (`gone`). When a registered device exists it exposes:
- device name
- VAT number
- ICV
- auto-send checkbox
- renew OTP button
- stop OTP button.

### Registration form
Initially hidden. Exact fields:
- VAT number, numeric, max 15
- commercial registration number, numeric, max 10
- common name
- organization name
- branch name
- country code
- national address:
  - building number
  - street name
  - district
  - postal code
  - city
  - business category
- OTP, numeric.

Save action is initially hidden with the registration section and becomes part of the registration workflow.

### Verified ZATCA state transitions
The Activity has explicit success/error callbacks for:
- device registration;
- OTP renewal;
- OTP stopping;
- auto-send save;
- branch loading;
- device-state loading.

Observed success messages include:
- `Device registered successfully`
- `OTP renewed successfully`
- `OTP stopped successfully`
- `Auto Send to Zatca saved`

Validation is explicit:
- required fields;
- exact digit length;
- digits-only validation.

The Activity has a change-email route using `change_email_flag=1`.

## 6. ZATCA database support

The mature migration contains ZATCA-related additions to company/branch/unit/status and bill metadata. The mature bill schema includes QR/hash/UUID/signing-related fields in later migrations.

Therefore ZATCA is an integration subsystem, not a cosmetic QR switch. Exact API/request signing behavior remains a separate source-trace task before implementation.

## 7. Reconstruction rule

The target implementation must not claim ZATCA, Bluetooth printing, PDF, sharing, or Android PrintService support merely because settings exist. Each capability must be implemented only after its source path and data contract are traced.

Current evidence status:
- printing architecture: VERIFIED
- thermal/Zebra paths: VERIFIED
- PDF preview path: VERIFIED
- bill print/share entry points: VERIFIED
- ZATCA settings UI/state callbacks: VERIFIED
- exact ZATCA API payload/signing protocol: STILL REQUIRES SOURCE TRACE
- exact every-share-option label/destination: STILL REQUIRES SOURCE TRACE
