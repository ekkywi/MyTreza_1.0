package com.trezanix.mytreza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.trezanix.mytreza.ui.features.main.MainScreen
import com.trezanix.mytreza.ui.features.splash.AppLoadingScreen
import com.trezanix.mytreza.ui.theme.MyTrezaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyTrezaTheme {
                var isLoading by remember { mutableStateOf(true) }

                Crossfade(
                    targetState = isLoading,
                    label = "SplashToMainTransition"
                ) { loading ->
                    if (loading) {
                        AppLoadingScreen(
                            onLoadingFinished = {
                                isLoading = false
                            }
                        )
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}