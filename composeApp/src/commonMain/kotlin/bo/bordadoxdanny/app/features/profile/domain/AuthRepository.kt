package bo.bordadoxdanny.app.features.profile.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(emailOrUsername: String, password: String): Result<User>
    suspend fun register(params: RegisterParams): Result<User>
    suspend fun sendVerificationCode(emailOrUsername: String): Result<String> // Returns the actual email
    suspend fun verifyCode(email: String, code: String): Result<Unit>
    suspend fun resetPassword(email: String, newPassword: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
    suspend fun updateLanguage(userId: String, languageCode: String): Result<Unit>
}
