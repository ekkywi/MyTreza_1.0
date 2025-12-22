package com.trezanix.mytreza.data.repository

import com.trezanix.mytreza.data.local.dao.WalletDao
import com.trezanix.mytreza.data.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {

    override fun getAllWallets(): Flow<List<WalletEntity>> {
        return walletDao.getAllWallets()
    }

    override suspend fun getWalletById(id: String): WalletEntity? {
        return walletDao.getWalletById(id)
    }

    override suspend fun insertWallet(wallet: WalletEntity) {
        walletDao.insertWallet(wallet)
    }

    override suspend fun deleteWalletById(id: String) {
        walletDao.deleteWalletById(id)
    }

    override suspend fun updateWalletBalance(id: String, newBalance: Double) {
        walletDao.updateBalance(id, newBalance)
    }
}