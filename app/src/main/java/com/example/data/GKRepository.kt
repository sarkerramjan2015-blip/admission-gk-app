package com.example.data

import kotlinx.coroutines.flow.Flow

class GKRepository(private val gkDao: GKDao) {

    fun getMainTopics(category: String): Flow<List<GKMainTopicEntity>> = gkDao.getMainTopics(category)
    fun getSubTopics(topicId: String): Flow<List<GKSubTopicEntity>> = gkDao.getSubTopics(topicId)
    fun getSubTopicById(subTopicId: String): Flow<GKSubTopicEntity?> = gkDao.getSubTopicById(subTopicId)
    fun getMCQsForSubTopic(subTopicId: String): Flow<List<MCQQuestionEntity>> = gkDao.getMCQsForSubTopic(subTopicId)
    fun getRecentGK(category: String): Flow<List<RecentGKEntity>> = gkDao.getRecentGK(category)
    fun getMegaQuizExams(): Flow<List<MegaQuizExamEntity>> = gkDao.getMegaQuizExams()
    fun getMegaQuizQuestions(examId: String): Flow<List<MegaQuizQuestionEntity>> = gkDao.getMegaQuizQuestions(examId)
    fun getUniversities(): Flow<List<UniversityEntity>> = gkDao.getUniversities()
    fun getYearsForUniversity(universityId: String): Flow<List<Int>> = gkDao.getYearsForUniversity(universityId)
    fun getUniversityQuestions(universityId: String, year: Int): Flow<List<UniversityQuestionEntity>> = gkDao.getUniversityQuestions(universityId, year)
    
    fun getProgressForDate(dateStr: String): Flow<MCQPracticeProgressEntity?> = gkDao.getProgressForDate(dateStr)
    fun getBestQuizResult(subTopicId: String): Flow<MCQQuizResultEntity?> = gkDao.getBestQuizResult(subTopicId)
    fun getLatestMegaQuizScore(): Flow<Double?> = gkDao.getLatestMegaQuizScore()
    
    fun getTotalTopicsCount(): Flow<Int> = gkDao.getTotalTopicsCount()
    fun getTotalSubTopicsCount(): Flow<Int> = gkDao.getTotalSubTopicsCount()
    fun getTotalPracticeMCQCount(): Flow<Int> = gkDao.getTotalPracticeMCQCount()
    fun getTotalMegaQuizCount(): Flow<Int> = gkDao.getTotalMegaQuizCount()
    fun getMonthlyProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>> = gkDao.getMonthlyProgress(monthPrefix)
    fun getYearlyProgress(yearPrefix: String): Flow<List<MCQPracticeProgressEntity>> = gkDao.getYearlyProgress(yearPrefix)
    fun getAllProgress(): Flow<List<MCQPracticeProgressEntity>> = gkDao.getAllProgress()

    suspend fun insertMainTopics(topics: List<GKMainTopicEntity>) = gkDao.insertMainTopics(topics)
    suspend fun insertSubTopics(topics: List<GKSubTopicEntity>) = gkDao.insertSubTopics(topics)
    suspend fun insertMCQs(mcqs: List<MCQQuestionEntity>) = gkDao.insertMCQs(mcqs)
    suspend fun insertMCQ(mcq: MCQQuestionEntity) = gkDao.insertMCQ(mcq)
    suspend fun insertRecentGK(items: List<RecentGKEntity>) = gkDao.insertRecentGK(items)
    suspend fun insertSingleRecentGK(item: RecentGKEntity) = gkDao.insertSingleRecentGK(item)
    suspend fun insertMegaQuizExams(exams: List<MegaQuizExamEntity>) = gkDao.insertMegaQuizExams(exams)
    suspend fun insertMegaQuizQuestions(questions: List<MegaQuizQuestionEntity>) = gkDao.insertMegaQuizQuestions(questions)
    suspend fun insertUniversities(universities: List<UniversityEntity>) = gkDao.insertUniversities(universities)
    suspend fun insertUniversityQuestions(questions: List<UniversityQuestionEntity>) = gkDao.insertUniversityQuestions(questions)
    
    suspend fun insertProgress(progress: MCQPracticeProgressEntity) = gkDao.insertProgress(progress)
    suspend fun insertQuizResult(result: MCQQuizResultEntity) = gkDao.insertQuizResult(result)
    suspend fun insertMegaQuizResult(result: MegaQuizResultEntity) = gkDao.insertMegaQuizResult(result)

    // ── Recent GK Admin ────────────────────────────────
    fun getRecentGKByStatus(status: String): Flow<List<RecentGKEntity>> = gkDao.getRecentGKByStatus(status)
    fun getAllRecentGK(): Flow<List<RecentGKEntity>> = gkDao.getAllRecentGK()
    suspend fun updateRecentGKStatus(id: String, status: String) = gkDao.updateRecentGKStatus(id, status, System.currentTimeMillis())

    // ── Dashboard Analytics ────────────────────────────
    fun getTotalPracticedMCQs(): Flow<Int> = gkDao.getTotalPracticedMCQs()
    fun getTotalCorrectMCQs(): Flow<Int> = gkDao.getTotalCorrectMCQs()
    fun getTotalQuizAttempts(): Flow<Int> = gkDao.getTotalQuizAttempts()
    fun getTotalMegaQuizAttempts(): Flow<Int> = gkDao.getTotalMegaQuizAttempts()
    fun getCurrentMonthProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>> = gkDao.getCurrentMonthProgress(monthPrefix)
    fun getRecentQuizResults(): Flow<List<MCQQuizResultEntity>> = gkDao.getRecentQuizResults()
    fun getRecentMegaQuizResults(): Flow<List<MegaQuizResultEntity>> = gkDao.getRecentMegaQuizResults()
    fun getTotalActiveDays(): Flow<Int> = gkDao.getTotalActiveDays()
    fun getCurrentMonthActiveDays(monthPrefix: String): Flow<Int> = gkDao.getCurrentMonthActiveDays(monthPrefix)
    fun getWeekProgress(weekStart: String): Flow<List<MCQPracticeProgressEntity>> = gkDao.getWeekProgress(weekStart)
}
