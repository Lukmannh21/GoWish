package com.example.projeklabmobile

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var wishlistAdapter: ArrayAdapter<String>
    private lateinit var listViewWishlist: ListView
    private lateinit var editTextNamaBarang: EditText
    private lateinit var editTextHargaBarang: EditText
    private lateinit var buttonAdd: Button
    private lateinit var textViewTotal: TextView
    private lateinit var spinnerWaktu: Spinner
    private lateinit var editTextTarget: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)
        listViewWishlist = findViewById(R.id.listViewWishlist)
        editTextNamaBarang = findViewById(R.id.editTextNamaBarang)
        editTextHargaBarang = findViewById(R.id.editTextHargaBarang)
        buttonAdd = findViewById(R.id.buttonAdd)
        textViewTotal = findViewById(R.id.textViewTotal)
        spinnerWaktu = findViewById(R.id.spinnerWaktu)
        editTextTarget = findViewById(R.id.editTextTarget)

        wishlistAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewWishlist.adapter = wishlistAdapter

        buttonAdd.setOnClickListener {
            val namaBarang = editTextNamaBarang.text.toString()
            val hargaBarang = editTextHargaBarang.text.toString().toDoubleOrNull()

            if (namaBarang.isNotEmpty() && hargaBarang != null) {
                val wishlistItem = WishlistItem(namaBarang = namaBarang, hargaBarang = hargaBarang)
                db.addWishlistItem(wishlistItem)
                updateWishlist()
            }
        }

        spinnerWaktu.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Minggu", "Bulan"))

        updateWishlist()
    }

    private fun updateWishlist() {
        val items = db.getAllWishlistItems()
        val names = items.map { "${it.namaBarang}: Rp${it.hargaBarang}" }
        wishlistAdapter.clear()
        wishlistAdapter.addAll(names)

        val totalHarga = items.sumOf { it.hargaBarang }
        val targetWaktu = editTextTarget.text.toString().toDoubleOrNull() ?: 1.0
        val perWaktu = if (spinnerWaktu.selectedItem == "Minggu") {
            totalHarga / (targetWaktu * 4)
        } else {
            totalHarga / targetWaktu
        }

        textViewTotal.text = "Total Tabungan: Rp${totalHarga} \nTabung per ${spinnerWaktu.selectedItem}: Rp${"%.2f".format(perWaktu)}"
    }
}