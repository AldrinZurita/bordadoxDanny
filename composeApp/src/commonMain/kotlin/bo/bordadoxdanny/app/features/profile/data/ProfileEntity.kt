package bo.bordadoxdanny.app.features.profile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileEntity(
    @PrimaryKey val id: Long = 1, // Single profile record
    val name: String,
    val email: String,
    val phone: String
)
