package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Book
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Composable for displaying a single book item in a card.
 * 
 * @param book The book to display
 * @param modifier Modifier for styling
 */
@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book icon
            Icon(
                imageVector = if (book.isRead) Icons.Default.CheckCircle else Icons.Default.Star,
                contentDescription = if (book.isRead) "Read book" else "Reading list book",
                modifier = Modifier.size(48.dp),
                tint = if (book.isRead) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Book details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (book.author != null) {
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Rating display for read books
                if (book.isRead && book.rating != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingStars(rating = book.rating)
                } else if (book.isRead) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Read (no rating)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "In reading list",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Composable for displaying star rating (1-5 stars).
 * 
 * @param rating The rating value from 1 to 5
 * @param modifier Modifier for styling
 */
@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "⭐".repeat(rating),
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun BookItemPreview() {
    MyApplicationTheme {
        BookItem(
            book = Book(
                id = 1,
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                isRead = true,
                rating = 5,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookItemReadingListPreview() {
    MyApplicationTheme {
        BookItem(
            book = Book(
                id = 2,
                title = "To Kill a Mockingbird",
                author = "Harper Lee",
                isRead = false,
                rating = null,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}
