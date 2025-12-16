package com.trezanix.mytreza.ui.features.auth.register

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.common.*
import com.trezanix.mytreza.ui.theme.*

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val scrollState = rememberScrollState()

    val privacyText = buildAnnotatedString {
        append(stringResource(R.string.register_checkbox_agree))

        withStyle(style = SpanStyle(color = BrandPrimary, fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.register_checkbox_terms_conditions))
        }

        append(stringResource(R.string.register_checkbox_and))

        withStyle(style = SpanStyle(color = BrandPrimary, fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.register_checkbox_privacy_policy))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground) // Soft Lavender Tint
    ) {
        // 1. Organic Soft Shapes
        Canvas(modifier = Modifier
            .fillMaxSize()
            .blur(80.dp)) {
            val width = size.width
            val height = size.height

            // Top Right Blob (Pastel Indigo/Violet)
            drawCircle(
                color = ShapeColor1,
                center = Offset(width * 0.8f, height * 0.2f),
                radius = width * 0.4f
            )
            
            // Bottom Left Blob (Pastel Pink)
            drawCircle(
                color = ShapeColor2,
                center = Offset(width * 0.2f, height * 0.8f),
                radius = width * 0.5f
            )
        }

        // 2. Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Reduced from 32
            
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(800))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GradientText(
                        text = stringResource(R.string.register_title),
                        style = MaterialTheme.typography.displaySmall,
                        brush = BrandGradient
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.register_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextHint,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Reduced from 32
            
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
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                     Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp), // Reduced vertical padding
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TrezaTextField(username, { username = it },
                            stringResource(R.string.register_label_username), Icons.Default.Person)
                        Spacer(modifier = Modifier.height(8.dp)) // Reduced from 16
                        TrezaTextField(fullname, { fullname = it },
                            stringResource(R.string.register_label_fullname), Icons.Default.Badge)
                        Spacer(modifier = Modifier.height(8.dp))
                        TrezaTextField(email, { email = it },
                            stringResource(R.string.register_label_email), Icons.Default.Email)
                        Spacer(modifier = Modifier.height(8.dp))
                        TrezaTextField(password, { password = it },
                            stringResource(R.string.register_label_password), Icons.Default.Lock, isPassword = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        TrezaTextField(confirm, { confirm = it },
                            stringResource(R.string.register_label_password_confirmation), Icons.Default.LockReset, isPassword = true)

                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Left Aligned Checkbox
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            TrezaCheckbox(
                                text = privacyText,
                                checked = isChecked,
                                onCheckedChange = { isChecked = it }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp)) // Reduced from 32
                        TrezaButton(stringResource(R.string.register_button), { /* API Call */ })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Reduced from 32
            
            // Footer
            Row(
                 modifier = Modifier.padding(bottom = 16.dp), // Reduced from 24
                 horizontalArrangement = Arrangement.Center,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                Text(stringResource(R.string.register_footer_existing_user), style = MaterialTheme.typography.bodyMedium, color = TextHint)
                Text(
                    stringResource(R.string.register_footer_login),
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    MyTrezaTheme {
        RegisterScreen()
    }
}