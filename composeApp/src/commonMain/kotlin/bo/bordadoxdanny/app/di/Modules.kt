package bo.bordadoxdanny.app.di

import bo.bordadoxdanny.app.features.cash.data.TransactionDao
import bo.bordadoxdanny.app.features.cash.data.TransactionRepositoryImpl
import bo.bordadoxdanny.app.data.database.AppDatabase
import bo.bordadoxdanny.app.data.database.getDatabaseBuilder
import bo.bordadoxdanny.app.data.database.createRoomDatabase
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import bo.bordadoxdanny.app.features.orders.data.OrderRepositoryImpl
import bo.bordadoxdanny.app.features.profile.data.ProfileDao
import bo.bordadoxdanny.app.features.profile.data.ProfileRepositoryImpl
import bo.bordadoxdanny.app.features.profile.data.UserDao
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.reports.data.ReportRepositoryImpl
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryDao
import bo.bordadoxdanny.app.data.preferences.SettingsDao
import bo.bordadoxdanny.app.features.cash.domain.TransactionRepository
import bo.bordadoxdanny.app.features.cash.domain.GetTransactionsUseCase
import bo.bordadoxdanny.app.features.cash.domain.GetTotalBalanceUseCase
import bo.bordadoxdanny.app.features.cash.presentation.CashViewModel
import bo.bordadoxdanny.app.features.orders.domain.CreateOrderUseCase
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import bo.bordadoxdanny.app.features.orders.presentation.OrdersViewModel
import bo.bordadoxdanny.app.features.orders.presentation.create.CreateOrderViewModel
import bo.bordadoxdanny.app.features.profile.domain.*
import bo.bordadoxdanny.app.features.reports.domain.*
import bo.bordadoxdanny.app.features.profile.presentation.AuthViewModel
import bo.bordadoxdanny.app.features.profile.presentation.LanguageViewModel
import bo.bordadoxdanny.app.features.profile.presentation.ProfileViewModel
import bo.bordadoxdanny.app.features.reports.presentation.ReportViewModel
import bo.bordadoxdanny.app.features.reports.presentation.ReportsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import bo.bordadoxdanny.app.domain.SyncDataUseCase

val dataModule = module {
    single<AppDatabase> { 
        createRoomDatabase(getDatabaseBuilder()) 
    }
    
    single<OrderDao> { get<AppDatabase>().orderDao() }
    single<TransactionDao> { get<AppDatabase>().transactionDao() }
    single<ReportDao> { get<AppDatabase>().reportDao() }
    single<ReportSummaryDao> { get<AppDatabase>().reportSummaryDao() }
    single<ProfileDao> { get<AppDatabase>().profileDao() }
    single<UserDao> { get<AppDatabase>().userDao() }
    single<SettingsDao> { get<AppDatabase>().settingsDao() }

    single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            syncScheduler = get()
        )
    }
    single<ReportRepository> { ReportRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory { GetOrdersUseCase(get()) }
    factory { CreateOrderUseCase(get(), get()) }
    factory { GetTransactionsUseCase(get()) }
    factory { GetTotalBalanceUseCase(get()) }
    factory { GetFinancialSummaryUseCase(get()) }
    factory { GetAvailablePeriodsUseCase(get()) }
    factory { GetAccountsReceivableUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory<SyncDataUseCase> { SyncDataUseCase() }
    
    // Auth UseCases
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SendVerificationCodeUseCase(get()) }
    factory { VerifyCodeUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { UpdateLanguageUseCase(get()) }
    factory { GetReportsUseCase(get()) }
}

val presentationModule = module {
    viewModelOf(::OrdersViewModel)
    viewModelOf(::CreateOrderViewModel)
    viewModelOf(::CashViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::ReportsViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::LanguageViewModel)
}

val commonModules = listOf(dataModule, domainModule, presentationModule)
