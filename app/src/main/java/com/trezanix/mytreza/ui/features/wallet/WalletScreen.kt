package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.ui.theme.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

data class WalletModel(
    val id: Int,
    val name: String,
    val type: String,
    val balance: Double,
    val accountNumber: String,
    val gradient: Brush
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onWalletClick: (Int) -> Unit = {},
    onAddWalletClick: () -> Unit = {}
) {
    val wallets = remember { getDummyWallets() }
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

@Composable
fun WalletCardItem(wallet: WalletModel, showMenu: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(wallet.gradient)
        ) {
            PremiumCardPattern()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CardChip()
                        ContactlessSymbol()
                    }

                    if (showMenu) {
                        Icon(Icons.Default.MoreVert, null, tint = Color.White.copy(0.8f))
                    } else {
                        Text(
                            text = "TREZA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(wallet.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(wallet.type.uppercase(), modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 10.sp), color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Column {
                            Text(text = wallet.accountNumber, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontWeight = FontWeight.Medium), color = Color.White.copy(alpha = 0.7f), letterSpacing = 3.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "VALID THRU 12/28", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        }
                        Text(
                            text = formatRupiah(wallet.balance),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black.copy(alpha = 0.2f), offset = Offset(2f, 2f), blurRadius = 4f)
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardChip() {
    Box(
        modifier = Modifier
            .size(width = 42.dp, height = 30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(brush = Brush.linearGradient(colors = listOf(Color(0xFFD4AF37), Color(0xFFFFDF80), Color(0xFFD4AF37)), start = Offset(0f, 0f), end = Offset(100f, 100f)))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 1.dp.toPx(); val lineColor = Color.Black.copy(alpha = 0.2f)
            drawLine(lineColor, Offset(0f, size.height * 0.33f), Offset(size.width, size.height * 0.33f), stroke)
            drawLine(lineColor, Offset(0f, size.height * 0.66f), Offset(size.width, size.height * 0.66f), stroke)
            drawLine(lineColor, Offset(size.width * 0.5f, 0f), Offset(size.width * 0.5f, size.height * 0.33f), stroke)
            drawLine(lineColor, Offset(size.width * 0.5f, size.height * 0.66f), Offset(size.width * 0.5f, size.height), stroke)
            drawRect(color = lineColor, topLeft = Offset(size.width * 0.25f, size.height * 0.33f), size = androidx.compose.ui.geometry.Size(size.width * 0.5f, size.height * 0.33f), style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
        }
    }
}

@Composable
fun ContactlessSymbol() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val centerX = size.width; val centerY = size.height / 2; val baseRadius = size.width * 0.3f; val strokeWidth = 1.5.dp.toPx(); val color = Color.White.copy(alpha = 0.6f)
        for (i in 0..2) {
            val radius = baseRadius + (i * 8.dp.toPx())
            drawArc(color = color, startAngle = 135f, sweepAngle = 90f, useCenter = false, topLeft = Offset(centerX - radius, centerY - radius), size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2), style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round))
        }
        drawCircle(color, radius = 1.5.dp.toPx(), center = Offset(centerX - baseRadius + 4.dp.toPx(), centerY))
    }
}

@Composable
fun PremiumCardPattern() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val path1 = Path().apply { moveTo(0f, height * 0.6f); cubicTo(width * 0.3f, height * 0.4f, width * 0.7f, height * 0.9f, width, height * 0.5f); lineTo(width, height); lineTo(0f, height); close() }
        drawPath(path1, Color.Black.copy(alpha = 0.08f))

        val path2 = Path().apply { moveTo(0f, height * 0.45f); cubicTo(width * 0.25f, height * 0.25f, width * 0.6f, height * 0.7f, width, height * 0.3f); lineTo(width, height); lineTo(0f, height); close() }
        drawPath(path2, Color.White.copy(alpha = 0.05f))

        drawCircle(brush = Brush.radialGradient(colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent), center = Offset(0f, 0f), radius = width * 0.6f), center = Offset(0f, 0f), radius = width * 0.6f)

        repeat(300) {
            val randomAlpha = Random.nextFloat() * (0.08f - 0.02f) + 0.02f
            val randomRadius = Random.nextFloat() * (1.5f - 0.5f) + 0.5f
            val randomX = Random.nextFloat() * width
            val randomY = Random.nextFloat() * height
            drawCircle(color = Color.White.copy(alpha = randomAlpha), radius = randomRadius.dp.toPx(), center = Offset(randomX, randomY))
        }
    }
}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount).replace("Rp", "Rp ").substringBefore(",00")
}

fun getDummyWallets(): List<WalletModel> {
    return listOf(
        WalletModel(1, "Main Cash", "Tunai", 1500000.0, "CASH 8890", Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1B5E20)))),
        WalletModel(2, "Bank BCA", "Debit Card", 12500000.0, "**** 4211", Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1)))),
        WalletModel(3, "GoPay", "E-Wallet", 450000.0, "0812****99", Brush.linearGradient(listOf(Color(0xFF039BE5), Color(0xFF01579B)))),
        WalletModel(4, "Jago Pockets", "Savings", 50000000.0, "JAGO 2201", Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))))
    )
}