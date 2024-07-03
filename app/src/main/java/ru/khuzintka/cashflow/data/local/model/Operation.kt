package ru.khuzintka.cashflow.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// ToDo: хранить и accountId и accountName избыточно,
// но так удобнее делать bind в OperationAdapter

@Parcelize
@Entity(tableName = "operation_table")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "count")
    var count: Long = 0L,

    @ColumnInfo(name = "desc")
    var desc: String = "",

    @ColumnInfo(name = "is_income")
    var isIncome: Boolean = true,

    @ColumnInfo(name = "account_id")
    var accountId: Long,

    @ColumnInfo(name = "account_name")
    var accountName: String = "",
) : Parcelable