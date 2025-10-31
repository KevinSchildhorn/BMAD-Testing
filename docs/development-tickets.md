# Book Tracking App - Development Tickets

## Ticket Overview

This document contains all development tickets for the Book Tracking App, organized by priority and sprint. Each ticket references the corresponding user story and includes technical implementation details.

---

## Ticket Status Legend

- **To Do:** Not yet started
- **In Progress:** Currently being worked on
- **In Review:** Code review pending
- **Done:** Completed and verified

---

## Sprint 0: Foundation ✅ CLOSED

**Sprint Status:** ✅ **CLOSED**  
**Sprint End Date:** 2024-12-28  
**Velocity:** 1 SP  
**Completion Rate:** 100%

### TICKET-001: Implement Data Persistence with Room Database
**Status:** Done ✅  
**User Story:** US-014  
**Priority:** P0 (MVP Critical)  
**Story Points:** 1  
**Sprint:** Sprint 0 (Foundation)

**Description:**
Set up Room database infrastructure for local data persistence. This includes database entities, DAOs, and repository pattern implementation.

**Acceptance Criteria:**
- Room database is configured in the project
- Book entity is created with required fields (id, title, author, rating, isRead, createdAt)
- DAO interface is created with CRUD operations
- Repository pattern is implemented
- Database can be accessed and persists data across app restarts
- All data operations use Kotlin coroutines and Flow for reactive updates

**Technical Tasks:**
- [x] Add Room dependencies to `build.gradle.kts`
- [x] Create `Book` entity class with Room annotations
- [x] Create `BookDao` interface with suspend functions
- [x] Create `BookDatabase` class extending `RoomDatabase`
- [x] Create `BookRepository` class implementing data access logic
- [x] Add database versioning strategy
- [x] Create database instance using singleton pattern (manual DI)
- [x] Write unit tests for repository and DAO

**Dependencies:** None (Foundation ticket)

**Technical Notes:**
- Use Room version compatible with current Android SDK (36)
- Consider using Hilt for dependency injection
- Implement Flow-based reactive queries for real-time updates
- Book entity should include: id (Primary Key), title (String), author (String?), rating (Int?), isRead (Boolean), createdAt (Long)

---

## Sprint 1: Core Infrastructure & Adding Books

### TICKET-002: Create Main Screen with Navigation
**Status:** To Do  
**User Story:** US-013  
**Priority:** P0 (MVP Critical)  
**Story Points:** 5  
**Sprint:** Sprint 1

**Description:**
Implement the main screen that displays both reading list and read books sections. This serves as the primary entry point and navigation hub for the app.

**Acceptance Criteria:**
- Main screen displays both reading list and read books sections
- Both sections are clearly distinguished visually
- Summary counts are displayed (e.g., "5 books in reading list", "12 books read")
- Empty states are handled gracefully with appropriate messages
- Screen is responsive and performs well
- Uses Jetpack Compose with Material 3 design
- Navigation structure is established for future screens

**Technical Tasks:**
- [ ] Create `MainScreen.kt` composable
- [ ] Implement section headers with counts
- [ ] Create empty state composables for both sections
- [ ] Implement scrollable layout using LazyColumn or similar
- [ ] Add Material 3 theming and styling
- [ ] Integrate with navigation component
- [ ] Create ViewModel for main screen state management
- [ ] Connect ViewModel to repository to observe book data
- [ ] Test with empty and populated states
- [ ] Ensure proper state handling on configuration changes

**Dependencies:** TICKET-001 (Data Persistence)

**Technical Notes:**
- Use Jetpack Compose Navigation
- Implement MVVM pattern with ViewModel
- Use Kotlin Flow for reactive data observation
- Consider using Compose Navigation for screen transitions
- Main screen can show both lists in tabs, sections, or unified view

---

### TICKET-003: Implement Add Book to Reading List
**Status:** To Do  
**User Story:** US-001  
**Priority:** P0 (MVP Critical)  
**Story Points:** 3  
**Sprint:** Sprint 1

**Description:**
Allow users to add books to their reading list by entering book title and optionally author name.

