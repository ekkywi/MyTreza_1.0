package com.trezanix.mytreza.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "categories",
    indices = [Index(value = ["type"]), Index(value = ["userId"])]
)
data class CategoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String,
    val iconName : String,
    val colorHex: String,
    val isDefault: Boolean = false,
    val userId: String? = null
)
