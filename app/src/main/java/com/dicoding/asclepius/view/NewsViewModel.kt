package com.dicoding.asclepius.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.api.ApiConfig
import com.dicoding.asclepius.data.remote.response.Article
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchHealthNews()
    }

    fun fetchHealthNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiConfig.apiService.getHealthNews(
                    apiKey = BuildConfig.NEWS_API_KEY
                )
                // Filter out articles with null titles or removed content
                val validArticles = response.articles.filter {
                    it.title != null && it.title != "[Removed]"
                }
                _articles.value = validArticles
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
