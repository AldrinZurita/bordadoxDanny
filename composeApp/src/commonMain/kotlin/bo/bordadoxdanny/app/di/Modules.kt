package bo.bordadoxdanny.app.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import bo.bordadoxdanny.app.data.cash.CashDao
import bo.bordadoxdanny.app.data.cash.CashRepositoryImpl
import bo.bordadoxdanny.app.data.database.AppDatabase
import bo.bordadoxdanny.app.data.database.getDatabaseBuilder
import bo.bordadoxdanny.app.data.orders.OrderDao
import bo.bordadoxdanny.app.data.orders.OrderRepositoryImpl
import bo.bordadoxdanny.app.data.profile.ProfileDao
import bo.bordadoxdanny.app.data.profile.ProfileRepositoryImpl
import bo.bordadoxdanny.app.data.reports.ReportDao
import bo.bordadoxdanny.app.data.reports.ReportRepositoryImpl
import bo.bordadoxdanny.app.domain.cash.CashRepository
import bo.bordadoxdanny.app.domain.cash.GetCashEntriesUseCase
import bo.bordadoxdanny.app.domain.orders.GetOrdersUseCase
import bo.bordadoxdanny.app.domain.orders.OrderRepository
import bo.bordadoxdanny.app.domain.profile.GetProfileUseCase
import bo.bordadoxdanny.app.domain.profile.ProfileRepository
import bo.bordadoxdanny.app.domain.reports.GetReportsUseCase
import bo.bordadoxdanny.app.domain.reports.ReportRepository
import bo.bordadoxdanny.app.presentation.cash.CashViewModel
import bo.bordadoxdanny.app.presentation.orders.OrdersViewModel
import bo.bordadoxdanny.app.presentation.profile.ProfileViewModel
import bo.bordadoxdanny.app.presentation.reports.ReportsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
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
}

val presentationModule = module {
    viewModelOf(::OrdersViewModel)
    viewModelOf(::CashViewModel)
    viewModelOf(::ReportsViewModel)
    viewModelOf(::ProfileViewModel)
}

val commonModules = listOf(dataModule, domainModule, presentationModule)
