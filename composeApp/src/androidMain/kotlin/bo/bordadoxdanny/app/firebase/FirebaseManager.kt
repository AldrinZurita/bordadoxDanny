package bo.bordadoxdanny.app.firebase

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

actual class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance().reference

    actual suspend fun saveData(path: String, data: Any): Result<Unit> = runCatching {
        database.child(path).setValue(data).await()
        Unit
    }

    actual suspend fun getData(path: String): Result<Any?> = runCatching {
        database.child(path).get().await().value
    }
}
