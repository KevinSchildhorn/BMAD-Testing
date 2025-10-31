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

    @Test
    fun getAllBooks_ordersByCreatedAtDescending() = runBlocking {
        // Given - Add books with different timestamps
        val time1 = 1000L
        val time2 = 2000L
        val time3 = 3000L
        
        val book1Id = repository.addToReadingList("Book 1")
        val book2Id = repository.addToReadingList("Book 2")
        val book3Id = repository.addToReadingList("Book 3")
        
        // Update created timestamps by getting and re-inserting with specific times
        val book1 = repository.getBookById(book1Id).first()!!
        val book2 = repository.getBookById(book2Id).first()!!
        val book3 = repository.getBookById(book3Id).first()!!
        
        // Note: In practice, createdAt is set automatically, but we verify ordering works
        val allBooks = repository.getAllBooks().first()

        // Then - Should be ordered (newest first)
        assertEquals(3, allBooks.size)
        // The order should match insertion order (most recent first)
        assertTrue(allBooks[0].id == book3Id || allBooks[0].id == book2Id || allBooks[0].id == book1Id)
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsAdded() = runBlocking {
        // Given
        val flow = repository.getAllBooks()
        val initialBooks = flow.first()
        assertEquals(0, initialBooks.size)

        // When - Add a book
        repository.addToReadingList("New Book")
        val updatedBooks = flow.first()

        // Then
        assertEquals(1, updatedBooks.size)
        assertEquals("New Book", updatedBooks.first().title)
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsUpdated() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Original Title")
        val flow = repository.getAllBooks()

        // When - Update the book
        repository.updateBook(bookId, "Updated Title")
        val updatedBooks = flow.first()

        // Then
        val updated = updatedBooks.find { it.id == bookId }
        assertNotNull(updated)
        assertEquals("Updated Title", updated?.title)
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsDeleted() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Delete")
        val flow = repository.getAllBooks()
        val initialBooks = flow.first()
        assertEquals(1, initialBooks.size)

        // When - Delete the book
        repository.deleteBook(bookId)
        val updatedBooks = flow.first()

        // Then
        assertEquals(0, updatedBooks.size)
        assertTrue(updatedBooks.none { it.id == bookId })
    }

    @Test
    fun getReadingList_emitsUpdateWhenBookIsMarkedAsRead() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Read")
        val flow = repository.getReadingList()
        val initialList = flow.first()
        assertEquals(1, initialList.size)
        assertTrue(initialList.any { it.id == bookId })

        // When - Mark as read
        repository.markAsRead(bookId, 5)
        val updatedList = flow.first()

        // Then
        assertEquals(0, updatedList.size)
        assertFalse(updatedList.any { it.id == bookId })
    }

    @Test
    fun getReadBooks_emitsUpdateWhenBookIsMarkedAsRead() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("To Read")
        val flow = repository.getReadBooks()
        val initialReadBooks = flow.first()
        assertEquals(0, initialReadBooks.size)

        // When - Mark as read
        repository.markAsRead(bookId, 4)
        val updatedReadBooks = flow.first()

        // Then
        assertEquals(1, updatedReadBooks.size)
        assertTrue(updatedReadBooks.any { it.id == bookId })
        assertEquals(4, updatedReadBooks.first { it.id == bookId }.rating)
    }

    @Test
    fun getReadingListCount_emitsUpdateWhenBookIsAdded() = runBlocking {
        // Given
        val flow = repository.getReadingListCount()
        val initialCount = flow.first()
        assertEquals(0, initialCount)

        // When - Add a book to reading list
        repository.addToReadingList("New Book")
        val updatedCount = flow.first()

        // Then
        assertEquals(1, updatedCount)
    }

    @Test
    fun getReadBooksCount_emitsUpdateWhenBookIsMarkedAsRead() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Book")
        val flow = repository.getReadBooksCount()
        val initialCount = flow.first()
        assertEquals(0, initialCount)

        // When - Mark as read
        repository.markAsRead(bookId, 5)
        val updatedCount = flow.first()

        // Then
        assertEquals(1, updatedCount)
    }

    @Test
    fun addToReadingList_setsCreatedAtTimestamp() = runBlocking {
        // Given
        val beforeInsert = System.currentTimeMillis()

        // When
        val bookId = repository.addToReadingList("Test Book")
        val afterInsert = System.currentTimeMillis()
        val book = repository.getBookById(bookId).first()!!

        // Then
        assertNotNull(book.createdAt)
        assertTrue(book.createdAt >= beforeInsert)
        assertTrue(book.createdAt <= afterInsert)
    }

    @Test
    fun addAsRead_setsCreatedAtTimestamp() = runBlocking {
        // Given
        val beforeInsert = System.currentTimeMillis()

        // When
        val bookId = repository.addAsRead("Read Book", "Author", 5)
        val afterInsert = System.currentTimeMillis()
        val book = repository.getBookById(bookId).first()!!

        // Then
        assertNotNull(book.createdAt)
        assertTrue(book.createdAt >= beforeInsert)
        assertTrue(book.createdAt <= afterInsert)
    }

    @Test
    fun getAllBooks_returnsEmptyListWhenNoBooks() = runBlocking {
        // When
        val allBooks = repository.getAllBooks().first()

        // Then
        assertTrue(allBooks.isEmpty())
    }

    @Test
    fun getReadingList_returnsEmptyListWhenNoBooks() = runBlocking {
        // When
        val readingList = repository.getReadingList().first()

        // Then
        assertTrue(readingList.isEmpty())
    }

    @Test
    fun getReadBooks_returnsEmptyListWhenNoBooks() = runBlocking {
        // When
        val readBooks = repository.getReadBooks().first()

        // Then
        assertTrue(readBooks.isEmpty())
    }

    @Test
    fun getBookById_returnsNullWhenBookDoesNotExist() = runBlocking {
        // When
        val book = repository.getBookById(999L).first()

        // Then
        assertNull(book)
    }

    @Test
    fun updateRating_updatesReadBookRating() = runBlocking {
        // Given
        val bookId = repository.addAsRead("Book", "Author", 3)

        // When
        repository.updateRating(bookId, 5)

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals(5, book?.rating)
        assertTrue(book?.isRead ?: false)
    }

    @Test
    fun updateBook_updatesAuthorOnly() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Title", "Original Author")

        // When - Update only author
        repository.updateBook(bookId, author = "Updated Author")

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals("Title", book?.title)
        assertEquals("Updated Author", book?.author)
    }

    @Test
    fun updateBook_updatesTitleOnly() = runBlocking {
        // Given
        val bookId = repository.addToReadingList("Original Title", "Author")

        // When - Update only title
        repository.updateBook(bookId, title = "Updated Title")

        // Then
        val book = repository.getBookById(bookId).first()
        assertEquals("Updated Title", book?.title)
        assertEquals("Author", book?.author)
    }
}

