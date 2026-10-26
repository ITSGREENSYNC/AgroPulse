package com.example.data

import com.example.model.AppLanguage
import com.example.model.SeverityLevel
import com.example.model.SprayCondition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppLocaleManager {
    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }
}

data class LocalizedStrings(
    val appSubtitle: String,
    val navOverview: String,
    val navScan: String,
    val navTreatments: String,
    val navWeather: String,
    val navAdvisor: String,
    val plotActive: String,
    val heroTitle: String,
    val heroSubtitle: String,
    val heroButton: String,
    val cameraRealtimeActive: String,
    val cameraPointHint: String,
    val inspectCure: String,
    val askAi: String,
    val confidenceLabel: String,
    val canopyRiskLabel: String,
    val storeTitle: String,
    val storeSubtitle: String,
    val catalogTab: String,
    val ordersTab: String,
    val searchPlaceholder: String,
    val dosageCalculatorTitle: String,
    val plotSizeLabel: String,
    val dilutionWaterLabel: String,
    val recommendedPacksLabel: String,
    val estimatedCostLabel: String,
    val addToCartBtn: String,
    val cartTitle: String,
    val checkoutBtn: String,
    val confirmOrderBtn: String,
    val weatherTitle: String,
    val microclimateTitle: String,
    val humidityLabel: String,
    val windVelocityLabel: String,
    val soilMoistureLabel: String,
    val rainChanceLabel: String,
    val hourlyWindowsTitle: String,
    val cropThreatsTitle: String,
    val chatWelcome: String,
    val chatInputPlaceholder: String,
    val quickPrompts: List<String>,
    val navGrowthLog: String = "Growth Log",
    val growthLogTitle: String = "Crop Growth Log",
    val growthLogSubtitle: String = "Date-stamped visual progression & field logs",
    val addFieldBtn: String = "+ Add Field",
    val logProgressBtn: String = "Log Progress Photo",
    val compareGrowthBtn: String = "Compare Growth",
    val growthStageLabel: String = "Growth Stage",
    val plantHeightLabel: String = "Plant Height",
    val healthScoreLabel: String = "Health Score",
    val managementNotesLabel: String = "Agronomy Notes",
    val saveMilestoneBtn: String = "Save Growth Milestone",
    val noMilestonesHint: String = "No progress photos logged yet for this field. Tap 'Log Progress Photo' below to record field health.",
    val comparisonTitle: String = "Growth Comparison",
    val growthDeltaLabel: String = "Growth Delta",
    val daysElapsedLabel: String = "Days Elapsed"
)

object LocStrings {

