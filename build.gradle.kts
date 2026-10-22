plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.focusguard"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.focusguard"
        minSdk = 26
        targetSdk = 28
        versionCode = 1
        versionName = "0.1"
    }
}
