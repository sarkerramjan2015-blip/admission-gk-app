package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UniversityQuestionEntity
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamPaperLayout(
    uniId: String,
    year: Int,
    questions: List<UniversityQuestionEntity>,
    onFinish: () -> Unit
) {
    val selectedAnswers = remember { mutableStateMapOf<String, String>() }

    val totalQuestions = questions.size
    val answeredCount = selectedAnswers.size
    val correctCount = questions.count { q ->
        selectedAnswers[q.id] == q.correctAnswer
    }
    val wrongCount = answeredCount - correctCount
    val progress = if (totalQuestions > 0) answeredCount.toFloat() / totalQuestions else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF1e00a9).copy(alpha = 0.95f),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onFinish) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${uniId.uppercase()} $year-${(year + 1).toString().substring(2)}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1e00a9)),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Finish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("PRACTICE PROGRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("$answeredCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                                Text(" / $totalQuestions", fontSize = 16.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("$correctCount CORRECT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10b981))
                            Text("$wrongCount WRONG", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFba1a1a))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFF1e00a9),
                        trackColor = Color(0xFFe9e6fa)
                    )
                }
            }
        },
        containerColor = Color(0xFFfcf8ff)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Instruction Banner
                Surface(
                    color = Color(0xFF8b4bfc).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF8b4bfc).copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFF712ae2))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Practice Mode: Selection reveals truth. Continuous feed enabled.",
                            fontSize = 14.sp,
                            color = Color(0xFF464555)
                        )
                    }
                }
            }

            itemsIndexed(questions) { index, question ->
                val selectedOption = selectedAnswers[question.id]
                val isAnswered = selectedOption != null
                val isCorrect = selectedOption == question.correctAnswer
                val optionsList = remember(question.options) {
                    try {
                        val array = JSONArray(question.options)
                        List(array.length()) { array.getString(it) }
                    } catch (e: Exception) {
                        emptyList()
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(0xFFe2dfff),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        "Question ${index + 1}",
                                        color = Color(0xFF1e00a9),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFeda951).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        "MEDIUM • ${question.subject.uppercase()}",
                                        color = Color(0xFF653e00),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            if (isAnswered && isCorrect) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF10b981))
                            } else {
                                Icon(Icons.Filled.BookmarkBorder, contentDescription = null, tint = Color(0xFF777587))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = question.questionText,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF1b1a28),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Options grid (2 columns)
                        val chunkedOptions = optionsList.chunked(2)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val labels = listOf("A", "B", "C", "D")
                            chunkedOptions.forEachIndexed { rowIndex, rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowItems.forEachIndexed { colIndex, optionText ->
                                        val optIndex = rowIndex * 2 + colIndex
                                        val label = if (optIndex < labels.size) labels[optIndex] else ""
                                        
                                        val isThisOptionSelected = selectedOption == label
                                        val isThisOptionCorrect = question.correctAnswer == label
                                        
                                        val borderColor = when {
                                            !isAnswered -> Color(0xFFc7c4d8)
                                            isThisOptionCorrect -> Color(0xFF10b981)
                                            isThisOptionSelected -> Color(0xFFba1a1a)
                                            else -> Color(0xFFc7c4d8)
                                        }
                                        
                                        val bgColor = when {
                                            !isAnswered -> Color.Transparent
                                            isThisOptionCorrect -> Color(0xFF10b981).copy(alpha = 0.1f)
                                            isThisOptionSelected -> Color(0xFFba1a1a).copy(alpha = 0.1f)
                                            else -> Color.Transparent
                                        }

                                        val textColor = when {
                                            !isAnswered -> Color(0xFF1b1a28)
                                            isThisOptionCorrect -> Color(0xFF065f46)
                                            isThisOptionSelected -> Color(0xFF93000a)
                                            else -> Color(0xFF1b1a28)
                                        }
                                        
                                        val circleBgColor = when {
                                            !isAnswered -> Color(0xFFf5f2ff)
                                            isThisOptionCorrect -> Color(0xFF10b981)
                                            isThisOptionSelected -> Color(0xFFba1a1a)
                                            else -> Color(0xFFf5f2ff)
                                        }
                                        
                                        val circleTextColor = when {
                                            !isAnswered -> Color(0xFF1b1a28)
                                            isThisOptionCorrect || isThisOptionSelected -> Color.White
                                            else -> Color(0xFF1b1a28)
                                        }

                                        val opacity = if (isAnswered && !isThisOptionCorrect && !isThisOptionSelected) 0.6f else 1f

                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable(enabled = !isAnswered) {
                                                    selectedAnswers[question.id] = label
                                                },
                                            shape = RoundedCornerShape(8.dp),
                                            color = bgColor,
                                            border = BorderStroke(1.dp, borderColor)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    modifier = Modifier.size(24.dp),
                                                    shape = CircleShape,
                                                    color = circleBgColor,
                                                    border = BorderStroke(1.dp, Color(0xFFc7c4d8))
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = circleTextColor)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = optionText,
                                                    fontSize = 16.sp,
                                                    color = textColor.copy(alpha = opacity)
                                                )
                                            }
                                        }
                                    }
                                    // Handle odd number of options in the last row
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        // Explanation Card (only shown if answered incorrectly)
                        if (isAnswered && !isCorrect && question.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFc7c4d8).copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = Color(0xFFf5f2ff),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color(0xFF1e00a9), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Why this answer?", color = Color(0xFF1e00a9), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = question.explanation,
                                        color = Color(0xFF464555),
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Padding for the bottom bar
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
