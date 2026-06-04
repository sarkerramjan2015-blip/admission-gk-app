# Full Project Audit Report: Admission GK

## 1. Executive Summary
The Admission GK app is currently in a prototype/MVP state. While it leverages modern Android technologies (Jetpack Compose, Room, Kotlin Coroutines), its architecture and implementation reveal that it is largely a static UI mockup. Core business logic, backend connectivity, and state management are missing or implemented poorly. The app relies heavily on hardcoded data, mock progress states, and parses JSON locally in the UI layer. Significant refactoring is required before considering a production release.

## 2. Project Overview
- **Platform**: Android (Jetpack Compose)
- **Language**: Kotlin
- **Architecture**: MVI/MVVM attempted, but heavily coupled UI. No Dependency Injection (DI).
- **Storage**: Room Database (Local SQLite) with `SeedData`.
- **Status**: Prototype / Mockup.

## 3. Startup Readiness Score
**Score: 3/10**
The app is not ready for real users. It acts as a static prototype. It lacks user authentication, a backend API to sync new questions (crucial for a GK app), payment gateways for premium features, and proper state management.

## 4. Critical Issues
- **Severity**: Critical
- **File/Screen**: `app/src/main/java/com/example/ui/screens/QuizScreens.kt` (Lines 120, 353) & `SubTopicDetailScreen.kt` (Line 293)
- **Problem**: `Moshi` builder and JSON parsing are instantiated inside `@Composable` functions.
- **Why it matters**: Composables can run hundreds of times per second (e.g., during animations or scrolling). Instantiating a JSON parser and parsing strings on the main thread during recomposition will cause massive performance drops, stuttering, and severe memory leaks.
- **Suggested fix**: Move JSON parsing to the `Repository` or `ViewModel` layer. For Room entities, use `TypeConverter` so the database returns proper lists instead of JSON strings.
- **Effort**: Medium

- **Severity**: Critical
- **File/Screen**: `app/src/main/java/com/example/data/Entities.kt` & `Converters.kt`
- **Problem**: `TypeConverter` is defined in `Converters.kt` but NEVER used in the actual entities (e.g., `val options: String` is used instead of `val options: List<String>`).
- **Why it matters**: Forces manual JSON parsing in the UI. 
- **Suggested fix**: Change `String` to `List<String>` in Entity fields (`options`, `importantFacts`, etc.) and annotate the database with `@TypeConverters(Converters::class)`.
- **Effort**: Small

- **Severity**: Critical
- **File/Screen**: `app/src/main/java/com/example/ui/screens/QuizScreens.kt`
- **Problem**: Quiz timer logic (`timeLeft`) is handled via `LaunchedEffect(Unit) { delay(1000) }` inside the Composable.
- **Why it matters**: Timer will be destroyed and recreated if the screen recomposes incorrectly, and will pause/break if the user rotates the screen or puts the app in the background.
- **Suggested fix**: Move timer logic to `GKViewModel` using Coroutine `ticker` or `CountDownTimer`, exposing a `StateFlow<Int>`.
- **Effort**: Medium

## 5. High Priority Issues
- **Severity**: High
- **File/Screen**: `app/src/main/java/com/example/ui/screens/CategoryScreen.kt` (Lines 314-332)
- **Problem**: `mockProgress`, `mockSubtitleListBD`, `mockSubtitleListInt` are hardcoded arrays providing fake UI data.
- **Why it matters**: Real users will see fake progress and fake subtitles regardless of their actual data or DB content.
- **Suggested fix**: Remove mock data and pull actual progress/subtitles from the `GKViewModel` connected to the Room DB.
- **Effort**: Medium

- **Severity**: High
- **File/Screen**: `MainActivity.kt` & `MainApp.kt`
- **Problem**: Missing Dependency Injection (e.g., Hilt/Dagger). `AppDatabase` and `GKRepository` are manually instantiated and passed down.
- **Why it matters**: Makes the app impossible to scale, test, or manage properly in a real-world scenario.
- **Suggested fix**: Implement Dagger-Hilt to inject Repositories and ViewModels.
- **Effort**: Large

