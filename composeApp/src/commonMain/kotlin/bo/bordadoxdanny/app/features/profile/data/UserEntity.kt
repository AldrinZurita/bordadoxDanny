package bo.bordadoxdanny.app.features.profile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // Firebase UID
    val username: String,
    val email: String,
    val phoneCountryCode: String,
    val phoneNumber: String,
    val firstName: String,
    val middleName: String?,
    val lastName1: String,
    val lastName2: String?,
    val ciNumber: String,
    val ciComplement: String?,
    val ciDepartment: String,
    val languageCode: String,
    val syncStatus: String = "PENDING",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
