package bo.bordadoxdanny.app.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class SecurityManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) = sharedPrefs.edit { putString("auth_token", token) }
    fun getToken(): String? = sharedPrefs.getString("auth_token", null)
    
    fun saveDemoPassword(password: String) = sharedPrefs.edit { putString("demo_password", password) }
    fun getDemoPassword(): String = sharedPrefs.getString("demo_password", "Usuario123") ?: "Usuario123"

    fun clear() = sharedPrefs.edit { 
        val demoPass = getDemoPassword()
        clear()
        saveDemoPassword(demoPass) // Keep the demo password even after clear
    }
}
