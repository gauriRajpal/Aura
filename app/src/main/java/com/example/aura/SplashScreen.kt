package com.example.aura



import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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