package bo.bordadoxdanny.app.firebase

import kotlinx.coroutines.flow.Flow

expect class FirebaseManager() {
    suspend fun saveData(path: String, data: Any): Result<Unit>
    suspend fun getData(path: String): Result<Any?>
    fun <T : Any> observeData(path: String, clazz: kotlin.reflect.KClass<T>): Flow<T?>
}
