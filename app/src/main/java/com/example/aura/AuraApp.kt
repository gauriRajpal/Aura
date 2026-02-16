package com.example.aura



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

//@Composable
//fun AuraApp(onSOSClick: () -> Unit,
//            onRecordClick: () -> Unit,
//            onLocationClick: () -> Unit) {
//
//    var showSplash by remember { mutableStateOf(true) }
//
//    LaunchedEffect(Unit) {
//        kotlinx.coroutines.delay(4000)
//        showSplash = false
//    }
//
//    if (showSplash) {
//        AuraSplashScreen()
//    } else {
//        HomeScreen(
//            onSOSClick = onSOSClick,
//            onRecordClick = onRecordClick,
//            onLocationClick = onLocationClick
//        )
//    }
//}

@Composable
fun AuraApp(
    onSOSClick: () -> Unit,
    onRecordClick: () -> Unit,
    onLocationClick: () -> Unit
) {

    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("home") }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        showSplash = false
    }

    if (showSplash) {
        AuraSplashScreen()
    } else {
        when (currentScreen) {

            "home" -> HomeScreen(
                onSOSClick = onSOSClick,
                onRecordClick = onRecordClick,
                onLocationClick = onLocationClick,
                onContactsClick = { currentScreen = "contacts" }
            )

            "contacts" -> ContactsScreen(
                onBack = { currentScreen = "home" }
            )
        }
    }
}
