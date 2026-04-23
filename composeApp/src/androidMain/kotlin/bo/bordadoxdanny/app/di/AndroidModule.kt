package bo.bordadoxdanny.app.di

import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.workers.LogUploadWorker
import bo.bordadoxdanny.app.workers.LogScheduler
import bo.bordadoxdanny.app.workers.WorkerScheduler
import bo.bordadoxdanny.app.util.Notifier
import bo.bordadoxdanny.app.util.AndroidNotifier
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val androidModule = module {
    // Factory for Worker
    worker { (params: WorkerParameters) ->
        LogUploadWorker(get(), params, get())
    }
    
    // Bind Scheduler
    single<WorkerScheduler> { LogScheduler(androidContext()) }

    // Bind the Notification Implementation
    single<Notifier> { AndroidNotifier(androidContext()) }
}
