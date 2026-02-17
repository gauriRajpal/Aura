package com.example.aura

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun RecordingHistoryScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    // Load recordings
    val recordings = remember {
        context.filesDir.listFiles()
            ?.filter { it.name.startsWith("aura_recording") }
            ?: emptyList()
    }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        // 🔙 Back Button
        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("🎙 Recording History")

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(recordings) { file ->
                RecordingItem(file) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(file.absolutePath)
                        prepare()
                        start()
                    }
                }
            }
        }
    }
}

@Composable
fun RecordingItem(file: File, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = file.name)
            Text(text = "Tap to play", modifier = Modifier.padding(top = 4.dp))
        }
    }
}
