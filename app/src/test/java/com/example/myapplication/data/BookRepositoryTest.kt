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
 * Unit tests for BookRepository.
 * Tests all repository methods and business logic.
 */
@RunWith(AndroidJUnit4::class)
class BookRepositoryTest {
    private lateinit var database: BookDatabase
    private lateinit var repository: BookRepository

    @Before
    fun setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        repository = BookRepository(database.bookDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addToReadingList_addsBookCorrectly() = runBlocking {
        // When
        val bookId = repository.addToReadingList("Test Book", "Test Author")

        // Then
        assertTrue(bookId > 0)
        val book = repository.getBookById(bookId).first()
        assertNotNull(book)
        assertEquals("Test Book", book?.title)
        assertEquals("Test Author", book?.author)
        assertFalse(book?.isRead ?: true)
        assertNull(book?.rating)
    }

    @Test
    fun addToReadingList_withoutAuthor_addsBookCorrectly() = runBlocking {
        // When
        val bookId = repository.addToReadingList("Test Book")

        // Then
        assertTrue(bookId > 0)
        val book = repository.getBookById(bookId).first()
        assertNotNull(book)
        assertEquals("Test Book", book?.title)
        assertNull(book?.author)
        assertFalse(book?.isRead ?: true)
    }

    @Test
    fun addAsRead_addsReadBookCorrectly() = runBlocking {
        // When
        val bookId = repository.addAsRead("Read Book", "Author", 5)

        // Then
        assertTrue(bookId > 0)
        val book = repository.getBookById(bookId).first()
        assertNotNull(book)
        assertEquals("Read Book", book?.title)
        assertEquals("Author", book?.author)
        assertTrue(book?.isRead ?: false)
        assertEquals(5, book?.rating)
    }

    @Test
    fun addAsRead_withInvalidRating_throwsException() = runBlocking {
        // Then
        try {
            repository.addAsRead("Book", "Author", 6)
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Rating must be between 1 and 5") ?: false)
        }
    }

    @Test
    fun markAsRead_marksBookAsRead() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Read Book")

        // When
        repository.markAsRead(bookId, 4)

        // Then
        val book = repository.getBookById(bookId).first()
        assertNotNull(book)
        assertTrue(book?.isRead ?: false)
        assertEquals(4, book?.rating)
        
        // Verify it appears in read books list
        val readBooks = repository.getReadBooks().first()
        assertTrue(readBooks.any { it.id == bookId })
        
        // Verify it no longer appears in reading list
        val readingList = repository.getReadingList().first()
        assertFalse(readingList.any { it.id == bookId })
    }

    @Test
    fun markAsRead_withInvalidRating_throwsException() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Read Book")

        // Then
        try {
            repository.markAsRead(bookId, 0)
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Rating must be between 1 and 5") ?: false)
        }
    }

    @Test
    fun updateRating_updatesRatingCorrectly() = runBlocking {
        // Given
        val bookId = repository.addAsRead("Book", "Author", 3)

        // When
        repository.updateRating(bookId, 5)

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals(5, book?.rating)
    }

    @Test
    fun updateRating_withInvalidRating_throwsException() = runBlocking {
        // Given
        val bookId = repository.addAsRead("Book", "Author", 3)

        // Then
        try {
            repository.updateRating(bookId, 10)
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Rating must be between 1 and 5") ?: false)
        }
    }

    @Test
    fun updateBook_updatesTitleAndAuthor() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Original Title", "Original Author")

        // When
        repository.updateBook(bookId, "Updated Title", "Updated Author")

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals("Updated Title", book?.title)
        assertEquals("Updated Author", book?.author)
    }

    @Test
    fun updateBook_partialUpdate_worksCorrectly() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Original Title", "Original Author")

        // When - update only title
        repository.updateBook(bookId, title = "Updated Title")

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals("Updated Title", book?.title)
        assertEquals("Original Author", book?.author) // Unchanged
    }

    @Test
    fun deleteBook_removesBook() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Delete")

        // When
        repository.deleteBook(bookId)

        // Then
        val book = repository.getBookById(bookId).first()
        assertNull(book)
    }

    @Test
    fun getReadingListCount_returnsCorrectCount() = runBlocking {
        // Given
        repository.addToReadingList("Book 1")
        repository.addToReadingList("Book 2")
        repository.addAsRead("Read Book", rating = 5)

        // When
        val count = repository.getReadingListCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getReadBooksCount_returnsCorrectCount() = runBlocking {
        // Given
        repository.addAsRead("Book 1", rating = 4)
        repository.addAsRead("Book 2", rating = 5)
        repository.addToReadingList("Unread Book")

        // When
        val count = repository.getReadBooksCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getAllBooks_returnsAllBooks() = runBlocking {
        // Given
        repository.addToReadingList("Book 1")
        repository.addAsRead("Book 2", rating = 5)

        // When
        val allBooks = repository.getAllBooks().first()

        // Then
        assertEquals(2, allBooks.size)
    }

    @Test
    fun markAsRead_nonexistentBook_throwsException() = runBlocking {
        // Then
        try {
            repository.markAsRead(999L, 5)
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("not found") ?: false)
        }
    }

    @Test
    fun updateRating_nonexistentBook_throwsException() = runBlocking {
        // Then
        try {
            repository.updateRating(999L, 5)
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("not found") ?: false)
        }
    }

    @Test
    fun updateBook_nonexistentBook_throwsException() = runBlocking {
        // Then
        try {
            repository.updateBook(999L, "Title")
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("not found") ?: false)
        }
    }
}

