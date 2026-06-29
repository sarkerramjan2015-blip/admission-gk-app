package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.R
import com.example.data.MegaQuizQuestionEntity
import com.example.ui.GKViewModel
import com.example.ui.util.parseHtml
import com.example.ui.navigation.MegaQuizLiveRoute
import com.example.ui.navigation.MegaQuizSubmittedRoute
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.delay

@Composable
fun MegaQuizLiveScreen(examId: String, viewModel: GKViewModel, navController: NavController) {
    val dbQuestions by remember(examId) { viewModel.getMegaQuizQuestions(examId) }.collectAsStateWithLifecycle(initialValue = emptyList())
    val exams by viewModel.megaQuizExams.collectAsStateWithLifecycle(initialValue = emptyList())
    val exam = remember(examId, exams) { exams.find { it.id == examId } }

    val serial = if (exam != null) exam.id.filter { it.isDigit() }.take(2).padStart(2, '0') else "—"
    val examTitle = "মেগা কুইজ $serial"
    val examSubtitle = exam?.title ?: ""

    var hasLoadedDb by remember { mutableStateOf(false) }
    LaunchedEffect(dbQuestions) { hasLoadedDb = true }

    val moshi = remember { Moshi.Builder().build() }
    val listType = remember { Types.newParameterizedType(List::class.java, String::class.java) }
    val jsonAdapter = remember { moshi.adapter<List<String>>(listType) }

    fun parseOptions(q: MegaQuizQuestionEntity): List<String> {
        val parsed = try { jsonAdapter.fromJson(q.options) } catch (e: Exception) { null }
        if (parsed != null && parsed.size >= 2) return parsed
        val all = mutableListOf(q.correctAnswer, "বিকল্প ক", "বিকল্প খ", "বিকল্প গ")
        all.shuffle()
        return all
    }

    val questions = remember(dbQuestions) { dbQuestions.take(30) }
    val optionsCache = remember(questions) { questions.associateWith { parseOptions(it) } }

    var selectedAnswers by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var timeLeft by remember { mutableIntStateOf(15 * 60) }
    var showSubmitDialog by remember { mutableStateOf(false) }
    var timeUp by remember { mutableStateOf(false) }

    val submitAction = {
        if (!timeUp) {
            timeUp = true
            var right = 0
            var wrong = 0
            questions.forEachIndexed { idx, q ->
                val ans = selectedAnswers[idx]
                if (ans != null) {
                    if (ans == q.correctAnswer) right++ else wrong++
                }
            }
            val score = right - (wrong * 0.25)
            val unanswered = questions.size - selectedAnswers.size
            viewModel.saveMegaQuizResult(examId, score, right, wrong, unanswered)
            navController.navigate(MegaQuizSubmittedRoute(questions.size, selectedAnswers.size, unanswered)) {
                popUpTo<MegaQuizLiveRoute> { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) { delay(1000); timeLeft-- }
        submitAction()
    }

    val answeredCount = selectedAnswers.size

    // Root layout: Column with fixed header + scrollable LazyColumn
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F9FF))) {
        // Fixed header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Brush.linearGradient(listOf(Color(0xFF2E2395), Color(0xFF3F36D9), Color(0xFF6C4DFF))))
        ) {
            Box(Modifier.size(200.dp).offset(x = (-60).dp, y = (-60).dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)), CircleShape))

            Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp, start = 16.dp, end = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f))
                    Surface(
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFFEF4444),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().clickable { showSubmitDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CheckCircle, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("পরীক্ষা জমা দিন", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(24.dp), contentScale = ContentScale.Fit)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(examTitle, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(examSubtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFFDDE3FF), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }

        // Scrollable content — Column with verticalScroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats card
            StatsCard(timeLeft = timeLeft, answeredCount = answeredCount, totalQuestions = questions.size)

            // Questions or states
            if (!hasLoadedDb && dbQuestions.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color(0xFF2638D9), strokeWidth = 3.dp)
                        Spacer(Modifier.height(14.dp))
                        Text("প্রশ্ন লোড হচ্ছে...", color = Color(0xFF8A90A8), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else if (questions.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Quiz, null, tint = Color(0xFFB0B6C8), modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("এই পরীক্ষার প্রশ্ন পাওয়া যায়নি", color = Color(0xFF8A90A8), style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("দয়া করে আবার চেষ্টা করুন", color = Color(0xFFB0B6C8), style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                questions.forEachIndexed { index, mq ->
                    val options = optionsCache[mq] ?: parseOptions(mq)
                    val selectedOption = selectedAnswers[index]
                    val isAnswered = selectedOption != null
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().drawBehind {
                            drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(16.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                        }) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF2638D9).copy(alpha = 0.08f)) {
                                        Text("প্রশ্ন ${"%02d".format(index + 1)}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 11.sp, color = Color(0xFF2638D9), fontWeight = FontWeight.Bold)
                                    }
                                    Text("মার্ক: ১", fontSize = 11.sp, color = Color(0xFF8A90A8), fontWeight = FontWeight.Medium)
                                }
                                Spacer(Modifier.height(14.dp))
                                Text(mq.questionText.parseHtml(), style = MaterialTheme.typography.titleMedium, color = Color(0xFF07113F), lineHeight = 24.sp)
                                Spacer(Modifier.height(18.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    val optionLabels = listOf("A", "B", "C", "D")
                                    options.forEachIndexed { optIndex, option ->
                                        val label = optionLabels.getOrNull(optIndex) ?: ""
                                        if (isAnswered) {
                                            if (isThisSelected(selectedOption, option)) {
                                                SelectedOptionRow(label = label, text = option)
                                            } else {
                                                MutedOptionRow(label = label, text = option)
                                            }
                                        } else {
                                            ClickableOptionRow(label = label, text = option, onClick = { selectedAnswers = selectedAnswers + (index to option) })
                                        }
                                    }
                                }
                                
                                if (!mq.relatedSubTopicId.isNullOrEmpty()) {
                                    Spacer(Modifier.height(16.dp))
                                    Surface(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            navController.navigate(com.example.ui.navigation.SubTopicDetailRoute(mq.relatedSubTopicId, ""))
                                        },
                                        color = Color(0xFFE5E9F8),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Filled.MenuBook, contentDescription = null, tint = Color(0xFF2638D9), modifier = Modifier.size(18.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Read This Topic",
                                                color = Color(0xFF2638D9),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Submit dialog
    if (showSubmitDialog) {
        val unanswered = questions.size - answeredCount
        Dialog(onDismissRequest = { showSubmitDialog = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)), contentAlignment = Alignment.Center) {
                Surface(modifier = Modifier.fillMaxWidth().padding(24.dp), shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 24.dp) {
                    Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = Color(0xFF2638D9).copy(alpha = 0.10f)) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Assignment, contentDescription = null, tint = Color(0xFF2638D9), modifier = Modifier.size(32.dp)) }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("পরীক্ষা জমা দেবেন?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF07113F))
                        Spacer(Modifier.height(8.dp))
                        Text("জমা দেওয়ার আগে আপনার উত্তরগুলোর সংক্ষিপ্ত তথ্য দেখে নিন।", color = Color(0xFF5E6480), fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Center, maxLines = 2)
                        Spacer(Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SubmitStatPill(modifier = Modifier.weight(1f), label = "মোট প্রশ্ন", value = "${questions.size}", color = Color(0xFF2638D9), bgColor = Color(0xFFEEF0FF))
                            SubmitStatPill(modifier = Modifier.weight(1f), label = "উত্তর দিয়েছেন", value = "$answeredCount", color = Color(0xFF00A878), bgColor = Color(0xFFEFFFF9))
                            SubmitStatPill(modifier = Modifier.weight(1f), label = "বাকি আছে", value = "$unanswered", color = Color(0xFFEF4444), bgColor = Color(0xFFFFF7F8))
                        }
                        if (unanswered > 0) {
                            Spacer(Modifier.height(16.dp))
                            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color(0xFFFFF4E6), border = BorderStroke(1.dp, Color(0xFFFDBA74).copy(alpha = 0.6f))) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFF6B93B), modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text("আপনি এখনো ${unanswered}টি প্রশ্নের উত্তর দেননি। তবুও জমা দিতে চান?", color = Color(0xFF5E6480), fontSize = 13.sp, lineHeight = 18.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text("পরীক্ষা জমা দেওয়ার পর আর কোনো উত্তর পরিবর্তন করা যাবে না।", color = Color(0xFF8A90A8), fontSize = 12.sp, lineHeight = 16.sp)
                                    }
                                }
                            }
                        } else {
                            Spacer(Modifier.height(16.dp))
                            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color(0xFFFFF8E1), border = BorderStroke(0.5.dp, Color(0xFFF6B93B).copy(alpha = 0.4f))) {
                                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Filled.Warning, null, tint = Color(0xFFF6B93B), modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("পরীক্ষা জমা দেওয়ার পর আর কোনো উত্তর পরিবর্তন করা যাবে না।", color = Color(0xFF5E6480), fontSize = 13.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { showSubmitDialog = false; submitAction() }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White)) {
                            Text("হ্যাঁ, জমা দিন", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(onClick = { showSubmitDialog = false }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.5.dp, Color(0xFFE5E9F8)), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF5E6480))) {
                            Text("না, ফিরে যাই", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.SubmitStatPill(modifier: Modifier, label: String, value: String, color: Color, bgColor: Color) {
    Surface(shape = RoundedCornerShape(12.dp), color = bgColor, modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = color.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
private fun StatsCard(timeLeft: Int, answeredCount: Int, totalQuestions: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatsItem(icon = Icons.Filled.Timer, iconTint = Color(0xFFEF4444), label = "সময় বাকি", value = { val min = timeLeft / 60; val sec = timeLeft % 60; "${"%02d".format(min)}:${"%02d".format(sec)}" }, subtext = "১৫ মিনিট")
            StatsItem(icon = Icons.Filled.ListAlt, iconTint = Color(0xFF6C4DFF), label = "উত্তর দিয়েছেন", value = { "${"%02d".format(answeredCount)} / ${totalQuestions}" }, subtext = null)
            StatsItem(icon = Icons.Filled.EmojiEvents, iconTint = Color(0xFF6C4DFF), label = "মোট মার্ক", value = { "${totalQuestions}" }, subtext = null)
            StatsItem(icon = Icons.Filled.CheckCircle, iconTint = Color(0xFF00A878), label = "পাস মার্ক", value = { "১২" }, subtext = null)
        }
    }
}

@Composable
private fun StatsItem(icon: ImageVector, iconTint: Color, label: String, value: @Composable () -> String, subtext: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(4.dp))
        Text(value(), style = MaterialTheme.typography.titleSmall, color = Color(0xFF07113F), fontWeight = FontWeight.ExtraBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = 9.sp, color = Color(0xFF8A90A8), fontWeight = FontWeight.Medium, maxLines = 1)
            if (subtext != null) { Text(" • $subtext", fontSize = 9.sp, color = Color(0xFF8A90A8), maxLines = 1) }
        }
    }
}

@Composable
private fun SelectedOptionRow(label: String, text: String) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFEEF0FF), shape = RoundedCornerShape(12.dp), border = BorderStroke(2.dp, Color(0xFF2638D9))) {
        Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFF2638D9), shape = CircleShape, modifier = Modifier.size(34.dp)) { Box(contentAlignment = Alignment.Center) { Text(label, color = Color.White, fontWeight = FontWeight.Bold) } }
                Spacer(Modifier.width(14.dp))
                Text(text.parseHtml(), fontSize = 16.sp, color = Color(0xFF07113F), fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF2638D9), modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun MutedOptionRow(label: String, text: String) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFF5F7FF), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFE5E9F8))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color.Transparent, border = BorderStroke(1.5.dp, Color(0xFFB0B6C8)), shape = CircleShape, modifier = Modifier.size(34.dp)) { Box(contentAlignment = Alignment.Center) { Text(label, color = Color(0xFFB0B6C8), fontWeight = FontWeight.Medium) } }
            Spacer(Modifier.width(14.dp))
            Text(text.parseHtml(), fontSize = 16.sp, color = Color(0xFFB0B6C8))
        }
    }
}

@Composable
private fun ClickableOptionRow(label: String, text: String, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), color = Color.White, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFE5E9F8))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color.Transparent, border = BorderStroke(1.5.dp, Color(0xFF5E6480)), shape = CircleShape, modifier = Modifier.size(34.dp)) { Box(contentAlignment = Alignment.Center) { Text(label, color = Color(0xFF5E6480), fontWeight = FontWeight.Medium) } }
            Spacer(Modifier.width(14.dp))
            Text(text.parseHtml(), fontSize = 16.sp, color = Color(0xFF07113F))
        }
    }
}

private fun isThisSelected(selected: String?, option: String): Boolean = selected == option
