package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trezanix.mytreza.data.local.entity.WalletEntity
import com.trezanix.mytreza.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    val walletListState: StateFlow<List<WalletModel>> = repository.getAllWallets()
        .map { entities ->
            entities.map { it.toUiModel() }
        }
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
        colorIndex: Int
    ) {
        viewModelScope.launch {
            val walletId = id ?: UUID.randomUUID().toString()
            val createdAt = _currentWallet.value?.createdAt ?: "01/24" // TODO: Pakai Date Formatter asli nanti

            val newWallet = WalletEntity(
                id = walletId,
                name = name,
                type = type,
                balance = balance,
                isShared = isShared,
                currency = "IDR",
                createdAt = createdAt,
                colorIndex = colorIndex
                // userId = "local_default" (Nanti pas ada Auth)
            )

            repository.insertWallet(newWallet)
        }
    }

    fun deleteWallet(id: String) {
        viewModelScope.launch {
            repository.deleteWalletById(id)
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
        gradient = getGradientByIndex(this.colorIndex)
    )
}

fun getGradientByIndex(index: Int): Brush {
    val gradients = listOf(
        Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1B5E20))), // 0. Green
        Brush.linearGradient(listOf(Color(0xFF66BB6A), Color(0xFF33691E))), // 1. Light Green
        Brush.linearGradient(listOf(Color(0xFF009688), Color(0xFF004D40))), // 2. Teal
        Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))), // 3. Blue (Trust)
        Brush.linearGradient(listOf(Color(0xFF039BE5), Color(0xFF01579B))), // 4. Light Blue
        Brush.linearGradient(listOf(Color(0xFF42A5F5), Color(0xFF1565C0))), // 5. Sky Blue
        Brush.linearGradient(listOf(Color(0xFF3949AB), Color(0xFF1A237E))), // 6. Indigo
        Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))), // 7. Orange
        Brush.linearGradient(listOf(Color(0xFFE53935), Color(0xFFB71C1C))), // 8. Red
        Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFBF360C))), // 9. Deep Orange
        Brush.linearGradient(listOf(Color(0xFFFFCA28), Color(0xFFFF6F00))), // 10. Amber
        Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))), // 11. Purple
        Brush.linearGradient(listOf(Color(0xFFBA68C8), Color(0xFF6A1B9A))), // 12. Light Purple
        Brush.linearGradient(listOf(Color(0xFFEC407A), Color(0xFF880E4F))), // 13. Pink
        Brush.linearGradient(listOf(Color(0xFF795548), Color(0xFF3E2723))), // 14. Brown
        Brush.linearGradient(listOf(Color(0xFF78909C), Color(0xFF37474F))), // 15. Blue Grey Light
        Brush.linearGradient(listOf(Color(0xFF424242), Color(0xFF212121))), // 16. Dark Grey
        Brush.linearGradient(listOf(Color(0xFF546E7A), Color(0xFF263238)))  // 17. Blue Grey Dark
    )
    return if (index in gradients.indices) gradients[index] else gradients[0]
}

