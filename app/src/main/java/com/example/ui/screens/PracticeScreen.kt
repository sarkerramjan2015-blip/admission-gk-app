package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKSubTopicEntity
import com.example.data.MCQQuestionEntity
import com.example.ui.GKViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

// We reuse some colors from SubTopicDetailScreen but add a few for practice
val SnPracticeEmerald = Color(0xFF10b981)
val SnPracticeRed = Color(0xFFef4444)
val SnPracticeEmeraldLight = Color(0xFFd1fae5)
val SnPracticeRedLight = Color(0xFFfee2e2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQPracticeScreen(subTopicId: String, viewModel: GKViewModel, navController: NavController) {
    val subTopic by remember(subTopicId) { viewModel.getSubTopicById(subTopicId) }.collectAsStateWithLifecycle(initialValue = null)
    val rawQuestions by remember(subTopicId) { viewModel.getMCQsForSubTopic(subTopicId) }.collectAsStateWithLifecycle(initialValue = emptyList())
    
    // Parse JSON options outside of the main list recomposition
    val moshi = remember { Moshi.Builder().build() }
    val listType = remember { Types.newParameterizedType(List::class.java, String::class.java) }
    val jsonAdapter = remember { moshi.adapter<List<String>>(listType) }
    
    val parsedQuestions = remember(rawQuestions) {
        rawQuestions.map { q ->
            val options = try { jsonAdapter.fromJson(q.options) ?: emptyList() } catch(e: Exception) { emptyList() }
            ParsedQuestion(q, options)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GK Master", fontWeight = FontWeight.Bold, color = SnPrimaryColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SnPrimaryColor)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = SnOnSurfaceVariantColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SnBackgroundColor.copy(alpha = 0.9f))
            )
        },
        containerColor = SnBackgroundColor
    ) { padding ->
        if (subTopic == null || parsedQuestions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (subTopic == null) {
                    CircularProgressIndicator(color = SnPrimaryColor)
                } else {
                    Text("No questions available for practice.", color = SnOnSurfaceVariantColor)
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
                // Header
                item {
                    PracticeHeader(subTopic = subTopic!!, totalQuestions = parsedQuestions.size)
                }
                
                // Stats (Hardcoded as per plan)
                item {
                    PracticeStatsSection()
                }

                // Questions
                itemsIndexed(parsedQuestions) { index, item ->
                    PracticeQuestionCard(index = index + 1, parsedQuestion = item)
                }
            }
        }
    }
}

data class ParsedQuestion(
    val entity: MCQQuestionEntity,
    val optionsList: List<String>
)

@Composable
fun PracticeHeader(subTopic: GKSubTopicEntity, totalQuestions: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = subTopic.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SnPrimaryColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Topic Progress: 10 / $totalQuestions",
                style = MaterialTheme.typography.bodyMedium,
                color = SnOnSurfaceVariantColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 10f / totalQuestions.coerceAtLeast(1).toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = SnPrimaryColor,
                trackColor = SnOutlineVariantColor.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun PracticeStatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Streak Card
        Card(
            modifier = Modifier.weight(1f).height(100.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(SnPrimaryColor, SnSecondaryColor)
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Streak", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("08", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    Text("Days", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
        }
        
        // Accuracy Card
        Card(
            modifier = Modifier.weight(1f).height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Accuracy", color = SnOnSurfaceVariantColor, fontSize = 12.sp)
                Text("84%", color = SnPrimaryColor, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Text("Excellent!", color = SnPracticeEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PracticeQuestionCard(index: Int, parsedQuestion: ParsedQuestion) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val isAnswered = selectedOption != null
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Question Text
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = SnPrimaryContainerColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = index.toString(),
                            color = SnPrimaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = parsedQuestion.entity.questionText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SnOnSurfaceColor,
                        lineHeight = 24.sp
                    )
                    
                    if (parsedQuestion.entity.sourceExam.isNotBlank() || parsedQuestion.entity.referenceText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (parsedQuestion.entity.referenceText.isNotBlank()) {
                                Text(
                                    text = parsedQuestion.entity.referenceText,
                                    color = Color(0xFFF59E0B), // Golden Star Color
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            if (parsedQuestion.entity.sourceExam.isNotBlank()) {
                                Surface(
                                    color = SnPrimaryContainerColor.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${parsedQuestion.entity.sourceExam} ${parsedQuestion.entity.year}".trim(),
                                        color = SnPrimaryColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Options
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val prefixes = listOf("A", "B", "C", "D", "E")
                parsedQuestion.optionsList.forEachIndexed { optIndex, optionText ->
                    val isCorrect = optionText == parsedQuestion.entity.correctAnswer
                    val isSelected = optionText == selectedOption
                    val showAsCorrect = isAnswered && isCorrect
                    val showAsWrong = isAnswered && isSelected && !isCorrect
                    
                    val containerColor = when {
                        showAsCorrect -> SnPracticeEmeraldLight
                        showAsWrong -> SnPracticeRedLight
                        else -> SnBackgroundColor
                    }
                    val borderColor = when {
                        showAsCorrect -> SnPracticeEmerald
                        showAsWrong -> SnPracticeRed
                        else -> SnOutlineVariantColor.copy(alpha = 0.5f)
                    }
                    val textColor = when {
                        showAsCorrect -> SnPracticeEmerald
                        showAsWrong -> SnPracticeRed
                        else -> SnOnSurfaceColor
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
                            // Prefix Circle
                            val prefixColor = if (showAsCorrect || showAsWrong) textColor else SnOnSurfaceVariantColor
                            val prefixBg = if (showAsCorrect || showAsWrong) textColor.copy(alpha = 0.1f) else SnOutlineVariantColor.copy(alpha = 0.2f)
                            
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
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            
                            if (showAsCorrect) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = "Correct", tint = SnPracticeEmerald)
                            }
                        }
                    }
                }
            }
            
            // Explanation Section
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
                        color = SnHighlightYellow.copy(alpha = 0.5f)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Lightbulb, contentDescription = "Explanation", tint = SnTertiaryColor)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Explanation", fontWeight = FontWeight.Bold, color = SnTertiaryColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = parsedQuestion.entity.explanation.ifBlank { "No explanation available." },
                                    color = SnOnSurfaceColor,
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
