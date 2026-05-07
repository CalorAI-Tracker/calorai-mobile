package dev.calorai.mobile.features.meal.data.ml

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImagePreprocessor {

    fun preprocess(bitmap: Bitmap): ByteBuffer {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_WIDTH, INPUT_HEIGHT, true)

        val inputBuffer = ByteBuffer.allocateDirect(
            INPUT_WIDTH * INPUT_HEIGHT * CHANNELS_COUNT * FLOAT_SIZE_BYTES
        ).apply {
            order(ByteOrder.nativeOrder())
        }

        val pixels = IntArray(INPUT_WIDTH * INPUT_HEIGHT)
        resizedBitmap.getPixels(
            pixels,
            0,
            INPUT_WIDTH,
            0,
            0,
            INPUT_WIDTH,
            INPUT_HEIGHT
        )

        var pixelIndex = 0
        repeat(INPUT_HEIGHT) {
            repeat(INPUT_WIDTH) {
                val pixel = pixels[pixelIndex++]

                val r = ((pixel shr 16) and 0xFF).toFloat() / NORMALIZATION_FACTOR
                val g = ((pixel shr 8) and 0xFF).toFloat() / NORMALIZATION_FACTOR
                val b = (pixel and 0xFF).toFloat() / NORMALIZATION_FACTOR

                inputBuffer.putFloat(r)
                inputBuffer.putFloat(g)
                inputBuffer.putFloat(b)
            }
        }

        inputBuffer.rewind()
        return inputBuffer
    }

    private companion object {
        private const val INPUT_WIDTH = 640
        private const val INPUT_HEIGHT = 640
        private const val CHANNELS_COUNT = 3
        private const val FLOAT_SIZE_BYTES = 4
        private const val NORMALIZATION_FACTOR = 255f
    }
}