    fun get(lang: AppLanguage): LocalizedStrings = when (lang) {
        AppLanguage.EN -> LocalizedStrings(
            appSubtitle = "Smart Crop Health & Treatment System",
            navOverview = "Overview",
            navScan = "Scan HUD",
            navTreatments = "Treatments",
            navWeather = "Ag-Weather",
            navAdvisor = "AI Doctor",
            plotActive = "Plot #4 Active",
            heroTitle = "Protect Your Harvest",
            heroSubtitle = "Scan leaves for instant fungal, pest & nutrient diagnosis with prescribed cure dosages.",
            heroButton = "Launch Crop Scanner HUD",
            cameraRealtimeActive = "AI REAL-TIME SCANNER ACTIVE",
            cameraPointHint = "Point camera at leaf lesions or stems",
            inspectCure = "Inspect & Cure",
            askAi = "Ask AI",
            confidenceLabel = "Confidence",
            canopyRiskLabel = "Canopy Risk",
            storeTitle = "Agro-Treatment Store",
            storeSubtitle = "Certified biologicals & curative chemistry",
            catalogTab = "Product Catalog",
            ordersTab = "Farm Orders",
            searchPlaceholder = "Search disease (Blight, Rust, Aphids)...",
            dosageCalculatorTitle = "Acreage Dosage Calculator",
            plotSizeLabel = "Farm Plot Size",
            dilutionWaterLabel = "Water Dilution Needed",
            recommendedPacksLabel = "Recommended Packs to Buy",
            estimatedCostLabel = "Estimated Total Cost",
            addToCartBtn = "Add Calculated Dose to Cart",
            cartTitle = "Farm Supply Cart",
            checkoutBtn = "Proceed to Checkout",
            confirmOrderBtn = "Place Order for Dispatch",
            weatherTitle = "Ag-Weather & Risk Radar",
            microclimateTitle = "Field Microclimate Telemetry",
            humidityLabel = "Humidity",
            windVelocityLabel = "Wind Velocity",
            soilMoistureLabel = "Soil Moisture",
            rainChanceLabel = "Rain Chance",
            hourlyWindowsTitle = "Hourly Spray Windows Today",
            cropThreatsTitle = "Localized Crop Disease & Threat Alerts",
            chatWelcome = "Welcome to AgroPulse AI! I am your precision agronomist. Ask me anything about crop diseases, spray schedules, or dosage calculations for your farm.",
            chatInputPlaceholder = "Ask your agronomist about crops, doses...",
            quickPrompts = listOf(
                "How do I prevent late blight organically?",
                "What is the copper fungicide dilution for 3.5 acres?",
                "Is it safe to spray neem oil during flowering?",
                "How do I treat interveinal yellowing on corn?"
            )
        )

        AppLanguage.HI -> LocalizedStrings(
            appSubtitle = "स्मार्ट फसल स्वास्थ्य एवं उपचार प्रणाली",
            navOverview = "अवलोकन",
            navScan = "स्कैनर",
            navTreatments = "दवाएं",
            navWeather = "मौसम अलर्ट",
            navAdvisor = "कृषि सलाहकार",
            plotActive = "खेत #4 सक्रिय",
            heroTitle = "अपनी फसल सुरक्षित रखें",
            heroSubtitle = "पत्तियों को स्कैन कर कीट, फफूंद व पोषक तत्वों की कमी की तुरंत पहचान और सटीक मात्रा जानें।",
            heroButton = "कैमरा स्कैनर शुरू करें",
            cameraRealtimeActive = "एआई लाइव स्कैनर चालू है",
            cameraPointHint = "कैमरे को पत्तियों के धब्बों या तने पर केंद्रित करें",
            inspectCure = "निरीक्षण व उपचार",
            askAi = "सलाहकार से पूछें",
            confidenceLabel = "सटीकता",
            canopyRiskLabel = "फसल जोखिम",
            storeTitle = "कृषि उपचार स्टोर",
            storeSubtitle = "प्रमाणित जैविक व कीटनाशक दवाएं",
            catalogTab = "उत्पाद सूची",
            ordersTab = "मेरे ऑर्डर",
            searchPlaceholder = "रोग खोजें (झुलसा, रतुआ, माहू)...",
            dosageCalculatorTitle = "प्रति एकड़ खुराक कैलकुलेटर",
            plotSizeLabel = "खेत का आकार",
            dilutionWaterLabel = "आवश्यक पानी (घोल)",
            recommendedPacksLabel = "अनुशंसित पैकेट संख्या",
            estimatedCostLabel = "अनुमानित कुल लागत",
            addToCartBtn = "माप के अनुसार कार्ट में जोड़ें",
            cartTitle = "कृषि आपूर्ति कार्ट",
            checkoutBtn = "ऑर्डर जारी रखें",
            confirmOrderBtn = "डिलीवरी के लिए ऑर्डर करें",
            weatherTitle = "कृषि मौसम एवं रोग चेतावनी",
            microclimateTitle = "खेत सूक्ष्म-जलवायु आंकड़े",
            humidityLabel = "नमी (आर्द्रता)",
            windVelocityLabel = "हवा की गति",
            soilMoistureLabel = "मिट्टी की नमी",
            rainChanceLabel = "बारिश की संभावना",
            hourlyWindowsTitle = "आज छिड़काव के अनुकूल घंटे",
            cropThreatsTitle = "स्थानीय फसल रोग एवं कीट चेतावनियां",
            chatWelcome = "एग्रोपल्स एआई में आपका स्वागत है! मैं आपका कृषि डॉक्टर हूं। फसल रोग, छिड़काव समय अथवा खुराक से संबंधित कोई भी प्रश्न पूछें।",
            chatInputPlaceholder = "अपनी फसल अथवा दवा के बारे में पूछें...",
            quickPrompts = listOf(
                "झुलसा रोग का जैविक रोकथाम कैसे करें?",
                "3.5 एकड़ के लिए कॉपर फंगीसाइड की सही मात्रा क्या है?",
                "क्या फूल आने के समय नीम का तेल छिड़क सकते हैं?",
                "मक्के की पत्तियों में पीलापन कैसे दूर करें?"
            )
        )

        AppLanguage.PA -> LocalizedStrings(
            appSubtitle = "ਸਮਾਰਟ ਫਸਲ ਸਿਹਤ ਅਤੇ ਇਲਾਜ ਪ੍ਰਣਾਲੀ",
            navOverview = "ਵੇਰਵਾ",
            navScan = "ਸਕੈਨਰ",
            navTreatments = "ਦਵਾਈਆਂ",
            navWeather = "ਮੌਸਮ ਅਲਰਟ",
            navAdvisor = "ਖੇਤੀ ਸਲਾਹਕਾਰ",
            plotActive = "ਖੇਤ ਨੰਬਰ 4 ਚਾਲੂ",
            heroTitle = "ਆਪਣੀ ਫਸਲ ਦੀ ਰਾਖੀ ਕਰੋ",
            heroSubtitle = "ਪੱਤਿਆਂ ਨੂੰ ਸਕੈਨ ਕਰਕੇ ਕੀੜੇ, ਉੱਲੀ ਅਤੇ ਖੁਰਾਕੀ ਤੱਤਾਂ ਦੀ ਘਾਟ ਦਾ ਤੁਰੰਤ ਪਤਾ ਲਗਾਓ।",
            heroButton = "ਕੈਮਰਾ ਸਕੈਨਰ ਖੋਲ੍ਹੋ",
            cameraRealtimeActive = "ਏਆਈ ਲਾਈਵ ਸਕੈਨਰ ਚਾਲੂ ਹੈ",
            cameraPointHint = "ਕੈਮਰੇ ਨੂੰ ਪੱਤੇ ਦੇ ਧੱਬਿਆਂ ਜਾਂ ਤਣੇ ਉੱਤੇ ਰੱਖੋ",
            inspectCure = "ਜਾਂਚ ਅਤੇ ਇਲਾਜ",
            askAi = "ਸਲਾਹਕਾਰ ਨੂੰ ਪੁੱਛੋ",
            confidenceLabel = "ਭਰੋਸੇਯੋਗਤਾ",
            canopyRiskLabel = "ਫਸਲ ਨੂੰ ਖਤਰਾ",
            storeTitle = "ਖੇਤੀ ਦਵਾਈਆਂ ਦੀ ਦੁਕਾਨ",
            storeSubtitle = "ਪ੍ਰਮਾਣਿਤ ਜੈਵਿਕ ਅਤੇ ਕੀਟਨਾਸ਼ਕ ਦਵਾਈਆਂ",
            catalogTab = "ਦਵਾਈਆਂ ਦੀ ਸੂਚੀ",
            ordersTab = "ਮੇਰੇ ਆਰਡਰ",
            searchPlaceholder = "ਬਿਮਾਰੀ ਖੋਜੋ (ਝੁਲਸਾ, ਕੁੰਗੀ, ਤੇਲਾ)...",
            dosageCalculatorTitle = "ਪ੍ਰਤੀ ਏਕੜ ਦਵਾਈ ਕੈਲਕੁਲੇਟਰ",
            plotSizeLabel = "ਖੇਤ ਦਾ ਰਕਬਾ (ਏਕੜ)",
            dilutionWaterLabel = "ਲੋੜੀਂਦਾ ਪਾਣੀ",
            recommendedPacksLabel = "ਲੋੜੀਂਦੇ ਪੈਕਟ",
            estimatedCostLabel = "ਅੰਦਾਜ਼ਨ ਕੁੱਲ ਖਰਚਾ",
            addToCartBtn = "ਗਿਣਤੀ ਅਨੁਸਾਰ ਕਾਰਟ ਵਿੱਚ ਪਾਓ",
            cartTitle = "ਖੇਤੀ ਸਮਾਨ ਕਾਰਟ",
            checkoutBtn = "ਆਰਡਰ ਅੱਗੇ ਵਧਾਓ",
            confirmOrderBtn = "ਡਿਲੀਵਰੀ ਆਰਡਰ ਕਰੋ",
            weatherTitle = "ਖੇਤੀ ਮੌਸਮ ਅਤੇ ਚੇਤਾਵਨੀ",
            microclimateTitle = "ਖੇਤ ਦੇ ਮੌਸਮੀ ਅੰਕੜੇ",
            humidityLabel = "ਹਵਾ ਵਿੱਚ ਨਮੀ",
            windVelocityLabel = "ਹਵਾ ਦੀ ਰਫ਼ਤਾਰ",
            soilMoistureLabel = "ਜ਼ਮੀਨ ਦੀ ਸਿੱਲ੍ਹ",
            rainChanceLabel = "ਮੀਂਹ ਦੀ ਸੰਭਾਵਨਾ",
            hourlyWindowsTitle = "ਸਪਰੇਅ ਕਰਨ ਲਈ ਸਭ ਤੋਂ ਵਧੀਆ ਸਮਾਂ",
            cropThreatsTitle = "ਇਲਾਕਾਈ ਫਸਲੀ ਬਿਮਾਰੀਆਂ ਦੀ ਚੇਤਾਵਨੀ",
            chatWelcome = "ਐਗਰੋਪਲਸ ਏਆਈ ਵਿੱਚ ਤੁਹਾਡਾ ਸਵਾਗਤ ਹੈ! ਮੈਂ ਤੁਹਾਡਾ ਖੇਤੀ ਮਾਹਿਰ ਹਾਂ। ਫਸਲ ਦੀਆਂ ਬਿਮਾਰੀਆਂ, ਸਪਰੇਅ ਦੇ ਸਮੇਂ ਜਾਂ ਦਵਾਈਆਂ ਦੀ ਮਾਤਰਾ ਬਾਰੇ ਕੋਈ ਵੀ ਸਵਾਲ ਪੁੱਛੋ।",
            chatInputPlaceholder = "ਫਸਲ ਜਾਂ ਦਵਾਈ ਬਾਰੇ ਸਵਾਲ ਪੁੱਛੋ...",
            quickPrompts = listOf(
                "ਕਣਕ ਦੀ ਪੀਲੀ ਕੁੰਗੀ ਦੀ ਰੋਕਥਾਮ ਕਿਵੇਂ ਕਰੀਏ?",
                "3.5 ਏਕੜ ਲਈ ਤਾਂਬੇ ਵਾਲੀ ਉੱਲੀਨਾਸ਼ਕ ਦੀ ਕਿੰਨੀ ਖੁਰਾਕ ਚਾਹੀਦੀ ਹੈ?",
                "ਕੀ ਫੁੱਲ ਪੈਣ ਵੇਲੇ ਨਿੰਮ ਦੇ ਤੇਲ ਦੀ ਸਪਰੇਅ ਕਰ ਸਕਦੇ ਹਾਂ?",
                "ਮੱਕੀ ਦੇ ਪੱਤਿਆਂ ਦਾ ਪੀਲਾਪਣ ਕਿਵੇਂ ਠੀਕ ਕਰੀਏ?"
            )
        )

        AppLanguage.MR -> LocalizedStrings(
            appSubtitle = "स्मार्ट पीक आरोग्य आणि उपचार प्रणाली",
            navOverview = "आढावा",
            navScan = "स्कॅनर",
            navTreatments = "औषधे",
            navWeather = "हवामान",
            navAdvisor = "कृषी तज्ज्ञ",
            plotActive = "शेत #4 सक्रिय",
            heroTitle = "आपले पीक सुरक्षित ठेवा",
            heroSubtitle = "पानांचे स्कॅन करून कीड, बुरशी व अन्नद्रव्यांची कमतरता तपासा आणि अचूक प्रमाण मिळवा.",
            heroButton = "कॅमेरा स्कॅनर सुरू करा",
            cameraRealtimeActive = "एआय लाइव्ह स्कॅनर सुरू आहे",
            cameraPointHint = "कॅमेरा पानांवरील डाग किंवा खोडावर धरा",
            inspectCure = "तपासा व उपचार करा",
            askAi = "तज्ज्ञांना विचारा",
            confidenceLabel = "अचूकता",
            canopyRiskLabel = "पिकाला धोका",
            storeTitle = "कृषी औषधे दालन",
            storeSubtitle = "प्रमाणित जैविक व रासायनिक औषधे",
            catalogTab = "उत्पादने सूची",
            ordersTab = "माझे ऑर्डर्स",
            searchPlaceholder = "रोग शोधा (करपा, तांबेरा, मावा)...",
            dosageCalculatorTitle = "एकरनिहाय औषध प्रमाण कॅल्क्युलेटर",
            plotSizeLabel = "शेताचे क्षेत्रफळ (एकर)",
            dilutionWaterLabel = "आवश्यक पाणी प्रमाण",
            recommendedPacksLabel = "आवश्यक बॉटल्स/पॅक",
            estimatedCostLabel = "अंदाजे एकूण किंमत",
            addToCartBtn = "मोजणीनुसार कार्टमध्ये जोडा",
            cartTitle = "कृषी साहित्य कार्ट",
            checkoutBtn = "खरेदी पूर्ण करा",
            confirmOrderBtn = "डिलिव्हरीसाठी ऑर्डर करा",
            weatherTitle = "शेती हवामान व धोक्याचा इशारा",
            microclimateTitle = "शेत हवामान निर्देशांक",
            humidityLabel = "हवेतील आर्द्रता",
            windVelocityLabel = "वाऱ्याचा वेग",
            soilMoistureLabel = "मातीतील ओलावा",
            rainChanceLabel = "पावसाची शक्यता",
            hourlyWindowsTitle = "आज फवारणीसाठी योग्य वेळ",
            cropThreatsTitle = "स्थानिक पीक रोग व कीड सूचना",
            chatWelcome = "ॲग्रोपल्स एआय मध्ये आपले स्वागत आहे! मी आपला कृषी सल्लागार आहे. पीक रोग, फवारणीचे वेळापत्रक किंवा औषधांच्या प्रमाणाबद्दल विचारा.",
            chatInputPlaceholder = "पिकाबद्दल किंवा औषधाबद्दल विचारा...",
            quickPrompts = listOf(
                "करपा रोगावर जैविक नियंत्रण कसे करावे?",
                "3.5 एकरांसाठी कॉपर बुरशीनाशकाचे प्रमाण किती असावे?",
                "फुलोऱ्याच्या अवस्थेत निंबोळी अर्क फवारता येतो का?",
                "मक्याच्या पानावरील पिवळेपणा कसा दूर करावा?"
            )
        )

        AppLanguage.TE -> LocalizedStrings(
            appSubtitle = "స్మార్ట్ పంట ఆరోగ్యం మరియు నివారణ వ్యవస్థ",
            navOverview = "సమీక్ష",
            navScan = "స్కాన్ హబ్",
            navTreatments = "మందులు",
            navWeather = "వాతావరణం",
            navAdvisor = "రైతు మిత్ర",
            plotActive = "పొలం #4 చురుకుగా ఉంది",
            heroTitle = "మీ పంటను రక్షించుకోండి",
            heroSubtitle = "ఆకులను స్కాన్ చేసి తెగుళ్లు, పురుగులు మరియు పోషక లోపాలను వెంటనే గుర్తించండి.",
            heroButton = "కెమెరా స్కానర్ ప్రారంభించండి",
            cameraRealtimeActive = "ఏఐ లైవ్ స్కానర్ యాక్టివ్",
            cameraPointHint = "కెమెరాను ఆకుల మచ్చలు లేదా కాండంపై ఉంచండి",
            inspectCure = "పరిశీలన & నివారణ",
            askAi = "ఏఐ ని అడగండి",
            confidenceLabel = "ఖచ్చితత్వం",
            canopyRiskLabel = "పంట ప్రమాదం",
            storeTitle = "వ్యవసాయ మందుల స్టోర్",
            storeSubtitle = "ధృవీకరించబడిన జీవ మరియు రసాయన మందులు",
            catalogTab = "ఉత్పత్తుల జాబితా",
            ordersTab = "నా ఆర్డర్లు",
            searchPlaceholder = "తెగులు పేరు వెతకండి (బ్లైట్, తుప్పు, పేను)...",
            dosageCalculatorTitle = "ఎకరా మోతాదు కాలిక్యులేటర్",
            plotSizeLabel = "పొలం విస్తీర్ణం (ఎకరాలు)",
            dilutionWaterLabel = "అవసరమైన నీటి పరిమాణం",
            recommendedPacksLabel = "సిఫార్సు చేసిన ప్యాకెట్లు",
            estimatedCostLabel = "అంచనా మొత్తం ఖర్చు",
            addToCartBtn = "లెక్కించిన మోతాదును కార్ట్‌కు జోడించండి",
            cartTitle = "వ్యవసాయ వస్తువుల కార్ట్",
            checkoutBtn = "ఆర్డర్ పూర్తి చేయండి",
            confirmOrderBtn = "డెలివరీ ఆర్డర్ ఇవ్వండి",
            weatherTitle = "వ్యవసాయ వాతావరణం & హెచ్చరికలు",
            microclimateTitle = "పొలం వాతావరణ సమాచారం",
            humidityLabel = "తేమ శాతం",
            windVelocityLabel = "గాలి వేగం",
            soilMoistureLabel = "నేల తేమ",
            rainChanceLabel = "వర్షం అవకాశం",
            hourlyWindowsTitle = "ఈ రోజు స్ప్రే చేయడానికి అనుకూల సమయాలు",
            cropThreatsTitle = "స్థానిక పంట తెగులు హెచ్చరికలు",
            chatWelcome = "అగ్రోపల్స్ ఏఐకి స్వాగతం! నేను మీ వ్యవసాయ నిపుణుడిని. పంట తెగుళ్లు, స్ప్రే సమయాలు లేదా మందుల మోతాదుల గురించి నన్ను అడగండి.",
            chatInputPlaceholder = "పంట లేదా మందుల గురించి అడగండి...",
            quickPrompts = listOf(
                "ఆకుమచ్చ తెగులును సేంద్రీయంగా ఎలా నివారించాలి?",
                "3.5 ఎకరాలకు కాపర్ శిలీంద్రనాశిని ఎంత కలపాలి?",
                "పూత దశలో వేప నూనెను పిచికారీ చేయవచ్చా?",
                "మొక్కజొన్న ఆకుల పసుపు రంగును ఎలా నయం చేయాలి?"
            )
        )

        AppLanguage.TA -> LocalizedStrings(
            appSubtitle = "ஸ்மார்ட் பயிர் நலம் மற்றும் சிகிச்சை முறை",
            navOverview = "கண்ணோட்டம்",
            navScan = "ஸ்கேனர்",
            navTreatments = "மருந்துகள்",
            navWeather = "வானிலை",
            navAdvisor = "விவசாய ஆலோசகர்",
            plotActive = "வயல் #4 செயலில் உள்ளது",
            heroTitle = "உங்கள் பயிரைப் பாதுகாக்கவும்",
            heroSubtitle = "இலைகளை ஸ்கேன் செய்து பூச்சிகள், பூஞ்சை நோய்களை உடனடியாகக் கண்டறியவும்.",
            heroButton = "கேமரா ஸ்கேனரைத் திறக்கவும்",
            cameraRealtimeActive = "AI நேரடி ஸ்கேனர் இயங்குகிறது",
            cameraPointHint = "இலைகளின் புள்ளிகள் அல்லது தண்டின் மீது கேமராவைக் காட்டுங்கள்",
            inspectCure = "ஆய்வு செய்து குணப்படுத்து",
            askAi = "ஆலோசகரிடம் கேளுங்கள்",
            confidenceLabel = "துல்லியம்",
            canopyRiskLabel = "பயிர் ஆபத்து",
            storeTitle = "விவசாய மருந்துகள் கடை",
            storeSubtitle = "சான்றளிக்கப்பட்ட இயற்கை மற்றும் பூச்சிக்கொல்லி மருந்துகள்",
            catalogTab = "மருந்து பட்டியல்",
            ordersTab = "எனது ஆர்டர்கள்",
            searchPlaceholder = "நோய்களைத் தேடுங்கள் (கருகல், துரு, அசுவினி)...",
            dosageCalculatorTitle = "ஏக்கர் அளவு கணக்கீட்டாளர்",
            plotSizeLabel = "வயல் பரப்பளவு (ஏக்கர்)",
            dilutionWaterLabel = "தேவையான தண்ணீர் அளவு",
            recommendedPacksLabel = "பரிந்துரைக்கப்பட்ட பாக்கெட்டுகள்",
            estimatedCostLabel = "மதிப்பிடப்பட்ட மொத்த விலை",
            addToCartBtn = "கணக்கிடப்பட்ட அளவை கார்ட்டில் சேர்",
            cartTitle = "விவசாயப் பொருட்கள் கூடை",
            checkoutBtn = "செக்அவுட் தொடரவும்",
            confirmOrderBtn = "விநியோகத்திற்கு ஆர்டர் செய்",
            weatherTitle = "விவசாய வானிலை மற்றும் எச்சரிக்கைகள்",
            microclimateTitle = "வயல்வெளி வானிலை தரவுகள்",
            humidityLabel = "ஈரப்பதம்",
            windVelocityLabel = "காற்றின் வேகம்",
            soilMoistureLabel = "மண் ஈரப்பதம்",
            rainChanceLabel = "மழை வாய்ப்பு",
            hourlyWindowsTitle = "இன்று தெளிப்பதற்கு உகந்த நேரங்கள்",
            cropThreatsTitle = "வட்டாரப் பயிர் நோய் எச்சரிக்கைகள்",
            chatWelcome = "அக்ரோபல்ஸ் AI-க்கு வரவேற்கிறோம்! நான் உங்கள் வேளாண்மை மருத்துவர். பயிர் நோய்கள், மருந்து தெளிக்கும் நேரம் மற்றும் அளவு குறித்து கேட்கலாம்.",
            chatInputPlaceholder = "பயிர் அல்லது மருந்துகள் குறித்து கேளுங்கள்...",
            quickPrompts = listOf(
                "இலைக்கருகல் நோயை இயற்கை முறையில் தடுப்பது எப்படி?",
                "3.5 ஏக்கருக்கு காப்பர் பூஞ்சாணக்கொல்லி அளவு எவ்வளவு?",
                "பூக்கும் தருணத்தில் வேப்பெண்ணெய் தெளிக்கலாமா?",
                "சோள இலை மஞ்சள் நிறமாவதைத் தடுப்பது எப்படி?"
            )
        )

        AppLanguage.BN -> LocalizedStrings(
            appSubtitle = "স্মার্ট ফসল স্বাস্থ্য ও প্রতিকার ব্যবস্থা",
            navOverview = "সংক্ষিপ্ত বিবরণ",
            navScan = "স্ক্যানার",
            navTreatments = "ওষুধ",
            navWeather = "আবহাওয়া",
            navAdvisor = "কৃষি উপদেষ্টা",
            plotActive = "ক্ষেত #৪ সক্রিয়",
            heroTitle = "আপনার ফসল রক্ষা করুন",
            heroSubtitle = "পাতার ছবি স্ক্যান করে ছত্রাক, পোকা ও পুষ্টির অভাব নির্ণয় করুন এবং সঠিক ডোজ জানুন।",
            heroButton = "ক্যামেরা স্ক্যানার চালু করুন",
            cameraRealtimeActive = "এআই লাইভ স্ক্যানার চলছে",
            cameraPointHint = "পাতার দাগ বা কাণ্ডের ওপর ক্যামেরা রাখুন",
            inspectCure = "পরীক্ষা ও নিরাময়",
            askAi = "পরামর্শদাতার সাহায্য নিন",
            confidenceLabel = "নির্ভুলতা",
            canopyRiskLabel = "ফসলের ঝুঁকি",
            storeTitle = "কৃষি ওষুধের দোকান",
            storeSubtitle = "প্রমাণিত জৈব ও বালাইনাশক ওষুধ",
            catalogTab = "ওষুধের তালিকা",
            ordersTab = "আমার অর্ডার",
            searchPlaceholder = "রোগ খুঁজুন (ব্লাইট, মরিচা, জাবপোকা)...",
            dosageCalculatorTitle = "বিঘা/একর ওষুধের মাত্রা ক্যালকুলেটর",
            plotSizeLabel = "জমির পরিমাণ (একর)",
            dilutionWaterLabel = "প্রয়োজনীয় জল",
            recommendedPacksLabel = "প্রয়োজনীয় প্যাকেট",
            estimatedCostLabel = "আনুমানিক মোট খরচ",
            addToCartBtn = "হিসাব অনুযায়ী কার্টে যোগ করুন",
            cartTitle = "কৃষি পণ্যের কার্ট",
            checkoutBtn = "অর্ডার সম্পন্ন করুন",
            confirmOrderBtn = "ডেলিভারির জন্য অর্ডার দিন",
            weatherTitle = "কৃষি আবহাওয়া ও সতর্কতা",
            microclimateTitle = "ক্ষেতের আবহাওয়া সূচক",
            humidityLabel = "বাতাসের আর্দ্রতা",
            windVelocityLabel = "বাতাসের গতি",
            soilMoistureLabel = "মাটির আর্দ্রতা",
            rainChanceLabel = "বৃষ্টির সম্ভাবনা",
            hourlyWindowsTitle = "আজ স্প্রে করার উপযুক্ত সময়",
            cropThreatsTitle = "স্থানীয় ফসল রোগ ও পোকার সতর্কতা",
            chatWelcome = "অ্যাগ্রোপালস এআই-তে স্বাগতম! আমি আপনার কৃষি বিশেষজ্ঞ। ফসলের রোগ, স্প্রে করার সময় বা ওষুধের মাত্রা সম্পর্কে যেকোনো প্রশ্ন করুন।",
            chatInputPlaceholder = "ফসল বা ওষুধ সম্পর্কে প্রশ্ন করুন...",
            quickPrompts = listOf(
                "আলুর ধসা রোগ প্রাকৃতিকভাবে কীভাবে রোধ করব?",
                "৩.৫ একরের জন্য কপার ছত্রাকনাশকের মাত্রা কত?",
                "ফুল আসার সময় কি নিম তেল স্প্রে করা নিরাপদ?",
                "ভুট্টার পাতার হলুদ ভাব দূর করার উপায় কী?"
            )
        )

        AppLanguage.GU -> LocalizedStrings(
            appSubtitle = "સ્માર્ટ પાક આરોગ્ય અને ઉપચાર પદ્ધતિ",
            navOverview = "ઝાંખી",
            navScan = "સ્કેનર",
            navTreatments = "દવાઓ",
            navWeather = "હવામાન",
            navAdvisor = "કૃષિ સલાહકાર",
            plotActive = "ખેતર #4 સક્રિય",
            heroTitle = "તમારા પાકનું રક્ષણ કરો",
            heroSubtitle = "પાંદડા સ્કેન કરીને જીવાત, ફૂગ અને પોષક તત્વોની ઉણપ ઓળખો અને ચોક્કસ માત્રા મેળવો.",
            heroButton = "કેમેરા સ્કેનર શરૂ કરો",
            cameraRealtimeActive = "એઆઈ લાઈવ સ્કેનર ચાલુ છે",
            cameraPointHint = "કેમેરાને પાંદડાના ડાઘ કે થડ પર રાખો",
            inspectCure = "તપાસ અને ઉપચાર",
            askAi = "સલાહકારને પૂછો",
            confidenceLabel = "ચોકસાઈ",
            canopyRiskLabel = "પાકનું જોખમ",
            storeTitle = "કૃષિ દવાઓની દુકાન",
            storeSubtitle = "પ્રમાણિત જૈવિક અને કીટનાશક દવાઓ",
            catalogTab = "દવાઓની યાદી",
            ordersTab = "મારા ઓર્ડર",
            searchPlaceholder = "રોગ શોધો (સુકારો, ગેરુ, મોલોમશી)...",
            dosageCalculatorTitle = "વીઘા/એકર દીઠ દવા ગણતરી",
            plotSizeLabel = "ખેતરનું માપ (એકર)",
            dilutionWaterLabel = "જરૂરી પાણીનો જથ્થો",
            recommendedPacksLabel = "જરૂરી પેકેટ સંખ્યા",
            estimatedCostLabel = "અંદાજિત કુલ ખર્ચ",
            addToCartBtn = "ગણતરી મુજબ કાર્ટમાં ઉમેરો",
            cartTitle = "ખેતી સામાન કાર્ટ",
            checkoutBtn = "ઓર્ડર આગળ વધારો",
            confirmOrderBtn = "ડિલિવરી માટે ઓર્ડર કરો",
            weatherTitle = "ખેતી હવામાન અને ચેતવણી",
            microclimateTitle = "ખેતરના હવામાનના આંકડા",
            humidityLabel = "ભેજનું પ્રમાણ",
            windVelocityLabel = "પવનની ગતિ",
            soilMoistureLabel = "જમીનનો ભેજ",
            rainChanceLabel = "વરસાદની શક્યતા",
            hourlyWindowsTitle = "આજે છંટકાવ માટે યોગ્ય સમય",
            cropThreatsTitle = "સ્થાનિક પાક રોગ ચેતવણીઓ",
            chatWelcome = "એગ્રોપલ્સ એઆઈમાં આપનું સ્વાગત છે! હું તમારો ખેતી નિષ્ણાત છું. પાકના રોગ, છંટકાવના સમય અથવા દવાઓની માત્રા વિશે પૂછો.",
            chatInputPlaceholder = "પાક અથવા દવા વિશે પૂછો...",
            quickPrompts = listOf(
                "ચરમી રોગનું જૈવિક નિયંત્રણ કેવી રીતે કરવું?",
                "3.5 એકર માટે કોપર ફૂગનાશકનું પ્રમાણ કેટલું?",
                "ફૂલ આવવાના સમયે લીમડાના તેલનો છંટકાવ કરી શકાય?",
                "મકાઈના પાન પીળા પડતા કેવી રીતે અટકાવવા?"
            )
        )

        AppLanguage.KN -> LocalizedStrings(
            appSubtitle = "ಸ್ಮಾರ್ಟ್ ಬೆಳೆ ಆರೋಗ್ಯ ಮತ್ತು ಪರಿಹಾರ ವ್ಯವಸ್ಥೆ",
            navOverview = "ಅವಲೋಕನ",
            navScan = "ಸ್ಕ್ಯಾನರ್",
            navTreatments = "ಔಷಧಿಗಳು",
            navWeather = "ಹವಾಮಾನ",
            navAdvisor = "ಕೃಷಿ ಸಲಹೆಗಾರ",
            plotActive = "ಜಮೀನು #4 ಸಕ್ರಿಯ",
            heroTitle = "ನಿಮ್ಮ ಬೆಳೆಯನ್ನು ರಕ್ಷಿಸಿ",
            heroSubtitle = "ಎಲೆಗಳನ್ನು ಸ್ಕ್ಯಾನ್ ಮಾಡಿ ರೋಗಗಳು, ಕೀಟಗಳು ಮತ್ತು ಪೋಷಕಾಂಶಗಳ ಕೊರತೆಯನ್ನು ತಕ್ಷಣ ಪತ್ತೆಹಚ್ಚಿ.",
            heroButton = "ಕ್ಯಾಮೆರಾ ಸ್ಕ್ಯಾನರ್ ಪ್ರಾರಂಭಿಸಿ",
            cameraRealtimeActive = "AI ಲೈವ್ ಸ್ಕ್ಯಾನರ್ ಸಕ್ರಿಯವಾಗಿದೆ",
            cameraPointHint = "ಎಲೆಯ ಕಲೆಗಳು ಅಥವಾ ಕಾಂಡದ ಮೇಲೆ ಕ್ಯಾಮೆರಾ ಇರಿಸಿ",
            inspectCure = "ಪರಿಶೀಲಿಸಿ ಮತ್ತು ಗುಣಪಡಿಸಿ",
            askAi = "ಸಲಹೆಗಾರರನ್ನು ಕೇಳಿ",
            confidenceLabel = "ಖಚಿತತೆ",
            canopyRiskLabel = "ಬೆಳೆಗೆ ಅಪಾಯ",
            storeTitle = "ಕೃಷಿ ಔಷಧಿ ಅಂಗಡಿ",
            storeSubtitle = "ಪ್ರಮಾಣೀಕೃತ ಸಾವಯವ ಮತ್ತು ಕೀಟನಾಶಕಗಳು",
            catalogTab = "ಔಷಧಿ ಪಟ್ಟಿ",
            ordersTab = "ನನ್ನ ಆರ್ಡರ್‌ಗಳು",
            searchPlaceholder = "ರೋಗ ಹುಡುಕಿ (ಬ್ಲೈಟ್, ತುಕ್ಕು, ಜಿಗಿಹುಳು)...",
            dosageCalculatorTitle = "ಎಕರೆವಾರು ಪ್ರಮಾಣ ಕ್ಯಾಲ್ಕುಲೇಟರ್",
            plotSizeLabel = "ಜಮೀನಿನ ವಿಸ್ತೀರ್ಣ (ಎಕರೆ)",
            dilutionWaterLabel = "ಅಗತ್ಯವಿರುವ ನೀರಿನ ಪ್ರಮಾಣ",
            recommendedPacksLabel = "ಶಿಫಾರಸು ಮಾಡಿದ ಪ್ಯಾಕೆಟ್‌ಗಳು",
            estimatedCostLabel = "ಅಂದಾಜು ಒಟ್ಟು ವೆಚ್ಚ",
            addToCartBtn = "ಲೆಕ್ಕಾಚಾರದ ಪ್ರಕಾರ ಕಾರ್ಟ್‌ಗೆ ಸೇರಿಸಿ",
            cartTitle = "ಕೃಷಿ ವಸ್ತುಗಳ ಕಾರ್ಟ್",
            checkoutBtn = "ಆರ್ಡರ್ ಮುಂದುವರಿಸಿ",
            confirmOrderBtn = "ಡೆಲಿವರಿಗೆ ಆರ್ಡರ್ ಮಾಡಿ",
            weatherTitle = "ಕೃಷಿ ಹವಾಮಾನ ಮತ್ತು ಎಚ್ಚರಿಕೆಗಳು",
            microclimateTitle = "ಜಮೀನಿನ ಹವಾಮಾನ ಮಾಹಿತಿ",
            humidityLabel = "ತೇವಾಂಶ",
            windVelocityLabel = "ಗಾಳಿಯ ವೇಗ",
            soilMoistureLabel = "ಮಣ್ಣಿನ ತೇವಾಂಶ",
            rainChanceLabel = "ಮಳೆಯ ಸಾಧ್ಯತೆ",
            hourlyWindowsTitle = "ಇಂದು ಸಿಂಪಡಣೆಗೆ ಸೂಕ್ತ ಸಮಯ",
            cropThreatsTitle = "ಸ್ಥಳೀಯ ಬೆಳೆ ರೋಗ ಎಚ್ಚರಿಕೆಗಳು",
            chatWelcome = "ಅಗ್ರೋಪಲ್ಸ್ AI ಗೆ ಸುಸ್ವಾಗತ! ನಾನು ನಿಮ್ಮ ಕೃಷಿ ವೈದ್ಯ. ಬೆಳೆ ರೋಗಗಳು, ಸಿಂಪಡಣೆ ಸಮಯ ಅಥವಾ ಔಷಧಿ ಪ್ರಮಾಣಗಳ ಬಗ್ಗೆ ಕೇಳಿ.",
            chatInputPlaceholder = "ಬೆಳೆ ಅಥವಾ ಔಷಧಿಗಳ ಬಗ್ಗೆ ಕೇಳಿ...",
            quickPrompts = listOf(
                "ಎಲೆ ಮಚ್ಚೆ ರೋಗವನ್ನು ಸಾವಯವವಾಗಿ ತಡೆಗಟ್ಟುವುದು ಹೇಗೆ?",
                "3.5 ಎಕರೆಗೆ ತಾಮ್ರದ ಶಿಲೀಂಧ್ರನಾಶಕ ಪ್ರಮಾಣ ಎಷ್ಟು?",
                "ಹೂಬಿಡುವ ಸಮಯದಲ್ಲಿ ಬೇವಿನ ಎಣ್ಣೆ ಸಿಂಪಡಿಸಬಹುದೇ?",
                "ಮೆಕ್ಕೆಜೋಳದ ಎಲೆ ಹಳದಿಯಾಗುವುದನ್ನು ತಡೆಯುವುದು ಹೇಗೆ?"
            )
        )

        AppLanguage.ML -> LocalizedStrings(
            appSubtitle = "സ്മാർട്ട് വിള ആരോഗ്യ-പ്രതിരോധ സംവിധാനം",
            navOverview = "അവലോകനം",
            navScan = "സ്കാനർ",
            navTreatments = "മരുന്നുകൾ",
            navWeather = "കാലാവസ്ഥ",
            navAdvisor = "കൃഷി ഉപദേശകൻ",
            plotActive = "തോട്ടം #4 സജീവം",
            heroTitle = "നിങ്ങളുടെ വിള സംരക്ഷിക്കുക",
            heroSubtitle = "ഇലകൾ സ്കാൻ ചെയ്ത് രോഗങ്ങളും കീടങ്ങളും പോഷകക്കുറവും ഉടനടി കണ്ടെത്തുക.",
            heroButton = "ക്യാമറ സ്കാനർ തുറക്കുക",
            cameraRealtimeActive = "AI ലൈവ് സ്കാനർ പ്രവർത്തനക്ഷമം",
            cameraPointHint = "ഇലകളിലെ പാടുകളിലോ തണ്ടിലോ ക്യാമറ കാണിക്കുക",
            inspectCure = "പരിശോധിച്ച് പരിഹരിക്കുക",
            askAi = "വിദഗ്ദ്ധനോട് ചോദിക്കുക",
            confidenceLabel = "കൃത്യത",
            canopyRiskLabel = "വിള അപകടസാധ്യത",
            storeTitle = "കാർഷിക മരുന്ന് സ്റ്റോർ",
            storeSubtitle = "സർട്ടിഫൈഡ് ജൈവ-കീടനാശിനികൾ",
            catalogTab = "ഉൽപ്പന്ന നിര",
            ordersTab = "എന്റെ ഓർഡറുകൾ",
            searchPlaceholder = "രോഗം തിരയുക (മുരടിപ്പ്, തുരുമ്പ്, മണ്ഡരി)...",
            dosageCalculatorTitle = "ഏക്കർ തോതിലുള്ള മരുന്ന് അളവ്",
            plotSizeLabel = "സ്ഥലത്തിന്റെ വിസ്തീർണ്ണം (ഏക്കർ)",
            dilutionWaterLabel = "ആവശ്യമായ വെള്ളം",
            recommendedPacksLabel = "ആവശ്യമായ പാക്കറ്റുകൾ",
            estimatedCostLabel = "ആകെ ചെലവ്",
            addToCartBtn = "കണക്കുകൂട്ടിയ അളവ് കാർട്ടിലേക്ക് ചേർക്കുക",
            cartTitle = "കാർഷിക സാമഗ്രികൾ",
            checkoutBtn = "ഓർഡർ തുടരുക",
            confirmOrderBtn = "ഡെലിവറി ഓർഡർ ചെയ്യുക",
            weatherTitle = "കാർഷിക കാലാവസ്ഥാ മുന്നറിയിപ്പ്",
            microclimateTitle = "തോട്ടത്തിലെ കാലാവസ്ഥാ വിവരങ്ങൾ",
            humidityLabel = "അന്തരീക്ഷ ഈർപ്പം",
            windVelocityLabel = "കാറ്റിന്റെ വേഗത",
            soilMoistureLabel = "മണ്ണിലെ ഈർപ്പം",
            rainChanceLabel = "മഴ സാധ്യത",
            hourlyWindowsTitle = "മരുന്ന് തളിക്കാൻ അനുയോജ്യമായ സമയം",
            cropThreatsTitle = "പ്രാദേശിക വിളരോഗ മുന്നറിയിപ്പുകൾ",
            chatWelcome = "അഗ്രോപൾസ് AI-ലേക്ക് സ്വാഗതം! ഞാൻ നിങ്ങളുടെ കൃഷി ഉപദേശകനാണ്. വിളരോഗങ്ങൾ, മരുന്ന് തളിക്കൽ എന്നിവയെക്കുറിച്ച് ചോദിക്കാം.",
            chatInputPlaceholder = "വിളകളെക്കുറിച്ചോ മരുന്നിനെക്കുറിച്ചോ ചോദിക്കുക...",
            quickPrompts = listOf(
                "ഇലപ്പുള്ളി രോഗം ജൈവരീതിയിൽ എങ്ങനെ നിയന്ത്രിക്കാം?",
                "3.5 ഏക്കറിന് എത്ര കോപ്പർ കുമിൾനാശിനി ആവശ്യമാണ്?",
                "പൂവിടുന്ന സമയത്ത് വേപ്പെണ്ണ തളിക്കുന്നത് സുരക്ഷിതമാണോ?",
                "മക്കച്ചോളത്തിന്റെ ഇല മഞ്ഞളിക്കുന്നത് എങ്ങനെ തടയാം?"
            )
        )

        AppLanguage.OR -> LocalizedStrings(
            appSubtitle = "ସ୍ମାର୍ଟ ଫସଲ ସ୍ୱାସ୍ଥ୍ୟ ଓ ପ୍ରତିକାର ପ୍ରଣାଳୀ",
            navOverview = "ସମୀକ୍ଷା",
            navScan = "ସ୍କାନର୍",
            navTreatments = "ଔଷଧ",
            navWeather = "ପାଣିପାଗ",
            navAdvisor = "କୃଷି ପରାମର୍ଶଦାତା",
            plotActive = "ଜମି #୪ ସକ୍ରିୟ",
            heroTitle = "ଆପଣଙ୍କ ଫସଲକୁ ସୁରକ୍ଷିତ ରଖନ୍ତୁ",
            heroSubtitle = "ପତ୍ର ସ୍କାନ କରି ରୋଗ, ପୋକ ଏବଂ ପୋଷକ ତତ୍ତ୍ୱର ଅଭାବ ତୁରନ୍ତ ଚିହ୍ନଟ କରନ୍ତୁ।",
            heroButton = "କ୍ୟାମେରା ସ୍କାନର୍ ଖୋଲନ୍ତୁ",
            cameraRealtimeActive = "AI ଲାଇଭ୍ ସ୍କାନର୍ ଚାଲୁଅଛି",
            cameraPointHint = "ପତ୍ରର ଦାଗ କିମ୍ବା କାଣ୍ଡ ଉପରେ କ୍ୟାମେରା ରଖନ୍ତୁ",
            inspectCure = "ପରୀକ୍ଷା ଓ ପ୍ରତିକାର",
            askAi = "ପରାମର୍ଶଦାତାଙ୍କୁ ପଚାରନ୍ତୁ",
            confidenceLabel = "ସଠିକତା",
            canopyRiskLabel = "ଫସଲ ପ୍ରତି ବିପଦ",
            storeTitle = "କୃଷି ଔଷଧ ଦୋକାନ",
            storeSubtitle = "ପ୍ରମାଣିତ ଜୈବିକ ଓ କୀଟନାଶକ ଔଷଧ",
            catalogTab = "ଔଷଧ ତାଲିକା",
            ordersTab = "ମୋର ଅର୍ଡର",
            searchPlaceholder = "ରୋଗ ଖୋଜନ୍ତୁ (ପତ୍ରପୋଡ଼ା, କଳଙ୍କୀ, ଜାଉପୋକ)...",
            dosageCalculatorTitle = "ଏକର ପିଛା ଔଷଧ ମାତ୍ରା କାଲକୁଲେଟର",
            plotSizeLabel = "ଜମିର ଆକାର (ଏକର)",
            dilutionWaterLabel = "ଆବଶ୍ୟକ ପାଣି",
            recommendedPacksLabel = "ଆବଶ୍ୟକ ପ୍ୟାକେଟ୍",
            estimatedCostLabel = "ଆନୁମାନିକ ମୋଟ ଖର୍ଚ୍ଚ",
            addToCartBtn = "ହିସାବ ଅନୁସାରେ କାର୍ଟରେ ଯୋଡ଼ନ୍ତୁ",
            cartTitle = "କୃଷି ସାମଗ୍ରୀ କାର୍ଟ",
            checkoutBtn = "ଅର୍ଡର ଆଗକୁ ବଢ଼ାନ୍ତୁ",
            confirmOrderBtn = "ଡେଲିଭରୀ ପାଇଁ ଅର୍ଡର କରନ୍ତୁ",
            weatherTitle = "କୃଷି ପାଣିପାଗ ଓ ଚେତାବନୀ",
            microclimateTitle = "ଜମିର ପାଣିପାଗ ତଥ୍ୟ",
            humidityLabel = "ବାୟୁମଣ୍ଡଳୀୟ ଆର୍ଦ୍ରତା",
            windVelocityLabel = "ପବନର ଗତି",
            soilMoistureLabel = "ମାଟିର ଓଦାଭାବ",
            rainChanceLabel = "ବର୍ଷା ସମ୍ଭାବନା",
            hourlyWindowsTitle = "ଔଷଧ ସ୍ପ୍ରେ କରିବା ପାଇଁ ଉପଯୁକ୍ତ ସମୟ",
            cropThreatsTitle = "ଆଞ୍ଚଳିକ ଫସଲ ରୋଗ ସୂଚନା",
            chatWelcome = "ଆଗ୍ରୋପଲ୍ସ AI କୁ ସ୍ୱାଗତ! ମୁଁ ଆପଣଙ୍କ କୃଷି ବିଶେଷଜ୍ଞ। ଫସଲ ରୋଗ, ସ୍ପ୍ରେ କରିବା ସମୟ ବା ଔଷଧ ମାତ୍ରା ବିଷୟରେ ପଚାରନ୍ତୁ।",
            chatInputPlaceholder = "ଫସଲ ବା ଔଷଧ ବିଷୟରେ ପଚାରନ୍ତୁ...",
            quickPrompts = listOf(
                "ପତ୍ରପୋଡ଼ା ରୋଗର ଜୈବିକ ନିୟନ୍ତ୍ରଣ କିପରି କରିବେ?",
                "୩.୫ ଏକର ପାଇଁ କପର କବକନାଶକର ପରିମାଣ କେତେ?",
                "ଫୁଲ ଆସିବା ସମୟରେ ନିମ୍ବ ତେଲ ସ୍ପ୍ରେ କରିବା ନିରାପଦ କି?",
                "ମକା ପତ୍ରର ହଳଦିଆ ଭାବ କିପରି ଦୂର କରିବେ?"
            )
        )
    }

