package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Book
import com.example.myapplication.data.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the book list screen.
 * Observes books from the repository and provides UI state.
 */
class BookListViewModel(
    private val repository: BookRepository
) : ViewModel() {

    /**
     * StateFlow containing all books from the database.
     * Automatically updates when books are added, modified, or deleted.
     */
    val books: StateFlow<List<Book>> = repository.getAllBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
