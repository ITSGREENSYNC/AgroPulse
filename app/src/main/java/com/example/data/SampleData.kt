package com.example.data

import com.example.model.AgWeatherAlert
import com.example.model.AgWeatherData
import com.example.model.CropDiseaseDetection
import com.example.model.HourlySprayWindow
import com.example.model.NormalizedRect
import com.example.model.PathogenType
import com.example.model.SeverityLevel
import com.example.model.SprayCondition
import com.example.model.TreatmentProduct

object SampleData {

    val treatments: List<TreatmentProduct> = listOf(
        TreatmentProduct(
            id = "prod_copper_50wp",
            name = "CopperGuard 50 WP",
            category = "Fungicide & Bactericide",
            activeIngredient = "Copper Oxychloride 50% WP",
            recommendedDosagePerAcre = 600.0,
            dosageUnit = "g/acre",
            dilutionWaterLitersPerAcre = 200,
            packSize = "1 kg pack",
            packUnitsPerBottle = 1000.0,
            priceUsd = 18.50,
            rating = 4.8f,
            reviewsCount = 342,
            organicCertified = true,
            targetDiseases = listOf("Early Blight", "Late Blight", "Bacterial Spot", "Apple Scab"),
            description = "Broad-spectrum preventative copper fungicide. Forms a protective barrier on leaves preventing spore germination. Certified for organic use.",
            safetyPreharvestIntervalDays = 3
        ),
        TreatmentProduct(
            id = "prod_neem_shield",
            name = "EcoNeem Pure Bio-Insecticide",
            category = "Bio-Pesticide",
            activeIngredient = "Cold-Pressed Azadirachtin 10,000 ppm",
            recommendedDosagePerAcre = 400.0,
            dosageUnit = "ml/acre",
            dilutionWaterLitersPerAcre = 150,
            packSize = "1 Liter Bottle",
            packUnitsPerBottle = 1000.0,
            priceUsd = 24.00,
            rating = 4.9f,
            reviewsCount = 512,
            organicCertified = true,
            targetDiseases = listOf("Aphids", "Whiteflies", "Spider Mites", "Thrips", "Bollworms"),
            description = "Potent botanical antifeedant and insect growth regulator. Harmless to honeybees and ladybugs when applied during dusk.",
            safetyPreharvestIntervalDays = 1
        ),
        TreatmentProduct(
            id = "prod_tricho_bio",
            name = "TrichoRoot Bio-Fungicide",
            category = "Biological Control",
            activeIngredient = "Trichoderma harzianum 2x10^9 CFU/g",
            recommendedDosagePerAcre = 500.0,
            dosageUnit = "g/acre",
            dilutionWaterLitersPerAcre = 200,
            packSize = "1 kg Eco-pack",
            packUnitsPerBottle = 1000.0,
            priceUsd = 15.00,
            rating = 4.7f,
            reviewsCount = 189,
            organicCertified = true,
            targetDiseases = listOf("Root Rot", "Fusarium Wilt", "Sheath Blight", "Damping Off"),
            description = "Beneficial fungal antagonist that colonizes roots and leaf surfaces, outcompeting pathogenic fungi and stimulating plant immune defenses.",
            safetyPreharvestIntervalDays = 0
        ),
        TreatmentProduct(
            id = "prod_azoxy_curative",
            name = "StrobeMax Systemic Fungicide",
            category = "Systemic Fungicide",
            activeIngredient = "Azoxystrobin 23% SC",
            recommendedDosagePerAcre = 200.0,
            dosageUnit = "ml/acre",
            dilutionWaterLitersPerAcre = 150,
            packSize = "500 ml Bottle",
            packUnitsPerBottle = 500.0,
            priceUsd = 32.50,
            rating = 4.9f,
            reviewsCount = 420,
            organicCertified = false,
            targetDiseases = listOf("Northern Leaf Blight", "Stripe Rust", "Sheath Blight", "Powdery Mildew"),
            description = "High-potency translaminar strobilurin fungicide. Halts existing fungal respiration and prevents cellular spore expansion within 4 hours.",
            safetyPreharvestIntervalDays = 7
        ),
        TreatmentProduct(
            id = "prod_bt_caterpillar",
            name = "Thuricide BT Bio-Control",
            category = "Bio-Insecticide",
            activeIngredient = "Bacillus thuringiensis kurstaki",
            recommendedDosagePerAcre = 350.0,
            dosageUnit = "g/acre",
            dilutionWaterLitersPerAcre = 150,
            packSize = "500 g Tub",
            packUnitsPerBottle = 500.0,
            priceUsd = 19.99,
            rating = 4.8f,
            reviewsCount = 278,
            organicCertified = true,
            targetDiseases = listOf("Fall Armyworm", "Corn Earworm", "Tomato Hornworm", "Cabbage Looper"),
            description = "Target-specific biological crystal endotoxin. Disrupts caterpillar gut within hours of feeding without affecting beneficial insects or wildlife.",
            safetyPreharvestIntervalDays = 0
        ),
        TreatmentProduct(
            id = "prod_foliar_zinc_boron",
            name = "AgroBoost Zn-B Chelated Foliar",
            category = "Micronutrient Spray",
            activeIngredient = "Zinc EDTA 12% + Boron 2% Soluble",
            recommendedDosagePerAcre = 250.0,
            dosageUnit = "g/acre",
            dilutionWaterLitersPerAcre = 150,
            packSize = "1 kg Container",
            packUnitsPerBottle = 1000.0,
            priceUsd = 14.20,
            rating = 4.6f,
            reviewsCount = 144,
            organicCertified = false,
            targetDiseases = listOf("Zinc Deficiency (Interveinal Chlorosis)", "Poor Pollination / Blossom Drop"),
            description = "Fast-absorbing chelated micronutrient formula that restores chlorophyll synthesis, increases leaf size, and enhances grain fill rate.",
            safetyPreharvestIntervalDays = 0
        ),
        TreatmentProduct(
            id = "prod_bio_potassium_silicate",
            name = "SilicaFort Leaf Armor",
            category = "Bio-Stimulant & Leaf Protectant",
            activeIngredient = "Soluble Potassium Silicate (SiO2 20%)",
            recommendedDosagePerAcre = 500.0,
            dosageUnit = "ml/acre",
            dilutionWaterLitersPerAcre = 150,
            packSize = "1 Liter Bottle",
            packUnitsPerBottle = 1000.0,
            priceUsd = 21.00,
            rating = 4.7f,
            reviewsCount = 98,
            organicCertified = true,
            targetDiseases = listOf("Powdery Mildew", "Aphids", "Heat & Drought Stress"),
            description = "Deposits crystalline silica directly into plant epidermal cell walls, mechanically repelling chewing insects and blocking fungal hyphae penetration.",
            safetyPreharvestIntervalDays = 0
        )
    )

