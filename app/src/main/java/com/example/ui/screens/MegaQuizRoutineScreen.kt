package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.R
import com.example.data.MegaQuizExamEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionBottomBar
import com.example.ui.navigation.MegaQuizIntroRoute
import com.example.ui.navigation.MegaQuizRoutineDetailRoute
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// ══════════════════════════════════════════════════════════════
// COLOR TOKENS
// ══════════════════════════════════════════════════════════════

private object RColors {
    val bg = Color(0xFFF7F9FF)
    val cardWhite = Color(0xFFFFFFFF)
    val cardBorder = Color(0xFFE5E9F8)
    val textPrimary = Color(0xFF07113F)
    val textSecondary = Color(0xFF5E6480)
    val textMuted = Color(0xFF8A90A8)
    val primaryIndigo = Color(0xFF2638D9)
    val brightIndigo = Color(0xFF2F3DEB)
    val accentPurple = Color(0xFF6C4DFF)
    val softPurple = Color(0xFF7B5CFF)
    val liveRed = Color(0xFFFF3B30)
    val errorRed = Color(0xFFEF4444)
    val successGreen = Color(0xFF00A878)
    val softRedBg = Color(0xFFFFF7F8)
    val softRedBorder = Color(0xFFFFB8C0)
    val softIndigoBg = Color(0xFFF7F6FF)
    val softPurpleBg = Color(0xFFF3F0FF)
    val bdTeal = Color(0xFF009B7A)
    val intBlue = Color(0xFF2434D8)
    val mixedPurple = Color(0xFF7C3AED)
    val topGradStart = Color(0xFF2E2395)
    val topGradMid = Color(0xFF3F36D9)
    val topGradEnd = Color(0xFF6C4DFF)
    val disableBg = Color(0xFFF1F3F5)
    val disableText = Color(0xFFB0B6C8)
}

// ══════════════════════════════════════════════════════════════
// COUNTDOWN UTILITIES
// ══════════════════════════════════════════════════════════════

data class Countdown(val days: Long, val hours: Long, val minutes: Long, val seconds: Long)

fun computeCountdown(examDateMs: Long): Countdown? {
    val remMs = examDateMs - System.currentTimeMillis()
    if (remMs <= 0) return null
    val totalSec = remMs / 1000
    return Countdown(
        days = totalSec / 86400,
        hours = (totalSec % 86400) / 3600,
        minutes = (totalSec % 3600) / 60,
        seconds = totalSec % 60
    )
}

fun formatDate(examDateMs: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        sdf.format(Date(examDateMs))
    } catch (e: Exception) { "—" }
}

fun formatCountdownShort(c: Countdown): String {
    return if (c.days > 0) "${c.days}d ${c.hours}h ${c.minutes}m"
    else "${c.hours}h ${c.minutes}m ${c.seconds}s"
}

// ══════════════════════════════════════════════════════════════
// EXAM META
// ══════════════════════════════════════════════════════════════

data class ExamWithMeta(
    val exam: MegaQuizExamEntity,
    val isActive: Boolean,
    val isPast: Boolean
)

fun buildExamList(exams: List<MegaQuizExamEntity>): List<ExamWithMeta> {
    val now = System.currentTimeMillis()
    val sorted = exams.sortedBy { it.examDate }
    val activeIdx = sorted.indexOfFirst { it.status == "LIVE" }
        .takeIf { it >= 0 }
        ?: sorted.indexOfFirst { it.status == "UPCOMING" && it.examDate > now }
    return sorted.mapIndexed { idx, exam ->
        val isPast = exam.status == "COMPLETED" || (exam.examDate <= now && exam.status != "LIVE")
        val isActive = idx == activeIdx && !isPast
        ExamWithMeta(exam = exam, isActive = isActive, isPast = isPast)
    }
}

// ══════════════════════════════════════════════════════════════
// SCHEDULE DATA (20 items — real + fallback)
// ══════════════════════════════════════════════════════════════

