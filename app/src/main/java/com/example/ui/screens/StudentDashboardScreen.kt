package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.example.data.MCQPracticeProgressEntity
import com.example.data.MCQQuizResultEntity
import com.example.data.MegaQuizResultEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.*
import com.example.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StudentDashboardScreen(viewModel: GKViewModel, navController: NavController) {
    val totalPracticed by viewModel.totalPracticedMCQs.collectAsStateWithLifecycle()
    val totalCorrect by viewModel.totalCorrectMCQs.collectAsStateWithLifecycle()
    val totalQuizAttempts by viewModel.totalQuizAttempts.collectAsStateWithLifecycle()
    val totalMegaQuizAttempts by viewModel.totalMegaQuizAttempts.collectAsStateWithLifecycle()
    val totalActiveDays by viewModel.totalActiveDays.collectAsStateWithLifecycle()
    val weekProgress by viewModel.weekProgress.collectAsStateWithLifecycle()
    val recentQuizResults by viewModel.recentQuizResults.collectAsStateWithLifecycle()
    val recentMegaQuizResults by viewModel.recentMegaQuizResults.collectAsStateWithLifecycle()
    val currentMonthProgress by viewModel.currentMonthProgress.collectAsStateWithLifecycle()
    val currentMonthActiveDays by viewModel.currentMonthActiveDays.collectAsStateWithLifecycle()
    val bdTopics by viewModel.bdTopics.collectAsStateWithLifecycle()
    val intTopics by viewModel.intTopics.collectAsStateWithLifecycle()

    val today = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val monthName = remember { currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) }

    val accuracy = if (totalPracticed > 0) (totalCorrect.toFloat() / totalPracticed * 100).toInt() else 0
    val monthDaysTotal = currentMonth.lengthOfMonth()
    val streakEmoji = when {
        totalActiveDays >= 30 -> "\uD83D\uDD25"
        totalActiveDays >= 14 -> "\uD83D\uDE80"
        totalActiveDays >= 7 -> "\u2B50"
        totalActiveDays >= 1 -> "\uD83C\uDF31"
        else -> "\uD83D\uDC4B"
    }

    // Weekly chart data: last 7 days with practice count
    val weekDays = remember(today, weekProgress) {
        (6 downTo 0).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val progress = weekProgress.find { it.dateStr == dateStr }
            val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            Triple(dayLabel, progress?.totalPracticed ?: 0, date == today)
        }
    }
    val weekMax = weekDays.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

    val hour = runCatching { java.time.LocalTime.now().hour }.getOrDefault(12)
    val greeting = when { hour < 12 -> "Good Morning"; hour < 17 -> "Good Afternoon"; hour < 20 -> "Good Evening"; else -> "Good Night" }

    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        // Background grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 40.dp.toPx()
            val lineColor = Color(0xFFE2E4FF).copy(alpha = 0.12f)
            var x = 0f
            while (x < size.width) { drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.7f); x += gridSize }
            var y = 0f
            while (y < size.height) { drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.7f); y += gridSize }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = AppBackground, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Student Dashboard", style = MaterialTheme.typography.titleLarge, color = BrandPrimary, fontWeight = FontWeight.Bold)
                            Text("Your complete admission prep overview", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(999.dp), color = BrandPrimary.copy(alpha = 0.06f), border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.10f))) {
                            Text("$streakEmoji $totalActiveDays Days", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            bottomBar = { PremiumBottomBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Hero Section ────────────────────────
                item {
                    DashboardHero(
                        greeting = greeting,
                        name = "Admission Aspirant",
                        totalPracticed = totalPracticed,
                        accuracy = accuracy,
                        activeDays = currentMonthActiveDays,
                        totalDays = monthDaysTotal,
                        monthName = monthName,
                        quizAttempts = totalQuizAttempts,
                        megaQuizAttempts = totalMegaQuizAttempts
                    )
                }

                // ── Quick Action Cards ──────────────────
                item {
                    QuickActionsRow(navController = navController)
                }

                // ── Weekly Activity Chart ───────────────
                item {
                    WeeklyActivityCard(weekDays = weekDays, weekMax = weekMax)
                }

                // ── Subject-wise Progress ───────────────
                item {
                    SubjectProgressSection(
                        bdTopicCount = bdTopics.size,
                        intTopicCount = intTopics.size,
                        totalPracticed = totalPracticed,
                        navController = navController
                    )
                }

                // ── Month Consistency ──────────────────
                item {
                    MonthlyConsistencyCard(
                        activeDays = currentMonthActiveDays,
                        totalDays = monthDaysTotal,
                        monthName = monthName,
                        monthProgress = currentMonthProgress
                    )
                }

                // ── Recent Quiz Results ─────────────────
                if (recentQuizResults.isNotEmpty()) {
                    item {
                        SectionTitle("Latest Quiz Results", "See All") { }
                    }
                    items(recentQuizResults.take(5)) { result ->
                        QuizResultRow(result = result)
                    }
                }

                // ── Recent Mega Quiz Results ────────────
                if (recentMegaQuizResults.isNotEmpty()) {
                    item {
                        SectionTitle("Latest Mega Quiz Results", "See All") {
                            navController.navigate(MegaQuizRoutineRoute)
                        }
                    }
                    items(recentMegaQuizResults.take(5)) { result ->
                        MegaQuizResultRow(result = result)
                    }
                }

                // ── Achievement Milestones ──────────────
                item {
                    AchievementsCard(
                        totalPracticed = totalPracticed,
                        totalActiveDays = totalActiveDays,
                        totalQuizAttempts = totalQuizAttempts,
                        totalMegaQuizAttempts = totalMegaQuizAttempts
                    )
                }

                // ── Bottom Spacer ───────────────────────
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

// ── Dashboard Hero ────────────────────────────────────
@Composable
private fun DashboardHero(
    greeting: String,
    name: String,
    totalPracticed: Int,
    accuracy: Int,
    activeDays: Int,
    totalDays: Int,
    monthName: String,
    quizAttempts: Int,
    megaQuizAttempts: Int
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(600), label = "eA")
    val entOffset by animateFloatAsState(if (appeared) 0f else 30f, tween(600, easing = FastOutSlowInEasing), label = "eO")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .alpha(entAlpha)
            .offset(y = entOffset.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(listOf(BrandPrimaryDeep, BrandPrimary, BrandSecondary))
            )
            .padding(24.dp)
    ) {
        Column {
            Text("$greeting \uD83D\uDC4B", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(name, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.ExtraBold)

            Spacer(Modifier.height(20.dp))

            // Big stat row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroStatBox(totalPracticed.toString(), "MCQs\nSolved", Modifier.weight(1f))
                HeroStatBox("$accuracy%", "Overall\nAccuracy", Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroStatBox("$activeDays/$totalDays", "$monthName\nActive Days", Modifier.weight(1f))
                HeroStatBox("$quizAttempts", "Quiz\nAttempts", Modifier.weight(1f))
            }
        }

        // Watermark emoji
        Text(
            "\uD83D\uDCDA",
            fontSize = 80.sp,
            color = Color.White.copy(alpha = 0.05f),
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-8).dp, y = 8.dp)
        )
    }
}

