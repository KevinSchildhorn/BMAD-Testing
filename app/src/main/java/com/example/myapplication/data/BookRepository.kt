package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository for managing book data.
 * 
 * Provides a clean abstraction layer between the ViewModels and the data source (Room database).
 * Handles all database operations and exposes reactive data streams using Flow.
 * 
 * This repository follows the Repository pattern to:
 * - Abstract data source implementation details
 * - Enable easier testing (can be mocked)
 * - Provide a single source of truth for data operations
 * - Handle data operations using coroutines for async operations
 */
class BookRepository(private val bookDao: BookDao) {

    /**
     * Observe all books in the database.
     * Emits a new list whenever any book changes.
     */
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    /**
     * Observe all books in the reading list (books not yet read).
     * Emits a new list whenever any reading list book changes.
     */
    fun getReadingList(): Flow<List<Book>> = bookDao.getReadingList()

    /**
     * Observe all read books.
     * Emits a new list whenever any read book changes.
     */
    fun getReadBooks(): Flow<List<Book>> = bookDao.getReadBooks()

    /**
     * Observe a single book by ID.
     * Emits the book whenever it changes.
     */
    fun getBookById(id: Long): Flow<Book?> = bookDao.getBookById(id)

    /**
     * Add a book to the reading list.
     * 
     * @param title Book title (required)
     * @param author Book author (optional)
     * @return The ID of the inserted book
     */
    suspend fun addToReadingList(title: String, author: String? = null): Long {
        val book = Book(
            title = title,
            author = author,
            isRead = false,
            rating = null
        )
        return bookDao.insertBook(book)
    }

    /**
     * Add a book directly as read.
     * 
     * @param title Book title (required)
     * @param author Book author (optional)
     * @param rating Star rating from 1-5 (required)
     * @return The ID of the inserted book
     */
    suspend fun addAsRead(title: String, author: String? = null, rating: Int): Long {
        require(rating in 1..5) { "Rating must be between 1 and 5" }
        val book = Book(
            title = title,
            author = author,
            isRead = true,
            rating = rating
        )
        return bookDao.insertBook(book)
    }

    /**
     * Mark a book from reading list as read.
     * 
     * @param bookId The ID of the book to mark as read
     * @param rating Star rating from 1-5 (required)
     */
    suspend fun markAsRead(bookId: Long, rating: Int) {
        require(rating in 1..5) { "Rating must be between 1 and 5" }
        val book = getBookByIdSync(bookId)
            ?: throw IllegalArgumentException("Book with id $bookId not found")
        
        val updatedBook = book.copy(
            isRead = true,
            rating = rating
        )
        bookDao.updateBook(updatedBook)
    }

    /**
     * Update a book's rating.
     * 
     * @param bookId The ID of the book to update
     * @param rating New star rating from 1-5
     */
    suspend fun updateRating(bookId: Long, rating: Int) {
        require(rating in 1..5) { "Rating must be between 1 and 5" }
        val book = getBookByIdSync(bookId)
            ?: throw IllegalArgumentException("Book with id $bookId not found")
        
        val updatedBook = book.copy(rating = rating)
        bookDao.updateBook(updatedBook)
    }

    /**
     * Update a book's information (title and/or author).
     * 
     * @param bookId The ID of the book to update
     * @param title New title (optional, will not update if null)
     * @param author New author (optional, will not update if null)
     */
    suspend fun updateBook(bookId: Long, title: String? = null, author: String? = null) {
        val book = getBookByIdSync(bookId)
            ?: throw IllegalArgumentException("Book with id $bookId not found")
        
        val updatedBook = book.copy(
            title = title ?: book.title,
            author = author ?: book.author
        )
        bookDao.updateBook(updatedBook)
    }

    /**
     * Delete a book from the database.
     * 
     * @param bookId The ID of the book to delete
     */
    suspend fun deleteBook(bookId: Long) {
        bookDao.deleteBookById(bookId)
    }

    /**
     * Get count of books in reading list.
     */
    fun getReadingListCount(): Flow<Int> = bookDao.getReadingListCount()

    /**
     * Get count of read books.
     */
    fun getReadBooksCount(): Flow<Int> = bookDao.getReadBooksCount()

    /**
     * Helper function to get a book synchronously (for updates).
     * Note: This should only be used in suspend functions.
     */
    private suspend fun getBookByIdSync(id: Long): Book? {
        return bookDao.getBookById(id).firstOrNull()
    }
}

