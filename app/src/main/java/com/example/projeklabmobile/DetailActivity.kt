package com.example.projeklabmobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeklabmobile.adapter.WeekAdapter
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private lateinit var textViewNamaBarang: TextView
    private lateinit var textViewHargaBarang: TextView
    private lateinit var textViewTargetWaktu: TextView
    private lateinit var textViewProgress: TextView
    private lateinit var recyclerViewWeeks: RecyclerView
    private var targetWeeks: Int = 0

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

        textViewNamaBarang.text = namaBarang
        textViewHargaBarang.text = "Rp $hargaBarang"
        textViewTargetWaktu.text = "$targetWaktu Minggu"
        targetWeeks = targetWaktu

        // Atur RecyclerView
        recyclerViewWeeks.layoutManager = LinearLayoutManager(this)
        val weeks = (1..targetWeeks).map { "Minggu $it" }
        val adapter = WeekAdapter(weeks) { progress ->
            textViewProgress.text = "Progress: $progress%"
        }
        recyclerViewWeeks.adapter = adapter
    }
}