@Composable
private fun HeroStatBox(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.18f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.78f), textAlign = TextAlign.Center, lineHeight = 14.sp)
        }
    }
}

// ── Quick Actions Row ─────────────────────────────────
@Composable
private fun QuickActionsRow(navController: NavController) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "qa")

    val actions = listOf(
        QuickAction(Icons.Filled.Public, "BD GK", BdGkColor) { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) },
        QuickAction(Icons.Filled.Language, "INT GK", IntGkColor) { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) },
        QuickAction(Icons.Filled.EmojiEvents, "Mega Quiz", MegaQuizColor) { navController.navigate(MegaQuizRoutineRoute) },
        QuickAction(Icons.Filled.HistoryEdu, "Q-Bank", QuestionBankColor) { navController.navigate(QuestionBankRoute(null, null)) { launchSingleTop = true } },
        QuickAction(Icons.Filled.Quiz, "Practice", BrandAccent) { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) },
        QuickAction(Icons.Filled.RssFeed, "GK Feed", BrandPrimary) { navController.navigate(RecentGKRoute) }
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp).alpha(entAlpha)) {
        Text("Quick Actions", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            actions.forEach { action ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, action.accent.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                        .clickable(onClick = action.onClick)
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = action.accent.copy(alpha = 0.08f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(action.icon, contentDescription = null, tint = action.accent, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(action.label, style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold, maxLines = 1)
                }
            }
        }
    }
}

private data class QuickAction(val icon: ImageVector, val label: String, val accent: Color, val onClick: () -> Unit)

// ── Weekly Activity Chart ─────────────────────────────
@Composable
private fun WeeklyActivityCard(weekDays: List<Triple<String, Int, Boolean>>, weekMax: Int) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "wa")

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).alpha(entAlpha),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Weekly Activity", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("MCQ practice over the last 7 days", style = MaterialTheme.typography.labelSmall, color = TextSecondary)

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weekDays.forEach { (dayLabel, count, isToday) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            count.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (count > 0) BrandPrimary else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))

                        val barHeight by animateFloatAsState(
                            if (count > 0) ((count.toFloat() / weekMax) * 100.dp.value) else 4f,
                            tween(800, easing = FastOutSlowInEasing),
                            label = "bar$dayLabel"
                        )
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(barHeight.dp.coerceAtLeast(4.dp))
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 6.dp, bottomEnd = 6.dp))
                                .let { mod ->
                                    if (count > 0) mod.background(Brush.verticalGradient(listOf(BrandPrimary, BrandSecondary)))
                                    else mod.background(Color(0xFFE8E8F0))
                                }
                        )
                        if (isToday) {
                            Spacer(Modifier.height(2.dp))
                            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandAccent))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isToday) BrandPrimary else TextMuted,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// ── Subject Progress Section ──────────────────────────
