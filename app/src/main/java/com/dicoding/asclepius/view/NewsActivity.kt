package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: NewsAdapter
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.newsToolbar)
        binding.newsToolbar.setNavigationOnClickListener { finish() }

        adapter = NewsAdapter()
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.newsRecyclerView.adapter = adapter

        binding.btnRetry.setOnClickListener { viewModel.fetchHealthNews() }

        viewModel.isLoading.observe(this) { loading ->
            binding.loadingState.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) {
                binding.newsRecyclerView.visibility = View.GONE
                binding.errorState.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                binding.errorState.visibility = View.VISIBLE
                binding.newsRecyclerView.visibility = View.GONE
                binding.loadingState.visibility = View.GONE
            } else {
                binding.errorState.visibility = View.GONE
            }
        }

        viewModel.articles.observe(this) { articles ->
            adapter.submitList(articles)
            binding.newsRecyclerView.visibility = if (articles.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
}
