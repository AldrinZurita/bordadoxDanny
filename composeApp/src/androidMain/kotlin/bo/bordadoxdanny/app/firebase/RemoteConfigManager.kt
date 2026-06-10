package bo.bordadoxdanny.app.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import bo.bordadoxdanny.app.BuildConfig
import android.util.Log

class RemoteConfigManagerImpl : RemoteConfigManager {
    private val remoteConfig: FirebaseRemoteConfig? by lazy {
        try {
            FirebaseRemoteConfig.getInstance()
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Failed to get FirebaseRemoteConfig instance: ${e.message}")
            null
        }
    }

    init {
        setupConfig()
    }

    private fun setupConfig() {
        val config = remoteConfig ?: return
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.IS_DEBUG) 0 else 3600)
            .build()
        config.setConfigSettingsAsync(configSettings)
        config.setDefaultsAsync(mapOf(
            "show_accounts_receivable" to true,
            "max_accounts_receivable_display" to 10L
        ))
        config.fetchAndActivate()
    }

    override fun getShowAccountsReceivable(): Boolean {
        return remoteConfig?.getBoolean("show_accounts_receivable") ?: true
    }

    override fun getMaxAccountsReceivable(): Long {
        return remoteConfig?.getLong("max_accounts_receivable_display") ?: 10L
    }
}
