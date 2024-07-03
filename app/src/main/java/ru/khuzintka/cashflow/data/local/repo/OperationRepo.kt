package ru.khuzintka.cashflow.data.local.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.khuzintka.cashflow.data.local.dao.OperationDao
import ru.khuzintka.cashflow.data.local.model.Operation
import javax.inject.Inject

class OperationRepo
@Inject constructor(private val operationDao: OperationDao) {
    fun all(): Flow<List<Operation>> = flow { emit(operationDao.getAll()) }

    fun byId(id: Long): Flow<Operation> = flow { emit(operationDao.getById(id)) }

    fun insert(operation: Operation) = operationDao.insert(operation)

    fun update(operation: Operation) = operationDao.update(operation)

    fun delete(operation: Operation) = operationDao.delete(operation)

    fun deleteById(id: Long) = operationDao.deleteById(id)

    fun byAccountId(accountId: Long): Flow<List<Operation>> =
        flow { emit(operationDao.getByAccountId(accountId)) }

    fun deleteByAccountId(accountId: Long) = operationDao.deleteByAccountId(accountId)

    fun income(): Flow<List<Operation>> = flow { emit(operationDao.getIncome()) }

    fun expenses(): Flow<List<Operation>> = flow { emit(operationDao.getExpenses()) }

    fun size(): Long {
        return operationDao.getAll().size.toLong()
    }

    fun nextId(): Long {
        val items = operationDao.getAll()
        return if (items.isEmpty()) 1L else items.maxBy { it.id }.id + 1
    }
}