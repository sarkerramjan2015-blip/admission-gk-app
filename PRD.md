# Product Requirements Document (PRD)
## Project: Admission GK App

### 1. Overview
The **Admission GK App** is a comprehensive educational platform designed for university admission candidates in Bangladesh. It provides structured General Knowledge (GK) study materials, question banks, practice tools, and live mega quizzes to help students prepare effectively for their exams. The app supports both a student interface and an admin dashboard.

### 2. Target Audience
- **Primary:** University admission candidates in Bangladesh (e.g., Dhaka University, Rajshahi University, etc.).
- **Secondary:** Job seekers preparing for BCS or bank exams requiring general knowledge.
- **Admin:** Content creators, teachers, and system administrators managing the educational content.

### 3. Core Features & App Flow

#### 3.1 Authentication & User Profiles
- **Google Sign-In:** Secure login using Firebase Authentication.
- **Role-based Access:** Differentiates between regular students and administrators.
- **Profile Screen:** Displays user information, overall progress, accuracy rates, and allows logging out.

#### 3.2 Student Dashboard (Home Screen)
- **Daily Progress Tracker:** Visual circular progress bar showing daily targets (e.g., 50 MCQs practiced).
- **Mega Quiz Banner:** Highlights the next upcoming Mega Quiz with countdowns and quick access to the routine.
- **University Question Bank Marquee:** A visually appealing, smoothly scrolling marquee displaying university logos (DU, RU, CU, JU) to quickly access past questions.
- **Study Categories Grid:** Quick navigation to main GK topics:
  - Bangladesh Affairs
  - International Affairs
  - Recent GK
  - Question Bank

#### 3.3 Study Module (Notes & Materials)
- **Topic & Subtopic Lists:** Hierarchical organization of study materials.
- **Detailed Study Notes:** 
  - Rich text formatting using Markdown (Markwon) for rendering text, tables, and images.
  - "Book-like" comfortable reading UI (Paper-colored background).
  - Dedicated sections for **Important Facts** (bullet points) and **Confusion Clearances** to resolve common student mistakes.
- **Integrated Actions:** Direct buttons from the study page to "Start Practice" or "Take Exam" on that specific subtopic.

#### 3.4 Practice & Exam System
- **Practice Mode:** 
  - Allows students to answer MCQs with instant feedback.
  - Displays detailed explanations for right/wrong answers.
  - Tracks correct, wrong, and skipped counts.
- **Quiz Mode:** 
  - Time-bound exam environment for a specific subtopic.
  - Final score screen showing percentage and detailed performance.

#### 3.5 Mega Quiz System
- **Routine Screen:** Displays upcoming scheduled live exams.
- **Live Exam Interface:** A strict, time-bound interface for participating in nationwide/platform-wide mock tests.
- **Result & Leaderboard:** Displays score, correct/wrong count, and relative ranking compared to other students.

#### 3.6 Recent GK Feed
- A scrollable feed (similar to social media) providing bite-sized updates on current affairs.

#### 3.7 Admin Dashboard
- **Content Management:** Tools to add, edit, or delete MCQs, Subtopics, and Notes.
- **Mega Quiz Setup:** Scheduling new live exams and adding questions.
- **User Management:** Overseeing user roles and activity.

### 4. Technical Architecture Overview
- **UI Framework:** Jetpack Compose (Modern, declarative UI).
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with Unidirectional Data Flow (StateFlow/SharedFlow).
- **Local Database:** Room Database for fast offline access to study materials and user progress.
- **Remote Backend & Auth:** Firebase (Firestore for sync, Firebase Auth for login).
- **Image Rendering:** Coil & Markwon ImagesPlugin.
- **Navigation:** Jetpack Compose Navigation (`NavHost`).
- **Data Initialization:** Seed data pattern (e.g., `BD1Seed.kt`) to prepopulate the local Room database on first launch.

### 5. Future Roadmap for Rewrite
- Implement real-time sync between Room and Firestore.
- Add dynamic dark/light theme support.
- Introduce gamification (badges, streaks, points).
- Implement a robust offline-first synchronization strategy using WorkManager.
