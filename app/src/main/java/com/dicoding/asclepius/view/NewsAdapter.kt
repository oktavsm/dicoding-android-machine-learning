package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.Article
import com.dicoding.asclepius.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : ListAdapter<Article, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(old: Article, new: Article) = old.url == new.url
            override fun areContentsTheSame(old: Article, new: Article) = old == new
        }
        private val INPUT_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        private val OUTPUT_FORMAT = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.newsTitle.text = article.title ?: "No Title"
            binding.newsDescription.text = article.description ?: "No description available."
            binding.newsSource.text = article.source.name ?: "Unknown Source"

            // Format date
            binding.newsDate.text = try {
                val date = INPUT_FORMAT.parse(article.publishedAt ?: "")
                if (date != null) OUTPUT_FORMAT.format(date) else article.publishedAt ?: ""
            } catch (e: Exception) {
                article.publishedAt ?: ""
            }

            // Load thumbnail
            if (!article.urlToImage.isNullOrBlank()) {
                binding.newsImage.load(article.urlToImage) {
                    crossfade(true)
                    error(R.drawable.ic_place_holder)
                    placeholder(R.drawable.ic_place_holder)
                }
            } else {
                binding.newsImage.setImageResource(R.drawable.ic_place_holder)
            }

            // Open article in browser
            binding.btnReadMore.setOnClickListener {
                article.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    binding.root.context.startActivity(intent)
                }
            }

            binding.root.setOnClickListener {
                article.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
