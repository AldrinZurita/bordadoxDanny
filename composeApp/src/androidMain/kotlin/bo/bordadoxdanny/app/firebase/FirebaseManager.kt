package bo.bordadoxdanny.app.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

actual class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance().reference

    actual suspend fun saveData(path: String, data: Any): Result<Unit> = runCatching {
        database.child(path).setValue(data).await()
        Unit
    }

    actual suspend fun <T : Any> getData(path: String, clazz: KClass<T>): Result<T?> = runCatching {
        val snapshot = database.child(path).get().await()
        snapshot.getValue(clazz.java)
    }

    actual fun <T : Any> observeData(path: String, clazz: KClass<T>): Flow<T?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(clazz.java))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.child(path).addValueEventListener(listener)
        awaitClose { database.child(path).removeEventListener(listener) }
    }
}
