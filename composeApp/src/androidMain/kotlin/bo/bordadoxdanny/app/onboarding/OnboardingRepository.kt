package bo.bordadoxdanny.app.onboarding

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OnboardingRepository(
    private val remoteConfig: FirebaseRemoteConfig
) {
    suspend fun getSlides(languageCode: String): List<OnboardingSlide> = suspendCoroutine { continuation ->
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val configJson = remoteConfig.getString("onboarding_config")
                val slides = mutableListOf<OnboardingSlide>()
                try {
                    val jsonArray = JSONArray(configJson)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        
                        val id = jsonObject.getInt("id")
                        
                        val titleObj = jsonObject.getJSONObject("title")
                        val title = if (titleObj.has(languageCode)) titleObj.getString(languageCode) else titleObj.getString("en")
                        
                        val descObj = jsonObject.getJSONObject("description")
                        val description = if (descObj.has(languageCode)) descObj.getString(languageCode) else descObj.getString("en")
                        
                        val imgObj = jsonObject.getJSONObject("image_url")
                        val imageUrl = if (imgObj.has(languageCode)) imgObj.getString(languageCode) else imgObj.getString("en")
                        
                        slides.add(OnboardingSlide(id, title, description, imageUrl))
                    }
                    continuation.resume(slides)
                } catch (e: Exception) {
                    Log.e("OnboardingRepository", "Error parsing onboarding JSON", e)
                    continuation.resume(emptyList())
                }
            } else {
                Log.e("OnboardingRepository", "Fetch failed: ${task.exception?.message}")
                continuation.resume(emptyList())
            }
        }
    }
}