**Acceptance Criteria:**
- User can open a form/screen to add a new book
- Book title field is required (validation)
- Author field is optional
- User can save the book to reading list
- User receives confirmation that book was added (snackbar/toast)
- Book appears in the reading list immediately after adding
- Form validation prevents saving invalid data

**Technical Tasks:**
- [ ] Create `AddBookDialog.kt` or `AddBookScreen.kt` composable
- [ ] Create form with TextField for title (required) and author (optional)
- [ ] Implement form validation
- [ ] Create `AddBookViewModel` for form state management
- [ ] Add "Add Book" action button in main screen (FAB or menu item)
- [ ] Implement save functionality in ViewModel
- [ ] Call repository to insert book with `isRead = false`
- [ ] Show confirmation message (Snackbar)
- [ ] Update UI state after successful save
- [ ] Handle error cases (validation, save failures)
- [ ] Add unit tests for ViewModel

**Dependencies:** TICKET-001 (Data Persistence), TICKET-002 (Main Screen)

**Technical Notes:**
- Use Material 3 TextField components
- Implement proper text input validation
- Use coroutines for async operations
- Consider using a Dialog or Bottom Sheet for add form
- Book should be saved with `isRead = false` and current timestamp

---

### TICKET-004: Display Reading List
**Status:** To Do  
**User Story:** US-002  
**Priority:** P0 (MVP Critical)  
**Story Points:** 2  
**Sprint:** Sprint 1

**Description:**
Display all books in the user's reading list in a clear, scrollable format.

**Acceptance Criteria:**
- Reading list section displays all books where `isRead = false`
- List displays book title and author (if provided)
- List is scrollable if there are many books
- Books are displayed in a clear, readable format (cards or list items)
- Empty state message is shown when reading list is empty
- List updates automatically when books are added/removed

**Technical Tasks:**
- [ ] Create `ReadingListSection.kt` composable
- [ ] Create `BookItem.kt` composable for displaying book information
- [ ] Implement LazyColumn for scrollable list
- [ ] Query books from repository where `isRead = false`
- [ ] Observe Flow from repository for real-time updates
- [ ] Implement empty state UI
- [ ] Style book items with Material 3 components
- [ ] Ensure proper layout and spacing
- [ ] Handle loading states
- [ ] Test with multiple books and empty state

**Dependencies:** TICKET-001 (Data Persistence), TICKET-002 (Main Screen), TICKET-003 (Add Book)

**Technical Notes:**
- Use Compose LazyColumn for efficient scrolling
- Observe repository Flow to get reactive updates
- Book items should be visually distinct and readable
- Consider using Card composable for book items
- Empty state should encourage user to add books

---

## Sprint 2: Reading Workflow

### TICKET-005: Implement Star Rating Component
**Status:** To Do  
**User Story:** US-005  
**Priority:** P0 (MVP Critical)  
**Story Points:** 2  
**Sprint:** Sprint 2

**Description:**
Create a reusable star rating component that allows users to select a rating from 1 to 5 stars.

**Acceptance Criteria:**
- User can select rating from 1 to 5 stars
- Star rating is visually clear and intuitive
- Stars are interactive (tap to select rating)
- Rating can be displayed read-only
- Rating can be edited/selected
- Visual feedback when rating is selected
- Component is reusable across the app

**Technical Tasks:**
- [ ] Create `StarRating.kt` composable component
- [ ] Implement interactive star selection (editable mode)
- [ ] Implement read-only star display (display mode)
- [ ] Add visual feedback (filled/empty stars, hover states)
- [ ] Handle touch events for rating selection
- [ ] Style with Material 3 theming
- [ ] Make component parameterizable (initial rating, editable flag)
- [ ] Add unit tests for rating logic
- [ ] Document component usage

**Dependencies:** TICKET-001 (Data Persistence)

**Technical Notes:**
- Use IconButton or custom clickable composable for stars
- Consider using Material Icons for star shapes
- Rating should be stored as Int (1-5)
- Component should emit rating changes via callback
- Can use `FilledStar` and `StarBorder` icons from Material Icons

---

### TICKET-006: Mark Book as Read with Rating
**Status:** To Do  
**User Story:** US-003  
**Priority:** P0 (MVP Critical)  
**Story Points:** 3  
**Sprint:** Sprint 2

