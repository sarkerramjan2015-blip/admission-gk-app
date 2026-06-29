package com.example.firebase

import com.example.data.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

/**
 * Cloud Firestore sync service.
 * Provides methods to pull data from Firestore and push user results.
 */
class FirestoreSyncService {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // ── Pull (Read from Firestore) ──────────────────────

    suspend fun pullMainTopics(category: String): List<GKMainTopicEntity> {
        return db.collection("gk_main_topics")
            .whereEqualTo("category", category)
            .orderBy("orderIndex")
            .get()
            .await()
            .toObjects(FirestoreMainTopic::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullSubTopics(parentTopicId: String): List<GKSubTopicEntity> {
        return db.collection("gk_sub_topics")
            .whereEqualTo("parentTopicId", parentTopicId)
            .orderBy("orderIndex")
            .get()
            .await()
            .toObjects(FirestoreSubTopic::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullAllSubTopics(): List<GKSubTopicEntity> {
        return db.collection("gk_sub_topics")
            .get()
            .await()
            .toObjects(FirestoreSubTopic::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullMCQs(subTopicId: String): List<MCQQuestionEntity> {
        return db.collection("mcq_questions")
            .whereEqualTo("subTopicId", subTopicId)
            .whereEqualTo("status", "APPROVED")
            .get()
            .await()
            .toObjects(FirestoreMcq::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullAllMCQs(): List<MCQQuestionEntity> {
        return db.collection("mcq_questions")
            .whereEqualTo("status", "APPROVED")
            .get()
            .await()
            .toObjects(FirestoreMcq::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullMegaExams(): List<MegaQuizExamEntity> {
        return db.collection("mega_quiz_exams")
            .orderBy("examDate")
            .get()
            .await()
            .toObjects(FirestoreMegaExam::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullMegaQuestions(examId: String): List<MegaQuizQuestionEntity> {
        return db.collection("mega_quiz_questions")
            .whereEqualTo("examId", examId)
            .get()
            .await()
            .toObjects(FirestoreMegaQuestion::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullAllMegaQuestions(): List<MegaQuizQuestionEntity> {
        return db.collection("mega_quiz_questions")
            .get()
            .await()
            .toObjects(FirestoreMegaQuestion::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullRecentGK(category: String): List<RecentGKEntity> {
        return db.collection("recent_gk")
            .whereEqualTo("category", category)
            .whereEqualTo("status", "APPROVED")
            .orderBy("createdAt")
            .get()
            .await()
            .toObjects(FirestoreRecentGK::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullUniversities(): List<UniversityEntity> {
        return db.collection("universities")
            .orderBy("orderIndex")
            .get()
            .await()
            .toObjects(FirestoreUniversity::class.java)
            .map { it.toEntity() }
    }

    suspend fun pullUniversityQuestions(universityId: String, year: Int): List<UniversityQuestionEntity> {
        return db.collection("university_questions")
            .whereEqualTo("universityId", universityId)
            .whereEqualTo("year", year)
            .get()
            .await()
            .toObjects(FirestoreUniversityQuestion::class.java)
            .map { it.toEntity() }
    }

    // ── Push (Write to Firestore) ──────────────────────

    suspend fun saveMegaResult(result: MegaQuizResultEntity) {
        db.collection("mega_quiz_results")
            .document(result.id)
            .set(FirestoreMegaResult(
                id = result.id,
                examId = result.examId,
                score = result.score,
                correctCount = result.correctCount,
                wrongCount = result.wrongCount,
                unansweredCount = result.unansweredCount,
                dateTaken = result.dateTaken
            ))
            .await()
    }

    suspend fun savePracticeProgress(progress: MCQPracticeProgressEntity) {
        val data = mapOf(
            "dateStr" to progress.dateStr,
            "totalPracticed" to progress.totalPracticed,
            "correctCount" to progress.correctCount,
            "wrongCount" to progress.wrongCount
        )
        db.collection("mcq_practice_progress")
            .document(progress.dateStr)
            .set(data)
            .await()
    }

    // ── Subscription ──────────────────────────────────

    suspend fun getSubscription(uid: String): FirestoreSubscription? {
        val doc = db.collection("subscriptions").document(uid).get().await()
        return if (doc.exists()) doc.toObject(FirestoreSubscription::class.java) else null
    }

    suspend fun saveSubscription(sub: FirestoreSubscription) {
        db.collection("subscriptions").document(sub.uid).set(sub).await()
    }

    // ── Mega Quiz Payment ─────────────────────────────

    suspend fun getMegaQuizPayment(uid: String, examId: String): FirestoreMegaQuizPayment? {
        val id = "${uid}_${examId}"
        val doc = db.collection("mega_quiz_payments").document(id).get().await()
        return if (doc.exists()) doc.toObject(FirestoreMegaQuizPayment::class.java) else null
    }

    suspend fun saveMegaQuizPayment(payment: FirestoreMegaQuizPayment) {
        db.collection("mega_quiz_payments").document(payment.id).set(payment).await()
    }

    suspend fun hasPaidForExam(uid: String, examId: String): Boolean {
        return getMegaQuizPayment(uid, examId) != null
    }

    // ── Mapping Extensions ──────────────────────────────

    private fun FirestoreMainTopic.toEntity() = GKMainTopicEntity(id, category, title, orderIndex, iconName)
    private fun FirestoreSubTopic.toEntity() = GKSubTopicEntity(id, parentTopicId, title, orderIndex, note, importantFacts, confusionClearance, referenceLinks)
    private fun FirestoreMcq.toEntity(): MCQQuestionEntity {
        return MCQQuestionEntity(
            id = id,
            subTopicId = subTopicId,
            questionText = questionText,
            options = options,
            correctAnswer = correctAnswer,
            explanation = explanation,
            sourceExam = sourceExam,
            year = year,
            referenceText = referenceBook,
            status = status
        )
    }
    private fun FirestoreMegaExam.toEntity() = MegaQuizExamEntity(
        id, title, examDate, status
    )
    private fun FirestoreMegaQuestion.toEntity() = MegaQuizQuestionEntity(
        id, examId, questionText, options, correctAnswer, explanation, relatedSubTopicId
    )
    private fun FirestoreRecentGK.toEntity(): RecentGKEntity {
        return RecentGKEntity(
            id = id,
            category = category,
            topicTitle = topicTitle,
            specialTopicNote = specialTopicNote,
            sourceText = source,
            contributorName = contributor,
            status = status,
            createdAt = createdAt,
            approvedAt = updatedAt
        )
    }
    private fun FirestoreUniversity.toEntity(): UniversityEntity {
        return UniversityEntity(
            id = id,
            shortName = shortCode,
            fullName = fullName,
            logoResName = logoUrl,
            orderIndex = orderIndex
        )
    }
    private fun FirestoreUniversityQuestion.toEntity(): UniversityQuestionEntity {
        return UniversityQuestionEntity(
            id = id,
            universityId = universityId,
            year = year,
            unitName = unit,
            subject = subject,
            questionText = questionText,
            options = options,
            correctAnswer = correctAnswer,
            explanation = explanation,
            referenceText = sourceUrl
        )
    }

    // Helper: convert QuerySnapshot to List<T>
    private inline fun <reified T> QuerySnapshot.toObjects(clazz: Class<T>): List<T> {
        return documents.mapNotNull { it.toObject(clazz) }
    }
}
