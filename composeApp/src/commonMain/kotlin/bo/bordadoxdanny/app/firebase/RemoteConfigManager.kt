package bo.bordadoxdanny.app.firebase

expect class RemoteConfigManager() {
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getAllConfigs(): Map<String, String>
}
