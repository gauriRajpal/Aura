
package com.example.aura

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aura.ui.theme.AuraTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {

    // Volume press tracking
    private var volumePressCount = 0
    private var lastPressTime: Long = 0

    // Shake detection
    private lateinit var sensorManager: SensorManager
    private var lastShakeTime: Long = 0

    // Text to Speech
    private lateinit var tts: android.speech.tts.TextToSpeech

//
        private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val perms = mutableListOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (android.os.Build.VERSION.SDK_INT >= 31) {
            perms.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        androidx.core.app.ActivityCompat.requestPermissions(
            this,
            perms.toTypedArray(),
            101
        )


        // Setup accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        tts = android.speech.tts.TextToSpeech(this) { status ->
            if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                tts.language = java.util.Locale.US
            }
        }
        fusedLocationClient = com.google.android.gms.location.LocationServices
            .getFusedLocationProviderClient(this)


        setContent {
            AuraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AlertScreen(
                        modifier = Modifier.padding(innerPadding),
                        onTrigger = { triggerEmergency() }
                    )
                }
            }
        }
    }

    // ---------------- VOLUME BUTTON TRIGGER ----------------
    override fun onKeyDown(keyCode: Int, event: android.view.KeyEvent?): Boolean {

        if (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN ||
            keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP) {

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastPressTime < 1500) {
                volumePressCount++
            } else {
                volumePressCount = 1
            }

            lastPressTime = currentTime

            if (volumePressCount >= 3) {
                volumePressCount = 0
                triggerEmergency()
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    // ---------------- SHAKE DETECTION ----------------
    override fun onSensorChanged(event: SensorEvent?) {

        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val acceleration = sqrt((x*x + y*y + z*z).toDouble())

            if (acceleration > 18) { // shake threshold

                val currentTime = System.currentTimeMillis()

                if (currentTime - lastShakeTime > 1500) {
                    lastShakeTime = currentTime
                    triggerEmergency()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // ---------------- EMERGENCY ACTION ----------------
//    private fun triggerEmergency() {
//
//        runOnUiThread {
//
//            // Speak emergency loudly
//            tts.speak(
//                "Emergency! Emergency!",
//                android.speech.tts.TextToSpeech.QUEUE_FLUSH,
//                null,
//                null
//            )
//
//            // OPTIONAL: play siren too
//            // val mediaPlayer = MediaPlayer.create(this, R.raw.siren)
//            // mediaPlayer.start()
//        }
//    }


    private fun triggerEmergency() {

        val phoneNumber = "+919799919727"
        val smsManager = android.telephony.SmsManager.getDefault()

        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "No location permission", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this, "Getting location…", Toast.LENGTH_SHORT).show()

        var sent = false

        // Try last known first (instant if available)
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null && !sent) {
                val msg = "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
                try {
                    smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
                    Toast.makeText(this, "Sent with lastLocation", Toast.LENGTH_LONG).show()
                    sent = true
                } catch (e: Exception) {
                    Toast.makeText(this, "SMS error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "lastLocation failed", Toast.LENGTH_SHORT).show()
        }

        // Request fresh GPS after 2s if not sent
        android.os.Handler(mainLooper).postDelayed({

            if (sent) return@postDelayed

            Toast.makeText(this, "Requesting fresh GPS…", Toast.LENGTH_SHORT).show()

            val req = com.google.android.gms.location.LocationRequest.Builder(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, 1000
            ).setMaxUpdates(1).build()

            val cb = object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(res: com.google.android.gms.location.LocationResult) {
                    val loc = res.lastLocation
                    if (loc != null && !sent) {
                        val msg = "🚨 EMERGENCY!\nhttps://maps.google.com/?q=${loc.latitude},${loc.longitude}"
                        try {
                            smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
                            Toast.makeText(this@MainActivity, "Sent with fresh GPS", Toast.LENGTH_LONG).show()
                            sent = true
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "SMS error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Fresh GPS null", Toast.LENGTH_SHORT).show()
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(req, cb, mainLooper)

        }, 2000)

        // Hard fallback after 7s
        android.os.Handler(mainLooper).postDelayed({
            if (!sent) {
                try {
                    smsManager.sendTextMessage(
                        phoneNumber, null,
                        "🚨 EMERGENCY! Location unavailable.",
                        null, null
                    )
                    Toast.makeText(this, "Fallback SMS sent", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Fallback SMS error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }, 7000)
    }






//    private fun triggerEmergency() {
//
//        val phoneNumber = "919799919727"
//
//        val smsManager = android.telephony.SmsManager.getDefault()
//
//        try {
//            smsManager.sendTextMessage(
//                phoneNumber,
//                null,
//                "TEST MESSAGE FROM AURA",
//                null,
//                null
//            )
//
//            Toast.makeText(this, "SMS SENT", Toast.LENGTH_LONG).show()
//
//        } catch (e: Exception) {
//            Toast.makeText(this, "SMS FAILED: ${e.message}", Toast.LENGTH_LONG).show()
//        }
//    }


}

// ---------------- COMPOSE UI ----------------
@Composable
fun AlertScreen(modifier: Modifier = Modifier, onTrigger: () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = { onTrigger() }) {
            Text(text = "Trigger Aura Alert")
        }
    }
}
