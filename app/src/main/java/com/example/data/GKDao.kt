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
}
