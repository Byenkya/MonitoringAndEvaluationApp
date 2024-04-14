package com.example.monitoringandevaluationapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LocationEntity::class, SavedAssessmentEntity::class, GroupEntity::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    abstract fun savedAssessmentDao(): SavedAssessmentDao

    abstract fun groupDao(): GroupDao

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
                        )
//                       .addMigrations(MIGRATION_1_2)
                       .fallbackToDestructiveMigration() // Add this line for destructive migration
                       .build()

                        Log.d("AppDatabase", "Database instance: $instance")
                        INSTANCE = instance
                        instance
                    } catch (e: Exception) {
                        Log.e("AppDatabase", "Database creation failed", e)
                        throw RuntimeException("Database initialization failed: ${e.message}")
                    }
            }
        }

        // Define migration from version 1 to version 2
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic goes here
                // You need to write SQL statements to migrate data if necessary
                // Example: database.execSQL("ALTER TABLE tableName ADD COLUMN newColumn TEXT")
            }
        }
    }
}
