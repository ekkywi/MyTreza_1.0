package com.trezanix.mytreza.ui.features.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.data.local.entity.TransactionEntity
import com.trezanix.mytreza.ui.theme.BrandPrimary
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class TransactionUiItem(
    val id: String,
    val title: String,
    val date: String,
    val amount: Double,
    val type: String,
    val icon: ImageVector = Icons.Default.ShoppingBag,
    val colorHex: String? = null
)

@Composable
fun TransactionHistoryList(
    transactions: List<TransactionUiItem>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaction History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            TextButton(onClick = { /* TODO: See All */ }) {
                Text("See All", color = BrandPrimary)
            }
        }
        if (transactions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("No Transaction History", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                transactions.forEach { item ->
                    TransactionItemRow(item = item, onClick = { onItemClick(item.id) })
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TransactionItemRow(
    item: TransactionUiItem,
    onClick: () -> Unit
) {
    val isExpense = item.type == "EXPENSE"
    val amountColor = if (isExpense) Color(0xFFFF5252) else Color(0xFF4CAF50)
    val amountPrefix = if (isExpense) "- " else "+ "

    val formattedAmount = try {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.format(item.amount)
    } catch (e: Exception) { "Rp ${item.amount}" }

    val formattedDate = try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        val dateObj = parser.parse(item.date)
        formatter.format(dateObj ?: java.util.Date())
    } catch (e: Exception) { item.date }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (item.colorHex != null) try { Color(android.graphics.Color.parseColor(item.colorHex)).copy(0.1f) } catch (e:Exception){Color.Gray.copy(0.1f)} else amountColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isExpense) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = if (item.colorHex != null) try { Color(android.graphics.Color.parseColor(item.colorHex)) } catch (e:Exception){Color.Gray} else amountColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(
            text = "$amountPrefix$formattedAmount",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = amountColor
        )
    }
}

