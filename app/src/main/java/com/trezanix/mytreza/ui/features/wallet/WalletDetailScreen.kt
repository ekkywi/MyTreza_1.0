package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailScreen(
    walletId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSeeAllClick: () -> Unit
) {
    val wallet = remember { getDummyWallets().find { it.id == walletId } ?: getDummyWallets()[0] }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 100.dp,
                bottom = 120.dp
            )
        ) {
            item {
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    WalletCardItem(wallet = wallet, showMenu = false)
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ActionButtons(
                    onEditClick = onEditClick,
                    onArchiveClick = { showArchiveDialog = true },
                    onDeleteClick = { showDeleteDialog = true }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                StatsCard()
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Transaksi",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BrandDarkText
                    )
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandPrimary,
                        modifier = Modifier
                            .clickable { onSeeAllClick() }
                            .padding(4.dp)
                    )
                }
            }
            items(10) { index ->
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    GlassTransactionItem(
                        title = if(index % 2 == 0) "Starbucks Coffee" else "Transfer Masuk",
                        amount = if(index % 2 == 0) "-Rp 55.000" else "+Rp 200.000",
                        isExpense = index % 2 == 0
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SurfaceColor,
                            SurfaceColor.copy(alpha = 0.9f),
                            SurfaceColor.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            CenterAlignedTopAppBar(
                title = { Text("Detail Dompet", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(SurfaceColor.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = BrandDarkText)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }

        if (showArchiveDialog) {
            TrezaAlertDialog(
                title = "Arsipkan Dompet?",
                text = "Dompet ini akan disembunyikan dari total saldo, tapi riwayat transaksi tetap tersimpan.",
                confirmText = "Arsipkan",
                dismissText = "Batal",
                icon = Icons.Outlined.Archive,
                confirmColor = Color(0xFFFB8C00),
                onConfirm = {
                    onArchiveClick()
                    showArchiveDialog = false
                },
                onDismiss = { showArchiveDialog = false }
            )
        }

        if (showDeleteDialog) {
            TrezaAlertDialog(
                title = "Hapus Dompet?",
                text = "Tindakan ini permanen. Semua riwayat transaksi di dompet ini akan hilang selamanya.",
                confirmText = "Hapus Permanen",
                dismissText = "Batal",
                icon = Icons.Outlined.Delete,
                confirmColor = Color(0xFFD32F2F),
                onConfirm = {
                    onDeleteClick()
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

@Composable
fun ActionButtons(
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButtonItem(
            text = "Edit",
            icon = Icons.Default.Edit,
            containerColor = SurfaceColor.copy(alpha = 0.8f),
            contentColor = BrandPrimary,
            borderColor = BrandPrimary.copy(alpha = 0.2f),
            onClick = onEditClick,
            modifier = Modifier.weight(1f)
        )
        ActionButtonItem(
            text = "Arsip",
            icon = Icons.Outlined.Archive,
            containerColor = Color(0xFFFFF3E0).copy(alpha = 0.8f),
            contentColor = Color(0xFFFB8C00),
            borderColor = Color(0xFFFB8C00).copy(alpha = 0.2f),
            onClick = onArchiveClick,
            modifier = Modifier.weight(1f)
        )

        ActionButtonItem(
            text = "Hapus",
            icon = Icons.Outlined.Delete,
            containerColor = Color(0xFFFFEBEE).copy(alpha = 0.8f),
            contentColor = Color(0xFFD32F2F),
            borderColor = Color.Transparent,
            onClick = onDeleteClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionButtonItem(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun TrezaAlertDialog(
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    icon: ImageVector,
    confirmColor: Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(icon, contentDescription = null, tint = confirmColor, modifier = Modifier.size(32.dp))
        },
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
        },
        text = {
            Text(text, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = TextHint)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = confirmColor, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(confirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(dismissText, color = TextHint, fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = SurfaceColor,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 6.dp
    )
}

@Composable
fun StatsCard() {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = SurfaceColor.copy(alpha = 0.6f)), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Bulan Ini", style = MaterialTheme.typography.labelLarge, color = TextHint)
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) { Text("Sehat", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32)) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(progress = { 0.4f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = BrandPrimary, trackColor = Color.White.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Keluar: Rp 2.5jt", style = MaterialTheme.typography.labelSmall, color = Color(0xFFE53935))
                Text("Masuk: Rp 15jt", style = MaterialTheme.typography.labelSmall, color = Color(0xFF43A047))
            }
        }
    }
}

@Composable
fun GlassTransactionItem(title: String, amount: String, isExpense: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(Color.White, CircleShape).border(1.dp, Color.Black.copy(0.05f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = if(isExpense) Icons.Default.ArrowOutward else Icons.Default.ArrowDownward, contentDescription = null, tint = if(isExpense) Color(0xFFE53935) else Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = BrandDarkText); Text("Hari ini, 10:00", style = MaterialTheme.typography.bodySmall, color = TextHint) }
        }
        Text(amount, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = if (isExpense) Color(0xFFE53935) else Color(0xFF43A047))
    }
}