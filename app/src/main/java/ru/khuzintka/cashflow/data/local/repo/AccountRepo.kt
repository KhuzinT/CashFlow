package ru.khuzintka.cashflow.data.local.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.khuzintka.cashflow.data.local.dao.AccountDao
import ru.khuzintka.cashflow.data.local.model.Account
import javax.inject.Inject

class AccountRepo
@Inject constructor(private val accountDao: AccountDao) {
    fun all(): Flow<List<Account>> = flow { emit(accountDao.getAll()) }

    fun byId(id: Long): Flow<Account> = flow { emit(accountDao.getById(id)) }

    fun insert(account: Account) = accountDao.insert(account)

    fun update(account: Account) = accountDao.update(account)

    fun delete(account: Account) = accountDao.delete(account)

    fun deleteById(id: Long) = accountDao.deleteById(id)

    fun deleteAll() = accountDao.deleteAll()

    fun size(): Long {
        return accountDao.getAll().size.toLong()
    }

    fun nextId(): Long {
        val items = accountDao.getAll()
        return if (items.isEmpty()) 1L else items.maxBy { it.id }.id + 1
    }
}