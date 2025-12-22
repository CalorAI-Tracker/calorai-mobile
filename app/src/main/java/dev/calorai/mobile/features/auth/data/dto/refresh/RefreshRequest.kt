package dev.calorai.mobile.features.auth.data.dto.refresh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(

    @SerialName("refreshToken")
    val refreshToken: String,
)
