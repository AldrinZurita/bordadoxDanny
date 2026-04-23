package bo.bordadoxdanny.app.di

import bo.bordadoxdanny.app.features.cash.data.CashDao
import bo.bordadoxdanny.app.features.cash.data.CashRepositoryImpl
import bo.bordadoxdanny.app.data.database.AppDatabase
import bo.bordadoxdanny.app.data.database.getDatabaseBuilder
import bo.bordadoxdanny.app.data.database.createRoomDatabase
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import bo.bordadoxdanny.app.features.orders.data.OrderRepositoryImpl
import bo.bordadoxdanny.app.features.profile.data.ProfileDao
import bo.bordadoxdanny.app.features.profile.data.ProfileRepositoryImpl
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.reports.data.ReportRepositoryImpl
import bo.bordadoxdanny.app.features.settings.data.UserPreferencesDao
import bo.bordadoxdanny.app.features.settings.presentation.SettingsViewModel
import bo.bordadoxdanny.app.features.notifications.TranslationService
import bo.bordadoxdanny.app.features.cash.domain.CashRepository
import bo.bordadoxdanny.app.features.cash.domain.GetCashEntriesUseCase
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import bo.bordadoxdanny.app.features.profile.domain.GetProfileUseCase
import bo.bordadoxdanny.app.features.profile.domain.ProfileRepository
import bo.bordadoxdanny.app.features.reports.domain.GetReportsUseCase
import bo.bordadoxdanny.app.features.reports.domain.ReportRepository
import bo.bordadoxdanny.app.features.cash.presentation.CashViewModel
import bo.bordadoxdanny.app.features.orders.presentation.OrdersViewModel
import bo.bordadoxdanny.app.features.profile.presentation.ProfileViewModel
import bo.bordadoxdanny.app.features.reports.presentation.ReportsViewModel
import bo.bordadoxdanny.app.domain.SyncDataUseCase
import bo.bordadoxdanny.app.util.Notifier
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> { 
        createRoomDatabase(getDatabaseBuilder()) 
    }
    
    single<OrderDao> { get<AppDatabase>().orderDao() }
    single<CashDao> { get<AppDatabase>().cashDao() }
    single<ReportDao> { get<AppDatabase>().reportDao() }
    single<ProfileDao> { get<AppDatabase>().profileDao() }
    single<UserPreferencesDao> { get<AppDatabase>().userPreferencesDao() }

    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<CashRepository> { CashRepositoryImpl(get()) }
    single<ReportRepository> { ReportRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }
    single { TranslationService(get(), AppConfig.LOCO_API_KEY) }
}

val domainModule = module {
    factory { GetOrdersUseCase(get()) }
    factory { GetCashEntriesUseCase(get()) }
    factory { GetReportsUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { SyncDataUseCase() }
}

val presentationModule = module {
    factory { OrdersViewModel(get()) }
    factory { CashViewModel(get()) }
    factory { ReportsViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { SettingsViewModel(get(), get(), get()) }
}

val commonModules = listOf(dataModule, networkModule, domainModule, presentationModule)
