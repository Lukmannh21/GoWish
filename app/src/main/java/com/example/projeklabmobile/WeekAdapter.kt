package com.example.projeklabmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projeklabmobile.DatabaseHelper
import com.example.projeklabmobile.R

class WeekAdapter(
    private val weeks: List<String>,
    private val itemId: String,  // ID item wishlist yang sedang diproses
    private val onProgressChange: (Int) -> Unit
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    // Menyimpan status progress per minggu dalam bentuk set
    private val checkedWeeks = mutableSetOf<Int>()
    private val db = DatabaseHelper()

    init {
        // Load data progress dari Firebase ketika adapter pertama kali dibuat
        loadProgress()
    }

    // Memuat status progress dari Firebase
    private fun loadProgress() {
        db.getWishlistItemProgress(itemId) { progress ->
            // Menghitung jumlah minggu yang dicentang berdasarkan progress yang disimpan
            val totalChecked = (progress * weeks.size) / 100
            // Menyimpan status progress per minggu dalam bentuk set
            checkedWeeks.clear()
            for (i in 0 until totalChecked) {
                checkedWeeks.add(i)
            }
            // Memperbarui UI setelah data berhasil dimuat
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val week = weeks[position]
        holder.textViewWeek.text = week

        // Menghapus listener untuk menghindari update yang tidak perlu
        holder.checkBoxWeek.setOnCheckedChangeListener(null)

        // Set checkbox status berdasarkan apakah minggu ini sudah dicentang sebelumnya
        holder.checkBoxWeek.isChecked = checkedWeeks.contains(position)

        // Listener untuk menangani perubahan status checkbox
        holder.checkBoxWeek.setOnCheckedChangeListener { _, isChecked ->
            // Update set berdasarkan apakah checkbox dicentang atau tidak
            if (isChecked) {
                checkedWeeks.add(position)
            } else {
                checkedWeeks.remove(position)
            }

            // Hitung progress berdasarkan jumlah minggu yang dicentang
            val progress = (checkedWeeks.size * 100) / weeks.size

            // Panggil callback untuk memperbarui UI di DetailActivity
            onProgressChange(progress)

            // Simpan progress terbaru ke Firebase
            db.updateWishlistProgress(itemId, progress) { success ->
                if (!success) {
                    // Tampilkan pesan error jika gagal menyimpan progress
                    Toast.makeText(holder.itemView.context, "Gagal menyimpan progress", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = weeks.size

    class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWeek: TextView = itemView.findViewById(R.id.textViewWeek)
        val checkBoxWeek: CheckBox = itemView.findViewById(R.id.checkBoxWeek)
    }
}
