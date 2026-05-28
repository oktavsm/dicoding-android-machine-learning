package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.local.dao.PredictionHistoryDao
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import kotlinx.coroutines.flow.Flow

class PredictionRepository(private val dao: PredictionHistoryDao) {

    fun getAllHistory(): Flow<List<PredictionHistory>> = dao.getAllHistory()

    suspend fun insertHistory(history: PredictionHistory) = dao.insert(history)

    suspend fun deleteHistory(history: PredictionHistory) = dao.delete(history)

    suspend fun deleteAll() = dao.deleteAll()
}