**Description:**
Allow users to mark a book from their reading list as read, requiring them to provide a rating.

**Acceptance Criteria:**
- User can select a book from reading list
- User is prompted to rate the book (1-5 stars) when marking as read
- Rating is required before marking as read
- Book is removed from reading list and added to read books list
- Book retains all original information (title, author)
- User receives confirmation of the action
- UI updates immediately after marking as read

**Technical Tasks:**
- [ ] Add "Mark as Read" action to book items in reading list
- [ ] Create dialog/screen for rating selection
- [ ] Integrate StarRating component from TICKET-005
- [ ] Create `MarkAsReadViewModel` for state management
- [ ] Implement logic to update book (`isRead = true`, set rating)
- [ ] Call repository to update book
- [ ] Show confirmation message
- [ ] Handle validation (rating must be selected)
- [ ] Update UI state reactively
- [ ] Add unit tests for ViewModel

**Dependencies:** TICKET-001 (Data Persistence), TICKET-004 (Reading List), TICKET-005 (Star Rating)

**Technical Notes:**
- Update book entity in database with `isRead = true` and rating value
- Use repository update method
- Rating dialog can be a Bottom Sheet or Dialog
- Book automatically appears in read books list via Flow observation

---

### TICKET-007: Display Read Books List
**Status:** To Do  
**User Story:** US-007  
**Priority:** P0 (MVP Critical)  
**Story Points:** 2  
**Sprint:** Sprint 2

**Description:**
Display all books the user has marked as read, including their ratings.

**Acceptance Criteria:**
- Read books section displays all books where `isRead = true`
- List displays book title, author (if provided), and star rating
- List is scrollable if there are many books
- Books are displayed in a clear, readable format
- Empty state message is shown when no books have been read
- List updates automatically when books are marked as read

**Technical Tasks:**
- [ ] Create `ReadBooksSection.kt` composable
- [ ] Update `BookItem.kt` to display ratings for read books
- [ ] Integrate StarRating component for display
- [ ] Query books from repository where `isRead = true`
- [ ] Observe Flow from repository for real-time updates
- [ ] Implement empty state UI
- [ ] Ensure visual distinction from reading list
- [ ] Style with Material 3 components
- [ ] Handle loading states
- [ ] Test with multiple books and empty state

**Dependencies:** TICKET-001 (Data Persistence), TICKET-002 (Main Screen), TICKET-005 (Star Rating)

**Technical Notes:**
- Similar implementation to TICKET-004 but filtering `isRead = true`
- Book items should display rating visually
- Use read-only StarRating component for display
- Section should show count of read books

---

### TICKET-008: Add Book Directly as Read
**Status:** To Do  
**User Story:** US-004  
**Priority:** P0 (MVP Critical)  
**Story Points:** 3  
**Sprint:** Sprint 2

**Description:**
Allow users to add a book directly to their read books list without first adding it to the reading list.

**Acceptance Criteria:**
- User can open a form to add a book as read
- Book title field is required (validation)
- Author field is optional
- Rating is required (1-5 stars)
- User can save the book directly to read books list
- User receives confirmation that book was added
- Book appears in the read books list immediately
- Form validation prevents saving invalid data

**Technical Tasks:**
- [ ] Create `AddReadBookDialog.kt` or `AddReadBookScreen.kt` composable
- [ ] Create form with TextField for title (required), author (optional), and rating (required)
- [ ] Integrate StarRating component for rating selection
- [ ] Implement form validation (title and rating required)
- [ ] Create or reuse ViewModel for form state management
- [ ] Add "Add Read Book" action in main screen or read books section
- [ ] Implement save functionality in ViewModel
- [ ] Call repository to insert book with `isRead = true` and rating
- [ ] Show confirmation message
- [ ] Update UI state after successful save
- [ ] Handle error cases
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-002 (Main Screen), TICKET-005 (Star Rating), TICKET-007 (Read Books List)

**Technical Notes:**
- Similar to TICKET-003 but requires rating and sets `isRead = true`
- Can reuse some components from TICKET-003
- Book should be saved with rating and current timestamp
- Consider combining with TICKET-003 into a single reusable form component

---

