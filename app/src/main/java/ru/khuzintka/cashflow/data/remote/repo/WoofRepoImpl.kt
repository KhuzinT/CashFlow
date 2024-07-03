package ru.khuzintka.cashflow.data.remote.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.khuzintka.cashflow.data.remote.model.Woof
import ru.khuzintka.cashflow.data.remote.service.WoofService

class WoofRepoImpl(private val service: WoofService) : WoofRepo {
    override suspend fun getWoof(): Flow<Woof> = flow {
        service.getResponse().body()?.let { emit(it) }
    }
}