package bo.bordadoxdanny.app.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

actual class RemoteConfigManager actual constructor() {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        setConfigSettingsAsync(configSettings)
    }

    actual suspend fun fetchAndActivate(): Boolean {
        return try {

            remoteConfig.fetchAndActivate().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    actual fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    actual fun getAllConfigs(): Map<String, String> {
        val all = remoteConfig.all
        return all.mapValues { it.value.asString() }
    }
}
