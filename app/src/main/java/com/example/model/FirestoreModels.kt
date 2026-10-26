package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreFarmField(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val cropType: String = "",
    val acreage: Double = 0.0,
    val sowingDateMillis: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreGrowthMilestone(
    val id: String = "",
    val userId: String = "",
    val fieldId: String = "",
    val stage: String = "",
    val plantHeightCm: Int = 0,
    val healthScore: Int = 0,
    val notes: String = "",
    val dateLabel: String = "",
    val daysAfterSowing: Int = 0,
    val photoPath: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreCropScan(
    val id: String = "",
    val userId: String = "",
    val cropName: String = "",
    val diseaseName: String = "",
    val severity: String = "",
    val confidence: Double = 0.0,
    val affectedAreaPercent: Double = 0.0,
    val notes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreCartItem(
    val id: String = "",
    val productId: String = "",
    val userId: String = "",
    val quantity: Int = 0,
    val targetPlotAcres: Double = 1.0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreFarmOrder(
    val id: String = "",
    val orderId: String = "",
    val userId: String = "",
    val summary: String = "",
    val totalUsd: Double = 0.0,
    val farmAddress: String = "",
    val status: String = "",
    val estimatedArrival: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
