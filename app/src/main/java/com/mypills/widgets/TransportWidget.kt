package com.mypills.widgets.transport

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

class TransportWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            TransportWidgetContent()
        }
    }

    @Composable
    private fun TransportWidgetContent() {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color.White))
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "🚌 Próximos Ônibus",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ColorProvider(Color.Black)
                    )
                )
                
                Spacer(modifier = GlanceModifier.height(12.dp))
                
                // Bus arrivals
                repeat(3) { index ->
                    BusArrivalItem(
                        routeNumber = listOf("101", "205", "314")[index],
                        destination = listOf("Centro", "Shopping", "Terminal")[index],
                        arrivalTime = listOf("3 min", "8 min", "15 min")[index]
                    )
                    
                    if (index < 2) {
                        Spacer(modifier = GlanceModifier.height(8.dp))
                    }
                }
                
                Spacer(modifier = GlanceModifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.Start
                ) {
                    Button(
                        text = "Planejar Rota",
                        onClick = actionStartActivity<MainActivity>(),
                        modifier = GlanceModifier.defaultWeight()
                    )
                    
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    
                    Button(
                        text = "Atualizar",
                        onClick = actionStartActivity<MainActivity>(),
                        modifier = GlanceModifier.defaultWeight()
                    )
                }
            }
        }
    }

    @Composable
    private fun BusArrivalItem(
        routeNumber: String,
        destination: String,
        arrivalTime: String
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(ColorProvider(Color(0xFFF8F9FA)))
                .padding(8.dp)
                .cornerRadius(6.dp),
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            // Route number
            Box(
                modifier = GlanceModifier
                    .background(ColorProvider(Color(0xFF2196F3)))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .cornerRadius(4.dp)
            ) {
                Text(
                    text = routeNumber,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ColorProvider(Color.White)
                    )
                )
            }
            
            Spacer(modifier = GlanceModifier.width(8.dp))
            
            // Destination
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = destination,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.Black)
                    )
                )
            }
            
            // Arrival time
            Text(
                text = arrivalTime,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color(0xFFFF9800))
                )
            )
        }
    }
}

class TransportWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TransportWidget()
}

