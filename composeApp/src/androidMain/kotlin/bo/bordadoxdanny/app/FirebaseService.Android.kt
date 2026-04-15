package bo.bordadoxdanny.app


import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class FirebaseService actual constructor() {

    actual suspend fun getToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val exception = task.exception ?: Exception("Error desconocido al obtener FCM Token")
                    Log.e("FirebaseService", "FCM token registration failed", exception)
                    continuation.resumeWithException(exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FirebaseService", "Token obtenido: $token")
                continuation.resume(token ?: "")
            }
    }
}