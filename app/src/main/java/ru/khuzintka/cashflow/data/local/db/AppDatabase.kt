package ru.khuzintka.cashflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.khuzintka.cashflow.data.local.dao.AccountDao
import ru.khuzintka.cashflow.data.local.dao.OperationDao
import ru.khuzintka.cashflow.data.local.model.Account
import ru.khuzintka.cashflow.data.local.model.Operation

@Database(entities = [Account::class, Operation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun operationDao(): OperationDao

}