package com.trezanix.mytreza.ui.features.wallet

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.theme.*

data class WalletTypeOption(
    val dbValue: String,
    val labelRes: Int,
    val descRes: Int
)

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
    val walletTypes = listOf(
        WalletTypeOption("Cash", R.string.type_cash, R.string.desc_cash),
        WalletTypeOption("Bank", R.string.type_bank, R.string.desc_bank),
        WalletTypeOption("E-Wallet", R.string.type_ewallet, R.string.desc_ewallet),
        WalletTypeOption("Savings", R.string.type_savings, R.string.desc_savings),
        WalletTypeOption("Investment", R.string.type_investment, R.string.desc_investment)
    )

    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var selectedTypeOption by remember { mutableStateOf(walletTypes[0]) }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isSharedWallet by remember { mutableStateOf(false) }

    val currentWallet by viewModel.currentWallet.collectAsState()

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
    val isEditMode = walletId != null
    val infiniteTransition = rememberInfiniteTransition(label = "AuroraForm")
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Orb1"
    )
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1.0f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Scale1"
    )
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Orb2"
    )

    LaunchedEffect(walletId) {
        if (walletId != null) {
            viewModel.loadWalletById(walletId)
        } else {
            viewModel.clearCurrentWallet()
        }
    }

    LaunchedEffect(currentWallet) {
        currentWallet?.let { wallet ->
            name = wallet.name
            balance = wallet.balance.toLong().toString()
            isSharedWallet = wallet.isShared
            selectedColorIndex = wallet.colorIndex
            selectedTypeOption = walletTypes.find { it.dbValue == wallet.type } ?: walletTypes[0]
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .blur(60.dp)) {
            val width = size.width
            val height = size.height
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandPrimary.copy(alpha = 0.40f), BrandPrimary.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(width * 0.2f, height * 0.2f + offset1),
                    radius = width * 0.9f * scale1
                ), center = Offset(width * 0.2f, height * 0.2f), radius = width * 0.8f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BrandAccent.copy(alpha = 0.35f), BrandAccent.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(width * 0.8f + offset2, height * 0.8f),
                    radius = width * 0.8f
                ), center = Offset(width * 0.8f, height * 0.8f), radius = width * 0.8f
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.9f),
                                    Color.White.copy(alpha = 0.7f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                if (walletId == null) stringResource(R.string.wallet_form_header_add) else stringResource(R.string.wallet_form_header_edit),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Return", tint = Color.Black)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                    )
                }
            },
            bottomBar = {
                val context = LocalContext.current

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = {
                                    if (name.isNotEmpty()) {
                                        val finalBalance = if (balance.isEmpty()) 0.0 else balance.toDoubleOrNull() ?: 0.0
                                        viewModel.saveWallet(
                                            id = walletId,
                                            name = name,
                                            type = selectedTypeOption.dbValue,
                                            balance = finalBalance,
                                            isShared = isSharedWallet,
                                            colorIndex = selectedColorIndex,
                                            onSuccess =  {
                                                onSaveClick()
                                            }
                                        )
                            } else {
                                Toast.makeText(context, "Please complete your Wallet Name and Balance!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("Save Wallet", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
                    Text(
                        stringResource(R.string.wallet_form_input_preview),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandDarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val safeGradient = if(selectedColorIndex in gradients.indices) gradients[selectedColorIndex] else gradients[0]

                    WalletCardItem(
                        wallet = WalletModel(
                            id = walletId ?: "PREVIEW",
                            name = if(name.isEmpty()) "WALLET NAME" else name,
                            type = stringResource(selectedTypeOption.labelRes),
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
                        label = stringResource(R.string.wallet_form_input_label_name),
                        value = name,
                        onValueChange = { name = it },
                        placeholder = stringResource(R.string.wallet_form_input_placeholder_name)
                    )
                }

                item {
                    GlassInput(
                        label = if (isEditMode) stringResource(R.string.wallet_form_input_label_balance_locked) else stringResource(R.string.wallet_form_input_label_balance),
                        value = balance,
                        onValueChange = { newValue ->
                            val cleanValue = newValue.filter { it.isDigit() }

                            if (!isEditMode) {
                                balance = cleanValue
                            }
                        },
                        placeholder = "0",
                        keyboardType = KeyboardType.Number,
                        prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = Color.Black) },
                        enabled = !isEditMode
                    )

                    if (isEditMode) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = BrandDarkText.copy(0.7f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.wallet_form_input_note_balance),
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandDarkText.copy(0.7f)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = if (isEditMode) stringResource(R.string.wallet_form_input_label_type_locked) else stringResource(R.string.wallet_form_input_label_type),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandDarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(walletTypes) { typeOption ->
                            val isSelected = selectedTypeOption == typeOption

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (!isEditMode) selectedTypeOption = typeOption
                                },
                                label = { Text(stringResource(typeOption.labelRes)) },

                                enabled = !isEditMode || isSelected,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White.copy(alpha = 0.7f),
                                    labelColor = Color.Black,

                                    disabledSelectedContainerColor = BrandPrimary.copy(alpha = 0.8f),
                                    disabledLabelColor = Color.White,
                                    disabledContainerColor = Color.Transparent,
                                    disabledLeadingIconColor = Color.Gray
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = !isEditMode,
                                    selected = isSelected,
                                    borderColor = if(isSelected) Color.Transparent else Color.Black.copy(alpha=0.1f),
                                    disabledBorderColor = if(isSelected) Color.Transparent else Color.Black.copy(alpha=0.05f)
                                )
                            )
                        }
                    }

                    if (isEditMode) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = BrandDarkText.copy(0.7f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.wallet_form_input_note_type),
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandDarkText.copy(0.7f)
                            )
                        }
                    }

                    AnimatedVisibility(visible = !isEditMode) {
                        val infoText = stringResource(selectedTypeOption.descRes)

                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth()
                                .background(
                                    Color.White.copy(alpha = 0.6f),
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    BrandPrimary.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(infoText, style = MaterialTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.8f))
                        }
                    }
                }

                item {
                    Text(
                        stringResource(R.string.wallet_form_input_label_card_color),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandDarkText
                    )
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
                                        width = if (selectedColorIndex == index) 3.dp else 1.dp,
                                        color = if (selectedColorIndex == index) BrandPrimary else Color.White,
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
                            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                if (isSharedWallet) BrandPrimary else Color.Black.copy(alpha = 0.1f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.wallet_form_input_label_shared_wallet), fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(stringResource(R.string.wallet_form_input_sublabel_shared_wallet), style = MaterialTheme.typography.labelSmall, color = BrandDarkText.copy(0.7f))
                            }
                            Switch(
                                checked = isSharedWallet,
                                onCheckedChange = { isSharedWallet = it },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = BrandPrimary,
                                    uncheckedTrackColor = Color.Black.copy(0.1f)
                                )
                            )
                        }

                        AnimatedVisibility(visible = isSharedWallet) {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = Color.Black.copy(0.1f))
                                Text(stringResource(R.string.wallet_form_input_note_shared_wallet), style = MaterialTheme.typography.labelSmall, color = BrandDarkText.copy(0.7f))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
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
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = BrandDarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.8f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.7f),

                disabledContainerColor = Color.Black.copy(alpha = 0.05f),
                disabledTextColor = Color.Black.copy(alpha = 0.8f),
                disabledPrefixColor = Color.Black.copy(alpha = 0.8f),
                disabledBorderColor = Color.Transparent,

                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.1f),

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = BrandPrimary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            prefix = prefix,
            singleLine = true
        )
    }
}