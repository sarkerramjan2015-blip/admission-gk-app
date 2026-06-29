package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import com.example.ui.GKViewModel
import com.example.ui.navigation.CategoryRoute
import com.example.ui.navigation.DashboardRoute
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.MegaQuizRoutineRoute
import com.example.ui.navigation.ProfileRoute
import com.example.ui.navigation.ProgressReportRoute
import com.example.ui.navigation.QuestionBankRoute
import com.example.ui.navigation.RecentGKRoute
import com.example.ui.theme.*
import java.time.LocalTime
import kotlinx.coroutines.delay

// ══════════════════════════════════════════════════════════════
// DESIGN TOKENS — Color System
// ══════════════════════════════════════════════════════════════

private object AppColors {
    // Background
    val backgroundBase = Color(0xFFF7F9FF)
    val backgroundSoft = Color(0xFFF5F7FF)
    val glowBlue = Color(0xFFEAF0FF)
    val glowPurple = Color(0xFFF0ECFF)
    val gridLine = Color(0xFFD8DFF5).copy(alpha = 0.10f)

    // Cards
    val cardWhite = Color(0xFFFFFFFF)
    val cardSoft = Color(0xFFFAFBFF)
    val cardTintedBlue = Color(0xFFF3F6FF)
    val cardTintedPurple = Color(0xFFF4F0FF)
    val cardTintedTeal = Color(0xFFEFFFF9)
    val cardBorder = Color(0xFFE5E9F8)
    val softDivider = Color(0xFFE1E6F5)

    // Brand
    val primaryIndigo = Color(0xFF2638D9)
    val brightIndigo = Color(0xFF2F3DEB)
    val deepNavy = Color(0xFF071A5E)
    val darkNavy = Color(0xFF08145E)
    val accentPurple = Color(0xFF6C4DFF)
    val softPurple = Color(0xFF7B5CFF)

    // Categories
    val bdTealStart = Color(0xFF009B7A)
    val bdTealEnd = Color(0xFF00B894)
    val intBlueStart = Color(0xFF2434D8)
    val intPurpleEnd = Color(0xFF6C4DFF)

    // Mega Quiz
    val megaStart = Color(0xFF071A5E)
    val megaMid = Color(0xFF1624B8)
    val megaEnd = Color(0xFF2537E8)
    val megaGlow = Color(0xFF6C4DFF)

    // Status
    val successGreen = Color(0xFF00A878)
    val errorRed = Color(0xFFEF4444)
    val warningGold = Color(0xFFF6B93B)
    val liveRed = Color(0xFFFF3B30)

    // Text
    val textPrimary = Color(0xFF07113F)
    val textSecondary = Color(0xFF5E6480)
    val textMuted = Color(0xFF8A90A8)
    val textOnDark = Color(0xFFFFFFFF)
    val textOnDarkSecondary = Color(0xFFDDE3FF)

    // Buttons
    val primaryBtnStart = Color(0xFF2F3DEB)
    val primaryBtnEnd = Color(0xFF7B5CFF)
    val tealBtnText = Color(0xFF007E68)
    val indigoBtnText = Color(0xFF2638D9)
    val softBtnBg = Color(0xFFEEF2FF)

    // Bottom Nav
    val navSelectedBg = Color(0xFFEEF0FF)
    val navSelectedIcon = Color(0xFF2638D9)
    val navUnselectedIcon = Color(0xFF7B819C)
    val navIndicator = Color(0xFF2638D9)
}

// ══════════════════════════════════════════════════════════════
// BACKGROUND COMPOSABLE
// ══════════════════════════════════════════════════════════════

