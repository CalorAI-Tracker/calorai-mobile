package dev.calorai.mobile.features.meal.data.ml

import android.content.Context
import android.graphics.Bitmap
import dev.calorai.mobile.features.meal.domain.ml.MealPhotoDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MealPhotoDetectorImpl(
    private val context: Context,
    private val imagePreprocessor: ImagePreprocessor,
) : MealPhotoDetector {

    private val interpreter: Interpreter by lazy {
        Interpreter(loadModelFile())
    }

    override suspend fun detect(bitmap: Bitmap): Boolean = withContext(Dispatchers.Default) {
        val inputBuffer = imagePreprocessor.preprocess(bitmap)
        val output = Array(1) { Array(DETECTIONS_COUNT) { FloatArray(DETECTION_VALUES_COUNT) } }

        interpreter.run(inputBuffer, output)

        output[0].any { detection ->
            detection[SCORE_INDEX] > SCORE_THRESHOLD
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(MODEL_NAME)

        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.startOffset,
                fileDescriptor.declaredLength
            )
        }
    }

    private companion object {
        private const val MODEL_NAME = "food_object_detector.tflite"
        private const val DETECTIONS_COUNT = 300
        private const val DETECTION_VALUES_COUNT = 6
        private const val SCORE_INDEX = 4
        private const val SCORE_THRESHOLD = 0.6f
    }
}
