package com.example.aura

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AuraApp() {

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(4000)
        showSplash = false
    }

    var currentScreen by remember { mutableStateOf("home") }

    if (showSplash) {

        AuraSplashScreen()

    } else {

        when (currentScreen) {

            "home" -> HomeScreen(
                onFakeCallClick = { currentScreen = "fake" },
                onSafeZoneClick = { currentScreen = "safezone" }
            )


            "fake" -> FakeCallScreen(
                onEndCall = { currentScreen = "home" }
            )
            "safezone" -> SafeZoneScreen {
                currentScreen = "home"
            }

        }
    }
}
