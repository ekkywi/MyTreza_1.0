package com.trezanix.mytreza.ui.features.main.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.theme.SurfaceColor

data class AtomicAction(
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun AtomicBottomSheetContent(
    onDismiss: () -> Unit,
    onTransactionClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    val context = LocalContext.current

    fun showComingSoon(featureName: String) {
        val message = context.getString(R.string.fab_sheet_coming_soon, featureName)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        onDismiss()
    }

    val actions = listOf(
        AtomicAction(stringResource(R.string.action_scan), Icons.Default.QrCodeScanner, Color(0xFF2196F3)) {
            showComingSoon(context.getString(R.string.action_scan))
        },
        AtomicAction(stringResource(R.string.action_transaction), Icons.Default.ReceiptLong, Color(0xFF4CAF50)) {
            onDismiss()
            onTransactionClick()
        },
        AtomicAction(stringResource(R.string.action_transfer), Icons.Default.SyncAlt, Color(0xFFFF9800)) {
            showComingSoon(context.getString(R.string.action_transfer))
        },
        AtomicAction(stringResource(R.string.action_wallet), Icons.Default.AccountBalanceWallet, Color(0xFF9C27B0)) {
            onDismiss()
            onWalletClick()
        },
        AtomicAction(stringResource(R.string.action_budget), Icons.Default.Savings, Color(0xFF00BCD4)) {
            showComingSoon(context.getString(R.string.action_budget))
        },
        AtomicAction(stringResource(R.string.action_bills), Icons.Default.Event, Color(0xFF673AB7)) {
            showComingSoon(context.getString(R.string.action_bills))
        },
        AtomicAction(stringResource(R.string.action_goals), Icons.Default.Flag, Color(0xFFFF5722)) {
            showComingSoon(context.getString(R.string.action_goals))
        },
        AtomicAction(stringResource(R.string.action_debt), Icons.Default.MoneyOff, Color(0xFFF44336)) {
            showComingSoon(context.getString(R.string.action_debt))
        },
        AtomicAction(stringResource(R.string.action_stock), Icons.Default.ShowChart, Color(0xFF3F51B5)) {
            showComingSoon(context.getString(R.string.action_stock))
        },
        AtomicAction(stringResource(R.string.action_gold), Icons.Default.Stars, Color(0xFFFFC107)) {
            showComingSoon(context.getString(R.string.action_gold))
        },
        AtomicAction(stringResource(R.string.action_crypto), Icons.Default.CurrencyBitcoin, Color(0xFF607D8B)) {
            showComingSoon(context.getString(R.string.action_crypto))
        },
        AtomicAction(stringResource(R.string.action_protection), Icons.Default.Shield, Color(0xFF009688)) {
            showComingSoon(context.getString(R.string.action_protection))
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceColor)
            .padding(bottom = 48.dp)
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.fab_sheet_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Text(
            text = stringResource(R.string.fab_sheet_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(actions) { action ->
                AtomicActionItem(action)
            }
        }
    }
}

@Composable
fun AtomicActionItem(action: AtomicAction) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { action.onClick() }
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(action.color.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = action.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = action.label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}