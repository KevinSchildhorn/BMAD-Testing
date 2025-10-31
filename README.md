# BMAD Testing

This is a test of the [BMAD](https://github.com/bmad-code-org/BMAD-METHOD) (Business Multi-Agent Development) framework for multi-agent collaboration on software development tasks.
## Agent System

### Core Development Team

| Agent       | Role               | Primary Functions                              | When to Use                                       |
| ----------- | ------------------ | ---------------------------------------------- | ------------------------------------------------- |
| `analyst`   | Business Analyst   | Market research, requirements gathering        | Project planning, competitive analysis            |
| `pm`        | Product Manager    | PRD creation, feature prioritization           | Strategic planning, roadmaps                      |
| `architect` | Solution Architect | System design, technical architecture          | Complex systems, scalability planning             |
| `dev`       | Developer          | Sequential task execution, testing, validation | Story implementation with test-driven development |
| `qa`        | QA Specialist      | Code review, refactoring, test validation      | Senior developer review via `review-story` task   |
| `ux-expert` | UX Designer        | UI/UX design, prototypes                       | User experience, interface design                 |
| `po`        | Product Owner      | Backlog management, story validation           | Story refinement, acceptance criteria             |
| `sm`        | Scrum Master       | Sprint planning, story creation                | Project management, workflow                      |

### Meta Agents

| Agent               | Role             | Primary Functions                     | When to Use                       |
| ------------------- | ---------------- | ------------------------------------- | --------------------------------- |
| `bmad-orchestrator` | Team Coordinator | Multi-agent workflows, role switching | Complex multi-role tasks          |
| `bmad-master`       | Universal Expert | All capabilities without switching    | Single-session comprehensive work |

## Prompts

> @pm Create a PRD for an app that tracks what books you've read and how you would rate the book from 1 to 5. Also it should have the option of a reading list, which is a list of books you want to read

created `prd.md`

```
Created Section 1: Goals and Background Context.
Rationale:
Goals align with your requirements: tracking read books, 1–5 rating, reading list
Background positions this as a simpler, more personal alternative to Goodreads
Assumption: focus on individual users (not social/sharing) for simplicity
Assumptions:
Individual user focus (no social features)
Basic rating (1–5 stars)
Simple reading list (to-read books)
```

> @architect Design the system architecture

created `architecture.md`

> @sm can you create some user stories for the book tracking app

```
Content Summary:
Technical Summary: Native Android app using MVVM, Jetpack Compose, and Room
High Level Overview: Single-activity, local-first architecture with no backend
Architecture Diagram: Mermaid diagram showing UI → ViewModel → Repository → DAO → Database flow
Architectural Patterns: MVVM, Repository Pattern, Single Source of Truth, Observer Pattern
Rationale:
MVVM: Android-recommended pattern; separates concerns; testable; works with Compose
Room Database: Type-safe ORM; local SQLite; supports Flow for reactive updates
Jetpack Compose: Already in project; declarative UI; reactive updates
Kotlin Coroutines & Flow: Handles async operations; reactive data streams
Offline-First: No backend needed for MVP; simpler architecture; better UX
Assumptions:
Mobile-only; no cloud sync in MVP
Single-user; no multi-device sync needed yet
Data fits in local SQLite (Room handles this)
Trade-offs:
Pros: Simple, offline-first, no network dependencies, fast local queries
Cons: No cloud backup, single-device only, limited scale (fine for personal use)
```
created `user-stories.md`

```
Created a user stories document for the book tracking app. It includes 14 user stories organized into 6 epics:
Book Management — adding and managing books in the reading list
Rating System — 1–5 star rating
View Read Books — viewing reading history
Book Organization — editing and deleting books
Navigation and UI — navigating between sections
Data Persistence — local storage
Each story follows the standard format:
As a [user type]
I want to [action]
So that [benefit]
```
> @pm can you prioritize some features

created `feature-prioritization.md`

```
Priority structure
P0 (Must Have - MVP Critical): 8 features (19 story points)
Data Persistence (foundation)
Main Screen/Navigation
Add to Reading List
View Reading List
Rate Books (1-5 stars)
Mark Book as Read
View Read Books
Add Books Directly as Read
P1 (Should Have - High Value): 5 features (11 story points)
Delete from Reading List
Delete Read Books
Update Book Rating
Edit Book Information
View Book Details
P2 (Nice to Have): Future enhancements
Search/Filter, Statistics, Export, Reading Goals, etc.
Recommended sprint plan
Sprint 0 (Foundation): Data persistence setup - 1 SP
Sprint 1 (Core): Main screen, add books, view lists - 10 SP
Sprint 2 (Reading Workflow): Rating, mark as read, view read books - 13 SP
Sprint 3 (Organization): Delete, edit, update rating - 8 SP
Sprint 4 (Enhancement): Book details, polish - 3 SP
```

> @po can you look at the user-stories.md and create some tickets around the work

created `development-tickets.md`

```
Tickets created
Sprint 0 (Foundation):
TICKET-001: Data Persistence (1 SP)
Sprint 1 (Core):
TICKET-002: Main Screen (5 SP)
TICKET-003: Add Book to Reading List (3 SP)
TICKET-004: Display Reading List (2 SP)
Sprint 2 (Reading Workflow):
TICKET-005: Star Rating Component (2 SP)
TICKET-006: Mark Book as Read (3 SP)
TICKET-007: Display Read Books List (2 SP)
TICKET-008: Add Book Directly as Read (3 SP)
TICKET-009: Navigation (3 SP)
Sprint 3 (Organization):
TICKET-010: Remove Book from Reading List (2 SP)
TICKET-011: Delete Read Book (2 SP)
TICKET-012: Update Book Rating (2 SP)
TICKET-013: Edit Book Information (2 SP)
Sprint 4 (Enhancement):
TICKET-014: Book Details Screen (3 SP)

Total: 14 tickets, 35 story points

```

