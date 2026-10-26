package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NormalizedRect
import com.example.model.SeverityLevel
import com.example.ui.theme.SeverityHealthy
import com.example.ui.theme.SeverityMild
import com.example.ui.theme.SeverityModerate
import com.example.ui.theme.SeveritySevere

@Composable
fun SeverityBadge(
    severity: SeverityLevel,
    customLabel: String? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (severity) {
        SeverityLevel.HEALTHY -> Triple(SeverityHealthy.copy(alpha = 0.15f), SeverityHealthy, Icons.Default.CheckCircle)
        SeverityLevel.MILD -> Triple(SeverityMild.copy(alpha = 0.18f), SeverityMild, Icons.Default.Warning)
        SeverityLevel.MODERATE -> Triple(SeverityModerate.copy(alpha = 0.18f), SeverityModerate, Icons.Default.Warning)
        SeverityLevel.SEVERE -> Triple(SeveritySevere.copy(alpha = 0.18f), SeveritySevere, Icons.Default.Warning)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = customLabel ?: severity.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CameraHUDOverlay(
    boundingBoxes: List<NormalizedRect>,
    severity: SeverityLevel,
    diseaseLabel: String,
    confidence: Float,
    isScanningActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_transition")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    val boxColor = when (severity) {
        SeverityLevel.HEALTHY -> Color(0xFF4CAF50)
        SeverityLevel.MILD -> Color(0xFFFFB300)
        SeverityLevel.MODERATE -> Color(0xFFFF9800)
        SeverityLevel.SEVERE -> Color(0xFFF44336)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Draw reticle corner brackets
        val bracketLength = 36.dp.toPx()
        val bracketStroke = 4.dp.toPx()
        val cornerColor = Color.White.copy(alpha = 0.85f)

        val padding = 40.dp.toPx()
        val rectLeft = padding
        val rectTop = padding
        val rectRight = width - padding
        val rectBottom = height - padding

        // Top-Left
        drawLine(cornerColor, Offset(rectLeft, rectTop), Offset(rectLeft + bracketLength, rectTop), bracketStroke)
        drawLine(cornerColor, Offset(rectLeft, rectTop), Offset(rectLeft, rectTop + bracketLength), bracketStroke)

        // Top-Right
        drawLine(cornerColor, Offset(rectRight, rectTop), Offset(rectRight - bracketLength, rectTop), bracketStroke)
        drawLine(cornerColor, Offset(rectRight, rectTop), Offset(rectRight, rectTop + bracketLength), bracketStroke)

        // Bottom-Left
        drawLine(cornerColor, Offset(rectLeft, rectBottom), Offset(rectLeft + bracketLength, rectBottom), bracketStroke)
        drawLine(cornerColor, Offset(rectLeft, rectBottom), Offset(rectLeft, rectBottom - bracketLength), bracketStroke)

        // Bottom-Right
        drawLine(cornerColor, Offset(rectRight, rectBottom), Offset(rectRight - bracketLength, rectBottom), bracketStroke)
        drawLine(cornerColor, Offset(rectRight, rectBottom), Offset(rectRight, rectBottom - bracketLength), bracketStroke)

        // Animated laser scan line if active
        if (isScanningActive) {
            val currentLaserY = height * laserY
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        boxColor.copy(alpha = 0.35f),
                        boxColor.copy(alpha = 0.8f),
                        boxColor.copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    startY = currentLaserY - 24.dp.toPx(),
                    endY = currentLaserY + 24.dp.toPx()
                ),
                topLeft = Offset(rectLeft, currentLaserY - 20.dp.toPx()),
                size = Size(rectRight - rectLeft, 40.dp.toPx())
            )
            drawLine(
                color = Color.White,
                start = Offset(rectLeft, currentLaserY),
                end = Offset(rectRight, currentLaserY),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Draw dynamic disease bounding boxes
        boundingBoxes.forEach { box ->
            val bLeft = width * box.left
            val bTop = height * box.top
            val bWidth = width * (box.right - box.left)
            val bHeight = height * (box.bottom - box.top)

            // Bounding box fill & outline
            drawRoundRect(
                color = boxColor.copy(alpha = 0.18f),
                topLeft = Offset(bLeft, bTop),
                size = Size(bWidth, bHeight),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
            drawRoundRect(
                color = boxColor,
                topLeft = Offset(bLeft, bTop),
                size = Size(bWidth, bHeight),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width = 3.dp.toPx())
            )

            // Crosshair corners on each bounding box
            val cornerLen = 14.dp.toPx()
            drawLine(boxColor, Offset(bLeft, bTop), Offset(bLeft + cornerLen, bTop), 4.dp.toPx())
            drawLine(boxColor, Offset(bLeft, bTop), Offset(bLeft, bTop + cornerLen), 4.dp.toPx())
            drawLine(boxColor, Offset(bLeft + bWidth, bBottom(bTop, bHeight)), Offset(bLeft + bWidth - cornerLen, bBottom(bTop, bHeight)), 4.dp.toPx())
            drawLine(boxColor, Offset(bLeft + bWidth, bBottom(bTop, bHeight)), Offset(bLeft + bWidth, bBottom(bTop, bHeight) - cornerLen), 4.dp.toPx())
        }
    }
}

private fun bBottom(top: Float, height: Float) = top + height
