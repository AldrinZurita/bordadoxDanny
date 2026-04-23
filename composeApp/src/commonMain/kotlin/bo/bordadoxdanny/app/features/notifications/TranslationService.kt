package bo.bordadoxdanny.app.features.notifications

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class TranslationService(private val httpClient: HttpClient, private val apiKey: String) {
    
    suspend fun translate(assetId: String, targetLang: String): String {
        // 1. Default English values (Fallback)
        val englishDefault = when(assetId) {
            "title" -> "New Order Alert"
            "body" -> "A new embroidery request has arrived."
            else -> assetId
        }

        // 2. If target is English or API Key is missing, return default
        if (targetLang.startsWith("en") || apiKey.isEmpty()) return englishDefault

        // 3. Try to fetch the translation from Localise.biz Export API
        return try {
            // Localise.biz usually uses 'es' for Spanish (Spain)
            val shortLang = targetLang.substringBefore("-").lowercase()
            
            // The Export API is the most reliable way to get translations
            // Endpoint: /api/export/locale/{locale}.json
            val url = "https://localise.biz/api/export/locale/$shortLang.json"
            println("BORDADOS_TRANS: Exporting locale '$shortLang' from $url")

            val response = httpClient.get(url) {
                parameter("key", apiKey)
            }

            if (response.status == HttpStatusCode.OK) {
                // Parse the full JSON object (e.g., { "title": "...", "body": "..." })
                val json = response.body<JsonObject>()
                val translated = json[assetId]?.jsonPrimitive?.content
                
                if (!translated.isNullOrEmpty()) {
                    println("BORDADOS_TRANS: ✅ SUCCESS ($shortLang) -> '$translated'")
                    return translated
                } else {
                    println("BORDADOS_TRANS: ⚠️ ID '$assetId' not found in exported JSON")
                }
            } else {
                println("BORDADOS_TRANS: ❌ Export API Error ${response.status}")
            }
            
            englishDefault
        } catch (e: Exception) {
            println("BORDADOS_TRANS: 🔥 Error: ${e.message}")
            englishDefault
        }
    }
}
