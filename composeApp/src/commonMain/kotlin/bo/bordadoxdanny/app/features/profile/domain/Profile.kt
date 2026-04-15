package bo.bordadoxdanny.app.features.profile.domain

data class Profile(
    val id: Long = 1,
    val name: String,
    val email: String,
    val phone: String
)
