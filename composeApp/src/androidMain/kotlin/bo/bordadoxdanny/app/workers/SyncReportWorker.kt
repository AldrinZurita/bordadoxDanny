package bo.bordadoxdanny.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryDao
import bo.bordadoxdanny.app.features.reports.data.toDto
import bo.bordadoxdanny.app.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class SyncReportWorker(
    context: Context,
    params: WorkerParameters,
    private val reportSummaryDao: ReportSummaryDao,
    private val firebaseManager: FirebaseManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return Result.failure()
        
        val pendingSummaries = reportSummaryDao.getPendingSummaries()
        
        if (pendingSummaries.isEmpty()) return Result.success()

        return try {
            pendingSummaries.forEach { entity ->
                val dto = entity.toDto()
                firebaseManager.saveData("reports/$userId/summaries/${entity.periodId}", dto).getOrThrow()
                reportSummaryDao.updateSyncStatus(entity.periodId, "SYNCED")
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
