package com.example.projeklabmobile

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var wishlistAdapter: ArrayAdapter<String>
    private lateinit var listViewWishlist: ListView
    private lateinit var editTextNamaBarang: EditText
    private lateinit var editTextHargaBarang: EditText
    private lateinit var editTextTarget: EditText
    private lateinit var buttonAdd: Button
    private lateinit var spinnerWaktu: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper()
        listViewWishlist = findViewById(R.id.listViewWishlist)
        editTextNamaBarang = findViewById(R.id.editTextNamaBarang)
        editTextHargaBarang = findViewById(R.id.editTextHargaBarang)
        editTextTarget = findViewById(R.id.editTextTarget)
        buttonAdd = findViewById(R.id.buttonAdd)
        spinnerWaktu = findViewById(R.id.spinnerWaktu)

        wishlistAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewWishlist.adapter = wishlistAdapter

        buttonAdd.setOnClickListener {
            val namaBarang = editTextNamaBarang.text.toString()
            val hargaBarang = editTextHargaBarang.text.toString().toDoubleOrNull()
            val targetWaktu = editTextTarget.text.toString().toDoubleOrNull()

            if (namaBarang.isNotEmpty() && hargaBarang != null && targetWaktu != null) {
                val wishlistItem = WishlistItem(
                    id = db.database.reference.push().key ?: "",
                    namaBarang = namaBarang,
                    hargaBarang = hargaBarang,
                    targetWaktu = targetWaktu
                )
                db.addWishlistItem(wishlistItem) { success ->
                    if (success) {
                        updateWishlist()
                    }
                }
            }
        }

        listViewWishlist.setOnItemClickListener { _, _, position, _ ->
            val item = listViewWishlist.getItemAtPosition(position) as String
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("item_name", item)
            }
            startActivity(intent)
        }

        spinnerWaktu.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Minggu", "Bulan"))

        updateWishlist()
    }

    private fun updateWishlist() {
        db.getAllWishlistItems { items ->
            val names = items.map { "${it.namaBarang}: Rp${it.hargaBarang}" }
            wishlistAdapter.clear()
            wishlistAdapter.addAll(names)
        }
    }
}