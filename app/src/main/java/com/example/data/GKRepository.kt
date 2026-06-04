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
}
