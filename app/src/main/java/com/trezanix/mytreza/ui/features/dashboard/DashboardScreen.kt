package com.trezanix.mytreza.ui.features.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.dashboard.components.*

@Composable
fun DashboardScreen() {
    var userMenus by remember { mutableStateOf(MenuProvider.allMenus) }

    var showCustomizeScreen by remember { mutableStateOf(false) }

    if (showCustomizeScreen) {
        MenuCustomizationScreen(
            currentMenus = userMenus,
            onSave = { newOrder ->
                userMenus = newOrder
                showCustomizeScreen = false
            },
            onBack = { showCustomizeScreen = false }
        )
    } else {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            DashboardHeader()

            Spacer(modifier = Modifier.height(12.dp))

            WealthCard()

            DashboardMenuSection(
                userMenuList = userMenus,
                onMenuClick = { featureId ->
                    // TODO: Handle Navigasi ke Fitur disini
                    when (featureId) {
                        FeatureID.WALLET -> { /* NavController.navigate("wallet") */ }
                        FeatureID.TRANSACTION -> { /* NavController.navigate("transaction") */ }
                        // dst...
                        else -> { /* Coming Soon Toast */ }
                    }
                },
                onEditMenuClick = {
                    showCustomizeScreen = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransactionHistory()

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}