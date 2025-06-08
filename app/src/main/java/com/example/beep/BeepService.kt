package com.example.beep

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BeepService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val hourlyInterval = 60 * 60 * 1000L  // 1 hour
    private val channelId = "beep_service_channel"
    private val notificationId = 1
    lateinit var builder: NotificationCompat.Builder
    lateinit var channel: NotificationChannel
    lateinit var manager: NotificationManager

    private val beepRunnable = object : Runnable {
        override fun run() {
            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
            handler.postDelayed(this, hourlyInterval)
        }
    }

    override fun onCreate() {
        Log.w("SERVICE", "XXXXXX onCreate")
        super.onCreate()
        startForegroundNotification()
        scheduleFirstBeepAtNextFullHour()
    }

    override fun onDestroy() {
        Log.w("SERVICE", "XXXXXX onDestroy")
        handler.removeCallbacks(beepRunnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundNotification() {
        Log.w("SERVICE", "XXXXXX startForegroundNotification")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                channelId,
                "Beep Service",
                NotificationManager.IMPORTANCE_LOW
            )
            manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        builder = NotificationCompat.Builder(this, channelId)
        builder.setContentTitle("Beep Service")
            .setContentText("Running hourly beeps")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        startForeground(notificationId, builder.build())
    }

    private fun scheduleFirstBeepAtNextFullHour() {
        val now = System.currentTimeMillis()
        val nextHour = getNextFullHourCalendar().timeInMillis
        val delay = nextHour - now
        updateNotification()
        beep()
        handler.postDelayed({
            beep()
            updateNotification()
            handler.postDelayed(beepRunnable, hourlyInterval)
        }, delay)
    }

    private fun beep() {
        val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
    }

    private fun updateNotification() {
        val nextBeepTime = getNextFullHourCalendar()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val nextBeepString = timeFormat.format(nextBeepTime.time)

        builder.setContentTitle("Beep Service Running")
            .setContentText("Next beep at $nextBeepString")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        manager.notify(notificationId, builder.build())
    }

    private fun getNextFullHourCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }
}