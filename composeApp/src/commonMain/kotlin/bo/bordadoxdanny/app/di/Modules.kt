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
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    // 🔥 UNICA INSTANCIA PARA TODO EL PROYECTO
    single<AppDatabase> { 
        createRoomDatabase(getDatabaseBuilder()) 
    }
    
    single<OrderDao> { get<AppDatabase>().orderDao() }
    single<CashDao> { get<AppDatabase>().cashDao() }
    single<ReportDao> { get<AppDatabase>().reportDao() }
    single<ProfileDao> { get<AppDatabase>().profileDao() }

    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<CashRepository> { CashRepositoryImpl(get()) }
    single<ReportRepository> { ReportRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetOrdersUseCase(get()) }
    factory { GetCashEntriesUseCase(get()) }
    factory { GetReportsUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { SyncDataUseCase() }
}

val presentationModule = module {
    viewModelOf(::OrdersViewModel)
    viewModelOf(::CashViewModel)
    viewModelOf(::ReportsViewModel)
    viewModelOf(::ProfileViewModel)
}

val commonModules = listOf(dataModule, domainModule, presentationModule)
