package ru.khuzintka.cashflow.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import ru.khuzintka.cashflow.data.remote.model.CurrencyResponse

interface CurrencyService {
    @GET("daily_json.js")
    suspend fun getResponse(): Response<CurrencyResponse>
}