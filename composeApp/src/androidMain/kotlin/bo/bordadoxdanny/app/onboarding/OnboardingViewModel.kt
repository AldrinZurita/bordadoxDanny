package bo.bordadoxdanny.app.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val _slides = MutableStateFlow<List<OnboardingSlide>>(emptyList())
    val slides: StateFlow<List<OnboardingSlide>> = _slides.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    val isFirstSlide: Boolean
        get() = currentIndex.value == 0

    val isLastSlide: Boolean
        get() = currentIndex.value == slides.value.lastIndex

    fun loadSlides() {
        val lang = Locale.getDefault().language.let {
            if (it in listOf("es", "en", "fr")) it else "en"
        }

        viewModelScope.launch {
            val repository = OnboardingRepository(FirebaseRemoteConfig.getInstance())
            _slides.value = repository.getSlides(lang)
        }
    }

    fun next() {
        if (!isLastSlide) {
            _currentIndex.value++
        }
    }

    fun previous() {
        if (!isFirstSlide) {
            _currentIndex.value--
        }
    }

    fun setCompleted() {
        viewModelScope.launch {
            OnboardingPreferences.setOnboardingCompleted(getApplication())
        }
    }
}
