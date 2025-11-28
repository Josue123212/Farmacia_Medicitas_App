package com.example.farmacia_medicitas.widget

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.text.FontWeight
import androidx.room.Room
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.Spacer
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.Button
import com.example.farmacia_medicitas.MainActivity
import com.example.farmacia_medicitas.R
import com.example.farmacia_medicitas.data.local.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicitasWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "medicitas.db").build()
        val units = withContext(Dispatchers.IO) { db.cartDao().getTotalUnits() }
        provideContent { Content(units) }
    }

    @Composable
    private fun Content(cartUnits: Int) {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .cornerRadius(16.dp)
                    .background(GlanceTheme.colors.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.padding(bottom = 8.dp)) {
                    Image(
                        provider = ImageProvider(R.mipmap.ic_launcher),
                        contentDescription = "Logo",
                        modifier = GlanceModifier.size(20.dp)
                    )
                    Column(modifier = GlanceModifier.padding(start = 8.dp)) {
                        Text(
                            text = "Farmacia Medicitas",
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Tu salud primero",
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier
                        .background(GlanceTheme.colors.secondaryContainer)
                        .cornerRadius(12.dp)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Carrito: ${cartUnits}",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSecondaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.size(width = 0.dp, height = 12.dp))

                Row(horizontalAlignment = Alignment.CenterHorizontally, modifier = GlanceModifier.padding(8.dp)) {
                    Button(
                        text = "Buscar",
                        onClick = actionRunCallback(
                            OpenAppAction::class.java,
                            actionParametersOf(OpenAppAction.ROUTE_KEY to "search")
                        )
                    )
                    Button(
                        text = "Carrito",
                        onClick = actionRunCallback(
                            OpenAppAction::class.java,
                            actionParametersOf(OpenAppAction.ROUTE_KEY to "cart")
                        ),
                        modifier = GlanceModifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
