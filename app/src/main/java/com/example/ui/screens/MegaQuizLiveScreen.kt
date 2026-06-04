package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.GKViewModel
import com.example.ui.navigation.MegaQuizLiveRoute
import com.example.ui.navigation.MegaQuizSubmittedRoute
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizLiveScreen(examId: String, viewModel: GKViewModel, navController: NavController) {
    val rawQuestions by remember(examId) { viewModel.getMegaQuizQuestions(examId) }.collectAsStateWithLifecycle(initialValue = emptyList())
    // Mocking 30 questions by repeating the dummy list
    val questions = if (rawQuestions.isNotEmpty()) {
        (0..5).flatMap { rawQuestions }.take(30)
    } else emptyList()

    if (questions.isEmpty()) {
        Scaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading Exam...", color = Color(0xFF777587))
            }
        }
        return
    }

    var selectedAnswers by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var timeLeft by remember { mutableIntStateOf(1182) } // 19m 42s as per mockup
    var showSubmitDialog by remember { mutableStateOf(false) }
    
    val submitAction = {
        var right = 0
        var wrong = 0
        questions.forEachIndexed { idx, q -> 
            val ans = selectedAnswers[idx]
            if (ans != null) {
                if (ans == q.correctAnswer) right++ else wrong++
            }
        }
        val score = right - (wrong * 0.25)
        navController.navigate(MegaQuizSubmittedRoute(questions.size, selectedAnswers.size, questions.size - selectedAnswers.size)) { popUpTo<MegaQuizLiveRoute> { inclusive = true } }
    }

    LaunchedEffect(Unit) {
        while(timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        submitAction()
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF1e00a9).copy(alpha = 0.95f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().zIndex(10f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Timer, contentDescription = "Timer", tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Mega Quiz 1", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0xFF3525cd)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFFb1afff), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                val minutes = timeLeft / 60
                                val seconds = timeLeft % 60
                                Text(String.format("%02d:%02d", minutes, seconds), color = Color(0xFFb1afff), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = { showSubmitDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1e00a9)),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text("Submit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "Exams")
        },
        containerColor = Color(0xFFfcf8ff)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Stats Bar (Floating Sub-Header)
            Surface(
                color = Color(0xFFf5f2ff),
                modifier = Modifier.fillMaxWidth().zIndex(5f),
                border = BorderStroke(width = 1.dp, color = Color(0xFFc7c4d8).copy(alpha=0.5f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Progress:", color = Color(0xFF464555), fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color(0xFFe2dfff), shape = RoundedCornerShape(999.dp)) {
                            Text("${selectedAnswers.size}/${questions.size} Answered", color = Color(0xFF1e00a9), fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    LinearProgressIndicator(
                        progress = { selectedAnswers.size / questions.size.toFloat() },
                        modifier = Modifier.width(100.dp).height(8.dp).clip(RoundedCornerShape(999.dp)),
                        color = Color(0xFF1e00a9),
                        trackColor = Color(0xFFe3e0f4)
                    )
                }
            }

            // Main Content Canvas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val moshi = Moshi.Builder().build()
                val listType = Types.newParameterizedType(List::class.java, String::class.java)
                val jsonAdapter = moshi.adapter<List<String>>(listType)

                itemsIndexed(questions) { index, mq ->
                    val options = try { jsonAdapter.fromJson(mq.options) ?: emptyList() } catch(e: Exception) { emptyList() }
                    val selectedOption = selectedAnswers[index]
                    val isAnswered = selectedOption != null

                    Card(
                        modifier = Modifier.fillMaxWidth().alpha(if (isAnswered) 0.95f else 1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                        elevation = CardDefaults.cardElevation(if(isAnswered) 1.dp else 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Text("QUESTION ${String.format("%02d", index + 1)}", color = Color(0xFF1e00a9), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                                if (isAnswered) {
                                    Surface(color = Color(0xFF10b981).copy(alpha=0.1f), shape = RoundedCornerShape(999.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF10b981), modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Answered", color = Color(0xFF10b981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    Icon(Icons.Filled.BookmarkBorder, contentDescription = null, tint = Color(0xFF777587))
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(mq.questionText, style = MaterialTheme.typography.titleLarge, color = Color(0xFF1b1a28), lineHeight = 28.sp)
                            Spacer(modifier = Modifier.height(24.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                val optionLabels = listOf("A", "B", "C", "D")
                                options.forEachIndexed { optIndex, option ->
                                    val isThisSelected = selectedOption == option
                                    val label = optionLabels.getOrNull(optIndex) ?: ""
                                    
                                    if (isAnswered) {
                                        if (isThisSelected) {
                                            // Selected Option
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = Color(0xFFe2dfff),
                                                shape = RoundedCornerShape(12.dp),
                                                border = BorderStroke(2.dp, Color(0xFF1e00a9))
                                            ) {
                                                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Surface(color = Color(0xFF1e00a9), shape = CircleShape, modifier = Modifier.size(32.dp)) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text(label, color = Color.White, fontWeight = FontWeight.Medium)
                                                            }
                                                        }
                                                        Spacer(modifier = Modifier.width(16.dp))
                                                        Text(option, fontSize = 18.sp, color = Color(0xFF3323cc), fontWeight = FontWeight.SemiBold)
                                                    }
                                                    Surface(color = Color(0xFF1e00a9).copy(alpha=0.1f), shape = RoundedCornerShape(8.dp)) {
                                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                                            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF1e00a9), modifier = Modifier.size(16.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text("LOCKED", color = Color(0xFF1e00a9), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            // Muted Option
                                            Surface(
                                                modifier = Modifier.fillMaxWidth().alpha(0.6f),
                                                color = Color(0xFFf5f2ff),
                                                shape = RoundedCornerShape(12.dp),
                                                border = BorderStroke(1.dp, Color(0xFFc7c4d8))
                                            ) {
                                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Surface(color = Color.Transparent, border = BorderStroke(1.dp, Color(0xFF777587)), shape = CircleShape, modifier = Modifier.size(32.dp)) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Text(label, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Text(option, fontSize = 18.sp, color = Color(0xFF464555))
                                                }
                                            }
                                        }
                                    } else {
                                        // Unanswered Option
                                        Surface(
                                            modifier = Modifier.fillMaxWidth().clickable {
                                                selectedAnswers = selectedAnswers + (index to option)
                                            },
                                            color = Color(0xFFfcf8ff),
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, Color(0xFFc7c4d8))
                                        ) {
                                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Surface(color = Color.Transparent, border = BorderStroke(1.dp, Color(0xFF777587)), shape = CircleShape, modifier = Modifier.size(32.dp)) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(label, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text(option, fontSize = 18.sp, color = Color(0xFF1b1a28))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Add a bottom spacing to ensure the last item is not hidden behind bottom bar
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    if (showSubmitDialog) {
        Dialog(
            onDismissRequest = { showSubmitDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1b1a28).copy(alpha = 0.4f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)),
                    color = Color.White,
                    shadowElevation = 24.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = Color(0xFF3525cd),
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp).padding(bottom = 16.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.TaskAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                        
                        Text("Submit Exam?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = Color(0xFF1b1a28))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("You're about to end your session. Here's your summary:", color = Color(0xFF464555), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = Color(0xFFf5f2ff), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("TOTAL", fontSize = 10.sp, color = Color(0xFF464555), fontWeight = FontWeight.Bold)
                                    Text("${questions.size}", color = Color(0xFF1e00a9), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Surface(color = Color(0xFF10b981).copy(alpha=0.1f), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("ANSWERED", fontSize = 10.sp, color = Color(0xFF10b981), fontWeight = FontWeight.Bold)
                                    Text("${selectedAnswers.size}", color = Color(0xFF10b981), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Surface(color = Color(0xFFba1a1a).copy(alpha=0.1f), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("SKIPPED", fontSize = 10.sp, color = Color(0xFFba1a1a), fontWeight = FontWeight.Bold)
                                    Text("${questions.size - selectedAnswers.size}", color = Color(0xFFba1a1a), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Surface(color = Color(0xFFffddb8), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Filled.Info, contentDescription = null, tint = Color(0xFF673f00), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Submit করার পর answer change করা যাবে না।", color = Color(0xFF2a1700), fontSize = 13.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = {
                                showSubmitDialog = false
                                submitAction()
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e00a9))
                        ) {
                            Text("Submit Now", fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        TextButton(
                            onClick = { showSubmitDialog = false },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Cancel", fontWeight = FontWeight.Bold, color = Color(0xFF464555))
                        }
                    }
                }
            }
        }
    }
}
