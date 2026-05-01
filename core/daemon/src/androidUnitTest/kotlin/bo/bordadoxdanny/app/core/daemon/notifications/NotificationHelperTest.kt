package bo.bordadoxdanny.app.core.daemon.notifications

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNotificationManager

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NotificationHelperTest {
    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var shadowNotificationManager: ShadowNotificationManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        shadowNotificationManager = shadowOf(notificationManager)
    }

    @Test
    fun `createChannel registers a channel on API 26 plus`() {
        NotificationHelper.createChannel(context)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel("daemon_channel")
            assertNotNull(channel)
            assertEquals("Daemon Alerts", channel.name)
        }
    }

    @Test
    fun `showRecoveryNotification posts notification with ID 1001`() {
        NotificationHelper.createChannel(context)
        NotificationHelper.showRecoveryNotification(context)
        
        val notifications = shadowNotificationManager.allNotifications
        assertEquals(1, notifications.size)
        val notification = notifications[0]
        // In Robolectric/Android, we can't easily get the ID from the notification object itself in all versions, 
        // but shadowNotificationManager keeps track.
        // Actually, shadowNotificationManager.getNotification(1001) is better.
        val postedNotification = shadowNotificationManager.getNotification(1001)
        assertNotNull(postedNotification)
        
        val shadowNotification = shadowOf(postedNotification)
        assertEquals("Connection restored", shadowNotification.contentTitle)
    }

    @Test
    fun `showFailureNotification posts notification with ID 1002 and includes formatted time in body`() {
        NotificationHelper.createChannel(context)
        val someEpochMillis = 1704067200000L // 2024-01-01 00:00:00 UTC
        NotificationHelper.showFailureNotification(context, someEpochMillis)
        
        val postedNotification = shadowNotificationManager.getNotification(1002)
        assertNotNull(postedNotification)
        
        val shadowNotification = shadowOf(postedNotification)
        val contentText = shadowNotification.contentText.toString()
        assertTrue(contentText.contains(Regex("\\d{2}:\\d{2}")))
    }

    @Test
    fun `notifications are NOT posted when POST_NOTIFICATIONS permission is denied`() {
        shadowOf(ApplicationProvider.getApplicationContext<Context>()).denyPermissions(Manifest.permission.POST_NOTIFICATIONS)
        
        NotificationHelper.showRecoveryNotification(context)
        NotificationHelper.showFailureNotification(context, System.currentTimeMillis())
        
        assertEquals(0, shadowNotificationManager.allNotifications.size)
    }
}
