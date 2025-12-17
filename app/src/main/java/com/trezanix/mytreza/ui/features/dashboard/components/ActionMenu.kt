package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.ui.theme.BrandPrimary
import com.trezanix.mytreza.ui.theme.BrandSecondary
import com.trezanix.mytreza.ui.theme.SurfaceColor

data class MenuItem(val label: String, val icon: ImageVector, val color: Color)

@Composable
fun ActionMenuGrid() {
    val menus = listOf(
        MenuItem("Transfer", Icons.Default.Send, BrandPrimary),
        MenuItem("Top Up", Icons.Default.AccountBalanceWallet, BrandSecondary),
        MenuItem("QRIS", Icons.Default.QrCodeScanner, Color(0xFFF2994A)),
        MenuItem("Bill", Icons.Default.ReceiptLong, Color(0xFFEB5757)),
        MenuItem("Invest", Icons.Default.ShowChart, Color(0xFF27AE60)),
        MenuItem("Budget", Icons.Default.PieChart, Color(0xFF9B51E0)),
        MenuItem("History", Icons.Default.History, Color(0xFF2D9CDB)),
        MenuItem("Lainnya", Icons.Default.MoreHoriz, MaterialTheme.colorScheme.onSurface)
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            menus.take(4).forEach { MenuIconItem(it) }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            menus.takeLast(4).forEach { MenuIconItem(it) }
        }
    }
}

@Composable
fun MenuIconItem(item: MenuItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceColor)
                .clickable { /* Handle Click */ }
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = item.color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}