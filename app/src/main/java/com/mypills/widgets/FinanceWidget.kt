package com.mypills.widgets.finance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.*
import com.mypills.MainActivity

class FinanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            FinanceWidgetContent()
        }
    }

    @Composable
    private fun FinanceWidgetContent() {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color.White))
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "💰 Resumo Financeiro",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ColorProvider(Color.Black)
                    )
                )
                
                Spacer(modifier = GlanceModifier.height(12.dp))
                
                // Financial metrics
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.Start
                ) {
                    FinanceMetricItem(
                        label = "Receitas",
                        value = "R$ 3.200",
                        color = Color(0xFF4CAF50)
                    )
                    
                    Spacer(modifier = GlanceModifier.width(16.dp))
                    
                    FinanceMetricItem(
                        label = "Despesas",
                        value = "R$ 2.450",
                        color = Color(0xFFF44336)
                    )
                }
                
                Spacer(modifier = GlanceModifier.height(8.dp))
                
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.Start
                ) {
                    FinanceMetricItem(
                        label = "Saldo",
                        value = "R$ 750",
                        color = Color(0xFF2196F3)
                    )
                    
                    Spacer(modifier = GlanceModifier.width(16.dp))
                    
                    FinanceMetricItem(
                        label = "Medicamentos",
                        value = "R$ 245",
                        color = Color(0xFFFF9800)
                    )
                }
                
                Spacer(modifier = GlanceModifier.height(16.dp))
                
                // Action button
                Button(
                    text = "Ver Detalhes",
                    onClick = actionStartActivity<MainActivity>(),
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun FinanceMetricItem(
        label: String,
        value: String,
        color: Color
    ) {
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(Color.Gray)
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = ColorProvider(color)
                )
            )
        }
    }
}

class FinanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FinanceWidget()
}
