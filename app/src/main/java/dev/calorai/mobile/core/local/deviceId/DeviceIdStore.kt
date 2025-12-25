package dev.calorai.mobile.core.local.deviceId

import android.content.Context
import java.util.UUID

interface DeviceIdStore {

    suspend fun getDeviceId(): DeviceId
}

class DeviceIdStoreImpl constructor(
    context: Context,
) : DeviceIdStore {

    private companion object {
        private const val DEVICE_ID_PREFS = "device_id_prefs"
        private const val DEVICE_ID_KEY = "device_id"
    }

    private val prefs by lazy {
        context.getSharedPreferences(DEVICE_ID_PREFS, Context.MODE_PRIVATE)
    }

    override suspend fun getDeviceId(): DeviceId {
        return prefs.getString(DEVICE_ID_KEY, null)?.let { DeviceId(it) } ?: run {
            val newDeviceId = generateDeviceId()
            prefs.edit().apply {
                putString(DEVICE_ID_KEY, newDeviceId.value)
                apply()
            }
            newDeviceId
        }
    }

    private fun generateDeviceId(): DeviceId = DeviceId(UUID.randomUUID().toString())
}
