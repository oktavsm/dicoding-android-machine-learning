package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.historyToolbar)
        binding.historyToolbar.setNavigationOnClickListener { finish() }

        adapter = HistoryAdapter { item ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_confirm))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.delete(item)
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = adapter

        viewModel.history.observe(this) { historyList ->
            adapter.submitList(historyList)
            if (historyList.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.historyRecyclerView.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.historyRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}
