package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.content.SharedPreferences
import com.example.data.GKMainTopicEntity
import com.example.data.GKRepository
import com.example.data.MCQPracticeProgressEntity
import com.example.data.MCQQuizResultEntity
import com.example.data.GKSubTopicEntity
import com.example.data.MCQQuestionEntity
import com.example.data.MegaQuizExamEntity
import com.example.data.MegaQuizQuestionEntity
import com.example.data.MegaQuizResultEntity
import com.example.data.RecentGKEntity
import com.example.data.UniversityEntity
import com.example.data.UniversityQuestionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

class GKViewModel(private val repository: GKRepository, private val uid: String) : ViewModel() {
    // ── Subscription (local SharedPrefs) ──────────
    data class LocalSubscription(
        val isTrialActive: Boolean = false,
        val trialEndDate: Long = 0L,
        val isSubscribed: Boolean = false,
        val planType: String = "NONE",
        val subscriptionEndDate: Long = 0L,
        val purchaseAmount: Int = 0
    )

    private var subPrefs: SharedPreferences? = null

    fun setContext(ctx: Context) {
        subPrefs = ctx.getSharedPreferences("sub_$uid", Context.MODE_PRIVATE)
        _subscription.value = loadSubscription()
    }

    private fun loadSubscription(): LocalSubscription {
        val p = subPrefs ?: return LocalSubscription()
        return LocalSubscription(
            isTrialActive = p.getBoolean("isTrialActive", false),
            trialEndDate = p.getLong("trialEndDate", 0L),
            isSubscribed = p.getBoolean("isSubscribed", false),
            planType = p.getString("planType", "NONE") ?: "NONE",
            subscriptionEndDate = p.getLong("subscriptionEndDate", 0L),
            purchaseAmount = p.getInt("purchaseAmount", 0)
        )
    }

    private fun saveSubscription(sub: LocalSubscription) {
        subPrefs?.edit()?.apply {
            putBoolean("isTrialActive", sub.isTrialActive)
            putLong("trialEndDate", sub.trialEndDate)
            putBoolean("isSubscribed", sub.isSubscribed)
            putString("planType", sub.planType)
            putLong("subscriptionEndDate", sub.subscriptionEndDate)
            putInt("purchaseAmount", sub.purchaseAmount)
            apply()
        }
    }

