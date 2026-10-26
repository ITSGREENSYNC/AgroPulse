package com.example.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey val id: String,
    val cropName: String,
    val diseaseName: String,
    val severity: String,
    val confidence: Float,
    val affectedAreaPercent: Int,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int,
    val targetPlotAcres: Double
)

@Entity(tableName = "farm_orders")
data class FarmOrderEntity(
    @PrimaryKey val orderId: String,
    val summary: String,
    val totalUsd: Double,
    val farmAddress: String,
    val status: String,
    val estimatedArrival: String,
    val timestamp: Long
)

@Entity(tableName = "farm_fields")
data class FarmFieldEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cropType: String,
    val acreage: Double,
    val sowingDateMillis: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "growth_milestones")
data class GrowthMilestoneEntity(
    @PrimaryKey val id: String,
    val fieldId: String,
    val photoPath: String, // Local storage file path or specimen asset
    val stage: String,     // Germination, Vegetative, Flowering, Grain Fill, Harvest
    val plantHeightCm: Int,
    val healthScore: Int,  // 1-10
    val notes: String,
    val dateLabel: String,
    val daysAfterSowing: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface AgroDao {
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanHistoryEntity)

    @Query("DELETE FROM scan_history WHERE id = :scanId")
    suspend fun deleteScan(scanId: String)

    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeCartItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM farm_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<FarmOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: FarmOrderEntity)

    @Update
    suspend fun updateOrder(order: FarmOrderEntity)

    @Query("SELECT * FROM farm_fields ORDER BY createdAt ASC")
    fun getAllFields(): Flow<List<FarmFieldEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FarmFieldEntity)

    @Query("DELETE FROM farm_fields WHERE id = :fieldId")
    suspend fun deleteField(fieldId: String)

    @Query("SELECT * FROM growth_milestones WHERE fieldId = :fieldId ORDER BY timestamp ASC")
    fun getMilestonesForField(fieldId: String): Flow<List<GrowthMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: GrowthMilestoneEntity)

    @Query("DELETE FROM growth_milestones WHERE id = :milestoneId")
    suspend fun deleteMilestone(milestoneId: String)
}

@Database(
    entities = [
        ScanHistoryEntity::class,
        CartItemEntity::class,
        FarmOrderEntity::class,
        FarmFieldEntity::class,
        GrowthMilestoneEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AgroPulseDatabase : RoomDatabase() {
    abstract fun agroDao(): AgroDao
}
