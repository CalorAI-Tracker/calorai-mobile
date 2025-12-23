package dev.calorai.mobile.core.utils

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.util.Locale

val Context.locale: Locale
    get() {
        val locales = ConfigurationCompat.getLocales(resources.configuration)
        return locales[0] ?: Locale.getDefault()
    }
