package com.example.data

import com.example.firebase.FirestoreSyncService
import com.example.firebase.FirestoreSubscription
import com.example.firebase.FirestoreMegaQuizPayment
import kotlinx.coroutines.flow.Flow

/**
 * Cloud-synced repository that wraps local Room DB and Firestore.
 * Data flows: Firestore → Room → UI.
 * User results flow: UI → Room + push to Firestore.
 */
class FirebaseCloudRepository(
    private val localRepo: GKRepository,
    private val cloudSync: FirestoreSyncService = FirestoreSyncService()
) {

    // ── Sync from Firestore to Room ─────────────────────

    suspend fun syncAllFromCloud() {
        try {
            // Sync main topics
            val bdTopics = cloudSync.pullMainTopics("BANGLADESH")
            val intTopics = cloudSync.pullMainTopics("INTERNATIONAL")
            if (bdTopics.isNotEmpty() || intTopics.isNotEmpty()) {
                localRepo.insertMainTopics(bdTopics + intTopics)
            }

            // Sync sub topics
            val allSubTopics = cloudSync.pullAllSubTopics()
            if (allSubTopics.isNotEmpty()) {
                localRepo.insertSubTopics(allSubTopics)
            }

            // Sync MCQs
            val allMcqs = cloudSync.pullAllMCQs()
            if (allMcqs.isNotEmpty()) {
                localRepo.insertMCQs(allMcqs)
            }

            // Sync mega exams + questions
            val megaExams = cloudSync.pullMegaExams()
            if (megaExams.isNotEmpty()) {
                localRepo.insertMegaQuizExams(megaExams)
            }

            val allMegaQs = cloudSync.pullAllMegaQuestions()
            if (allMegaQs.isNotEmpty()) {
                localRepo.insertMegaQuizQuestions(allMegaQs)
            }

            // Sync universities + questions (up to 3 years)
            val unis = cloudSync.pullUniversities()
            if (unis.isNotEmpty()) {
                localRepo.insertUniversities(unis)
            }
            for (uni in unis) {
                for (year in (2020..2026)) {
                    val uqs = cloudSync.pullUniversityQuestions(uni.id, year)
                    if (uqs.isNotEmpty()) {
                        localRepo.insertUniversityQuestions(uqs)
                    }
                }
            }
        } catch (_: Exception) {
            // Offline — use local data only
        }
    }

    // ── Push results to cloud ──────────────────────────

    suspend fun saveMegaResultAndSync(result: MegaQuizResultEntity) {
        localRepo.insertMegaQuizResult(result)
        try { cloudSync.saveMegaResult(result) } catch (_: Exception) {}
    }

    suspend fun savePracticeProgressAndSync(progress: MCQPracticeProgressEntity) {
        localRepo.insertProgress(progress)
        try { cloudSync.savePracticeProgress(progress) } catch (_: Exception) {}
    }

    // ── Existing local flows (pass-through) ─────────────

    val bdTopics: Flow<List<GKMainTopicEntity>> get() = localRepo.getMainTopics("BANGLADESH")
    val intTopics: Flow<List<GKMainTopicEntity>> get() = localRepo.getMainTopics("INTERNATIONAL")
    fun getSubTopics(topicId: String): Flow<List<GKSubTopicEntity>> = localRepo.getSubTopics(topicId)
    fun getSubTopicById(subTopicId: String): Flow<GKSubTopicEntity?> = localRepo.getSubTopicById(subTopicId)
    fun getMCQsForSubTopic(subTopicId: String): Flow<List<MCQQuestionEntity>> = localRepo.getMCQsForSubTopic(subTopicId)
    fun getRecentGK(category: String): Flow<List<RecentGKEntity>> = localRepo.getRecentGK(category)
    fun getMegaQuizExams(): Flow<List<MegaQuizExamEntity>> = localRepo.getMegaQuizExams()
    fun getMegaQuizQuestions(examId: String): Flow<List<MegaQuizQuestionEntity>> = localRepo.getMegaQuizQuestions(examId)
    fun getLatestMegaQuizScore(): Flow<Double?> = localRepo.getLatestMegaQuizScore()
    fun getTotalTopicsCount(): Flow<Int> = localRepo.getTotalTopicsCount()
    fun getTotalSubTopicsCount(): Flow<Int> = localRepo.getTotalSubTopicsCount()
    fun getTotalPracticeMCQCount(): Flow<Int> = localRepo.getTotalPracticeMCQCount()
    fun getTotalMegaQuizCount(): Flow<Int> = localRepo.getTotalMegaQuizCount()
    fun getUniversities(): Flow<List<UniversityEntity>> = localRepo.getUniversities()
    fun getYearsForUniversity(universityId: String): Flow<List<Int>> = localRepo.getYearsForUniversity(universityId)
    fun getUniversityQuestions(universityId: String, year: Int): Flow<List<UniversityQuestionEntity>> = localRepo.getUniversityQuestions(universityId, year)
    fun getProgressForDate(date: String): Flow<MCQPracticeProgressEntity?> = localRepo.getProgressForDate(date)
    fun getMonthlyProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>> = localRepo.getMonthlyProgress(monthPrefix)
    fun getYearlyProgress(yearPrefix: String): Flow<List<MCQPracticeProgressEntity>> = localRepo.getYearlyProgress(yearPrefix)
    fun getAllProgress(): Flow<List<MCQPracticeProgressEntity>> = localRepo.getAllProgress()

    // ── Subscription (Firestore) ────────────────────────

    suspend fun getSubscription(uid: String): FirestoreSubscription? = cloudSync.getSubscription(uid)

    suspend fun saveSubscription(sub: FirestoreSubscription) = cloudSync.saveSubscription(sub)

    // ── Mega Quiz Payment (Firestore) ───────────────────

    suspend fun hasPaidForExam(uid: String, examId: String): Boolean = cloudSync.hasPaidForExam(uid, examId)

    suspend fun saveMegaQuizPayment(payment: FirestoreMegaQuizPayment) = cloudSync.saveMegaQuizPayment(payment)
}
