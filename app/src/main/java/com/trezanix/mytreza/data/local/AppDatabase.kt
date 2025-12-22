package com.trezanix.mytreza.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trezanix.mytreza.data.local.dao.WalletDao
import com.trezanix.mytreza.data.local.entity.WalletEntity

@Database(
    entities = [WalletEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}