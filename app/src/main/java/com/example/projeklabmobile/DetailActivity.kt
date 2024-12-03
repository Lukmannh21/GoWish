package com.example.projeklabmobile

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class DetailActivity : AppCompatActivity() {
    private lateinit var textViewNamaBarang: TextView
    private lateinit var textViewHargaBarang: TextView
    private lateinit var textViewTargetWaktu: TextView
    private lateinit var textViewProgress: TextView
    private lateinit var recyclerViewWeeks: RecyclerView
    private var targetWeeks: Int = 0
    private lateinit var itemId: String  // Menyimpan ID item wishlist

    private val db = DatabaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        textViewNamaBarang = findViewById(R.id.NamaBarang)
        textViewHargaBarang = findViewById(R.id.hargaBarang)
        textViewTargetWaktu = findViewById(R.id.targetWaktu)
        textViewProgress = findViewById(R.id.Progress)
        recyclerViewWeeks = findViewById(R.id.recyclerViewWeeks)

        val namaBarang = intent.getStringExtra("item_name") ?: ""
        val hargaBarang = intent.getDoubleExtra("item_price", 0.0)
        val targetWaktu = intent.getIntExtra("item_target", 0)
        itemId = intent.getStringExtra("item_id") ?: "" // Ambil ID item dari Intent

        textViewNamaBarang.text = namaBarang
        textViewHargaBarang.text = "Rp $hargaBarang"
        textViewTargetWaktu.text = "$targetWaktu Minggu"
        targetWeeks = targetWaktu

        // Atur RecyclerView
        recyclerViewWeeks.layoutManager = LinearLayoutManager(this)
        val weeks = (1..targetWeeks).map { "Minggu $it" }

        // Ambil progress dari Firebase untuk item ini
        db.getWishlistItemProgress(itemId) { progress ->
            // Set initial progress
            textViewProgress.text = "Progress: $progress%"

            // Update progress UI di RecyclerView
            val adapter = WeekAdapter(weeks, itemId) { updatedProgress ->
                textViewProgress.text = "Progress: $updatedProgress%" // Callback untuk update progress
            }
            recyclerViewWeeks.adapter = adapter
        }
    }
}
