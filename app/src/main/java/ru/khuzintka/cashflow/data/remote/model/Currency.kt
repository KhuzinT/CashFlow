package ru.khuzintka.cashflow.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    @Expose
    @SerializedName("ID")
    var id: String,

    @Expose
    @SerializedName("NumCode")
    var numCode: String,

    @Expose
    @SerializedName("CharCode")
    var charCode: String,

    @Expose
    @SerializedName("Nominal")
    var nominal: Int,

    @Expose
    @SerializedName("Name")
    var name: String,

    @Expose
    @SerializedName("Value")
    var value: Double,

    @Expose
    @SerializedName("Previous")
    var previous: Double,
) : Parcelable