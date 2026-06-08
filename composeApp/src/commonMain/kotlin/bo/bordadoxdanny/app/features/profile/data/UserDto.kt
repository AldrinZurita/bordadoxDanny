package bo.bordadoxdanny.app.features.profile.data

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val phoneCountryCode: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val middleName: String? = null,
    val lastName1: String = "",
    val lastName2: String? = null,
    val ciNumber: String = "",
    val ciComplement: String? = null,
    val ciDepartment: String = "",
    val languageCode: String = "en",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
