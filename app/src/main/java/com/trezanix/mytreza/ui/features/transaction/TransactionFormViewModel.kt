package com.trezanix.mytreza.ui.features.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trezanix.mytreza.data.local.entity.CategoryEntity
import com.trezanix.mytreza.data.local.entity.TransactionEntity
import com.trezanix.mytreza.data.local.entity.WalletEntity
import com.trezanix.mytreza.data.repository.TransactionRepository
import com.trezanix.mytreza.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    walletRepository: WalletRepository
) : ViewModel() {
    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _date = MutableStateFlow(System.currentTimeMillis())
    val date  = _date.asStateFlow()

    private val _transactionType = MutableStateFlow("EXPENSE")
    val transactionType = _transactionType.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _selectedWallet = MutableStateFlow<WalletEntity?>(null)
    val selectedWallet = _selectedWallet.asStateFlow()

    val wallets: StateFlow<List<WalletEntity>> = walletRepository.getAllWallets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onAmountChange(value: String) {
        if (value.all { it.isDigit() }) {
            _amount.value = value
        }
    }

    fun onNoteChange(value: String) {
        _note.value = value
    }

    fun onDateChange(timestamp: Long) {
        _date.value = timestamp
    }

    fun onTypeChange(type: String) {
        _transactionType.value = type
        _selectedCategory.value = null
    }

    fun onCategorySelect(category: CategoryEntity) {
        _selectedCategory.value = category
    }

    fun onWalletSelect(wallet: WalletEntity) {
        _selectedWallet.value = wallet
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        val amountValue = _amount.value.toDoubleOrNull() ?: 0.0
        val wallet = _selectedWallet.value
        val category = _selectedCategory.value

        if (amountValue > 0 && wallet != null) {
            viewModelScope.launch {
                val newTransaction = TransactionEntity(
                    amount = amountValue,
                    note = _note.value,
                    date = _date.value,
                    type = _transactionType.value,
                    walletId = wallet.id,
                    categoryId = category?.id
                )
                transactionRepository.addTransaction(newTransaction)
                onSuccess()
            }
        }
    }
}