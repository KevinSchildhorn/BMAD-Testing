package com.example.myapplication.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for BookDao.
 * Tests all CRUD operations and Flow queries.
 */
@RunWith(AndroidJUnit4::class)
class BookDaoTest {
    private lateinit var database: BookDatabase
    private lateinit var bookDao: BookDao

    @Before
    fun setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        bookDao = database.bookDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertBook_returnsCorrectId() = runBlocking {
        // Given
        val book = Book(
            title = "Test Book",
            author = "Test Author",
            isRead = false
        )

        // When
        val insertedId = bookDao.insertBook(book)

        // Then
        assertTrue(insertedId > 0)
    }

    @Test
    fun getBookById_returnsCorrectBook() = runBlocking {
        // Given
        val book = Book(
            title = "Test Book",
            author = "Test Author",
            isRead = false
        )
        val insertedId = bookDao.insertBook(book)

        // When
        val retrievedBook = bookDao.getBookById(insertedId).first()

        // Then
        assertNotNull(retrievedBook)
        assertEquals(book.title, retrievedBook?.title)
        assertEquals(book.author, retrievedBook?.author)
        assertEquals(book.isRead, retrievedBook?.isRead)
        assertEquals(insertedId, retrievedBook?.id)
    }

    @Test
    fun getAllBooks_returnsAllBooks() = runBlocking {
        // Given
        val book1 = Book(title = "Book 1", isRead = false)
        val book2 = Book(title = "Book 2", isRead = true, rating = 5)
        bookDao.insertBook(book1)
        bookDao.insertBook(book2)

        // When
        val allBooks = bookDao.getAllBooks().first()

        // Then
        assertEquals(2, allBooks.size)
    }

    @Test
    fun getReadingList_returnsOnlyUnreadBooks() = runBlocking {
        // Given
        val readingListBook = Book(title = "To Read", isRead = false)
        val readBook = Book(title = "Read Book", isRead = true, rating = 4)
        bookDao.insertBook(readingListBook)
        bookDao.insertBook(readBook)

        // When
        val readingList = bookDao.getReadingList().first()

        // Then
        assertEquals(1, readingList.size)
        assertEquals("To Read", readingList.first().title)
        assertFalse(readingList.first().isRead)
    }

    @Test
    fun getReadBooks_returnsOnlyReadBooks() = runBlocking {
        // Given
        val readingListBook = Book(title = "To Read", isRead = false)
        val readBook = Book(title = "Read Book", isRead = true, rating = 4)
        bookDao.insertBook(readingListBook)
        bookDao.insertBook(readBook)

        // When
        val readBooks = bookDao.getReadBooks().first()

        // Then
        assertEquals(1, readBooks.size)
        assertEquals("Read Book", readBooks.first().title)
        assertTrue(readBooks.first().isRead)
        assertEquals(4, readBooks.first().rating)
    }

    @Test
    fun updateBook_updatesCorrectly() = runBlocking {
        // Given
        val book = Book(title = "Original Title", author = "Original Author", isRead = false)
        val insertedId = bookDao.insertBook(book)
        val retrievedBook = bookDao.getBookById(insertedId).first()!!

        // When
        val updatedBook = retrievedBook.copy(
            title = "Updated Title",
            author = "Updated Author",
            isRead = true,
            rating = 5
        )
        bookDao.updateBook(updatedBook)

        // Then
        val result = bookDao.getBookById(insertedId).first()
        assertNotNull(result)
        assertEquals("Updated Title", result?.title)
        assertEquals("Updated Author", result?.author)
        assertTrue(result?.isRead == true)
        assertEquals(5, result?.rating)
    }

    @Test
    fun deleteBook_removesBook() = runBlocking {
        // Given
        val book = Book(title = "To Delete", isRead = false)
        val insertedId = bookDao.insertBook(book)

        // When
        val bookToDelete = bookDao.getBookById(insertedId).first()!!
        bookDao.deleteBook(bookToDelete)

        // Then
        val result = bookDao.getBookById(insertedId).first()
        assertNull(result)
    }

    @Test
    fun deleteBookById_removesBook() = runBlocking {
        // Given
        val book = Book(title = "To Delete", isRead = false)
        val insertedId = bookDao.insertBook(book)

        // When
        bookDao.deleteBookById(insertedId)

        // Then
        val result = bookDao.getBookById(insertedId).first()
        assertNull(result)
    }

    @Test
    fun getReadingListCount_returnsCorrectCount() = runBlocking {
        // Given
        bookDao.insertBook(Book(title = "Book 1", isRead = false))
        bookDao.insertBook(Book(title = "Book 2", isRead = false))
        bookDao.insertBook(Book(title = "Book 3", isRead = true, rating = 5))

        // When
        val count = bookDao.getReadingListCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getReadBooksCount_returnsCorrectCount() = runBlocking {
        // Given
        bookDao.insertBook(Book(title = "Book 1", isRead = true, rating = 4))
        bookDao.insertBook(Book(title = "Book 2", isRead = true, rating = 5))
        bookDao.insertBook(Book(title = "Book 3", isRead = false))

        // When
        val count = bookDao.getReadBooksCount().first()

        // Then
        assertEquals(2, count)
    }
}

