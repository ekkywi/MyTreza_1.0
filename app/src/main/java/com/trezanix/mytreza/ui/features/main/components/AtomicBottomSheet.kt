package com.trezanix.mytreza.ui.features.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.ui.theme.BrandPrimary
import com.trezanix.mytreza.ui.theme.BrandSecondary
import com.trezanix.mytreza.ui.theme.SurfaceColor

data class AtomicAction(
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun AtomicBottomSheetContent(
    onDismiss: () -> Unit
) {
    val actions = listOf(
        AtomicAction("Transaksi", Icons.Default.Add, BrandPrimary) { /* TODO */ },
        AtomicAction("Transfer", Icons.Default.SwapHoriz, BrandSecondary) { /* TODO */ },
        AtomicAction("Scan QR", Icons.Default.QrCodeScanner, Color(0xFFF2994A)) { /* TODO */ },
        AtomicAction("Top Up", Icons.Default.AccountBalanceWallet, Color(0xFF27AE60)) { /* TODO */ },
        AtomicAction("Budget", Icons.Default.PieChart, Color(0xFFEB5757)) { /* TODO */ },
        AtomicAction("Analisis", Icons.Default.Analytics, Color(0xFF9B51E0)) { /* TODO */ },
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 48.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
        }

        Text(
            text = "Aksi Cepat",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(actions) { action ->
                AtomicActionItem(action)
            }
        }
    }
}

@Composable
fun AtomicActionItem(action: AtomicAction) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { action.onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(action.color.copy(alpha = 0.15f))
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = action.color,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = action.label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}