package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class FeatureID {
    TRANSACTION, SCAN, TRANSFER, WALLET,
    ANALYSIS, BUDGET, BILLS, HISTORY,
    GOALS, STOCK, GOLD, CRYPTO,
    DEBT, INSURANCE, TOOLS, MORE
}

data class TrezaMenuItem(
    val id: FeatureID,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val isComingSoon: Boolean = false
)

object MenuProvider {
    val allMenus = listOf(
        TrezaMenuItem(FeatureID.TRANSACTION, "Transaksi", Icons.Default.ReceiptLong, Color(0xFF4CAF50)),
        TrezaMenuItem(FeatureID.SCAN, "Scan", Icons.Default.QrCodeScanner, Color(0xFF2196F3)),
        TrezaMenuItem(FeatureID.TRANSFER, "Pindah Dana", Icons.Default.SyncAlt, Color(0xFFFF9800)),
        TrezaMenuItem(FeatureID.WALLET, "Dompet", Icons.Default.AccountBalanceWallet, Color(0xFF9C27B0)),

        TrezaMenuItem(FeatureID.ANALYSIS, "Analisis", Icons.Default.PieChart, Color(0xFFE91E63)),
        TrezaMenuItem(FeatureID.BUDGET, "Budget", Icons.Default.Savings, Color(0xFF00BCD4)),
        TrezaMenuItem(FeatureID.BILLS, "Tagihan", Icons.Default.Event, Color(0xFF673AB7)),
        TrezaMenuItem(FeatureID.HISTORY, "Riwayat", Icons.Default.History, Color(0xFF795548)),

        TrezaMenuItem(FeatureID.GOALS, "Goals", Icons.Default.Flag, Color(0xFFFF5722)),
        TrezaMenuItem(FeatureID.STOCK, "Saham", Icons.Default.ShowChart, Color(0xFF3F51B5)),
        TrezaMenuItem(FeatureID.GOLD, "Emas", Icons.Default.Stars, Color(0xFFFFC107)),
        TrezaMenuItem(FeatureID.CRYPTO, "Kripto", Icons.Default.CurrencyBitcoin, Color(0xFF607D8B)),

        TrezaMenuItem(FeatureID.DEBT, "Utang", Icons.Default.MoneyOff, Color(0xFFF44336), true),
        TrezaMenuItem(FeatureID.INSURANCE, "Proteksi", Icons.Default.Shield, Color(0xFF009688), true),
        TrezaMenuItem(FeatureID.TOOLS, "Tools", Icons.Default.Calculate, Color(0xFF9E9E9E), true),
    )

    val moreItem = TrezaMenuItem(FeatureID.MORE, "Lainnya", Icons.Default.GridView, Color(0xFF424242))
}