- **Severity**: High
- **File/Screen**: Entire App
- **Problem**: No real Backend API connection. `SeedData.kt` hardcodes local DB entries.
- **Why it matters**: A General Knowledge app needs continuous updates. You cannot update the app via Play Store just to add new GK questions.
- **Suggested fix**: Implement Retrofit to fetch daily/weekly GK updates from a real server (e.g., Node.js/Firebase).
- **Effort**: Large

## 6. Medium Priority Issues
- **Severity**: Medium
- **File/Screen**: Multiple (`HomeScreen.kt`, `CategoryScreen.kt`, `TopicListScreen.kt`)
- **Problem**: Hardcoded custom colors (e.g., `val SnPrimaryColor = Color(...)`) instead of using Compose `MaterialTheme.colorScheme`.
- **Why it matters**: Prevents easy implementation of Dark Mode and leads to inconsistent UI if a primary color needs to change.
- **Suggested fix**: Define colors in `Theme.kt` and use `MaterialTheme.colorScheme.primary`, `secondary`, etc.
- **Effort**: Medium

- **Severity**: Medium
- **File/Screen**: `SeedData.kt` (Line 11)
- **Problem**: `SeedData.populateDatabase` is called on every app launch in `MainActivity`. It checks if topics exist, but it's an unnecessary IO operation every time.
- **Why it matters**: Slight delay in startup time.
- **Suggested fix**: Use Room's `RoomDatabase.Callback` `onCreate` method to populate the database only once when it's created.
- **Effort**: Small

## 7. Low Priority Issues
- **Severity**: Low
- **File/Screen**: `app/src/main/java/com/example/ui/screens/SubTopicDetailScreen.kt`
- **Problem**: Background `Canvas` rendering lots of dots.
- **Why it matters**: Can cause frame drops on low-end Android devices.
- **Suggested fix**: Use an image drawable resource (`.webp`) with repeat modes instead of drawing via `Canvas` manually.
- **Effort**: Small

## 8. Garbage / Unused / Duplicate Code List
- **Garbage**: `data class data_class_hack(...)` in `CategoryScreen.kt:444`. Extremely unprofessional naming and placement.
- **Garbage**: Fake `onClick` actions `/* Search */`, `/* TOC */` left in production UI.
- **Unused**: commented out dependencies in `build.gradle.kts` (`// implementation(libs.firebase.ai)` etc.). Clean them up if not needed.

## 9. Logic Bugs
- **Severity**: High
- **File/Screen**: `app/src/main/java/com/example/ui/screens/QuizScreens.kt` (Line 266)
- **Problem**: `MegaQuizLiveScreen` repeats questions `(0..5).flatMap { rawQuestions }.take(30)` to fake a 30-question exam.
- **Why it matters**: User will see the same questions repeating.
- **Suggested fix**: Load actual 30 distinct questions from the DB or backend.
- **Effort**: Small

- **Severity**: Medium
- **File/Screen**: `app/src/main/java/com/example/ui/screens/QuizScreens.kt` (Line 66 & 292)
- **Problem**: Quiz scoring is calculated dynamically inside the UI layer `onClick` listener.
- **Why it matters**: Business logic should not be inside UI composables.
- **Suggested fix**: Move calculation to `GKViewModel.submitQuiz(answers)`.
- **Effort**: Small

## 10. UI/UX Problems
- **Bottom Navigation**: Clicks don't work (`HomeScreen.kt:312`).
- **Empty States**: If a quiz has no questions, it just shows centered text. Needs a proper empty state illustration.
- **Visual Feedback**: The Timer in `MegaQuizLiveScreen` turns red when `< 60s`, but there is no pulse animation or sound. It is easy to miss.

## 11. Home Page Problems and Improvements
- **Problems**:
  - Search bar is completely disconnected.
  - "Recent GK" has fake action `/* navigate */`.
  - Dashboard summary shows hardcoded numbers (25, 18, 07).
  - Timer for Mega Quiz uses hardcoded values (02 Days, 04 Hrs).
- **Improvements**:
  - Connect the search bar to a DB query using `Flow` and debounce.
  - Show a "Daily Streak" to encourage retention.
  - Create a generic "Dashboard Data" object to feed the summary cards from actual DB stats.

## 12. Security Problems
- **Problem**: No authentication or authorization.
- **Why it matters**: Users cannot save their progress across devices. Anyone can spoof the Mega Quiz results.
- **Fix**: Integrate Firebase Authentication or a custom JWT auth system. 

