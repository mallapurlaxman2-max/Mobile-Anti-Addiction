package com.example.focusguard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {
    private lateinit var duration: EditText
    private lateinit var allowlist: EditText
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("guard", MODE_PRIVATE)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 32, 28, 28)
            setBackgroundColor(0xFF070B12.toInt())
        }
        fun label(text: String, size: Float = 16f) = TextView(this).apply {
            this.text = text; textSize = size; setTextColor(0xFFF1F6FF.toInt())
            setPadding(0, 10, 0, 8)
        }
        root.addView(label("FOCUS GUARD", 26f))
        root.addView(label("Restrict distracting apps for a chosen time."))
        status = label(if (prefs.getLong("end_time", 0L) > System.currentTimeMillis()) {
            "A focus session is currently active."
        } else "No active focus session.")
        root.addView(status)

        root.addView(label("Duration (minutes)"))
        duration = EditText(this).apply {
            setText("60"); inputType = 2; setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFF9AA8B8.toInt()); hint = "e.g. 30, 60, 180"
        }
        root.addView(duration, ViewGroup.LayoutParams(-1, -2))

        root.addView(label("Allowed app package names (comma-separated)"))
        root.addView(label("Example: com.whatsapp, com.android.dialer, com.google.android.apps.photos", 12f))
        allowlist = EditText(this).apply {
            setText(prefs.getString("allowlist", "com.whatsapp,com.android.dialer,com.google.android.apps.photos,com.google.android.dialer"))
            setTextColor(0xFFFFFFFF.toInt()); setHintTextColor(0xFF9AA8B8.toInt())
            minLines = 3; gravity = Gravity.TOP
        }
        root.addView(allowlist, ViewGroup.LayoutParams(-1, -2))

        val access = Button(this).apply {
            text = "1. Enable Accessibility Service"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        }
        root.addView(access)
        root.addView(label("Turn on Focus Guard in Android Accessibility settings. This permission is required to detect app changes." , 12f))

        val start = Button(this).apply {
            text = "2. START FOCUS SESSION"
            setOnClickListener {
                val mins = duration.text.toString().toLongOrNull()
                if (mins == null || mins !in 1..1440) {
                    Toast.makeText(this@MainActivity, "Enter 1 to 1440 minutes.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                prefs.edit()
                    .putLong("end_time", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(mins))
                    .putString("allowlist", allowlist.text.toString())
                    .apply()
                status.text = "Focus session active for $mins minute(s)."
                Toast.makeText(this@MainActivity, "Focus session started.", Toast.LENGTH_SHORT).show()
            }
        }
        root.addView(start)
        val stop = Button(this).apply {
            text = "End session"
            setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("End focus session?")
                    .setMessage("You can end the session from this control. This starter project is not tamper-proof.")
                    .setNegativeButton("Keep focusing", null)
                    .setPositiveButton("End") { _, _ ->
                        prefs.edit().putLong("end_time", 0L).apply()
                        status.text = "No active focus session."
                    }.show()
            }
        }
        root.addView(stop)
        root.addView(label("Important: Android does not let a normal app create a truly unbreakable lock. The phone owner can disable Accessibility, force-stop/uninstall the app, or change system settings. The warning siren is a deterrent, not impossible to silence.", 12f))
        setContentView(ScrollView(this).apply { addView(root) })
    }
}
