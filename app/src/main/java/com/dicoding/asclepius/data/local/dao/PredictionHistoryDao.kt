package com.dicoding.asclepius.data.local.dao

import androidx.room.*
import com.dicoding.asclepius.data.local.entity.PredictionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: PredictionHistory)

    @Query("SELECT * FROM prediction_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<PredictionHistory>>

    @Delete
    suspend fun delete(history: PredictionHistory)

    @Query("DELETE FROM prediction_history")
    suspend fun deleteAll()
}
