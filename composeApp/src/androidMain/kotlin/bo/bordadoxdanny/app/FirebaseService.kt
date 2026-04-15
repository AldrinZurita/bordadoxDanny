package bo.bordadoxdanny.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM_LOG"
        private const val CHANNEL_ID = "fcm_default_channel"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "📩 ¡Mensaje recibido de: ${remoteMessage.from}!")

        // 1. Extraer datos (si existen)
        val dataTitle = remoteMessage.data["title"]
        val dataBody = remoteMessage.data["body"]
        val route = remoteMessage.data["open_screen"]

        // 2. Extraer notificación (si existe)
        val notification = remoteMessage.notification
        
        val finalTitle = notification?.title ?: dataTitle ?: "Bordados Danny"
        val finalBody = notification?.body ?: dataBody ?: "Tienes un nuevo mensaje"

        Log.d(TAG, "Contenido: $finalTitle - $finalBody - Ruta: $route")
        showNotification(finalTitle, finalBody, route)
    }

    private fun showNotification(title: String, message: String, route: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones Importantes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para alertas de la app"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_screen", route ?: "github")
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo Token generado: $token")
    }
}
