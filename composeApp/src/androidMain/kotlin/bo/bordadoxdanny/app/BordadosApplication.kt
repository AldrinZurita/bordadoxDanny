package bo.bordadoxdanny.app

import android.app.Application
import android.util.Log
import bo.bordadoxdanny.app.di.commonModules
import bo.bordadoxdanny.app.di.androidModule
import bo.bordadoxdanny.app.workers.WorkerScheduler
import com.google.firebase.FirebaseApp
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
            modules(commonModules + androidModule)
        }

        // 🔥 Sincronización inicial al arrancar
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            try {
                val scheduler = get<WorkerScheduler>()
                scheduler.syncConfigNow()
            } catch (e: Exception) {
                Log.e("BORDADOS", "Error iniciando sincronización: ${e.message}")
            }
        }, 1000)
    }
}
