package ru.khuzintka.cashflow.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import ru.khuzintka.cashflow.data.remote.model.Woof

interface WoofService {
    @GET("woof.json")
    suspend fun getResponse(): Response<Woof>
}