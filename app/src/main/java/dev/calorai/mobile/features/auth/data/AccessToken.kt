package dev.calorai.mobile.features.auth.data

@JvmInline
value class AccessToken(val value: String)

fun AccessToken.toBearerHeader(): String = "Bearer ${this.value}"

const val BEARER_HEADER = "Authorization"
