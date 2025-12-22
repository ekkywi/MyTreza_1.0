package com.trezanix.mytreza.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String,
    val balance: Double,
    val currency: String = "IDR",
    val isShared: Boolean = false,
    val ownerId: String = "SELF",
    val createdAt: String = "01/24",
    val colorIndex: Int = 0
)