    fun getSeverityLabel(severity: SeverityLevel, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> severity.label
        AppLanguage.HI -> when (severity) {
            SeverityLevel.HEALTHY -> "ઉત્કૃષ્ટ સ્વાસ્થ્ય (સ્વસ્થ)"
            SeverityLevel.MILD -> "हल्का खतरा (10-25%)"
            SeverityLevel.MODERATE -> "मध्यम खतरा (26-55%)"
            SeverityLevel.SEVERE -> "गंभीर खतरा (>55%)"
        }
        AppLanguage.PA -> when (severity) {
            SeverityLevel.HEALTHY -> "ਬਿਲਕੁਲ ਤੰਦਰੁਸਤ"
            SeverityLevel.MILD -> "ਹਲਕਾ ਖਤਰਾ (10-25%)"
            SeverityLevel.MODERATE -> "ਦਰਮਿਆਨਾ ਖਤਰਾ (26-55%)"
            SeverityLevel.SEVERE -> "ਗੰਭੀਰ ਖਤਰਾ (>55%)"
        }
        AppLanguage.MR -> when (severity) {
            SeverityLevel.HEALTHY -> "उत्कृष्ट आरोग्य (निरोगी)"
            SeverityLevel.MILD -> "किरकोळ धोका (10-25%)"
            SeverityLevel.MODERATE -> "मध्यम धोका (26-55%)"
            SeverityLevel.SEVERE -> "गंभीर धोका (>55%)"
        }
        AppLanguage.TE -> when (severity) {
            SeverityLevel.HEALTHY -> "ఆరోగ్యకరమైన పంట"
            SeverityLevel.MILD -> "స్వల్ప ముప్పు (10-25%)"
            SeverityLevel.MODERATE -> "మధ్యస్థ ముప్పు (26-55%)"
            SeverityLevel.SEVERE -> "తీవ్రమైన ముప్పు (>55%)"
        }
        AppLanguage.TA -> when (severity) {
            SeverityLevel.HEALTHY -> "ஆரோக்கியமான பயிர்"
            SeverityLevel.MILD -> "குறைந்த ஆபத்து (10-25%)"
            SeverityLevel.MODERATE -> "நடுத்தர ஆபத்து (26-55%)"
            SeverityLevel.SEVERE -> "கடுமையான ஆபத்து (>55%)"
        }
        AppLanguage.BN -> when (severity) {
            SeverityLevel.HEALTHY -> "সম্পূর্ণ সুস্থ ফসল"
            SeverityLevel.MILD -> "হালকা ঝুঁকি (১০-২৫%)"
            SeverityLevel.MODERATE -> "মাঝারি ঝুঁকি (২৬-৫৫%)"
            SeverityLevel.SEVERE -> "মারাত্মক ঝুঁকি (>৫৫%)"
        }
        AppLanguage.GU -> when (severity) {
            SeverityLevel.HEALTHY -> "ઉત્કૃષ્ટ સ્વાસ્થ્ય (સ્વસ્થ)"
            SeverityLevel.MILD -> "હળવું જોખમ (10-25%)"
            SeverityLevel.MODERATE -> "મધ્યમ જોખમ (26-55%)"
            SeverityLevel.SEVERE -> "ગંભીર જોખમ (>55%)"
        }
        AppLanguage.KN -> when (severity) {
            SeverityLevel.HEALTHY -> "ಉತ್ತಮ ಆರೋಗ್ಯ (ಆರೋಗ್ಯಕರ)"
            SeverityLevel.MILD -> "ಸ್ವಲ್ಪ ಅಪಾಯ (10-25%)"
            SeverityLevel.MODERATE -> "ಮಧ್ಯಮ ಅಪಾಯ (26-55%)"
            SeverityLevel.SEVERE -> "ಗಂಭೀರ ಅಪಾಯ (>55%)"
        }
        AppLanguage.ML -> when (severity) {
            SeverityLevel.HEALTHY -> "നല്ല ആരോഗ്യമുള്ള വിള"
            SeverityLevel.MILD -> "ചെറിയ ഭീഷണി (10-25%)"
            SeverityLevel.MODERATE -> "ഇടത്തരം ഭീഷണി (26-55%)"
            SeverityLevel.SEVERE -> "ഗുരുതരമായ ഭീഷണി (>55%)"
        }
        AppLanguage.OR -> when (severity) {
            SeverityLevel.HEALTHY -> "ସମ୍ପୂର୍ଣ୍ଣ ସୁସ୍ଥ ଫସଲ"
            SeverityLevel.MILD -> "ସାମାନ୍ୟ ବିପଦ (୧୦-୨୫%)"
            SeverityLevel.MODERATE -> "ମଧ୍ୟମ ବିପଦ (୨୬-୫୫%)"
            SeverityLevel.SEVERE -> "ଗମ୍ଭୀର ବିପଦ (>୫୫%)"
        }
    }

