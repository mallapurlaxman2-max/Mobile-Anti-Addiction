# Focus Guard (Android starter project)

This is a beginner-friendly Android Studio starter project demonstrating:
- A focus timer (1–1440 minutes)
- A comma-separated allowlist of package names
- An AccessibilityService that detects foreground app changes
- A blocking screen and a best-effort 60-second alert tone after a blocked app is detected

## Important limitations
This is a prototype, not a tamper-proof digital wellbeing product. Android does not allow an ordinary app to guarantee that the owner cannot bypass it. The user can disable the Accessibility service, force-stop/uninstall the app, change system settings, lower/mute audio, or use some system surfaces. The alert tone cannot be guaranteed to play at full volume on every phone and must not be described as unstoppable. Do not use this prototype to restrict someone else's phone without their informed consent.

The app currently uses package names rather than a visual installed-app picker. Different phone brands use different Gallery, Phone, launcher and dialer package names. Replace the defaults with the packages on your device. WhatsApp is usually `com.whatsapp`; Google Photos is `com.google.android.apps.photos`; phone/dialer packages vary.

## Build on your laptop
1. Install Android Studio from https://developer.android.com/studio
2. Extract this ZIP.
3. In Android Studio choose **Open** and select the extracted `FocusGuardAndroid` folder.
4. Allow Gradle sync and install any requested Android SDK 35 components.
5. On your Android phone, enable Developer options (tap Build number seven times in About phone), then enable USB debugging.
6. Connect the phone by USB and accept the debugging prompt.
7. In Android Studio select your phone as the target and press **Run**.
8. Open Focus Guard, tap **Enable Accessibility Service**, find Focus Guard in Accessibility settings, and enable it.
9. Return to Focus Guard, set the allowed package names and duration, then tap **START FOCUS SESSION**.

## Find package names
This prototype does not include an installed-app picker. The example list is only a starting point. Phone/Gallery packages vary by manufacturer. For an app you want to allow, you can find its package name using ADB:
`adb shell pm list packages`
Then look for likely package names. Do not remove emergency/essential apps from the allowlist without understanding the consequences.

## Expected behavior
When a non-allowed app is detected during an active session, the service attempts to return to Home, opens a restriction screen, and plays a short alert pattern for up to one minute. Android/OEM behavior may limit this. The session timer is stored in app preferences, but this prototype does not yet recover every background-service edge case after reboot.

## Next improvements
- Installed-app picker instead of typing package names
- Reboot recovery and a foreground notification
- Device-owner/kiosk mode for dedicated devices
- Better audio focus handling and explicit user-controlled test sound
- Automated tests across Android versions and manufacturers
