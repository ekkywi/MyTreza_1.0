package com.trezanix.mytreza.ui.features.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import com.trezanix.mytreza.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    // TODO: Nanti ambil dari User Session/Preference yang sebenarnya
    private val currentUserId = "user_local_dev"

    val expenseCategories: StateFlow<List<CategoryEntity>> = repository.getCategories("EXPENSE", currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incomeCategories: StateFlow<List<CategoryEntity>> = repository.getCategories("INCOME", currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCustomCategory(name: String, type: String, iconName: String, colorHex: String) {
        viewModelScope.launch {
            val newCategory = CategoryEntity(
                name = name,
                type = type,
                iconName = iconName,
                colorHex = colorHex,
                isDefault = false,
                userId = currentUserId
            )
            repository.addCategory(newCategory)
        }
    }

    fun deleteCategory(category: CategoryEntity, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(category)
            } catch (e: Exception) {
                onError(e.message ?: "Failed to delete category")
            }
        }
    }
}