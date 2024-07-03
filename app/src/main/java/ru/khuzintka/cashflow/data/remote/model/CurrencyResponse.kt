package ru.khuzintka.cashflow.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyResponse(
    @Expose
    @SerializedName("Date")
    var date: String,

    @Expose
    @SerializedName("PreviousDate")
    var previousDate: String,

    @Expose
    @SerializedName("PreviousURL")
    var previousUrl: String,

    @Expose
    @SerializedName("Timestamp")
    var timestamp: String,

    @Expose
    @SerializedName("Valute")
    var valute: Map<String, Currency>,
) : Parcelable