package com.example.motivationalquotes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log

object NotificationHelper {
    private const val CHANNEL_ID = "motivational_quotes"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Motivational Quotes"
            val descriptionText = "Shows motivational quotes when phone is unlocked"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, quote: String) {
        // Check if notifications are enabled
        val sharedPreferences = context.getSharedPreferences("MotivationalQuotes", Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("notifications_enabled", true)) {
            return
        }

        // Create an intent that opens the MainActivity when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Your Daily Motivation")
            .setContentText(quote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote))

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
                Log.d("NotificationHelper", "Notification sent successfully")
            }
        } catch (e: SecurityException) {
            Log.e("NotificationHelper", "Notification permission not granted", e)
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Failed to send notification", e)
        }
    }
}
