package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Info
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.ui.theme.*

data class WalletMemberInput(
    val username: String,
    val role: String,
    val avatarUrl: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletFormScreen(
    walletId: String? = null,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Tunai") }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isSharedWallet by remember { mutableStateOf(false) }

    val currentWallet by viewModel.currentWallet.collectAsState()
    val invitedMembers = remember { mutableStateListOf<WalletMemberInput>() }
    val walletTypes = listOf("Tunai", "Bank", "E-Wallet", "Savings", "Investasi")
    val gradients = listOf(
        Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1B5E20))),
        Brush.linearGradient(listOf(Color(0xFF66BB6A), Color(0xFF33691E))),
        Brush.linearGradient(listOf(Color(0xFF009688), Color(0xFF004D40))),
        Brush.linearGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))),
        Brush.linearGradient(listOf(Color(0xFF039BE5), Color(0xFF01579B))),
        Brush.linearGradient(listOf(Color(0xFF42A5F5), Color(0xFF1565C0))),
        Brush.linearGradient(listOf(Color(0xFF3949AB), Color(0xFF1A237E))),
        Brush.linearGradient(listOf(Color(0xFFFB8C00), Color(0xFFE65100))),
        Brush.linearGradient(listOf(Color(0xFFE53935), Color(0xFFB71C1C))),
        Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFBF360C))),
        Brush.linearGradient(listOf(Color(0xFFFFCA28), Color(0xFFFF6F00))),
        Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))),
        Brush.linearGradient(listOf(Color(0xFFBA68C8), Color(0xFF6A1B9A))),
        Brush.linearGradient(listOf(Color(0xFFEC407A), Color(0xFF880E4F))),
        Brush.linearGradient(listOf(Color(0xFF795548), Color(0xFF3E2723))),
        Brush.linearGradient(listOf(Color(0xFF78909C), Color(0xFF37474F))),
        Brush.linearGradient(listOf(Color(0xFF424242), Color(0xFF212121))),
        Brush.linearGradient(listOf(Color(0xFF546E7A), Color(0xFF263238)))
    )
    val isEditMode = walletId !=null

    LaunchedEffect(walletId) {
        if (walletId != null) {
            viewModel.loadWalletById(walletId)
        } else {
            viewModel.clearCurrentWallet()
        }
    }

    LaunchedEffect(currentWallet) {
        currentWallet?.let {
            name = it.name
            balance = it.balance.toLong().toString()
            selectedType = it.type
            isSharedWallet = it.isShared
            selectedColorIndex = it.colorIndex
        }
    }

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (walletId == null) "Tambah Dompet" else "Edit Dompet",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceColor)
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
                    onClick = {
                        if (name.isNotEmpty() && balance.isNotEmpty()) {
                            viewModel.saveWallet(
                                id = walletId,
                                name = name,
                                type = selectedType,
                                balance = balance.toDoubleOrNull() ?: 0.0,
                                isShared = isSharedWallet,
                                colorIndex = selectedColorIndex
                            )
                            onSaveClick()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
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

                val safeGradient = if(selectedColorIndex in gradients.indices) gradients[selectedColorIndex] else gradients[0]

                WalletCardItem(
                    wallet = WalletModel(
                        id = walletId ?: "PREVIEW",
                        name = if(name.isEmpty()) "Nama Dompet" else name,
                        type = selectedType,
                        balance = balance.toDoubleOrNull() ?: 0.0,
                        gradient = safeGradient,
                        isShared = isSharedWallet,
                        currency = "IDR",
                        isArchived = false
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
                    label = if (isEditMode) "Saldo (Terkunci)" else "Saldo Awal",
                    value = balance,
                    onValueChange = {
                        if (!isEditMode && it.all { char -> char.isDigit() }) {
                            balance = it
                        }
                    },
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = BrandDarkText) },
                    enabled = !isEditMode
                )

                if (isEditMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TextHint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Saldo hanya dapat diubah melalui transaksi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextHint
                        )
                    }
                }
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
                            )
                        )
                    }
                }

                AnimatedVisibility(visible = true) {
                    val infoText = when (selectedType) {
                        "Tunai" -> "Uang fisik yang dipegang tangan. Tidak ada biaya admin."
                        "Bank" -> "Rekening bank utama untuk gaji dan transfer."
                        "E-Wallet" -> "Saldo digital (GoPay, OVO, Dana) untuk pembayaran cepat."
                        "Savings" -> "Tabungan khusus atau dana darurat."
                        "Investasi" -> "Saldo RDN atau Buying Power. Bukan nilai aset sahamnya."
                        else -> "Pilih tipe dompet."
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .background(BrandPrimary.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(infoText, style = MaterialTheme.typography.bodySmall, color = BrandDarkText.copy(alpha = 0.8f))
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
                                    color = if (selectedColorIndex == index) BrandPrimary else Color.Transparent,
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

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if(isSharedWallet) BrandPrimary else Color.LightGray.copy(alpha=0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Shared Wallet", fontWeight = FontWeight.Bold, color = BrandDarkText)
                            Text("Kelola dana bersama tim/keluarga", style = MaterialTheme.typography.labelSmall, color = TextHint)
                        }
                        Switch(
                            checked = isSharedWallet,
                            onCheckedChange = { isSharedWallet = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = BrandPrimary)
                        )
                    }

                    AnimatedVisibility(visible = isSharedWallet) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = Color.LightGray.copy(0.3f))
                            Text("Tambah Anggota (Coming Soon)", style = MaterialTheme.typography.labelSmall, color = TextHint)
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
    prefix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
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
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceColor.copy(alpha = 0.5f),
                unfocusedContainerColor = SurfaceColor.copy(alpha = 0.3f),
                disabledContainerColor = Color.Black.copy(alpha = 0.05f),
                disabledBorderColor = Color.Transparent,
                disabledTextColor = TextPrimary.copy(alpha = 0.7f),
                disabledPrefixColor = TextPrimary.copy(alpha = 0.7f),
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