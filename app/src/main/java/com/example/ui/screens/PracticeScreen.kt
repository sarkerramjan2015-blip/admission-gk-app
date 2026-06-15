package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import com.example.ui.util.parseHtml
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKSubTopicEntity
import com.example.data.MCQQuestionEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionTopBar
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.components.MetricCard
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQPracticeScreen(
    subTopicId: String,
    viewModel: GKViewModel,
    navController: NavController,
) {
    val subTopic by remember(subTopicId) { viewModel.getSubTopicById(subTopicId) }
        .collectAsStateWithLifecycle(initialValue = null)
    val rawQuestions by remember(subTopicId) { viewModel.getMCQsForSubTopic(subTopicId) }
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val moshi = remember { Moshi.Builder().build() }
    val listType = remember {
        Types.newParameterizedType(List::class.java, String::class.java)
    }
    val jsonAdapter = remember { moshi.adapter<List<String>>(listType) }

    val parsedQuestions = remember(rawQuestions) {
        rawQuestions.map { q ->
            val options = try {
                jsonAdapter.fromJson(q.options) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
            ParsedQuestion(q, options)
        }
    }

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admission GK",
                subtitle = "MCQ Practice",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (subTopic == null || parsedQuestions.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (subTopic == null) {
                    LoadingState(message = "Loading practice questions...")
                } else {
                    EmptyState(
                        title = "No practice questions yet",
                        message = "This topic is ready for notes. MCQ practice will appear here once questions are added.",
                        icon = Icons.Filled.CheckCircle
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    PracticeHeader(
                        subTopic = subTopic!!,
                        totalQuestions = parsedQuestions.size
                    )
                }
                item {
                    PracticeStatsSection(totalQuestions = parsedQuestions.size)
                }
                itemsIndexed(parsedQuestions) { index, item ->
                    PracticeQuestionCard(index = index + 1, parsedQuestion = item)
                }
            }
        }
    }
}

data class ParsedQuestion(
    val entity: MCQQuestionEntity,
    val optionsList: List<String>,
)

@Composable
private fun PracticeHeader(subTopic: GKSubTopicEntity, totalQuestions: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = subTopic.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Topic Progress: 10 / $totalQuestions",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 10f / totalQuestions.coerceAtLeast(1).toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = BrandPrimary,
                trackColor = AppOutline.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun PracticeStatsSection(totalQuestions: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            label = "Questions",
            value = totalQuestions.toString(),
            supportingText = "Instant feedback",
            icon = Icons.Filled.CheckCircle,
            accentColor = BrandPrimary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            label = "Mode",
            value = "Practice",
            supportingText = "No timer",
            icon = Icons.Filled.Lightbulb,
            accentColor = SuccessColor
        )
    }
}

@Composable
private fun PracticeQuestionCard(index: Int, parsedQuestion: ParsedQuestion) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val isAnswered = selectedOption != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, AppOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = BrandPrimary.copy(alpha = 0.1f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = index.toString(),
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = parsedQuestion.entity.questionText.parseHtml(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 24.sp
                    )

                    if (parsedQuestion.entity.sourceExam.isNotBlank() || parsedQuestion.entity.referenceText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (parsedQuestion.entity.referenceText.isNotBlank()) {
                                Text(
                                    text = parsedQuestion.entity.referenceText,
                                    color = BrandAccent,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            if (parsedQuestion.entity.sourceExam.isNotBlank()) {
                                Surface(
                                    color = BrandPrimary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${parsedQuestion.entity.sourceExam} ${parsedQuestion.entity.year}".trim(),
                                        color = BrandPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 6.dp,
                                            vertical = 2.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val prefixes = listOf("A", "B", "C", "D", "E")
                parsedQuestion.optionsList.forEachIndexed { optIndex, optionText ->
                    val isCorrect = optionText == parsedQuestion.entity.correctAnswer
                    val isSelected = optionText == selectedOption
                    val showAsCorrect = isAnswered && isCorrect
                    val showAsWrong = isAnswered && isSelected && !isCorrect

                    val containerColor = when {
                        showAsCorrect -> SuccessColor.copy(alpha = 0.1f)
                        showAsWrong -> ErrorColor.copy(alpha = 0.1f)
                        else -> AppBackground
                    }
                    val borderColor = when {
                        showAsCorrect -> SuccessColor
                        showAsWrong -> ErrorColor
                        else -> AppOutline.copy(alpha = 0.5f)
                    }
                    val textColor = when {
                        showAsCorrect -> SuccessColor
                        showAsWrong -> ErrorColor
                        else -> TextPrimary
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isAnswered) {
                                selectedOption = optionText
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = containerColor,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val prefixColor = if (showAsCorrect || showAsWrong) textColor
                            else TextSecondary
                            val prefixBg = if (showAsCorrect || showAsWrong) textColor.copy(alpha = 0.1f)
                            else AppOutline.copy(alpha = 0.2f)

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(prefixBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = prefixes.getOrElse(optIndex) { "-" },
                                    color = prefixColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText.parseHtml(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )

                            if (showAsCorrect) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = SuccessColor
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isAnswered,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = BrandAccent.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.Lightbulb,
                                contentDescription = "Explanation",
                                tint = BrandAccent
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Explanation",
                                    fontWeight = FontWeight.Bold,
                                    color = BrandAccent
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = parsedQuestion.entity.explanation
                                        .ifBlank { "No explanation available." }.parseHtml(),
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