### TICKET-009: Navigate Between Reading List and Read Books
**Status:** To Do  
**User Story:** US-012  
**Priority:** P0 (MVP Critical) - May be P1 if main screen shows both together  
**Story Points:** 3  
**Sprint:** Sprint 2

**Description:**
Implement navigation between reading list and read books views if main screen doesn't show both together.

**Acceptance Criteria:**
- User can switch between reading list and read books views
- Navigation is intuitive and clearly labeled
- User's current location in the app is visually indicated
- Navigation works smoothly with no lag
- Uses standard Android navigation patterns

**Technical Tasks:**
- [ ] Evaluate if needed based on TICKET-002 implementation
- [ ] If needed, implement navigation using Jetpack Compose Navigation
- [ ] Create navigation routes for each screen
- [ ] Implement bottom navigation, tabs, or drawer navigation
- [ ] Add navigation state management
- [ ] Ensure proper back button handling
- [ ] Test navigation flow
- [ ] Style navigation components with Material 3

**Dependencies:** TICKET-002 (Main Screen)

**Technical Notes:**
- Only needed if main screen shows lists separately (tabs/pages)
- If main screen shows both together, this ticket may be cancelled or downgraded
- Consider using BottomNavigation or TabRow from Material 3
- Navigation state should be preserved on configuration changes

---

## Sprint 3: Organization Features

### TICKET-010: Remove Book from Reading List
**Status:** To Do  
**User Story:** US-010  
**Priority:** P1 (Should Have)  
**Story Points:** 2  
**Sprint:** Sprint 3

**Description:**
Allow users to delete a book from their reading list.

**Acceptance Criteria:**
- User can delete a book from reading list
- User is prompted to confirm deletion
- Book is removed from reading list
- User receives confirmation of deletion
- Deletion cannot be undone
- UI updates immediately after deletion

**Technical Tasks:**
- [ ] Add delete action to book items in reading list (swipe, long press, or menu)
- [ ] Implement confirmation dialog
- [ ] Create ViewModel method for deletion
- [ ] Call repository to delete book
- [ ] Show confirmation message (Snackbar)
- [ ] Handle error cases
- [ ] Update UI state reactively
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-004 (Reading List)

**Technical Notes:**
- Use repository delete method
- Implement swipe-to-delete or long-press menu for delete action
- Confirmation dialog prevents accidental deletions
- Consider Material 3 AlertDialog for confirmation

---

### TICKET-011: Delete Read Book
**Status:** To Do  
**User Story:** US-011  
**Priority:** P1 (Should Have)  
**Story Points:** 2  
**Sprint:** Sprint 3

**Description:**
Allow users to delete a book from their read books list.

**Acceptance Criteria:**
- User can delete a book from read books list
- User is prompted to confirm deletion
- Book is permanently removed from read books list
- User receives confirmation of deletion
- Deletion cannot be undone
- UI updates immediately after deletion

**Technical Tasks:**
- [ ] Add delete action to book items in read books list
- [ ] Implement confirmation dialog
- [ ] Create ViewModel method for deletion
- [ ] Call repository to delete book
- [ ] Show confirmation message
- [ ] Handle error cases
- [ ] Update UI state reactively
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-007 (Read Books List)

**Technical Notes:**
- Similar implementation to TICKET-010
- Can reuse delete confirmation dialog component
- Ensure consistent deletion UX across both lists

---

### TICKET-012: Update Book Rating
**Status:** To Do  
**User Story:** US-006  
**Priority:** P1 (Should Have)  
**Story Points:** 2  
**Sprint:** Sprint 3

**Description:**
Allow users to update the rating of a book they've already read.

**Acceptance Criteria:**
- User can select a book from read books list
- User can change the star rating
- Updated rating is saved immediately
- User receives confirmation of rating update
- Updated rating is reflected in the UI immediately

**Technical Tasks:**
- [ ] Add edit rating action to read book items
- [ ] Create dialog/screen for rating update
- [ ] Integrate StarRating component (editable mode)
- [ ] Create ViewModel method for rating update
- [ ] Call repository to update book rating
- [ ] Show confirmation message
- [ ] Update UI state reactively
- [ ] Handle validation
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-005 (Star Rating), TICKET-007 (Read Books List)

