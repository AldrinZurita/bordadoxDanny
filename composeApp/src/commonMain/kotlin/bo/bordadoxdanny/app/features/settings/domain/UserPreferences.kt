package bo.bordadoxdanny.app.features.settings.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey val id: Int = 0,
    val languageCode: String = "en-US"
)
