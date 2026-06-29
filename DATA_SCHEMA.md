# Data Architecture & Schema
## Project: Admission GK App

This document outlines the core data models and database architecture used in the application. The app relies heavily on a local SQLite database using **Room**, populated initially via a **Seed Data Pattern**.

---

### 1. Core Study Materials

#### `GKMainTopicEntity`
Represents the highest level of categorization (e.g., "Bangladesh Affairs", "International Affairs").
- `id` (String): Primary Key
- `category` (String): e.g., "BANGLADESH", "INTERNATIONAL"
- `title` (String): Display title
- `orderIndex` (Int): For UI sorting
- `iconName` (String): Identifier for UI icon

#### `GKSubTopicEntity`
Represents a specific chapter or sub-topic (e.g., "ভৌগোলিক অবস্থান ও সীমানা"). Contains the actual study notes.
- `id` (String): Primary Key
- `parentTopicId` (String): Foreign key referencing `GKMainTopicEntity`
- `title` (String): Subtopic title
- `orderIndex` (Int): For UI sorting
- `note` (String): Detailed study content in **Markdown** format (rendered via Markwon).
- `importantFacts` (String): JSON serialized list of strings for quick bullet points.
- `confusionClearance` (String): Text addressing common mistakes/confusions.
- `referenceLinks` (String): JSON serialized list of reference URLs.

---

### 2. Assessment & Practice

#### `MCQQuestionEntity`
The central entity for all multiple-choice questions linked to a subtopic.
- `id` (String): Primary Key
- `subTopicId` (String): Reference to `GKSubTopicEntity`
- `questionText` (String): The question
- `options` (String): JSON serialized list of 4 options
- `correctAnswer` (String): The exact string of the correct option
- `explanation` (String): Explanation shown after answering
- `sourceExam` (String): e.g., "DU A Unit", "BCS 45"
- `year` (String): Year of the exam
- `referenceText` (String): Citation or book reference
- `status` (String): "PENDING", "APPROVED", "REJECTED" (For admin moderation)

---

### 3. Live Exams (Mega Quiz)

#### `MegaQuizExamEntity`
Represents a scheduled live mock test.
- `id` (String): Primary Key
- `title` (String): Exam Title
- `examDate` (Long): Timestamp of the exam
- `status` (String): "UPCOMING", "LIVE", "COMPLETED"

#### `MegaQuizQuestionEntity`
Questions specifically assigned to a Mega Quiz.
- `id` (String): Primary Key
- `examId` (String): Reference to `MegaQuizExamEntity`
- `questionText` (String)
- `options` (String): JSON list
- `correctAnswer` (String)
- `explanation` (String)
- `relatedSubTopicId` (String?): Optional link to study material

#### `MegaQuizResultEntity`
Stores a user's result for a completed Mega Quiz.
- `id` (String): Primary Key
- `examId` (String): Reference to `MegaQuizExamEntity`
- `score` (Double): Total calculated score (considering negative marking)
- `correctCount` (Int)
- `wrongCount` (Int)
- `unansweredCount` (Int)
- `dateTaken` (Long): Timestamp

---

### 4. Progress Tracking

#### `MCQPracticeProgressEntity`
Tracks daily activity for the user's dashboard target.
- `dateStr` (String): Primary Key (Format: "YYYY-MM-DD")
- `totalPracticed` (Int): Number of questions attempted today
- `correctCount` (Int)
- `wrongCount` (Int)

#### `MCQQuizResultEntity`
Records the outcome of a standard subtopic quiz.
- `id` (String): Primary Key
- `subTopicId` (String): The subtopic tested
- `scorePercentage` (Double): 0.0 to 100.0
- `dateTaken` (Long)

---

### 5. University Question Bank

#### `UniversityEntity`
- `id` (String): Primary Key
- `shortName` (String): e.g., "DU"
- `fullName` (String): e.g., "Dhaka University"
- `logoResName` (String): Resource name for the logo
- `orderIndex` (Int)

#### `UniversityQuestionEntity`
Past questions mapped to specific universities and units.
- `id` (String): Primary Key
- `universityId` (String)
- `year` (Int): e.g., 2023
- `unitName` (String): e.g., "A Unit", "B Unit"
- `subject` (String): e.g., "Physics", "GK"
- `questionText`, `options`, `correctAnswer`, `explanation`, `referenceText`

---

### 6. Dynamic Content Feed

#### `RecentGKEntity`
Used for the scrolling "Recent GK" social-media style feed.
- `id` (String): Primary Key
- `category` (String): "BD", "INT"
- `topicTitle` (String)
- `specialTopicNote` (String): Main content
- `confusionCorner` (String)
- `sourceText` (String): Source of news
- `contributorName` (String): Who posted it
- `status` (String): Approval status
- `createdAt` (Long)
- `approvedAt` (Long?)

---

### 7. Initialization Strategy (Seed Pattern)
To ensure the app has immediate offline value, the Room database is pre-populated on the first launch. 
- **`SeedData.kt`**: A central aggregator that compiles lists of `GKSubTopicEntity` and `MCQQuestionEntity`.
- **Module Seeds**: Data is split into isolated files (e.g., `BD1Seed.kt`, `BD2Seed.kt`) to keep code maintainable. These files return hardcoded instances of the entities containing markdown strings and JSON arrays.
