package ru.khuzintka.cashflow.data.remote.repo

import kotlinx.coroutines.flow.Flow
import ru.khuzintka.cashflow.data.remote.model.Woof

interface WoofRepo {
    suspend fun getWoof(): Flow<Woof>
}