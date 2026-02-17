//package com.example.aura
//
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.content.*
//import android.os.SystemClock
//import android.util.Log
//import androidx.core.content.ContextCompat
//import android.content.pm.PackageManager
//import android.Manifest
//
//class BluetoothRadar(
//    private val context: Context,
//    private val onDeviceFound: (String) -> Unit,
//    private val onSuspicious: (String) -> Unit
//) {
//
//    private val adapter = BluetoothAdapter.getDefaultAdapter()
//
//    // Tracks detections of devices
//    private val seenDevices = mutableMapOf<String, MutableList<Long>>()
//
//    // Prevents same device alert spam
//    private val alertedDevices = mutableSetOf<String>()
//
//    private val receiver = object : BroadcastReceiver() {
//
//        override fun onReceive(c: Context?, intent: Intent?) {
//
//            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
//
//                val device: BluetoothDevice? =
//                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//
//                val address = device?.address ?: return
//                val name = try {
//                    if (androidx.core.content.ContextCompat.checkSelfPermission(
//                            context,
//                            android.Manifest.permission.BLUETOOTH_CONNECT
//                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//                    ) {
//                        device.name ?: "Unknown device"
//                    } else {
//                        "Unknown device"
//                    }
//                } catch (e: SecurityException) {
//                    "Unknown device"
//                }
//
//
//                Log.d("AURA_BT", "Found device: $name")
//
//                val now = SystemClock.elapsedRealtime()
//
//                val timestamps = seenDevices.getOrPut(address) { mutableListOf() }
//                timestamps.add(now)
//
//                // Remove detections older than 3 minutes
//                timestamps.removeIf { now - it > 180000 }
//
//                // If detected repeatedly → suspicious
//                if (timestamps.size >= 4 && !alertedDevices.contains(address)) {
//                    alertedDevices.add(address)
//                    Log.d("AURA_BT", "Suspicious device: $name")
//                    onSuspicious(name)
//                }
//            }
//        }
//    }
//
//    fun startScan() {
//
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        context.registerReceiver(receiver, filter)
//
//        try {
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//
//                adapter?.startDiscovery()
//                Log.d("AURA_BT", "Bluetooth discovery started")
//
//            } else {
//                Log.d("AURA_BT", "Bluetooth scan permission missing")
//            }
//        } catch (e: SecurityException) {
//            Log.d("AURA_BT", "Security exception: ${e.message}")
//        }
//    }
//
//    fun stopScan() {
//
//        try {
//            context.unregisterReceiver(receiver)
//        } catch (_: Exception) {}
//
//        try {
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//
//                adapter?.cancelDiscovery()
//                Log.d("AURA_BT", "Bluetooth discovery stopped")
//
//            }
//        } catch (e: SecurityException) {
//            Log.d("AURA_BT", "Security exception: ${e.message}")
//        }
//    }
//}

package com.example.aura

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat

class BluetoothRadar(
    private val context: Context,
    private val onDeviceFound: (String, Int) -> Unit,
    private val onSuspicious: (String) -> Unit
) {

    private val adapter = BluetoothAdapter.getDefaultAdapter()

    // Track timestamps for each device
    private val seenDevices = mutableMapOf<String, MutableList<Long>>()

    // Prevent alert spam
    private val alertedDevices = mutableSetOf<String>()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(c: Context?, intent: Intent?) {

            if (intent?.action == BluetoothDevice.ACTION_FOUND) {

                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                val address = device?.address ?: return

                val name = try {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        device.name ?: "Unknown device"
                    } else {
                        "Unknown device"
                    }
                } catch (_: SecurityException) {
                    "Unknown device"
                }

                val now = SystemClock.elapsedRealtime()

                val timestamps = seenDevices.getOrPut(address) { mutableListOf() }
                timestamps.add(now)

                // remove old detections (older than 3 min)
                timestamps.removeIf { now - it > 180000 }

                val count = timestamps.size

                Log.d("AURA_BT", "Found $name count=$count")

                // 🔵 Send to UI every time
                onDeviceFound(name, count)

                // 🔴 suspicious logic
                if (count >= 4 && !alertedDevices.contains(address)) {
                    alertedDevices.add(address)
                    onSuspicious(name)
                }
            }
        }
    }

    fun startScan() {

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)

        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                adapter?.startDiscovery()
                Log.d("AURA_BT", "Discovery started")
            }
        } catch (e: SecurityException) {
            Log.d("AURA_BT", "Scan error ${e.message}")
        }
    }

    fun stopScan() {
        try { context.unregisterReceiver(receiver) } catch (_: Exception) {}

        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                adapter?.cancelDiscovery()
            }
        } catch (_: SecurityException) {}
    }
}

