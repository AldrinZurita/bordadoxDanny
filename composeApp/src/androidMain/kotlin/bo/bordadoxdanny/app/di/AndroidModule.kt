package bo.bordadoxdanny.app.di

import bo.bordadoxdanny.app.worker.LogScheduler
import org.koin.dsl.module

val androidModule = module {
    single { LogScheduler(get()) }
}
