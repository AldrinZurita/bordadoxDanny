package bo.bordadoxdanny.app.features.config.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class ConfigEntity(
    @PrimaryKey val key: String,
    val value: String
)
