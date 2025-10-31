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
            isRead = false,
            createdAt = System.currentTimeMillis()
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
            isRead = false,
            createdAt = System.currentTimeMillis()
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
        val book1 = Book(title = "Book 1", isRead = false, createdAt = System.currentTimeMillis())
        val book2 = Book(title = "Book 2", isRead = true, rating = 5, createdAt = System.currentTimeMillis())
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
        val readingListBook = Book(title = "To Read", isRead = false, createdAt = System.currentTimeMillis())
        val readBook = Book(title = "Read Book", isRead = true, rating = 4, createdAt = System.currentTimeMillis())
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
        val readingListBook = Book(title = "To Read", isRead = false, createdAt = System.currentTimeMillis())
        val readBook = Book(title = "Read Book", isRead = true, rating = 4, createdAt = System.currentTimeMillis())
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
        val book = Book(title = "Original Title", author = "Original Author", isRead = false, createdAt = System.currentTimeMillis())
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
        val book = Book(title = "To Delete", isRead = false, createdAt = System.currentTimeMillis())
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
        val book = Book(title = "To Delete", isRead = false, createdAt = System.currentTimeMillis())
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
        bookDao.insertBook(Book(title = "Book 1", isRead = false, createdAt = System.currentTimeMillis()))
        bookDao.insertBook(Book(title = "Book 2", isRead = false, createdAt = System.currentTimeMillis()))
        bookDao.insertBook(Book(title = "Book 3", isRead = true, rating = 5, createdAt = System.currentTimeMillis()))

        // When
        val count = bookDao.getReadingListCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getReadBooksCount_returnsCorrectCount() = runBlocking {
        // Given
        bookDao.insertBook(Book(title = "Book 1", isRead = true, rating = 4, createdAt = System.currentTimeMillis()))
        bookDao.insertBook(Book(title = "Book 2", isRead = true, rating = 5, createdAt = System.currentTimeMillis()))
        bookDao.insertBook(Book(title = "Book 3", isRead = false, createdAt = System.currentTimeMillis()))

        // When
        val count = bookDao.getReadBooksCount().first()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getAllBooks_ordersByCreatedAtDescending() = runBlocking {
        // Given - Insert books with different timestamps
        val time1 = 1000L
        val time2 = 2000L
        val time3 = 3000L
        
        val book1 = Book(title = "Book 1", createdAt = time1, isRead = false)
        val book2 = Book(title = "Book 2", createdAt = time2, isRead = false)
        val book3 = Book(title = "Book 3", createdAt = time3, isRead = false)
        
        bookDao.insertBook(book1)
        bookDao.insertBook(book2)
        bookDao.insertBook(book3)

        // When
        val allBooks = bookDao.getAllBooks().first()

        // Then - Should be ordered by createdAt DESC (newest first)
        assertEquals(3, allBooks.size)
        assertEquals("Book 3", allBooks[0].title) // Most recent
        assertEquals("Book 2", allBooks[1].title)
        assertEquals("Book 1", allBooks[2].title) // Oldest
    }

    @Test
    fun getReadingList_ordersByCreatedAtDescending() = runBlocking {
        // Given
        val time1 = 1000L
        val time2 = 2000L
        
        val book1 = Book(title = "Older Book", createdAt = time1, isRead = false)
        val book2 = Book(title = "Newer Book", createdAt = time2, isRead = false)
        
        bookDao.insertBook(book1)
        bookDao.insertBook(book2)

        // When
        val readingList = bookDao.getReadingList().first()

        // Then
        assertEquals(2, readingList.size)
        assertEquals("Newer Book", readingList[0].title)
        assertEquals("Older Book", readingList[1].title)
    }

    @Test
    fun getReadBooks_ordersByCreatedAtDescending() = runBlocking {
        // Given
        val time1 = 1000L
        val time2 = 2000L
        
        val book1 = Book(title = "Older Read", createdAt = time1, isRead = true, rating = 4)
        val book2 = Book(title = "Newer Read", createdAt = time2, isRead = true, rating = 5)
        
        bookDao.insertBook(book1)
        bookDao.insertBook(book2)

        // When
        val readBooks = bookDao.getReadBooks().first()

        // Then
        assertEquals(2, readBooks.size)
        assertEquals("Newer Read", readBooks[0].title)
        assertEquals("Older Read", readBooks[1].title)
    }

    @Test
    fun insertBooks_bulkInsertsMultipleBooks() = runBlocking {
        // Given
        val books = listOf(
            Book(title = "Book 1", isRead = false, createdAt = System.currentTimeMillis()),
            Book(title = "Book 2", isRead = false, createdAt = System.currentTimeMillis()),
            Book(title = "Book 3", isRead = true, rating = 5, createdAt = System.currentTimeMillis())
        )

        // When
        bookDao.insertBooks(books)

        // Then
        val allBooks = bookDao.getAllBooks().first()
        assertEquals(3, allBooks.size)
        assertTrue(allBooks.any { it.title == "Book 1" })
        assertTrue(allBooks.any { it.title == "Book 2" })
        assertTrue(allBooks.any { it.title == "Book 3" })
    }

    @Test
    fun insertBook_setsCreatedAtTimestamp() = runBlocking {
        // Given
        val beforeInsert = System.currentTimeMillis()
        val book = Book(title = "Test Book", isRead = false, createdAt = System.currentTimeMillis())

        // When
        val insertedId = bookDao.insertBook(book)
        val afterInsert = System.currentTimeMillis()
        val retrievedBook = bookDao.getBookById(insertedId).first()!!

        // Then
        assertNotNull(retrievedBook.createdAt)
        assertTrue(retrievedBook.createdAt >= beforeInsert)
        assertTrue(retrievedBook.createdAt <= afterInsert)
    }

    @Test
    fun insertBook_withNullAuthor_savesCorrectly() = runBlocking {
        // Given
        val book = Book(
            title = "Book Without Author",
            author = null,
            isRead = false,
            createdAt = System.currentTimeMillis()
        )

        // When
        val insertedId = bookDao.insertBook(book)
        val retrievedBook = bookDao.getBookById(insertedId).first()!!

        // Then
        assertEquals("Book Without Author", retrievedBook.title)
        assertNull(retrievedBook.author)
        assertFalse(retrievedBook.isRead)
    }

    @Test
    fun insertBook_withNullRating_savesCorrectly() = runBlocking {
        // Given
        val book = Book(
            title = "Book Without Rating",
            author = "Author",
            isRead = false,
            rating = null,
            createdAt = System.currentTimeMillis()
        )

        // When
        val insertedId = bookDao.insertBook(book)
        val retrievedBook = bookDao.getBookById(insertedId).first()!!

        // Then
        assertEquals("Book Without Rating", retrievedBook.title)
        assertNull(retrievedBook.rating)
        assertFalse(retrievedBook.isRead)
    }

    @Test
    fun getAllBooks_emptyDatabase_returnsEmptyList() = runBlocking {
        // When
        val allBooks = bookDao.getAllBooks().first()

        // Then
        assertTrue(allBooks.isEmpty())
    }

    @Test
    fun getReadingList_emptyDatabase_returnsEmptyList() = runBlocking {
        // When
        val readingList = bookDao.getReadingList().first()

        // Then
        assertTrue(readingList.isEmpty())
    }

    @Test
    fun getReadBooks_emptyDatabase_returnsEmptyList() = runBlocking {
        // When
        val readBooks = bookDao.getReadBooks().first()

        // Then
        assertTrue(readBooks.isEmpty())
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsAdded() = runBlocking {
        // Given
        val flow = bookDao.getAllBooks()
        val initialBooks = flow.first()
        assertEquals(0, initialBooks.size)

        // When - Insert a book
        bookDao.insertBook(Book(title = "New Book", isRead = false, createdAt = System.currentTimeMillis()))
        val updatedBooks = flow.first()

        // Then
        assertEquals(1, updatedBooks.size)
        assertEquals("New Book", updatedBooks.first().title)
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsUpdated() = runBlocking {
        // Given
        val book = Book(title = "Original Title", isRead = false, createdAt = System.currentTimeMillis())
        val insertedId = bookDao.insertBook(book)
        val flow = bookDao.getAllBooks()

        // When - Update the book
        val retrievedBook = bookDao.getBookById(insertedId).first()!!
        val updatedBook = retrievedBook.copy(title = "Updated Title", isRead = true, rating = 5)
        bookDao.updateBook(updatedBook)
        val updatedBooks = flow.first()

        // Then
        val updated = updatedBooks.find { it.id == insertedId }
        assertNotNull(updated)
        assertEquals("Updated Title", updated?.title)
        assertTrue(updated?.isRead == true)
        assertEquals(5, updated?.rating)
    }

    @Test
    fun getAllBooks_emitsUpdateWhenBookIsDeleted() = runBlocking {
        // Given
        val book = Book(title = "To Delete", isRead = false, createdAt = System.currentTimeMillis())
        val insertedId = bookDao.insertBook(book)
        val flow = bookDao.getAllBooks()
        val initialBooks = flow.first()
        assertEquals(1, initialBooks.size)

        // When - Delete the book
        bookDao.deleteBookById(insertedId)
        val updatedBooks = flow.first()

        // Then
        assertEquals(0, updatedBooks.size)
        assertTrue(updatedBooks.none { it.id == insertedId })
    }

    @Test
    fun getBookById_emitsNullWhenBookDoesNotExist() = runBlocking {
        // When
        val book = bookDao.getBookById(999L).first()

        // Then
        assertNull(book)
    }

    @Test
    fun getBookById_emitsUpdateWhenBookIsUpdated() = runBlocking {
        // Given
        val book = Book(title = "Original", isRead = false, createdAt = System.currentTimeMillis())
        val insertedId = bookDao.insertBook(book)
        val flow = bookDao.getBookById(insertedId)

        // When - Update the book
        val retrievedBook = flow.first()!!
        val updatedBook = retrievedBook.copy(title = "Updated")
        bookDao.updateBook(updatedBook)
        val updated = flow.first()

        // Then
        assertNotNull(updated)
        assertEquals("Updated", updated?.title)
    }
}

