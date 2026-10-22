package com.example.focusguard

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class GuardAccessibilityService : AccessibilityService() {
    private var lastBlockedPackage = ""
    private var lastBlockedAt = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName ||
            packageName == "com.android.systemui" ||
            packageName == "com.android.launcher3") return

        val prefs = getSharedPreferences("guard", MODE_PRIVATE)
        val endTime = prefs.getLong("end_time", 0L)
        if (System.currentTimeMillis() >= endTime) return

        val allowed = prefs.getString("allowlist", "")!!
            .split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (allowed.contains(packageName)) return

        // Avoid repeatedly opening the blocker for the same app every event.
        val now = System.currentTimeMillis()
        if (packageName == lastBlockedPackage && now - lastBlockedAt < 1500) return
        lastBlockedPackage = packageName
        lastBlockedAt = now

        try {
            performGlobalAction(GLOBAL_ACTION_HOME)
            val intent = Intent(this, LockActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            SirenPlayer.start(this)
        } catch (_: Exception) { }
    }

    override fun onInterrupt() {}
}
