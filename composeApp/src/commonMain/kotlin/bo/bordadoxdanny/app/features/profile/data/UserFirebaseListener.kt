package bo.bordadoxdanny.app.features.profile.data

import bo.bordadoxdanny.app.firebase.FirebaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserFirebaseListener(
    private val firebaseManager: FirebaseManager,
    private val userDao: UserDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    fun startListening(userId: String) {
        firebaseManager.observeData("users/$userId", UserDto::class)
            .onEach { dto ->
                dto?.let {
                    userDao.upsert(it.toEntity())
                }
            }
            .launchIn(scope)
    }
}
