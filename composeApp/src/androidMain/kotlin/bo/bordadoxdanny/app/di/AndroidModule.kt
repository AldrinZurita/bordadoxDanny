package bo.bordadoxdanny.app.di

import bo.bordadoxdanny.app.workers.LogUploadWorker
import bo.bordadoxdanny.app.workers.LogScheduler
import bo.bordadoxdanny.app.workers.WorkerScheduler
import bo.bordadoxdanny.app.features.orders.worker.OrderSyncWorker
import bo.bordadoxdanny.app.features.orders.worker.AndroidSyncScheduler
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import bo.bordadoxdanny.app.workers.SyncReportWorker
import bo.bordadoxdanny.app.workers.SyncUserWorker
import bo.bordadoxdanny.app.features.profile.data.AuthRepositoryImpl
import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import bo.bordadoxdanny.app.firebase.RemoteConfigManager
import bo.bordadoxdanny.app.firebase.RemoteConfigManagerImpl
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf

val androidModule = module {
    // Workers
    workerOf(::LogUploadWorker)
    workerOf(::OrderSyncWorker)
    workerOf(::SyncReportWorker)
    workerOf(::SyncUserWorker)

    // Schedulers
    single<WorkerScheduler> { LogScheduler(androidContext()) }
    single<SyncScheduler> { AndroidSyncScheduler(androidContext()) }
    
    // Repositories (Android specific implementations)
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    
    // Remote Config
    single<RemoteConfigManager> { RemoteConfigManagerImpl() }
}
