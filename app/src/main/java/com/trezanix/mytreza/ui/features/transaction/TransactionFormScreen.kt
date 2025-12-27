package com.trezanix.mytreza.ui.features.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.ui.common.CategoryIcons
import com.trezanix.mytreza.ui.features.category.CategorySelectionSheet
import com.trezanix.mytreza.ui.features.transaction.components.WalletSelector
import com.trezanix.mytreza.ui.theme.BrandPrimary
import com.trezanix.mytreza.ui.theme.BrandSecondary

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.saveTransaction {
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Transaction", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(0.2f), CircleShape)
                    .padding(4.dp)
            ) {
                listOf("EXPENSE", "INCOME").forEach { tabType ->
                    val isSelected = type == tabType
                    val label = if (tabType == "EXPENSE") "Expense" else "Income"
                    val bgColor = if (isSelected) {
                        if (tabType == "EXPENSE") Color.Red.copy(0.1f) else Color.Green.copy(0.1f)
                    } else Color.Transparent
                    val textColor = if (isSelected) {
                        if (tabType == "EXPENSE") Color.Red else Color.Green
                    } else Color.Gray

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(bgColor)
                            .clickable { viewModel.onTypeChange(tabType) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = label, color = textColor, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Amount", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            TextField(
                value = amount,
                onValueChange = { viewModel.onAmountChange(it) },
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                ),
                prefix = {
                    Text("Rp ", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .clickable { showCategorySheet = true }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedCategory != null) {
                                    try { Color(android.graphics.Color.parseColor(selectedCategory!!.colorHex)) }
                                    catch (e:Exception) { BrandSecondary }
                                } else Color.LightGray.copy(0.3f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selectedCategory != null)
                                CategoryIcons.getIconByName(selectedCategory!!.iconName)
                            else Icons.Default.Category,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Category", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Text(
                            text = selectedCategory?.name ?: "Select Category",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            WalletSelector(
                wallets = wallets,
                selectedWallet = selectedWallet,
                onWalletSelected = { viewModel.onWalletSelect(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { viewModel.onNoteChange(it) },
                label = { Text("Note (Optional)") },
                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }

        if (showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = { showCategorySheet = false },
                containerColor = Color.White
            ) {
                CategorySelectionSheet(
                    onCategorySelected = { category ->
                        viewModel.onCategorySelect(category)
                        showCategorySheet = false
                    },
                    onAddCategoryClick = {
                        // TODO: Arahkan ke Create Category Screen (Next Feature)
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}