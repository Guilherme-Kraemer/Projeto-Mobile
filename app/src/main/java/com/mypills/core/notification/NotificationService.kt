// app/src/main/java/com/mypills/core/notification/NotificationService.kt
package com.mypills.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.mypills.R
import com.mypills.MainActivity

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_reminders"
        const val FINANCE_CHANNEL_ID = "finance_alerts"
        const val TRANSPORT_CHANNEL_ID = "transport_updates"
        const val SHOPPING_CHANNEL_ID = "shopping_reminders"
        const val GENERAL_CHANNEL_ID = "general_notifications"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
                },
                
                NotificationChannel(
                    FINANCE_CHANNEL_ID,
                    "Alertas Financeiros",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Alertas sobre gastos e orçamento"
                },
                
                NotificationChannel(
                    TRANSPORT_CHANNEL_ID,
                    "Transporte Público",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Atualizações sobre horários de ônibus"
                },
                
                NotificationChannel(
                    SHOPPING_CHANNEL_ID,
                    "Lista de Compras",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Lembretes sobre lista de compras"
                },
                
                NotificationChannel(
                    GENERAL_CHANNEL_ID,
                    "Notificações Gerais",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Outras notificações do app"
                }
            )

            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }

    fun showMedicationReminder(
        id: Int,
        title: String,
        message: String,
        medicationId: String? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "medications")
            medicationId?.let { putExtra("medication_id", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, MEDICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_medication)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(createTakenAction(medicationId, id))
            .addAction(createSnoozeAction(medicationId, id))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }

    fun showFinanceAlert(
        id: Int,
        title: String,
        message: String,
        isUrgent: Boolean = false
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "finances")
        }

        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, FINANCE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_money)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (isUrgent) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }

    fun showTransportUpdate(
        id: Int,
        routeNumber: String,
        message: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "transport")
        }

        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, TRANSPORT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bus)
            .setContentTitle("Linha $routeNumber")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }

    fun showShoppingReminder(
        id: Int,
        title: String,
        itemCount: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "shopping")
        }

        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SHOPPING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shopping)
            .setContentTitle(title)
            .setContentText("$itemCount item(s) na sua lista")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        NotificationManagerCompat.from(context).notify(id, notification)
    }

    private fun createTakenAction(medicationId: String?, notificationId: Int): NotificationCompat.Action {
        val intent = Intent(context, MedicationActionReceiver::class.java).apply {
            action = "MEDICATION_TAKEN"
            putExtra("medication_id", medicationId)
            putExtra("notification_id", notificationId)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            "taken_$medicationId".hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Action.Builder(
            R.drawable.ic_check,
            "Tomei",
            pendingIntent
        ).build()
    }

    private fun createSnoozeAction(medicationId: String?, notificationId: Int): NotificationCompat.Action {
        val intent = Intent(context, MedicationActionReceiver::class.java).apply {
            action = "MEDICATION_SNOOZE"
            putExtra("medication_id", medicationId)
            putExtra("notification_id", notificationId)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            "snooze_$medicationId".hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Action.Builder(
            R.drawable.ic_snooze,
            "10min",
            pendingIntent
        ).build()
    }

    fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }

    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}

// app/src/main/java/com/mypills/core/notification/MedicationActionReceiver.kt
package com.mypills.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.medications.domain.usecase.TakeMedicationUseCase

@AndroidEntryPoint
class MedicationActionReceiver : BroadcastReceiver() {
    
    @Inject lateinit var takeMedicationUseCase: TakeMedicationUseCase
    @Inject lateinit var notificationService: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        val medicationId = intent.getStringExtra("medication_id") ?: return
        val notificationId = intent.getIntExtra("notification_id", 0)
        
        // Cancel the notification
        NotificationManagerCompat.from(context).cancel(notificationId)

        when (intent.action) {
            "MEDICATION_TAKEN" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        takeMedicationUseCase(medicationId, 1.0)
                        // Show confirmation notification
                        notificationService.showMedicationReminder(
                            id = notificationId + 1000,
                            title = "✓ Medicamento registrado",
                            message = "Lembrete concluído com sucesso!"
                        )
                    } catch (e: Exception) {
                        // Handle error
                    }
                }
            }
            "MEDICATION_SNOOZE" -> {
                // Reschedule notification for 10 minutes later
                CoroutineScope(Dispatchers.IO).launch {
                    kotlinx.coroutines.delay(10 * 60 * 1000) // 10 minutes
                    notificationService.showMedicationReminder(
                        id = notificationId,
                        title = "⏰ Lembrete de Medicamento",
                        message = "Hora de tomar seu medicamento!",
                        medicationId = medicationId
                    )
                }
            }
        }
    }
}