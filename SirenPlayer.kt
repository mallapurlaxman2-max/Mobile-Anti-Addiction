package com.example.focusguard

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

object SirenPlayer {
    private val handler = Handler(Looper.getMainLooper())
    private var tone: ToneGenerator? = null
    private var stopTask: Runnable? = null

    fun start(context: Context) {
        stop()
        try {
            val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            // Best effort: request alarm stream volume. Android/OEM policy may prevent changing it.
            val max = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audio.setStreamVolume(AudioManager.STREAM_ALARM, max, 0)
            tone = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            val endAt = System.currentTimeMillis() + 60_000L
            val pulse = object : Runnable {
                override fun run() {
                    if (System.currentTimeMillis() >= endAt) {
                        stop()
                        return
                    }
                    try { tone?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 900) } catch (_: Exception) {}
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(pulse)
            stopTask = Runnable { stop() }
            handler.postDelayed(stopTask!!, 60_000L)
        } catch (_: Exception) {
            stop()
        }
    }

    fun stop() {
        stopTask?.let { handler.removeCallbacks(it) }
        stopTask = null
        handler.removeCallbacksAndMessages(null)
        try { tone?.stopTone(); tone?.release() } catch (_: Exception) {}
        tone = null
    }
}
