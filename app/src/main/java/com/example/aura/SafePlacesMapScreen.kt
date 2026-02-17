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
import android.content.pm.PackageManager
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

@Composable
fun SafePlacesMapScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDxr8vBeEZuxnyTwXZ68-aySE-C0UEVSpk")   // 🔴 PUT YOUR KEY HERE
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

                            if (location != null) {

                                val userLatLng = LatLng(location.latitude, location.longitude)

                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)
                                )

                                map.addMarker(
                                    MarkerOptions()
                                        .position(userLatLng)
                                        .title("You are here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                )

                                // ⭐ FETCH REAL SAFE PLACES
                                fetchSafePlaces(ctx, userLatLng) { places ->

                                    places.forEach { place ->

                                        val latLng = place.latLng ?: return@forEach
                                        val name = place.name ?: "Safe Place"

                                        map.addMarker(
                                            MarkerOptions()
                                                .position(latLng)
                                                .title(name)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        )
                                    }
                                }

                            } else {
                                val fallback = LatLng(26.4499, 74.6399)
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(fallback, 14f)
                                )
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
        modifier = Modifier.fillMaxSize()
    )
}
fun fetchSafePlaces(
    context: android.content.Context,
    location: LatLng,
    onResult: (List<Place>) -> Unit
) {

    val hasPermission =
        androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    if (!hasPermission) {
        onResult(emptyList())
        return
    }

    val client = Places.createClient(context)

    val fields = listOf(
        Place.Field.NAME,
        Place.Field.LAT_LNG
    )

    val request = FindCurrentPlaceRequest.newInstance(fields)

    client.findCurrentPlace(request)
        .addOnSuccessListener { response ->
            val places = response.placeLikelihoods.map { it.place }
            onResult(places)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}
