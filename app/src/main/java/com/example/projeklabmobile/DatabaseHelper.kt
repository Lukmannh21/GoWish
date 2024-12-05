package com.example.projeklabmobile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val userId: String
        get() = auth.currentUser?.uid ?: ""  // Mengambil ID user yang sedang login

    private val wishlistRef: DatabaseReference
        get() = database.getReference("wishlist").child(userId)

    // Menambahkan item ke wishlist
    fun addWishlistItem(item: WishlistItem, onComplete: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(false)  // Mengembalikan false jika user tidak login
            return
        }
        val key = wishlistRef.push().key ?: return
        wishlistRef.child(key).setValue(item)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)  // Menyelesaikan callback dengan status sukses
            }
    }

    // Mengambil semua item wishlist
    fun getAllWishlistItems(onComplete: (List<WishlistItem>) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(emptyList())  // Mengembalikan list kosong jika user tidak login
            return
        }
        wishlistRef.get().addOnSuccessListener { snapshot ->
            val items = snapshot.children.mapNotNull {
                // Menangani kemungkinan bahwa data yang diambil tidak sesuai tipe
                try {
                    it.getValue(WishlistItem::class.java)
                } catch (e: Exception) {
                    // Menangani error konversi jika ada
                    null
                }
            }
            onComplete(items)  // Mengembalikan list item wishlist
        }
    }


    // Mengupdate progress item wishlist
    fun updateWishlistProgress(id: String, progress: Int, onComplete: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(false)  // Mengembalikan false jika user tidak login
            return
        }
        wishlistRef.child(id).child("progress").setValue(progress)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)  // Menyelesaikan callback dengan status sukses
            }
    }

    // Mengambil progress item wishlist berdasarkan ID
    fun getWishlistItemProgress(id: String, onComplete: (Int) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(0)  // Mengembalikan 0 jika tidak ada user yang login
            return
        }
        wishlistRef.child(id).child("progress").get().addOnSuccessListener { snapshot ->
            val progress = snapshot.getValue(Int::class.java) ?: 0  // Mengambil nilai progress atau 0 jika null
            onComplete(progress)  // Menyelesaikan callback dengan nilai progress
        }
    }

    // Fungsi tambahan untuk meng get item berdasarkan ID
    fun getWishlistItemById(id: String, onComplete: (WishlistItem?) -> Unit) {
        if (userId.isEmpty()) {
            onComplete(null)  // Mengembalikan null jika user tidak login
            return
        }
        wishlistRef.child(id).get().addOnSuccessListener { snapshot ->
            val item = snapshot.getValue(WishlistItem::class.java)  // Mengambil item berdasarkan ID
            onComplete(item)  // Menyelesaikan callback dengan item
        }
    }
}
