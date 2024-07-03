package ru.khuzintka.cashflow.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Woof(
    @Expose
    @SerializedName("fileSizeBytes")
    var size: Int,

    @Expose
    @SerializedName("url")
    var url: String,
) : Parcelable