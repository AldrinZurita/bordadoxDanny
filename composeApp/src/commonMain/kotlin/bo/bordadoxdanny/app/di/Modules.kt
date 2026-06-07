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
import bo.bordadoxdanny.app.features.profile.data.UserDao
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.reports.data.ReportRepositoryImpl
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryDao
import bo.bordadoxdanny.app.data.preferences.SettingsDao
import bo.bordadoxdanny.app.data.preferences.PreferencesRepository
import bo.bordadoxdanny.app.features.cash.domain.CashRepository
import bo.bordadoxdanny.app.features.cash.domain.GetCashEntriesUseCase
import bo.bordadoxdanny.app.features.orders.domain.CreateOrderUseCase
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import bo.bordadoxdanny.app.features.profile.domain.*
import bo.bordadoxdanny.app.features.reports.domain.*
import bo.bordadoxdanny.app.features.cash.presentation.CashViewModel
import bo.bordadoxdanny.app.features.orders.presentation.OrdersViewModel
import bo.bordadoxdanny.app.features.orders.presentation.create.CreateOrderViewModel
import bo.bordadoxdanny.app.features.profile.presentation.ProfileViewModel
import bo.bordadoxdanny.app.features.profile.presentation.AuthViewModel
import bo.bordadoxdanny.app.features.reports.presentation.ReportViewModel
import bo.bordadoxdanny.app.domain.SyncDataUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> { 
        createRoomDatabase(getDatabaseBuilder()) 
    }
    
    single<OrderDao> { get<AppDatabase>().orderDao() }
    single<CashDao> { get<AppDatabase>().cashDao() }
    single<ReportDao> { get<AppDatabase>().reportDao() }
    single<ReportSummaryDao> { get<AppDatabase>().reportSummaryDao() }
    single<ProfileDao> { get<AppDatabase>().profileDao() }
    single<UserDao> { get<AppDatabase>().userDao() }
    single<SettingsDao> { get<AppDatabase>().settingsDao() }

    single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<CashRepository> { CashRepositoryImpl(get()) }
    single<ReportRepository> { ReportRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory { GetOrdersUseCase(get()) }
    factory { CreateOrderUseCase(get()) }
    factory { GetCashEntriesUseCase(get()) }
    factory { GetFinancialSummaryUseCase(get()) }
    factory { GetAvailablePeriodsUseCase(get()) }
    factory { GetAccountsReceivableUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { SyncDataUseCase() }
    
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
    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfileViewModel)
    viewModel { LanguageViewModel(get()) }
}

val commonModules = listOf(dataModule, domainModule, presentationModule)
