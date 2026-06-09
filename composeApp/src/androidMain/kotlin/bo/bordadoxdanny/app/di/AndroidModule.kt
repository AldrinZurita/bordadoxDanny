package bo.bordadoxdanny.app.di

import bo.bordadoxdanny.app.workers.LogUploadWorker
import bo.bordadoxdanny.app.workers.LogScheduler
import bo.bordadoxdanny.app.workers.WorkerScheduler
import bo.bordadoxdanny.app.features.orders.worker.OrderSyncWorker
import bo.bordadoxdanny.app.features.orders.worker.AndroidSyncScheduler
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import bo.bordadoxdanny.app.workers.SyncReportWorker
import bo.bordadoxdanny.app.workers.SyncUserWorker
import bo.bordadoxdanny.app.features.cash.worker.SyncTransactionWorker
import bo.bordadoxdanny.app.features.profile.data.AuthRepositoryImpl
import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import bo.bordadoxdanny.app.firebase.RemoteConfigManager
import bo.bordadoxdanny.app.firebase.RemoteConfigManagerImpl
import bo.bordadoxdanny.app.firebase.FirebaseManager
import bo.bordadoxdanny.app.network.ApiService
import bo.bordadoxdanny.app.network.RetrofitClient
import bo.bordadoxdanny.app.data.preferences.PreferencesRepository
import bo.bordadoxdanny.app.data.preferences.PreferencesRepositoryImpl
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val androidModule = module {
    // Firebase Components
    singleOf(::FirebaseManager)
    singleOf(::RemoteConfigManagerImpl) bind RemoteConfigManager::class

    // Workers
    workerOf(::LogUploadWorker)
    workerOf(::OrderSyncWorker)
    workerOf(::SyncReportWorker)
    workerOf(::SyncUserWorker)
    workerOf(::SyncTransactionWorker)

    // Schedulers
    single { LogScheduler(androidContext()) } bind WorkerScheduler::class
    singleOf(::AndroidSyncScheduler) bind SyncScheduler::class
    
    // Repositories
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    single { PreferencesRepositoryImpl(get()) } bind PreferencesRepository::class
    single { RetrofitClient.apiService } bind ApiService::class
}
