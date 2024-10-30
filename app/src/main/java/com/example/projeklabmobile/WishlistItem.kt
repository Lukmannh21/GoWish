package com.example.projeklabmobile

data class WishlistItem(
    val id: String = "",
    val namaBarang: String = "",
    val hargaBarang: Double = 0.0,
    val targetWaktu: Double = 0.0,
    val progress: Int = 0 // buat persenan progresnya ygy
)