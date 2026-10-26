package com.example.model

enum class PathogenType(val label: String) {
    FUNGAL("Fungal Disease"),
    BACTERIAL("Bacterial Blight"),
    VIRAL("Plant Virus"),
    PEST("Insect / Pest Infestation"),
    DEFICIENCY("Nutrient Deficiency"),
    HEALTHY("Healthy Foliage")
}

enum class SeverityLevel(val label: String) {
    HEALTHY("Optimal Health"),
    MILD("Mild Risk (10-25%)"),
    MODERATE("Moderate Threat (26-55%)"),
    SEVERE("Severe / Critical (>55%)")
}

data class NormalizedRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

data class CropDiseaseDetection(
    val id: String,
    val cropName: String,
    val diseaseOrPestName: String,
    val scientificName: String,
    val pathogenType: PathogenType,
    val severity: SeverityLevel,
    val confidence: Float,
    val affectedAreaPercent: Int,
    val primarySymptoms: List<String>,
    val organicRemedies: List<String>,
    val chemicalTreatments: List<String>,
    val recommendedProductIds: List<String>,
    val boundingBoxes: List<NormalizedRect> = emptyList(),
    val specimenImageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class TreatmentProduct(
    val id: String,
    val name: String,
    val category: String, // Fungicide, Bio-Insecticide, Foliar Spray, Bio-Stimulant
    val activeIngredient: String,
    val recommendedDosagePerAcre: Double, // in ml or grams
    val dosageUnit: String, // "ml/acre", "g/acre", "kg/acre"
    val dilutionWaterLitersPerAcre: Int = 150,
    val packSize: String, // e.g. "500 ml", "1 L", "5 kg"
    val packUnitsPerBottle: Double, // e.g. 500.0 ml
    val priceUsd: Double,
    val rating: Float,
    val reviewsCount: Int,
    val organicCertified: Boolean,
    val targetDiseases: List<String>,
    val description: String,
    val safetyPreharvestIntervalDays: Int
)

data class CartItem(
    val product: TreatmentProduct,
    val quantity: Int,
    val targetPlotAcres: Double = 1.0
)

enum class OrderStatus(val label: String) {
    CONFIRMED("Order Placed"),
    DISPATCHED("Dispatched from Agro-Depot"),
    OUT_FOR_DELIVERY("In Transit to Farm"),
    DELIVERED("Delivered")
}

data class FarmOrder(
    val orderId: String,
    val items: List<CartItem>,
    val totalUsd: Double,
    val farmAddress: String,
    val timestamp: Long,
    val status: OrderStatus,
    val estimatedArrivalDate: String
)

enum class SprayCondition(val label: String) {
    OPTIMAL("Optimal Window"),
    CAUTION("Moderate Caution"),
    POOR("Do Not Spray")
}

data class HourlySprayWindow(
    val hourLabel: String,
    val temperatureC: Int,
    val windSpeedKmh: Int,
    val rainProbabilityPercent: Int,
    val condition: SprayCondition,
    val advice: String
)

data class AgWeatherAlert(
    val id: String,
    val title: String,
    val alertType: String, // Fungal Outbreak Warning, High Heat Stress, Pest Surge
    val description: String,
    val actionItem: String,
    val affectedCrops: List<String>,
    val isCritical: Boolean
)

data class AgWeatherData(
    val locationName: String,
    val currentTempC: Int,
    val feelsLikeC: Int,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val windDirection: String,
    val precipitationChance: Int,
    val soilMoisturePercent: Int,
    val uvIndex: Int,
    val overallSprayWindow: SprayCondition,
    val sprayWindowReason: String,
    val hourlyForecast: List<HourlySprayWindow>,
    val activeAlerts: List<AgWeatherAlert>
)

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachedDiagnosis: CropDiseaseDetection? = null,
    val isThinking: Boolean = false
)

enum class MessageSender {
    USER,
    AGRONOMIST_AI
}
