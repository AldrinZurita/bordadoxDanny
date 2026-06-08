package bo.bordadoxdanny.app.network

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/v1/auth/send-code")
    suspend fun sendVerificationCode(@Body request: SendCodeRequest): SendCodeResponse

    @POST("api/v1/auth/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequest): VerifyCodeResponse

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

    @POST("api/v1/fcm/save-token")
    suspend fun saveFcmToken(
        @Header("Authorization") token: String,
        @Body request: SaveFcmTokenRequest
    ): SaveFcmTokenResponse
}

@Serializable
data class LoginRequest(val emailOrUsername: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val user: UserDto)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val phoneCountryCode: String,
    val phoneNumber: String,
    val firstName: String,
    val middleName: String?,
    val lastName1: String,
    val lastName2: String?,
    val ciNumber: String,
    val ciComplement: String?,
    val ciDepartment: String
)

@Serializable
data class RegisterResponse(val userId: String, val email: String)

@Serializable
data class SendCodeRequest(val emailOrUsername: String)

@Serializable
data class SendCodeResponse(val message: String)

@Serializable
data class VerifyCodeRequest(val email: String, val code: String)

@Serializable
data class VerifyCodeResponse(val success: Boolean)

@Serializable
data class ResetPasswordRequest(val email: String, val newPassword: String)

@Serializable
data class ResetPasswordResponse(val success: Boolean)

@Serializable
data class SaveFcmTokenRequest(val token: String, val platform: String = "android")

@Serializable
data class SaveFcmTokenResponse(val success: Boolean)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName1: String
)
