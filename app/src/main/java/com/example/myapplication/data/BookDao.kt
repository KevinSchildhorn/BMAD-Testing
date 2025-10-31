package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Book entity.
 * Provides methods to interact with the books table in the database.
 */
@Dao
interface BookDao {
    /**
     * Observe all books in the database as a Flow.
     * Emits a new list whenever any book changes.
     */
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    /**
     * Observe all books in the reading list (not read yet).
     * Emits a new list whenever any reading list book changes.
     */
    @Query("SELECT * FROM books WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getReadingList(): Flow<List<Book>>

    /**
     * Observe all read books.
     * Emits a new list whenever any read book changes.
     */
    @Query("SELECT * FROM books WHERE isRead = 1 ORDER BY createdAt DESC")
    fun getReadBooks(): Flow<List<Book>>

    /**
     * Get a single book by ID.
     * Returns a Flow that emits the book when it changes.
     */
    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Long): Flow<Book?>

    /**
     * Insert a new book into the database.
     * @param book The book to insert
     * @return The ID of the inserted book
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBook(book: Book): Long

    /**
     * Insert multiple books into the database.
     * @param books The books to insert
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBooks(books: List<Book>)

    /**
     * Update an existing book in the database.
     * @param book The book to update
     */
    @Update
    suspend fun updateBook(book: Book)

    /**
     * Delete a book from the database.
     * @param book The book to delete
     */
    @Delete
    suspend fun deleteBook(book: Book)

    /**
     * Delete a book by ID.
     * @param id The ID of the book to delete
     */
    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: Long)

    /**
     * Get count of books in reading list.
     */
    @Query("SELECT COUNT(*) FROM books WHERE isRead = 0")
    fun getReadingListCount(): Flow<Int>

    /**
     * Get count of read books.
     */
    @Query("SELECT COUNT(*) FROM books WHERE isRead = 1")
    fun getReadBooksCount(): Flow<Int>
}

