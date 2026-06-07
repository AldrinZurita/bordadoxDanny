package bo.bordadoxdanny.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.features.profile.data.UserDao
import bo.bordadoxdanny.app.features.profile.data.toDto
import bo.bordadoxdanny.app.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class SyncUserWorker(
    context: Context,
    params: WorkerParameters,
    private val userDao: UserDao,
    private val firebaseManager: FirebaseManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return Result.failure()
        
        val pendingUsers = userDao.getPendingUsers()
        
        if (pendingUsers.isEmpty()) return Result.success()

        return try {
            pendingUsers.forEach { entity ->
                val dto = entity.toDto()
                firebaseManager.saveData("users/$userId", dto).getOrThrow()
                userDao.updateSyncStatus(entity.id, "SYNCED")
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
