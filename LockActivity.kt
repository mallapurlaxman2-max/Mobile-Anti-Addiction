package com.example.focusguard

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class LockActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0xFF070B12.toInt())
        }
        root.addView(TextView(this).apply {
            text = "FOCUS SESSION ACTIVE"
            textSize = 26f
            setTextColor(0xFFFF5555.toInt())
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "This app is not on your allowed list. Return to your permitted apps until the timer ends."
            textSize = 18f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        })
        setContentView(root)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Intentionally keep this screen visible; Android system controls may still dismiss it.
    }
}
