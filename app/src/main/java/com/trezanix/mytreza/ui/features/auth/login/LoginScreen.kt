package com.trezanix.mytreza.ui.features.auth.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.ui.draw.blur
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.common.*
import com.trezanix.mytreza.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgot: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground) // Soft Lavender Tint
    ) {
        // 1. Organic Soft Shapes with BLUR (Atmospheric Glow)
        Canvas(modifier = Modifier
            .fillMaxSize()
            .blur(80.dp)) {
            val width = size.width
            val height = size.height

            // Top Left Blob (Pastel Indigo)
            drawCircle(
                color = ShapeColor1,
                center = Offset(width * 0.1f, height * 0.1f),
                radius = width * 0.6f // Larger radius for diffuse glow
            )
            
            // Bottom Right Blob (Pastel Pink)
            drawCircle(
                color = ShapeColor2,
                center = Offset(width * 0.9f, height * 0.9f),
                radius = width * 0.7f
            )
        }

        // 2. Centered Content with Entry Animation
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(800))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Header (Outside Card) with Gradient
                    GradientText(
                        text = stringResource(R.string.login_welcome_title),
                        style = MaterialTheme.typography.displaySmall,
                        brush = BrandGradient
                    )
                    
                    Text(
                        text = stringResource(R.string.login_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextHint
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Soft Card Container
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn(animationSpec = tween(800, delayMillis = 100))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor.copy(alpha = 0.95f)), // Glassmorphism
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp) // Soft shadow
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TrezaTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = stringResource(R.string.login_label_email),
                            leadingIcon = Icons.Default.Person
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        TrezaTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = stringResource(R.string.login_label_password),
                            leadingIcon = Icons.Default.Lock,
                            isPassword = true
                        )
                        
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 32.dp), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = stringResource(R.string.login_forgot_password),
                                color = BrandPrimary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onNavigateToForgot() }
                            )
                        }
                        
                        TrezaButton(stringResource(R.string.login_button_signin), { onLoginSuccess() })
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Footer
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.login_footer_new_user), style = MaterialTheme.typography.bodyMedium, color = TextHint)
                Text(
                    stringResource(R.string.login_footer_create_account),
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    MyTrezaTheme {
        LoginScreen()
    }
}