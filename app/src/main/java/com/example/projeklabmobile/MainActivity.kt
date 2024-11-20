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
            val targetWaktu = editTextTarget.text.toString().toIntOrNull()

            if (namaBarang.isNotEmpty() && hargaBarang != null && targetWaktu != null) {
                val wishlistItem = WishlistItem(
                    id = "", // Biarkan kosong, Firebase akan otomatis menghasilkan key
                    namaBarang = namaBarang,
                    hargaBarang = hargaBarang,
                    targetWaktu = targetWaktu
                )
                db.addWishlistItem(wishlistItem) { success ->
                    if (success) {
                        updateWishlist()
                        Toast.makeText(this, "Item berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Gagal menambahkan item", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Harap isi semua kolom dengan benar", Toast.LENGTH_SHORT).show()
            }
        }


        listViewWishlist.setOnItemClickListener { _, _, position, _ ->
            db.getAllWishlistItems { items ->
                val item = items[position]
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("item_name", item.namaBarang)
                    putExtra("item_price", item.hargaBarang)
                    putExtra("item_target", item.targetWaktu)
                }
                startActivity(intent)
            }
        }

        spinnerWaktu.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Minggu", "Bulan"))

        updateWishlist()
    }

    private fun updateWishlist() {
        db.getAllWishlistItems { items ->
            val names = items.map { "${it.namaBarang}: Rp${it.hargaBarang}" }
            runOnUiThread {
                wishlistAdapter.clear()
                wishlistAdapter.addAll(names)
            }
        }
    }

}
