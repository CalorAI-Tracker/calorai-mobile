package dev.calorai.mobile.core.uikit.contextMenu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

interface PrimaryContextMenuItem<KEY> {
    val key: KEY

    @get:StringRes val titleRes: Int
    @get:DrawableRes val iconRes: Int

    @get:Composable val titleColor: Color
    @get:Composable val iconColor: Color
}
