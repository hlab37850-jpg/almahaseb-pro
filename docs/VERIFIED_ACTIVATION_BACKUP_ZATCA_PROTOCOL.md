# VERIFIED_ACTIVATION_BACKUP_ZATCA_PROTOCOL.md

Status: ANALYSIS ONLY.

## Activation / licensing

`activationActivity` is a real Activity, not a static settings page.

Verified behavior:
- inflates generated binding `C1193a2`;
- enables the ActionBar back/up button;
- creates `Z00` database helper;
- if preference `black_list` is true, clears `black_date` and `black_date_done` then invokes `F0()`;
- invokes `C3966tX.e(this)`;
- invokes `Z00.qe(this,this)`;
- sets static activation flag `f=true`;
- root touch on ACTION_DOWN finishes the Activity;
- menu hides the activation item itself.

The source does not justify treating activation as a local boolean. It delegates the substantive activation/licensing operation to `Z00.qe` and contains blacklist state.

## Automatic database backup

`DbBackupListener_Service` is a JobService.

Verified decision chain:
1. Reads `prefAutoBackup`; if disabled, backup work is skipped.
2. Reads `db_data_changed2`; if no data change is recorded, backup work is skipped.
3. Checks `C2160f10.i()`.
4. Builds a date-based database filename.
5. Invokes database backup/copy logic against `Z00.L`.
6. Resets `db_data_changed`.
7. Stores `last_backup_time`.
8. Resets `bk_drive_flag`.
9. Stores `auto_filename` and `auto_drive_date_request`.
10. If the Drive flag/state requires it, schedules the Google Drive-related path.
11. Emits a notification containing the backup path.

This proves automatic backup is conditional on both a preference and database-change state; it is not simply a periodic blind copy.

## Reminder service

`ReminderListener_Service` is another JobService.

Verified:
- feature is gated by `prefOthers_reminders`;
- change-state `reminder_data_changed` is checked;
- reminder records are loaded through `Z00.N4()`;
- notification opens `old.Reminders`;
- notification channel is created and includes reminder count.

## ZATCA HTTP protocol — source-level evidence

The ZATCA HTTP helper uses:

Base URL:
`https://bkp2.dyndns.org:5002`

Transport:
- `HttpURLConnection`
- POST
- connect timeout 15 seconds
- read timeout 25 seconds
- Content-Type: JSON
- Accept: application/json
- Bearer Authorization when a token exists.

Verified endpoint:
- `/api/auth/login`
- `/api/invoices/create-and-submit`

A 401 response can cause a re-login attempt; invalid login is surfaced explicitly.

### Login/registration payload evidence

The source constructs JSON with these fields for the authentication path:
- `taxerId`
- `egsId`
- `certId`
- `otp`
- `vat_number`
- `deviceId`
- `email`

Registration/device data construction includes:
- `vatNumber`
- `crNumber`
- `commonName`
- `companyName`
- `unitName`
- `egsSerialNumber`
- `countryCode`
- `registeredAddress`
- `industry`
- `businessCategory`
- `city`
- `addressLine1`
- `addressLine2`
- `postalCode`
- `location`
- `environment_Type`
- `otp`
- `device_id`
- `email`

### Invoice submission payload evidence

The invoice JSON model contains:
- taxpayerId
- egsUnitId
- invoiceNo
- uuid
- invoiceType
- invoiceDateTime
- currencyCode
- taxCurrencyCode
- sellerName
- vatNumber
- seller street/building/district/city/postal code
- buyer name/VAT/address fields
- buyer country code
- supplyDate
- lineExtensionAmount
- taxExclusiveAmount
- taxInclusiveAmount
- payableAmount
- vatAmount
- AllowanceAmount
- qrPlaceholder
- icv
- pihBase64
- is_back
- reasonText
- originalInvoiceId
- currencyRate
- lines[].

Each line contains:
- lineId
- itemName
- quantity
- unitCode
- unitPrice
- discountAmount
- taxPercent
- taxCategoryId
- lineExtensionAmount
- taxAmount
- lineTotalWithVat.

### Response model

The source parses:
- invoiceId
- invoiceNo
- uuid
- zatcaStatus
- validationStatus
- httpStatusCode
- icv
- pih
- qr
- signedXml
- invoiceHash
- clearedInvoice
- errors
- warnings.

Device response parsing includes:
- taxpayerId/taxerId
- egsUnitId
- certificateId
- unitName
- errorMessage/message/title.

## Important implementation boundary

The source confirms a real remote ZATCA integration and real licensing/activation state. It does NOT justify replacing these with fake local success states.

Before implementation, remaining work is to trace:
- all callers that construct the ZATCA model objects;
- exact device registration endpoint(s) beyond the two explicitly recovered endpoint strings;
- exact persistence of token/device/certificate/ICV/PIH/QR/hash data;
- exact activation network calls inside `Z00.qe`;
- all backup/Google Drive helper methods and user-visible backup/restore flows;
- all remaining print/share menu branches.

No application code is added by this document.
