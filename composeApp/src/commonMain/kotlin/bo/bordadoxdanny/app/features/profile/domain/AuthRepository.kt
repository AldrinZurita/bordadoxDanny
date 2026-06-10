package bo.bordadoxdanny.app.features.profile.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(params: RegisterParams): Result<User>
    suspend fun sendVerificationCode(email: String): Result<String>
    suspend fun verifyCode(email: String, code: String): Result<Boolean>
    suspend fun resetPassword(email: String, newPassword: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
    suspend fun updateLanguage(userId: String, languageCode: String): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
}
