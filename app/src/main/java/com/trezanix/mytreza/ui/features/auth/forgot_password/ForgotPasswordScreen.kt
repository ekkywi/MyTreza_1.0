package com.trezanix.mytreza.ui.features.auth.forgot_password

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
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trezanix.mytreza.R
import com.trezanix.mytreza.ui.common.*
import com.trezanix.mytreza.ui.theme.*

@Composable
fun ForgotPasswordScreen(onNavigateToLogin: () -> Unit = {}) {
    // State untuk input
    var email by remember { mutableStateOf("") }

    // State untuk logika tampilan (Input vs Sukses)
    var isEmailSent by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
    ) {
        // 1. Organic Soft Shapes (Background)
        Canvas(modifier = Modifier.fillMaxSize().blur(80.dp)) {
            val width = size.width
            val height = size.height

            // Central Top Blob (Soft Indigo)
            drawCircle(
                color = ShapeColor1.copy(alpha = 0.6f),
                center = Offset(width * 0.5f, height * 0.1f),
                radius = width * 0.7f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // --- HEADER ---
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(800))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GradientText(
                        text = stringResource(R.string.forgot_title), // "Account Recovery"
                        style = MaterialTheme.typography.displaySmall,
                        brush = BrandGradient
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle hanya muncul jika belum terkirim agar tampilan bersih saat sukses
                    if (!isEmailSent) {
                        Text(
                            text = stringResource(R.string.forgot_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextHint,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- MAIN CARD ---
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn(animationSpec = tween(800, delayMillis = 100))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor.copy(alpha = 0.95f)), // Glassmorphism
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // LOGIKA SWAPPING UI (Input -> Success)
                        if (!isEmailSent) {
                            // TAMPILAN 1: FORM INPUT EMAIL
                            TrezaTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = stringResource(R.string.forgot_label_email),
                                leadingIcon = Icons.Default.Person
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            TrezaButton(stringResource(R.string.forgot_button_request), {
                                // Di sini nanti logika Firebase: auth.sendPasswordResetEmail(email)
                                if (email.isNotEmpty()) {
                                    isEmailSent = true // Ubah state menjadi sukses
                                }
                            })

                        } else {
                            // TAMPILAN 2: PESAN SUKSES
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.forgot_success_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BrandDarkText
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Pesan tentang Spam Folder
                            Text(
                                text = stringResource(R.string.forgot_success_desc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextHint,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Tombol untuk kembali ke login
                            TrezaButton(stringResource(R.string.forgot_button_back), {
                                onNavigateToLogin()
                            })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- FOOTER ---
            // Hanya tampilkan footer "Remembered?" jika masih di form input
            if (!isEmailSent) {
                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.forgot_footer_question), style = MaterialTheme.typography.bodyMedium, color = TextHint)
                    Text(
                        stringResource(R.string.forgot_footer_action),
                        style = MaterialTheme.typography.titleMedium,
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewForgotPasswordScreen() {
    MyTrezaTheme {
        ForgotPasswordScreen()
    }
}