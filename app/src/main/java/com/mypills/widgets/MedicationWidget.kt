package com.mypills.widgets.medication

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.*
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.mypills.MainActivity
import com.mypills.features.medications.domain.usecase.GetAllMedicationsUseCase
import com.mypills.features.reminders.domain.usecase.GetUpcomingRemindersUseCase
import com.mypills.core.database.entity.MedicationStatus

class MedicationWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MedicationWidgetContent()
        }
    }

    @Composable
    private fun MedicationWidgetContent() {
        val context = LocalContext.current
        
        GlanceTheme {
            LazyColumn(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color.White))
                    .padding(16.dp)
            ) {
                item {
                    // Header
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💊 Medicamentos Hoje",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = ColorProvider(Color.Black)
                            )
                        )
                    }
                }
                
                item { Spacer(modifier = GlanceModifier.height(12.dp)) }
                
                // Medication items (static for widget)
                items(3) { index ->
                    MedicationWidgetItem(
                        name = "Medicamento ${index + 1}",
                        time = "${8 + index * 4}:00",
                        status = if (index == 0) "taken" else "pending"
                    )
                    if (index < 2) {
                        Spacer(modifier = GlanceModifier.height(8.dp))
                    }
                }
                
                item { Spacer(modifier = GlanceModifier.height(12.dp)) }
                
                // Action button
                item {
                    Button(
                        text = "Abrir App",
                        onClick = actionStartActivity<MainActivity>(),
                        modifier = GlanceModifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    @Composable
    private fun MedicationWidgetItem(
        name: String,
        time: String,
        status: String
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(
                    ColorProvider(
                        if (status == "taken") Color(0xFFE8F5E8) else Color(0xFFFFF3E0)
                    )
                )
                .padding(8.dp)
                .cornerRadius(8.dp),
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorProvider(Color.Black)
                    )
                )
                Text(
                    text = time,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(Color.Gray)
                    )
                )
            }
            
            Text(
                text = if (status == "taken") "✓" else "⏰",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(
                        if (status == "taken") Color(0xFF4CAF50) else Color(0xFFFF9800)
                    )
                )
            )
        }
    }
}

class MedicationWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MedicationWidget()
}
