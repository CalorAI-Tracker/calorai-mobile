package dev.calorai.mobile.features.meal.domain.ml

import android.graphics.Bitmap

interface MealPhotoDetector {

    suspend fun detect(bitmap: Bitmap): Boolean
}