- **Problem**: Mega Quiz answers are stored in cleartext locally before the exam.
- **Why it matters**: A tech-savvy user can extract the SQLite DB and cheat in the live Mega Quiz.
- **Fix**: Fetch Mega Quiz questions dynamically from the backend ONLY when the quiz starts. Validate answers server-side.

## 13. Performance Problems
- JSON parsing inside `@Composable` (mentioned in Critical).
- Heavy `Canvas` usage for backgrounds.
- List operations (`mcqs.forEach`, `flatMap`) inside UI click listeners.

## 14. Database/API Problems
- **Problem**: No real API.
- **Fix**: Define Retrofit interfaces for `/api/v1/topics`, `/api/v1/quiz`, etc.
- **Problem**: `Entities.kt` uses JSON Strings for arrays.
- **Fix**: Use Room `@TypeConverters`.

## 15. Missing Features (Must Have)
- User Authentication (Login/Registration).
- Real Backend Sync (fetch new GK topics daily).
- Profile & Settings (Dark mode toggle, notification settings).
- Push Notifications (Daily GK reminders).

## 16. Feature Ideas for Real-World Startup Version
- **Must Have**: Progress Sync across devices, Daily Leaderboard.
- **Should Have**: PDF Export of Notes, Offline Mode caching.
- **Could Have**: Subject-wise analytics (e.g., "You are weak in Geography").
- **Future Premium**: Paid Mock Tests (bKash/Nagad integration), Live doubt solving.

## 17. Suggested Better App Flow
1. **Splash Screen** -> Checks session.
2. **Onboarding** -> If first time, show features.
3. **Login / Signup** (OTP based for BD).
4. **Home (Dashboard)** -> Daily tasks, streaks, recent GK.
5. **Topics Explorer** -> Read Notes.
6. **Practice Mode** -> Endless MCQ.
7. **Exam Mode** -> Time-bound tests with negative marking.
8. **Leaderboard & Profile** -> Check rank.

## 18. Suggested Better Home Page Layout
- **Top**: User Profile mini-card + Daily Streak 🔥 + Notification Icon.
- **Search Bar**: Sticky search.
- **Highlight Card**: "Today's Must Read Topic" (dynamic).
- **Grid Categories**: BD GK, INT GK, Recent GK, University QB.
- **Banner**: Live / Upcoming Mega Quiz.
- **Bottom Section**: Performance graph/summary of the week.

## 19. Manual Testing Checklist
- [ ] Put the app in background during a Quiz. Verify timer pauses/continues correctly.
- [ ] Rotate the screen during a Quiz. Verify state is not lost (currently it will be due to lack of ViewModel state saving).
- [ ] Force close app and reopen. Verify last read topic is remembered.
- [ ] Turn off WiFi. Verify the app shows an "Offline Mode" banner but still loads cached Room data.
- [ ] Click "Submit" very fast multiple times on quiz. Ensure it doesn't navigate twice.

## 20. Recommended Fixing Roadmap
### Phase 1: Critical Architecture Fixes (Next 2 Days)
- Refactor Entities to use `List<String>` and `TypeConverters`.
- Remove Moshi instances from Composables.
- Implement Hilt for Dependency Injection.
- Move business logic (scoring, timers) to `GKViewModel`.

### Phase 2: UI/UX Cleanup & Fake Data Removal (Next 3 Days)
- Remove `mockProgress` and hardcoded subtitles.
- Refactor custom colors to use `MaterialTheme.colorScheme`.
- Fix Bottom Navigation and Search bar bindings.
- Remove `data_class_hack`.

### Phase 3: Backend Integration (Next 2 Weeks)
- Set up a real backend (Node.js/Firebase).
- Implement Retrofit.
- Create Login/Signup flow.
- Sync real GK data to Room DB.

### Phase 4: Production/Startup Readiness (Final Polish)
- Implement Firebase Crashlytics and Analytics.
- Add payment gateways if pursuing premium features.
- Thorough QA using the checklist.

## 21. Final Recommendation
**DO NOT release this build.** The current architecture is a ticking time bomb for performance issues (Moshi in UI) and is entirely fake/hardcoded. Stop adding new screens. Dedicate the next week to Phase 1 and Phase 2 of the roadmap to build a solid foundation (DI, ViewModels, Room Converters). Once the foundation is solid, connect a real backend.
