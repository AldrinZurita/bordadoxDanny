package bo.bordadoxdanny.app.features.reports.data

import bo.bordadoxdanny.app.firebase.FirebaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReportFirebaseListener(
    private val firebaseManager: FirebaseManager,
    private val reportSummaryDao: ReportSummaryDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    fun startListening(userId: String) {
        // Listening to report summaries
        firebaseManager.observeData("reports/$userId/summaries", ReportSummaryDto::class)
            .onEach { dto ->
                dto?.let {
                    reportSummaryDao.upsert(it.toEntity())
                }
            }
            .launchIn(scope)
            
        // Note: For a list of summaries, we might need a different approach if observeData only returns one item
        // Assuming 'summaries' is a map of periodId -> DTO
    }
}
