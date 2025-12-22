package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.ui.theme.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random
import java.util.UUID

// ✅ MODEL FINAL (Updated)
data class WalletModel(
    val id: String,
    val name: String,
    val type: String,
    val balance: Double,
    val gradient: Brush,
    val isShared: Boolean = false,
    val currency: String = "IDR",
    val createdAt: String = "01/24" // Format MM/YY ala Kartu Kredit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onWalletClick: (String) -> Unit = {},
    onAddWalletClick: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    val wallets by viewModel.walletListState.collectAsState()
    val totalBalance = wallets.sumOf { it.balance }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 100.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { TotalBalanceGlassCard(totalBalance) }
            item {
                Text(
                    text = "Daftar Akun",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 24.dp, top = 8.dp),
                    color = TextPrimary
                )
            }

            if (wallets.isEmpty()) {
                item {
                    Text(
                        text = "Belum ada dompet. Tambah sekarang!",
                        modifier = Modifier.padding(24.dp),
                        color = TextHint
                    )
                }
            }

            items(wallets) { wallet ->
                Box(modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clickable { onWalletClick(wallet.id) }
                ) {
                    WalletCardItem(wallet)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SurfaceColor.copy(alpha = 0.95f),
                            SurfaceColor.copy(alpha = 0.8f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Dompet Saya",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BrandDarkText
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun TotalBalanceGlassCard(total: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor.copy(alpha = 0.75f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Kekayaan Bersih", style = MaterialTheme.typography.labelMedium, color = TextHint)
            Text(formatRupiah(total), style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp), color = BrandDarkText)
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Black.copy(0.05f))
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryItem("Pemasukan", "+Rp 15.000.000", Color(0xFF4CAF50), Icons.Default.ArrowDownward)
                Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.Black.copy(0.1f)))
                SummaryItem("Pengeluaran", "-Rp 4.500.000", Color(0xFFE53935), Icons.Default.ArrowUpward)
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, amount: String, color: Color, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextHint)
        }
        Text(text = amount, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = BrandDarkText)
    }
}

// ✅ LAYOUT FINAL: REAL CREDIT CARD STYLE (FIXED INFO)
@Composable
fun WalletCardItem(wallet: WalletModel, showMenu: Boolean = false) {
    // Format UUID agar terlihat seperti nomor kartu
    val formattedCardNumber = remember(wallet.id) {
        wallet.id.replace("-", "").take(16).chunked(4).joinToString("  ")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(wallet.gradient)
        ) {
            // 1. Pattern Background
            PremiumCardPattern()

            // Overlay Vignette
            Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(Color.Transparent, Color.Black.copy(0.3f)), radius = 800f)))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // --- BARIS 1: BRAND & CONTACTLESS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Kiri Atas: Logo & Wallet Type
                    Column {
                        Text(
                            text = "TREZA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        // ✅ TIPE WALLET (Di bawah logo, mirip tulisan "DEBIT" di kartu asli)
                        Text(
                            text = wallet.type.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    // Kanan Atas: Contactless / Menu
                    if (showMenu) {
                        Icon(Icons.Default.MoreVert, null, tint = Color.White, modifier = Modifier.clickable { /* Menu */ })
                    } else {
                        ContactlessSymbol()
                    }
                }

                // --- BARIS 2: EMV CHIP ---
                Spacer(modifier = Modifier.height(18.dp))
                // Logika Chip: Emas jika Shared/Savings
                val useGoldChip = wallet.isShared || wallet.type == "Savings" || wallet.type == "Investment"
                EmvChip(isGold = useGoldChip)

                Spacer(modifier = Modifier.height(14.dp))

                // --- BARIS 3: NOMOR KARTU (UUID) ---
                Text(
                    text = formattedCardNumber.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp,
                        shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black.copy(0.5f), offset = Offset(1f, 1f), blurRadius = 2f)
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                // --- BARIS 4: INFO BAWAH ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Kiri Bawah: Member Since & Nama
                    Column {
                        // ✅ MEMBER SINCE (Pengganti Valid Thru)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MEMBER\nSINCE", // Istilah umum di kartu kredit
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 5.sp, lineHeight = 6.sp),
                                color = Color.White.copy(0.7f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Tanggal Pembuatan
                            Text(
                                text = wallet.createdAt,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Nama Pemilik
                        Text(
                            text = wallet.name.uppercase(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace,
                                shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black.copy(0.5f), offset = Offset(1f, 1f), blurRadius = 2f)
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Kanan Bawah: Currency & Balance
                    Column(horizontalAlignment = Alignment.End) {
                        // ✅ CURRENCY CODE & SHARED BADGE
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if(wallet.isShared) {
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Text("SHARED", modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.dp), style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, fontWeight = FontWeight.Bold, color = BrandPrimary))
                                }
                            }
                            Text(
                                text = wallet.currency, // IDR / USD
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Saldo
                        Text(
                            text = formatRupiah(wallet.balance),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// --- VISUAL ELEMENTS ---

@Composable
fun EmvChip(isGold: Boolean = false) {
    val gradientColors = if (isGold) {
        listOf(Color(0xFFFFECB3), Color(0xFFFFCA28), Color(0xFFFFECB3))
    } else {
        listOf(Color(0xFFF5F5F5), Color(0xFFBDBDBD), Color(0xFFEEEEEE))
    }
    val borderColor = if (isGold) Color(0xFF8D6E63).copy(0.3f) else Color.Black.copy(0.1f)

    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 35.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(brush = Brush.linearGradient(colors = gradientColors, start = Offset(0f, 0f), end = Offset(100f, 100f)))
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 1.dp.toPx()
            val lineColor = if (isGold) Color(0xFF5D4037).copy(0.2f) else Color.Black.copy(0.25f)
            val w = size.width; val h = size.height

            drawLine(lineColor, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), stroke)
            drawLine(lineColor, Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), stroke)
            drawLine(lineColor, Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), stroke)
            drawRoundRect(color = lineColor, topLeft = Offset(w * 0.25f, h * 0.25f), size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f), style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
        }
    }
}

