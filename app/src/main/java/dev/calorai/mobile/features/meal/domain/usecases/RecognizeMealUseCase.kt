package dev.calorai.mobile.features.meal.domain.usecases

import android.content.Context
import android.net.Uri
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealRecognizeResult

interface RecognizeMealUseCase {

    suspend operator fun invoke(uri: Uri): MealRecognizeResult
}

class RecognizeMealUseCaseImpl constructor(
    private val context: Context,
    private val repository: MealRepository,
) : RecognizeMealUseCase {

    override suspend fun invoke(uri: Uri): MealRecognizeResult {
        val contentResolver = context.contentResolver
        val inputStream = requireNotNull(contentResolver.openInputStream(uri))
        return inputStream.use { inputStream ->
            repository.mealRecognize(image = inputStream.readBytes())
        }
    }
}
