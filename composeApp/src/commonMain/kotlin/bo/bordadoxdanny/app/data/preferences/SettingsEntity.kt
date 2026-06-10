package bo.bordadoxdanny.app.data.preferences

import androidx.room.Entity

@Entity(
    tableName = "settings",
    primaryKeys = ["key", "userId"]
)
data class SettingsEntity(
    val key: String,
    val userId: String,
    val value: String
)