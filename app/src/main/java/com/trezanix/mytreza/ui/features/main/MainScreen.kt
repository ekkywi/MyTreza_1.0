package com.trezanix.mytreza.ui.features.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.ui.common.BottomNavItem
import com.trezanix.mytreza.ui.features.analysis.AnalysisScreen
import com.trezanix.mytreza.ui.features.dashboard.DashboardScreen
import com.trezanix.mytreza.ui.features.profile.ProfileScreen
import com.trezanix.mytreza.ui.features.wallet.WalletScreen
import com.trezanix.mytreza.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Dashboard) }
    var showAddSheet by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "AuroraBreathing")
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
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1.0f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Scale2"
    )

    BackHandler(enabled = currentScreen != BottomNavItem.Dashboard) {
        currentScreen = BottomNavItem.Dashboard
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
                    colors = listOf(
                        BrandPrimary.copy(alpha = 0.40f),
                        BrandPrimary.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.2f, height * 0.2f + offset1),
                    radius = width * 0.9f * scale1
                ),
                center = Offset(width * 0.2f, height * 0.2f),
                radius = width * 0.8f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BrandAccent.copy(alpha = 0.35f),
                        BrandAccent.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.8f + offset2, height * 0.8f),
                    radius = width * 0.8f * scale2
                ),
                center = Offset(width * 0.8f, height * 0.8f),
                radius = width * 0.8f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BrandSecondary.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.5f),
                    radius = width * 0.7f
                )
            )
        }

        Scaffold(
            containerColor = Color.Transparent,

            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .align(Alignment.BottomCenter)
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.7f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceColor.copy(alpha = 0.90f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            GlassNavItem(
                                item = BottomNavItem.Dashboard,
                                isSelected = currentScreen == BottomNavItem.Dashboard,
                                onClick = { currentScreen = BottomNavItem.Dashboard }
                            )
                            GlassNavItem(
                                item = BottomNavItem.Analysis,
                                isSelected = currentScreen == BottomNavItem.Analysis,
                                onClick = { currentScreen = BottomNavItem.Analysis }
                            )

                            Spacer(modifier = Modifier.width(60.dp))

                            GlassNavItem(
                                item = BottomNavItem.Wallet,
                                isSelected = currentScreen == BottomNavItem.Wallet,
                                onClick = { currentScreen = BottomNavItem.Wallet }
                            )
                            GlassNavItem(
                                item = BottomNavItem.Profile,
                                isSelected = currentScreen == BottomNavItem.Profile,
                                onClick = { currentScreen = BottomNavItem.Profile }
                            )
                        }
                    }

                    val fabShape = RoundedCornerShape(20.dp)

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(y = (-10).dp)
                            .size(60.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = fabShape,
                                spotColor = BrandPrimary
                            )
                            .border(
                                width = 4.dp,
                                color = SurfaceColor.copy(alpha = 0.5f),
                                shape = fabShape
                            )
                            .background(
                                brush = BrandGradient,
                                shape = fabShape
                            )
                            .clip(fabShape)
                            .clickable { showAddSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Transaction",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 100.dp)
                    .fillMaxSize()
            ) {
                Crossfade(
                    targetState = currentScreen,
                    label = "MainScreenNavigation",
                    animationSpec = tween(durationMillis = 300)
                ) { targetState ->
                    when (targetState) {
                        BottomNavItem.Dashboard -> DashboardScreen()
                        BottomNavItem.Analysis -> AnalysisScreen()
                        BottomNavItem.Wallet -> WalletScreen()
                        BottomNavItem.Profile -> ProfileScreen()
                    }
                }
            }

            if (showAddSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showAddSheet = false },
                    containerColor = SurfaceColor,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Atomic Action Menu\n(Coming Soon)",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextHint,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        label = "NavItemScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(12.dp)
            .scale(scale)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (isSelected) BrandPrimary else TextHint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(BrandPrimary, CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}