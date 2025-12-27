package com.trezanix.mytreza.ui.features.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.data.local.entity.WalletEntity
import com.trezanix.mytreza.ui.theme.BrandPrimary
import com.trezanix.mytreza.ui.theme.BrandSecondary

@Composable
fun WalletSelector(
    wallets: List<WalletEntity>,
    selectedWallet: WalletEntity?,
    onWalletSelected: (WalletEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

   Box(
       modifier = Modifier
           .fillMaxWidth()
           .clip(RoundedCornerShape(16.dp))
           .background(Color.White.copy(alpha = 0.5f))
           .clickable { expanded = true }
           .padding(16.dp)
   ) {
       Row(
           verticalAlignment = Alignment.CenterVertically
       ) {
           Box(
               modifier = Modifier
                   .size(40.dp)
                   .clip(RoundedCornerShape(10.dp))
                   .background(BrandPrimary.copy(alpha = 0.1f)),
               contentAlignment = Alignment.Center
           ) {
               Icon(Icons.Default.Wallet, contentDescription = null, tint = BrandPrimary)
           }

           Spacer(modifier = Modifier.width(16.dp))

           Column(modifier = Modifier.weight(1f)) {
               Text(
                   text = "Source of Funds",
                   style = MaterialTheme.typography.labelMedium,
                   color = Color.Gray
               )
               Text(
                   text = selectedWallet?.name ?: "Select Wallet",
                   style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                   color = Color.Black
               )
           }
           Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
       }

       DropdownMenu(
           expanded = expanded,
           onDismissRequest = { expanded = false },
           modifier = Modifier.background(Color.White)
       ) {
           wallets.forEach { wallet ->
               DropdownMenuItem(
                   text = {
                       Column {
                           Text(wallet.name, fontWeight = FontWeight.Bold)
                           Text("Rp ${wallet.balance}", style = MaterialTheme.typography.bodySmall)
                       }
                   },
                   onClick = {
                       onWalletSelected(wallet)
                       expanded = false
                   }
               )
           }
       }
   }
}