    fun getSprayConditionLabel(condition: SprayCondition, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> condition.label
        AppLanguage.HI -> when (condition) {
            SprayCondition.OPTIMAL -> "छिड़काव के लिए उत्तम समय"
            SprayCondition.CAUTION -> "मध्यम सावधानी रखें"
            SprayCondition.POOR -> "आज छिड़काव न करें"
        }
        AppLanguage.PA -> when (condition) {
            SprayCondition.OPTIMAL -> "ਸਪਰੇਅ ਲਈ ਵਧੀਆ ਸਮਾਂ"
            SprayCondition.CAUTION -> "ਸਾਵਧਾਨੀ ਵਰਤੋ"
            SprayCondition.POOR -> "ਅੱਜ ਸਪਰੇਅ ਨਾ ਕਰੋ"
        }
        AppLanguage.MR -> when (condition) {
            SprayCondition.OPTIMAL -> "फवारणीसाठी उत्तम वेळ"
            SprayCondition.CAUTION -> "सावधगिरी बाळगा"
            SprayCondition.POOR -> "आज फवारणी करू नका"
        }
        AppLanguage.TE -> when (condition) {
            SprayCondition.OPTIMAL -> "స్ప్రే చేయడానికి ఉత్తమ సమయం"
            SprayCondition.CAUTION -> "జాగ్రత్త వహించండి"
            SprayCondition.POOR -> "ఈరోజు స్ప్రే చేయవద్దు"
        }
        AppLanguage.TA -> when (condition) {
            SprayCondition.OPTIMAL -> "தெளிக்க சிறந்த நேரம்"
            SprayCondition.CAUTION -> "எச்சரிக்கையுடன் தெளிக்கவும்"
            SprayCondition.POOR -> "இன்று தெளிக்க வேண்டாம்"
        }
        AppLanguage.BN -> when (condition) {
            SprayCondition.OPTIMAL -> "স্প্রে করার উপযুক্ত সময়"
            SprayCondition.CAUTION -> "সতর্কতা অবলম্বন করুন"
            SprayCondition.POOR -> "আজ স্প্রে করবেন না"
        }
        AppLanguage.GU -> when (condition) {
            SprayCondition.OPTIMAL -> "છંટકાવ માટે ઉત્તમ સમય"
            SprayCondition.CAUTION -> "સાવચેતી રાખો"
            SprayCondition.POOR -> "આજે છંટકાવ ન કરવો"
        }
        AppLanguage.KN -> when (condition) {
            SprayCondition.OPTIMAL -> "ಸಿಂಪಡಣೆಗೆ ಅತ್ಯುತ್ತಮ ಸಮಯ"
            SprayCondition.CAUTION -> "ಎಚ್ಚರಿಕೆ ವಹಿಸಿ"
            SprayCondition.POOR -> "ಇಂದು ಸಿಂಪಡಿಸಬೇಡಿ"
        }
        AppLanguage.ML -> when (condition) {
            SprayCondition.OPTIMAL -> "മരുന്ന് തളിക്കാൻ ഏറ്റവും അനുയോജ്യം"
            SprayCondition.CAUTION -> "ജാഗ്രത പാലിക്കുക"
            SprayCondition.POOR -> "ഇന്ന് മരുന്ന് തളിക്കരുത്"
        }
        AppLanguage.OR -> when (condition) {
            SprayCondition.OPTIMAL -> "ଔଷଧ ସ୍ପ୍ରେ ପାଇଁ ଉତ୍ତମ ସମୟ"
            SprayCondition.CAUTION -> "ସାବଧାନତା ଅବଲମ୍ବନ କରନ୍ତୁ"
            SprayCondition.POOR -> "ଆଜି ସ୍ପ୍ରେ କରନ୍ତୁ ନାହିଁ"
        }
    }

