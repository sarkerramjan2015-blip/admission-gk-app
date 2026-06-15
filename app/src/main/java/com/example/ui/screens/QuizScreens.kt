package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import com.example.ui.GKViewModel
import com.example.ui.util.parseHtml
import com.example.ui.navigation.MCQQuizLiveRoute
import com.example.ui.navigation.MCQQuizResultRoute
import com.example.ui.navigation.MCQPracticeRoute
import com.example.ui.navigation.SubTopicDetailRoute
import com.example.ui.navigation.MegaQuizLiveRoute
import kotlinx.coroutines.delay

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
            submitQuiz(subTopicId, quizQuestions, answeredMap, 360 - timeLeft, navController)
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
                                    navController
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizIntroScreen(
    examId: String,
    viewModel: GKViewModel,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("মেগা কুইজ নিয়ম", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GradientMegaQuiz)
            )

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(32.dp))

                Surface(
                    color = AppSurface,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            "মেগা কুইজের নিয়মাবলী",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = BrandPrimary
                        )
                        Spacer(Modifier.height(16.dp))

                        RuleItem(Icons.AutoMirrored.Filled.List, "৩০টি বহুনির্বাচনী প্রশ্ন")
                        RuleItem(Icons.Filled.Timer, "২০ মিনিট সময়সীমা")
                        RuleItem(Icons.Filled.CheckCircle, "প্রতি সঠিক উত্তরের জন্য ১ নম্বর")
                        RuleItem(
                            Icons.Filled.Cancel,
                            "প্রতি ভুল উত্তরের জন্য −০.২৫ নম্বর কাটা হবে"
                        )

                        Spacer(Modifier.height(32.dp))

                        var agreed by remember { mutableStateOf(false) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { agreed = !agreed }
                        ) {
                            Checkbox(checked = agreed, onCheckedChange = { agreed = it })
                            Text("আমি কুইজের সকল নিয়ম মেনে নিচ্ছি", color = TextSecondary)
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = { navController.navigate(MegaQuizLiveRoute(examId)) },
                            enabled = agreed,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                        ) {
                            Text("পরীক্ষা শুরু করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RuleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 16.sp, color = TextSecondary)
    }
}
