package com.example.aura

import com.google.firebase.database.FirebaseDatabase

object ContactRepository {

    private val db = FirebaseDatabase.getInstance(
        "https://aura-b8c86-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    private val ref = db.getReference("users/user1/contacts")

    fun addContact(number: String) {
        val id = ref.push().key ?: return
        ref.child(id).setValue(Contact(number, false))
    }

    fun setDefault(id: String) {
        ref.get().addOnSuccessListener { snap ->
            snap.children.forEach {
                it.ref.child("isDefault").setValue(false)
            }
            ref.child(id).child("isDefault").setValue(true)
        }
    }
}
