package ru.khuzintka.cashflow.data.remote.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.khuzintka.cashflow.data.remote.model.Currency
import ru.khuzintka.cashflow.data.remote.service.CurrencyService

class CurrencyRepoImpl(private val service: CurrencyService) : CurrencyRepo {

    private val dollarKey = "USD"
    private val euroKey = "EUR"

    override suspend fun getDollarRate(): Flow<Currency> = flow {
        service.getResponse().body()?.valute?.get(dollarKey)?.let { emit(it) }
    }

    override suspend fun getEuroRate(): Flow<Currency> = flow {
        service.getResponse().body()?.valute?.get(euroKey)?.let { emit(it) }
    }
}