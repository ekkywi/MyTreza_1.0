package com.trezanix.mytreza.data.repository

import com.trezanix.mytreza.data.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getAllWallets(): Flow<List<WalletEntity>>
    suspend fun getWalletById(id: String): WalletEntity?
    suspend fun insertWallet(wallet: WalletEntity)
    suspend fun deleteWalletById(id: String)
    suspend fun updateWalletBalance(id: String, newBalance: Double)
    suspend fun updateWalletArchived(id: String, isArchived: Boolean)
    suspend fun updateWallet(wallet: WalletEntity)
}