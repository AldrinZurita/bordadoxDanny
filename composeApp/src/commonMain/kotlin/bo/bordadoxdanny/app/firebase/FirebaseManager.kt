package bo.bordadoxdanny.app.firebase

expect class FirebaseManager() {
    suspend fun saveData(path: String, data: Any): Result<Unit>
    suspend fun getData(path: String): Result<Any?>
}
