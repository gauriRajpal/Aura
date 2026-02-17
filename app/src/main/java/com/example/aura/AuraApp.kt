package com.example.aura



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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

//@Composable
//fun AuraApp(
//    radarDevices: List<Pair<String, Int>>,
//    onSOSClick: () -> Unit,
//    onRecordClick: () -> Unit,
//    onLocationClick: () -> Unit,
//    detectedDevices: SnapshotStateList<Pair<String, Int>>
//) {
//
//    var showSplash by remember { mutableStateOf(true) }
//    var currentScreen by remember { mutableStateOf("home") }
//    val detectedDevices = remember { mutableStateOf("home") }
//
//    LaunchedEffect(Unit) {
//        kotlinx.coroutines.delay(2000)
//        showSplash = false
//    }
//
//    if (showSplash) {
//        AuraSplashScreen()
//    } else {
//        when (currentScreen) {
//
//            "home" -> HomeScreen(
//                onSOSClick = onSOSClick,
//                onRecordClick = onRecordClick,
//                onLocationClick = onLocationClick,
//                onContactsClick = { currentScreen = "contacts"},
//                onRadarClick = { currentScreen = "radar" },
//                detectedDevices = detectedDevices
//            )
//
//            "contacts" -> ContactsScreen(
//                onBack = { currentScreen = "home" })
//            "radar" -> RadarScreen(
//                devices = detectedDevices,
//                        onBack = { currentScreen = "home" })
//        }
//    }
//}
//}
import androidx.compose.runtime.*

//
//@Composable
//fun AuraApp(
//    onSOSClick: () -> Unit,
//    onRecordClick: () -> Unit,
//    onLocationClick: () -> Unit,
//    detectedDevices: SnapshotStateList<Pair<String, Int>>
//) {
//
//    var showSplash by remember { mutableStateOf(true) }
//    var currentScreen by remember { mutableStateOf("home") }
//
//    LaunchedEffect(Unit) {
//        kotlinx.coroutines.delay(2000)
//        showSplash = false
//    }
//
//    if (showSplash) {
//        AuraSplashScreen()
//    } else {
//        when (currentScreen) {
//
//            "home" -> HomeScreen(
//                onSOSClick = onSOSClick,
//                onRecordClick = onRecordClick,
//                onLocationClick = onLocationClick,
//                onContactsClick = { currentScreen = "contacts" },
//                onRadarClick = { currentScreen = "radar" },
//                onFakeCallClick = { currentScreen = "fake" },
//                detectedDevices = detectedDevices
//            )
//
//            "contacts" -> ContactsScreen(
//                onBack = { currentScreen = "home" }
//            )
//
//            "radar" -> RadarScreen(
//                devices = detectedDevices.toList(),
//                onBack = { currentScreen = "home" }
//            )
//        }
//    }
//}



@Composable
fun AuraApp(
    onSOSClick: () -> Unit,
    onRecordClick: () -> Unit,
    onLocationClick: () -> Unit,
    onSmartWalkClick: () -> Unit,
    detectedDevices: SnapshotStateList<Pair<String, Int>>,
    showSafetyDialog: Boolean,
    onDismissSafetyDialog: () -> Unit
) {

    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("home") }
    var showSafetyDialog by remember { mutableStateOf(false) }

    var smartWalkEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        showSplash = false
    }

    if (showSplash) {
        AuraSplashScreen()
        return
    }

    when (currentScreen) {

        "home" -> HomeScreen(
            onSOSClick = onSOSClick,
            onRecordClick = onRecordClick,
            onLocationClick = onLocationClick,
            onContactsClick = { currentScreen = "contacts" },
            onRadarClick = { currentScreen = "radar" },
            onFakeCallClick = { currentScreen = "fake" },
            onSmartWalkClick = onSmartWalkClick,
            detectedDevices = detectedDevices
        )

        "contacts" -> ContactsScreen(
            onBack = { currentScreen = "home" }
        )

        "radar" -> RadarScreen(
            devices = detectedDevices.toList(),
            onBack = { currentScreen = "home" }
        )

        "fake" -> FakeCallScreen(
            onEndCall = { currentScreen = "home" }
        )

    }
    if (showSafetyDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = { showSafetyDialog = false }) {
                    Text("I'm Safe")
                }
            },
            title = { Text("Safety Check") },
            text = { Text("We detected no movement.\nAre you safe?") }
        )
    }

}