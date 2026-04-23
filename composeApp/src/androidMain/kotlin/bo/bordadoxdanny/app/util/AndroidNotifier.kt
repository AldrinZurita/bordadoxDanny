package bo.bordadoxdanny.app.util

import android.content.Context

class AndroidNotifier(private val context: Context) : Notifier {
    override fun showNotification(title: String, body: String) {
        NotificationHelper.showNotification(context, title, body)
    }
}
