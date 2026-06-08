package bo.bordadoxdanny.app

import android.app.Application
import android.util.Log
import bo.bordadoxdanny.app.core.daemon.notifications.NotificationHelper
import bo.bordadoxdanny.app.core.daemon.worker.HeartbeatScheduler
import bo.bordadoxdanny.app.core.daemon.di.daemonModule
import bo.bordadoxdanny.app.di.commonModules
import bo.bordadoxdanny.app.di.androidModule
import bo.bordadoxdanny.app.workers.LogScheduler
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BordadosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        try {
            FirebaseApp.initializeApp(this)
            Log.d("BORDADOS", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("BORDADOS", "Firebase initialization failed: ${e.message}")
        }

        ContextProvider.init(this)

        startKoin {
            androidContext(this@BordadosApplication)
            workManagerFactory()
            modules(commonModules + androidModule + daemonModule)
        }

        try {
            get<bo.bordadoxdanny.app.firebase.RemoteConfigManager>()
        } catch (e: Exception) {
            Log.e("BORDADOS", "RemoteConfig initialization failed: ${e.message}")
        }

        // Initialize Daemon components
        NotificationHelper.createChannel(this)
        HeartbeatScheduler.start(this)

        // 🔥 RESTAURADO: WorkManager habilitado con 2 segundos de delay
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            try {
                val logScheduler = get<LogScheduler>()
                logScheduler.testWorkImmediately()
            } catch (e: Exception) {
                Log.e("BORDADOS", "Error en scheduler: ${e.message}")
            }
        }, 2000)
    }
}
