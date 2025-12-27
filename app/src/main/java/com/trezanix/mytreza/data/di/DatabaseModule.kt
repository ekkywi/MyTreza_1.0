package com.trezanix.mytreza.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.trezanix.mytreza.data.local.AppDatabase
import com.trezanix.mytreza.data.local.dao.CategoryDao
import com.trezanix.mytreza.data.local.dao.WalletDao
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import com.trezanix.mytreza.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        providerCategoryDao: Provider<CategoryDao>
        ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mytreza_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDefaultCategories(providerCategoryDao.get())
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWalletDao(database: AppDatabase): WalletDao {return database.walletDao()}

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    private suspend fun populateDefaultCategories(dao: CategoryDao) {
        val default = listOf(
            createDefault("Food and Drink", "EXPENSE", "fastfood", "#FF5722"),
            createDefault("Transportation", "EXPENSE", "commute", "#03A9F4"),
            createDefault("Shopping", "EXPENSE", "shopping_bag", "#E91E63"),
            createDefault("Entertainment", "EXPENSE", "movie", "#9C27B0"),
            createDefault("Healthcare", "EXPENSE", "medical_services", "#F44336"),
            createDefault("Education", "EXPENSE", "school", "#FFC107"),
            createDefault("Household", "EXPENSE", "home", "#795548"),
            createDefault("Pet", "EXPENSE", "pets", "#FF9800"),
            createDefault("Salary", "INCOME", "payments", "#4CAF50"),
            createDefault("Bonus", "INCOME", "card_giftcard", "#8BC34A")
        )
        dao.insertAll(default)
    }

    private fun createDefault(
        name: String,
        type: String,
        icon: String,
        color: String
    ): CategoryEntity {
        return CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            type = type,
            iconName = icon,
            colorHex = color,
            isDefault = true,
            userId = null
        )
    }
}
