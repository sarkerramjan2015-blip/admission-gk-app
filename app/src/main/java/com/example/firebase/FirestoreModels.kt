package com.example.firebase

/**
 * Firestore document model for GK Main Topics.
 */
data class FirestoreMainTopic(
    val id: String = "",
    val category: String = "",
    val title: String = "",
    val orderIndex: Int = 0,
    val iconName: String = ""
)

data class FirestoreSubTopic(
    val id: String = "",
    val parentTopicId: String = "",
    val title: String = "",
    val orderIndex: Int = 0,
    val note: String = "",
    val importantFacts: String = "[]",
    val confusionClearance: String = "",
    val referenceLinks: String = "[]"
)

data class FirestoreMcq(
    val id: String = "",
    val subTopicId: String = "",
    val questionText: String = "",
    val options: String = "[]",
    val correctAnswer: String = "",
    val explanation: String = "",
    val sourceExam: String = "",
    val year: String = "",
    val referenceBook: String = "",
    val status: String = "APPROVED"
)

data class FirestoreMegaExam(
    val id: String = "",
    val title: String = "",
    val examDate: Long = 0L,
    val status: String = "UPCOMING"
)

data class FirestoreMegaQuestion(
    val id: String = "",
    val examId: String = "",
    val questionText: String = "",
    val options: String = "[]",
    val correctAnswer: String = "",
    val explanation: String = ""
)

data class FirestoreMegaResult(
    val id: String = "",
    val examId: String = "",
    val score: Double = 0.0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unansweredCount: Int = 0,
    val dateTaken: Long = 0L
)

data class FirestoreRecentGK(
    val id: String = "",
    val category: String = "",
    val topicTitle: String = "",
    val specialTopicNote: String = "",
    val source: String = "",
    val contributor: String = "",
    val status: String = "APPROVED",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

data class FirestoreUniversity(
    val id: String = "",
    val shortCode: String = "",
    val fullName: String = "",
    val logoUrl: String = "",
    val orderIndex: Int = 0
)

data class FirestoreUniversityQuestion(
    val id: String = "",
    val universityId: String = "",
    val year: Int = 0,
    val unit: String = "",
    val subject: String = "",
    val questionText: String = "",
    val options: String = "[]",
    val correctAnswer: String = "",
    val explanation: String = "",
    val sourceUrl: String = ""
)

// ── Subscription ─────────────────────────────────────
data class FirestoreSubscription(
    val uid: String = "",
    val isTrialActive: Boolean = true,
    val trialStartDate: Long = 0L,
    val trialEndDate: Long = 0L,
    val isSubscribed: Boolean = false,
    val planType: String = "NONE",
    val subscriptionStartDate: Long? = null,
    val subscriptionEndDate: Long? = null,
    val purchaseAmount: Int = 0
)

// ── Mega Quiz Payment (per-quiz) ────────────────────
data class FirestoreMegaQuizPayment(
    val id: String = "",
    val uid: String = "",
    val examId: String = "",
    val amount: Int = 20,
    val paidAt: Long = 0L
)
