package bo.bordadoxdanny.app.firebase

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

expect class FirebaseManager() {
    suspend fun saveData(path: String, data: Any): Result<Unit>
    suspend fun <T : Any> getData(path: String, clazz: KClass<T>): Result<T?>
    fun <T : Any> observeData(path: String, clazz: KClass<T>): Flow<T?>
}
