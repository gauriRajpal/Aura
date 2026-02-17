//
//package com.example.aura
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.media.MediaPlayer
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.speech.tts.TextToSpeech
//import android.telephony.SmsManager
//import android.view.KeyEvent
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import com.example.aura.ui.theme.AuraTheme
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import kotlin.math.sqrt
//import com.google.firebase.database.FirebaseDatabase
//import java.util.Locale
//
//class MainActivity : ComponentActivity(), SensorEventListener {
//
//    // Volume press tracking
//    private var volumePressCount = 0
//    private var lastPressTime: Long = 0
//
//    // Shake detection
//    private lateinit var sensorManager: SensorManager
//    private var lastShakeTime: Long = 0
//
//    // Text to Speech
//    private lateinit var tts: TextToSpeech
//
////
//        private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        val database = FirebaseDatabase.getInstance(
//            "https://aura-b8c86-default-rtdb.asia-southeast1.firebasedatabase.app"
//        )
//
//        val ref = database.getReference("connection_test")
//
//        ref.setValue("Hello from Aura")
//            .addOnSuccessListener {
//                Toast.makeText(this, "Firebase write success", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Firebase write failed: ${it.message}", Toast.LENGTH_LONG).show()
//            }
//
//
//
//        saveContact("+91XXXXXXXXXX")
//
//        val perms = mutableListOf(
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//        if (Build.VERSION.SDK_INT >= 31) {
//            perms.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//        }
//
//        ActivityCompat.requestPermissions(
//            this,
//            perms.toTypedArray(),
//            101
//        )
//
//
//        // Setup accelerometer
//        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        accelerometer?.also {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
//        }
//
//        tts = TextToSpeech(this) { status ->
//            if (status == TextToSpeech.SUCCESS) {
//                tts.language = Locale.US
//            }
//        }
//        fusedLocationClient = LocationServices
//            .getFusedLocationProviderClient(this)
//
//
//        setContent {
//            AuraTheme {
//                AuraApp()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    AlertScreen(
//                        modifier = Modifier.padding(innerPadding),
//                        onTrigger = { triggerEmergency() }
//                    )
//                }
//            }
//        }
//    }
//
//    // ---------------- VOLUME BUTTON TRIGGER ----------------
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
//            keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//
//            val currentTime = System.currentTimeMillis()
//
//            if (currentTime - lastPressTime < 1500) {
//                volumePressCount++
//            } else {
//                volumePressCount = 1
//            }
//
//            lastPressTime = currentTime
//
//            if (volumePressCount >= 3) {
//                volumePressCount = 0
//                triggerEmergency()
//            }
//        }
//
//        return super.onKeyDown(keyCode, event)
//    }
//
//    // ---------------- SHAKE DETECTION ----------------
//    override fun onSensorChanged(event: SensorEvent?) {
//
//        event?.let {
//            val x = it.values[0]
//            val y = it.values[1]
//            val z = it.values[2]
//
//            val acceleration = sqrt((x*x + y*y + z*z).toDouble())
//
//            if (acceleration > 18) { // shake threshold
//
//                val currentTime = System.currentTimeMillis()
//
//                if (currentTime - lastShakeTime > 1500) {
//                    lastShakeTime = currentTime
//                    triggerEmergency()
//                }
//            }
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//
//    // ---------------- EMERGENCY ACTION ----------------
////    private fun triggerEmergency() {
////
////        runOnUiThread {
////
////            // Speak emergency loudly
////            tts.speak(
////                "Emergency! Emergency!",
////                android.speech.tts.TextToSpeech.QUEUE_FLUSH,
////                null,
////                null
////            )
////
////            // OPTIONAL: play siren too
////            // val mediaPlayer = MediaPlayer.create(this, R.raw.siren)
////            // mediaPlayer.start()
////        }
////    }
//
//
//    private fun triggerEmergency() {
//
//        val phoneNumber = "+919799919727"
//        val smsManager = SmsManager.getDefault()
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Toast.makeText(this, "No location permission", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        Toast.makeText(this, "Getting location…", Toast.LENGTH_SHORT).show()
//
//        var sent = false
//
//        // Try last known first (instant if available)
//        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//            if (loc != null && !sent) {
//                val msg = "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
//                try {
//                    smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
//                    Toast.makeText(this, "Sent with lastLocation", Toast.LENGTH_LONG).show()
//                    sent = true
//                } catch (e: Exception) {
//                    Toast.makeText(this, "SMS error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }.addOnFailureListener {
//            Toast.makeText(this, "lastLocation failed", Toast.LENGTH_SHORT).show()
//        }
//
//        // Request fresh GPS after 2s if not sent
//        Handler(mainLooper).postDelayed({
//
//            if (sent) return@postDelayed
//
//            Toast.makeText(this, "Requesting fresh GPS…", Toast.LENGTH_SHORT).show()
//
//            val req = LocationRequest.Builder(
//                Priority.PRIORITY_HIGH_ACCURACY, 1000
//            ).setMaxUpdates(1).build()
//
//            val cb = object : LocationCallback() {
//                override fun onLocationResult(res: LocationResult) {
//                    val loc = res.lastLocation
//                    if (loc != null && !sent) {
//                        val msg = "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
//                        try {
//                            smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
//                            Toast.makeText(this@MainActivity, "Sent with fresh GPS", Toast.LENGTH_LONG).show()
//                            sent = true
//                        } catch (e: Exception) {
//                            Toast.makeText(this@MainActivity, "SMS error: ${e.message}", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(this@MainActivity, "Fresh GPS null", Toast.LENGTH_SHORT).show()
//                    }
//                    fusedLocationClient.removeLocationUpdates(this)
//                }
//            }
//
//            fusedLocationClient.requestLocationUpdates(req, cb, mainLooper)
//
//        }, 2000)
//
//        // Hard fallback after 7s
//        Handler(mainLooper).postDelayed({
//            if (!sent) {
//                try {
//                    smsManager.sendTextMessage(
//                        phoneNumber, null,
//                        "🚨 EMERGENCY! Location unavailable.",
//                        null, null
//                    )
//                    Toast.makeText(this, "Fallback SMS sent", Toast.LENGTH_LONG).show()
//                } catch (e: Exception) {
//                    Toast.makeText(this, "Fallback SMS error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }, 7000)
//    }
//
//
//    private fun saveContact(number: String) {
//
//        val database = FirebaseDatabase.getInstance(
//            "https://aura-b8c86-default-rtdb.asia-southeast1.firebasedatabase.app"
//        )
//
//        val ref = database.getReference("users")
//            .child("user1")
//            .child("contacts")
//            .push()
//
//        ref.setValue(number)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//
//
//
////    private fun triggerEmergency() {
////
////        val phoneNumber = "919799919727"
////
////        val smsManager = android.telephony.SmsManager.getDefault()
////
////        try {
////            smsManager.sendTextMessage(
////                phoneNumber,
////                null,
////                "TEST MESSAGE FROM AURA",
////                null,
////                null
////            )
////
////            Toast.makeText(this, "SMS SENT", Toast.LENGTH_LONG).show()
////
////        } catch (e: Exception) {
////            Toast.makeText(this, "SMS FAILED: ${e.message}", Toast.LENGTH_LONG).show()
////        }
////    }
//
//
//}
//
//// ---------------- COMPOSE UI ----------------
//@Composable
//fun AlertScreen(modifier: Modifier = Modifier, onTrigger: () -> Unit) {
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Button(onClick = { onTrigger() }) {
//            Text(text = "Trigger Aura Alert")
//        }
//    }
//}































package com.example.aura

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.*
import android.os.*
import android.speech.tts.TextToSpeech
import android.telephony.SmsManager
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.aura.ui.theme.AuraTheme
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale
import kotlin.math.sqrt
import androidx.compose.runtime.mutableStateListOf

class MainActivity : ComponentActivity(), SensorEventListener {

    private var volumePressCount = 0
    private var lastPressTime: Long = 0

    private lateinit var sensorManager: SensorManager
    private var lastShakeTime: Long = 0

    private var shakeCount = 0
    private var firstShakeTime = 0L


    private lateinit var tts: TextToSpeech
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var radar: BluetoothRadar


    val detectedDevices = mutableStateListOf<Pair<String, Int>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Permissions
        val perms = mutableListOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )


//        radar = BluetoothRadar(this) {
//            Toast.makeText(this, "⚠️ Same device following you", Toast.LENGTH_LONG).show()
//            triggerEmergency()
//        }


        radar = BluetoothRadar(
            context = this,
            onDeviceFound = { name, count ->

                val index = detectedDevices.indexOfFirst { it.first == name }

                if (index >= 0) {
                    detectedDevices[index] = name to count
                } else {
                    detectedDevices.add(name to count)
                }
            },
            onSuspicious = { name ->
                Toast.makeText(this, "⚠ Frequent nearby device: $name", Toast.LENGTH_LONG).show()
            }
        )



        val btperms = mutableListOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        ActivityCompat.requestPermissions(this, btperms.toTypedArray(), 101)


        // Sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accel?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // TTS
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) tts.language = Locale.US
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 🔴 IMPORTANT: CONNECT FRONTEND TO BACKEND HERE
        setContent {
            AuraTheme {
                AuraApp(
                    detectedDevices = detectedDevices,
                    onSOSClick = { triggerEmergency() },
                    onRecordClick = { startRecording() },
                    onLocationClick = { triggerEmergency() }
                )
            }
        }
    }
