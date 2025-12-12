package dev.calorai.mobile.features.main.data.dto.dailyNutrition.getDailyStats

data class GetDailyStatsResponse(
    val date: String,
"plan": {
    "kcal": 2300,
    "proteinG": 160,
    "fatG": 70,
    "carbsG": 250
},
"actual": {
    "kcal": 2300,
    "proteinG": 160,
    "fatG": 70,
    "carbsG": 250
},
"remaining": {
    "kcal": 2300,
    "proteinG": 160,
    "fatG": 70,
    "carbsG": 250
},
"completionPercent": {
    // TODO: Узнать что это, и поменять 
    "additionalProp1": 0,
    "additionalProp2": 0,
    "additionalProp3": 0
}
)
