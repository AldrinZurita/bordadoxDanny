package bo.bordadoxdanny.app.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import bo.bordadoxdanny.app.BuildConfig

/**
 * RemoteConfig Keys:
 * - show_accounts_receivable: Boolean (default: true)
 * - max_accounts_receivable_display: Long (default: 10)
 */
class RemoteConfigManagerImpl : RemoteConfigManager {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.IS_DEBUG) 0 else 3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(
            "show_accounts_receivable" to true,
            "max_accounts_receivable_display" to 10L
        ))
        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }

    override fun getShowAccountsReceivable(): Boolean {
        return remoteConfig.getBoolean("show_accounts_receivable")
    }

    override fun getMaxAccountsReceivable(): Long {
        return remoteConfig.getLong("max_accounts_receivable_display")
    }
}
