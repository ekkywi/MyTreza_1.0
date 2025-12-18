package com.trezanix.mytreza.ui.features.analysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun MonthlyComparisonCard(
    currentExpense: Double,
    lastMonthExpense: Double
) {
    val diff = currentExpense - lastMonthExpense
    val isHigher = diff > 0
    val percentage = if (lastMonthExpense > 0) (diff / lastMonthExpense * 100).toInt() else 0

    val statusText = if (isHigher) "Naik ${percentage}%" else "Turun ${Math.abs(percentage)}%"
    val statusColor = if (isHigher) Color(0xFFC62828) else Color(0xFF2E7D32)
    val barColor = statusColor

    val maxValue = max(currentExpense, lastMonthExpense)
    val safeMax = if (maxValue == 0.0) 1.0 else maxValue
    val currentHeight = (currentExpense / safeMax).toFloat()
    val lastHeight = (lastMonthExpense / safeMax).toFloat()

    val glassCardColor = Color.White.copy(alpha = 0.9f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = glassCardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Vs Bulan Lalu", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = statusColor
                    )
                }
                Text(
                    text = (if (isHigher) "+" else "") + formatCurrencyCompact(diff),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                BarItem(
                    label = "Bulan Lalu",
                    amount = lastMonthExpense,
                    heightRatio = lastHeight,
                    color = Color.LightGray
                )

                BarItem(
                    label = "Bulan Ini",
                    amount = currentExpense,
                    heightRatio = currentHeight,
                    color = barColor
                )
            }
        }
    }
}

@Composable
private fun RowScope.BarItem(
    label: String,
    amount: Double,
    heightRatio: Float,
    color: Color
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = formatCurrencyCompact(amount),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(100.dp)
                .width(40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = max(0.05f, heightRatio))
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(color)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black
        )
    }
}