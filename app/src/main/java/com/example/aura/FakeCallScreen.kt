package com.example.aura

import android.media.MediaPlayer
import android.net.Uri
import android.provider.Settings
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale




@Composable
fun FakeCallScreen(onEndCall: () -> Unit) {

    val context = LocalContext.current

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var callConnected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        // 🔔 Play default ringtone
        val ringtoneUri: Uri = Settings.System.DEFAULT_RINGTONE_URI
        mediaPlayer = MediaPlayer.create(context, ringtoneUri)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        delay(3000)

        // Stop ringtone
        mediaPlayer?.stop()

        callConnected = true

        // 🎙️ Voice playback using TTS
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.speak(
                    "This is Police Control Room. Unit 4 is arriving near your location. Please stay calm.",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            tts?.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color(0xFF0F172A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = if (callConnected) "Call Connected" else "Incoming Call",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Police Emergency Unit",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    mediaPlayer?.stop()
                    tts?.shutdown()
                    onEndCall()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = CircleShape,
                modifier = Modifier.size(90.dp)
            ) {
                Text("End", color = Color.White)
            }
        }
    }
}
