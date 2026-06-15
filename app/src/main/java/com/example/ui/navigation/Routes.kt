package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable data object SplashRoute
@Serializable data object HomeRoute
@Serializable data class CategoryRoute(val category: String, val title: String)
@Serializable data class TopicListRoute(val topicId: String, val title: String)
@Serializable data class SubTopicDetailRoute(val subTopicId: String, val title: String)
@Serializable data class MCQQuizRoute(val subTopicId: String)
@Serializable data class MCQQuizLiveRoute(val subTopicId: String)
@Serializable data class MCQQuizResultRoute(
    val subTopicId: String, 
    val score: Float, 
    val total: Int, 
    val correct: Int, 
    val wrong: Int, 
    val timeTakenSeconds: Int
)
@Serializable data class MegaQuizIntroRoute(val examId: String)
@Serializable data class MegaQuizLiveRoute(val examId: String)
@Serializable data class MegaQuizSubmittedRoute(val total: Int, val answered: Int, val unanswered: Int)
@Serializable data class MegaQuizResultRoute(val score: Double, val total: Int, val right: Int, val wrong: Int)
@Serializable data class QuestionBankRoute(val uniId: String? = null, val year: Int? = null)
@Serializable data object RecentGKRoute
@Serializable data object LoginRoute
@Serializable data object DashboardRoute
@Serializable data class MCQPracticeRoute(val subTopicId: String)
@Serializable data object ProfileRoute
@Serializable data object MegaQuizRoutineRoute
@Serializable data object AdminDashboardRoute
@Serializable data object ProgressReportRoute