    private val todayDateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    val todayProgress: StateFlow<MCQPracticeProgressEntity?> = repository.getProgressForDate(todayDateStr)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Home Page Data
    val bdTopics: StateFlow<List<GKMainTopicEntity>> = repository.getMainTopics("BANGLADESH")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val intTopics: StateFlow<List<GKMainTopicEntity>> = repository.getMainTopics("INTERNATIONAL")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentGKBD: StateFlow<List<RecentGKEntity>> = repository.getRecentGK("BD")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentGKInt: StateFlow<List<RecentGKEntity>> = repository.getRecentGK("INT")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val megaQuizExams: StateFlow<List<MegaQuizExamEntity>> = repository.getMegaQuizExams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val totalTopicsCount: StateFlow<Int> = repository.getTotalTopicsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val totalSubTopicsCount: StateFlow<Int> = repository.getTotalSubTopicsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val totalPracticeMCQCount: StateFlow<Int> = repository.getTotalPracticeMCQCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val totalMegaQuizCount: StateFlow<Int> = repository.getTotalMegaQuizCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ── Dashboard Analytics ────────────────────────────
    val totalPracticedMCQs: StateFlow<Int> = repository.getTotalPracticedMCQs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCorrectMCQs: StateFlow<Int> = repository.getTotalCorrectMCQs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalQuizAttempts: StateFlow<Int> = repository.getTotalQuizAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMegaQuizAttempts: StateFlow<Int> = repository.getTotalMegaQuizAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalActiveDays: StateFlow<Int> = repository.getTotalActiveDays()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val weekProgress: StateFlow<List<MCQPracticeProgressEntity>> = repository.getWeekProgress(
        LocalDate.now().with(java.time.DayOfWeek.MONDAY).format(DateTimeFormatter.ISO_LOCAL_DATE)
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentQuizResults: StateFlow<List<MCQQuizResultEntity>> = repository.getRecentQuizResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentMegaQuizResults: StateFlow<List<MegaQuizResultEntity>> = repository.getRecentMegaQuizResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentMonthProgress: StateFlow<List<MCQPracticeProgressEntity>> = repository.getCurrentMonthProgress(
        java.time.YearMonth.now().toString() + "-"
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentMonthActiveDays: StateFlow<Int> = repository.getCurrentMonthActiveDays(
        java.time.YearMonth.now().toString() + "-"
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ── Subscription State ─────────────────────
    private val _subscription = MutableStateFlow<LocalSubscription?>(null)
    val subscription: StateFlow<LocalSubscription?> = _subscription

    fun startTrial() {
        val now = System.currentTimeMillis()
        val thirtyDaysMs = 30L * 24 * 60 * 60 * 1000
        val sub = LocalSubscription(isTrialActive = true, trialEndDate = now + thirtyDaysMs)
        saveSubscription(sub)
        _subscription.value = sub
    }

    fun purchasePlan(planType: String, amount: Int, durationDays: Long) {
        val now = System.currentTimeMillis()
        val endDate = if (durationDays < 0) Long.MAX_VALUE else now + (durationDays * 24 * 60 * 60 * 1000)
        val sub = LocalSubscription(
            isTrialActive = false,
            isSubscribed = true,
            planType = planType,
            subscriptionEndDate = endDate,
            purchaseAmount = amount
        )
        saveSubscription(sub)
        _subscription.value = sub
    }

    // ── Mega Quiz Payment (local) ──────────────
    private val megaQuizPayments = mutableSetOf<String>()

    fun payForExam(examId: String, onPaid: () -> Unit) {
        megaQuizPayments.add(examId)
        onPaid()
    }

    fun hasPaidForExam(examId: String): Boolean {
        return megaQuizPayments.contains(examId)
    }

    fun isPremiumActive(sub: LocalSubscription?): Boolean {
        if (sub == null) return false
        if (sub.isSubscribed && sub.subscriptionEndDate > System.currentTimeMillis()) return true
        if (sub.isTrialActive && sub.trialEndDate > System.currentTimeMillis()) return true
        return false
    }

    fun getSubscriptionStatus(sub: LocalSubscription?): String {
        if (sub == null) return "FREE_USER"
        if (sub.isSubscribed && sub.subscriptionEndDate > System.currentTimeMillis()) return sub.planType
        if (sub.isTrialActive && sub.trialEndDate > System.currentTimeMillis()) return "TRIAL"
        return "EXPIRED"
    }

    fun getTrialRemainingDays(sub: LocalSubscription?): Long {
        if (sub == null || !sub.isTrialActive) return 0
        val remaining = sub.trialEndDate - System.currentTimeMillis()
        return if (remaining > 0) remaining / (24 * 60 * 60 * 1000) else 0
    }

    fun getSubscriptionRemainingDays(sub: LocalSubscription?): Long {
        if (sub == null || !sub.isSubscribed) return 0
        val remaining = sub.subscriptionEndDate - System.currentTimeMillis()
        return if (remaining > 0) remaining / (24 * 60 * 60 * 1000) else 0
    }

    // Used for dynamic views
    fun getSubTopics(topicId: String): Flow<List<GKSubTopicEntity>> = 
        repository.getSubTopics(topicId)

    fun getSubTopicById(subTopicId: String): Flow<GKSubTopicEntity?> =
        repository.getSubTopicById(subTopicId)

    fun getAllSubTopics(): Flow<List<GKSubTopicEntity>> =
        repository.getAllSubTopics()

    fun getMCQsForSubTopic(subTopicId: String): Flow<List<MCQQuestionEntity>> =
        repository.getMCQsForSubTopic(subTopicId)

    fun getUniversities(): Flow<List<UniversityEntity>> = repository.getUniversities()

    fun getYearsForUniversity(universityId: String): Flow<List<Int>> = repository.getYearsForUniversity(universityId)

    fun getUniversityQuestions(universityId: String, year: Int): Flow<List<UniversityQuestionEntity>> = 
        repository.getUniversityQuestions(universityId, year)

    fun getMegaQuizQuestions(examId: String): Flow<List<MegaQuizQuestionEntity>> =
        repository.getMegaQuizQuestions(examId)

    fun getMegaQuizSubTopics(examId: String): Flow<List<GKSubTopicEntity>> =
        repository.getMegaQuizSubTopics(examId)
        
    fun getLatestMegaQuizScore(): Flow<Double?> = repository.getLatestMegaQuizScore()

    fun saveMegaQuizResult(examId: String, score: Double, correct: Int, wrong: Int, unanswered: Int) {
        viewModelScope.launch {
            val result = MegaQuizResultEntity(
                id = "mq_res_${System.currentTimeMillis()}",
                examId = examId,
                score = score,
                correctCount = correct,
                wrongCount = wrong,
                unansweredCount = unanswered,
                dateTaken = System.currentTimeMillis()
            )
            repository.insertMegaQuizResult(result)
        }
    }

    fun saveQuizResult(subTopicId: String, score: Double, total: Int, correct: Int, wrong: Int) {
        viewModelScope.launch {
            val result = MCQQuizResultEntity(
                id = "qr_${System.currentTimeMillis()}",
                subTopicId = subTopicId,
                scorePercentage = score,
                dateTaken = System.currentTimeMillis()
            )
            repository.insertQuizResult(result)

            val p = todayProgress.value
            val newProgress = if (p != null) {
                MCQPracticeProgressEntity(
                    dateStr = todayDateStr,
                    totalPracticed = p.totalPracticed + total,
                    correctCount = p.correctCount + correct,
                    wrongCount = p.wrongCount + wrong
                )
            } else {
                MCQPracticeProgressEntity(
                    dateStr = todayDateStr,
                    totalPracticed = total,
                    correctCount = correct,
                    wrongCount = wrong
                )
            }
            repository.insertProgress(newProgress)
        }
    }
    
    fun getMonthlyProgress(monthPrefix: String): Flow<List<MCQPracticeProgressEntity>> = repository.getMonthlyProgress(monthPrefix)
    
    fun getYearlyProgress(yearPrefix: String): Flow<List<MCQPracticeProgressEntity>> = repository.getYearlyProgress(yearPrefix)
    
    fun getAllProgress(): Flow<List<MCQPracticeProgressEntity>> = repository.getAllProgress()

    // ── Recent GK Management ───────────────────────────
    val pendingRecentGKs: StateFlow<List<RecentGKEntity>> = repository.getRecentGKByStatus("PENDING")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val approvedRecentGKs: StateFlow<List<RecentGKEntity>> = repository.getAllRecentGK()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitRecentGK(
        category: String,
        title: String,
        note: String,
        confusionCorner: String = "",
        source: String,
        contributor: String,
    ) {
        viewModelScope.launch {
            val entity = RecentGKEntity(
                id = "rgk_${System.currentTimeMillis()}",
                category = category,
                topicTitle = title,
                specialTopicNote = note,
                confusionCorner = confusionCorner,
                sourceText = source,
                contributorName = contributor,
                status = "PENDING",
                createdAt = System.currentTimeMillis(),
                approvedAt = null
            )
            repository.insertSingleRecentGK(entity)
        }
    }

    fun approveRecentGK(id: String) {
        viewModelScope.launch { repository.updateRecentGKStatus(id, "APPROVED") }
    }

    fun rejectRecentGK(id: String) {
        viewModelScope.launch { repository.updateRecentGKStatus(id, "REJECTED") }
    }

    fun deleteRecentGK(id: String) {
        viewModelScope.launch { repository.updateRecentGKStatus(id, "DELETED") }
    }

    class Factory(private val repository: GKRepository, private val uid: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GKViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GKViewModel(repository, uid) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
