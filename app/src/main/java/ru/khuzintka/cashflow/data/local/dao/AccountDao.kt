package ru.khuzintka.cashflow.data.local.dao

import androidx.room.*
import ru.khuzintka.cashflow.data.local.model.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account_table")
    fun getAll(): List<Account>

    @Query("SELECT * FROM account_table WHERE id = :id")
    fun getById(id: Long): Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(account: Account)

    @Query("DELETE FROM account_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM account_table")
    fun deleteAll()
}