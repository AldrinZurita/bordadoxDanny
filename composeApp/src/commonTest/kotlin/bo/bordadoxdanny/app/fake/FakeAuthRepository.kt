package bo.bordadoxdanny.app.fake

import bo.bordadoxdanny.app.features.profile.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthRepository(
    private val shouldReturnLoginError: Boolean = false,
    private val shouldReturnRegisterError: Boolean = false,
    private val duplicateEmail: String = "",
    private val duplicateUsername: String = "",
    private val registeredEmails: List<String> = listOf("usuario@gmail.com"),
    private val fakeUser: User = User(
        id = "fake-id",
        username = "usuario",
        email = "usuario@gmail.com",
        phoneCountryCode = "+591",
        phoneNumber = "71234567",
        firstName = "Usuario",
        middleName = null,
        lastName1 = "Demo",
        lastName2 = null,
        ciNumber = "1234567",
        ciComplement = null,
        ciDepartment = "LP",
        languageCode = "en",
        isVerified = false,
        syncStatus = "SYNCED"
    )
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(if (shouldReturnLoginError) null else fakeUser)
    private var storedCode: String = "123456"

    override suspend fun login(email: String, password: String): Result<String> {
        if (email.isBlank() || password.isBlank()) return Result.failure(Exception("field_required"))
        return if (shouldReturnLoginError) {
            Result.failure(Exception("Invalid credentials"))
        } else {
            _currentUser.value = fakeUser
            Result.success("fake-token")
        }
    }

    override suspend fun register(params: RegisterParams): Result<User> {
        if (params.email == duplicateEmail) return Result.failure(Exception("email_taken"))
        if (params.username == duplicateUsername) return Result.failure(Exception("username_taken"))
        return if (shouldReturnRegisterError) {
            Result.failure(Exception("Register error"))
        } else {
            val newUser = fakeUser.copy(id = "new-id", email = params.email, username = params.username)
            _currentUser.value = newUser
            Result.success(newUser)
        }
    }

    override suspend fun sendVerificationCode(email: String): Result<String> {
        val foundEmail = registeredEmails.find { it == email || fakeUser.username == email }
            ?: return Result.failure(Exception("User not found"))
        storedCode = "654321"
        return Result.success(foundEmail)
    }

    override suspend fun verifyCode(email: String, code: String): Result<Boolean> {
        return if (code == storedCode) Result.success(true)
        else Result.failure(Exception("Invalid code"))
    }

    override suspend fun resetPassword(email: String, newPassword: String): Result<Unit> = Result.success(Unit)

    override suspend fun logout(): Result<Unit> {
        _currentUser.value = null
        return Result.success(Unit)
    }

    override fun getCurrentUser(): Flow<User?> = _currentUser.asStateFlow()

    override suspend fun updateLanguage(userId: String, languageCode: String): Result<Unit> {
        val current = _currentUser.value
        if (current != null && current.id == userId) {
            _currentUser.value = current.copy(languageCode = languageCode)
        }
        return Result.success(Unit)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
