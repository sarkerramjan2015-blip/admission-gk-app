# Build
- Use `assembleDebug` instead of `assembleRelease` during development. Confidence: 0.70

# Architecture
- Student Dashboard features should be integrated into the Profile screen, not as standalone cards on the HomeScreen. Confidence: 0.65

# Communication
- Reply in English, not Bengali. User explicitly rejects Bengali responses, even in casual/informal contexts. Confidence: 0.85

# Data Storage
- Use Firebase (server-side) for all data storage, not Room DB. User explicitly rejects local database. Confidence: 0.65

# Authentication
- Use Gmail/Google Sign-In for login authentication. Confidence: 0.50

# Mega Quiz
- Mega Quiz costs 20 TK per quiz to participate. Each month has 2 quizzes. Confidence: 0.65

# Workflow
- Process data/content files autonomously in sequence without asking for user permission between files. Only notify when all files are complete. Confidence: 0.70

# Content Quality
- Replace all placeholder/generic MCQs with real past competitive exam questions, appending exam name and year (e.g., [BCS 41st], [DU A 2019]). Confidence: 0.70
- Never use truncation comments like "// rest unchanged" — always output complete, compilable code for each file. Confidence: 0.70
- When updating data files, only modify string literals (Bengali text, notes, questions). Never change Kotlin logic, data classes, UI components, or variable names. Confidence: 0.70
- GKSubTopicEntity note strings must use structured markdown/HTML: bold heading for topic name, minimalist Unicode symbols (✦, ✧, ❖, ⟡, ➢, ◘, ✔, ✘, ⚠️, ⬡) instead of standard colorful emojis, markdown tables for tabular data, color-coded HTML font tags (#D32F2F red bold for most-important, #1976D2 blue for important, default for general), and always include ### 💡 Confusion Corner and ### 🛑 Alert / Common Mistakes sections at the bottom. Confidence: 0.85
- When refactoring seed data notes, NEVER alter, delete, or hallucinate factual information (numbers, dates, history). Only change formatting (Markdown, HTML colors, symbols, section structure). Core information MUST remain exactly the same. Confidence: 0.80

