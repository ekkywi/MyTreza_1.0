package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletFormScreen(
    walletId: Int? = null,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    val existingWallet = remember {
        if (walletId != null) getDummyWallets().find { it.id == walletId } else null
    }

    var name by remember { mutableStateOf(existingWallet?.name ?: "") }
    var balance by remember { mutableStateOf(existingWallet?.balance?.toInt()?.toString() ?: "") }
    var accountNumber by remember { mutableStateOf(existingWallet?.accountNumber ?: "") }
    var selectedType by remember { mutableStateOf(existingWallet?.type ?: "Tunai") }
    var selectedColorIndex by remember { mutableStateOf(0) }

    val walletTypes = listOf(
        "Tunai",
        "Bank",
        "E-Wallet",
        "Savings",
        "Investasi"
    )

    val gradients = listOf(
        Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1B5E20))),
        Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))),
        Brush.linearGradient(listOf(Color(0xFF039BE5), Color(0xFF01579B))),
        Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))),
        Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))),
        Brush.linearGradient(listOf(Color(0xFFE53935), Color(0xFFB71C1C)))
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (walletId == null) "Tambah Dompet" else "Edit Dompet",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(SurfaceColor.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = BrandDarkText)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Simpan Dompet", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                Text("Preview Tampilan", style = MaterialTheme.typography.labelLarge, color = TextHint)
                Spacer(modifier = Modifier.height(12.dp))
                WalletCardItem(
                    wallet = WalletModel(
                        id = 0,
                        name = if(name.isEmpty()) "Nama Dompet" else name,
                        type = selectedType,
                        balance = balance.toDoubleOrNull() ?: 0.0,
                        accountNumber = if(accountNumber.isEmpty()) "**** ****" else accountNumber,
                        gradient = gradients[selectedColorIndex]
                    ),
                    showMenu = false
                )
            }

            item {
                GlassInput(
                    label = "Nama Dompet",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Contoh: Tabungan Nikah"
                )
            }

            item {
                GlassInput(
                    label = "Saldo Saat Ini",
                    value = balance,
                    onValueChange = { if (it.all { char -> char.isDigit() }) balance = it },
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = BrandDarkText) }
                )
            }

            item {
                GlassInput(
                    label = "Nomor Rekening / Kartu (Opsional)",
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    placeholder = "Contoh: 1234-5678-9000"
                )
            }

            item {
                Text("Tipe Dompet", style = MaterialTheme.typography.labelLarge, color = TextHint)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(walletTypes) { _, type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPrimary,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedType == type,
                                borderColor = if(selectedType==type) Color.Transparent else BrandPrimary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }

            item {
                Text("Warna Kartu", style = MaterialTheme.typography.labelLarge, color = TextHint)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    itemsIndexed(gradients) { index, brush ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(brush)
                                .clickable { selectedColorIndex = index }
                                .border(
                                    width = if (selectedColorIndex == index) 3.dp else 0.dp,
                                    color = if (selectedColorIndex == index) Color.White else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColorIndex == index) {
                                Icon(Icons.Default.Check, null, tint = Color.White)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun GlassInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: @Composable (() -> Unit)? = null
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge, color = TextHint)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextHint.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceColor.copy(alpha = 0.5f),
                unfocusedContainerColor = SurfaceColor.copy(alpha = 0.3f),
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = BrandPrimary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            prefix = prefix,
            singleLine = true
        )
    }
}