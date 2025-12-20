package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.trezanix.mytreza.R

enum class FeatureID {
    TRANSACTION, SCAN, TRANSFER, WALLET,
    ANALYSIS, BUDGET, BILLS, HISTORY,
    GOALS, STOCK, GOLD, CRYPTO,
    DEBT, INSURANCE, TOOLS, MORE
}

data class TrezaMenuItem(
    val id: FeatureID,
    val title: Int,
    val icon: ImageVector,
    val color: Color,
    val isComingSoon: Boolean = false
)

object MenuProvider {
    val allMenus = listOf(
        TrezaMenuItem(
            id = FeatureID.TRANSACTION,
            title = R.string.dashboard_menu_transaction,
            icon = Icons.Default.ReceiptLong,
            color = Color(0xFF4CAF50),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.SCAN,
            title = R.string.dashboard_menu_scan,
            icon = Icons.Default.QrCodeScanner,
            color = Color(0xFF2196F3),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.TRANSFER,
            title = R.string.dashboard_menu_transfer_funds,
            icon = Icons.Default.SyncAlt,
            color = Color(0xFFFF9800),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.WALLET,
            title = R.string.dashboard_menu_wallet,
            icon = Icons.Default.AccountBalanceWallet,
            color = Color(0xFF9C27B0)
        ),
        TrezaMenuItem(
            id = FeatureID.ANALYSIS,
            title = R.string.dashboard_menu_analysis,
            icon = Icons.Default.PieChart,
            color = Color(0xFFE91E63),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.BUDGET,
            title = R.string.dashboard_menu_budget,
            icon = Icons.Default.Savings,
            color = Color(0xFF00BCD4),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.BILLS,
            title = R.string.dashboard_menu_bill,
            icon = Icons.Default.Event,
            color = Color(0xFF673AB7),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.HISTORY,
            title = R.string.dashboard_menu_history,
            icon = Icons.Default.History,
            color = Color(0xFF795548),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.GOALS,
            title = R.string.dashboard_menu_goals,
            icon = Icons.Default.Flag,
            color = Color(0xFFFF5722),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.STOCK,
            title = R.string.dashboard_menu_stock,
            icon = Icons.Default.ShowChart,
            color = Color(0xFF3F51B5),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.GOLD,
            title = R.string.dashboard_menu_gold,
            icon = Icons.Default.Stars,
            color = Color(0xFFFFC107),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.CRYPTO,
            title = R.string.dashboard_menu_crypto,
            icon = Icons.Default.CurrencyBitcoin,
            color = Color(0xFF607D8B),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.DEBT,
            title = R.string.dashboard_menu_debt,
            icon = Icons.Default.MoneyOff,
            color = Color(0xFFF44336),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.INSURANCE,
            title = R.string.dashboard_menu_protection,
            icon = Icons.Default.Shield,
            color = Color(0xFF009688),
            isComingSoon = true
        ),
        TrezaMenuItem(
            id = FeatureID.TOOLS,
            title = R.string.dashboard_menu_tools,
            icon = Icons.Default.Calculate,
            color = Color(0xFF9E9E9E),
            isComingSoon = true
        )
    )

    val moreItem =
        TrezaMenuItem(
            id = FeatureID.MORE,
            title = R.string.dashboard_menu_other,
            Icons.Default.GridView,
            Color(0xFF424242))
}