package com.mypills.features.reminders.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.mypills.R
import com.mypills.MainActivity
import com.mypills.features.reminders.domain.model.*

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_reminders"
        const val APPOINTMENT_CHANNEL_ID = "appointment_reminders"
        const val GENERAL_CHANNEL_ID = "general_reminders"
        const val URGENT_CHANNEL_ID = "urgent_reminders"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channels = listOf(
            NotificationChannel(
                MEDICATION_CHANNEL_ID,
                "Lembretes de Medicamentos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações para tomar medicamentos"
                enableVibration(true)
                enableLights(true)
            },
            
            NotificationChannel(
                APPOINTMENT_CHANNEL_ID,
                "Consultas e Compromissos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Lembretes de consultas médicas e compromissos"
            },
            
            NotificationChannel(
                GENERAL_CHANNEL_ID,
                "Lembretes Gerais",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Outros tipos de lembretes"
            },
            
            NotificationChannel(
                URGENT_CHANNEL_ID,
                "Lembretes Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Lembretes com prioridade urgente"
                enableVibration(true)
                enableLights(true)
                setBypassDnd(true)
            }
        )

        channels.forEach { notificationManager.createNotificationChannel(it) }
    }

    fun showReminderNotification(reminder: Reminder) {
        val channelId = getChannelForReminder(reminder)
        val notificationId = reminder.id.hashCode()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reminder_id", reminder.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val completeIntent = createActionIntent("COMPLETE", reminder.id, notificationId)
        val snoozeIntent = createActionIntent("SNOOZE", reminder.id, notificationId)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(getIconForReminderType(reminder.type))
            .setContentTitle(reminder.title)
            .setContentText(reminder.description ?: "Toque para ver detalhes")
            .setPriority(getPriorityForReminder(reminder))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_check,
                "Marcar como Concluído",
                completeIntent
            )
            .addAction(
                R.drawable.ic_snooze,
                "Soneca",
                snoozeIntent
            )
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }

    private fun getChannelForReminder(reminder: Reminder): String {
        return when {
            reminder.priority == ReminderPriority.URGENT -> URGENT_CHANNEL_ID
            reminder.type == ReminderType.MEDICATION -> MEDICATION_CHANNEL_ID
            reminder.type == ReminderType.APPOINTMENT -> APPOINTMENT_CHANNEL_ID
            else -> GENERAL_CHANNEL_ID
        }
    }

    private fun getIconForReminderType(type: ReminderType): Int {
        return when (type) {
            ReminderType.MEDICATION -> R.drawable.ic_medication
            ReminderType.APPOINTMENT -> R.drawable.ic_event
            ReminderType.TASK -> R.drawable.ic_task
            ReminderType.SHOPPING -> R.drawable.ic_shopping
            ReminderType.FINANCE -> R.drawable.ic_money
            ReminderType.EXERCISE -> R.drawable.ic_fitness
            ReminderType.CUSTOM -> R.drawable.ic_reminder
        }
    }

    private fun getPriorityForReminder(reminder: Reminder): Int {
        return when (reminder.priority) {
            ReminderPriority.LOW -> NotificationCompat.PRIORITY_LOW
            ReminderPriority.MEDIUM -> NotificationCompat.PRIORITY_DEFAULT
            ReminderPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            ReminderPriority.URGENT -> NotificationCompat.PRIORITY_MAX
        }
    }

    private fun createActionIntent(action: String, reminderId: String, notificationId: Int): PendingIntent {
        val intent = Intent(context, ReminderActionReceiver::class.java).apply {
            this.action = action
            putExtra("reminder_id", reminderId)
            putExtra("notification_id", notificationId)
        }
        
        return PendingIntent.getBroadcast(
            context,
            "$action$reminderId".hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancelNotification(reminderId: String) {
        val notificationId = reminderId.hashCode()
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }
}
