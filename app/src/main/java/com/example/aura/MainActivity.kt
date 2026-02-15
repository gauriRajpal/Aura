package com.example.aura

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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AuraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AlertScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AlertScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {

                // Show toast message
                Toast.makeText(context, "🚨 Aura Alert Triggered!", Toast.LENGTH_SHORT).show()

                // OPTIONAL: play siren sound if you add siren.mp3 in res/raw
                // val mediaPlayer = MediaPlayer.create(context, R.raw.siren)
                // mediaPlayer.start()

            }
        ) {
            Text(text = "Trigger Aura Alert")
        }
    }
}
