package com.example.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LocalPhotoStorage {

    private const val FOLDER_NAME = "growth_logs"

    fun saveDateStampedBitmap(
        context: Context,
        original: Bitmap,
        fieldName: String,
        dateMillis: Long = System.currentTimeMillis()
    ): String {
        val folder = File(context.filesDir, FOLDER_NAME).apply { mkdirs() }
        val dateStamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(dateMillis))
        val fileName = "growth_${System.currentTimeMillis()}.jpg"
        val file = File(folder, fileName)

        // Draw date-stamp watermark onto bitmap
        val mutable = original.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutable)

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = (mutable.width * 0.035f).coerceIn(24f, 52f)
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setShadowLayer(4f, 2f, 2f, Color.BLACK)
        }

        val tagText = "📅 $dateStamp • $fieldName"
        canvas.drawText(tagText, 30f, mutable.height - 30f, paint)

        FileOutputStream(file).use { out ->
            mutable.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        return file.absolutePath
    }

    fun createPlaceholderGrowthBitmap(
        stageName: String,
        heightCm: Int,
        bgColorInt: Int = 0xFF2E7D32.toInt()
    ): Bitmap {
        val width = 640
        val height = 480
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(bgColorInt)

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 36f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            setShadowLayer(6f, 3f, 3f, Color.BLACK)
        }

        canvas.drawText("🌱 $stageName", width / 2f, height / 2f - 20f, paint)
        paint.textSize = 28f
        canvas.drawText("Crop Height: ${heightCm} cm", width / 2f, height / 2f + 30f, paint)

        return bitmap
    }
}
