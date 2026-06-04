package bo.bordadoxdanny.app.di

import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.workers.LogUploadWorker
import bo.bordadoxdanny.app.workers.LogScheduler
import bo.bordadoxdanny.app.workers.WorkerScheduler
import bo.bordadoxdanny.app.features.orders.worker.OrderSyncWorker
import bo.bordadoxdanny.app.features.orders.worker.AndroidSyncScheduler
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf

val androidModule = module {
    // Factory modernizado para Workers en Koin
    workerOf(::LogUploadWorker)
    workerOf(::OrderSyncWorker)

    // Vinculamos la interfaz común con la implementación de Android
    single<WorkerScheduler> { LogScheduler(androidContext()) }
    single<SyncScheduler> { AndroidSyncScheduler(androidContext()) }
}
