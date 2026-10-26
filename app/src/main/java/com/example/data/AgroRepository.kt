package com.example.data

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import com.example.data.api.GeminiClient
import com.example.data.db.AgroPulseDatabase
import com.example.data.db.CartItemEntity
import com.example.data.db.FarmFieldEntity
import com.example.data.db.FarmOrderEntity
import com.example.data.db.GrowthMilestoneEntity
import com.example.data.db.ScanHistoryEntity
import com.example.data.storage.LocalPhotoStorage
import com.example.model.AgWeatherData
import com.example.model.AppLanguage
import com.example.model.CartItem
import com.example.model.ChatMessage
import com.example.model.CropDiseaseDetection
import com.example.model.FarmOrder
import com.example.model.MessageSender
import com.example.model.OrderStatus
import com.example.model.TreatmentProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AgroRepository(private val context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AgroPulseDatabase::class.java,
        "agropulse.db"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.agroDao()

    private val _weatherState = MutableStateFlow<AgWeatherData>(SampleData.weatherData)
    val weatherState: StateFlow<AgWeatherData> = _weatherState.asStateFlow()

    private val _catalog = MutableStateFlow<List<TreatmentProduct>>(SampleData.treatments)
    val catalog: StateFlow<List<TreatmentProduct>> = _catalog.asStateFlow()

    val scanHistory: Flow<List<ScanHistoryEntity>> = dao.getAllScans()

    val fieldsFlow: Flow<List<FarmFieldEntity>> = dao.getAllFields()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDefaultFieldsIfEmpty()
        }
    }

    private suspend fun seedDefaultFieldsIfEmpty() {
        val existingFields = dao.getAllFields().first()
        if (existingFields.isEmpty()) {
            val now = System.currentTimeMillis()
            val dayMillis = 86400000L

            val field1 = FarmFieldEntity(
                id = "field_corn_plot4",
                name = "Plot #4 - North Corn",
                cropType = "Corn (Maize - Pioneer 3394)",
                acreage = 4.5,
                sowingDateMillis = now - (42 * dayMillis)
            )
            val field2 = FarmFieldEntity(
                id = "field_tomato_plot2",
                name = "Plot #2 - Riverfield Tomatoes",
                cropType = "Roma Tomatoes (Determinate)",
                acreage = 2.0,
                sowingDateMillis = now - (28 * dayMillis)
            )
            val field3 = FarmFieldEntity(
                id = "field_orchard_plot1",
                name = "Plot #1 - East Orchard",
                cropType = "Honeycrisp Apples",
                acreage = 6.0,
                sowingDateMillis = now - (120 * dayMillis)
            )

            dao.insertField(field1)
            dao.insertField(field2)
            dao.insertField(field3)

            // Seed progressive growth milestones with date-stamped photos in local storage
            val bmpDay7 = LocalPhotoStorage.createPlaceholderGrowthBitmap("Emergence / Seedling", 8, 0xFF388E3C.toInt())
            val pathDay7 = LocalPhotoStorage.saveDateStampedBitmap(context, bmpDay7, field1.name, now - (35 * dayMillis))
            dao.insertMilestone(
                GrowthMilestoneEntity(
                    id = "ms_corn_1",
                    fieldId = field1.id,
                    photoPath = pathDay7,
                    stage = "Germination / Emergence",
                    plantHeightCm = 8,
                    healthScore = 9,
                    notes = "Uniform seedling emergence across all rows. Soil moisture at optimal 68%.",
                    dateLabel = "Day 7",
                    daysAfterSowing = 7,
                    timestamp = now - (35 * dayMillis)
                )
            )

            val bmpDay21 = LocalPhotoStorage.createPlaceholderGrowthBitmap("Vegetative V4 Stage", 25, 0xFF2E7D32.toInt())
            val pathDay21 = LocalPhotoStorage.saveDateStampedBitmap(context, bmpDay21, field1.name, now - (21 * dayMillis))
            dao.insertMilestone(
                GrowthMilestoneEntity(
                    id = "ms_corn_2",
                    fieldId = field1.id,
                    photoPath = pathDay21,
                    stage = "Early Vegetative",
                    plantHeightCm = 25,
                    healthScore = 9,
                    notes = "Applied balanced N-P-K side-dressing. Deep green chlorophyll development.",
                    dateLabel = "Day 21",
                    daysAfterSowing = 21,
                    timestamp = now - (21 * dayMillis)
                )
            )

            val bmpDay35 = LocalPhotoStorage.createPlaceholderGrowthBitmap("Rapid Canopy V8 Stage", 65, 0xFF1B5E20.toInt())
            val pathDay35 = LocalPhotoStorage.saveDateStampedBitmap(context, bmpDay35, field1.name, now - (7 * dayMillis))
            dao.insertMilestone(
                GrowthMilestoneEntity(
                    id = "ms_corn_3",
                    fieldId = field1.id,
                    photoPath = pathDay35,
                    stage = "Rapid Vegetative Canopy",
                    plantHeightCm = 65,
                    healthScore = 8,
                    notes = "Complete row canopy closure. Inspected for leaf spot, zero pest pressure detected.",
                    dateLabel = "Day 35",
                    daysAfterSowing = 35,
                    timestamp = now - (7 * dayMillis)
                )
            )

            val bmpDay42 = LocalPhotoStorage.createPlaceholderGrowthBitmap("Tasseling & Silking", 112, 0xFF0D5302.toInt())
            val pathDay42 = LocalPhotoStorage.saveDateStampedBitmap(context, bmpDay42, field1.name, now)
            dao.insertMilestone(
                GrowthMilestoneEntity(
                    id = "ms_corn_4",
                    fieldId = field1.id,
                    photoPath = pathDay42,
                    stage = "Tasseling / Flowering",
                    plantHeightCm = 112,
                    healthScore = 9,
                    notes = "Tassels emerging actively. Optimal gentle breeze aiding uniform pollination.",
                    dateLabel = "Day 42",
                    daysAfterSowing = 42,
                    timestamp = now
                )
            )
        }
    }

    fun getMilestonesForField(fieldId: String): Flow<List<GrowthMilestoneEntity>> =
        dao.getMilestonesForField(fieldId)

    suspend fun createField(name: String, cropType: String, acreage: Double, sowingDateMillis: Long) {
        val field = FarmFieldEntity(
            id = "field_" + UUID.randomUUID().toString().take(8),
            name = name,
            cropType = cropType,
            acreage = acreage,
            sowingDateMillis = sowingDateMillis
        )
        dao.insertField(field)
    }

    suspend fun deleteField(fieldId: String) {
        dao.deleteField(fieldId)
    }

    suspend fun saveGrowthMilestone(
        fieldId: String,
        fieldName: String,
        photoBitmap: Bitmap,
        stage: String,
        plantHeightCm: Int,
        healthScore: Int,
        notes: String,
        sowingDateMillis: Long
    ) {
        val now = System.currentTimeMillis()
        val daysElapsed = ((now - sowingDateMillis) / 86400000L).toInt().coerceAtLeast(1)
        val photoPath = LocalPhotoStorage.saveDateStampedBitmap(context, photoBitmap, fieldName, now)

        val milestone = GrowthMilestoneEntity(
            id = "ms_" + UUID.randomUUID().toString().take(8),
            fieldId = fieldId,
            photoPath = photoPath,
            stage = stage,
            plantHeightCm = plantHeightCm,
            healthScore = healthScore,
            notes = notes,
            dateLabel = "Day $daysElapsed",
            daysAfterSowing = daysElapsed,
            timestamp = now
        )
        dao.insertMilestone(milestone)
    }

    suspend fun deleteMilestone(milestoneId: String) {
        dao.deleteMilestone(milestoneId)
    }

    // Real-time cart flow combining local database with product catalog
    val cartFlow: Flow<List<CartItem>> = dao.getCartItems().map { entities ->
        entities.mapNotNull { entity ->
            val product = _catalog.value.find { it.id == entity.productId }
            product?.let { CartItem(product = it, quantity = entity.quantity, targetPlotAcres = entity.targetPlotAcres) }
        }
    }

    val ordersFlow: Flow<List<FarmOrder>> = dao.getAllOrders().map { entities ->
        entities.map { entity ->
            FarmOrder(
                orderId = entity.orderId,
                items = emptyList(), // Summary in entity
                totalUsd = entity.totalUsd,
                farmAddress = entity.farmAddress,
                timestamp = entity.timestamp,
                status = try { OrderStatus.valueOf(entity.status) } catch (e: Exception) { OrderStatus.CONFIRMED },
                estimatedArrivalDate = entity.estimatedArrival
            )
        }
    }

    suspend fun recordScan(detection: CropDiseaseDetection, notes: String = "") {
        dao.insertScan(
            ScanHistoryEntity(
                id = UUID.randomUUID().toString(),
                cropName = detection.cropName,
                diseaseName = detection.diseaseOrPestName,
                severity = detection.severity.name,
                confidence = detection.confidence,
                affectedAreaPercent = detection.affectedAreaPercent,
                notes = notes,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun addToCart(product: TreatmentProduct, quantity: Int = 1, acres: Double = 1.0) {
        val existing = dao.getCartItems().first().find { it.productId == product.id }
        val newQty = (existing?.quantity ?: 0) + quantity
        dao.upsertCartItem(
            CartItemEntity(
                productId = product.id,
                quantity = newQty,
                targetPlotAcres = acres
            )
        )
    }

    suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            dao.removeCartItem(productId)
        } else {
            val existing = dao.getCartItems().first().find { it.productId == productId }
            if (existing != null) {
                dao.upsertCartItem(existing.copy(quantity = quantity))
            }
        }
    }

    suspend fun removeCartItem(productId: String) {
        dao.removeCartItem(productId)
    }

    suspend fun placeOrder(farmAddress: String): FarmOrder {
        val items = cartFlow.first()
        val total = items.sumOf { it.product.priceUsd * it.quantity }
        val orderId = "AGP-" + UUID.randomUUID().toString().take(8).uppercase()
        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(System.currentTimeMillis() + 86400000L * 2))

        val summary = items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }

        dao.insertOrder(
            FarmOrderEntity(
                orderId = orderId,
                summary = summary,
                totalUsd = total,
                farmAddress = farmAddress,
                status = OrderStatus.CONFIRMED.name,
                estimatedArrival = dateStr,
                timestamp = System.currentTimeMillis()
            )
        )
        dao.clearCart()

        return FarmOrder(
            orderId = orderId,
            items = items,
            totalUsd = total,
            farmAddress = farmAddress,
            timestamp = System.currentTimeMillis(),
            status = OrderStatus.CONFIRMED,
            estimatedArrivalDate = dateStr
        )
    }

    suspend fun chatWithAgronomist(
        conversation: List<ChatMessage>,
        contextDiagnosis: CropDiseaseDetection?,
        language: AppLanguage = AppLanguage.EN
    ): Result<String> {
        val pairs = conversation.map { msg ->
            val role = if (msg.sender == MessageSender.USER) "user" else "model"
            role to msg.text
        }
        val contextStr = contextDiagnosis?.let {
            "Plant: ${it.cropName}, Diagnosed: ${it.diseaseOrPestName} (${it.scientificName}), Severity: ${it.severity.name}, Symptoms: ${it.primarySymptoms.joinToString()}"
        }
        return GeminiClient.askAgronomist(pairs, contextStr, language)
    }
}
