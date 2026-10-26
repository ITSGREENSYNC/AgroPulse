package com.example.data

import android.content.Context
import android.graphics.Bitmap
import com.example.R
import com.example.data.api.GeminiClient
import com.example.data.storage.LocalPhotoStorage
import com.example.model.AgWeatherData
import com.example.model.AppLanguage
import com.example.model.CartItem
import com.example.model.ChatMessage
import com.example.model.CropDiseaseDetection
import com.example.model.FarmOrder
import com.example.model.FirestoreCartItem
import com.example.model.FirestoreCropScan
import com.example.model.FirestoreFarmField
import com.example.model.FirestoreFarmOrder
import com.example.model.FirestoreGrowthMilestone
import com.example.model.MessageSender
import com.example.model.OrderStatus
import com.example.model.TreatmentProduct
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FirestoreAgroRepository(
    val db: FirebaseFirestore,
    private val context: Context
) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        context.applicationContext
    )

    private val auth = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    private val _weatherState = MutableStateFlow<AgWeatherData>(SampleData.weatherData)
    val weatherState: StateFlow<AgWeatherData> = _weatherState.asStateFlow()

    private val _catalog = MutableStateFlow<List<TreatmentProduct>>(SampleData.treatments)
    val catalog: StateFlow<List<TreatmentProduct>> = _catalog.asStateFlow()

    // Real-time observation of user's fields
    fun observeFields(): Flow<List<FirestoreFarmField>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/fields"
        emitAll(
            db.collection("users").document(uid).collection("fields")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreFarmField::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    // Real-time observation of milestones for a specific field
    fun observeMilestones(fieldId: String): Flow<List<FirestoreGrowthMilestone>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/growth_milestones"
        emitAll(
            db.collection("users").document(uid).collection("growth_milestones")
                .whereEqualTo("fieldId", fieldId)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreGrowthMilestone::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    // Real-time observation of crop scans
    fun observeScans(): Flow<List<FirestoreCropScan>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/scans"
        emitAll(
            db.collection("users").document(uid).collection("scans")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreCropScan::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    // Real-time observation of cart items mapped with product catalog
    fun observeCart(): Flow<List<CartItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/cart"
        emitAll(
            db.collection("users").document(uid).collection("cart")
                .snapshots()
                .map { snapshot ->
                    val items = snapshot.toObjects(FirestoreCartItem::class.java)
                    items.mapNotNull { item ->
                        val product = _catalog.value.find { it.id == item.productId }
                        product?.let {
                            CartItem(
                                product = it,
                                quantity = item.quantity,
                                targetPlotAcres = item.targetPlotAcres
                            )
                        }
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    // Real-time observation of orders
    fun observeOrders(): Flow<List<FarmOrder>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/orders"
        emitAll(
            db.collection("users").document(uid).collection("orders")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(FirestoreFarmOrder::class.java).map { order ->
                        FarmOrder(
                            orderId = order.orderId,
                            items = emptyList(),
                            totalUsd = order.totalUsd,
                            farmAddress = order.farmAddress,
                            timestamp = order.createdAt?.toDate()?.time ?: System.currentTimeMillis(),
                            status = try { OrderStatus.valueOf(order.status) } catch (e: Exception) { OrderStatus.CONFIRMED },
                            estimatedArrivalDate = order.estimatedArrival
                        )
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun createField(name: String, cropType: String, acreage: Double, sowingDateMillis: Long): String {
        val uid = requireUserId()
        val fieldId = "field_" + UUID.randomUUID().toString().take(8)
        val docRef = db.collection("users").document(uid).collection("fields").document(fieldId)
        val data = mapOf(
            "id" to fieldId,
            "userId" to uid,
            "name" to name,
            "cropType" to cropType,
            "acreage" to acreage,
            "sowingDateMillis" to sowingDateMillis,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data).await()
            return fieldId
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun deleteField(fieldId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("fields").document(fieldId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
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
    ): String {
        val uid = requireUserId()
        val milestoneId = "ms_" + UUID.randomUUID().toString().take(8)
        val now = System.currentTimeMillis()
        val daysElapsed = ((now - sowingDateMillis) / 86400000L).toInt().coerceAtLeast(1)
        val photoPath = LocalPhotoStorage.saveDateStampedBitmap(context, photoBitmap, fieldName, now)

        val docRef = db.collection("users").document(uid).collection("growth_milestones").document(milestoneId)
        val data = mapOf(
            "id" to milestoneId,
            "userId" to uid,
            "fieldId" to fieldId,
            "stage" to stage,
            "plantHeightCm" to plantHeightCm,
            "healthScore" to healthScore,
            "notes" to notes,
            "dateLabel" to "Day $daysElapsed",
            "daysAfterSowing" to daysElapsed,
            "photoPath" to photoPath,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data).await()
            return milestoneId
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun deleteMilestone(milestoneId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("growth_milestones").document(milestoneId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    suspend fun recordScan(detection: CropDiseaseDetection, notes: String = ""): String {
        val uid = requireUserId()
        val scanId = "scan_" + UUID.randomUUID().toString().take(8)
        val docRef = db.collection("users").document(uid).collection("scans").document(scanId)
        val data = mapOf(
            "id" to scanId,
            "userId" to uid,
            "cropName" to detection.cropName,
            "diseaseName" to detection.diseaseOrPestName,
            "severity" to detection.severity.name,
            "confidence" to detection.confidence.toDouble(),
            "affectedAreaPercent" to detection.affectedAreaPercent.toDouble(),
            "notes" to notes,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data).await()
            return scanId
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun addToCart(product: TreatmentProduct, quantity: Int = 1, acres: Double = 1.0) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("cart").document(product.id)
        try {
            val existing = docRef.get().await()
            val existingQty = existing.getLong("quantity")?.toInt() ?: 0
            val newQty = existingQty + quantity
            val isExisting = existing.exists()

            val data = mutableMapOf<String, Any>(
                "productId" to product.id,
                "userId" to uid,
                "quantity" to newQty,
                "targetPlotAcres" to acres,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (!isExisting) {
                data["createdAt"] = FieldValue.serverTimestamp()
            } else {
                existing.getTimestamp("createdAt")?.let { data["createdAt"] = it }
            }

            docRef.set(data).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("cart").document(productId)
        try {
            if (quantity <= 0) {
                docRef.delete().await()
            } else {
                val existing = docRef.get().await()
                if (existing.exists()) {
                    val createdAt = existing.getTimestamp("createdAt")
                    val acres = existing.getDouble("targetPlotAcres") ?: 1.0
                    val data = mutableMapOf<String, Any>(
                        "productId" to productId,
                        "userId" to uid,
                        "quantity" to quantity,
                        "targetPlotAcres" to acres,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                    createdAt?.let { data["createdAt"] = it }
                    docRef.set(data).await()
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun removeCartItem(productId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("cart").document(productId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    suspend fun placeOrder(farmAddress: String): FarmOrder {
        val uid = requireUserId()
        val items = observeCart().first()
        val total = items.sumOf { it.product.priceUsd * it.quantity }
        val orderId = "AGP-" + UUID.randomUUID().toString().take(8).uppercase()
        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(System.currentTimeMillis() + 86400000L * 2))
        val summary = items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }

        val docRef = db.collection("users").document(uid).collection("orders").document(orderId)
        val data = mapOf(
            "orderId" to orderId,
            "userId" to uid,
            "summary" to summary,
            "totalUsd" to total,
            "farmAddress" to farmAddress,
            "status" to OrderStatus.CONFIRMED.name,
            "estimatedArrival" to dateStr,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data).await()
            // clear cart documents
            val cartCollection = db.collection("users").document(uid).collection("cart")
            val cartSnapshot = cartCollection.get().await()
            for (doc in cartSnapshot.documents) {
                doc.reference.delete().await()
            }

            return FarmOrder(
                orderId = orderId,
                items = items,
                totalUsd = total,
                farmAddress = farmAddress,
                timestamp = System.currentTimeMillis(),
                status = OrderStatus.CONFIRMED,
                estimatedArrivalDate = dateStr
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun seedInitialDataIfEmpty() {
        try {
            val uid = requireUserId()
            val existing = db.collection("users").document(uid).collection("fields").limit(1).get().await()
            if (existing.isEmpty) {
                val now = System.currentTimeMillis()
                val dayMillis = 86400000L

                val field1Id = createField(
                    name = "Plot #4 - North Corn",
                    cropType = "Corn (Maize - Pioneer 3394)",
                    acreage = 4.5,
                    sowingDateMillis = now - (42 * dayMillis)
                )
                createField(
                    name = "Plot #2 - Riverfield Tomatoes",
                    cropType = "Roma Tomatoes (Determinate)",
                    acreage = 2.0,
                    sowingDateMillis = now - (28 * dayMillis)
                )
                createField(
                    name = "Plot #1 - East Orchard",
                    cropType = "Honeycrisp Apples",
                    acreage = 6.0,
                    sowingDateMillis = now - (120 * dayMillis)
                )

                // Seed initial growth milestone for field 1
                val bmpDay42 = LocalPhotoStorage.createPlaceholderGrowthBitmap("Tasseling & Silking", 112, 0xFF0D5302.toInt())
                saveGrowthMilestone(
                    fieldId = field1Id,
                    fieldName = "Plot #4 - North Corn",
                    photoBitmap = bmpDay42,
                    stage = "Tasseling / Flowering",
                    plantHeightCm = 112,
                    healthScore = 9,
                    notes = "Tassels emerging actively. Optimal gentle breeze aiding uniform pollination.",
                    sowingDateMillis = now - (42 * dayMillis)
                )
            }
        } catch (e: Exception) {
            // Non-fatal on seed
        }
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
