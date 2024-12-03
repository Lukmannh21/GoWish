package com.example.projeklabmobile

data class WishlistItem(
    var id: String = "",  // Changed to 'var' to allow reassignment
    var namaBarang: String = "",
    var hargaBarang: Double = 0.0,
    var targetWaktu: Int = 0, // Dalam minggu atau bulan, bisa disesuaikan
    var progress: Int = 0 // Progress checklist untuk item ini (0-100)
)
