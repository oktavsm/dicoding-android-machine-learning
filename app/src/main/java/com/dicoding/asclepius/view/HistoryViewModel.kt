package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.database.PredictionDatabase
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import com.dicoding.asclepius.data.repository.PredictionRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository

    val history: LiveData<List<PredictionHistory>>

    init {
        val dao = PredictionDatabase.getInstance(application).predictionHistoryDao()
        repository = PredictionRepository(dao)
        history = repository.getAllHistory().asLiveData()
    }

    fun delete(item: PredictionHistory) {
        viewModelScope.launch {
            repository.deleteHistory(item)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
