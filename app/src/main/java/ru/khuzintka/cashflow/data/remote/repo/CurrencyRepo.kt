package ru.khuzintka.cashflow.data.remote.repo

import kotlinx.coroutines.flow.Flow
import ru.khuzintka.cashflow.data.remote.model.Currency

interface CurrencyRepo {
    suspend fun getDollarRate(): Flow<Currency>
    suspend fun getEuroRate(): Flow<Currency>
}