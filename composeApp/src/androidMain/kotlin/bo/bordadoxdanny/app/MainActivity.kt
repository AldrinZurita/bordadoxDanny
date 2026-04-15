package bo.bordadoxdanny.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("FCM_LOG", "Permiso de notificaciones concedido: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. Verificar inicialización de Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.e("FCM_LOG", "❌ Firebase NO inicializado. Revisa tu google-services.json")
        } else {
            Log.d("FCM_LOG", "✅ Firebase inicializado correctamente")
            
            // OPCIÓN 1: Suscribirse a un tema para campañas globales instantáneas
            FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_LOG", "✅ Suscrito al tema 'all' con éxito")
                    } else {
                        Log.e("FCM_LOG", "❌ Error al suscribirse al tema")
                    }
                }
        }

        // 2. Pedir permisos (Android 13+)
        askNotificationPermission()

        // 3. Capturar ruta inicial si la app se abre desde una notificación
        val initialRoute = intent.getStringExtra("open_screen")
        Log.d("FCM_LOG", "Ruta inicial detectada: $initialRoute")

        // 4. Intentar obtener el Token con Logs detallados
        lifecycleScope.launch {
            try {
                Log.d("FCM_LOG", "⏳ Solicitando Token...")
                val token = getFirebaseToken()
                if (token != null) {
                    Log.d("FCM_LOG", "🚀 TU TOKEN FCM ES: $token")
                } else {
                    Log.e("FCM_LOG", "⚠️ El token regresó vacío (null)")
                }
            } catch (e: Exception) {
                Log.e("FCM_LOG", "❌ Error al obtener token: ${e.message}")
            }
        }

        setContent {
            val startScreen = remember { mutableStateOf(initialRoute) }
            
            App()

            LaunchedEffect(startScreen.value) {
                startScreen.value?.let {
                    Log.d("FCM_LOG", "Intentando navegar a: $it")
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val route = intent.getStringExtra("open_screen")
        if (route != null) {
            Log.d("FCM_LOG", "🔥 Nueva notificación detectada (onNewIntent)! Ruta: $route")
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

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
