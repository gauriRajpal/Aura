package com.example.aura


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(MapsComposeExperimentalApi::class)
@SuppressLint("MissingPermission")
@Composable
fun SafeZoneScreen(onBack: () -> Unit) {

    var safetyScore by remember { mutableStateOf(75) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(28.6139, 77.2090), // Default Delhi
            14f
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
        ) {

            // 🟢 Safe Zone Markers
            Marker(
                state = MarkerState(position = LatLng(28.6145, 77.2100)),
                title = "Police Station",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )

            Marker(
                state = MarkerState(position = LatLng(28.6120, 77.2080)),
                title = "24/7 Hospital",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )

            Marker(
                state = MarkerState(position = LatLng(28.6155, 77.2120)),
                title = "Safe Cafe",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )

            // 🔴 Example Danger Zone Circle
            Circle(
                center = LatLng(28.6115, 77.2050),
                radius = 300.0,
                fillColor = Color.Red.copy(alpha = 0.2f),
                strokeColor = Color.Red
            )
        }

        // 🟢 Safety Score Card
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E293B)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Safety Score",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "$safetyScore / 100",
                    color = Color.Green,
                    fontSize = 20.sp
                )
            }
        }

        // 🟢 Route Suggestion Button
        Button(
            onClick = {
                safetyScore = 90 // Simulated safer route
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Text("Take Me to Safest Route")
        }
    }
}