**Technical Notes:**
- Update only the rating field in database
- Use repository update method
- Rating dialog can be similar to marking as read dialog
- Can reuse components from TICKET-006

---

### TICKET-013: Edit Book Information
**Status:** To Do  
**User Story:** US-009  
**Priority:** P1 (Should Have)  
**Story Points:** 2  
**Sprint:** Sprint 3

**Description:**
Allow users to edit book title and author information for books in both reading list and read books list.

**Acceptance Criteria:**
- User can edit book title and author for books in reading list
- User can edit book title and author for books in read list
- User can update book information from book details screen
- Changes are saved and reflected immediately
- User receives confirmation of successful update
- Form validation prevents saving invalid data

**Technical Tasks:**
- [ ] Add edit action to book items (both lists)
- [ ] Create edit book dialog/screen
- [ ] Pre-populate form with existing book data
- [ ] Create ViewModel method for updating book
- [ ] Call repository to update book (title, author)
- [ ] Show confirmation message
- [ ] Update UI state reactively
- [ ] Handle validation (title required)
- [ ] Handle error cases
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-004 (Reading List), TICKET-007 (Read Books List)

**Technical Notes:**
- Update book entity with new title/author
- Reuse form components from add book screens
- Validation should ensure title is not empty
- Can be accessed from book details screen (TICKET-014) or directly from list

---

## Sprint 4: Enhancement

### TICKET-014: View Book Details Screen
**Status:** To Do  
**User Story:** US-008  
**Priority:** P1 (Should Have)  
**Story Points:** 3  
**Sprint:** Sprint 4

**Description:**
Create a detailed view screen for individual books that allows viewing and managing book information.

**Acceptance Criteria:**
- User can tap on a book to view its details
- Details screen shows book title, author, and rating (if read)
- User can edit book information from details screen
- User can delete book from details screen
- Navigation to and from details screen works smoothly

**Technical Tasks:**
- [ ] Create `BookDetailsScreen.kt` composable
- [ ] Implement navigation to details screen from book items
- [ ] Display all book information (title, author, rating, read status)
- [ ] Add edit action button
- [ ] Integrate with TICKET-013 (Edit Book) functionality
- [ ] Add delete action button
- [ ] Integrate with TICKET-010/TICKET-011 (Delete Book) functionality
- [ ] Implement proper navigation back handling
- [ ] Style with Material 3 components
- [ ] Handle loading and error states
- [ ] Add unit tests

**Dependencies:** TICKET-001 (Data Persistence), TICKET-005 (Star Rating), TICKET-013 (Edit Book), TICKET-010/TICKET-011 (Delete Book)

**Technical Notes:**
- Use Jetpack Compose Navigation for screen transitions
- Pass book ID or object to details screen
- Details screen can be accessed from both reading list and read books list
- Consider sharing ViewModel or creating separate ViewModel for details screen

---

## Ticket Summary by Sprint

| Sprint | Tickets | Total Story Points | Priority |
|--------|---------|-------------------|----------|
| Sprint 0 | TICKET-001 | 1 | P0 |
| Sprint 1 | TICKET-002, TICKET-003, TICKET-004 | 10 | P0 |
| Sprint 2 | TICKET-005, TICKET-006, TICKET-007, TICKET-008, TICKET-009 | 13 | P0 |
| Sprint 3 | TICKET-010, TICKET-011, TICKET-012, TICKET-013 | 8 | P1 |
| Sprint 4 | TICKET-014 | 3 | P1 |
| **Total** | **14 tickets** | **35 SP** | |

---

## MVP Definition

**MVP Tickets (Sprints 0-2):**
- TICKET-001: Data Persistence
- TICKET-002: Main Screen
- TICKET-003: Add Book to Reading List
- TICKET-004: Display Reading List
- TICKET-005: Star Rating Component
- TICKET-006: Mark Book as Read
- TICKET-007: Display Read Books List
- TICKET-008: Add Book Directly as Read
- TICKET-009: Navigation (if needed)

**MVP Total: 9 tickets, ~24 story points**

---

**Document Version:** 1.0  
**Last Updated:** 2024-12-28  
**Author:** PO (Product Owner)

