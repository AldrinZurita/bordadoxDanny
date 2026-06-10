package bo.bordadoxdanny.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.core.sync.SyncConfig
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryDao
import bo.bordadoxdanny.app.features.reports.data.toDto
import bo.bordadoxdanny.app.firebase.FirebaseManager

class SyncReportWorker(
    context: Context,
    params: WorkerParameters,
    private val reportSummaryDao: ReportSummaryDao,
    private val firebaseManager: FirebaseManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val userId = inputData.getString(SyncConfig.KEY_USER_ID) ?: return Result.failure()
        
        val pendingSummaries = reportSummaryDao.getPendingSummaries(userId)
        
        if (pendingSummaries.isEmpty()) return Result.success()

        return try {
            pendingSummaries.forEach { entity ->
                val dto = entity.toDto()
                firebaseManager.saveData("reports/$userId/summaries/${entity.periodId}", dto).getOrThrow()
                reportSummaryDao.updateSyncStatus(entity.periodId, userId, "SYNCED")
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
