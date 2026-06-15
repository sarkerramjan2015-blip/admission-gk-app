package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GKDao {
    @Query("SELECT * FROM gk_main_topics WHERE category = :category ORDER BY orderIndex ASC")
    fun getMainTopics(category: String): Flow<List<GKMainTopicEntity>>

    @Query("SELECT * FROM gk_sub_topics WHERE parentTopicId = :topicId ORDER BY orderIndex ASC")
    fun getSubTopics(topicId: String): Flow<List<GKSubTopicEntity>>

    @Query("SELECT * FROM gk_sub_topics WHERE id = :subTopicId")
    fun getSubTopicById(subTopicId: String): Flow<GKSubTopicEntity?>

    @Query("SELECT * FROM mcq_questions WHERE subTopicId = :subTopicId AND status = 'APPROVED'")
    fun getMCQsForSubTopic(subTopicId: String): Flow<List<MCQQuestionEntity>>

    @Query("SELECT * FROM recent_gk WHERE category = :category AND status = 'APPROVED' ORDER BY createdAt DESC")
    fun getRecentGK(category: String): Flow<List<RecentGKEntity>>

    @Query("SELECT * FROM mega_quiz_exams ORDER BY examDate DESC")
    fun getMegaQuizExams(): Flow<List<MegaQuizExamEntity>>

    @Query("SELECT * FROM mega_quiz_questions WHERE examId = :examId")
    fun getMegaQuizQuestions(examId: String): Flow<List<MegaQuizQuestionEntity>>

    @Query("SELECT * FROM universities ORDER BY orderIndex ASC")
    fun getUniversities(): Flow<List<UniversityEntity>>

    @Query("SELECT DISTINCT year FROM university_questions WHERE universityId = :universityId ORDER BY year DESC")
    fun getYearsForUniversity(universityId: String): Flow<List<Int>>

    @Query("SELECT * FROM university_questions WHERE universityId = :universityId AND year = :year")
    fun getUniversityQuestions(universityId: String, year: Int): Flow<List<UniversityQuestionEntity>>
    
    @Query("SELECT * FROM mcq_practice_progress WHERE dateStr = :dateStr")
    fun getProgressForDate(dateStr: String): Flow<MCQPracticeProgressEntity?>

    @Query("SELECT * FROM mcq_quiz_results WHERE subTopicId = :subTopicId ORDER BY scorePercentage DESC LIMIT 1")
    fun getBestQuizResult(subTopicId: String): Flow<MCQQuizResultEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainTopics(topics: List<GKMainTopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTopics(topics: List<GKSubTopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMCQs(mcqs: List<MCQQuestionEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMCQ(mcq: MCQQuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentGK(items: List<RecentGKEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleRecentGK(item: RecentGKEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMegaQuizExams(exams: List<MegaQuizExamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMegaQuizQuestions(questions: List<MegaQuizQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversityQuestions(questions: List<UniversityQuestionEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: MCQPracticeProgressEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: MCQQuizResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMegaQuizResult(result: MegaQuizResultEntity)
    
    @Query("SELECT score FROM mega_quiz_results ORDER BY dateTaken DESC LIMIT 1")
    fun getLatestMegaQuizScore(): Flow<Double?>
    
    @Query("SELECT COUNT(*) FROM gk_main_topics")
    fun getTotalTopicsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM gk_sub_topics")
    fun getTotalSubTopicsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM mcq_questions WHERE status = 'APPROVED'")
    fun getTotalPracticeMCQCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM mega_quiz_exams")
    fun getTotalMegaQuizCount(): Flow<Int>
    
    @Query("SELECT * FROM mcq_practice_progress WHERE dateStr LIKE :monthPrefix ORDER BY dateStr ASC")
    fun getMonthlyProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>>
    
    @Query("SELECT * FROM mcq_practice_progress WHERE dateStr LIKE :yearPrefix ORDER BY dateStr ASC")
    fun getYearlyProgress(yearPrefix: String): Flow<List<MCQPracticeProgressEntity>>
    
    @Query("SELECT * FROM mcq_practice_progress ORDER BY dateStr ASC")
    fun getAllProgress(): Flow<List<MCQPracticeProgressEntity>>

    // ── Recent GK Admin ────────────────────────────────
    @Query("SELECT * FROM recent_gk WHERE status = :status ORDER BY createdAt DESC")
    fun getRecentGKByStatus(status: String): Flow<List<RecentGKEntity>>

    @Query("SELECT * FROM recent_gk ORDER BY createdAt DESC")
    fun getAllRecentGK(): Flow<List<RecentGKEntity>>

    @Query("UPDATE recent_gk SET status = :status, approvedAt = :timestamp WHERE id = :id")
    suspend fun updateRecentGKStatus(id: String, status: String, timestamp: Long)

    // ── Dashboard Analytics ────────────────────────────
    @Query("SELECT COALESCE(SUM(totalPracticed), 0) FROM mcq_practice_progress")
    fun getTotalPracticedMCQs(): Flow<Int>

    @Query("SELECT COALESCE(SUM(correctCount), 0) FROM mcq_practice_progress")
    fun getTotalCorrectMCQs(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mcq_quiz_results")
    fun getTotalQuizAttempts(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mega_quiz_results")
    fun getTotalMegaQuizAttempts(): Flow<Int>

    @Query("SELECT * FROM mcq_practice_progress WHERE dateStr LIKE :monthPrefix ORDER BY dateStr ASC")
    fun getCurrentMonthProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>>

    @Query("SELECT * FROM mcq_quiz_results ORDER BY dateTaken DESC LIMIT 5")
    fun getRecentQuizResults(): Flow<List<MCQQuizResultEntity>>

    @Query("SELECT * FROM mega_quiz_results ORDER BY dateTaken DESC LIMIT 5")
    fun getRecentMegaQuizResults(): Flow<List<MegaQuizResultEntity>>

    @Query("SELECT COUNT(DISTINCT dateStr) FROM mcq_practice_progress WHERE totalPracticed > 0")
    fun getTotalActiveDays(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT dateStr) FROM mcq_practice_progress WHERE totalPracticed > 0 AND dateStr LIKE :monthPrefix")
    fun getCurrentMonthActiveDays(monthPrefix: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(totalPracticed), 0) FROM mcq_practice_progress")
    fun getTotalPracticedMCQsOnce(): Int

    @Query("SELECT * FROM mcq_practice_progress WHERE dateStr >= :weekStart ORDER BY dateStr ASC")
    fun getWeekProgress(weekStart: String): Flow<List<MCQPracticeProgressEntity>>
}