@Composable
private fun HomeBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(AppColors.backgroundBase)) {
        // Top-left subtle blue glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    Brush.radialGradient(
                        listOf(AppColors.glowBlue.copy(alpha = 0.50f), AppColors.glowBlue.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
        // Top-right subtle purple glow
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 120.dp, y = (-60).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        listOf(AppColors.glowPurple.copy(alpha = 0.45f), AppColors.glowPurple.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
        // Very subtle blueprint grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 48.dp.toPx()
            val lineColor = AppColors.gridLine
            var x = 0f
            while (x < size.width) {
                drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.5f)
                x += gridSize
            }
            var y = 0f
            while (y < size.height) {
                drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
                y += gridSize
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// REUSABLE HELPERS
// ══════════════════════════════════════════════════════════════

@Composable
private fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = AppColors.cardWhite,
        shadowElevation = 1.dp
    ) {
        Box(modifier = Modifier.drawBehind {
            drawRoundRect(
                color = AppColors.cardBorder,
                cornerRadius = CornerRadius(20.dp.toPx()),
                style = Stroke(1.dp.toPx())
            )
        }) {
            content()
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = AppColors.primaryIndigo.copy(alpha = 0.08f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = AppColors.primaryIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        if (actionLabel != null && onAction != null) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    actionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = AppColors.primaryIndigo,
                    fontWeight = FontWeight.SemiBold
                )
                if (actionIcon != null) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        actionIcon,
                        contentDescription = null,
                        tint = AppColors.primaryIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressRing(
    progress: Float,
    value: String,
    label: String,
    ringColor: Color
) {
    val sweep by animateFloatAsState(
        targetValue = progress * 360f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "sweep_$label"
    )
    Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 6.dp.toPx()
            val r = (size.minDimension - strokeW) / 2
            drawArc(
                color = AppColors.softDivider,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeW / 2, strokeW / 2),
                size = Size(r * 2, r * 2),
                style = Stroke(strokeW, cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(strokeW / 2, strokeW / 2),
                size = Size(r * 2, r * 2),
                style = Stroke(strokeW, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                value,
                style = MaterialTheme.typography.titleSmall,
                color = ringColor,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                label,
                fontSize = 10.sp,
                color = AppColors.textMuted,
                maxLines = 1
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// MAIN HOMESCREEN
// ══════════════════════════════════════════════════════════════

@Composable
fun HomeScreen(viewModel: GKViewModel, navController: NavController) {
    val bdTopics by viewModel.bdTopics.collectAsStateWithLifecycle()
    val intTopics by viewModel.intTopics.collectAsStateWithLifecycle()
    val megaQuizExams by viewModel.megaQuizExams.collectAsStateWithLifecycle()
    val todayProgress by viewModel.todayProgress.collectAsStateWithLifecycle()
    val totalTopics by viewModel.totalTopicsCount.collectAsStateWithLifecycle()
    val totalSubTopics by viewModel.totalSubTopicsCount.collectAsStateWithLifecycle()
    val totalMCQs by viewModel.totalPracticeMCQCount.collectAsStateWithLifecycle()
    val totalMegaQuiz by viewModel.totalMegaQuizCount.collectAsStateWithLifecycle()

    val hour = runCatching { LocalTime.now().hour }.getOrDefault(12)
    val greeting = when {
        hour < 12 -> "Shuvo Kal"
        hour < 17 -> "Shuvo Dupur"
        hour < 20 -> "Shuvo Sonjha"
        else -> "Shuvo Ratri"
    }
    val upcomingExam = megaQuizExams.firstOrNull { it.status == "LIVE" || it.status == "UPCOMING" }
    val isLive = upcomingExam?.status == "LIVE"

    var countdownTick by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            countdownTick++
        }
    }
    val countdownParts = remember(upcomingExam?.examDate, countdownTick) {
        if (upcomingExam != null && upcomingExam.examDate > 0) {
            val remMs = upcomingExam.examDate - System.currentTimeMillis()
            if (remMs > 0) {
                val totalSec = remMs / 1000
                listOf(
                    "%02d".format(totalSec / 86400),
                    "%02d".format((totalSec % 86400) / 3600),
                    "%02d".format((totalSec % 3600) / 60),
                    "%02d".format(totalSec % 60)
                )
            } else null
        } else null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeBackground()
        androidx.compose.material3.Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { PremiumBottomBar(navController) }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    CompactHeader(
                        greeting = greeting,
                        dayStreak = "7",
                        onProgressClick = { navController.navigate(ProgressReportRoute) }
                    )
                }
                item {
                    AnimatedStatPills(totalTopics, totalSubTopics, totalMCQs, totalMegaQuiz)
                }
                item {
                    StudyCards(
                        bd = bdTopics.size,
                        int = intTopics.size,
                        onBd = { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) },
                        onInt = { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) }
                    )
                }
                item {
                    MegaQuizCard(
                        isLive = isLive,
                        examTitle = upcomingExam?.title,
                        countdownParts = countdownParts,
                        onClick = { navController.navigate(MegaQuizRoutineRoute) }
                    )
                }
                item {
                    RecentGkSection(
                        onClick = { navController.navigate(RecentGKRoute) }
                    )
                }
                item {
                    QuestionBankCard(
                        onClick = { navController.navigate(QuestionBankRoute(null, null)) }
                    )
                }
                item {
                    val p = todayProgress
                    TodayProgressRings(
                        practiced = p?.totalPracticed ?: 0,
                        correct = p?.correctCount ?: 0,
                        wrong = p?.wrongCount ?: 0,
                        navController = navController
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// HEADER
// ══════════════════════════════════════════════════════════════

@Composable
private fun CompactHeader(
    greeting: String,
    dayStreak: String,
    onProgressClick: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(500, delayMillis = 50, easing = FastOutSlowInEasing),
        label = "headerEnt"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(entAlpha),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Logo + Greeting
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(14.dp),
                color = AppColors.primaryIndigo.copy(alpha = 0.10f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        tint = AppColors.primaryIndigo,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Admission GK",
                    style = MaterialTheme.typography.titleLarge,
                    color = AppColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Ajker GK practice shuru korun",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right: Streak + Progress pills
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            // Streak pill
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = AppColors.softBtnBg,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = AppColors.warningGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "$dayStreak Days",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.primaryIndigo,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
            // Progress pill
            Surface(
                modifier = Modifier,
                shape = RoundedCornerShape(999.dp),
                color = AppColors.cardWhite,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .drawBehind {
                            drawRoundRect(
                                color = AppColors.cardBorder,
                                cornerRadius = CornerRadius(999.dp.toPx()),
                                style = Stroke(1.dp.toPx())
                            )
                        }
                        .clickable(onClick = onProgressClick)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Progress",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.primaryIndigo,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AppColors.primaryIndigo,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// STAT PILLS
// ══════════════════════════════════════════════════════════════

private data class PillData(val icon: ImageVector, val label: String, val count: Int, val accent: Color)

@Composable
private fun AnimatedStatPills(t1: Int, t2: Int, t3: Int, t4: Int) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }

    val pills = listOf(
        PillData(Icons.AutoMirrored.Filled.MenuBook, "Topics", t1, AppColors.primaryIndigo),
        PillData(Icons.Filled.Public, "Sub Topics", t2, AppColors.accentPurple),
        PillData(Icons.Filled.Quiz, "Practice MCQ", t3, AppColors.bdTealStart),
        PillData(Icons.Filled.EmojiEvents, "Mega Quiz", t4, AppColors.warningGold)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        pills.forEachIndexed { index, pill ->
            StatPillItem(
                modifier = Modifier.weight(1f),
                icon = pill.icon,
                label = pill.label,
                count = pill.count,
                accent = pill.accent,
                delay = index * 100,
                visible = appeared
            )
        }
    }
}

@Composable
private fun RowScope.StatPillItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    count: Int,
    accent: Color,
    delay: Int,
    visible: Boolean
) {
    val entranceAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "sta_$label"
    )
    val entranceScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = tween(500, delayMillis = delay, easing = FastOutSlowInEasing),
        label = "sts_$label"
    )
    val displayCount by animateFloatAsState(
        targetValue = if (visible) count.toFloat() else 0f,
        animationSpec = tween(700, delayMillis = delay + 150, easing = FastOutSlowInEasing),
        label = "stc_$label"
    )
    val countText = if (count == 0) "Soon" else displayCount.toInt().toString()

    Surface(
        modifier = modifier
            .alpha(entranceAlpha)
            .scale(entranceScale),
        shape = RoundedCornerShape(14.dp),
        color = AppColors.cardWhite,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = AppColors.cardBorder,
                        cornerRadius = CornerRadius(14.dp.toPx()),
                        style = Stroke(0.5.dp.toPx())
                    )
                }
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = accent.copy(alpha = 0.10f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                countText,
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.textPrimary,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.textMuted,
                maxLines = 1
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// STUDY CARDS
// ══════════════════════════════════════════════════════════════

@Composable
private fun StudyCards(bd: Int, int: Int, onBd: () -> Unit, onInt: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StudyCategoryCard(
            modifier = Modifier.weight(1f),
            title = "BD GK",
            subtitle = "Bangladesh Bishoyaboli",
            count = bd,
            label = "Topics",
            buttonText = "Start Learning",
            gradient = Brush.linearGradient(listOf(AppColors.bdTealStart, AppColors.bdTealEnd)),
            accent = AppColors.bdTealStart,
            onClick = onBd
        )
        StudyCategoryCard(
            modifier = Modifier.weight(1f),
            title = "INT. GK",
            subtitle = "Antorjatik Bishoyaboli",
            count = int,
            label = "Topics",
            buttonText = "Explore",
            gradient = Brush.linearGradient(listOf(AppColors.intBlueStart, AppColors.intPurpleEnd)),
            accent = AppColors.softPurple,
            onClick = onInt
        )
    }
}

@Composable
private fun StudyCategoryCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    count: Int,
    label: String,
    buttonText: String,
    gradient: Brush,
    accent: Color,
    onClick: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "sca_$title"
    )
    val entScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.9f,
        animationSpec = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "scs_$title"
    )

    Card(
        modifier = modifier
            .alpha(entAlpha)
            .scale(entScale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Gradient background
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(22.dp))
                    .background(gradient)
                    .shiningEffect()
            )
            // Subtle border
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.20f),
                            cornerRadius = CornerRadius(22.dp.toPx()),
                            style = Stroke(1.dp.toPx())
                        )
                    }
            )
            // Watermark icon
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    if (title.startsWith("BD")) Icons.Filled.School else Icons.Filled.Public,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.08f),
                    modifier = Modifier.size(64.dp)
                )
            }
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Spacer(Modifier.weight(1f))
                // Count badge
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.20f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.30f))
                ) {
                    Text(
                        "$count $label",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(14.dp))
                // CTA Button
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            buttonText,
                            style = MaterialTheme.typography.labelMedium,
                            color = accent,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// MEGA QUIZ HERO CARD
// ══════════════════════════════════════════════════════════════

@Composable
private fun MegaQuizCard(
    isLive: Boolean,
    examTitle: String?,
    countdownParts: List<String>?,
    onClick: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "mqEnt"
    )
    val entScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.92f,
        animationSpec = tween(600, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "mqScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(entAlpha)
            .scale(entScale)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(listOf(AppColors.megaStart, AppColors.megaMid, AppColors.megaEnd))
            )
            .shiningEffect()
            .clickable(onClick = onClick)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.18f), Color.White.copy(alpha = 0.06f))
                    ),
                    cornerRadius = CornerRadius(24.dp.toPx()),
                    style = Stroke(1.dp.toPx())
                )
            }
    ) {
        // ── Background glow orbs ──
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-40).dp, y = (-40).dp)
                .background(
                    Brush.radialGradient(
                        listOf(AppColors.megaGlow.copy(alpha = 0.14f), AppColors.megaGlow.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 30.dp, y = 20.dp)
                .background(
                    Brush.radialGradient(
                        listOf(AppColors.megaGlow.copy(alpha = 0.10f), AppColors.megaGlow.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
        // ── Trophy watermark ──
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                Icons.Filled.EmojiEvents,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.04f),
                modifier = Modifier.size(96.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // ── Top bar: Mega Quiz badge (left) + Days left (right) ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Mega Quiz badge
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.10f),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.18f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = AppColors.warningGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Mega Quiz",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                // Right: Days left
                if (countdownParts != null) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color.White.copy(alpha = 0.10f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.18f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                "${countdownParts[0]} Days Left",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Title area ──
            Text(
                "Mega Quiz Exam",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(Modifier.height(2.dp))
            Text(
                examTitle ?: "Mega Quiz 3: Biggan, Projukti o Sadharon Gyan",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.textOnDarkSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(12.dp))

            // ── Status + Countdown ──
            if (countdownParts != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status badge
                    if (isLive) {
                        LiveBadge()
                    } else {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color.White.copy(alpha = 0.08f),
                            border = androidx.compose.foundation.BorderStroke(
                                0.5.dp, Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Schedule,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    "Exam Starts In",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))

                // Countdown boxes
                MegaCountdownRow(parts = countdownParts)
            } else {
                // Fallback when no exam data
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(
                        0.5.dp, Color.White.copy(alpha = 0.10f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = AppColors.textOnDarkSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Next Mega Quiz exam schedule coming soon.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.textOnDarkSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Buttons ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Join Now - primary
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onClick)
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Join Now",
                            style = MaterialTheme.typography.labelLarge,
                            color = AppColors.primaryIndigo,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AppColors.primaryIndigo,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                // View Routine - outlined
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Transparent,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.White.copy(alpha = 0.45f),
                                    cornerRadius = CornerRadius(10.dp.toPx()),
                                    style = Stroke(1.dp.toPx())
                                )
                            }
                            .clickable(onClick = onClick)
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "View Routine",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveBadge() {
    val inf = rememberInfiniteTransition(label = "liveBadge")
    val dotAlpha by inf.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse),
        label = "liveDot"
    )
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = AppColors.liveRed.copy(alpha = 0.18f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AppColors.liveRed.copy(alpha = 0.30f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = dotAlpha))
            )
            Spacer(Modifier.width(5.dp))
            Text(
                "LIVE",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MegaCountdownRow(parts: List<String>) {
    val labels = listOf("Days", "Hrs", "Min", "Sec")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        parts.forEachIndexed { index, value ->
            MegaCountdownBox(value = value, label = labels[index])
        }
    }
}

@Composable
private fun RowScope.MegaCountdownBox(value: String, label: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .drawBehind {
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.18f),
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(0.5.dp.toPx())
                )
            }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                value,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                softWrap = false
            )
            Text(
                label,
                fontSize = 9.sp,
                color = AppColors.textOnDarkSecondary,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// RECENT GK SECTION
// ══════════════════════════════════════════════════════════════

@Composable
private fun RecentGkSection(onClick: () -> Unit) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(400),
        label = "rgkEnt"
    )

    Column(modifier = Modifier.alpha(entAlpha)) {
        SectionHeader(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = "Recent GK",
            actionLabel = "View All",
            actionIcon = Icons.AutoMirrored.Filled.ArrowForward,
            onAction = onClick
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            RecentGkCompactCard(
                modifier = Modifier.weight(1f),
                title = "BD GK",
                subtitle = "Bangladesher Samprotik Tothyo",
                tintColor = AppColors.cardTintedTeal,
                accentColor = AppColors.bdTealStart,
                onClick = onClick
            )
            RecentGkCompactCard(
                modifier = Modifier.weight(1f),
                title = "INT. GK",
                subtitle = "Antorjatik Samprotik Tothyo",
                tintColor = AppColors.cardTintedPurple,
                accentColor = AppColors.accentPurple,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun RecentGkCompactCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    tintColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = AppColors.cardWhite,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = AppColors.cardBorder,
                        cornerRadius = CornerRadius(18.dp.toPx()),
                        style = Stroke(0.5.dp.toPx())
                    )
                }
                .background(tintColor, RoundedCornerShape(18.dp))
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                if (title == "BD GK") Icons.Filled.School else Icons.Filled.Public,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleSmall,
                            color = AppColors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.textSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AppColors.textMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// QUESTION BANK CARD
// ══════════════════════════════════════════════════════════════

@Composable
private fun QuestionBankCard(onClick: () -> Unit) {
    val scrollState = rememberScrollState()

    PremiumCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = AppColors.primaryIndigo.copy(alpha = 0.06f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.HistoryEdu,
                            contentDescription = null,
                            tint = AppColors.primaryIndigo,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "University Question Bank",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        "Last 20 years questions with detailed solutions",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.textSecondary,
                        maxLines = 1
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = AppColors.primaryIndigo,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // University logos row
            val scrollState = rememberScrollState()
            LaunchedEffect(scrollState.maxValue) {
                if (scrollState.maxValue > 0) {
                    while (true) {
                        scrollState.animateScrollTo(
                            value = scrollState.maxValue,
                            animationSpec = tween(
                                durationMillis = scrollState.maxValue * 15,
                                easing = LinearEasing
                            )
                        )
                        scrollState.scrollTo(0)
                    }
                }
            }
            val universities = listOf(
                UniLogo("DU", R.drawable.du),
                UniLogo("JU", R.drawable.ju),
                UniLogo("RU", R.drawable.ru),
                UniLogo("CU", R.drawable.cu),
                UniLogo("JNU", R.drawable.jnu),
                UniLogo("BU", R.drawable.bu),
                UniLogo("BUP", R.drawable.bup),
                UniLogo("BRUR", R.drawable.brur),
                UniLogo("KU", R.drawable.ku),
                UniLogo("SUST", R.drawable.sust),
                UniLogo("JKKNIU", R.drawable.jkkniu),
                UniLogo("IU", R.drawable.islami_university),
                UniLogo("COU", R.drawable.cou),
            )
            
            // Repeat list multiple times for infinite marquee effect
            val repeatedList = remember { List(20) { universities }.flatten() }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState, enabled = false),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                repeatedList.forEachIndexed { index, uni ->
                    UniversityLogoItem(
                        name = uni.name,
                        drawableRes = uni.drawableRes,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

private data class UniLogo(val name: String, val drawableRes: Int)

@Composable
private fun UniversityLogoItem(
    name: String,
    drawableRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AppColors.cardSoft,
            border = androidx.compose.foundation.BorderStroke(
                0.5.dp, AppColors.cardBorder
            )
        ) {
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = "$name logo",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            name,
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.textSecondary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

// ══════════════════════════════════════════════════════════════
// TODAY'S PROGRESS
// ══════════════════════════════════════════════════════════════

@Composable
private fun TodayProgressRings(
    practiced: Int,
    correct: Int,
    wrong: Int,
    navController: NavController
) {
    val correctFraction = if (practiced > 0) correct.toFloat() / practiced else 0f
    val wrongFraction = if (practiced > 0) wrong.toFloat() / practiced else 0f

    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            SectionHeader(
                icon = Icons.AutoMirrored.Filled.ShowChart,
                title = "Today's Progress",
                actionLabel = "View Dashboard",
                actionIcon = Icons.AutoMirrored.Filled.ArrowForward,
                onAction = { navController.navigate(DashboardRoute) }
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressRing(
                    progress = if (practiced > 0) 1f else 0f,
                    value = practiced.toString(),
                    label = "Solved",
                    ringColor = AppColors.primaryIndigo
                )
                ProgressRing(
                    progress = correctFraction,
                    value = correct.toString(),
                    label = "Correct",
                    ringColor = AppColors.successGreen
                )
                ProgressRing(
                    progress = wrongFraction,
                    value = wrong.toString(),
                    label = "Wrong",
                    ringColor = AppColors.errorRed
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// BOTTOM NAVIGATION BAR
// ══════════════════════════════════════════════════════════════

@Composable
private fun PremiumBottomBar(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(28.dp),
        color = AppColors.cardWhite.copy(alpha = 0.96f),
        shadowElevation = 6.dp
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRoundRect(
                            color = AppColors.cardBorder,
                            cornerRadius = CornerRadius(28.dp.toPx()),
                            style = Stroke(0.5.dp.toPx())
                        )
                    }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    icon = Icons.Filled.Home,
                    label = "Home",
                    selected = true,
                    onClick = { navController.navigate(HomeRoute) { launchSingleTop = true } }
                )
                NavItem(
                    icon = Icons.Filled.Public,
                    label = "GK",
                    selected = false,
                    onClick = { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
                )
                NavItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "Study",
                    selected = false,
                    onClick = { navController.navigate(QuestionBankRoute(null, null)) { launchSingleTop = true } }
                )
                NavItem(
                    icon = Icons.Filled.EmojiEvents,
                    label = "Quiz",
                    selected = false,
                    onClick = { navController.navigate(MegaQuizRoutineRoute) { launchSingleTop = true } }
                )
                NavItem(
                    icon = Icons.Filled.Person,
                    label = "Profile",
                    selected = false,
                    onClick = { navController.navigate(ProfileRoute) { launchSingleTop = true } }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val selectedScale by animateFloatAsState(
        targetValue = if (selected) 1.10f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "navScale_$label"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) AppColors.navSelectedBg else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (selected) AppColors.navSelectedIcon else AppColors.navUnselectedIcon,
            modifier = Modifier.size(if (selected) 24.dp else 20.dp).scale(selectedScale)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) AppColors.navSelectedIcon else AppColors.navUnselectedIcon,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
        if (selected) {
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(AppColors.navIndicator)
            )
        }
    }
}
