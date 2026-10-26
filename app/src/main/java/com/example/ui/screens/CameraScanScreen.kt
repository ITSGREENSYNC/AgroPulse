package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.model.CropDiseaseDetection
import com.example.model.PathogenType
import com.example.model.TreatmentProduct
import com.example.ui.components.CameraHUDOverlay
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.ForestGreen40
import com.example.ui.theme.HarvestGold40
import com.example.ui.viewmodels.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScanScreen(
    scanViewModel: ScanViewModel,
    treatmentCatalog: List<TreatmentProduct>,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.EN,
    onOpenLanguageModal: () -> Unit = {},
    onBuyTreatmentClicked: (TreatmentProduct) -> Unit,
    onConsultAgronomist: (CropDiseaseDetection) -> Unit,
    modifier: Modifier = Modifier
) {
    val loc = com.example.data.LocStrings.get(currentLanguage)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by scanViewModel.uiState.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val activeDetection = uiState.activeDetection

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("camera_scan_screen")
    ) {
        // Camera View or Specimen Background
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            // Camera binding fallback
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // High-fidelity agrarian foliage backdrop when camera is unprovisioned in emulator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF1E381E), Color(0xFF0B140B)),
                            radius = 900f
                        )
                    )
            )
        }

        // Real-Time Bounding Box and Reticle HUD Overlay
        CameraHUDOverlay(
            boundingBoxes = activeDetection.boundingBoxes,
            severity = activeDetection.severity,
            diseaseLabel = activeDetection.diseaseOrPestName,
            confidence = activeDetection.confidence,
            isScanningActive = uiState.isScanningActive
        )

        // Top Control Bar & Crop Specimen Selector
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                    )
                )
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (uiState.isScanningActive) Color(0xFF00E676) else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.isScanningActive) loc.cameraRealtimeActive else "SCANNER PAUSED",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = loc.cameraPointHint,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .clickable { onOpenLanguageModal() }
                            .padding(end = 8.dp)
                            .testTag("scan_language_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLanguage.nativeName,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = { scanViewModel.toggleTorch() },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .size(36.dp)
                            .testTag("torch_button")
                    ) {
                        Icon(
                            imageVector = if (uiState.isTorchEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Toggle Torch",
                            tint = if (uiState.isTorchEnabled) HarvestGold40 else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Specimen Carousel (Quick Switcher for field crops & emulator demonstration)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.availableSpecimens.forEachIndexed { index, spec ->
                    val isSelected = index == uiState.selectedSpecimenIndex
                    FilterChip(
                        selected = isSelected,
                        onClick = { scanViewModel.selectSpecimen(index) },
                        label = {
                            Text(
                                text = "${spec.cropName}: ${spec.diseaseOrPestName}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Black.copy(alpha = 0.6f),
                            labelColor = Color.White.copy(alpha = 0.9f),
                            selectedContainerColor = ForestGreen40,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.White.copy(alpha = 0.3f),
                            selectedBorderColor = Color(0xFF69F0AE)
                        ),
                        modifier = Modifier.testTag("specimen_chip_$index")
                    )
                }
            }
        }

        // Bottom HUD Card (Instant Detection Results & Actions)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f), Color.Black)
                    )
                )
                .padding(16.dp)
        ) {
            // Notification toast if saved
            AnimatedVisibility(
                visible = uiState.scanSavedNotification != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = ForestGreen40,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = uiState.scanSavedNotification ?: "",
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E261E).copy(alpha = 0.94f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("diagnosis_hud_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeDetection.cropName.uppercase(),
                                color = Color(0xFF81C784),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = activeDetection.diseaseOrPestName,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = activeDetection.scientificName,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }

                        SeverityBadge(
                            severity = activeDetection.severity,
                            customLabel = com.example.data.LocStrings.getSeverityLabel(activeDetection.severity, currentLanguage)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${loc.confidenceLabel}: ",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${(activeDetection.confidence * 100).toInt()}%",
                                color = Color(0xFF69F0AE),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${loc.canopyRiskLabel}: ",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${activeDetection.affectedAreaPercent}%",
                                color = if (activeDetection.affectedAreaPercent > 40) Color(0xFFFF5252) else Color(0xFFFFD740),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = { scanViewModel.saveCurrentScan() },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                .testTag("save_scan_icon")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Save Scan Log",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { scanViewModel.openDetailSheet() },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("inspect_treatment_button")
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(loc.inspectCure, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { onConsultAgronomist(activeDetection) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF69F0AE)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("ask_ai_from_scan_button")
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF69F0AE), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(loc.askAi, color = Color(0xFF69F0AE))
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet with Comprehensive Crop Pathogen & Treatment Breakdown
    if (uiState.showDetailSheet) {
        ModalBottomSheet(
            onDismissRequest = { scanViewModel.closeDetailSheet() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("diagnosis_detail_bottom_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${activeDetection.cropName} Pathology Report",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = activeDetection.diseaseOrPestName,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    SeverityBadge(severity = activeDetection.severity)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key Symptoms
                Text(
                    text = "Identified Symptoms",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                activeDetection.primarySymptoms.forEach { symptom ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(text = "• ", fontWeight = FontWeight.Bold, color = ForestGreen40)
                        Text(text = symptom, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Organic & Cultural Remedies
                Text(
                    text = "Recommended Organic & Cultural Practices",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                activeDetection.organicRemedies.forEach { remedy ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(text = "✓ ", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        Text(text = remedy, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Recommended In-App Treatments & Direct Purchase Options
                Text(
                    text = "Prescribed Agro-Treatments (In-Stock)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                val matchedProducts = treatmentCatalog.filter {
                    it.id in activeDetection.recommendedProductIds ||
                            it.targetDiseases.any { d -> d.contains(activeDetection.diseaseOrPestName, ignoreCase = true) }
                }

                if (matchedProducts.isEmpty()) {
                    Text(
                        text = "Standard protective foliar micronutrients recommended.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                } else {
                    matchedProducts.forEach { product ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = product.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (product.organicCertified) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = Color(0xFFE8F5E9),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "ORGANIC",
                                                    color = Color(0xFF2E7D32),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${product.packSize} • Rate: ${product.recommendedDosagePerAcre.toInt()} ${product.dosageUnit}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", product.priceUsd)}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 14.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        scanViewModel.closeDetailSheet()
                                        onBuyTreatmentClicked(product)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("buy_product_${product.id}")
                                ) {
                                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Order", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
