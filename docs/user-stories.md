# Book Tracking App - User Stories

## Epic: Book Management

### US-001: Add Book to Reading List
**As a** reader  
**I want to** add books to my reading list  
**So that** I can track books I plan to read in the future

**Acceptance Criteria:**
- User can add a book by entering book title (required)
- User can optionally enter author name
- Book is saved to the reading list
- User receives confirmation that book was added
- Book appears in the reading list immediately

**Priority:** High  
**Story Points:** 3

---

### US-002: View Reading List
**As a** reader  
**I want to** view all books in my reading list  
**So that** I can see what books I plan to read

**Acceptance Criteria:**
- User can see a list of all books in their reading list
- List displays book title and author (if provided)
- List is scrollable if there are many books
- Books are displayed in a clear, readable format
- Empty state message is shown when reading list is empty

**Priority:** High  
**Story Points:** 2

---

### US-003: Mark Book as Read
**As a** reader  
**I want to** mark a book from my reading list as read  
**So that** I can track which books I've completed

**Acceptance Criteria:**
- User can select a book from reading list and mark it as read
- User is prompted to rate the book (1-5 stars) when marking as read
- Book is removed from reading list and added to read books list
- Book retains all original information (title, author)
- User receives confirmation of the action

**Priority:** High  
**Story Points:** 3

---

### US-004: Add Book Directly as Read
**As a** reader  
**I want to** add a book directly to my read books list  
**So that** I can track books I've already read without adding them to reading list first

**Acceptance Criteria:**
- User can add a book directly as read
- User must provide book title (required)
- User can optionally provide author name
- User must provide a rating (1-5 stars)
- Book is immediately added to read books list
- User receives confirmation that book was added

**Priority:** High  
**Story Points:** 3

---

## Epic: Rating System

### US-005: Rate a Book (1-5 Stars)
**As a** reader  
**I want to** rate books I've read using a 1-5 star system  
**So that** I can remember my opinion about each book

**Acceptance Criteria:**
- User can select rating from 1 to 5 stars
- Star rating is visually clear and intuitive
- Rating is required when marking book as read
- Rating can be updated later if needed
- Rating is displayed alongside book in read books list

**Priority:** High  
**Story Points:** 2

---

### US-006: Update Book Rating
**As a** reader  
**I want to** update the rating of a book I've already read  
**So that** I can adjust my rating if my opinion changes

**Acceptance Criteria:**
- User can select a book from read books list
- User can change the star rating
- Updated rating is saved immediately
- User receives confirmation of rating update
- Updated rating is reflected in the UI

**Priority:** Medium  
**Story Points:** 2

---

## Epic: View Read Books

### US-007: View Read Books List
**As a** reader  
**I want to** view all books I've read  
**So that** I can see my reading history

**Acceptance Criteria:**
- User can see a list of all books they've read
- List displays book title, author (if provided), and star rating
- List is scrollable if there are many books
- Books are displayed in a clear, readable format
- Empty state message is shown when no books have been read

**Priority:** High  
**Story Points:** 2

---

### US-008: View Book Details
**As a** reader  
**I want to** view details of a book in my read list  
**So that** I can see all information about that book

**Acceptance Criteria:**
- User can tap on a book to view its details
- Details screen shows book title, author, and rating
- User can edit book information from details screen
- User can delete book from details screen

**Priority:** Medium  
**Story Points:** 3

---

## Epic: Book Organization

### US-009: Edit Book Information
**As a** reader  
**I want to** edit book title and author information  
**So that** I can correct mistakes or update information

**Acceptance Criteria:**
- User can edit book title and author for books in reading list
- User can edit book title and author for books in read list
- User can update book information from book details screen
- Changes are saved and reflected immediately
- User receives confirmation of successful update

**Priority:** Medium  
**Story Points:** 2

---

### US-010: Remove Book from Reading List
**As a** reader  
**I want to** remove a book from my reading list  
**So that** I can keep my list organized and remove books I no longer want to read

**Acceptance Criteria:**
- User can delete a book from reading list
- User is prompted to confirm deletion
- Book is removed from reading list
- User receives confirmation of deletion
- Deletion cannot be undone

**Priority:** Medium  
**Story Points:** 2

---

### US-011: Delete Read Book
**As a** reader  
**I want to** delete a book from my read books list  
**So that** I can remove books I no longer want to track

**Acceptance Criteria:**
- User can delete a book from read books list
- User is prompted to confirm deletion
- Book is permanently removed from read books list
- User receives confirmation of deletion
- Deletion cannot be undone

**Priority:** Medium  
**Story Points:** 2

---

## Epic: Navigation and UI

### US-012: Navigate Between Reading List and Read Books
**As a** reader  
**I want to** easily navigate between my reading list and read books  
**So that** I can quickly access both sections of the app

**Acceptance Criteria:**
- User can switch between reading list and read books views
- Navigation is intuitive and clearly labeled
- User's current location in the app is visually indicated
- Navigation works smoothly with no lag

**Priority:** High  
**Story Points:** 3

---

### US-013: View Main Screen with Both Lists
**As a** reader  
**I want to** see an overview of both my reading list and read books  
**So that** I can quickly see my reading status at a glance

**Acceptance Criteria:**
- Main screen displays both reading list and read books
- Both sections are clearly distinguished
- User can see summary counts (e.g., "5 books in reading list", "12 books read")
- Empty states are handled gracefully
- Screen is responsive and performs well

**Priority:** High  
**Story Points:** 5

---

## Epic: Data Persistence

### US-014: Data Persists After App Restart
**As a** reader  
**I want to** have my books saved automatically  
**So that** my reading list and read books are still there when I reopen the app

**Acceptance Criteria:**
- All book data persists when app is closed and reopened
- Reading list is preserved
- Read books list is preserved
- All ratings are preserved
- No data loss occurs during normal app usage

**Priority:** High  
**Story Points:** 1 (Technical Story)

---

## Summary Statistics

| Epic | Story Count | Total Story Points |
|------|------------|-------------------|
| Book Management | 4 | 11 |
| Rating System | 2 | 4 |
| View Read Books | 2 | 5 |
| Book Organization | 3 | 6 |
| Navigation and UI | 2 | 8 |
| Data Persistence | 1 | 1 |
| **Total** | **14** | **35** |

---

## Priority Breakdown

- **High Priority:** 9 stories (25 story points)
- **Medium Priority:** 5 stories (10 story points)

---

**Document Version:** 1.0  
**Last Updated:** 2024-12-28  
**Author:** SM (Scrum Master)

