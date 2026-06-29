package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.R
import com.example.ui.GKViewModel
import com.example.ui.theme.*
import com.example.ui.util.parseHtml
import com.example.ui.navigation.MCQQuizLiveRoute
import com.example.ui.navigation.MCQQuizResultRoute
import com.example.ui.navigation.MCQPracticeRoute
import com.example.ui.navigation.SubTopicDetailRoute
import com.example.ui.navigation.MegaQuizLiveRoute
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQQuizIntroScreen(subTopicId: String, viewModel: GKViewModel, navController: NavController) {
    var agreed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admission GK", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandPrimary)
            )
        },
        bottomBar = {
            Surface(
                color = AppSurface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { agreed = !agreed }
                    ) {
                        Checkbox(
                            checked = agreed,
                            onCheckedChange = { agreed = it },
                            colors = CheckboxDefaults.colors(checkedColor = BrandPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "আমি উপরের সকল কুইজ নিয়মাবলী পড়েছি এবং বুঝেছি।",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(MCQQuizLiveRoute(subTopicId)) },
                        enabled = agreed,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("পরীক্ষা শুরু করুন", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    }
                }
            }
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Surface(color = Color(0xFFE0E0FF), shape = RoundedCornerShape(8.dp)) {
                    Icon(
                        Icons.Filled.Public,
                        contentDescription = null,
                        tint = BrandPrimary,
                        modifier = Modifier.padding(8.dp).size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "GENERAL KNOWLEDGE",
                    color = BrandSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
            Text(
                "টাইমড MCQ পরীক্ষা",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                "ভৌগোলিক অবস্থান ও সীমানা বিষয়ক কুইজ",
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Quiz,
                    iconTint = BrandPrimary,
                    value = "10",
                    label = "প্রশ্ন"
                )
                InfoCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Timer,
                    iconTint = BrandSecondary,
                    value = "06",
                    label = "মিনিট"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GradientMegaQuiz, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Verified, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "মার্কিং স্কিম",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("সঠিক", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                Text(
                                    "+১.০",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ভুল", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                Text(
                                    "−০.২৫",
                                    color = BrandAccent,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = AppSurfaceAlt,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(BrandPrimary)
                    )
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Surface(
                            color = BrandPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.LockClock,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp).size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                "উত্তর লক",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextPrimary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "একবার উত্তর নির্বাচন করলে তা আর পরিবর্তন করা যাবে না। উত্তর দেওয়ার আগে ভালোভাবে যাচাই করে নিন।",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "একবার উত্তর নির্বাচন করলে তা আর পরিবর্তন করা যাবে না।",
                                color = BrandPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQQuizLiveScreen(subTopicId: String, viewModel: GKViewModel, navController: NavController) {
    val allMcqs by remember(subTopicId) { viewModel.getMCQsForSubTopic(subTopicId) }
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var quizQuestions by remember { mutableStateOf<List<com.example.data.MCQQuestionEntity>>(emptyList()) }
    var questionOptions by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    var answeredMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var timeLeft by remember { mutableIntStateOf(360) }

    LaunchedEffect(allMcqs) {
        if (allMcqs.isNotEmpty() && quizQuestions.isEmpty()) {
            val selected = allMcqs.shuffled().take(10)
            quizQuestions = selected

            val moshi = com.squareup.moshi.Moshi.Builder().build()
            val listType =
                com.squareup.moshi.Types.newParameterizedType(List::class.java, String::class.java)
            val jsonAdapter = moshi.adapter<List<String>>(listType)

            val optionsMap = mutableMapOf<String, List<String>>()
            selected.forEach { q ->
                val opts = try {
                    jsonAdapter.fromJson(q.options) ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }
                optionsMap[q.id] = opts.shuffled()
            }
            questionOptions = optionsMap
        }
    }

    LaunchedEffect(quizQuestions) {
        if (quizQuestions.isNotEmpty()) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            submitQuiz(subTopicId, quizQuestions, answeredMap, 360 - timeLeft, navController, viewModel)
        }
    }

    if (quizQuestions.isEmpty()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Quiz Live") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandPrimary)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Admission GK",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(999.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val mins = timeLeft / 60
                                val secs = timeLeft % 60
                                Icon(
                                    Icons.Filled.Timer,
                                    contentDescription = null,
                                    tint = BrandAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    String.format("%02d:%02d", mins, secs),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.width(12.dp))
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(Color.White.copy(alpha = 0.2f))
                                )
                                Spacer(Modifier.width(12.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.Assignment,
                                    contentDescription = null,
                                    tint = Color(0xFFE0E0FF),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "${answeredMap.size}/10",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandPrimary)
                )
                LinearProgressIndicator(
                    progress = { answeredMap.size / 10f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = BrandSecondary,
                    trackColor = AppSurfaceAlt
                )
            }
        },
        containerColor = AppBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(quizQuestions.size) { index ->
                val q = quizQuestions[index]
                val opts = questionOptions[q.id] ?: emptyList()
                val isAnswered = answeredMap.containsKey(q.id)
                val selectedOpt = answeredMap[q.id]

                Card(
                    colors = CardDefaults.cardColors(containerColor = AppSurface),
                    border = BorderStroke(1.dp, AppOutline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Surface(
                                color = Color(0xFFE0E0FF),
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        "${index + 1}",
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Spacer(Modifier.width(16.dp))
                            Text(
                                q.questionText.parseHtml(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextPrimary,
                                lineHeight = 28.sp
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        val labels = listOf("A", "B", "C", "D")
                        opts.forEachIndexed { i, opt ->
                            val isThisSelected = selectedOpt == opt
                            val bgColor = if (isThisSelected) Color(0xFFE0E0FF) else Color.Transparent
                            val borderColor = if (isThisSelected) BrandPrimary else AppOutline
                            val textColor = if (isThisSelected) BrandPrimary else TextPrimary
                            val alpha = if (isAnswered && !isThisSelected) 0.5f else 1f

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .clickable(enabled = !isAnswered) {
                                        answeredMap = answeredMap + (q.id to opt)
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = bgColor.copy(alpha = alpha),
                                border = BorderStroke(1.dp, borderColor.copy(alpha = alpha))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${labels.getOrElse(i) { "" }}.",
                                        color = TextMuted,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(opt.parseHtml(), color = textColor, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

            if (answeredMap.size == 10) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = AppSurfaceAlt,
                            border = BorderStroke(1.dp, AppOutline),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Verified,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "পরীক্ষা শেষ হয়েছে?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "আপনার সকল উত্তর পুনরায় যাচাই করে নিন।",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = {
                                submitQuiz(
                                    subTopicId,
                                    quizQuestions,
                                    answeredMap,
                                    360 - timeLeft,
                                    navController,
                                    viewModel
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                        ) {
                            Text("সাবমিট করুন", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(12.dp))
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

private fun submitQuiz(
    subTopicId: String,
    questions: List<com.example.data.MCQQuestionEntity>,
    answers: Map<String, String>,
    timeTaken: Int,
    navController: NavController,
    viewModel: GKViewModel
) {
    var correct = 0
    var wrong = 0
    questions.forEach { q ->
        val userAns = answers[q.id]
        if (userAns != null) {
            if (userAns == q.correctAnswer) correct++ else wrong++
        }
    }
    val score = correct * 1.0f - wrong * 0.25f
    viewModel.saveQuizResult(subTopicId, score.toDouble(), questions.size, correct, wrong)
    navController.navigate(
        MCQQuizResultRoute(subTopicId, score, 10, correct, wrong, timeTaken)
    ) {
        popUpTo<MCQQuizLiveRoute> { inclusive = true }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQQuizResultScreen(
    subTopicId: String,
    score: Float,
    total: Int,
    correct: Int,
    wrong: Int,
    timeTakenSeconds: Int,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Admission GK",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandPrimary)
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = AppSurfaceAlt,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                CircularProgressIndicator(
                    progress = { correct / total.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = BrandSecondary,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$correct/$total",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandPrimary
                    )
                    Text(
                        "স্কোর অর্জন",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 2.sp
                    )
                }

                Surface(
                    color = BrandAccent,
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = 16.dp, y = (-8).dp)
                ) {
                    Text(
                        "টপ ৫%",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "চমৎকার পারফরম্যান্স!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                "আপনি এই টপিকের মূল বিষয়গুলো আয়ত্ত করেছেন। এই গতি বজায় রাখুন!",
                fontSize = 16.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.CheckCircle,
                    iconColor = SuccessColor,
                    value = String.format("%02d", correct),
                    label = "সঠিক"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Cancel,
                    iconColor = ErrorColor,
                    value = String.format("%02d", wrong),
                    label = "ভুল"
                )
                val mins = timeTakenSeconds / 60
                val secs = timeTakenSeconds % 60
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Timer,
                    iconColor = BrandPrimary,
                    value = String.format("%02d:%02d", mins, secs),
                    label = "সময়"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GradientMegaQuiz, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Insights,
                            contentDescription = null,
                            tint = BrandAccent
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "অগ্রগতি বিশ্লেষণ",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "মোট স্কোর: $score (ভুল উত্তরের জন্য −০.২৫ করে কাটা হয়েছে)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { navController.navigate(MCQPracticeRoute(subTopicId)) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Icon(Icons.Filled.Visibility, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("উত্তর দেখুন", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate(MCQQuizLiveRoute(subTopicId)) {
                                popUpTo<MCQQuizResultRoute> { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AppSurface,
                            contentColor = BrandPrimary
                        ),
                        border = BorderStroke(1.dp, AppOutline)
                    ) {
                        Icon(Icons.Filled.Replay, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("আবার অনুশীলন", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            navController.navigate(
                                SubTopicDetailRoute(subTopicId, "Topic")
                            ) {
                                popUpTo(SubTopicDetailRoute(subTopicId, "Topic")) { inclusive = false }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AppSurface,
                            contentColor = TextSecondary
                        ),
                        border = BorderStroke(1.dp, AppOutline)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("সাবটপিকে ফিরুন", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    value: String,
    label: String,
) {
    Surface(
        modifier = modifier,
        color = AppSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AppOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = TextPrimary
            )
            Text(label, fontSize = 14.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    value: String,
    label: String,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        border = BorderStroke(1.dp, AppOutline),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = TextPrimary
            )
            Text(label, fontSize = 14.sp, color = TextSecondary)
        }
    }
}

@Composable
fun MegaQuizIntroScreen(
    examId: String,
    viewModel: GKViewModel,
    navController: NavController,
) {
    val exams by viewModel.megaQuizExams.collectAsStateWithLifecycle(initialValue = emptyList())
    val exam = remember(examId, exams) { exams.find { it.id == examId } }

    val now = System.currentTimeMillis()
    val startWindow = remember(exam) {
        if (exam != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = exam.examDate
            cal.set(Calendar.HOUR_OF_DAY, 21)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        } else 0L
    }
    val endWindow = remember(startWindow) {
        if (startWindow > 0) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = startWindow
            cal.add(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 13)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        } else 0L
    }
    val canStart = exam != null // Allow testing at any time, but require a valid exam with seeded questions
    val windowStatus = remember(startWindow, endWindow) {
        val n = System.currentTimeMillis()
        when {
            endWindow > 0 && n > endWindow -> "ENDED"
            startWindow > 0 && n >= startWindow -> "LIVE"
            else -> "UPCOMING"
        }
    }

    val serial = if (exam != null) exam.id.filter { it.isDigit() }.take(2).padStart(2, '0') else "—"
    val examTitle = "মেগা কুইজ $serial"
    val examSubtitle = exam?.title ?: ""

    val dateStr = remember(exam?.examDate) {
        if (exam != null) {
            try {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                sdf.format(Date(exam.examDate))
            } catch (e: Exception) { "—" }
        } else "—"
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FF),
        bottomBar = {}
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Header ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Brush.linearGradient(listOf(Color(0xFF2E2395), Color(0xFF3F36D9), Color(0xFF6C4DFF))))
                ) {
                    Box(Modifier.size(220.dp).offset(x = (-60).dp, y = (-60).dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)), CircleShape))
                    Box(Modifier.size(180.dp).align(Alignment.BottomEnd).offset(x = 40.dp, y = 30.dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)), CircleShape))

                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 40.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                            }
                            Spacer(Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(26.dp), contentScale = ContentScale.Fit)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(examTitle, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(10.dp))
                                    StatusBadge(windowStatus)
                                }
                                Text(examSubtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFDDE3FF), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            // ── Exam Summary Card ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().drawBehind {
                            drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                        }.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Description, null, tint = Color(0xFF2638D9), modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("পরীক্ষার সারসংক্ষেপ", style = MaterialTheme.typography.titleSmall, color = Color(0xFF07113F), fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(14.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                SummaryItem(icon = Icons.Filled.Timer, label = "সময়সীমা", value = "১৫ মিনিট")
                                SummaryItem(icon = Icons.Filled.Grade, label = "মোট মার্ক", value = "৩০")
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                SummaryItem(icon = Icons.Filled.CalendarMonth, label = "তারিখ", value = dateStr)
                                SummaryItem(icon = Icons.Filled.Schedule, label = "পরীক্ষার সময়", value = "রাত ৯টা – দুপুর ১টা")
                            }
                        }
                    }
                }
            }

            // ── Rules Card ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().drawBehind {
                            drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                        }.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF6C4DFF).copy(alpha = 0.10f)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = Color(0xFF6C4DFF), modifier = Modifier.size(18.dp))
                                }
                                Spacer(Modifier.width(10.dp))
                                Text("পরীক্ষার নির্দেশনা", style = MaterialTheme.typography.titleSmall, color = Color(0xFF07113F), fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(14.dp))
                            RuleRow("পরীক্ষা শুরু করার পর টাইমার চালু হবে।")
                            RuleRow("১৫ মিনিট শেষ হলে পরীক্ষা স্বয়ংক্রিয়ভাবে শেষ হবে।")
                            RuleRow("একবার উত্তর সাবমিট করলে তা পরিবর্তন করা যাবে না।")
                            RuleRow("ইন্টারনেট সংযোগ স্থিতিশীল রাখুন।")
                            RuleRow("পরীক্ষা চলাকালীন app বন্ধ না করার চেষ্টা করুন।")

                            Spacer(Modifier.height(16.dp))
                            // Warning box
                            Box(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFF8E1)).drawBehind {
                                    drawRoundRect(color = Color(0xFFF6B93B).copy(alpha = 0.4f), cornerRadius = CornerRadius(12.dp.toPx()), style = Stroke(1.dp.toPx()))
                                }.padding(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Filled.Warning, null, tint = Color(0xFFF6B93B), modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("পরীক্ষা শুরু করার পর মাঝপথে বের হয়ে গেলে সময় চলতে থাকবে।", style = MaterialTheme.typography.bodySmall, color = Color(0xFF5E6480))
                                }
                            }
                        }
                    }
                }
            }

            // ── Availability + Start Button ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    // Availability card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = when {
                            canStart -> Color(0xFFF0FFF4)
                            windowStatus == "ENDED" -> Color(0xFFFFF7F8)
                            else -> Color(0xFFF7F6FF)
                        }),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().drawBehind {
                            drawRoundRect(
                                color = when {
                                    canStart -> Color(0xFF00A878).copy(alpha = 0.3f)
                                    windowStatus == "ENDED" -> Color(0xFFEF4444).copy(alpha = 0.3f)
                                    else -> Color(0xFF6C4DFF).copy(alpha = 0.25f)
                                },
                                cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx())
                            )
                        }.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    when {
                                        canStart -> Icons.Filled.CheckCircle
                                        windowStatus == "ENDED" -> Icons.Filled.Cancel
                                        else -> Icons.Filled.Schedule
                                    },
                                    null,
                                    tint = when {
                                        canStart -> Color(0xFF00A878)
                                        windowStatus == "ENDED" -> Color(0xFFEF4444)
                                        else -> Color(0xFF6C4DFF)
                                    },
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        when {
                                            canStart -> "আপনি প্রস্তুত"
                                            windowStatus == "ENDED" -> "পরীক্ষার সময় শেষ"
                                            else -> "পরীক্ষা নির্ধারিত সময়ে শুরু হবে"
                                        },
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xFF07113F),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        when {
                                            canStart -> "এখন মেগা কুইজে অংশ নিতে পারবেন"
                                            windowStatus == "ENDED" -> "এই পরীক্ষায় আর অংশ নেওয়া যাবে না"
                                            else -> "Start button রাত ৯টায় সক্রিয় হবে"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF5E6480),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Start button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = if (canStart) Color(0xFF2638D9) else Color(0xFFE1E6F5),
                        shadowElevation = if (canStart) 4.dp else 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().then(
                                if (canStart) Modifier.clickable { navController.navigate(MegaQuizLiveRoute(examId)) }
                                else Modifier
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    when {
                                        canStart -> "পরীক্ষা শুরু করুন"
                                        windowStatus == "ENDED" -> "পরীক্ষার সময় শেষ"
                                        else -> "পরীক্ষা এখনো শুরু হয়নি"
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (canStart) Color.White else Color(0xFF8A90A8),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    when {
                                        canStart -> "শুরু করার পর ১৫ মিনিট সময় পাবেন"
                                        windowStatus == "ENDED" -> "এই মেগা কুইজে অংশগ্রহণের সময় শেষ হয়েছে"
                                        else -> "পরীক্ষা শুরু হবে রাত ৯টায়"
                                    },
                                    fontSize = 10.sp,
                                    color = if (canStart) Color.White.copy(alpha = 0.75f) else Color(0xFFB0B6C8),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "LIVE" -> Color(0xFFEF4444).copy(alpha = 0.25f) to Color(0xFFEF4444)
        "ENDED" -> Color(0xFF8A90A8).copy(alpha = 0.20f) to Color(0xFF8A90A8)
        else -> Color(0xFF6C4DFF).copy(alpha = 0.20f) to Color(0xFF6C4DFF)
    }
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = bgColor,
        border = BorderStroke(0.5.dp, textColor.copy(alpha = 0.3f))
    ) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
            if (status == "LIVE") {
                val inf = rememberInfiniteTransition(label = "liveDot")
                val dotAlpha by inf.animateFloat(0.4f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "dot")
                Box(Modifier.size(5.dp).padding(end = 3.dp).clip(CircleShape).background(textColor.copy(alpha = dotAlpha)))
            }
            Text(
                status,
                fontSize = 10.sp, color = textColor, fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun SummaryItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Color(0xFF6C4DFF), modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleSmall, color = Color(0xFF07113F), fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF5E6480))
    }
}

@Composable
private fun RuleRow(text: String) {
    Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
        Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF00A878), modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF5E6480))
    }
}
