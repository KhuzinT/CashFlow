package ru.khuzintka.cashflow.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.khuzintka.cashflow.data.remote.repo.CurrencyRepo
import ru.khuzintka.cashflow.data.remote.repo.CurrencyRepoImpl
import ru.khuzintka.cashflow.data.remote.repo.WoofRepo
import ru.khuzintka.cashflow.data.remote.repo.WoofRepoImpl
import ru.khuzintka.cashflow.data.remote.service.CurrencyService
import ru.khuzintka.cashflow.data.remote.service.WoofService
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val timeout = 1L
    private val currencyUrl = "https://www.cbr-xml-daily.ru/"
    private val woofUrl = "https://random.dog/"

    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(timeout, TimeUnit.SECONDS)
        writeTimeout(timeout, TimeUnit.SECONDS)
        readTimeout(timeout, TimeUnit.SECONDS)
    }.build()

    private fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
        val gson = GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        return Retrofit.Builder().apply {
            baseUrl(url)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create(gson))
        }.build()
    }

    @Provides
    @Named("currency")
    fun provideCurrencyRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return provideRetrofit(okHttpClient, currencyUrl)
    }

    @Provides
    @Named("woof")
    fun provideWoofRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return provideRetrofit(okHttpClient, woofUrl)
    }

    @Provides
    fun provideCurrencyService(@Named("currency") retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)

    @Provides
    fun provideCurrencyRepo(service: CurrencyService): CurrencyRepo =
        CurrencyRepoImpl(service)

    @Provides
    fun provideWoofService(@Named("woof") retrofit: Retrofit): WoofService =
        retrofit.create(WoofService::class.java)

    @Provides
    fun provideWoofRepo(service: WoofService): WoofRepo =
        WoofRepoImpl(service)
}