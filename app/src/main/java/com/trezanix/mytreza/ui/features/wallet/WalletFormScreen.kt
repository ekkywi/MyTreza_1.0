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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    onSaveClick: () -> Unit
) {
    val existingWallet = remember {
        if (walletId != null) getDummyWallets().find { it.id == walletId } else null
    }

    var name by remember { mutableStateOf(existingWallet?.name ?: "") }
    var balance by remember { mutableStateOf(existingWallet?.balance?.toInt()?.toString() ?: "") }
    var selectedType by remember { mutableStateOf(existingWallet?.type ?: "Tunai") }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isSharedWallet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Editor") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResultError by remember { mutableStateOf<String?>(null) }

    val invitedMembers = remember { mutableStateListOf<WalletMemberInput>() }
    val scope = rememberCoroutineScope()
    val walletTypes = listOf("Cash", "Bank", "E-Wallet", "Savings", "Investment")
    val roles = listOf("Admin", "Editor", "Viewer")
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

            // 1. PREVIEW KARTU
            item {
                Text("Preview Tampilan", style = MaterialTheme.typography.labelLarge, color = TextHint)
                Spacer(modifier = Modifier.height(12.dp))
                WalletCardItem(
                    wallet = WalletModel(
                        id = "preview",
                        name = if(name.isEmpty()) "Nama Dompet" else name,
                        type = selectedType,
                        balance = balance.toDoubleOrNull() ?: 0.0,
                        gradient = gradients[selectedColorIndex],
                        isShared = isSharedWallet,
                        currency = "IDR"
                    ),
                    showMenu = false
                )
            }

            // 2. INPUT NAMA
            item {
                GlassInput(
                    label = "Nama Dompet",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Contoh: Tabungan Nikah"
                )
            }

            // 3. INPUT SALDO
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

            // 4. PILIH TIPE
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

                AnimatedVisibility(visible = true) {
                    val infoText = when (selectedType) {
                        "Cash" -> "Physical money held in hand, petty cash, or coin jars. No transaction fees."
                        "Bank" -> "Primary bank accounts (Checking/Debit) used for salary, transfers, and daily transactions."
                        "E-Wallet" -> "Digital balances for quick payments (e.g., GoPay, OVO) and online shopping."
                        "Savings" -> "Funds set aside for specific goals or emergency funds, not for daily spending."
                        "Investment" -> "Cash balance (RDN) or Buying Power inside your investment apps. Not the asset value itself."
                        else -> "Select a wallet type to see details."
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .background(BrandPrimary.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = BrandPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = infoText,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
                            color = BrandDarkText.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // 5. PILIH WARNA
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

            // 6. === SECTION SHARED WALLET ===
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if(isSharedWallet) BrandPrimary else Color.LightGray.copy(alpha=0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    // Toggle Switch
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

                    // Area Invite (Visible if ON)
                    AnimatedVisibility(visible = isSharedWallet) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = Color.LightGray.copy(0.3f))

                            Text("Tambah Anggota", style = MaterialTheme.typography.labelSmall, color = TextHint)
                            Spacer(modifier = Modifier.height(8.dp))

                            // SEARCH BAR
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = {
                                        searchQuery = it
                                        searchResultError = null
                                    },
                                    placeholder = { Text("Cari username...") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    isError = searchResultError != null
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                // BUTTON SIMULASI SEARCH
                                Button(
                                    onClick = {
                                        if (searchQuery.isNotEmpty()) {
                                            scope.launch {
                                                isSearching = true
                                                searchResultError = null
                                                delay(1500) // Fake Loading

                                                if (searchQuery.lowercase() == "admin") {
                                                    searchResultError = "User tidak ditemukan"
                                                } else if (invitedMembers.any { it.username == searchQuery }) {
                                                    searchResultError = "User sudah ada"
                                                } else {
                                                    invitedMembers.add(WalletMemberInput(searchQuery, selectedRole))
                                                    searchQuery = ""
                                                }
                                                isSearching = false
                                            }
                                        }
                                    },
                                    enabled = !isSearching && searchQuery.isNotEmpty(),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                                ) {
                                    if (isSearching) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                                    } else {
                                        Icon(Icons.Default.PersonAdd, null)
                                    }
                                }
                            }

                            if (searchResultError != null) {
                                Text(searchResultError!!, style = MaterialTheme.typography.labelSmall, color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // ROLE CHIPS
                            Text("Role:", style = MaterialTheme.typography.labelSmall, color = TextHint)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                roles.forEach { role ->
                                    FilterChip(
                                        selected = selectedRole == role,
                                        onClick = { selectedRole = role },
                                        label = { Text(role) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandPrimary.copy(alpha = 0.1f),
                                            selectedLabelColor = BrandPrimary
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // LIST INVITED MEMBER
                            if (invitedMembers.isNotEmpty()) {
                                Text("Akan Diundang (${invitedMembers.size})", style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                invitedMembers.forEachIndexed { index, member ->
                                    MemberItemRow(member = member, onRemove = { invitedMembers.removeAt(index) })
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// Sub-komponen Input Kaca
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

// Sub-komponen Row Member
@Composable
fun MemberItemRow(member: WalletMemberInput, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(member.username.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.username, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text("Role: ${member.role}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}