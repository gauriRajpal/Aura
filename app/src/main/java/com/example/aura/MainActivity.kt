package com.example.aura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aura.ui.theme.AuraTheme
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        enableEdgeToEdge()
        setContent {
            AuraTheme {

                AuraApp()
                }
            }
        }
    }

