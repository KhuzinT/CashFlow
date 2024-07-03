package ru.khuzintka.cashflow.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.khuzintka.cashflow.data.local.dao.AccountDao
import ru.khuzintka.cashflow.data.local.dao.OperationDao
import ru.khuzintka.cashflow.data.local.db.AppDatabase
import ru.khuzintka.cashflow.data.local.repo.AccountRepo
import ru.khuzintka.cashflow.data.local.repo.OperationRepo
import ru.khuzintka.cashflow.util.SharedPreferencesManager

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "db"
    ).build()

    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()

    @Provides
    fun provideOperationDao(appDatabase: AppDatabase): OperationDao = appDatabase.operationDao()

    @Provides
    fun provideAccountRepository(dao: AccountDao): AccountRepo = AccountRepo(dao)

    @Provides
    fun provideOperationRepository(dao: OperationDao): OperationRepo = OperationRepo(dao)

    @Provides
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferencesManager =
        SharedPreferencesManager(context)
}