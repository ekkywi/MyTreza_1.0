package com.trezanix.mytreza

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.trezanix.mytreza.ui.common.AuthViewModel
import com.trezanix.mytreza.ui.features.auth.forgot_password.ForgotPasswordScreen
import com.trezanix.mytreza.ui.features.auth.login.LoginScreen
import com.trezanix.mytreza.ui.features.auth.register.RegisterScreen
import com.trezanix.mytreza.ui.features.main.MainScreen
import com.trezanix.mytreza.ui.features.splash.AppLoadingScreen
import com.trezanix.mytreza.ui.theme.MyTrezaTheme
import dagger.hilt.android.AndroidEntryPoint

enum class AuthScreenState {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        enableEdgeToEdge()

        setContent {
            MyTrezaTheme {
                val context = LocalContext.current

                val isLoading by authViewModel.isLoading.collectAsState()
                val isLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

                var currentAuthScreen by remember { mutableStateOf(AuthScreenState.LOGIN) }

                Crossfade(targetState = isLoading, label = "RootTransition") { loading ->
                    if (loading) {
                        AppLoadingScreen(onLoadingFinished = {})
                    } else {
                        if (isLoggedIn) {
                            MainScreen()
                        } else {
                            Crossfade(targetState = currentAuthScreen, label = "AuthTransition") { screen ->
                                when (screen) {
                                    AuthScreenState.LOGIN -> {
                                        LoginScreen(
                                            onLoginSuccess = {
                                                authViewModel.login()
                                            },
                                            onNavigateToRegister = {
                                                currentAuthScreen = AuthScreenState.REGISTER
                                            },
                                            onNavigateToForgot = {
                                                currentAuthScreen = AuthScreenState.FORGOT_PASSWORD
                                            }
                                        )
                                    }
                                    AuthScreenState.REGISTER -> {
                                        RegisterScreen(
                                            onNavigateToLogin = {
                                                currentAuthScreen = AuthScreenState.LOGIN
                                            }
                                        )
                                    }
                                    AuthScreenState.FORGOT_PASSWORD -> {
                                        ForgotPasswordScreen(
                                            onNavigateToLogin = {
                                                currentAuthScreen = AuthScreenState.LOGIN
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}