package bo.bordadoxdanny.app.firebase

actual class FirebaseManager actual constructor() {
    actual suspend fun saveData(path: String, data: Any): Result<Unit> {
        TODO("Not yet implemented")
    }

    actual suspend fun getData(path: String): Result<Any?> {
        TODO("Not yet implemented")
    }
}