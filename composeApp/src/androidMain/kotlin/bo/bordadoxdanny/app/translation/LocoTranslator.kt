package bo.bordadoxdanny.app.translation

import android.util.Log
import bo.bordadoxdanny.app.di.AppConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LocoTranslationResponse(
    val id: String,
    val translated: Boolean,
    val translation: String
)

object LocoTranslator {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    suspend fun fetchTranslation(assetKey: String, localeCode: String): String {
        val apiKey = AppConfig.LOCO_API_KEY
        val url = "https://localise.biz/api/translations/$assetKey/$localeCode"
        
        Log.d("Loco", "Fetching: $url")

        return try {
            val response: LocoTranslationResponse = client.get(url) {
                header("Authorization", "Loco $apiKey")
            }.body()
            
            Log.d("Loco", "Success: ${response.translation}")
            response.translation
        } catch (e: Exception) {
            Log.e("Loco", "Error fetching translation for $assetKey: ${e.message}")
            assetKey // Fallback to key name
        }
    }
}
