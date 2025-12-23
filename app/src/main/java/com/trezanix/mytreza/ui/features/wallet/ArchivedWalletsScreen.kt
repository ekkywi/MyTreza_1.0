package com.trezanix.mytreza.ui.features.wallet

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trezanix.mytreza.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedWalletsScreen(
    onBackClick: () -> Unit,
    onWalletClick: (String) -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val archivedWallets by viewModel.archivedWalletListState.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "AuroraArchive")
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
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 100.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (archivedWallets.isEmpty()) {
                        item {
                            EmptyArchiveState()
                        }
                    } else {
                        items(archivedWallets) { wallet ->
                            Box(modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .clickable { onWalletClick(wallet.id) }
                            ) {
                                WalletCardItem(wallet = wallet, showMenu = false)
                            }
                        }

                        item {
                            Text(
                                text = "Klik kartu untuk memulihkan",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextHint,
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    SurfaceColor.copy(alpha = 0.95f),
                                    SurfaceColor.copy(alpha = 0.8f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "Arsip Dompet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BrandDarkText
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandDarkText)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyArchiveState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Unarchive,
            contentDescription = null,
            tint = BrandPrimary.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Arsip Kosong",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = BrandDarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Dompet yang Anda arsipkan\nakan muncul di sini.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextHint,
            textAlign = TextAlign.Center
        )
    }
}