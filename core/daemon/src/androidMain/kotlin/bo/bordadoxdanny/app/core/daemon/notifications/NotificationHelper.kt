package bo.bordadoxdanny.app.core.daemon.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

actual object NotificationHelper {
    private const val CHANNEL_ID = "daemon_channel"
    private const val RECOVERY_ID = 1001
    private const val FAILURE_ID = 1002

    actual fun createChannel(context: Any?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ctx = context as Context
            val name = "Daemon Alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    actual fun showRecoveryNotification(context: Any?) {
        val ctx = context as Context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.w("NOTIFY", "POST_NOTIFICATIONS not granted, skipping")
                return
            }
        }
        
        try {
            NotificationManagerCompat.from(ctx).notify(RECOVERY_ID, buildRecoveryNotification(ctx))
        } catch (e: SecurityException) {
            Log.e("NOTIFY", "Permission missing for notification", e)
        }
    }

    actual fun showFailureNotification(context: Any?, missedSinceMillis: Long) {
        val ctx = context as Context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.w("NOTIFY", "POST_NOTIFICATIONS not granted, skipping")
                return
            }
        }

        try {
            NotificationManagerCompat.from(ctx).notify(FAILURE_ID, buildFailureNotification(ctx, missedSinceMillis))
        } catch (e: SecurityException) {
            Log.e("NOTIFY", "Permission missing for notification", e)
        }
    }

    private fun buildRecoveryNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Connection restored")
            .setContentText("Auto-repair completed. Sync queue has been reset.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }

    private fun buildFailureNotification(context: Context, missedSinceMillis: Long): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Device heartbeat lost")
            .setContentText("No signal detected since ${formatTime(missedSinceMillis)}. Attempting auto-repair…")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }

    private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}
