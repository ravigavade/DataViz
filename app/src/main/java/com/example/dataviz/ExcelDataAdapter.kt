package com.example.dataviz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataviz.databinding.ItemRowBinding
//import com.example.simplexlsxviewer.databinding.ItemRowBinding

class ExcelDataAdapter(private val data: List<List<String>>) :
    RecyclerView.Adapter<ExcelDataAdapter.ExcelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcelViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExcelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExcelViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ExcelViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rowData: List<String>) {
            binding.tvRowData.text = rowData.joinToString(" | ")
        }
    }
}