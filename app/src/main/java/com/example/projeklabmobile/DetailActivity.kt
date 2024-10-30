package com.example.projeklabmobile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {
    private lateinit var db: FirebaseDatabase
    private lateinit var namaBarangTextView: TextView
    private lateinit var hargaBarangTextView: TextView
    private lateinit var targetWaktuTextView: TextView
    private lateinit var totalTabunganTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi komponen UI
        namaBarangTextView = findViewById(R.id.NamaBarang)
        hargaBarangTextView = findViewById(R.id.namaBarang)
        targetWaktuTextView = findViewById(R.id.targetWaktu)
        totalTabunganTextView = findViewById(R.id.totalTabungan)
        progressTextView = findViewById(R.id.Progress)
        imageView = findViewById(R.id.imageView)

        // Inisialisasi Firebase database
        db = FirebaseDatabase.getInstance()

        // Mendapatkan item_name dari Intent
        val itemName = intent.getStringExtra("item_name") ?: return

        // Panggil fungsi untuk memuat data berdasarkan nama item
        loadWishlistItemByName(itemName)
    }

    private fun loadWishlistItemByName(name: String) {
        val wishlistRef = db.getReference("wishlist")

        // Query untuk mendapatkan item berdasarkan nama
        wishlistRef.orderByChild("namaBarang").equalTo(name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (itemSnapshot in snapshot.children) {
                            val item = itemSnapshot.getValue(WishlistItem::class.java)
                            if (item != null) {
                                // Update komponen UI dengan data item
                                namaBarangTextView.text = item.namaBarang
                                hargaBarangTextView.text = "Rp${item.hargaBarang}"
                                targetWaktuTextView.text = "${item.targetWaktu} hari"
                                totalTabunganTextView.text = "Rp${item.totalTabungan}"
                                progressTextView.text = "Progress: ${item.progress}%"

                                // Mengatur gambar (contoh placeholder)
                                imageView.setImageResource(R.drawable.ic_launcher_background) // Ubah sesuai drawable yang ada
                            }
                        }
                    } else {
                        Toast.makeText(this@DetailActivity, "Item tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DetailActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
