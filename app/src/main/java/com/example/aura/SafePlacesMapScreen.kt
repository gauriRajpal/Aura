//package com.example.aura
//
//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.app.ActivityCompat
//import com.google.android.gms.maps.*
//import com.google.android.gms.maps.model.*
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.*
//
//@Composable
//fun SafePlacesMapScreen(onBack: () -> Unit) {
//
//    val context = LocalContext.current
//
//    // Initialize Places SDK once
//    LaunchedEffect(Unit) {
//        if (!Places.isInitialized()) {
//            Places.initialize(context, "YOUR_API_KEY")   // 🔴 PUT YOUR KEY HERE
//        }
//    }
//
//    AndroidView(
//        factory = { ctx ->
//
//            MapView(ctx).apply {
//
//                onCreate(null)
//
//                getMapAsync { map ->
//
//                    map.uiSettings.isZoomControlsEnabled = true
//
//                    val fusedLocationClient =
//                        com.google.android.gms.location.LocationServices
//                            .getFusedLocationProviderClient(ctx)
//
//                    // Check permission first
//                    if (ActivityCompat.checkSelfPermission(
//                            ctx,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//
//                        map.isMyLocationEnabled = true
//
//                        // Get last known user location
//                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//
//                            if (location != null) {
//
//                                val userLatLng = LatLng(location.latitude, location.longitude)
//
//                                // Move camera to user
//                                map.moveCamera(
//                                    CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)
//                                )
//
//                                // Marker for user
//                                map.addMarker(
//                                    MarkerOptions()
//                                        .position(userLatLng)
//                                        .title("You are here")
//                                )
//
//                                // 👇 Example safe place near user (demo)
//                                val safePlace = LatLng(
//                                    location.latitude + 0.003,
//                                    location.longitude + 0.003
//                                )
//
//                                map.addMarker(
//                                    MarkerOptions()
//                                        .position(safePlace)
//                                        .title("Nearby Safe Zone")
//                                )
//
//                            } else {
//
//                                // fallback location if GPS null
//                                val fallback = LatLng(26.4499, 74.6399)
//                                map.moveCamera(
//                                    CameraUpdateFactory.newLatLngZoom(fallback, 14f)
//                                )
//                            }
//                        }
//
//                    } else {
//
//                        // Permission not granted → fallback location
//                        val fallback = LatLng(26.4499, 74.6399)
//                        map.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(fallback, 14f)
//                        )
//                    }
//                }
//
//            }
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}
//fun fetchSafePlaces(
//    context: android.content.Context,
//    location: LatLng,
//    onResult: (List<Place>) -> Unit
//) {
//
//    // 🔴 Check permission FIRST
//    val hasPermission =
//        androidx.core.content.ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//
//    if (!hasPermission) {
//        onResult(emptyList())
//        return
//    }
//
//    val client = Places.createClient(context)
//
//    val fields = listOf(
//        Place.Field.NAME,
//        Place.Field.LAT_LNG
//    )
//
//    val request = FindCurrentPlaceRequest.newInstance(fields)
//
//    client.findCurrentPlace(request)
//        .addOnSuccessListener { response ->
//            val places = response.placeLikelihoods.map { it.place }
//            onResult(places)
//        }
//        .addOnFailureListener {
//            onResult(emptyList())
//        }
//}
//
//
package com.example.aura

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.unit.dp

@Composable
fun SafePlacesMapScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    var nearestLatLng by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(
                context,
                "Your API key"
            )
        }
    }
Column(Modifier.fillMaxSize()){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(onClick = { onBack() }) {
            Text("← Back")
        }
    }
    AndroidView(
        factory = { ctx ->

            MapView(ctx).apply {

                onCreate(null)

                getMapAsync { map ->

                    map.uiSettings.isZoomControlsEnabled = true

                    val fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(ctx)

                    if (ActivityCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        map.isMyLocationEnabled = true

                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

                            if (location == null) return@addOnSuccessListener

                            val userLatLng =
                                LatLng(location.latitude, location.longitude)

                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)
                            )

                            // 🔴 USER MARKER
                            map.addMarker(
                                MarkerOptions()
                                    .position(userLatLng)
                                    .title("You are here")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                    )
                            )

                            // ⭐ FETCH REAL SAFE PLACES
                            fetchSafePlaces(ctx, userLatLng) { places, nearest ->

                                places.forEach { place ->

                                    val latLng = place.latLng ?: return@forEach
                                    val distance =
                                        distanceBetween(userLatLng, latLng).toInt()

                                    val isNearest = place == nearest
                                    if (isNearest) {
                                        nearestLatLng = latLng
                                    }

                                    val marker = map.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(place.name)
                                            .snippet("${distance} meters away")
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    if (isNearest)
                                                        BitmapDescriptorFactory.HUE_AZURE
                                                    else
                                                        BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                    )

                                    // ⭐ SHOW NAME AUTOMATICALLY
                                    marker?.showInfoWindow()
                                }
                            }
                        }

                    } else {

                        val fallback = LatLng(26.4499, 74.6399)
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(fallback, 14f)
                        )
                    }
                }
            }
        },
        modifier = Modifier.weight(1f)
    )
    nearestLatLng?.let { target ->

        Button(
            onClick = { navigateToPlace(context, target) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Navigate to Nearest Safe Place")
        }
    }}
}

fun fetchSafePlaces(
    context: android.content.Context,
    userLatLng: LatLng,
    onResult: (List<Place>, Place?) -> Unit
) {

    val hasPermission =
        androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    if (!hasPermission) {
        onResult(emptyList(), null)
        return
    }

    val client = Places.createClient(context)

    val fields = listOf(
        Place.Field.NAME,
        Place.Field.LAT_LNG,
        Place.Field.TYPES
    )

    val request = FindCurrentPlaceRequest.newInstance(fields)

    client.findCurrentPlace(request)
        .addOnSuccessListener { response ->

            val places = response.placeLikelihoods.map { it.place }

            // ⭐ FIND NEAREST PLACE
            var nearestPlace: Place? = null
            var minDistance = Float.MAX_VALUE

            places.forEach { place ->

                val latLng = place.latLng ?: return@forEach
                val dist = distanceBetween(userLatLng, latLng)

                if (dist < minDistance) {
                    minDistance = dist
                    nearestPlace = place
                }
            }

            onResult(places, nearestPlace)
        }
        .addOnFailureListener {
            onResult(emptyList(), null)
        }
}

fun distanceBetween(a: LatLng, b: LatLng): Float {
    val results = FloatArray(1)
    android.location.Location.distanceBetween(
        a.latitude, a.longitude,
        b.latitude, b.longitude,
        results
    )
    return results[0]
}

fun navigateToPlace(context: Context, latLng: LatLng) {

    val uri = Uri.parse(
        "google.navigation:q=${latLng.latitude},${latLng.longitude}"
    )

    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Google Maps not installed", Toast.LENGTH_SHORT).show()
    }
}