@Composable
fun ContactlessSymbol() {
    Canvas(modifier = Modifier.size(24.dp).rotate(90f)) {
        val centerX = size.width / 2; val centerY = size.height; val baseRadius = size.width * 0.3f; val strokeWidth = 2.dp.toPx(); val color = Color.White.copy(alpha = 0.8f)
        for (i in 0..2) {
            val radius = baseRadius + (i * 10.dp.toPx())
            drawArc(color = color, startAngle = -135f, sweepAngle = 90f, useCenter = false, topLeft = Offset(centerX - radius, centerY - radius + (radius/2)), size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2), style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round))
        }
    }
}

@Composable
fun PremiumCardPattern() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width; val height = size.height
        val path1 = Path().apply { moveTo(0f, height * 0.6f); cubicTo(width * 0.3f, height * 0.4f, width * 0.7f, height * 0.9f, width, height * 0.5f); lineTo(width, height); lineTo(0f, height); close() }
        drawPath(path1, Color.Black.copy(alpha = 0.08f))
        val path2 = Path().apply { moveTo(0f, height * 0.45f); cubicTo(width * 0.25f, height * 0.25f, width * 0.6f, height * 0.7f, width, height * 0.3f); lineTo(width, height); lineTo(0f, height); close() }
        drawPath(path2, Color.White.copy(alpha = 0.05f))
        drawCircle(brush = Brush.radialGradient(colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent), center = Offset(0f, 0f), radius = width * 0.6f), center = Offset(0f, 0f), radius = width * 0.6f)
        repeat(300) {
            val randomAlpha = Random.nextFloat() * (0.08f - 0.02f) + 0.02f; val randomRadius = Random.nextFloat() * (1.5f - 0.5f) + 0.5f; val randomX = Random.nextFloat() * width; val randomY = Random.nextFloat() * height
            drawCircle(color = Color.White.copy(alpha = randomAlpha), radius = randomRadius.dp.toPx(), center = Offset(randomX, randomY))
        }
    }
}

fun Modifier.rotate(degrees: Float) = this.then(Modifier.graphicsLayer(rotationZ = degrees))

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount).replace("Rp", "Rp ").substringBefore(",00")
}
