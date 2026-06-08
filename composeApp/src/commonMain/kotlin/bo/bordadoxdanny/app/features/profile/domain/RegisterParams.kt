package bo.bordadoxdanny.app.features.profile.domain

data class RegisterParams(
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
