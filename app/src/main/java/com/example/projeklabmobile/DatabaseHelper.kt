package com.example.projeklabmobile

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseHelper {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val wishlistRef: DatabaseReference = database.getReference("wishlist")

    // Add wishlist item
    fun addWishlistItem(item: WishlistItem, onComplete: (Boolean) -> Unit) {
        val key = wishlistRef.push().key ?: return
        wishlistRef.child(key).setValue(item)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    // Get all wishlist items
    fun getAllWishlistItems(onComplete: (List<WishlistItem>) -> Unit) {
        wishlistRef.get().addOnSuccessListener { snapshot ->
            val items = snapshot.children.mapNotNull { it.getValue(WishlistItem::class.java) }
            onComplete(items)
        }
    }

    // Update progress of wishlist item
    fun updateWishlistProgress(id: String, progress: Int, onComplete: (Boolean) -> Unit) {
        wishlistRef.child(id).child("progress").setValue(progress)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
