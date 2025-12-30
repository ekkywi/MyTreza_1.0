package com.trezanix.mytreza.data.repository

import com.trezanix.mytreza.data.local.dao.TransactionDao
import com.trezanix.mytreza.data.local.dao.WalletDao
import com.trezanix.mytreza.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val walletDao: WalletDao
) {
    suspend fun addTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
        val wallet = walletDao.getWalletByIdSnapshot(transaction.walletId)
        if (wallet != null) {
            val newBalance = if (transaction.type == "INCOME") {
                wallet.balance + transaction.amount
            } else {
                wallet.balance - transaction.amount
            }
            walletDao.updateWallet(wallet.copy(balance = newBalance))
        }
    }
    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
        val wallet = walletDao.getWalletByIdSnapshot(transaction.walletId)
        if (wallet != null) {
            val newBalance = if (transaction.type == "INCOME") {
                wallet.balance - transaction.amount
            } else {
                wallet.balance + transaction.amount
            }
            walletDao.updateWallet(wallet.copy(balance = newBalance))
        }
    }
    fun getTransactionsByWallet(walletId: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByWallet(walletId)
    }
}