package com.trezanix.mytreza.ui.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.features.dashboard.components.TrezaMenuItem
import com.trezanix.mytreza.ui.theme.BrandPrimary
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCustomizationScreen(
    currentMenus: List<TrezaMenuItem>,
    onSave: (List<TrezaMenuItem>) -> Unit,
    onBack: () -> Unit
) {
    var editableList by remember { mutableStateOf(currentMenus.toMutableList()) }

    val primaryTextColor = Color(0xFF212121)
    val secondaryTextColor = Color(0xFF757575)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atur Menu Dashboard", style = MaterialTheme.typography.titleMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = primaryTextColor
                ),
                actions = {
                    TextButton(onClick = { onSave(editableList) }) {
                        Text("Simpan", color = BrandPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(BrandPrimary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "7 Menu teratas akan muncul di halaman utama. Gunakan panah untuk mengatur prioritas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BrandPrimary
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(editableList) { index, item ->
                    val isTop7 = index < 7

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .background(
                                    color = if (isTop7) Color(0xFFF5F5F5) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isTop7) BrandPrimary.copy(alpha = 0.3f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = secondaryTextColor,
                                modifier = Modifier.width(24.dp)
                            )

                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.color,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = primaryTextColor
                                )
                                if (isTop7) {
                                    Text(
                                        "Tampil di Dashboard",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BrandPrimary
                                    )
                                }
                            }

                            Row {
                                if (index > 0) {
                                    IconButton(
                                        onClick = {
                                            val newList = editableList.toMutableList()
                                            Collections.swap(newList, index, index - 1)
                                            editableList = newList
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.ArrowUpward, null, tint = secondaryTextColor, modifier = Modifier.size(16.dp))
                                    }
                                }
                                if (index < editableList.size - 1) {
                                    IconButton(
                                        onClick = {
                                            val newList = editableList.toMutableList()
                                            Collections.swap(newList, index, index + 1)
                                            editableList = newList
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.ArrowDownward, null, tint = secondaryTextColor, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        if (index == 6) {
                            HorizontalDivider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text = "--- Menu di bawah ini masuk ke 'Lainnya' ---",
                                style = MaterialTheme.typography.labelSmall,
                                color = secondaryTextColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}