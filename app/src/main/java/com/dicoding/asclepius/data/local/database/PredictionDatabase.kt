package com.dicoding.asclepius.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.dao.PredictionHistoryDao
import com.dicoding.asclepius.data.local.entity.PredictionHistory

@Database(entities = [PredictionHistory::class], version = 1, exportSchema = false)
abstract class PredictionDatabase : RoomDatabase() {

    abstract fun predictionHistoryDao(): PredictionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: PredictionDatabase? = null

        fun getInstance(context: Context): PredictionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PredictionDatabase::class.java,
                    "prediction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
