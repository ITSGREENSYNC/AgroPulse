package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocStrings
import com.example.data.db.FarmFieldEntity
import com.example.data.db.GrowthMilestoneEntity
import com.example.model.AppLanguage
import com.example.ui.theme.ForestGreen40
import com.example.ui.theme.HarvestGold40
import com.example.ui.viewmodels.GrowthLogViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthLogScreen(
    growthLogViewModel: GrowthLogViewModel,
    currentLanguage: AppLanguage = AppLanguage.EN,
    onOpenLanguageModal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val loc = LocStrings.getGrowthLog(currentLanguage)
    val fields by growthLogViewModel.fields.collectAsState()
    val uiState by growthLogViewModel.uiState.collectAsState()
    val milestones by growthLogViewModel.currentMilestones.collectAsState()

    val selectedField = uiState.selectedField ?: fields.firstOrNull()
    val context = LocalContext.current
    var customPickedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Android Photo Picker launcher (zero permissions needed per Play Policy)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bmp = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                }
                customPickedBitmap = bmp
            } catch (e: Exception) {
                // Ignore failure
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("growth_log_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Header bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = loc.growthLogTitle,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen40
                        )
                        Text(
                            text = loc.growthLogSubtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = ForestGreen40.copy(alpha = 0.1f),
                            modifier = Modifier
                                .clickable { onOpenLanguageModal() }
                                .testTag("btn_growth_lang_switch")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = ForestGreen40,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = currentLanguage.code.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen40
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { growthLogViewModel.openAddFieldSheet() },
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_add_field_header")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(loc.addFieldBtn, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Field Selector Chips / Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "My Field Plots (${fields.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.testTag("field_selector_row")
                    ) {
                        items(fields) { field ->
                            val isSelected = selectedField?.id == field.id
                            val daysElapsed = ((System.currentTimeMillis() - field.sowingDateMillis) / (86400000L)).coerceAtLeast(0)

                            Card(
                                onClick = { growthLogViewModel.selectField(field) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) ForestGreen40 else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
                                modifier = Modifier.testTag("field_chip_${field.id}")
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else ForestGreen40,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = field.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${field.cropType} • ${field.acreage} ac • Day $daysElapsed",
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Summary Card for Selected Field
            if (selectedField != null) {
                item {
                    val daysElapsed = ((System.currentTimeMillis() - selectedField.sowingDateMillis) / (86400000L)).coerceAtLeast(0)
                    val latestMilestone = milestones.lastOrNull()
                    val firstMilestone = milestones.firstOrNull()

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().testTag("field_summary_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedField.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${selectedField.cropType} • ${selectedField.acreage} Acres • Day $daysElapsed of cycle",
                                        fontSize = 12.sp,
                                        color = ForestGreen40,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (milestones.size >= 2) {
                                    Button(
                                        onClick = {
                                            growthLogViewModel.openComparisonModal(
                                                milestoneA = firstMilestone ?: milestones.first(),
                                                milestoneB = latestMilestone ?: milestones.last()
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = HarvestGold40),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.testTag("btn_compare_growth")
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(loc.compareGrowthBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                QuickMetricItem(
                                    label = "Milestones",
                                    value = "${milestones.size} Logged",
                                    icon = Icons.Default.CalendarToday
                                )
                                QuickMetricItem(
                                    label = loc.plantHeightLabel,
                                    value = "${latestMilestone?.plantHeightCm ?: 0} cm",
                                    icon = Icons.Default.Height
                                )
                                QuickMetricItem(
                                    label = loc.healthScoreLabel,
                                    value = "${latestMilestone?.healthScore ?: 10}/10",
                                    icon = Icons.Default.Speed
                                )
                            }
                        }
                    }
                }
            }

            // Timeline Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Growth Timeline (${milestones.size} photos)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = {
                            customPickedBitmap = null
                            growthLogViewModel.openAddMilestoneSheet()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_log_progress_top")
                    ) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(loc.logProgressBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Empty state if no milestones
            if (milestones.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp).testTag("empty_milestones_card")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = ForestGreen40.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Start Tracking Your Field",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = loc.noMilestonesHint,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Button(
                                onClick = {
                                    customPickedBitmap = null
                                    growthLogViewModel.openAddMilestoneSheet()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("btn_log_progress_empty")
                            ) {
                                Text(loc.logProgressBtn, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Chronological Timeline items
                itemsIndexed(milestones) { index, milestone ->
                    GrowthTimelineItemCard(
                        milestone = milestone,
                        isLast = index == milestones.lastIndex,
                        onDelete = { growthLogViewModel.deleteMilestone(milestone.id) },
                        onCompare = if (milestones.size >= 2) {
                            {
                                val other = if (index > 0) milestones[index - 1] else milestones[index + 1]
                                growthLogViewModel.openComparisonModal(other, milestone)
                            }
                        } else null
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Floating Action Button to quickly log photo
        FloatingActionButton(
            onClick = {
                customPickedBitmap = null
                growthLogViewModel.openAddMilestoneSheet()
            },
            containerColor = ForestGreen40,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("fab_add_milestone")
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AddAPhoto, contentDescription = "Log Growth Photo")
                Spacer(modifier = Modifier.width(8.dp))
                Text(loc.logProgressBtn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }

    // Modal Bottom Sheet: Add Milestone
    if (uiState.showAddMilestoneSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { growthLogViewModel.closeAddMilestoneSheet() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("bottom_sheet_add_milestone")
        ) {
            AddMilestoneSheetContent(
                selectedField = selectedField,
                uiState = uiState,
                loc = loc,
                customBitmap = customPickedBitmap,
                onPickPhoto = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onClearCustomPhoto = { customPickedBitmap = null },
                onStageChange = { stage -> growthLogViewModel.updateMilestoneForm(stage = stage) },
                onHeightChange = { h -> growthLogViewModel.updateMilestoneForm(heightCm = h) },
                onHealthScoreChange = { s -> growthLogViewModel.updateMilestoneForm(healthScore = s) },
                onNotesChange = { n -> growthLogViewModel.updateMilestoneForm(notes = n) },
                onSave = {
                    growthLogViewModel.saveMilestone(customPickedBitmap)
                    customPickedBitmap = null
                },
                onCancel = {
                    growthLogViewModel.closeAddMilestoneSheet()
                    customPickedBitmap = null
                }
            )
        }
    }

    // Modal Bottom Sheet: Add New Field Plot
    if (uiState.showAddFieldSheet) {
        val fieldSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { growthLogViewModel.closeAddFieldSheet() },
            sheetState = fieldSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("bottom_sheet_add_field")
        ) {
            AddFieldSheetContent(
                uiState = uiState,
                loc = loc,
                onNameChange = { name -> growthLogViewModel.updateNewFieldForm(name = name) },
                onCropChange = { crop -> growthLogViewModel.updateNewFieldForm(crop = crop) },
                onAcreageChange = { ac -> growthLogViewModel.updateNewFieldForm(acreage = ac) },
                onCreate = { growthLogViewModel.createField() },
                onCancel = { growthLogViewModel.closeAddFieldSheet() }
            )
        }
    }

    // Modal Dialog: Milestone Growth Comparison
    if (uiState.showComparisonModal && uiState.comparisonMilestoneA != null && uiState.comparisonMilestoneB != null) {
        GrowthComparisonDialog(
            milestoneA = uiState.comparisonMilestoneA!!,
            milestoneB = uiState.comparisonMilestoneB!!,
            allMilestones = milestones,
            loc = loc,
            onSelectA = { growthLogViewModel.setComparisonMilestoneA(it) },
            onSelectB = { growthLogViewModel.setComparisonMilestoneB(it) },
            onDismiss = { growthLogViewModel.closeComparisonModal() }
        )
    }
}

@Composable
fun QuickMetricItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, tint = ForestGreen40, modifier = Modifier.size(14.dp))
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun GrowthTimelineItemCard(
    milestone: GrowthMilestoneEntity,
    isLast: Boolean,
    onDelete: () -> Unit,
    onCompare: (() -> Unit)? = null
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Vertical Timeline Line & Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(ForestGreen40, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${milestone.daysAfterSowing}d",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(180.dp)
                        .background(ForestGreen40.copy(alpha = 0.35f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Milestone Content Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("milestone_card_${milestone.id}")
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Header row with stage tag and date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ForestGreen40.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "🌱 ${milestone.stage}",
                            color = ForestGreen40,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val dateFormatted = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(milestone.timestamp))
                        Text(
                            text = dateFormatted,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete milestone",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Date-Stamped Photo Preview
                val imageBitmap = remember(milestone.photoPath) {
                    try {
                        val file = File(milestone.photoPath)
                        if (file.exists()) {
                            BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1B381E))
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Milestone photo on day ${milestone.daysAfterSowing}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Fallback placeholder with stage tag
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Eco, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📅 Day ${milestone.daysAfterSowing} • ${milestone.stage}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Watermark Tag Pill in corner
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "📅 ${milestone.dateLabel} • ${milestone.plantHeightCm} cm",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Key metrics row: Plant Height & Health score
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Height, contentDescription = null, tint = ForestGreen40, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Height: ${milestone.plantHeightCm} cm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Health: ${milestone.healthScore}/10",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = if (milestone.healthScore >= 8) ForestGreen40 else HarvestGold40
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (milestone.healthScore >= 8) ForestGreen40 else HarvestGold40,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Agronomy notes quote
                if (milestone.notes.isNotBlank()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"${milestone.notes}\"",
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                if (onCompare != null) {
                    OutlinedButton(
                        onClick = onCompare,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.CompareArrows, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compare with another milestone", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AddMilestoneSheetContent(
    selectedField: FarmFieldEntity?,
    uiState: com.example.ui.viewmodels.GrowthLogUiState,
    loc: com.example.data.LocalizedStrings,
    customBitmap: Bitmap?,
    onPickPhoto: () -> Unit,
    onClearCustomPhoto: () -> Unit,
    onStageChange: (String) -> Unit,
    onHeightChange: (Int) -> Unit,
    onHealthScoreChange: (Int) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val stages = listOf(
        "Germination / Emergence",
        "Early Vegetative",
        "Rapid Vegetative Canopy",
        "Flowering / Tasseling",
        "Grain Fill / Pod Formation",
        "Ripening / Harvest"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = loc.logProgressBtn,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen40
                )
                Text(
                    text = "Field: ${selectedField?.name ?: "All Fields"} (${selectedField?.cropType ?: "Crop"})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        // Photo Selection Banner
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Progress Photo (Date-stamped & Saved Locally)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (customBitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            bitmap = customBitmap.asImageBitmap(),
                            contentDescription = "Selected photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = onClearCustomPhoto,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onPickPhoto,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick from Gallery", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { /* Default simulated field snapshot */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Eco, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate Snapshot", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Stage Selector Chips
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = loc.growthStageLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(stages) { stage ->
                    val isSelected = uiState.inputStage == stage
                    FilterChip(
                        selected = isSelected,
                        onClick = { onStageChange(stage) },
                        label = { Text(stage, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen40,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Height Slider
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = loc.plantHeightLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "${uiState.inputHeightCm} cm", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ForestGreen40)
            }
            Slider(
                value = uiState.inputHeightCm.toFloat(),
                onValueChange = { onHeightChange(it.toInt()) },
                valueRange = 5f..250f,
                steps = 48,
                modifier = Modifier.testTag("slider_height")
            )
        }

        // Health Score Slider (1-10)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = loc.healthScoreLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "${uiState.inputHealthScore}/10", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ForestGreen40)
            }
            Slider(
                value = uiState.inputHealthScore.toFloat(),
                onValueChange = { onHealthScoreChange(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.testTag("slider_health_score")
            )
        }

        // Management Notes Field
        OutlinedTextField(
            value = uiState.inputNotes,
            onValueChange = onNotesChange,
            label = { Text(loc.managementNotesLabel) },
            placeholder = { Text("e.g. Applied zinc spray, soil moisture high, vigorous tillering...") },
            modifier = Modifier.fillMaxWidth().testTag("input_milestone_notes"),
            shape = RoundedCornerShape(10.dp),
            maxLines = 3
        )

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1.5f).testTag("btn_save_milestone"),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(loc.saveMilestoneBtn, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AddFieldSheetContent(
    uiState: com.example.ui.viewmodels.GrowthLogUiState,
    loc: com.example.data.LocalizedStrings,
    onNameChange: (String) -> Unit,
    onCropChange: (String) -> Unit,
    onAcreageChange: (Double) -> Unit,
    onCreate: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add New Farm Field Plot",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen40
            )
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        OutlinedTextField(
            value = uiState.newFieldName,
            onValueChange = onNameChange,
            label = { Text("Field / Plot Name") },
            placeholder = { Text("e.g. Plot #5 - South Soybean") },
            modifier = Modifier.fillMaxWidth().testTag("input_field_name"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.newCropType,
            onValueChange = onCropChange,
            label = { Text("Crop Variety") },
            placeholder = { Text("e.g. Soybean, Wheat, Rice, Cotton") },
            modifier = Modifier.fillMaxWidth().testTag("input_crop_type"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Field Acreage", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "${String.format(Locale.US, "%.1f", uiState.newAcreage)} Acres", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ForestGreen40)
            }
            Slider(
                value = uiState.newAcreage.toFloat(),
                onValueChange = { onAcreageChange(it.toDouble()) },
                valueRange = 0.5f..20f,
                steps = 38
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onCreate,
                enabled = uiState.newFieldName.isNotBlank() && uiState.newCropType.isNotBlank(),
                modifier = Modifier.weight(1.5f).testTag("btn_create_field_submit"),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create Field Plot", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GrowthComparisonDialog(
    milestoneA: GrowthMilestoneEntity,
    milestoneB: GrowthMilestoneEntity,
    allMilestones: List<GrowthMilestoneEntity>,
    loc: com.example.data.LocalizedStrings,
    onSelectA: (GrowthMilestoneEntity) -> Unit,
    onSelectB: (GrowthMilestoneEntity) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_growth_comparison")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = loc.comparisonTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen40
                        )
                        Text(
                            text = "Side-by-side progression & delta analysis",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Side-by-side photo comparison
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Milestone A Card
                    MilestoneCompareColumn(
                        milestone = milestoneA,
                        label = "Initial (A)",
                        modifier = Modifier.weight(1f)
                    )

                    // Milestone B Card
                    MilestoneCompareColumn(
                        milestone = milestoneB,
                        label = "Later (B)",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Growth Analysis Metrics Summary
                val daysDiff = (milestoneB.daysAfterSowing - milestoneA.daysAfterSowing)
                val heightDiff = milestoneB.plantHeightCm - milestoneA.plantHeightCm
                val growthRatePerDay = if (daysDiff > 0) heightDiff.toDouble() / daysDiff else 0.0

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Growth Velocity & Delta Analysis",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ForestGreen40
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = loc.daysElapsedLabel, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (daysDiff >= 0) "+$daysDiff days" else "$daysDiff days",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = loc.growthDeltaLabel, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (heightDiff >= 0) "+$heightDiff cm" else "$heightDiff cm",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (heightDiff >= 0) ForestGreen40 else MaterialTheme.colorScheme.error
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Daily Rate", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${String.format(Locale.US, "%.1f", growthRatePerDay)} cm/d",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen40),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Comparison", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MilestoneCompareColumn(
    milestone: GrowthMilestoneEntity,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "$label • Day ${milestone.daysAfterSowing}",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = ForestGreen40
            )

            val imageBitmap = remember(milestone.photoPath) {
                try {
                    val file = File(milestone.photoPath)
                    if (file.exists()) {
                        BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                    } else null
                } catch (e: Exception) {
                    null
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF1B381E)),
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "🌱 Day ${milestone.daysAfterSowing}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(text = milestone.stage, fontSize = 10.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "Height: ${milestone.plantHeightCm} cm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = "Health: ${milestone.healthScore}/10", fontSize = 10.sp, color = ForestGreen40)
        }
    }
}
