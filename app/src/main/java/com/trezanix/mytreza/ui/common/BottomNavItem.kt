package com.trezanix.mytreza.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("Dashboard", Icons.Default.Dashboard)
    object Analysis : BottomNavItem("Analysis", Icons.Default.Analytics)
    object Wallet : BottomNavItem("Wallet", Icons.Default.AccountBalanceWallet)
    object Profile : BottomNavItem("Profile", Icons.Default.Person)
}