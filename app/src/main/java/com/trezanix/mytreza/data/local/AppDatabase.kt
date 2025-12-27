package com.trezanix.mytreza.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trezanix.mytreza.data.local.dao.WalletDao
import com.trezanix.mytreza.data.local.entity.WalletEntity
import com.trezanix.mytreza.data.local.dao.CategoryDao
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import com.trezanix.mytreza.data.local.dao.TransactionDao
import com.trezanix.mytreza.data.local.entity.TransactionEntity

@Database(
    entities = [
        WalletEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}