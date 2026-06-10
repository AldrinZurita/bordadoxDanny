package bo.bordadoxdanny.app.features.profile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val phone: String
)
