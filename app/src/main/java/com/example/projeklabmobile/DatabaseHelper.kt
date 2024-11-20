package com.example.projeklabmobile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    private val wishlistRef: DatabaseReference
        get() = database.getReference("wishlist").child(userId)

    // Add wishlist item
    fun addWishlistItem(item: WishlistItem, onComplete: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(false)
            return
        }
        val key = wishlistRef.push().key ?: return
        wishlistRef.child(key).setValue(item)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    // Get all wishlist items
    fun getAllWishlistItems(onComplete: (List<WishlistItem>) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(emptyList())
            return
        }
        wishlistRef.get().addOnSuccessListener { snapshot ->
            val items = snapshot.children.mapNotNull { it.getValue(WishlistItem::class.java) }
            onComplete(items)
        }
    }

    // Update progress of wishlist item
    fun updateWishlistProgress(id: String, progress: Int, onComplete: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(false)
            return
        }
        wishlistRef.child(id).child("progress").setValue(progress)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
