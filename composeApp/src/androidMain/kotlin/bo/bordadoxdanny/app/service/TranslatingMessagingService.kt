package bo.bordadoxdanny.app.service

import android.util.Log
import bo.bordadoxdanny.app.features.settings.data.UserPreferencesDao
import bo.bordadoxdanny.app.translation.LocoTranslator
import bo.bordadoxdanny.app.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class TranslatingMessagingService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val preferencesDao: UserPreferencesDao by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Data message received — keys: ${remoteMessage.data.keys}")

        // Guard: only handle data-only messages (no "notification" key)
        if (remoteMessage.notification != null) {
            Log.w("FCM", "Received notification payload. Automatic translation skipped.")
            return
        }

        val titleKey = remoteMessage.data["titleKey"]
        val bodyKey  = remoteMessage.data["bodyKey"]

        if (titleKey == null || bodyKey == null) {
            Log.w("FCM", "Missing titleKey or bodyKey in data payload.")
            return
        }

        serviceScope.launch {
            try {
                // 1. Read stored locale (matches Loco locale codes exactly)
                val prefs = preferencesDao.getPreferencesSync()
                val localeCode = prefs?.languageCode ?: "en-US"
                Log.d("Room", "Language read: $localeCode")

                // 2. Fetch translations from Loco (parallel)
                val translatedTitle = async { LocoTranslator.fetchTranslation(titleKey, localeCode) }
                val translatedBody  = async { LocoTranslator.fetchTranslation(bodyKey, localeCode) }

                val title = translatedTitle.await()
                val body = translatedBody.await()
                
                Log.d("Notif", "Showing notification: $title")

                // 3. Display translated notification
                NotificationHelper.showNotification(
                    context = applicationContext,
                    title = title,
                    body = body
                )
            } catch (e: Exception) {
                Log.e("FCM", "Unexpected error in service: ${e.message}")
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "New token: $token")
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
