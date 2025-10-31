package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a book in the database.
 * 
 * @param id Unique identifier for the book (auto-generated)
 * @param title Title of the book (required)
 * @param author Author of the book (optional)
 * @param rating Rating from 1-5 stars (null if not yet rated, used for read books)
 * @param isRead Whether the book has been read (true) or is in reading list (false)
 * @param createdAt Timestamp when the book was added to the database
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String? = null,
    val rating: Int? = null, // 1-5 stars, null for unrated books
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

