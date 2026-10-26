package com.example.model

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val regionHint: String,
    val promptInstruction: String
) {
    EN("en", "English", "English", "Global / All Regions", "English"),
    HI("hi", "हिन्दी", "Hindi", "North & Central (UP, MP, Bihar, Rajasthan, Haryana)", "Hindi (हिन्दी script)"),
    PA("pa", "ਪੰਜਾਬੀ", "Punjabi", "Punjab & Northern Plains", "Punjabi (Gurmukhi ਪੰਜਾਬੀ script)"),
    MR("mr", "मराठी", "Marathi", "Maharashtra (Vidarbha, Marathwada, Western)", "Marathi (मराठी script)"),
    TE("te", "తెలుగు", "Telugu", "Andhra Pradesh & Telangana", "Telugu (తెలుగు script)"),
    TA("ta", "தமிழ்", "Tamil", "Tamil Nadu & Cauvery Delta", "Tamil (தமிழ் script)"),
    BN("bn", "বাংলা", "Bengali", "West Bengal, Assam & East", "Bengali (বাংলা script)"),
    GU("gu", "ગુજરાતી", "Gujarati", "Gujarat (Saurashtra, Central)", "Gujarati (ગુજરાતી script)"),
    KN("kn", "ಕನ್ನಡ", "Kannada", "Karnataka (Deccan & Coastal)", "Kannada (ಕನ್ನಡ script)"),
    ML("ml", "മലയാളം", "Malayalam", "Kerala (High Ranges & Coastal)", "Malayalam (മലയാളം script)"),
    OR("or", "ଓଡ଼ିଆ", "Odia", "Odisha (Coastal & Western)", "Odia (ଓଡ଼ିଆ script)")
}
