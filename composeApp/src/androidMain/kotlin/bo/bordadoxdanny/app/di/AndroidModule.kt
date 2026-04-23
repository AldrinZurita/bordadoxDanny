package bo.bordadoxdanny.app.di

import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.workers.LogUploadWorker
import bo.bordadoxdanny.app.workers.SyncConfigWorker
import bo.bordadoxdanny.app.workers.LogScheduler
import bo.bordadoxdanny.app.workers.WorkerScheduler
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val androidModule = module {
    // FACTORY para el worker, NO single
    worker { (params: WorkerParameters) ->
        LogUploadWorker(get(), params, get())
    }
    worker { (params: WorkerParameters) ->
        SyncConfigWorker(get(), params)
    }
    // Vinculamos la interfaz común con la implementación de Android
    single<WorkerScheduler> { LogScheduler(androidContext()) }
}
