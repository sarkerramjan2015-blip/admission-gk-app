package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gk_main_topics")
data class GKMainTopicEntity(
    @PrimaryKey val id: String,
    val category: String, // BANGLADESH, INTERNATIONAL
    val title: String,
    val orderIndex: Int,
    val iconName: String
)

@Entity(tableName = "gk_sub_topics")
data class GKSubTopicEntity(
    @PrimaryKey val id: String,
    val parentTopicId: String,
    val title: String,
    val orderIndex: Int,
    val note: String,
    val importantFacts: String, // JSON list
    val confusionClearance: String,
    val referenceLinks: String // JSON list
)

@Entity(tableName = "mcq_questions")
data class MCQQuestionEntity(
    @PrimaryKey val id: String,
    val subTopicId: String,
    val questionText: String,
    val options: String, // JSON list of strings
    val correctAnswer: String,
    val explanation: String,
    val sourceExam: String,
    val year: String,
    val referenceText: String,
    val status: String // PENDING, APPROVED, REJECTED
)

@Entity(tableName = "recent_gk")
data class RecentGKEntity(
    @PrimaryKey val id: String,
    val category: String, // BD, INT
    val topicTitle: String,
    val specialTopicNote: String,
    val sourceText: String,
    val contributorName: String,
    val status: String,
    val createdAt: Long,
    val approvedAt: Long?
)

@Entity(tableName = "mega_quiz_exams")
data class MegaQuizExamEntity(
    @PrimaryKey val id: String,
    val title: String,
    val examDate: Long,
    val status: String // UPCOMING, LIVE, COMPLETED
)

@Entity(tableName = "mega_quiz_questions")
data class MegaQuizQuestionEntity(
    @PrimaryKey val id: String,
    val examId: String,
    val questionText: String,
    val options: String, // JSON list
    val correctAnswer: String,
    val explanation: String
)

@Entity(tableName = "mega_quiz_results")
data class MegaQuizResultEntity(
    @PrimaryKey val id: String,
    val examId: String,
    val score: Double,
    val correctCount: Int,
    val wrongCount: Int,
    val unansweredCount: Int,
    val dateTaken: Long
)

@Entity(tableName = "universities")
data class UniversityEntity(
    @PrimaryKey val id: String,
    val shortName: String,
    val fullName: String,
    val logoResName: String,
    val orderIndex: Int
)

@Entity(tableName = "university_questions")
data class UniversityQuestionEntity(
    @PrimaryKey val id: String,
    val universityId: String,
    val year: Int,
    val unitName: String,
    val subject: String,
    val questionText: String,
    val options: String, // JSON list
    val correctAnswer: String,
    val explanation: String,
    val referenceText: String
)

@Entity(tableName = "mcq_practice_progress")
data class MCQPracticeProgressEntity(
    @PrimaryKey val dateStr: String, // e.g. "2023-10-27"
    val totalPracticed: Int,
    val correctCount: Int,
    val wrongCount: Int
)

@Entity(tableName = "mcq_quiz_results")
data class MCQQuizResultEntity(
    @PrimaryKey val id: String,
    val subTopicId: String,
    val scorePercentage: Double,
    val dateTaken: Long
)
