package bo.bordadoxdanny.app.core.daemon.di

import android.annotation.SuppressLint
import android.provider.Settings
import bo.bordadoxdanny.app.core.daemon.db.DaemonDatabase
import bo.bordadoxdanny.app.core.daemon.db.createDaemonDatabase
import bo.bordadoxdanny.app.core.daemon.repository.FirebaseHeartbeatRepository
import bo.bordadoxdanny.app.core.daemon.repository.HeartbeatRepository
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

@SuppressLint("HardwareIds")
val daemonModule = module {
    // 1. Repository
    singleOf(::FirebaseHeartbeatRepository) bind HeartbeatRepository::class
    
    // 2. Daemon-specific Database
    single { createDaemonDatabase(androidContext()) }
    
    // 3. DAO from the Daemon Database
    single { get<DaemonDatabase>().syncQueueDao() }
    
    // 4. Device ID
    single(named("daemon_device_id")) {
        Settings.Secure.getString(
            androidContext().contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"
    }

    // 5. ViewModel definition - Using core DSL for KMP compatibility
    viewModel {
        WatchdogViewModel(
            deviceId = get(named("daemon_device_id")),
            repository = get(),
            syncQueueDao = get(),
            context = androidContext()
        )
    }
}
