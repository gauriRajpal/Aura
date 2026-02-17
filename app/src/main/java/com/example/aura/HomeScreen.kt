package com.example.aura


import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
@Composable
fun HomeScreen(
    onSOSClick: () -> Unit,
    onRecordClick: () -> Unit,
    onLocationClick: () -> Unit,
    onContactsClick: () -> Unit,
    onRadarClick: () -> Unit,
    onFakeCallClick: () -> Unit,
    onSmartWalkClick: () -> Unit,
    detectedDevices: List<Pair<String, Int>>
) {

    var smartWalkEnabled by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val audioRecorder = remember { AudioRecorder(context) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Toast.makeText(
            context,
            if (granted) "Mic permission granted" else "Mic permission denied",
            Toast.LENGTH_SHORT
        ).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81)
                    )
                )
            )
    ) {

        Image(
            painter = painterResource(id = R.drawable.aura_illustration),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(250.dp)
                .alpha(0.08f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // Top bar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Aura", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Text("Your Active Guardian", fontSize = 14.sp, color = Color(0xFFB8B5FF))
                }

                Box(
                    Modifier.size(42.dp).background(Color(0xFF1F2937), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚙", fontSize = 18.sp)
                }
            }

            Spacer(Modifier.height(40.dp))

            // SOS button
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                1f, 1.08f,
                infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                label = "pulse"
            )

            Button(
                onClick = onSOSClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4D6D)),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(140.dp)
                    .scale(scale)
            ) {
                Text("SOS", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(40.dp))

            // Features title
            Box(
                Modifier.fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Features", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            Column {

//                FeatureRow(
//                    title = "Smart Walk",
//                    subtitle = if (smartWalkEnabled) "Active" else "Inactive",
//                    onClick = { smartWalkEnabled = !smartWalkEnabled }
//                )
                FeatureRow(
                    title = "Smart Walk",
                    subtitle = "Tap to start monitoring",
                    onClick = { onSmartWalkClick() }
                )

                FeatureRow(
                    title = "Safe Locations",
                    subtitle = "Find safe locations",
                    onClick = onLocationClick
                )

                FeatureRow(
                    title = "Emergency Contacts",
                    subtitle = "Manage contacts",
                    onClick = onContactsClick
                )

                FeatureRow(
                    title = "Fake Call",
                    subtitle = "Trigger escape call",
                    onClick = onFakeCallClick
                )

                FeatureRow(
                    title = "Anti-Stalking Radar",
                    subtitle = "${detectedDevices.size} nearby devices",
                    onClick = onRadarClick
                )

                FeatureRow(
                    title = "Recording",
                    subtitle = if (isRecording) "Recording..." else "Tap to Record",
                    onClick = {

                        val hasPermission =
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED

                        if (!hasPermission) {
                            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        } else {

                            try {
                                if (!isRecording) {
                                    audioRecorder.startRecording()
                                    Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()
                                } else {
                                    val file = audioRecorder.stopRecording()
                                    Toast.makeText(
                                        context,
                                        "Saved: ${file?.name}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                isRecording = !isRecording

                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )


                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = { showHistory = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Saved Recordings")
                }
            }
        }
    }

    // Recording history overlay
    if (showHistory) {
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f))
        ) {
            RecordingHistoryScreen(
                onBack = { showHistory = false }
            )
        }
    }
}

//package com.example.aura
//
//
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.runtime.snapshots.SnapshotStateList
//
//@Composable
//fun HomeScreen( onSOSClick: () -> Unit,
//                onRecordClick: () -> Unit,
//                onLocationClick: () -> Unit,
//                onContactsClick: () -> Unit,
//                onRadarClick: () -> Unit,
//                onFakeCallClick: () -> Unit,
//                detectedDevices: SnapshotStateList<Pair<String, Int>>) {
//
//    var smartWalkEnabled by remember { mutableStateOf(false) }
//    var isRecording by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF0F172A),
//                        Color(0xFF1E1B4B),
//                        Color(0xFF312E81)
//                    )
//                )
//            )
//    )
//    {
//        Image(
//            painter = painterResource(id = R.drawable.aura_illustration),
//            contentDescription = null,
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .size(250.dp)
//                .alpha(0.08f)
//        )
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())   // 👈 THIS LINE ADDED
//                .padding(20.dp)
//        )
//        {
//
//            // 🔹 Top Bar
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Column {
//                    Text(
//                        text = "Aura",
//                        fontSize = 26.sp,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = Color.White,
//                        letterSpacing = 2.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "Your Active Guardian",
//                        fontSize = 14.sp,
//                        color = Color(0xFFB8B5FF)
//                    )
//                }
//
//                Box(
//                    modifier = Modifier
//                        .size(42.dp)
//                        .background(
//                            Color(0xFF1F2937),
//                            shape = CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("⚙", fontSize = 18.sp)
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // 🔴 SOS Button
//            // 🔴 SOS Button (Clean Pulse Version)
//
//            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
//
//            val scale by infiniteTransition.animateFloat(
//                initialValue = 1f,
//                targetValue = 1.08f,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(1000),
//                    repeatMode = RepeatMode.Reverse
//                ),
//                label = "pulseAnim"
//            )
//            var pressed by remember { mutableStateOf(false) }
//
//            val pressScale by animateFloatAsState(
//                targetValue = if (pressed) 0.9f else 1f,
//                animationSpec = tween(120),
//                label = "pressAnim"
//            )
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Button(
//                    onClick = {
//                        pressed = true
//                        pressed = false
//                        onSOSClick()
//                        // TODO: Trigger SOS
//                    }
//                    ,
//                    shape = CircleShape,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFFFF4D6D)
//                    ),
//                    modifier = Modifier
//                        .size(140.dp)
//                        .scale(scale * pressScale)
//
//                ) {
//                    Text(
//                        text = "SOS",
//                        fontSize = 26.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                }
//            }
//
//
//
//            Spacer(modifier = Modifier.height(30.dp))
//            Spacer(modifier = Modifier.height(30.dp))
//
//// 🔹 Section Title Box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(
//                                Color(0xFF7C3AED),
//                                Color(0xFF4F46E5)
//                            )
//                        ),
//                        shape = RoundedCornerShape(16.dp)
//                    )
//                    .padding(vertical = 14.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Set as Background",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White,
//                    letterSpacing = 1.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 🔹 Feature Grid
//            Column {
//
//                FeatureRow(
//                    title = "Smart Walk",
//                    subtitle = if (smartWalkEnabled) "Active" else "Inactive",
//                    onClick = { smartWalkEnabled = !smartWalkEnabled }
//                )
//
//                FeatureRow(
//                    title = "Live Location",
//                    subtitle = "Share with Guardians",
//                    onClick = { onLocationClick() }
//                )
//
//                var showContacts by remember { mutableStateOf(false) }
//
//                FeatureRow(
//                    title = "Emergency Contacts",
//                    subtitle = "Manage contacts",
//                    onClick = { onContactsClick() }
//                )
//
////                if (showContacts) {
////                    ContactsScreen()
////                }
//
//
//                FeatureRow(
//                    title = "Fake Call",
//                    subtitle = "Trigger escape call",
//                    onClick = { /* Trigger fake call */ }
//                )
//                FeatureRow(
//                    title = "Anti-Stalking Radar",
//                    subtitle = "Scan nearby devices",
//                    onClick = onRadarClick
//                )
//
//                FeatureRow(
//                    title = "Recording",
//                    subtitle = if (isRecording) "Recording..." else "Tap to Record",
//                    onClick = { isRecording = !isRecording
//                        onRecordClick()}
//                )
//            }
//        }
//    }
//}