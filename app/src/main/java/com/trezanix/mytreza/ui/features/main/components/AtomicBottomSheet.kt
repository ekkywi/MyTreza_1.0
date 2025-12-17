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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // Definisi 12 Aksi "Tambah Data" (Create New)
    // Warna disesuaikan dengan MenuData.kt agar konsisten
    val actions = listOf(
        // Row 1: Daily Operations
        AtomicAction("Scan", Icons.Default.QrCodeScanner, Color(0xFF2196F3)) { /* Buka Kamera */ },
        AtomicAction("Transaksi", Icons.Default.ReceiptLong, Color(0xFF4CAF50)) { /* Form Transaksi */ },
        AtomicAction("Pindah", Icons.Default.SyncAlt, Color(0xFFFF9800)) { /* Form Transfer */ },
        AtomicAction("Dompet", Icons.Default.AccountBalanceWallet, Color(0xFF9C27B0)) { /* Form Tambah Akun Bank */ },

        // Row 2: Planning & Control
        AtomicAction("Budget", Icons.Default.Savings, Color(0xFF00BCD4)) { /* Form Tambah Budget */ },
        AtomicAction("Tagihan", Icons.Default.Event, Color(0xFF673AB7)) { /* Form Tambah Tagihan */ },
        AtomicAction("Goals", Icons.Default.Flag, Color(0xFFFF5722)) { /* Form Buat Goal Baru */ },
        AtomicAction("Utang", Icons.Default.MoneyOff, Color(0xFFF44336)) { /* Form Catat Utang Baru */ },

        // Row 3: Asset & Protection
        AtomicAction("Saham", Icons.Default.ShowChart, Color(0xFF3F51B5)) { /* Form Beli Saham */ },
        AtomicAction("Emas", Icons.Default.Stars, Color(0xFFFFC107)) { /* Form Beli Emas */ },
        AtomicAction("Kripto", Icons.Default.CurrencyBitcoin, Color(0xFF607D8B)) { /* Form Beli Kripto */ },
        AtomicAction("Proteksi", Icons.Default.Shield, Color(0xFF009688)) { /* Form Tambah Asuransi */ }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceColor)
            .padding(bottom = 48.dp) // Padding bawah untuk safe area navigasi
    ) {
        // Drag Handle
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

        // Header
        Text(
            text = "Tambah Data Baru",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Text(
            text = "Pilih kategori aktivitas yang ingin dicatat",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp)
        )

        // Grid 4 Kolom x 3 Baris = 12 Items
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // 4 Kolom biar muat banyak
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 400.dp) // Height dinamis cukup
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
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { action.onClick() }
            .padding(4.dp)
    ) {
        // Icon Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp) // Ukuran sedikit lebih kecil (52dp) biar muat 4 kolom
                .clip(RoundedCornerShape(18.dp)) // Squircle
                .background(action.color.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = action.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Label Text
        Text(
            text = action.label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), // Font kecil rapi
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}