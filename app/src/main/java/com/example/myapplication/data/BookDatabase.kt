package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for the Book Tracking App.
 * 
 * This database stores books in a single table with the following schema:
 * - id: Primary key (auto-generated)
 * - title: Book title (required)
 * - author: Book author (optional)
 * - rating: Star rating 1-5 (nullable)
 * - isRead: Boolean indicating if book is read (false = reading list, true = read)
 * - createdAt: Timestamp when book was added
 * 
 * Version: 1 - Initial database schema
 */
@Database(
    entities = [Book::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        /**
         * Get the database instance.
         * Creates the database if it doesn't exist using singleton pattern.
         * 
         * @param context Application context
         * @return The database instance
         */
        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

