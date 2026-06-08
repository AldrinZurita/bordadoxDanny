package bo.bordadoxdanny.app.features.profile.presentation

data class PasswordRequirements(
    val hasLowercase: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasNumber: Boolean = false,
    val hasMinLength: Boolean = false
) {
    val allMet: Boolean get() = hasLowercase && hasUppercase && hasNumber && hasMinLength
}
