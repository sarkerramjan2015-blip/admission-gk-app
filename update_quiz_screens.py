import re

file_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\QuizScreens.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# We need to remove `MCQQuizScreen` and `QuizResultScreen`
# We'll use regex to match these top-level functions

# Remove MCQQuizScreen
content = re.sub(r'@OptIn\(ExperimentalMaterial3Api::class\)\s*@Composable\s*fun MCQQuizScreen\(.*?^}\n', '', content, flags=re.MULTILINE | re.DOTALL)

# Remove QuizResultScreen
content = re.sub(r'@OptIn\(ExperimentalMaterial3Api::class\)\s*@Composable\s*fun QuizResultScreen\(.*?^}\n', '', content, flags=re.MULTILINE | re.DOTALL)


# Now we append the new screens to the file.
new_screens = """
import androidx.compose.ui.text.style.TextDecoration
import com.example.ui.navigation.MCQQuizLiveRoute
import com.example.ui.navigation.MCQQuizResultRoute
import com.example.ui.navigation.SubTopicDetailRoute
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQQuizIntroScreen(subTopicId: String, viewModel: GKViewModel, navController: NavController) {
    var agreed by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admission Pro", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SnPrimaryColor)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { agreed = !agreed }) {
                        Checkbox(
                            checked = agreed,
                            onCheckedChange = { agreed = it },
                            colors = CheckboxDefaults.colors(checkedColor = SnPrimaryColor)
                        )
                        Text("I have read and understood all the quiz rules mentioned above.", color = SnOnSurfaceVariantColor, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(MCQQuizLiveRoute(subTopicId)) },
                        enabled = agreed,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SnPrimaryColor)
                    ) {
                        Text("Start Exam", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                Surface(color = SnPrimaryFixedColor, shape = RoundedCornerShape(8.dp)) {
                    Icon(Icons.Filled.Public, contentDescription = null, tint = SnOnPrimaryFixedVariantColor, modifier = Modifier.padding(8.dp).size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("GENERAL KNOWLEDGE", color = SnSecondaryColor, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
            }
            Text("Timed MCQ Exam", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = SnOnSurfaceColor)
            Text("ভৌগোলিক অবস্থান ও সীমানা বিষয়ক কুইজ", color = SnOnSurfaceVariantColor, modifier = Modifier.padding(top = 4.dp))
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowest),
                    border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha=0.3f))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Icon(Icons.Filled.Quiz, contentDescription = null, tint = SnPrimaryColor, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("10", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = SnOnSurfaceColor)
                        Text("Questions", fontSize = 14.sp, color = SnOnSurfaceVariantColor)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowest),
                    border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha=0.3f))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Icon(Icons.Filled.Timer, contentDescription = null, tint = SnSecondaryColor, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("06", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = SnOnSurfaceColor)
                        Text("Minutes", fontSize = 14.sp, color = SnOnSurfaceVariantColor)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SnMegaQuizGradient, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Verified, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Marking Scheme", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Correct", color = Color.White.copy(alpha=0.8f), fontSize = 14.sp)
                                Text("+1.0", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Wrong", color = Color.White.copy(alpha=0.8f), fontSize = 14.sp)
                                Text("-0.25", color = SnTertiaryFixedDimColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                color = SnSurfaceContainerLowColor,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(SnPrimaryColor))
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Surface(color = SnPrimaryContainerColor, shape = RoundedCornerShape(8.dp)) {
                            Icon(Icons.Filled.LockClock, contentDescription = null, tint = SnOnPrimaryContainerColor, modifier = Modifier.padding(8.dp).size(24.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Selection Lock", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SnOnSurfaceColor)
                            Spacer(Modifier.height(4.dp))
                            Text("Once an answer is selected, it cannot be changed. Review your choices carefully before tapping.", color = SnOnSurfaceVariantColor, fontSize = 14.sp, lineHeight = 20.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("একবার উত্তর নির্বাচন করলে তা আর পরিবর্তন করা যাবে না।", color = SnPrimaryColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
    val allMcqs by remember(subTopicId) { viewModel.getMCQsForSubTopic(subTopicId) }.collectAsStateWithLifecycle(initialValue = emptyList())
    
    // We want 10 questions randomly
    var quizQuestions by remember { mutableStateOf<List<com.example.data.MCQQuestionEntity>>(emptyList()) }
    // We want shuffled options for each question
    var questionOptions by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    
    var answeredMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var timeLeft by remember { mutableIntStateOf(360) } // 6 minutes
    
    LaunchedEffect(allMcqs) {
        if (allMcqs.isNotEmpty() && quizQuestions.isEmpty()) {
            val selected = allMcqs.shuffled().take(10)
            quizQuestions = selected
            
            val moshi = com.squareup.moshi.Moshi.Builder().build()
            val listType = com.squareup.moshi.Types.newParameterizedType(List::class.java, String::class.java)
            val jsonAdapter = moshi.adapter<List<String>>(listType)
            
            val optionsMap = mutableMapOf<String, List<String>>()
            selected.forEach { q ->
                val opts = try { jsonAdapter.fromJson(q.options) ?: emptyList() } catch(e: Exception) { emptyList() }
                optionsMap[q.id] = opts.shuffled()
            }
            questionOptions = optionsMap
        }
    }
    
    LaunchedEffect(quizQuestions) {
        if (quizQuestions.isNotEmpty()) {
            while(timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            // Auto submit when time is up
            submitQuiz(subTopicId, quizQuestions, answeredMap, 360 - timeLeft, navController)
        }
    }
    
    if (quizQuestions.isEmpty()) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Quiz Live") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            })
        }) { padding ->
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Admission Pro", fontWeight = FontWeight.Bold, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) { 
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) 
                        }
                    },
                    actions = {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(999.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                val mins = timeLeft / 60
                                val secs = timeLeft % 60
                                Icon(Icons.Filled.Timer, contentDescription = null, tint = SnTertiaryFixedDimColor, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(String.format("%02d:%02d", mins, secs), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(Modifier.width(12.dp))
                                Box(modifier = Modifier.width(1.dp).height(16.dp).background(Color.White.copy(alpha=0.2f)))
                                Spacer(Modifier.width(12.dp))
                                Icon(Icons.Filled.Assignment, contentDescription = null, tint = SnPrimaryFixedColor, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("${answeredMap.size}/10", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SnPrimaryColor.copy(alpha = 0.95f))
                )
                LinearProgressIndicator(
                    progress = { answeredMap.size / 10f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = SnSecondaryColor,
                    trackColor = SnSurfaceContainerColor
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(SnSurfaceColor)
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
                    colors = CardDefaults.cardColors(containerColor = SnSurfaceContainerLowestColor),
                    border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(crossAxisAlignment = Alignment.Top) {
                            Surface(
                                color = SnPrimaryFixedColor,
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("${index + 1}", color = SnOnPrimaryFixedVariantColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                            Spacer(Modifier.width(16.dp))
                            Text(q.questionText, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SnOnSurfaceColor, lineHeight = 28.sp)
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        val labels = listOf("A", "B", "C", "D")
                        opts.forEachIndexed { i, opt ->
                            val isThisSelected = selectedOpt == opt
                            val bgColor = if (isThisSelected) SnPrimaryFixedColor else Color.Transparent
                            val borderColor = if (isThisSelected) SnPrimaryColor else SnOutlineVariantColor
                            val textColor = if (isThisSelected) SnOnPrimaryFixedColor else SnOnSurfaceColor
                            
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
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("${labels.getOrElse(i) { "" }}.", color = SnOutlineColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(opt, color = textColor, fontSize = 16.sp)
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
                            color = SnSurfaceContainerLowColor,
                            border = BorderStroke(1.dp, SnOutlineVariantColor),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                                Icon(Icons.Filled.Verified, contentDescription = null, tint = SnPrimaryColor, modifier = Modifier.size(48.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("পরীক্ষা শেষ হয়েছে?", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = SnOnSurfaceColor)
                                Spacer(Modifier.height(4.dp))
                                Text("আপনার সকল উত্তর পুনরায় যাচাই করে নিন।", fontSize = 16.sp, color = SnOnSurfaceVariantColor)
                            }
                        }
                        
                        Button(
                            onClick = { submitQuiz(subTopicId, quizQuestions, answeredMap, 360 - timeLeft, navController) },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SnPrimaryColor)
                        ) {
                            Text("Submit Quiz", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(12.dp))
                            Icon(Icons.Filled.Send, contentDescription = null)
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
    navController: NavController
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
    navController.navigate(MCQQuizResultRoute(subTopicId, score, 10, correct, wrong, timeTaken)) {
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
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admission Pro", fontWeight = FontWeight.Bold, color = SnOnPrimaryColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SnOnPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SnPrimaryColor.copy(alpha = 0.95f))
            )
        },
        containerColor = SnSurfaceBlueColor
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Circular Score
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = SnSurfaceContainerColor,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                CircularProgressIndicator(
                    progress = { correct / total.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = SnSecondaryColor,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$correct/$total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = SnPrimaryColor)
                    Text("SCORE ACHIEVED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SnOnSurfaceVariantColor, letterSpacing = 2.sp)
                }
                
                Surface(
                    color = SnTertiaryFixedColor,
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = 16.dp, y = (-8).dp)
                ) {
                    Text("Top 5% 🚀", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = SnOnTertiaryFixedColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Excellent Performance!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SnOnSurfaceColor)
            Text("You've mastered the fundamentals of this topic. Keep this momentum going!", fontSize = 16.sp, color = SnOnSurfaceVariantColor, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))
            
            // Stats Bento Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(modifier = Modifier.weight(1f), icon = Icons.Filled.CheckCircle, iconColor = SnSuccessEmerald, value = String.format("%02d", correct), label = "Correct")
                StatCard(modifier = Modifier.weight(1f), icon = Icons.Filled.Cancel, iconColor = SnErrorRed, value = String.format("%02d", wrong), label = "Wrong")
                val mins = timeTakenSeconds / 60
                val secs = timeTakenSeconds % 60
                StatCard(modifier = Modifier.weight(1f), icon = Icons.Filled.Timer, iconColor = SnPrimaryColor, value = String.format("%02d:%02d", mins, secs), label = "Time")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress Analysis
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SnMegaQuizGradient, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Insights, contentDescription = null, tint = SnTertiaryFixedDimColor)
                        Spacer(Modifier.width(8.dp))
                        Text("Progress Analysis", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Total Calculated Score: $score (Negative marking applied: -0.25 for each wrong answer)", color = Color.White.copy(alpha=0.9f), fontSize = 16.sp, lineHeight = 24.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Actions
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /* Review Answers logic later */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SnPrimaryColor)
                ) {
                    Icon(Icons.Filled.Visibility, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Review Answers", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        onClick = { navController.navigate(MCQQuizLiveRoute(subTopicId)) { popUpTo<MCQQuizResultRoute> { inclusive = true } } },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = SnSurfaceContainerLowestColor, contentColor = SnPrimaryColor),
                        border = BorderStroke(1.dp, SnOutlineVariantColor)
                    ) {
                        Icon(Icons.Filled.Replay, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Practice Again", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { 
                            navController.navigate(SubTopicDetailRoute(subTopicId, "Topic")) {
                                popUpTo(SubTopicDetailRoute(subTopicId, "Topic")) { inclusive = false }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = SnSurfaceContainerLowestColor, contentColor = SnOnSurfaceVariantColor),
                        border = BorderStroke(1.dp, SnOutlineVariantColor)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Back to Subtopic", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, value: String, label: String) {
    Surface(
        modifier = modifier,
        color = SnSurfaceContainerLowestColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha=0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Surface(color = iconColor.copy(alpha=0.1f), shape = CircleShape, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp)) }
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = SnOnSurfaceColor)
            Text(label, fontSize = 14.sp, color = SnOnSurfaceVariantColor)
        }
    }
}

// Add these extra compose definitions at end since they use new compose components
"""

# Append to file
content += new_screens

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