@Composable
private fun SubjectProgressSection(bdTopicCount: Int, intTopicCount: Int, totalPracticed: Int, navController: NavController) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "sp")

    Column(modifier = Modifier.padding(horizontal = 16.dp).alpha(entAlpha)) {
        Text("Subject Progress", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SubjectCard(
                modifier = Modifier.weight(1f),
                title = "BD GK",
                topicCount = bdTopicCount,
                color = BdGkColor,
                gradient = GradientBdGkBg,
                emoji = "\uD83C\uDDE7\uD83C\uDDE9",
                onClick = { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
            )
            SubjectCard(
                modifier = Modifier.weight(1f),
                title = "INT GK",
                topicCount = intTopicCount,
                color = IntGkColor,
                gradient = GradientIntlGkBg,
                emoji = "\uD83C\uDF0D",
                onClick = { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) }
            )
        }

        Spacer(Modifier.height(12.dp))

        // Practice progress bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = BrandAccent.copy(alpha = 0.08f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Daily Goal Progress", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    val goal = 50
                    val fraction = (totalPracticed.toFloat() / goal).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFFE8E8F0))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(fraction).height(8.dp).clip(RoundedCornerShape(999.dp))
                                .background(Brush.horizontalGradient(listOf(BrandAccent, BrandPrimary)))
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text("$totalPracticed / $goal MCQs", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun SubjectCard(
    modifier: Modifier,
    title: String,
    topicCount: Int,
    color: Color,
    gradient: Brush,
    emoji: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(20.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        shadowElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.matchParentSize().clip(RoundedCornerShape(20.dp)).background(gradient))
            Text(emoji, fontSize = 48.sp, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp), color = Color.White.copy(alpha = 0.12f))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(topicCount.toString(), style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
                Text(title, style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Explore", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

// ── Monthly Consistency Card ──────────────────────────
@Composable
private fun MonthlyConsistencyCard(activeDays: Int, totalDays: Int, monthName: String, monthProgress: List<MCQPracticeProgressEntity>) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "mc")

    val fraction = if (totalDays > 0) activeDays.toFloat() / totalDays else 0f

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).alpha(entAlpha),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("$monthName Consistency", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Surface(shape = CircleShape, color = BrandPrimary.copy(alpha = 0.08f), modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("$activeDays", style = MaterialTheme.typography.titleLarge, color = BrandPrimary, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text("$activeDays of $totalDays days active", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFFE8E8F0))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(fraction.coerceIn(0f, 1f)).height(10.dp).clip(RoundedCornerShape(999.dp))
                        .background(Brush.horizontalGradient(listOf(BrandPrimary, BrandSecondary)))
                )
            }

            if (activeDays > 0) {
                Spacer(Modifier.height(14.dp))
                Text("Activity Heatmap", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                // Simple day grid
                val daysInMonth = monthProgress.associateBy { it.dateStr }
                val firstDay = java.time.LocalDate.of(java.time.YearMonth.now().year, java.time.YearMonth.now().month, 1)
                val days = (1..totalDays).map { firstDay.withDayOfMonth(it) }

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    days.forEach { date ->
                        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        val progress = daysInMonth[dateStr]
                        val intensity = if (progress != null && progress.totalPracticed > 0) {
                            when {
                                progress.totalPracticed >= 20 -> 1f
                                progress.totalPracticed >= 10 -> 0.7f
                                else -> 0.4f
                            }
                        } else 0.08f
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (progress != null && progress.totalPracticed > 0)
                                        BrandPrimary.copy(alpha = intensity)
                                    else Color(0xFFE8E8F0)
                                )
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Less", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFE8E8F0)))
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary.copy(alpha = 0.4f)))
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary.copy(alpha = 0.7f)))
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary))
                    Text("More", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

// ── Achievements Card ─────────────────────────────────
@Composable
private fun AchievementsCard(totalPracticed: Int, totalActiveDays: Int, totalQuizAttempts: Int, totalMegaQuizAttempts: Int) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "ac")

    data class Achievement(val icon: ImageVector, val emoji: String, val title: String, val unlocked: Boolean, val desc: String, val color: Color)

    val achievements = listOf(
        Achievement(Icons.Filled.EmojiEvents, "\uD83C\uDFC6", "Practice Starter", totalPracticed >= 10, "Solve 10 MCQs total", BrandAccent),
        Achievement(Icons.Filled.LocalFireDepartment, "\uD83D\uDD25", "7-Day Streak", totalActiveDays >= 7, "Active for 7 days", WarningColor),
        Achievement(Icons.Filled.Rocket, "\uD83D\uDE80", "Quiz Pro", totalQuizAttempts >= 5, "Complete 5 quizzes", BrandSecondary),
        Achievement(Icons.Filled.Stars, "\uD83C\uDF1F", "Mega Master", totalMegaQuizAttempts >= 1, "Attempt 1 Mega Quiz", MegaQuizColor),
        Achievement(Icons.Filled.Diamond, "\uD83D\uDC8E", "100 Club", totalPracticed >= 100, "Solve 100 MCQs", BrandPrimary),
        Achievement(Icons.Filled.AutoAwesome, "\uD83C\uDF0A", "30-Day Champion", totalActiveDays >= 30, "Active for 30 days", Color(0xFFEC4899))
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp).alpha(entAlpha)) {
        Text("Achievements", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 3.dp,
            border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                achievements.forEach { ach ->
                    Column(
                        modifier = Modifier.width(100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (ach.unlocked) ach.color.copy(alpha = 0.12f) else Color(0xFFF2F2F7),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    ach.emoji,
                                    fontSize = 22.sp,
                                    modifier = if (!ach.unlocked) Modifier.alpha(0.35f) else Modifier
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            ach.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (ach.unlocked) TextPrimary else TextMuted,
                            fontWeight = if (ach.unlocked) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                        Text(
                            ach.desc,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

// ── Section Title ─────────────────────────────────────
@Composable
private fun SectionTitle(title: String, action: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(
            action,
            modifier = Modifier.clip(RoundedCornerShape(999.dp)).clickable(onClick = onClick).padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = BrandPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Quiz Result Row ───────────────────────────────────
@Composable
private fun QuizResultRow(result: MCQQuizResultEntity) {
    val dateStr = runCatching {
        java.time.Instant.ofEpochMilli(result.dateTaken)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }.getOrDefault("Unknown")

    val resultColor = when {
        result.scorePercentage >= 70 -> SuccessColor
        result.scorePercentage >= 40 -> BrandAccent
        else -> ErrorColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, resultColor.copy(alpha = 0.12f))
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = resultColor.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("${result.scorePercentage.toInt()}%", style = MaterialTheme.typography.titleSmall, color = resultColor, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("MCQ Quiz", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Icon(Icons.Filled.TrendingUp, null, tint = resultColor, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Mega Quiz Result Row ──────────────────────────────
@Composable
private fun MegaQuizResultRow(result: MegaQuizResultEntity) {
    val dateStr = runCatching {
        java.time.Instant.ofEpochMilli(result.dateTaken)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }.getOrDefault("Unknown")

    val resultColor = when {
        result.score >= 70 -> SuccessColor
        result.score >= 40 -> BrandAccent
        else -> ErrorColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, resultColor.copy(alpha = 0.12f))
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = resultColor.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("${result.score.toInt()}%", style = MaterialTheme.typography.titleSmall, color = resultColor, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Mega Quiz", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("$dateStr  •  ${result.correctCount}C/${result.wrongCount}W", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Icon(Icons.Filled.EmojiEvents, null, tint = resultColor, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Bottom Navigation (replicated for standalone use) ──
@Composable
private fun PremiumBottomBar(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).navigationBarsPadding(),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xE6FFFFFF),
        shadowElevation = 8.dp
    ) {
        Box {
            Box(modifier = Modifier.matchParentSize().border(0.5.dp, Color(0x33C7C0EE), RoundedCornerShape(28.dp)))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                NavItem(Icons.Filled.Home, "Home", true) { navController.navigate(HomeRoute) { launchSingleTop = true } }
                NavItem(Icons.Filled.Public, "GK", false) { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
                NavItem(Icons.AutoMirrored.Filled.MenuBook, "Study", false) { navController.navigate(QuestionBankRoute(null, null)) { launchSingleTop = true } }
                NavItem(Icons.Filled.EmojiEvents, "Quiz", false) { navController.navigate(MegaQuizRoutineRoute) { launchSingleTop = true } }
                NavItem(Icons.Filled.Person, "Profile", false) { navController.navigate(ProfileRoute) { launchSingleTop = true } }
            }
        }
    }
}

@Composable
private fun NavItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    val cc = if (selected) BrandPrimary else TextMuted
    val sc by animateFloatAsState(if (selected) 1.15f else 1f, tween(300, easing = FastOutSlowInEasing), label = "ns")
    Column(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (selected) BrandPrimary.copy(alpha = 0.05f) else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, label, tint = cc, modifier = Modifier.size(if (selected) 24.dp else 20.dp).scale(sc))
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = cc, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, maxLines = 1)
        if (selected) { Spacer(Modifier.height(2.dp)); Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandPrimary)) }
    }
}
