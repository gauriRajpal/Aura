package com.example.aura

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.platform.LocalContext


@Composable
fun ContactsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var number by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(listOf<String>()) }

    val db = FirebaseDatabase.getInstance(
        "https://aura-b8c86-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    val ref = db.getReference("users/user1/contacts")

    // Load contacts automatically
    LaunchedEffect(Unit) {
        ref.get().addOnSuccessListener { snap ->
            val list = mutableListOf<String>()
            snap.children.forEach {
                val num = it.child("number").value?.toString()
                if (num != null) list.add(num)
            }
            contacts = list
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Emergency Contacts", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = number,
            onValueChange = { number = it },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                val normalized = normalizeNumber(number)

                if (normalized != null) {

                    val id = ref.push().key!!
                    ref.child(id).child("number").setValue(normalized)

                    contacts = contacts + normalized
                    number = ""

                } else {
                    Toast.makeText(context, "Enter valid number", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Contact")
        }


        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(contacts) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = contact,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
