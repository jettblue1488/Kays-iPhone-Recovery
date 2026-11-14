# Kay's iPhone Recovery — Black Widow Edition

Free Android APK for USB diagnostics and guided recovery instructions for iPhone 13 family.

## Build
GitHub Actions > Build Android APK > Run workflow, then download the APK artifact from the successful run. The artifact is a ZIP containing an APK.

## Current scope
- Android USB-host device enumeration and Apple vendor-ID recognition
- Guided recovery-mode instructions and non-erasing Update-first workflow
- Separate factory-restore instructions with erase warning
- Local diagnostic history
- Black-and-red native Android interface

**Not implemented:** direct iOS firmware restore from Android, firmware flashing, remote control of Finder / Apple Devices, automatic one-tap iOS repairs. These require additional verified platform support and cannot be promised for all Trek tablets.

Official iOS recovery uses Finder on Mac or Apple Devices on Windows. Restore erases data; Activation Lock remains in effect.
