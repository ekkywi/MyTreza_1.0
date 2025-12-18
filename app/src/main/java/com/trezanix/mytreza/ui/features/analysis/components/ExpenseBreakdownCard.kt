package com.trezanix.mytreza.ui.features.analysis.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.analysis.CategoryExpense

@Composable
fun ExpenseBreakdownCard(
    categories: List<CategoryExpense>,
    totalExpense: Double
) {
    val glassCardColor = Color.White.copy(alpha = 0.9f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = glassCardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                DonutChartSection(categories, totalExpense)
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.3f)
            )

            Text(
                text = "Detail Kategori",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            categories.forEach { category ->
                CategoryExpenseItem(category)

                if (category != categories.last()) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}