package com.trezanix.mytreza.ui.features.analysis.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.analysis.InsightData
import com.trezanix.mytreza.ui.features.analysis.InsightType

@Composable
fun InsightCard(insight: InsightData) {
    val (bgColor, iconColor) = when (insight.type) {
        InsightType.GOOD -> Color(0xFFE8F5E9).copy(alpha = 0.9f) to Color(0xFF2E7D32)
        InsightType.WARNING -> Color(0xFFFFF3E0).copy(alpha = 0.9f) to Color(0xFFEF6C00)
        InsightType.DANGER -> Color(0xFFFFEBEE).copy(alpha = 0.9f) to Color(0xFFC62828)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (insight.type == InsightType.GOOD) Icons.Default.Info else Icons.Default.Warning,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = insight.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = iconColor)
                Text(text = insight.message, style = MaterialTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.6f))
            }
        }
    }
}