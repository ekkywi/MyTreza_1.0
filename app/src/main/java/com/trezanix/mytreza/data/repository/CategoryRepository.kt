package com.trezanix.mytreza.data.repository

import com.trezanix.mytreza.data.local.dao.CategoryDao
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getCategories(type: String, userId: String): Flow<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(type, userId)
    }

    suspend fun addCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        if (category.isDefault) {
            throw Exception("System default categories cannot be deleted.")
        }
        categoryDao.deleteCategory(category)
    }
}