    fun getGrowthLog(lang: AppLanguage): LocalizedStrings {
        val base = get(lang)
        return when (lang) {
            AppLanguage.EN -> base
            AppLanguage.HI -> base.copy(
                navGrowthLog = "वृद्धि लॉग",
                growthLogTitle = "फसल विकास लॉग",
                growthLogSubtitle = "तस्वीरों के साथ विकास टाइमलाइन",
                addFieldBtn = "+ नया खेत जोड़ें",
                logProgressBtn = "+ प्रगति फोटो जोड़ें",
                compareGrowthBtn = "विकास तुलना",
                growthStageLabel = "वृद्धि चरण",
                plantHeightLabel = "पौधे की ऊंचाई",
                healthScoreLabel = "स्वास्थ्य स्कोर",
                managementNotesLabel = "कृषि प्रबंधन नोट्स",
                saveMilestoneBtn = "माइलस्टोन सहेजें",
                noMilestonesHint = "इस खेत के लिए अभी कोई फोटो दर्ज नहीं है। नीचे '+ प्रगति फोटो जोड़ें' पर टैप करें।",
                comparisonTitle = "फसल विकास तुलना",
                growthDeltaLabel = "वृद्धि अंतर",
                daysElapsedLabel = "कुल दिन"
            )
            AppLanguage.PA -> base.copy(
                navGrowthLog = "ਵਿਕਾਸ ਲੌਗ",
                growthLogTitle = "ਫਸਲ ਵਿਕਾਸ ਲੌਗ",
                growthLogSubtitle = "ਤਸਵੀਰਾਂ ਨਾਲ ਵਿਕਾਸ ਟਾਈਮਲਾਈਨ",
                addFieldBtn = "+ ਨਵਾਂ ਖੇਤ ਸ਼ਾਮਲ ਕਰੋ",
                logProgressBtn = "+ ਤਰੱਕੀ ਫੋਟੋ ਦਰਜ ਕਰੋ",
                compareGrowthBtn = "ਵਿਕਾਸ ਦੀ ਤੁਲਨਾ",
                growthStageLabel = "ਵਿਕਾਸ ਪੜਾਅ",
                plantHeightLabel = "ਬੂਟੇ ਦਾ ਕੱਦ",
                healthScoreLabel = "ਸਿਹਤ ਸਕੋਰ",
                managementNotesLabel = "ਖੇਤੀ ਨੋਟਿਸ",
                saveMilestoneBtn = "ਮਾਈਲਸਟੋਨ ਸੁਰੱਖਿਅਤ ਕਰੋ",
                noMilestonesHint = "ਇਸ ਖੇਤ ਲਈ ਹਾਲੇ ਕੋਈ ਫੋਟੋ ਦਰਜ ਨਹੀਂ ਹੈ। ਹੇਠਾਂ ਦਿੱਤੇ ਬਟਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ।",
                comparisonTitle = "ਵਿਕਾਸ ਮਾਈਲਸਟੋਨ ਤੁਲਨਾ",
                growthDeltaLabel = "ਕੱਦ ਵਿੱਚ ਵਾਧਾ",
                daysElapsedLabel = "ਬੀਜਣ ਤੋਂ ਬਾਅਦ ਦਿਨ"
            )
            AppLanguage.MR -> base.copy(
                navGrowthLog = "वाढ नोंद",
                growthLogTitle = "पीक वाढ नोंदवही",
                growthLogSubtitle = "तपशीलवार फोटो व वाढीची टाइमलाइन",
                addFieldBtn = "+ नवीन शेत जोडा",
                logProgressBtn = "+ वाढीचा फोटो नोंदवा",
                compareGrowthBtn = "वाढीची तुलना",
                growthStageLabel = "वाढीची अवस्था",
                plantHeightLabel = "रोपाची उंची",
                healthScoreLabel = "आरोग्य गुण",
                managementNotesLabel = "कृषी नोंदी",
                saveMilestoneBtn = "टप्पा जतन करा",
                noMilestonesHint = "या शेतासाठी अद्याप कोणताही फोटो नोंदवलेला नाही. '+ वाढीचा फोटो नोंदवा' टॅप करा.",
                comparisonTitle = "पीक वाढ तुलना",
                growthDeltaLabel = "वाढीतील फरक",
                daysElapsedLabel = "पेरणीनंतरचे दिवस"
            )
            AppLanguage.TE -> base.copy(
                navGrowthLog = "వృద్ధి లాగ్",
                growthLogTitle = "పంట పెరుగుదల లాగ్",
                growthLogSubtitle = "ఫోటోలతో పంట పెరుగుదల కాలక్రమం",
                addFieldBtn = "+ కొత్త పొలం జోడించండి",
                logProgressBtn = "+ పురోగతి ఫోటో నమోదు",
                compareGrowthBtn = "పెరుగుదల పోలిక",
                growthStageLabel = "పెరుగుదల దశ",
                plantHeightLabel = "మొక్క ఎత్తు",
                healthScoreLabel = "ఆరోగ్య స్కోరు",
                managementNotesLabel = "వ్యవసాయ నోట్స్",
                saveMilestoneBtn = "మైలురాయిని సేవ్ చేయండి",
                noMilestonesHint = "ఈ పొలానికి ఇంకా ఎలాంటి ఫోటోలు నమోదు కాలేదు. '+ పురోగతి ఫోటో నమోదు' నొక్కండి.",
                comparisonTitle = "పంట పెరుగుదల పోలిక",
                growthDeltaLabel = "ఎత్తులో పెరుగుదల",
                daysElapsedLabel = "విత్తిన రోజుల సంఖ్య"
            )
            AppLanguage.TA -> base.copy(
                navGrowthLog = "வளர்ச்சி பதிவு",
                growthLogTitle = "பயிர் வளர்ச்சி பதிவு",
                growthLogSubtitle = "படங்களுடன் கூடிய வளர்ச்சி காலக்கோடு",
                addFieldBtn = "+ புதிய வயல் சேர்க்க",
                logProgressBtn = "+ வளர்ச்சி படம் பதிவுசெய்",
                compareGrowthBtn = "வளர்ச்சி ஒப்பீடு",
                growthStageLabel = "வளர்ச்சி நிலை",
                plantHeightLabel = "செடி உயரம்",
                healthScoreLabel = "ஆரோக்கிய மதிப்பீடு",
                managementNotesLabel = "வேளாண் குறிப்புகள்",
                saveMilestoneBtn = "மைல்கல்லைச் சேமிக்கவும்",
                noMilestonesHint = "இந்த வயலுக்கு இன்னும் படங்கள் பதிவு செய்யப்படவில்லை. '+ வளர்ச்சி படம் பதிவுசெய்' என்பதைத் தொடவும்.",
                comparisonTitle = "பயிர் வளர்ச்சி ஒப்பீடு",
                growthDeltaLabel = "வளர்ச்சி வித்தியாசம்",
                daysElapsedLabel = "விதைத்த நாட்கள்"
            )
            AppLanguage.BN -> base.copy(
                navGrowthLog = "वृद्धि লগ",
                growthLogTitle = "ফসলের বৃদ্ধি লগ",
                growthLogSubtitle = "ছবি সহ বৃদ্ধির টাইমলাইন",
                addFieldBtn = "+ নতুন জমি যুক্ত করুন",
                logProgressBtn = "+ অগ্রগতি ছবি তুলুন",
                compareGrowthBtn = "বৃদ্ধির তুলনা করুন",
                growthStageLabel = "বৃদ্ধির পর্যায়",
                plantHeightLabel = "গাছের উচ্চতা",
                healthScoreLabel = "স্বাস্থ্য স্কোর",
                managementNotesLabel = "কৃষি ব্যবস্থাপনা নোট",
                saveMilestoneBtn = "সংরক্ষণ করুন",
                noMilestonesHint = "এই জমির জন্য এখনও কোনো ছবি সংরক্ষিত নেই। নিচে '+ অগ্রগতি ছবি তুলুন' এ ট্যাপ করুন।",
                comparisonTitle = "ফসলের বৃদ্ধির তুলনা",
                growthDeltaLabel = "উচ্চতা বৃদ্ধি",
                daysElapsedLabel = "বপনের পর দিন"
            )
            AppLanguage.GU -> base.copy(
                navGrowthLog = "વિકાસ લોગ",
                growthLogTitle = "પાક વિકાસ લોગ",
                growthLogSubtitle = "ફોટા સાથે વિકાસ ટાઈમલાઈન",
                addFieldBtn = "+ નવું ખેતર ઉમેરો",
                logProgressBtn = "+ પ્રગતિ ફોટો નોંધો",
                compareGrowthBtn = "વિકાસની સરખામણી",
                growthStageLabel = "વિકાસ તબક્કો",
                plantHeightLabel = "છોડની ઊંચાઈ",
                healthScoreLabel = "સ્વાસ્થ્ય સ્કોર",
                managementNotesLabel = "ખેતી નોંધો",
                saveMilestoneBtn = "માઈલસ્ટોન સાચવો",
                noMilestonesHint = "આ ખેતર માટે હજી સુધી કોઈ ફોટો લેવામાં આવ્યો નથી. '+ પ્રગતિ ફોટો નોંધો' પર ટેપ કરો.",
                comparisonTitle = "પાક વિકાસ સરખામણી",
                growthDeltaLabel = "ઊંચાઈમાં વધારો",
                daysElapsedLabel = "વાવણી પછીના દિવસો"
            )
            AppLanguage.KN -> base.copy(
                navGrowthLog = "ಬೆಳವಣಿಗೆ ಲಾಗ್",
                growthLogTitle = "ಬೆಳೆ ಬೆಳವಣಿಗೆ ಲಾಗ್",
                growthLogSubtitle = "ಫೋಟೋಗಳೊಂದಿಗೆ ಬೆಳವಣಿಗೆಯ ಕಾಲಮಾನ",
                addFieldBtn = "+ ಹೊಸ ಜಮೀನು ಸೇರಿಸಿ",
                logProgressBtn = "+ ಪ್ರಗತಿ ಫೋಟೋ ದಾಖಲಿಸಿ",
                compareGrowthBtn = "ಬೆಳವಣಿಗೆ ಹೋಲಿಕೆ",
                growthStageLabel = "ಬೆಳವಣಿಗೆಯ ಹಂತ",
                plantHeightLabel = "ಗಿಡದ ಎತ್ತರ",
                healthScoreLabel = "ಆರೋಗ್ಯ ಸ್ಕೋರ್",
                managementNotesLabel = "ಕೃಷಿ ಟಿಪ್ಪಣಿಗಳು",
                saveMilestoneBtn = "ಮೈಲಿಗಲ್ಲು ಉಳಿಸಿ",
                noMilestonesHint = "ಈ ಜಮೀನಿಗೆ ಇನ್ನೂ ಯಾವುದೇ ಫೋಟೋ ದಾಖಲಾಗಿಲ್ಲ. '+ ಪ್ರಗತಿ ಫೋಟೋ ದಾಖಲಿಸಿ' ಟ್ಯಾಪ್ ಮಾಡಿ.",
                comparisonTitle = "ಬೆಳೆ ಬೆಳವಣಿಗೆ ಹೋಲಿಕೆ",
                growthDeltaLabel = "ಎತ್ತರದಲ್ಲಿ ಹೆಚ್ಚಳ",
                daysElapsedLabel = "ಬಿತ್ತನೆಯ ನಂತರದ ದಿನಗಳು"
            )
            AppLanguage.ML -> base.copy(
                navGrowthLog = "വളർച്ച ലോഗ്",
                growthLogTitle = "വിള വളർച്ചാ ലോഗ്",
                growthLogSubtitle = "ഫോട്ടോകൾ സഹിതമുള്ള വളർച്ചാ ടൈംലൈൻ",
                addFieldBtn = "+ പുതിയ കൃഷിയിടം ചേർക്കുക",
                logProgressBtn = "+ പുരോഗതി ഫോട്ടോ രേഖപ്പെടുത്തുക",
                compareGrowthBtn = "വളർച്ചാ താരതമ്യം",
                growthStageLabel = "വളർച്ചാ ഘട്ടം",
                plantHeightLabel = "ചെടിയുടെ ഉയരം",
                healthScoreLabel = "ആരോഗ്യ സ്കോർ",
                managementNotesLabel = "കാർഷിക കുറിപ്പുകൾ",
                saveMilestoneBtn = "രേഖപ്പെടുത്തുക",
                noMilestonesHint = "ഈ കൃഷിയിടത്തിൽ ഇതുവരെ ഫോട്ടോകൾ രേഖപ്പെടുത്തിയിട്ടില്ല. താഴെ ടാപ്പ് ചെയ്യുക.",
                comparisonTitle = "വിള വളർച്ച താരതമ്യം",
                growthDeltaLabel = "ഉയരത്തിലെ വർദ്ധനവ്",
                daysElapsedLabel = "വിത്ത് വിതച്ച ദിവസങ്ങൾ"
            )
            AppLanguage.OR -> base.copy(
                navGrowthLog = "ବୃଦ୍ଧି ଲଗ୍",
                growthLogTitle = "ଫସଲ ବୃଦ୍ଧି ଲଗ୍",
                growthLogSubtitle = "ଫଟୋ ସହିତ ବୃଦ୍ଧି ଟାଇମଲାଇନ୍",
                addFieldBtn = "+ ନୂତନ ଜମି ଯୋଡ଼ନ୍ତୁ",
                logProgressBtn = "+ ବୃଦ୍ଧି ଫଟୋ ରଖନ୍ତୁ",
                compareGrowthBtn = "ବୃଦ୍ଧି ତୁଳନା",
                growthStageLabel = "ବୃଦ୍ଧି ପର୍ଯ୍ୟାୟ",
                plantHeightLabel = "ଗଛର ଉଚ୍ଚତା",
                healthScoreLabel = "ସ୍ୱାସ୍ଥ୍ୟ ସ୍କୋର",
                managementNotesLabel = "କୃଷି ଟିପ୍ପଣୀ",
                saveMilestoneBtn = "ସାଇତନ୍ତୁ",
                noMilestonesHint = "ଏହି ଜମି ପାଇଁ କୌଣସି ଫଟୋ ରଖାଯାଇ ନାହିଁ। '+ ବୃଦ୍ଧି ଫଟୋ ରଖନ୍ତୁ' କ୍ଲିକ୍ କରନ୍ତୁ।",
                comparisonTitle = "ଫସଲ ବୃଦ୍ଧି ତୁଳନା",
                growthDeltaLabel = "ଉଚ୍ଚତା ବୃଦ୍ଧି",
                daysElapsedLabel = "ବୁଣିବା ପରଠାରୁ ଦିନ"
            )
        }
    }
}
