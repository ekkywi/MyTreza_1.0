package com.trezanix.mytreza.ui.features.transaction

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.ui.common.CategoryIcons
import com.trezanix.mytreza.ui.features.category.CategorySelectionSheet
import com.trezanix.mytreza.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransactionFormViewModel = hiltViewModel()
) {
    val amount by viewModel.amount.collectAsState()
    val note by viewModel.note.collectAsState()
    val type by viewModel.transactionType.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedWallet by viewModel.selectedWallet.collectAsState()
    val wallets by viewModel.wallets.collectAsState()

    var showCategorySheet by remember { mutableStateOf(false) }
    var walletExpanded by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "AuroraTransaction")
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

    val activeColor = if (type == "EXPENSE") Color(0xFFFF5252) else Color(0xFF4CAF50)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
    ) {
        Canvas(modifier = Modifier.fillMaxSize().blur(60.dp)) {
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
                                "Add Transaction",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
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
                        .imePadding()
                ) {
                    Button(
                        onClick = {
                            Log.d("DEBUG_TRX", "Save clicked. Amount: ${amount}, Wallet: ${selectedWallet?.name}, Cat: ${selectedCategory?.name}")

                            if (amount.isNotEmpty() && selectedCategory != null && selectedWallet != null) {
                                viewModel.saveTransaction {
                                    Log.d("DEBUG_TRX", "Transaction Saved. Finishing.")
                                    onNavigateBack()
                                }
                            } else {
                                val missing = mutableListOf<String>()
                                if (amount.isEmpty()) missing.add("Amount")
                                if (selectedCategory == null) missing.add("Category")
                                if (selectedWallet == null) missing.add("Wallet")

                                Toast.makeText(context, "Please complete the Amount, Category and Wallet!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("Save Transaction", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White.copy(alpha = 0.6f)) // Glass effect
                            .padding(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            listOf("EXPENSE", "INCOME").forEach { tabType ->
                                val isSelected = type == tabType
                                val label = if (tabType == "EXPENSE") "Expense" else "Income"

                                val tabBgColor by animateColorAsState(
                                    targetValue = if (isSelected) activeColor else Color.Transparent,
                                    animationSpec = tween(300), label = "TabBg"
                                )
                                val tabTextColor by animateColorAsState(
                                    targetValue = if (isSelected) Color.White else Color.Black.copy(0.6f),
                                    animationSpec = tween(300), label = "TabText"
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(tabBgColor)
                                        .clickable { viewModel.onTypeChange(tabType) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = tabTextColor,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Amount",
                            style = MaterialTheme.typography.labelMedium,
                            color = BrandDarkText.copy(0.7f)
                        )

                        TextField(
                            value = amount,
                            onValueChange = {
                                val cleanValue =it.filter { char -> char.isDigit() }
                                viewModel.onAmountChange(it)
                                            },
                            textStyle = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                textAlign = TextAlign.Center
                            ),
                            placeholder = {
                                Text("0", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Gray.copy(0.4f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            },
                            prefix = {
                                Text("Rp ", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText.copy(0.6f), modifier = Modifier.padding(top = 8.dp))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = BrandPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    Text(
                        "Transaction Detail",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = BrandDarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GlassSelector(
                        title = "Category",
                        value = selectedCategory?.name ?: "Select Category",
                        icon = if (selectedCategory != null) CategoryIcons.getIconByName(selectedCategory!!.iconName) else Icons.Default.Category,
                        iconColor = try {
                            if (selectedCategory != null) Color(android.graphics.Color.parseColor(selectedCategory!!.colorHex)) else BrandSecondary
                        } catch (e: Exception) { BrandSecondary },
                        onClick = { showCategorySheet = true }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box {
                        GlassSelector(
                            title = "Source Wallet",
                            value = selectedWallet?.name ?: "Select Wallet",
                            icon = Icons.Default.Wallet,
                            iconColor = BrandPrimary,
                            onClick = { walletExpanded = true }
                        )

                        DropdownMenu(
                            expanded = walletExpanded,
                            onDismissRequest = { walletExpanded = false },
                            modifier = Modifier.background(Color.White).width(280.dp)
                        ) {
                            wallets.forEach { wallet ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(wallet.name, fontWeight = FontWeight.Bold)
                                            Text("Rp ${wallet.balance}", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    },
                                    onClick = {
                                        viewModel.onWalletSelect(wallet)
                                        walletExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    GlassInputTransaction(
                        label = "Note",
                        value = note,
                        onValueChange = { viewModel.onNoteChange(it) },
                        placeholder = "Write additional notes...",
                        icon = Icons.Default.Notes
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        if (showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = { showCategorySheet = false },
                containerColor = Color.White
            ) {
                CategorySelectionSheet(
                    transactionType = type,
                    onCategorySelected = { category ->
                        viewModel.onCategorySelect(category)
                        showCategorySheet = false
                    },
                    onAddCategoryClick = {}
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun GlassSelector(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.6f)) // Glass background
            .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = BrandDarkText.copy(0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (value.startsWith("Select")) BrandDarkText.copy(0.5f) else BrandDarkText
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = BrandDarkText.copy(0.4f)
        )
    }
}

@Composable
fun GlassInputTransaction(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector
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
            leadingIcon = { Icon(icon, contentDescription = null, tint = BrandDarkText.copy(0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.8f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.6f),

                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.1f),

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = BrandPrimary
            ),
            singleLine = true
        )
    }
}