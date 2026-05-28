package com.dicoding.asclepius.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val onDeleteClick: (PredictionHistory) -> Unit
) : ListAdapter<PredictionHistory, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PredictionHistory>() {
            override fun areItemsTheSame(old: PredictionHistory, new: PredictionHistory) = old.id == new.id
            override fun areContentsTheSame(old: PredictionHistory, new: PredictionHistory) = old == new
        }
        private val DATE_FORMAT = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PredictionHistory) {
            // Load thumbnail
            binding.historyImageView.load(Uri.parse(item.imageUri)) {
                crossfade(true)
                error(R.drawable.ic_place_holder)
            }

            val isCancer = item.label.contains("Cancer", ignoreCase = true) &&
                    !item.label.contains("Non", ignoreCase = true)

            binding.historyLabel.apply {
                text = item.label
                setTextColor(
                    context.getColor(
                        if (isCancer) R.color.cancer_color else R.color.non_cancer_color
                    )
                )
            }

            binding.historyConfidence.text = String.format("%.1f%%", item.confidenceScore * 100f)
            binding.historyTimestamp.text = DATE_FORMAT.format(Date(item.timestamp))
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
