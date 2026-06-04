package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.GKMainTopicEntity
import com.example.data.GKRepository
import com.example.data.GKSubTopicEntity
import com.example.data.MCQQuestionEntity
import com.example.data.MegaQuizExamEntity
import com.example.data.MegaQuizQuestionEntity
import com.example.data.RecentGKEntity
import com.example.data.UniversityEntity
import com.example.data.UniversityQuestionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GKViewModel(private val repository: GKRepository) : ViewModel() {

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

    // Used for dynamic views
    fun getSubTopics(topicId: String): Flow<List<GKSubTopicEntity>> = 
        repository.getSubTopics(topicId)

    fun getSubTopicById(subTopicId: String): Flow<GKSubTopicEntity?> =
        repository.getSubTopicById(subTopicId)

    fun getMCQsForSubTopic(subTopicId: String): Flow<List<MCQQuestionEntity>> =
        repository.getMCQsForSubTopic(subTopicId)

    fun getUniversities(): Flow<List<UniversityEntity>> = repository.getUniversities()

    fun getYearsForUniversity(universityId: String): Flow<List<Int>> = repository.getYearsForUniversity(universityId)

    fun getUniversityQuestions(universityId: String, year: Int): Flow<List<UniversityQuestionEntity>> = 
        repository.getUniversityQuestions(universityId, year)

    fun getMegaQuizQuestions(examId: String): Flow<List<MegaQuizQuestionEntity>> =
        repository.getMegaQuizQuestions(examId)
        
    fun getLatestMegaQuizScore(): Flow<Double?> = repository.getLatestMegaQuizScore()

    class Factory(private val repository: GKRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GKViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GKViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
