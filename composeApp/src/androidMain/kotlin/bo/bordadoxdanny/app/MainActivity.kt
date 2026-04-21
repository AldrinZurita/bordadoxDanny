package bo.bordadoxdanny.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import bo.bordadoxdanny.app.data.database.AppDatabase
import bo.bordadoxdanny.app.firebase.FirebaseManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val TAG = "DEBUG_BORDADOS"
    
    // Instancia inyectada por Koin (Singleton)
    private val database: AppDatabase by inject()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d(TAG, "Permiso de notificaciones concedido: $isGranted")
        if (isGranted) {
            createNotificationChannel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            ContextProvider.init(applicationContext)
            enableEdgeToEdge()

            if (FirebaseApp.getApps(this).isNotEmpty()) {
                Log.d(TAG, "Firebase está inicializado")
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) Log.d(TAG, "FCM TOKEN ACTUAL: ${task.result}")
                }
                setupRemoteConfig()
            }

            // ELIMINADO: runRoomTest() - Evitamos competencia con WorkManager al iniciar
            
            askNotificationPermission()
            createNotificationChannel()

            setContent {
                App()
            }

        } catch (e: Exception) {
            Log.e(TAG, "CRASH EN ONCREATE: ${e.message}")
            setContent { Text("Error crítico: ${e.message}") }
        }
    }

    private fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) 
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val cloudValue = remoteConfig.getString("test_message")
                Log.d(TAG, "☁️ REMOTE CONFIG: $cloudValue")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = MyFcmService.CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(MyFcmService.CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
