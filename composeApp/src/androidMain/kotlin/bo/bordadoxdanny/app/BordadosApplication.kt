package bo.bordadoxdanny.app

import android.app.Application
import bo.bordadoxdanny.app.di.commonModules
import bo.bordadoxdanny.app.di.androidModule
import bo.bordadoxdanny.app.worker.LogScheduler
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BordadosApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this)
        
        startKoin {
            androidContext(this@BordadosApplication)
            modules(commonModules + androidModule)
        }
        
        // Obtenemos el scheduler y ejecutamos el test inmediato
        val logScheduler: LogScheduler = get()
        
        // Ejecutamos el test inmediato solo para ver el println en Logcat ahora
        logScheduler.runImmediateTest()
        
        // También dejamos programada la tarea periódica real
        logScheduler.schedulePeriodicUpload()
    }
}