/// Scan
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (requestCode == 101 &&
        grantResults.isNotEmpty() &&
        grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

        radar.startScan()
    }
}


    // ---------------- VOLUME TRIGGER ----------------
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
            keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            val now = System.currentTimeMillis()

            volumePressCount =
                if (now - lastPressTime < 1500) volumePressCount + 1 else 1

            lastPressTime = now

            if (volumePressCount >= 3) {
                volumePressCount = 0
                triggerEmergency()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    // ---------------- SHAKE TRIGGER ----------------
//    override fun onSensorChanged(event: SensorEvent?) {
//        event?.let {
//            val acc = sqrt(
//                (it.values[0]*it.values[0] +
//                        it.values[1]*it.values[1] +
//                        it.values[2]*it.values[2]).toDouble()
//            )
//
//            if (acc > 18) {
//                val now = System.currentTimeMillis()
//                if (now - lastShakeTime > 1500) {
//                    lastShakeTime = now
//                    triggerEmergency()
//                }
//            }
//        }
//    }

    override fun onSensorChanged(event: SensorEvent?) {

        event?.let {

            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val acceleration = sqrt((x*x + y*y + z*z).toDouble())

            if (acceleration > 18) {

                val now = System.currentTimeMillis()

                if (shakeCount == 0) {
                    firstShakeTime = now
                }

                shakeCount++

                // Reset if shaking too slow
                if (now - firstShakeTime > 6000) {
                    shakeCount = 0
                }

                // Trigger after enough spikes in 6 sec window
                if (shakeCount >= 12) {
                    shakeCount = 0
                    triggerEmergency()
                }
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // ---------------- MAIN EMERGENCY ----------------

private fun triggerEmergency() {
    if (System.currentTimeMillis() - lastShakeTime < 5000) return
    lastShakeTime = System.currentTimeMillis()


    val smsManager = SmsManager.getDefault()

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Toast.makeText(this, "No location permission", Toast.LENGTH_LONG).show()
        return
    }

    Toast.makeText(this, "Getting location…", Toast.LENGTH_SHORT).show()

    val database = FirebaseDatabase.getInstance(
        "https://aura-b8c86-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    val contactsRef = database.getReference("users/user1/contacts")

    var sent = false

    // 🔹 Try last known location first
    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->

        val message =
            if (loc != null)
                "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
            else
                "🚨 EMERGENCY! Location unavailable."

        contactsRef.get().addOnSuccessListener { snapshot ->

            if (!snapshot.exists()) {
                Toast.makeText(this, "No contacts saved", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

//            snapshot.children.forEach { child ->
//
//                var number = child.child("number").value?.toString()
//
//                Log.d("AURA_DEBUG", "Raw Firebase number = $number")
//
//                number = number?.replace("\"", "")
//                    ?.replace(" ", "")
//                    ?.replace("-", "")
//                    ?.trim()
//
//                if (number != null && number.length == 10) {
//                    number = "+91$number"
//                }
//
//                Log.d("AURA_DEBUG", "Normalized number = $number")
//
//                // 🔴 TEMP: send to your own number to verify loop runs
//                val testNumber = "+919799919727"
//
//                try {
//                    smsManager.sendTextMessage(testNumber, null, message, null, null)
//                    Log.d("AURA_DEBUG", "SMS attempt made")
//                } catch (e: Exception) {
//                    Log.d("AURA_DEBUG", "SMS failed: ${e.message}")
//                }
//            }

            snapshot.children.forEach { child ->
                Log.d("AURA_DEBUG", "Child value = ${child.value}")
                Log.d("AURA_DEBUG", "Child children = ${child.children.toList()}")

                var number = child.child("number").value?.toString()

                if (!number.isNullOrBlank()) {

                    // 🔹 Normalize number safely
                    number = number.replace("\"", "")
                        .replace(" ", "")
                        .replace("-", "")
                        .trim()

                    if (number.length == 10) {
                        number = "+91$number"
                    }

                    try {
                        Log.d("AURA_DEBUG", "Child value = ${child.value}")
                        Log.d("AURA_DEBUG", "Child children = ${child.children.toList()}")
                        smsManager.sendTextMessage(number, null, message, null, null)
                        Toast.makeText(this, "Sent to $number", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Failed for $number", Toast.LENGTH_SHORT).show()
                    }
                }
            }



            Toast.makeText(this, "SOS sent to all contacts", Toast.LENGTH_LONG).show()
            sent = true
        }
    }

    // 🔹 Request fresh GPS if needed
    Handler(mainLooper).postDelayed({

        if (sent) return@postDelayed

        val req = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).setMaxUpdates(1).build()

        val cb = object : LocationCallback() {
            override fun onLocationResult(res: LocationResult) {

                val loc = res.lastLocation

                val message =
                    if (loc != null)
                        "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
                    else
                        "🚨 EMERGENCY! Location unavailable."

                contactsRef.get().addOnSuccessListener { snapshot ->

                    snapshot.children.forEach { child ->

                        val number = child.child("number").value?.toString()

                        if (!number.isNullOrBlank()) {
                            try {
                                smsManager.sendTextMessage(number, null, message, null, null)
                            } catch (e: Exception) {
                                Toast.makeText(this@MainActivity, "Failed for $number", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    Toast.makeText(this@MainActivity, "SOS sent with fresh GPS", Toast.LENGTH_LONG).show()
                    sent = true
                }

                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(req, cb, mainLooper)

    }, 2000)

    // 🔹 Hard fallback
    Handler(mainLooper).postDelayed({

        if (!sent) {

            contactsRef.get().addOnSuccessListener { snapshot ->

                snapshot.children.forEach { child ->

                    val number = child.child("number").value?.toString()

                    if (!number.isNullOrBlank()) {
                        try {
                            smsManager.sendTextMessage(
                                number,
                                null,
                                "🚨 EMERGENCY! Location unavailable.",
                                null,
                                null
                            )
                        } catch (_: Exception) {}
                    }
                }

                Toast.makeText(this, "Fallback SMS sent", Toast.LENGTH_LONG).show()
            }
        }

    }, 7000)
}


    // ---------------- RECORDING STUB ----------------
    private fun startRecording() {
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
    }
}
