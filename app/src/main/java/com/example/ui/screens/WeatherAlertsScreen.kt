package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AgWeatherAlert
import com.example.model.HourlySprayWindow
import com.example.model.SprayCondition
import com.example.ui.theme.ForestGreen40
import com.example.ui.theme.HarvestGold40
import com.example.ui.theme.SeverityMild
import com.example.ui.theme.SeverityModerate
import com.example.ui.theme.SeveritySevere
import com.example.ui.viewmodels.WeatherViewModel

@Composable
fun WeatherAlertsScreen(
    weatherViewModel: WeatherViewModel,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.EN,
    onOpenLanguageModal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val loc = com.example.data.LocStrings.get(currentLanguage)
    val weatherData by weatherViewModel.weatherData.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("weather_alerts_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header with Language Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loc.weatherTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = weatherData.locationName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = ForestGreen40.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .clickable { onOpenLanguageModal() }
                        .testTag("weather_language_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = ForestGreen40,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentLanguage.nativeName,
                            color = ForestGreen40,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Spray Window Banner
        item {
            val statusLabel = com.example.data.LocStrings.getSprayConditionLabel(weatherData.overallSprayWindow, currentLanguage)
            val (bgColor, tintColor) = when (weatherData.overallSprayWindow) {
                SprayCondition.OPTIMAL -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
                SprayCondition.CAUTION -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
                SprayCondition.POOR -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = bgColor),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, tintColor.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("spray_window_banner")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (weatherData.overallSprayWindow == SprayCondition.OPTIMAL) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = tintColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = statusLabel,
                                fontWeight = FontWeight.Bold,
                                color = tintColor,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "${weatherData.currentTempC}°C",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = tintColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = weatherData.sprayWindowReason,
                        fontSize = 13.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Microclimate Metrics Grid
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Field Microclimate Telemetry",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherMetricItem(
                            icon = Icons.Default.Opacity,
                            label = "Humidity",
                            value = "${weatherData.humidityPercent}%",
                            subText = "Fungal trigger >75%"
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.Air,
                            label = "Wind Velocity",
                            value = "${weatherData.windSpeedKmh} km/h",
                            subText = "Dir: ${weatherData.windDirection}"
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.Yard,
                            label = "Soil Moisture",
                            value = "${weatherData.soilMoisturePercent}%",
                            subText = "Root saturation"
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherMetricItem(
                            icon = Icons.Default.Thunderstorm,
                            label = "Rain Chance",
                            value = "${weatherData.precipitationChance}%",
                            subText = "Low wash-off"
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.WbSunny,
                            label = "UV Index",
                            value = "${weatherData.uvIndex} (High)",
                            subText = "Midday solar peak"
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.WbSunny,
                            label = "Feels Like",
                            value = "${weatherData.feelsLikeC}°C",
                            subText = "Leaf evapotranspiration"
                        )
                    }
                }
            }
        }

        // Hourly Spray Opportunities
        item {
            Column {
                Text(
                    text = "Hourly Spray Windows Today",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    weatherData.hourlyForecast.forEach { hour ->
                        HourlyForecastCard(hour)
                    }
                }
            }
        }

        // Active Agricultural Disease Alerts
        item {
            Text(
                text = "Localized Crop Disease & Threat Alerts (${weatherData.activeAlerts.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(weatherData.activeAlerts) { alert ->
            WeatherAlertCard(alert = alert)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WeatherMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    subText: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(ForestGreen40.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ForestGreen40, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = subText, fontSize = 9.sp, color = Color.Gray)
    }
}

@Composable
fun HourlyForecastCard(hour: HourlySprayWindow) {
    val (pillColor, textColor) = when (hour.condition) {
        SprayCondition.OPTIMAL -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        SprayCondition.CAUTION -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        SprayCondition.POOR -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(115.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = hour.hourLabel, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(color = pillColor, shape = RoundedCornerShape(4.dp)) {
                Text(
                    text = hour.condition.label.split(" ")[0],
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "${hour.temperatureC}°C", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "${hour.windSpeedKmh} km/h wind", fontSize = 10.sp, color = Color.Gray)
            Text(text = "${hour.rainProbabilityPercent}% rain", fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun WeatherAlertCard(alert: AgWeatherAlert) {
    val borderColor = if (alert.isCritical) SeveritySevere else SeverityModerate

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("weather_alert_${alert.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = borderColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = alert.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = borderColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = alert.alertType,
                        color = borderColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = alert.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = ForestGreen40.copy(alpha = 0.08f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Action: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ForestGreen40
                    )
                    Text(
                        text = alert.actionItem,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(text = "Target Crops: ", fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = alert.affectedCrops.joinToString(", "),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
