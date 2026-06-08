package bo.bordadoxdanny.app.firebase

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

actual class FirebaseManager actual constructor() {
    actual suspend fun saveData(path: String, data: Any): Result<Unit> {
        TODO("Not yet implemented for iOS")
    }

    actual suspend fun <T : Any> getData(path: String, clazz: KClass<T>): Result<T?> {
        TODO("Not yet implemented for iOS")
    }

    actual fun <T : Any> observeData(path: String, clazz: KClass<T>): Flow<T?> {
        TODO("Not yet implemented for iOS")
    }
}