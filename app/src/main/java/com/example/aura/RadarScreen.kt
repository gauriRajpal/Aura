package com.example.aura

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun RadarScreen(
//    devices: List<Pair<String, Int>>,
//    onBack: () -> Unit
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//    ) {
//
//        // 🔙 Back button
//        Text(
//            text = "← Back",
//            color = Color(0xFF7C3AED),
//            fontSize = 16.sp,
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//                .clickable { onBack() }
//        )
//
//        // Title
//        Text(
//            text = "Nearby Devices",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // If no devices
//        if (devices.isEmpty()) {
//            Text(
//                text = "Scanning for nearby devices...",
//                color = Color.LightGray,
//                fontSize = 14.sp
//            )
//        }
//
//        // Device list
//        devices.forEach { (name, count) ->
//
//            val color =
//                if (count >= 4) Color.Red   // suspicious
//                else Color(0xFF7C3AED)
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = color.copy(alpha = 0.2f)
//                ),
//                shape = RoundedCornerShape(14.dp)
//            ) {
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    Text(
//                        text = name,
//                        color = Color.White,
//                        fontSize = 16.sp
//                    )
//
//                    Text(
//                        text = "Seen $count",
//                        color = Color.LightGray,
//                        fontSize = 13.sp
//                    )
//                }
//            }
//        }
//    }
//}


@Composable
fun RadarScreen(
    devices: List<Pair<String, Int>>,
    onBack: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // 🔙 Back button
            Text(
                text = "← Back",
                color = Color(0xFFB8B5FF),
                fontSize = 16.sp,
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.height(20.dp))

            // 🔴 Title
            Text(
                text = "Anti-Stalking Radar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Nearby Bluetooth activity",
                fontSize = 14.sp,
                color = Color(0xFFB8B5FF)
            )

            Spacer(Modifier.height(24.dp))

            // 📡 Device list
            LazyColumn {

                items(devices) { (name, count) ->

                    val suspicious = count >= 4

                    val bgColor =
                        if (suspicious) Color(0xFF7F1D1D)
                        else Color(0xFF1F2937)

                    val glow =
                        if (suspicious) Color.Red.copy(alpha = 0.4f)
                        else Color.Transparent

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(
                                elevation = if (suspicious) 20.dp else 6.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = glow,
                                spotColor = glow
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = bgColor
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(
                                    text = name,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text =
                                    if (suspicious)
                                        "⚠ Frequent nearby device"
                                    else
                                        "Normal detection",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            Text(
                                text = "Seen $count",
                                color = Color(0xFFB8B5FF),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
