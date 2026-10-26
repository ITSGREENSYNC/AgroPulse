package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.AgroRepository
import com.example.data.AppLocaleManager
import com.example.data.LocStrings
import com.example.model.AppLanguage
import com.example.ui.components.LanguageSelectorBottomSheet
import com.example.ui.screens.AgronomistChatScreen
import com.example.ui.screens.CameraScanScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GrowthLogScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.WeatherAlertsScreen
import com.example.ui.theme.AgroPulseTheme
import com.example.ui.theme.ForestGreen40
import com.example.ui.viewmodels.ChatViewModel
import com.example.ui.viewmodels.GrowthLogViewModel
import com.example.ui.viewmodels.ScanViewModel
import com.example.ui.viewmodels.StoreViewModel
import com.example.ui.viewmodels.WeatherViewModel

enum class AgroScreen(
    val defaultTitle: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    OVERVIEW("Overview", Icons.Default.Home, Icons.Outlined.Home),
    SCAN_CROP("Scan HUD", Icons.Default.CameraAlt, Icons.Outlined.CameraAlt),
    GROWTH_LOG("Growth Log", Icons.Default.Eco, Icons.Outlined.Eco),
    TREATMENTS("Treatments", Icons.Default.ShoppingBag, Icons.Outlined.ShoppingBag),
    WEATHER("Ag-Weather", Icons.Default.Cloud, Icons.Outlined.Cloud),
    AI_ADVISOR("AI Doctor", Icons.Default.Psychology, Icons.Outlined.Psychology)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = AgroRepository(applicationContext)
        val scanViewModel = ScanViewModel(repository)
        val storeViewModel = StoreViewModel(repository)
        val weatherViewModel = WeatherViewModel(repository)
        val chatViewModel = ChatViewModel(repository)
        val growthLogViewModel = GrowthLogViewModel(repository)

        setContent {
            AgroPulseTheme {
                val currentLanguage by AppLocaleManager.currentLanguage.collectAsState()
                val loc = LocStrings.getGrowthLog(currentLanguage)

                var currentTab by remember { mutableIntStateOf(AgroScreen.OVERVIEW.ordinal) }
                var showLanguageModal by remember { mutableStateOf(false) }

                val weatherData by weatherViewModel.weatherData.collectAsState()
                val scanHistory by repository.scanHistory.collectAsState(initial = emptyList())
                val catalog by storeViewModel.products.collectAsState()

                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = ForestGreen40,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            AgroScreen.values().forEachIndexed { index, screen ->
                                val isSelected = currentTab == index
                                val labelText = when (screen) {
                                    AgroScreen.OVERVIEW -> loc.navOverview
                                    AgroScreen.SCAN_CROP -> loc.navScan
                                    AgroScreen.GROWTH_LOG -> loc.navGrowthLog
                                    AgroScreen.TREATMENTS -> loc.navTreatments
                                    AgroScreen.WEATHER -> loc.navWeather
                                    AgroScreen.AI_ADVISOR -> loc.navAdvisor
                                }

                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentTab = index },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                            contentDescription = labelText
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = labelText,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ForestGreen40,
                                        selectedTextColor = ForestGreen40,
                                        indicatorColor = ForestGreen40.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    when (AgroScreen.values()[currentTab]) {
                        AgroScreen.OVERVIEW -> {
                            DashboardScreen(
                                weatherData = weatherData,
                                scanHistory = scanHistory,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                onNavigateToScan = { currentTab = AgroScreen.SCAN_CROP.ordinal },
                                onNavigateToGrowthLog = { currentTab = AgroScreen.GROWTH_LOG.ordinal },
                                onNavigateToStore = { currentTab = AgroScreen.TREATMENTS.ordinal },
                                onNavigateToWeather = { currentTab = AgroScreen.WEATHER.ordinal },
                                onNavigateToChat = { currentTab = AgroScreen.AI_ADVISOR.ordinal },
                                onSelectCropForScan = { idx ->
                                    scanViewModel.selectSpecimen(idx)
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AgroScreen.SCAN_CROP -> {
                            CameraScanScreen(
                                scanViewModel = scanViewModel,
                                treatmentCatalog = catalog,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                onBuyTreatmentClicked = { product ->
                                    storeViewModel.openDosageCalculator(product)
                                    currentTab = AgroScreen.TREATMENTS.ordinal
                                },
                                onConsultAgronomist = { detection ->
                                    chatViewModel.attachDiagnosis(detection)
                                    chatViewModel.sendMessage(
                                        customPrompt = "Diagnosis: ${detection.cropName} - ${detection.diseaseOrPestName}. What is your prescription and spray advice?",
                                        language = currentLanguage
                                    )
                                    currentTab = AgroScreen.AI_ADVISOR.ordinal
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AgroScreen.GROWTH_LOG -> {
                            GrowthLogScreen(
                                growthLogViewModel = growthLogViewModel,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AgroScreen.TREATMENTS -> {
                            MarketplaceScreen(
                                storeViewModel = storeViewModel,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AgroScreen.WEATHER -> {
                            WeatherAlertsScreen(
                                weatherViewModel = weatherViewModel,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AgroScreen.AI_ADVISOR -> {
                            AgronomistChatScreen(
                                chatViewModel = chatViewModel,
                                currentLanguage = currentLanguage,
                                onOpenLanguageModal = { showLanguageModal = true },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }

                if (showLanguageModal) {
                    LanguageSelectorBottomSheet(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { selected ->
                            AppLocaleManager.setLanguage(selected)
                        },
                        onDismiss = { showLanguageModal = false }
                    )
                }
            }
        }
    }
}
