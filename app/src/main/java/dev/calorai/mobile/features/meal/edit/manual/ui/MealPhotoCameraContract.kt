package dev.calorai.mobile.features.meal.edit.manual.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import dev.calorai.mobile.BuildConfig
import java.io.File

class MealPhotoCameraContract : ActivityResultContract<Unit, Uri?>() {

    private var outputUri: Uri? = null

    override fun createIntent(context: Context, input: Unit): Intent {
        val uri = context.createMealPhotoUri()
        outputUri = uri

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return outputUri.takeIf { resultCode == Activity.RESULT_OK }
    }

    private fun Context.createMealPhotoUri(): Uri {
        val dir = File(cacheDir, MEAL_PHOTOS_DIR).apply { mkdirs() }
        val file = File.createTempFile(MEAL_PHOTO_PREFIX, MEAL_PHOTO_SUFFIX, dir)

        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            file,
        )
    }

    private companion object {
        private const val MEAL_PHOTOS_DIR = "meal_photos"
        private const val MEAL_PHOTO_PREFIX = "meal_"
        private const val MEAL_PHOTO_SUFFIX = ".jpg"
    }
}
