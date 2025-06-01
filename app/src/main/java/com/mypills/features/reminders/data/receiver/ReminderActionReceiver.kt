package com.mypills.features.reminders.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.reminders.domain.usecase.CompleteReminderUseCase
import com.mypills.features.reminders.domain.usecase.SnoozeReminderUseCase

@AndroidEntryPoint
class ReminderActionReceiver : BroadcastReceiver() {
    
    @Inject lateinit var completeReminderUseCase: CompleteReminderUseCase
    @Inject lateinit var snoozeReminderUseCase: SnoozeReminderUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id") ?: return
        val notificationId = intent.getIntExtra("notification_id", 0)
        
        // Cancel the notification
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }

        CoroutineScope(Dispatchers.IO).launch {
            when (intent.action) {
                "COMPLETE" -> {
                    completeReminderUseCase(reminderId)
                }
                "SNOOZE" -> {
                    snoozeReminderUseCase(reminderId, 10) // 10 minutes snooze
                }
            }
        }
    }
}
