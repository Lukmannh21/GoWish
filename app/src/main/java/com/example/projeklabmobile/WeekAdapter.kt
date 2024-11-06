package com.example.projeklabmobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeklabmobile.R

class WeekAdapter(
    private val weeks: List<String>,
    private val onProgressChange: (Int) -> Unit
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private val checkedWeeks = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val week = weeks[position]
        holder.textViewWeek.text = week

        holder.checkBoxWeek.setOnCheckedChangeListener(null)
        holder.checkBoxWeek.isChecked = checkedWeeks.contains(position)

        holder.checkBoxWeek.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedWeeks.add(position)
            } else {
                checkedWeeks.remove(position)
            }
            val progress = (checkedWeeks.size * 100) / weeks.size
            onProgressChange(progress)
        }
    }

    override fun getItemCount(): Int = weeks.size

    class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWeek: TextView = itemView.findViewById(R.id.textViewWeek)
        val checkBoxWeek: CheckBox = itemView.findViewById(R.id.checkBoxWeek)
    }
}
