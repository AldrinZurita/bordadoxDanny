package bo.bordadoxdanny.app

import android.app.Application
import bo.bordadoxdanny.app.di.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BordadosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this)
        startKoin {
            androidContext(this@BordadosApplication)
            modules(commonModules)
        }
    }
}
