package ru.khuzintka.cashflow.data.local.dao

import androidx.room.*
import ru.khuzintka.cashflow.data.local.model.Operation

@Dao
interface OperationDao {
    @Query("SELECT * FROM operation_table")
    fun getAll(): List<Operation>

    @Query("SELECT * FROM operation_table WHERE id = :id")
    fun getById(id: Long): Operation

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(operation: Operation)

    @Update
    fun update(operation: Operation)

    @Delete
    fun delete(operation: Operation)

    @Query("DELETE FROM operation_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM operation_table WHERE account_id = :accountId")
    fun getByAccountId(accountId: Long): List<Operation>

    @Query("DELETE FROM operation_table WHERE account_id = :accountId")
    fun deleteByAccountId(accountId: Long)

    @Query("SELECT * FROM operation_table WHERE is_income = 1")
    fun getIncome(): List<Operation>

    @Query("SELECT * FROM operation_table WHERE is_income = 0")
    fun getExpenses(): List<Operation>

    @Query("DELETE FROM operation_table")
    fun deleteAll()
}