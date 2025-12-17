package com.trezanix.mytreza.ui.features.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class TransactionType { INCOME, EXPENSE }

data class Transaction(
    val id: String,
    val description: String?,
    val category: String,
    val amount: Double,
    val dateTime: String,
    val type: TransactionType,
    val icon: ImageVector,
    val color: Color
)

val now = LocalDateTime.now()

val mockTransactions = listOf(
    Transaction(
        "1", "Sarapan Bubur", "Makanan", 15000.0,
        now.minusHours(1).toString(),
        TransactionType.EXPENSE, Icons.Default.Fastfood, Color(0xFFFFA726)
    ),
    Transaction(
        "2", "Gaji Bonus", "Gaji", 5000000.0,
        now.minusDays(1).withHour(9).withMinute(0).toString(),
        TransactionType.INCOME, Icons.Default.Work, Color(0xFF66BB6A)
    ),
    Transaction(
        "3", "Netflix Premium", "Tagihan", 186000.0,
        now.minusDays(2).withHour(20).withMinute(15).toString(),
        TransactionType.EXPENSE, Icons.Default.ShoppingBag, Color(0xFFAB47BC)
    ),
    Transaction(
        "4", "Kembang Api (Tahun Lalu)", "Hiburan", 500000.0,
        now.minusYears(1).withMonth(12).withDayOfMonth(31).withHour(23).withMinute(50).toString(),
        TransactionType.EXPENSE, Icons.Default.Stars, Color(0xFFEF5350)
    ),
    Transaction(
        "5", "Investasi Awal", "Investasi", 10000000.0,
        "2022-05-20T10:00:00",
        TransactionType.EXPENSE, Icons.Default.ShowChart, Color(0xFF42A5F5)
    )
)

@Composable
fun TransactionHistory() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaksi Terkini",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            TextButton(onClick = { /* Navigate to Full History */ }) {
                Text(
                    text = "Lihat Semua",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            mockTransactions.take(5).forEach { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun TransactionItem(item: Transaction) {
    val displayTitle = if (item.description.isNullOrEmpty()) item.category else item.description
    val displaySubtitle = if (!item.description.isNullOrEmpty()) item.category else null

    val amountColor = if (item.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFC62828)
    val amountPrefix = if (item.type == TransactionType.INCOME) "+ " else "- "

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(item.color.copy(alpha = 0.15f))
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayTitle,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF212121),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (displaySubtitle != null) {
                Text(
                    text = displaySubtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$amountPrefix${formatCurrency(item.amount)}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = amountColor
            )
            Text(
                text = formatDateTimeDisplay(item.dateTime),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color.Gray
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    return format.format(amount).replace("Rp", "Rp")
}

fun formatDateTimeDisplay(isoString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoString)
        val now = LocalDateTime.now()

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeStr = dateTime.format(timeFormatter)

        val datePart = when {
            dateTime.toLocalDate().isEqual(now.toLocalDate()) -> "Hari ini"
            dateTime.toLocalDate().isEqual(now.minusDays(1).toLocalDate()) -> "Kemarin"
            dateTime.year != now.year -> { // Tahun Beda
                val yearFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
                dateTime.format(yearFormatter)
            }
            else -> { // Tahun Sama
                val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale("id", "ID"))
                dateTime.format(dateFormatter)
            }
        }

        "$datePart • $timeStr"

    } catch (e: Exception) {
        isoString
    }
}