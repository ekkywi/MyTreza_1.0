package com.trezanix.mytreza.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trezanix.mytreza.ui.common.AuthViewModel
import com.trezanix.mytreza.ui.features.profile.components.*

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(bottom = 100.dp)
    ) {
        ProfileHeader(
            name = "Trezanix Developer",
            username = "trezanix_developer",
            email = "developer@trezanix.com"
        )

        SectionTitle("Pengaturan")
        ProfileMenuSection {
            ProfileMenuItem(
                icon = Icons.Outlined.Palette,
                title = "Tampilan",
                subtitle = "Tema Gelap & Warna",
                onClick = {}
            )
            MenuDivider()
            ProfileMenuItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifikasi",
                subtitle = "Atur jadwal pengingat",
                onClick = {}
            )
            MenuDivider()
            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                title = "Umum",
                subtitle = "Mata uang & Bahasa",
                onClick = {}
            )
        }

        SectionTitle("Keamanan")
        ProfileMenuSection {
            ProfileMenuItem(
                icon = Icons.Outlined.Lock,
                title = "Kunci Aplikasi",
                subtitle = "PIN & Biometrik",
                onClick = {}
            )
            MenuDivider()
            ProfileMenuItem(
                icon = Icons.Outlined.Help,
                title = "Bantuan & Dukungan",
                onClick = {}
            )
        }

        SectionTitle("Akun")
        ProfileMenuSection {
            ProfileMenuItem(
                icon = Icons.Default.Logout,
                title = "Keluar",
                isDestructive = true,
                onClick = {
                    viewModel.logout()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MyTreza v1.0.0",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray.copy(alpha = 0.7f)
            )
            Text(
                text = "Made with ❤️ by Trezanix",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 24.dp),
        thickness = 1.dp,
        color = Color.LightGray.copy(alpha = 0.2f)
    )
}