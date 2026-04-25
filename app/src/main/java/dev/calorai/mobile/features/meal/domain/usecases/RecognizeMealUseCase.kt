package dev.calorai.mobile.features.meal.domain.usecases

import android.content.Context
import android.net.Uri
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealRecognizeEntry

interface RecognizeMealUseCase {

    suspend operator fun invoke(uri: Uri): MealRecognizeEntry
}

class RecognizeMealUseCaseImpl constructor(
    private val context: Context,
    private val repository: MealRepository,
) : RecognizeMealUseCase {

    override suspend fun invoke(uri: Uri): MealRecognizeEntry {
        val contentResolver = context.contentResolver
        val inputStream = requireNotNull(contentResolver.openInputStream(uri))
        return inputStream.use { inputStream ->
            repository.mealRecognize(image = inputStream.readBytes())
        }
    }
}
