package com.dicoding.asclepius.view

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.database.PredictionDatabase
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import com.dicoding.asclepius.data.repository.PredictionRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        val dao = PredictionDatabase.getInstance(application).predictionHistoryDao()
        repository = PredictionRepository(dao)
    }

    fun setImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun saveHistory(history: PredictionHistory) {
        viewModelScope.launch {
            repository.insertHistory(history)
        }
    }
}