private data class ScheduleItem(
    val serial: Int,
    val title: String,
    val topic: String,
    val category: String, // BD, INT, Mixed
    val dateMs: Long,
    val isLive: Boolean = false,
    val isPast: Boolean = false,
    val examId: String? = null
)

private fun buildScheduleData(exams: List<MegaQuizExamEntity>, skipSerial: Int = 0): List<ScheduleItem> {
    val now = System.currentTimeMillis()
    val sorted = exams.sortedBy { it.examDate }
    val result = mutableListOf<ScheduleItem>()

    // Add real exams first, skip those before skipSerial
    sorted.forEachIndexed { idx, exam ->
        val serial = idx + 1
        if (serial <= skipSerial) return@forEachIndexed
        val isPast = exam.status == "COMPLETED" || (exam.examDate <= now && exam.status != "LIVE")
        result.add(
            ScheduleItem(
                serial = serial,
                title = "মেগা কুইজ ${"%02d".format(serial)}",
                topic = exam.title,
                category = when {
                    exam.title.contains("BD", ignoreCase = true) -> "BD"
                    exam.title.contains("INT", ignoreCase = true) -> "INT"
                    else -> "Mixed"
                },
                dateMs = exam.examDate,
                isLive = exam.status == "LIVE",
                isPast = isPast,
                examId = exam.id
            )
        )
    }


    return result
}

// ══════════════════════════════════════════════════════════════
// MAIN SCREEN
// ══════════════════════════════════════════════════════════════

@Composable
fun MegaQuizRoutineScreen(viewModel: GKViewModel, navController: NavController) {
    val rawExams by viewModel.megaQuizExams.collectAsStateWithLifecycle(initialValue = emptyList())
    val latestScore by viewModel.getLatestMegaQuizScore().collectAsStateWithLifecycle(initialValue = null)

    var tick by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) { while (true) { delay(1000); tick++ } }

    val examList = remember(rawExams, tick) { buildExamList(rawExams) }

    val runningExam = examList.firstOrNull { it.exam.status == "LIVE" }
    val upcomingExam = examList.firstOrNull { it.isActive && !it.isPast && it.exam.status != "LIVE" }

    // Determine skipSerial: skip #01 if running card shown, skip #02 if upcoming card shown
    val skipSerial = when {
        runningExam != null && upcomingExam != null -> 2
        runningExam != null -> 1
        upcomingExam != null -> 1
        else -> 0
    }
    val schedule = remember(rawExams, tick) { buildScheduleData(rawExams, skipSerial) }

    Scaffold(
        topBar = { },
        bottomBar = {
            AdmissionBottomBar(navController = navController, currentRoute = "MegaQuiz")
        },
        containerColor = RColors.bg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // ── Top Bar (as first item) ──
            item { MegaRoutineTopBar(onBack = { navController.popBackStack() }) }

            // ── Running Exam ──
            if (runningExam != null) {
                item { RunningExamCard(exam = runningExam, tick, onStart = { navController.navigate(MegaQuizIntroRoute(runningExam.exam.id)) }, onRoutine = { navController.navigate(MegaQuizRoutineDetailRoute(runningExam.exam.id)) }) }
            }

            // ── Next Upcoming Exam ──
            if (upcomingExam != null) {
                item { NextUpcomingExamCard(exam = upcomingExam, tick, onJoin = { navController.navigate(MegaQuizIntroRoute(upcomingExam.exam.id)) }, onRoutine = { navController.navigate(MegaQuizRoutineDetailRoute(upcomingExam.exam.id)) }) }
            }

            // ── Upcoming Schedule ──
            item { UpcomingScheduleSection(schedule = schedule, tick = tick, navController = navController) }

            // ── Preparation Tips ──
            item { PreparationTipsCard() }

            // ── Previous Result ──
            item { PreviousResultButton(navController = navController) }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// TOP BAR
// ══════════════════════════════════════════════════════════════

@Composable
private fun MegaRoutineTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                Brush.linearGradient(listOf(RColors.topGradStart, RColors.topGradMid, RColors.topGradEnd))
            )
    ) {
        Box(Modifier.size(200.dp).offset(x = (-60).dp, y = (-60).dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)), CircleShape))
        Box(Modifier.size(180.dp).align(Alignment.BottomEnd).offset(x = 40.dp, y = 30.dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)), CircleShape))

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            // Back button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))

            // Logo + Title row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Column {
                    Text("Admission GK", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("মেগা কুইজ রুটিন", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFDDE3FF))
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// SECTION TITLE
// ══════════════════════════════════════════════════════════════

