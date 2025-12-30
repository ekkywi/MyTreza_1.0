package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trezanix.mytreza.data.local.entity.WalletEntity
import com.trezanix.mytreza.ui.features.transaction.components.TransactionUiItem
import com.trezanix.mytreza.data.repository.TransactionRepository
import com.trezanix.mytreza.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val walletListState: StateFlow<List<WalletModel>> = repository.getAllWallets()
        .map { entities ->
            entities
                .filter{ !it.isArchived }
                .map { it.toUiModel() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val archivedWalletListState: StateFlow<List<WalletModel>> = repository.getAllWallets()
        .map { entities ->
            entities
                .filter { it.isArchived }
                .map { it.toUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentWallet = MutableStateFlow<WalletEntity?>(null)
    val currentWallet: StateFlow<WalletEntity?> = _currentWallet.asStateFlow()

    fun loadWalletById(id: String) {
        viewModelScope.launch {
            _currentWallet.value = repository.getWalletById(id)
        }
    }

    fun clearCurrentWallet() {
        _currentWallet.value = null
    }

    fun saveWallet(
        id: String? = null,
        name: String,
        type: String,
        balance: Double,
        isShared: Boolean,
        colorIndex: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (id != null) {
                val current = _currentWallet.value
                val existingDate = current?.createdAt ?: run {
                     val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
                     formatter.format(Date())
                }
                val updatedWallet = WalletEntity(
                    id = id,
                    name = name,
                    type = type,
                    balance = balance,
                    isShared = isShared,
                    currency = current?.currency ?: "IDR",
                    createdAt = existingDate,
                    colorIndex = colorIndex,
                    isArchived = current?.isArchived ?: false
                )
                repository.updateWallet(updatedWallet)
            } else {
                val walletId = UUID.randomUUID().toString()
                val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
                val finalDate = formatter.format(Date())

                val newWallet = WalletEntity(
                    id = walletId,
                    name = name,
                    type = type,
                    balance = balance,
                    isShared = isShared,
                    currency = "IDR",
                    createdAt = finalDate,
                    colorIndex = colorIndex,
                    isArchived = false
                )
                repository.insertWallet(newWallet)
            }

            onSuccess()
        }
    }

    fun attemptDeleteWallet(id: String): String? {
        val wallet = _currentWallet.value ?: return "Data not found"
        if (wallet.balance > 0.0) return "Failed! Balance must be 0 before it is permanently deleted."
        performDelete(id)
        return null
    }

    fun performDelete(id: String) {
        viewModelScope.launch { repository.deleteWalletById(id) }
    }

    fun attemptArchiveWallet(id: String): String? {
        val wallet = _currentWallet.value ?: return "Data error"
        if (wallet.balance > 0.0) return "Balance must be 0 to archive wallet."
        viewModelScope.launch { repository.updateWalletArchived(id, true) }
        return null
    }

    fun unarchiveWallet(id: String) {
        viewModelScope.launch { repository.updateWalletArchived(id, false) }
    }

    private val _uiTransactions = MutableStateFlow<List<TransactionUiItem>>(emptyList())
    val uiTransactions: StateFlow<List<TransactionUiItem>> = _uiTransactions.asStateFlow()

    fun loadTransactionsForWallet(walletId: String) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByWallet(walletId).collect { entities ->
                val uiList = entities.map { entity ->
                    val dateString = try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.format(Date(entity.date))
                    } catch (e: Exception) {
                        entity.date.toString()
                    }
                    TransactionUiItem(
                        id = entity.id,
                        title = entity.note.ifEmpty { "Transaction" },
                        date = dateString,
                        amount = entity.amount,
                        type = entity.type,
                        colorHex = if (entity.type == "EXPENSE") "#FF5252" else "#4CAF50"
                    )
                }
                _uiTransactions.value = uiList
            }
        }
    }
}

fun WalletEntity.toUiModel(): WalletModel {
    return WalletModel(
        id = this.id,
        name = this.name,
        type = this.type,
        balance = this.balance,
        isShared = this.isShared,
        currency = this.currency,
        createdAt = this.createdAt,
        gradient = getGradientByIndex(this.colorIndex),
        isArchived = this.isArchived
    )
}

fun getGradientByIndex(index: Int): Brush {
    val gradients = listOf(
        Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1B5E20))),
        Brush.linearGradient(listOf(Color(0xFF66BB6A), Color(0xFF33691E))),
        Brush.linearGradient(listOf(Color(0xFF009688), Color(0xFF004D40))),
        Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))),
        Brush.linearGradient(listOf(Color(0xFF039BE5), Color(0xFF01579B))),
        Brush.linearGradient(listOf(Color(0xFF42A5F5), Color(0xFF1565C0))),
        Brush.linearGradient(listOf(Color(0xFF3949AB), Color(0xFF1A237E))),
        Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))),
        Brush.linearGradient(listOf(Color(0xFFE53935), Color(0xFFB71C1C))),
        Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFBF360C))),
        Brush.linearGradient(listOf(Color(0xFFFFCA28), Color(0xFFFF6F00))),
        Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))),
        Brush.linearGradient(listOf(Color(0xFFBA68C8), Color(0xFF6A1B9A))),
        Brush.linearGradient(listOf(Color(0xFFEC407A), Color(0xFF880E4F))),
        Brush.linearGradient(listOf(Color(0xFF795548), Color(0xFF3E2723))),
        Brush.linearGradient(listOf(Color(0xFF78909C), Color(0xFF37474F))),
        Brush.linearGradient(listOf(Color(0xFF424242), Color(0xFF212121))),
        Brush.linearGradient(listOf(Color(0xFF546E7A), Color(0xFF263238)))
    )
    return if (index in gradients.indices) gradients[index] else gradients[0]
}