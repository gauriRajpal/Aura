
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
    private fun triggerEmergency() {

        runOnUiThread {

            // Speak emergency loudly
            tts.speak(
                "Emergency! Emergency!",
                android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )

            // OPTIONAL: play siren too
            // val mediaPlayer = MediaPlayer.create(this, R.raw.siren)
            // mediaPlayer.start()
        }
    }

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
