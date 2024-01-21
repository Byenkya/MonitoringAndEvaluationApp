package com.example.monitoringandevaluationapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocationEntity::class, SavedAssessmentEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    abstract fun savedAssessmentDao(): SavedAssessmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            Log.d("AppDatabase", "Getting the database instance")
            return INSTANCE ?: synchronized(this) {
                try {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()

                    Log.d("AppDatabase", "Database instance: $instance")
                    INSTANCE = instance
                    instance
                    } catch (e: Exception) {
                        Log.e("AppDatabase", "Database creation failed", e)
                        throw RuntimeException("Database initialization failed: ${e.message}")
                    }
            }
        }
    }
}
