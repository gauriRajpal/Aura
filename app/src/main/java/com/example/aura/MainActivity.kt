package com.example.aura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.aura.ui.theme.AuraTheme
import androidx.compose.animation.core.animateFloatAsState



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuraTheme {

                AuraApp()
                }
            }
        }
    }


@Composable
fun AuraApp() {

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(4000)
        showSplash = false
    }

    if (showSplash) {
        AuraSplashScreen()
    } else {
        HomeScreen()
    }
}
@Composable
fun HomeScreen() {

    var smartWalkEnabled by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81)
                    )
                )
            )
    )
    {
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
                .padding(20.dp)
        ) {

            // 🔹 Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Aura",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your Active Guardian",
                        fontSize = 14.sp,
                        color = Color(0xFFB8B5FF)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Color(0xFF1F2937),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚙", fontSize = 18.sp)
                }
            }


            Spacer(modifier = Modifier.height(40.dp))

            // 🔴 SOS Button
            // 🔴 SOS Button (Clean Pulse Version)

            val infiniteTransition = rememberInfiniteTransition(label = "pulse")

            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAnim"
            )
            var pressed by remember { mutableStateOf(false) }

            val pressScale by animateFloatAsState(
                targetValue = if (pressed) 0.9f else 1f,
                animationSpec = tween(120),
                label = "pressAnim"
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {

                Button(
                    onClick = {
                        pressed = true
                        pressed = false
                        // TODO: Trigger SOS
                    }
                    ,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4D6D)
                    ),
                    modifier = Modifier
                        .size(140.dp)
                        .scale(scale * pressScale)

                ) {
                    Text(
                        text = "SOS",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }



            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Feature Grid
            Column {

                FeatureRow(
                    title = "Smart Walk",
                    subtitle = if (smartWalkEnabled) "Active" else "Inactive",
                    onClick = { smartWalkEnabled = !smartWalkEnabled }
                )

                FeatureRow(
                    title = "Live Location",
                    subtitle = "Share with Guardians",
                    onClick = { /* Start location logic */ }
                )

                FeatureRow(
                    title = "Emergency Contacts",
                    subtitle = "Manage contacts",
                    onClick = { /* Navigate */ }
                )

                FeatureRow(
                    title = "Fake Call",
                    subtitle = "Trigger escape call",
                    onClick = { /* Trigger fake call */ }
                )

                FeatureRow(
                    title = "Recording",
                    subtitle = if (isRecording) "Recording..." else "Tap to Record",
                    onClick = { isRecording = !isRecording }
                )
            }
        }
    }
}
@Composable
fun FeatureRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}


@Composable
fun AuraSplashScreen() {

    var visible by remember { mutableStateOf(false) }

    // Trigger animation
    LaunchedEffect(Unit) {
        visible = true
    }

    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "fadeAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF312E81)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alphaAnim)
        ) {

            // Image
            Image(
                painter = painterResource(id = R.drawable.aura_illustration),
                contentDescription = "Aura Illustration",
                modifier = Modifier
                    .height(240.dp)
                    .padding(bottom = 20.dp)
            )

            // App Name
            Text(
                text = "AURA",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "The Active Guardian",
                fontSize = 18.sp,
                color = Color(0xFFA5B4FC)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We stand up for your safety.",
                fontSize = 14.sp,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}




