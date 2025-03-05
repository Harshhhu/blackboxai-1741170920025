package com.example.motivationalquotes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class UnlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_USER_PRESENT -> {
                // Phone was unlocked
                Log.d("UnlockReceiver", "Device unlocked event received")
                handleUnlock(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // Device has finished booting
                NotificationHelper.createNotificationChannel(context)
            }
        }
    }

    private fun handleUnlock(context: Context) {
        try {
            Log.d("UnlockReceiver", "Handling unlock event")
            // Get shared preferences
            val sharedPreferences = context.getSharedPreferences("MotivationalQuotes", Context.MODE_PRIVATE)
            
            // Get the selected category
            val categoryName = sharedPreferences.getString("selected_category", "MOTIVATIONAL")
            val category = QuoteProvider.QuoteCategory.valueOf(categoryName ?: "MOTIVATIONAL")
            
            // Get a random quote from the selected category
            val quote = QuoteProvider.getRandomQuote(category)
            Log.d("UnlockReceiver", "Selected quote: $quote")
            
            // Show the notification
            NotificationHelper.showNotification(context, quote)
            Log.d("UnlockReceiver", "Notification sent successfully")
        } catch (e: Exception) {
            Log.e("UnlockReceiver", "Error handling unlock event", e)
        }
    }
}
