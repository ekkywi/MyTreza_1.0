package com.trezanix.mytreza.ui.features.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.dashboard.components.*

@Composable
fun DashboardScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DashboardHeader()

        Spacer(modifier = Modifier.height(12.dp))

        WealthCard()

        ActionMenuGrid()

        TransactionHistory()
    }
}