@Composable
private fun SectionTitle(title: String, action: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(4.dp).clip(CircleShape).background(RColors.primaryIndigo))
            Spacer(Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, color = RColors.textPrimary, fontWeight = FontWeight.Bold)
        }
        if (action != null) {
            Text(action, style = MaterialTheme.typography.labelSmall, color = RColors.primaryIndigo, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ══════════════════════════════════════════════════════════════
// RUNNING EXAM CARD
// ══════════════════════════════════════════════════════════════

@Composable
private fun RunningExamCard(
    exam: ExamWithMeta,
    tick: Long,
    onStart: () -> Unit,
    onRoutine: () -> Unit
) {
    val countdown = remember(exam.exam.examDate, tick) { computeCountdown(exam.exam.examDate) }
    val dateStr = remember(exam.exam.examDate) { formatDate(exam.exam.examDate) }
    val cdText = if (countdown != null) remember(countdown) { formatCountdownShort(countdown) } else "চলমান"
    val serial = exam.exam.id.filter { it.isDigit() }.take(2).padStart(2, '0')
    val examTitle = "মেগা কুইজ ${serial}"
    val topicTitle = exam.exam.title

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        SectionTitle("চলমান পরীক্ষা")
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = RColors.softRedBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().drawBehind {
                drawRoundRect(color = RColors.softRedBorder.copy(alpha = 0.5f), cornerRadius = CornerRadius(24.dp.toPx()), style = Stroke(1.dp.toPx()))
            }) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Icon + LIVE badge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(70.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(52.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Color(0xFFFF4B5C), RColors.errorRed))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.EmojiEvents, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        LivePill()
                    }

                    Spacer(Modifier.width(14.dp))

                    // Middle: details
                    Column(modifier = Modifier.weight(1f)) {
                        Text(examTitle, style = MaterialTheme.typography.titleMedium, color = RColors.textPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(topicTitle, style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textMuted, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(dateStr, style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, null, tint = RColors.errorRed, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(cdText, style = MaterialTheme.typography.bodySmall, color = RColors.errorRed, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    // Right: buttons
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("#$serial", style = MaterialTheme.typography.labelSmall, color = RColors.textMuted, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.height(44.dp).width(80.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = RColors.errorRed,
                            shadowElevation = 2.dp
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clickable(onClick = onStart), contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Join", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier.height(36.dp).width(80.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, RColors.textMuted.copy(alpha = 0.35f))
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clickable(onClick = onRoutine), contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textSecondary, modifier = Modifier.size(12.dp))
                                    Spacer(Modifier.width(3.dp))
                                    Text("Routine", style = MaterialTheme.typography.labelSmall, color = RColors.textSecondary, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LivePill() {
    val inf = rememberInfiniteTransition(label = "livePill")
    val dotAlpha by inf.animateFloat(0.4f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "dot")
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = RColors.errorRed.copy(alpha = 0.18f),
        border = BorderStroke(0.5.dp, RColors.errorRed.copy(alpha = 0.35f))
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(Modifier.size(5.dp).clip(CircleShape).background(Color.White.copy(alpha = dotAlpha)))
            Text("LIVE", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// ══════════════════════════════════════════════════════════════
// NEXT UPCOMING EXAM CARD
// ══════════════════════════════════════════════════════════════

@Composable
private fun NextUpcomingExamCard(
    exam: ExamWithMeta,
    tick: Long,
    onJoin: () -> Unit,
    onRoutine: () -> Unit
) {
    val countdown = remember(exam.exam.examDate, tick) { computeCountdown(exam.exam.examDate) }
    val dateStr = remember(exam.exam.examDate) { formatDate(exam.exam.examDate) }
    val cdText = if (countdown != null) remember(countdown) { formatCountdownShort(countdown) } else "—"
    val serial = exam.exam.id.filter { it.isDigit() }.take(2).padStart(2, '0')
    val examTitle = "মেগা কুইজ ${serial}"
    val topicTitle = exam.exam.title

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        SectionTitle("পরবর্তী পরীক্ষা")
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = RColors.softIndigoBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().drawBehind {
                drawRoundRect(color = RColors.accentPurple.copy(alpha = 0.25f), cornerRadius = CornerRadius(24.dp.toPx()), style = Stroke(1.dp.toPx()))
            }) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Icon + UPCOMING badge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(70.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(52.dp).clip(CircleShape).background(Brush.linearGradient(listOf(RColors.accentPurple, RColors.primaryIndigo))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        UpcomingPill()
                    }

                    Spacer(Modifier.width(14.dp))

                    // Middle: details
                    Column(modifier = Modifier.weight(1f)) {
                        Text(examTitle, style = MaterialTheme.typography.titleMedium, color = RColors.textPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(topicTitle, style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textMuted, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(dateStr, style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, null, tint = RColors.accentPurple, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(cdText, style = MaterialTheme.typography.bodySmall, color = RColors.accentPurple, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    // Right: buttons
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("#$serial", style = MaterialTheme.typography.labelSmall, color = RColors.textMuted, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.height(44.dp).width(80.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = RColors.primaryIndigo,
                            shadowElevation = 2.dp
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clickable(onClick = onJoin), contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Join", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier.height(36.dp).width(80.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, RColors.textMuted.copy(alpha = 0.35f))
                        ) {
                            Box(modifier = Modifier.fillMaxSize().clickable(onClick = onRoutine), contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textSecondary, modifier = Modifier.size(12.dp))
                                    Spacer(Modifier.width(3.dp))
                                    Text("Routine", style = MaterialTheme.typography.labelSmall, color = RColors.textSecondary, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingPill() {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = RColors.primaryIndigo.copy(alpha = 0.12f),
        border = BorderStroke(0.5.dp, RColors.primaryIndigo.copy(alpha = 0.25f))
    ) {
        Text(
            "UPCOMING",
            Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            fontSize = 9.sp, color = RColors.primaryIndigo, fontWeight = FontWeight.ExtraBold
        )
    }
}

// ══════════════════════════════════════════════════════════════
// UPCOMING SCHEDULE SECTION
// ══════════════════════════════════════════════════════════════

@Composable
private fun UpcomingScheduleSection(
    schedule: List<ScheduleItem>,
    tick: Long,
    navController: NavController
) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(4.dp).clip(CircleShape).background(RColors.primaryIndigo))
                Spacer(Modifier.width(8.dp))
                Text("আসন্ন পরীক্ষার সূচি", style = MaterialTheme.typography.titleSmall, color = RColors.textPrimary, fontWeight = FontWeight.Bold)
            }
            Text("মোট ${schedule.size}টি পরীক্ষা", style = MaterialTheme.typography.labelSmall, color = RColors.textMuted, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RColors.cardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.drawBehind {
                drawRoundRect(color = RColors.cardBorder, cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
            }) {
                schedule.forEachIndexed { index, item ->
                    ScheduleListItem(item = item, tick = tick, navController = navController)
                    if (index < schedule.size - 1) {
                        Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).padding(horizontal = 16.dp).background(RColors.cardBorder))
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// SCHEDULE LIST ITEM
// ══════════════════════════════════════════════════════════════

@Composable
private fun ScheduleListItem(
    item: ScheduleItem,
    tick: Long,
    navController: NavController
) {
    val countdown = remember(item.dateMs, tick) { computeCountdown(item.dateMs) }
    val dateStr = remember(item.dateMs) { formatDate(item.dateMs) }
    val daysLeft = when {
        item.isPast -> "সম্পন্ন"
        countdown == null -> "আজ"
        countdown.days > 0 -> "${countdown.days}d বাকি"
        else -> "আজ"
    }

    val (catColor, catIcon) = when (item.category) {
        "BD" -> when {
            item.topic.contains("ইতিহাস", true) || item.topic.contains("মুক্তিযুদ্ধ", true) -> RColors.bdTeal to Icons.Filled.AccountBalance
            item.topic.contains("ভূগোল", true) -> RColors.bdTeal to Icons.Filled.LocationOn
            item.topic.contains("অর্থনীতি", true) || item.topic.contains("কৃষি", true) -> RColors.bdTeal to Icons.AutoMirrored.Filled.TrendingUp
            item.topic.contains("সংবিধান", true) || item.topic.contains("সরকার", true) || item.topic.contains("প্রশাসন", true) -> RColors.bdTeal to Icons.Filled.AccountBalance
            item.topic.contains("সংস্কৃতি", true) || item.topic.contains("সাহিত্য", true) -> RColors.bdTeal to Icons.AutoMirrored.Filled.MenuBook
            item.topic.contains("শিক্ষা", true) || item.topic.contains("স্বাস্থ্য", true) -> RColors.bdTeal to Icons.Filled.School
            item.topic.contains("সাম্প্রতিক", true) -> RColors.bdTeal to Icons.AutoMirrored.Filled.Article
            else -> RColors.bdTeal to Icons.AutoMirrored.Filled.MenuBook
        }
        "INT" -> when {
            item.topic.contains("সংস্থা", true) || item.topic.contains("জাতিসংঘ", true) -> RColors.intBlue to Icons.Filled.Public
            item.topic.contains("ইতিহাস", true) || item.topic.contains("সভ্যতা", true) -> RColors.intBlue to Icons.Filled.History
            item.topic.contains("ভূগোল", true) -> RColors.intBlue to Icons.Filled.Public
            item.topic.contains("অর্থনীতি", true) -> RColors.intBlue to Icons.AutoMirrored.Filled.TrendingUp
            item.topic.contains("রাজনীতি", true) -> RColors.intBlue to Icons.Filled.Gavel
            item.topic.contains("দেশ", true) || item.topic.contains("রাজধানী", true) -> RColors.intBlue to Icons.Filled.Flag
            item.topic.contains("বিজ্ঞান", true) || item.topic.contains("প্রযুক্তি", true) -> RColors.intBlue to Icons.Filled.Science
            item.topic.contains("পরিবেশ", true) || item.topic.contains("জলবায়ু", true) -> RColors.intBlue to Icons.Filled.Eco
            item.topic.contains("সাম্প্রতিক", true) -> RColors.intBlue to Icons.AutoMirrored.Filled.Article
            else -> RColors.intBlue to Icons.Filled.Public
        }
        else -> RColors.mixedPurple to Icons.Filled.AutoStories
    }

    val isJoinable = !item.isPast && item.examId != null
    val joinBg = if (isJoinable) RColors.primaryIndigo else RColors.disableBg

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Serial
        Text(
            "#${"%02d".format(item.serial)}",
            style = MaterialTheme.typography.labelSmall,
            color = RColors.textMuted,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(32.dp)
        )

        Spacer(Modifier.width(6.dp))

        // Category icon
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(catColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(catIcon, null, tint = catColor, modifier = Modifier.size(18.dp))
        }

        Spacer(Modifier.width(10.dp))

        // Title + topic
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = RColors.textPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                item.topic,
                style = MaterialTheme.typography.bodySmall,
                color = RColors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp
            )
        }

        Spacer(Modifier.width(8.dp))

        // Date + days left
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textMuted, modifier = Modifier.size(11.dp))
                Spacer(Modifier.width(3.dp))
                Text(dateStr, fontSize = 10.sp, color = RColors.textMuted, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = when {
                    item.isPast -> RColors.cardBorder.copy(alpha = 0.6f)
                    daysLeft == "আজ" -> RColors.successGreen.copy(alpha = 0.10f)
                    else -> RColors.primaryIndigo.copy(alpha = 0.08f)
                }
            ) {
                Text(
                    daysLeft,
                    Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    fontSize = 9.sp,
                    color = when {
                        item.isPast -> RColors.textMuted
                        daysLeft == "আজ" -> RColors.successGreen
                        else -> RColors.primaryIndigo
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Join + Routine buttons
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Surface(
                modifier = Modifier.height(30.dp).widthIn(min = 54.dp),
                shape = RoundedCornerShape(8.dp),
                color = joinBg
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().then(
                        if (isJoinable && item.examId != null) Modifier.clickable {
                            navController.navigate(MegaQuizIntroRoute(item.examId))
                        } else Modifier
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Join",
                        fontSize = 10.sp,
                        color = if (isJoinable) Color.White else RColors.disableText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Surface(
                modifier = Modifier.height(26.dp).widthIn(min = 54.dp),
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent,
                border = BorderStroke(0.5.dp, RColors.textMuted.copy(alpha = 0.25f))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().clickable {
                        navController.navigate(MegaQuizRoutineDetailRoute(item.examId))
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Filled.CalendarMonth, null, tint = RColors.textMuted, modifier = Modifier.size(8.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("Routine", fontSize = 8.sp, color = RColors.textMuted, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PREPARATION TIPS CARD
// ══════════════════════════════════════════════════════════════

@Composable
private fun PreparationTipsCard() {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = RColors.cardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().drawBehind {
                drawRoundRect(color = RColors.cardBorder, cornerRadius = CornerRadius(22.dp.toPx()), style = Stroke(0.5.dp.toPx()))
            }.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(RColors.accentPurple.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.TipsAndUpdates, null, tint = RColors.accentPurple, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("প্রস্তুতির কৌশল", style = MaterialTheme.typography.titleSmall, color = RColors.textPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                TipRow("প্রথমে স্পেশাল নোট ভালো করে পড়ুন")
                TipRow("MCQ প্র্যাকটিস করে নিজেকে যাচাই করুন")
                TipRow("টাইমড মেগা কুইজে অংশগ্রহণ করুন")
                TipRow("প্রতিটি পরীক্ষার রেজাল্ট রেকর্ড থাকবে")
            }
        }
    }
}

@Composable
private fun TipRow(text: String) {
    Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.CheckCircle, null, tint = RColors.successGreen, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary)
    }
}

// ══════════════════════════════════════════════════════════════
// PREVIOUS RESULT BUTTON
// ══════════════════════════════════════════════════════════════

@Composable
private fun PreviousResultButton(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = RColors.softPurpleBg,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("results") }
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(RColors.accentPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.BarChart, null, tint = RColors.accentPurple, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("আগের রেজাল্ট দেখুন", style = MaterialTheme.typography.titleSmall, color = RColors.primaryIndigo, fontWeight = FontWeight.Bold)
                        Text("মেগা কুইজের আগের পারফরম্যান্স দেখুন", style = MaterialTheme.typography.bodySmall, color = RColors.textSecondary, maxLines = 1)
                    }
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = RColors.primaryIndigo, modifier = Modifier.size(20.dp))
            }
        }
    }
}