    val sampleDetections: List<CropDiseaseDetection> = listOf(
        CropDiseaseDetection(
            id = "det_corn_blight",
            cropName = "Corn (Maize)",
            diseaseOrPestName = "Northern Corn Leaf Blight",
            scientificName = "Exserohilum turcicum",
            pathogenType = PathogenType.FUNGAL,
            severity = SeverityLevel.SEVERE,
            confidence = 0.94f,
            affectedAreaPercent = 62,
            primarySymptoms = listOf(
                "Long, elliptical, grayish-green to tan lesions on lower and middle leaves",
                "Lesions measuring 2 to 15 cm in length with dark fungal sporulation in damp conditions",
                "Severe loss of photosynthetic canopy during crucial grain-fill stage"
            ),
            organicRemedies = listOf(
                "Apply Copper Hydroxide spray at early lesion onset",
                "Post-harvest deep tillage to bury crop debris harboring fungal mycelium",
                "Rotate with non-host legume crops (Soybean, Cowpea) next season"
            ),
            chemicalTreatments = listOf(
                "Apply StrobeMax (Azoxystrobin 23% SC) at 200 ml/acre",
                "Tank mix with Mancozeb 75% WP for dual-site protection"
            ),
            recommendedProductIds = listOf("prod_azoxy_curative", "prod_copper_50wp"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.18f, top = 0.22f, right = 0.82f, bottom = 0.48f),
                NormalizedRect(left = 0.25f, top = 0.54f, right = 0.78f, bottom = 0.78f)
            )
        ),
        CropDiseaseDetection(
            id = "det_tomato_early_blight",
            cropName = "Tomato",
            diseaseOrPestName = "Early Blight",
            scientificName = "Alternaria solani",
            pathogenType = PathogenType.FUNGAL,
            severity = SeverityLevel.MODERATE,
            confidence = 0.96f,
            affectedAreaPercent = 38,
            primarySymptoms = listOf(
                "Concentric dark brown 'target-board' ring spots on older leaves",
                "Yellow chlorotic halos surrounding lesions",
                "Progressive defoliation from ground level upwards"
            ),
            organicRemedies = listOf(
                "Spray CopperGuard 50 WP every 7-10 days",
                "Prune lower infected suckers and leaves touching soil",
                "Mulch beds with straw to prevent soil-splash transmission"
            ),
            chemicalTreatments = listOf(
                "Apply Chlorothalonil or Azoxystrobin spray",
                "Ensure coverage of leaf undersides and stem collars"
            ),
            recommendedProductIds = listOf("prod_copper_50wp", "prod_tricho_bio"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.15f, top = 0.28f, right = 0.60f, bottom = 0.62f),
                NormalizedRect(left = 0.55f, top = 0.45f, right = 0.88f, bottom = 0.82f)
            )
        ),
        CropDiseaseDetection(
            id = "det_wheat_stripe_rust",
            cropName = "Wheat",
            diseaseOrPestName = "Stripe Rust (Yellow Rust)",
            scientificName = "Puccinia striiformis",
            pathogenType = PathogenType.FUNGAL,
            severity = SeverityLevel.SEVERE,
            confidence = 0.91f,
            affectedAreaPercent = 54,
            primarySymptoms = listOf(
                "Bright yellow to orange pustules arranged in linear stripes between leaf veins",
                "Powdery orange spores rubbing off easily on fingertips",
                "Rapid chlorosis and grain shriveling"
            ),
            organicRemedies = listOf(
                "Foliar application of Potassium Silicate (SilicaFort) to strengthen leaf cuticles",
                "Plant certified resistant varieties (e.g., HD3086, DBW187)"
            ),
            chemicalTreatments = listOf(
                "Immediate systemic curative application of StrobeMax / Propiconazole 25% EC",
                "Spray within 48 hours before pustules engulf the flag leaf"
            ),
            recommendedProductIds = listOf("prod_azoxy_curative", "prod_bio_potassium_silicate"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.22f, top = 0.18f, right = 0.76f, bottom = 0.85f)
            )
        ),
        CropDiseaseDetection(
            id = "det_soy_aphid",
            cropName = "Soybean",
            diseaseOrPestName = "Soybean Aphid Infestation",
            scientificName = "Aphis glycines",
            pathogenType = PathogenType.PEST,
            severity = SeverityLevel.MODERATE,
            confidence = 0.89f,
            affectedAreaPercent = 42,
            primarySymptoms = listOf(
                "Dense colonies of tiny pear-shaped yellowish-green insects clustered on new shoots",
                "Sticky honeydew secretions coating foliage, attracting black sooty mold",
                "Leaf curling, stunting, and reduced pod set"
            ),
            organicRemedies = listOf(
                "Spray EcoNeem Bio-Insecticide at 400 ml/acre during late afternoon",
                "Release or conserve native beneficials: ladybugs, hoverfly larvae, lacewings",
                "High-pressure spray of potassium salt of fatty acids"
            ),
            chemicalTreatments = listOf(
                "Targeted selective insecticide if threshold exceeds 250 aphids per plant"
            ),
            recommendedProductIds = listOf("prod_neem_shield", "prod_bio_potassium_silicate"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.30f, top = 0.35f, right = 0.70f, bottom = 0.65f)
            )
        ),
        CropDiseaseDetection(
            id = "det_potato_late_blight",
            cropName = "Potato",
            diseaseOrPestName = "Late Blight",
            scientificName = "Phytophthora infestans",
            pathogenType = PathogenType.FUNGAL,
            severity = SeverityLevel.SEVERE,
            confidence = 0.97f,
            affectedAreaPercent = 70,
            primarySymptoms = listOf(
                "Water-soaked dark brownish-purple lesions rapidly expanding across leaflets",
                "White fuzzy mildew growth visible on the underside of leaves in humid mornings",
                "Foul odor in heavily infected canopy with rapid vine collapse"
            ),
            organicRemedies = listOf(
                "High-rate CopperGuard 50 WP preventative shield",
                "Destroy cull piles and eliminate infected volunteer potato plants",
                "Maintain wide plant spacing for enhanced canopy aeration"
            ),
            chemicalTreatments = listOf(
                "Apply translaminar Cymoxanil or Dimethomorph tank-mixed with Mancozeb",
                "Repeat applications every 5-7 days under persistent rain/dew conditions"
            ),
            recommendedProductIds = listOf("prod_copper_50wp", "prod_azoxy_curative"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.12f, top = 0.20f, right = 0.88f, bottom = 0.68f)
            )
        ),
        CropDiseaseDetection(
            id = "det_apple_powdery_mildew",
            cropName = "Apple Orchard",
            diseaseOrPestName = "Apple Powdery Mildew",
            scientificName = "Podosphaera leucotricha",
            pathogenType = PathogenType.FUNGAL,
            severity = SeverityLevel.MILD,
            confidence = 0.93f,
            affectedAreaPercent = 18,
            primarySymptoms = listOf(
                "Silvery-white powdery fungal felt on surface of young shoots and blossom clusters",
                "Leaves narrow, crinkled, and curled upward with stunted terminal growth",
                "Russeting netting on developing apple fruit skins"
            ),
            organicRemedies = listOf(
                "Apply wettable sulfur or Potassium Bicarbonate sprays",
                "Prune out infected 'silver tip' twigs during dormant winter pruning",
                "Spray SilicaFort leaf strengthener"
            ),
            chemicalTreatments = listOf(
                "Preventative triazole or strobilurin application from tight cluster through petal fall"
            ),
            recommendedProductIds = listOf("prod_bio_potassium_silicate", "prod_azoxy_curative"),
            boundingBoxes = listOf(
                NormalizedRect(left = 0.25f, top = 0.30f, right = 0.75f, bottom = 0.70f)
            )
        ),
        CropDiseaseDetection(
            id = "det_healthy_wheat",
            cropName = "Durum Wheat",
            diseaseOrPestName = "Healthy Stand",
            scientificName = "Triticum durum",
            pathogenType = PathogenType.HEALTHY,
            severity = SeverityLevel.HEALTHY,
            confidence = 0.99f,
            affectedAreaPercent = 0,
            primarySymptoms = listOf(
                "Deep chlorophyll-rich green leaves with upright erect canopy posture",
                "No lesions, chlorosis, fungal sporulation, or chewing pest damage detected",
                "Robust tiller development with vigorous root crown density"
            ),
            organicRemedies = listOf(
                "Maintain optimal balanced drip/furrow irrigation",
                "Routine preventative foliar sea-mineral application to sustain vigor"
            ),
            chemicalTreatments = emptyList(),
            recommendedProductIds = listOf("prod_foliar_zinc_boron"),
            boundingBoxes = emptyList()
        )
    )

    val weatherData: AgWeatherData = AgWeatherData(
        locationName = "Pleasant Valley Agri-District (Plot #4)",
        currentTempC = 23,
        feelsLikeC = 24,
        humidityPercent = 78,
        windSpeedKmh = 9,
        windDirection = "SSE",
        precipitationChance = 15,
        soilMoisturePercent = 64,
        uvIndex = 6,
        overallSprayWindow = SprayCondition.OPTIMAL,
        sprayWindowReason = "Gentle 9 km/h breeze & zero rain forecast next 5 hours. High droplet retention with minimal drift risk.",
        hourlyForecast = listOf(
            HourlySprayWindow("06:00", 18, 5, 5, SprayCondition.OPTIMAL, "Ideal calm conditions. Low wind drift."),
            HourlySprayWindow("08:00", 21, 8, 10, SprayCondition.OPTIMAL, "Prime foliar absorption window."),
            HourlySprayWindow("10:00", 24, 11, 15, SprayCondition.OPTIMAL, "Good spray window. Dew fully evaporated."),
            HourlySprayWindow("12:00", 28, 16, 20, SprayCondition.CAUTION, "Higher ambient temperature. Evaporation rate increasing."),
            HourlySprayWindow("14:00", 30, 22, 35, SprayCondition.POOR, "Wind gusts >20 km/h. High risk of spray drift."),
            HourlySprayWindow("16:00", 28, 25, 45, SprayCondition.POOR, "High wind & rain cloud buildup. Delay spraying."),
            HourlySprayWindow("18:00", 24, 12, 20, SprayCondition.CAUTION, "Winds settling. Suitable for evening bio-pesticide."),
            HourlySprayWindow("20:00", 21, 6, 10, SprayCondition.OPTIMAL, "Calm twilight. Excellent for Neem/BT sprays.")
        ),
        activeAlerts = listOf(
            AgWeatherAlert(
                id = "alert_blight_risk",
                title = "High Fungal Blight Sporulation Risk",
                alertType = "Disease Risk Index: 8.4/10",
                description = "Prolonged leaf wetness (>7 hours) combined with 78% relative humidity creates critical germination conditions for Early and Late Blight spores.",
                actionItem = "Apply preventative CopperGuard 50 WP or bio-fungicide protective shield before incoming rain front.",
                affectedCrops = listOf("Tomato", "Potato", "Corn"),
                isCritical = true
            ),
            AgWeatherAlert(
                id = "alert_spray_window",
                title = "Optimal Morning Spray Window Active",
                alertType = "Ag-Operation Advisory",
                description = "Wind speeds under 12 km/h until 11:30 AM. Ideal droplet deposition without chemical drift into neighboring watercourses.",
                actionItem = "Complete foliar fungicide and micronutrient tank mixes prior to midday heat.",
                affectedCrops = listOf("All Plots"),
                isCritical = false
            ),
            AgWeatherAlert(
                id = "alert_aphid_surge",
                title = "Warm Soil & Pest Surge Watch",
                alertType = "Entomology Alert",
                description = "Rising nighttime temperatures have triggered rapid aphid nymph hatching across soybean and legume fields.",
                actionItem = "Inspect underside of new growth. Apply EcoNeem at dusk if count exceeds economic threshold.",
                affectedCrops = listOf("Soybean", "Cotton", "Vegetables"),
                isCritical = false
            )
        )
    )

    val quickChatPrompts = listOf(
        "How do I prevent late blight without heavy chemicals?",
        "What is the right copper fungicide dilution for 3.5 acres?",
        "Is it safe to spray neem oil during crop flowering?",
        "How do I treat interveinal yellowing on corn leaves?"
    )
}
