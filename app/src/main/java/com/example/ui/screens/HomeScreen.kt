package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
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
import com.example.data.RecentGKEntity
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
import java.time.Instant
import java.time.LocalTime

// ─── Blueprint Grid Background ────────────────────────
@Composable
private fun BlueprintGrid(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val gridSize = 40.dp.toPx()
        val lineColor = Color(0xFFE2E4FF).copy(alpha = 0.12f)
        var x = 0f
        while (x < size.width) { drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.7f); x += gridSize }
        var y = 0f
        while (y < size.height) { drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.7f); y += gridSize }
    }
}

// ─── Premium White Card with Gradient Border ──────────
@Composable
private fun PremiumWhiteCard(modifier: Modifier, gradientBorder: Brush? = null, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) { Box(content = content) }
        if (gradientBorder != null) {
            Box(modifier = Modifier.matchParentSize().drawBehind {
                drawRoundRect(gradientBorder, size = size, cornerRadius = CornerRadius(24.dp.toPx()), style = Stroke(1.5.dp.toPx()))
            })
        }
    }
}

// ─── Metallic Shimmer Button ──────────────────────────
@Composable
private fun PremiumButton(text: String, onClick: () -> Unit, modifier: Modifier, containerColor: Color, contentColor: Color = Color.White) {
    val inf = rememberInfiniteTransition(label = "pb_$text")
    val sweep by inf.animateFloat(-1.5f, 2.5f, infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart), label = "sw")
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = containerColor,
        shadowElevation = 6.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge, color = contentColor, fontWeight = FontWeight.Bold, maxLines = 1)
            Box(modifier = Modifier.matchParentSize().offset(x = ((sweep * 360).dp)).background(Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent, Color.White.copy(alpha = 0.40f), Color.White.copy(alpha = 0.15f), Color.Transparent))))
        }
    }
}

// ─── Circular Progress Ring ───────────────────────────
@Composable
private fun ProgressRing(progress: Float, value: String, label: String, ringColor: Color) {
    val inf = rememberInfiniteTransition(label = "rg_$label")
    val pulse by inf.animateFloat(0.6f, 1f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Reverse), label = "pu")
    val sweep by animateFloatAsState(progress * 360f, tween(1000, easing = FastOutSlowInEasing), label = "sw")
    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sw = 7.dp.toPx()
            val r = (size.minDimension - sw) / 2
            drawArc(Color(0xFFEEECF8), -90f, 360f, false, Offset(sw / 2, sw / 2), Size(r * 2, r * 2), style = Stroke(sw, cap = StrokeCap.Round))
            drawArc(ringColor, -90f, sweep, false, Offset(sw / 2, sw / 2), Size(r * 2, r * 2), style = Stroke(sw, cap = StrokeCap.Round))
        }
        Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Brush.radialGradient(listOf(ringColor.copy(alpha = pulse * 0.10f), Color.Transparent))))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium, color = ringColor, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(label, fontSize = 10.sp, color = TextMuted, maxLines = 1)
        }
    }
}

// ─── Main HomeScreen ──────────────────────────────────
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
    val greeting = when { hour < 12 -> "শুভ সকাল"; hour < 17 -> "শুভ দুপুর"; hour < 20 -> "শুভ সন্ধ্যা"; else -> "শুভ রাত্রি" }
    val upcomingExam = megaQuizExams.firstOrNull { it.status == "LIVE" || it.status == "UPCOMING" }
    val isLive = upcomingExam?.status == "LIVE"
    val countdownParts = remember(upcomingExam?.examDate) {
        if (upcomingExam != null && upcomingExam.examDate > 0) {
            val rem = upcomingExam.examDate - Instant.now().epochSecond
            if (rem > 0) listOf("%02d".format(rem / 86400), "%02d".format((rem % 86400) / 3600), "%02d".format((rem % 3600) / 60)) else null
        } else null
    }

    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        BlueprintGrid()
        androidx.compose.material3.Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { PremiumBottomBar(navController) }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item { CompactHeader(dayStreak = "7 Days", onProgressClick = { navController.navigate(ProgressReportRoute) }) }
                item { AnimatedStatPills(totalTopics, totalSubTopics, totalMCQs, totalMegaQuiz) }
                item { StudyCards(bdTopics.size, intTopics.size, { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }, { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) }) }
                item { MegaQuizCard(isLive, upcomingExam?.title, countdownParts) { navController.navigate(MegaQuizRoutineRoute) } }
                item { RecentGkSection { navController.navigate(RecentGKRoute) } }
                item { QuestionBankCard { navController.navigate(QuestionBankRoute(null, null)) } }
                item { val p = todayProgress; TodayProgressRings(p?.totalPracticed ?: 0, p?.correctCount ?: 0, p?.wrongCount ?: 0, navController) }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────
@Composable
private fun CompactHeader(dayStreak: String, onProgressClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).border(2.dp, BrandPrimary.copy(alpha = 0.15f), CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(26.dp), contentScale = ContentScale.Fit)
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Admission GK", style = MaterialTheme.typography.titleLarge, color = BrandPrimary, fontWeight = FontWeight.Bold)
                Text("Ready for today's GK practice?", style = MaterialTheme.typography.labelSmall, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Surface(shape = RoundedCornerShape(999.dp), color = BrandPrimary.copy(alpha = 0.06f), border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.10f))) {
                Text("\u2B50 $dayStreak", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Spacer(Modifier.height(4.dp))
            Surface(modifier = Modifier.clickable(onClick = onProgressClick), shape = RoundedCornerShape(999.dp), color = BrandAccent.copy(alpha = 0.08f), border = BorderStroke(1.dp, BrandAccent.copy(alpha = 0.15f))) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Progress", style = MaterialTheme.typography.labelSmall, color = BrandAccent, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

// ── Stat Pills (polished: light stroke, shining sweep, marquee, count animation) ──
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedStatPills(t1: Int, t2: Int, t3: Int, t4: Int) {
    val scrollState = rememberScrollState()
    val autoScrollActive = remember { mutableStateOf(true) }

    LaunchedEffect(scrollState.maxValue) {
        while (autoScrollActive.value && scrollState.maxValue > 0) {
            scrollState.animateScrollTo(
                scrollState.value + 1,
                animationSpec = tween(120, easing = LinearEasing)
            )
            if (scrollState.value >= scrollState.maxValue) {
                scrollState.animateScrollTo(0, animationSpec = tween(600, easing = FastOutSlowInEasing))
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var vis by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { vis = true }

        val pills = listOf(
            PillData(Icons.AutoMirrored.Filled.MenuBook, "Topics", t1, BrandPrimary),
            PillData(Icons.Filled.Public, "Sub Topics", t2, BdGkColor),
            PillData(Icons.Filled.Quiz, "Practice MCQ", t3, BrandAccent),
            PillData(Icons.Filled.EmojiEvents, "Mega Quiz", t4, BrandSecondary)
        )

        pills.forEachIndexed { i, pill ->
            PolishedStatPill(
                icon = pill.icon,
                label = pill.label,
                count = pill.count,
                accent = pill.accent,
                delay = i * 120,
                visible = vis
            )
        }
    }
}

private data class PillData(val icon: ImageVector, val label: String, val count: Int, val accent: Color)

@Composable
private fun PolishedStatPill(icon: ImageVector, label: String, count: Int, accent: Color, delay: Int, visible: Boolean) {
    val inf = rememberInfiniteTransition(label = "pl_${label}")
    val shineOffset by inf.animateFloat(-1f, 2f, infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart), label = "sh")
    val borderAlpha by inf.animateFloat(0.3f, 0.7f, infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Reverse), label = "bo")

    val entranceA by animateFloatAsState(if (visible) 1f else 0f, tween(500, delayMillis = delay, easing = FastOutSlowInEasing), label = "ea")
    val entranceS by animateFloatAsState(if (visible) 1f else 0.6f, tween(500, delayMillis = delay, easing = FastOutSlowInEasing), label = "es")

    // Animated count number
    val displayCount by animateFloatAsState(if (visible) count.toFloat() else 0f, tween(800, delayMillis = delay + 200, easing = FastOutSlowInEasing), label = "dc")
    val countText = if (count == 0) "Soon" else if (label == "Mega Quiz" && count > 0) "\u221E" else displayCount.toInt().toString()

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.scale(entranceS).alpha(entranceA)
    ) {
        Box(
            modifier = Modifier.drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(listOf(accent.copy(alpha = borderAlpha * 0.6f), accent.copy(alpha = borderAlpha * 0.15f))),
                    cornerRadius = CornerRadius(999.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            }
        ) {
            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = accent.copy(alpha = 0.08f), modifier = Modifier.size(30.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.widthIn(max = 100.dp).basicMarquee(iterations = Int.MAX_VALUE)) {
                    Text(countText, style = MaterialTheme.typography.labelLarge, color = TextPrimary, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                    Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, maxLines = 1)
                }
            }

            // Shining sweep overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = (shineOffset * 200).dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent)
                        )
                    )
            )
        }
    }
}

// ── Premium Study Cards (BD GK & INT GK) ────────────
@Composable
private fun StudyCards(bd: Int, int: Int, onBd: () -> Unit, onInt: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        PremiumStudyCard(
            modifier = Modifier.weight(1f),
            title = "BD GK",
            subtitle = "বাংলাদেশ বিষয়াবলি",
            count = bd,
            label = "Topics",
            btnText = "Start Learning",
            gradient = Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF14B8A6), Color(0xFF34D399))),
            glowColor = Color(0xFF10B981),
            mapEmoji = "\uD83C\uDDE7\uD83C\uDDE9",
            accent = Color(0xFF0D9488),
            onClick = onBd
        )
        PremiumStudyCard(
            modifier = Modifier.weight(1f),
            title = "INT. GK",
            subtitle = "আন্তর্জাতিক বিষয়াবলি",
            count = int,
            label = "Topics",
            btnText = "Explore",
            gradient = Brush.linearGradient(listOf(Color(0xFF5B21B6), Color(0xFF7C3AED), Color(0xFFA78BFA))),
            glowColor = Color(0xFF8B5CF6),
            mapEmoji = "\uD83C\uDF0D",
            accent = Color(0xFF7C3AED),
            onClick = onInt
        )
    }
}

@Composable
private fun PremiumStudyCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    count: Int,
    label: String,
    btnText: String,
    gradient: Brush,
    glowColor: Color,
    mapEmoji: String,
    accent: Color,
    onClick: () -> Unit,
) {
    // ── Animations ──
    val inf = rememberInfiniteTransition(label = "psc_$title")

    // Slower glow orbs (4s cycle)
    val glowAlpha by inf.animateFloat(
        initialValue = 0.25f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "gAlpha"
    )
    val glowAlpha2 by inf.animateFloat(
        initialValue = 0.15f, targetValue = 0.45f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Reverse),
        label = "gAlpha2"
    )

    // Slow animated border pulse
    val borderPulse by inf.animateFloat(
        initialValue = 0.2f, targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "bPulse"
    )

    // Map watermark subtle rotation
    val mapRotation by inf.animateFloat(
        initialValue = -2f, targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "mRot"
    )

    // Slow shimmer sweep on button
    val btnShimmer by inf.animateFloat(
        initialValue = -1.5f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Restart),
        label = "btnSh"
    )

    // Card-level subtle floating scale
    val cardFloat by inf.animateFloat(
        initialValue = 1f, targetValue = 1.01f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "cFloat"
    )

    // Entrance
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }

    val entranceAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(600, easing = FastOutSlowInEasing), label = "eA")
    val entranceScale by animateFloatAsState(if (appeared) 1f else 0.85f, tween(600, easing = FastOutSlowInEasing), label = "eS")
    val contentDelay by animateFloatAsState(if (appeared) 1f else 0f, tween(800, delayMillis = 300, easing = FastOutSlowInEasing), label = "cD")

    Card(
        modifier = modifier
            .scale(cardFloat)
            .alpha(entranceAlpha)
            .scale(entranceScale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // ── Gradient Background ──
            Box(modifier = Modifier.matchParentSize().clip(RoundedCornerShape(24.dp)).background(gradient))

            // ── Map Watermark (subtle rotation) ──
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    mapEmoji,
                    fontSize = 80.sp,
                    color = Color.White.copy(alpha = 0.06f),
                    modifier = Modifier.rotate(mapRotation)
                )
            }

            // ── Top Glow Orb ──
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = (-35).dp, y = (-25).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(glowColor.copy(alpha = glowAlpha), glowColor.copy(alpha = 0f))
                        ),
                        CircleShape
                    )
            )

            // ── Bottom Glow Orb ──
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(100.dp)
                    .offset(x = 20.dp, y = 20.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(glowColor.copy(alpha = glowAlpha2), glowColor.copy(alpha = 0f))
                        ),
                        CircleShape
                    )
            )

            // ── Animated Gradient Border ──
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = borderPulse * 0.4f),
                                    glowColor.copy(alpha = borderPulse * 0.2f)
                                )
                            ),
                            cornerRadius = CornerRadius(24.dp.toPx()),
                            style = Stroke(2.dp.toPx())
                        )
                    }
            )

            // ── Content ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(contentDelay),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top badge
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color.White.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.30f))
                    ) {
                        Text(
                            "$count $label",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                // Title and subtitle
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.88f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(10.dp))

                    // ── Polished Button ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                btnText,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Slow metallic shimmer sweep
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .offset(x = (btnShimmer * 200).dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0.5f),
                                            Color.White.copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

// ── Premium Mega Quiz Card (Clean — no glow, subtle border, countdown) ──
@Composable
private fun MegaQuizCard(isLive: Boolean, examTitle: String?, parts: List<String>?, onClick: () -> Unit) {
    val inf = rememberInfiniteTransition(label = "mq")

    // Subtle border pulse (very low opacity)
    val borderAlpha by inf.animateFloat(0.08f, 0.18f, infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse), label = "ba")

    // Button shimmer
    val btnShimmer1 by inf.animateFloat(-2f, 3f, infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart), label = "bs1")
    val btnShimmer2 by inf.animateFloat(-1f, 3.5f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), label = "bs2")

    // Animated countdown number (decreasing)
    val dayCount = remember(parts) { parts?.get(0)?.toIntOrNull() ?: 0 }
    val animatedDay by animateFloatAsState(dayCount.toFloat(), tween(800, easing = FastOutSlowInEasing), label = "dday")

    // Entrance
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500, easing = FastOutSlowInEasing), label = "eA")
    val entScale by animateFloatAsState(if (appeared) 1f else 0.92f, tween(500, easing = FastOutSlowInEasing), label = "eS")
    val contentAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(700, delayMillis = 200, easing = FastOutSlowInEasing), label = "cA")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .alpha(entAlpha)
            .scale(entScale)
            .clip(RoundedCornerShape(24.dp))
            .background(GradientMegaQuizBg)
            .clickable(onClick = onClick)
            .drawBehind {
                // Clean subtle border
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = borderAlpha),
                            Color.White.copy(alpha = borderAlpha * 0.6f)
                        )
                    ),
                    cornerRadius = CornerRadius(24.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            }
            .padding(24.dp)
    ) {
        // ── Content ──
        Column(modifier = Modifier.alpha(contentAlpha)) {
            // Live badge or Day count badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                if (isLive) SimpleLivePill()
                else Spacer(Modifier.width(1.dp))

                // Day count badge
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.30f))
                ) {
                    Text(
                        "\uD83D\uDCC5 ${animatedDay.toInt()} Days",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                "Mega Quiz Exam",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                examTitle ?: "Compete with thousands of students across the country.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // ── Countdown Timer ──
            if (parts != null) {
                Spacer(Modifier.height(20.dp))
                // "Exam Starts In" label
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.20f))
                ) {
                    Text(
                        "\u23F0 Exam Starts In",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(12.dp))
                CountdownTimerRow(parts)
                Spacer(Modifier.height(22.dp))
            } else {
                Spacer(Modifier.height(22.dp))
            }

            // ── Buttons Row ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Join Now
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.28f))
                        .clickable(onClick = onClick)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                        Text("Join Now", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = (btnShimmer1 * 250).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.Transparent, Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0.12f), Color.Transparent)
                                )
                            )
                    )
                }

                // View Routine
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .drawBehind {
                            drawRoundRect(
                                Color.White.copy(alpha = 0.55f),
                                cornerRadius = CornerRadius(14.dp.toPx()),
                                style = Stroke(1.5.dp.toPx())
                            )
                        }
                        .clickable(onClick = onClick)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                        Text("View Routine", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = (btnShimmer2 * 250).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.Transparent, Color.White.copy(alpha = 0.30f), Color.White.copy(alpha = 0.08f), Color.Transparent)
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleLivePill() {
    val inf = rememberInfiniteTransition(label = "slp")
    val dotAlpha by inf.animateFloat(0.6f, 1f, infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse), label = "da")
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = ErrorColor.copy(alpha = 0.30f),
        border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.40f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White.copy(alpha = dotAlpha)))
            Text("LIVE", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.ExtraBold, maxLines = 1)
        }
    }
}

@Composable
private fun CountdownTimerRow(parts: List<String>) {
    val labels = listOf("Days", "Hrs", "Min")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        parts.forEachIndexed { index, value ->
            CleanCountdownUnit(value, labels[index])
        }
    }
}

@Composable
private fun CleanCountdownUnit(value: String, label: String) {
    val fontSize = when {
        value.length <= 2 -> 28.sp
        value.length <= 4 -> 22.sp
        value.length <= 6 -> 16.sp
        else -> 12.sp
    }
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                fontSize = fontSize,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                softWrap = false
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.70f),
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PremiumLiveBadge() {
    val inf = rememberInfiniteTransition(label = "plb")
    val pulseAlpha by inf.animateFloat(0.5f, 1f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Reverse), label = "pa")
    val ringScale by inf.animateFloat(0.7f, 1.3f, infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Reverse), label = "rs")

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        // Pulsing dot with ring
        Box(contentAlignment = Alignment.Center) {
            // Outer ring
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .scale(ringScale)
                    .clip(CircleShape)
                    .background(ErrorColor.copy(alpha = 0.25f))
            )
            // Inner dot
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(ErrorColor.copy(alpha = pulseAlpha))
            )
        }
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = ErrorColor.copy(alpha = 0.30f),
            border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.40f))
        ) {
            Text(
                "LIVE",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PremiumCountdownUnit(value: String, label: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.18f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.30f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f), maxLines = 1)
        }
    }
}

// ── Premium Recent GK Rows ─────────────────────────────
@Composable
private fun RecentGkRow(title: String, borderGradient: Brush, items: List<RecentGKEntity>, seeAllLabel: String, onSeeAll: () -> Unit) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val sectionAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500), label = "sa")

    Column(modifier = Modifier.padding(horizontal = 16.dp).alpha(sectionAlpha)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .clickable(onClick = onSeeAll)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(seeAllLabel, style = MaterialTheme.typography.labelMedium, color = BrandPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = BrandPrimary, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        if (items.isEmpty()) {
            PremiumWhiteCard(Modifier.fillMaxWidth(), borderGradient) {
                Text("No recent GK updates yet", modifier = Modifier.padding(18.dp), style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items.forEachIndexed { index, item ->
                    PremiumRecentGkCard(
                        borderGradient = borderGradient,
                        title = item.topicTitle,
                        subtitle = item.specialTopicNote,
                        delay = index * 80
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumRecentGkCard(borderGradient: Brush, title: String, subtitle: String, delay: Int) {
    val inf = rememberInfiniteTransition(label = "rg_${title.take(4)}")

    // Slow border glow pulse
    val borderGlow by inf.animateFloat(0.3f, 0.65f, infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Reverse), label = "bg")

    // Subtle card float
    val cardFloat by inf.animateFloat(1f, 1.02f, infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Reverse), label = "cf")

    // Entrance
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(450, delayMillis = delay, easing = FastOutSlowInEasing), label = "ea")
    val entOffsetY by animateFloatAsState(if (appeared) 0f else 15f, tween(450, delayMillis = delay, easing = FastOutSlowInEasing), label = "ey")

    Box(
        modifier = Modifier
            .width(260.dp)
            .scale(cardFloat)
            .alpha(entAlpha)
            .offset(y = entOffsetY.dp)
    ) {
        // Animated gradient border
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        brush = borderGradient,
                        cornerRadius = CornerRadius(20.dp.toPx()),
                        style = Stroke(2.dp.toPx())
                    )
                }
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Category tag
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = BrandPrimary.copy(alpha = 0.04f),
                    border = BorderStroke(0.5.dp, BrandPrimary.copy(alpha = 0.06f))
                ) {
                    Text(
                        "GK Update",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(12.dp))

                // Action row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AddCircle, null, tint = BrandPrimary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add Info", style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    Surface(
                        shape = CircleShape,
                        color = BrandPrimary.copy(alpha = 0.05f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// ── Recent GK Section with header + two distinct buttons ──
@Composable
private fun RecentGkSection(onClick: () -> Unit) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(400), label = "sa")

    Column(modifier = Modifier.alpha(entAlpha)) {
        // ── Section header label ──
        Surface(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
            shape = RoundedCornerShape(999.dp),
            color = BrandPrimary.copy(alpha = 0.04f),
            border = BorderStroke(0.5.dp, BrandPrimary.copy(alpha = 0.06f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandPrimary))
                Spacer(Modifier.width(6.dp))
                Text("Recent GK", style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            RecentGkButton(
                modifier = Modifier.weight(1f),
                title = "BD GK",
                subtitle = "বাংলাদেশের সাম্প্রতিক তথ্য",
                flag = "\uD83C\uDDE7\uD83C\uDDE9",
                gradient = Brush.linearGradient(listOf(Color(0xFF0D4D3D), Color(0xFF0F766E), Color(0xFF14B8A6))),
                onClick = onClick
            )
            RecentGkButton(
                modifier = Modifier.weight(1f),
                title = "INT. GK",
                subtitle = "আন্তর্জাতিক সাম্প্রতিক তথ্য",
                flag = "\uD83C\uDF0D",
                gradient = Brush.linearGradient(listOf(Color(0xFF4A1942), Color(0xFF7C256C), Color(0xFFC241A0))),
                onClick = onClick
            )
        }
    }
}

@Composable
private fun RecentGkButton(
    modifier: Modifier,
    title: String,
    subtitle: String,
    flag: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    val inf = rememberInfiniteTransition(label = "rgb_$title")
    val borderPulse by inf.animateFloat(0.15f, 0.35f, infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Reverse), label = "bp")
    val btnShimmer by inf.animateFloat(-1.5f, 3f, infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Restart), label = "bs")

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(500, easing = FastOutSlowInEasing), label = "ea")

    Card(
        modifier = modifier.alpha(entAlpha).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.matchParentSize().clip(RoundedCornerShape(20.dp)).background(gradient))

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(Color.White.copy(alpha = borderPulse), Color.White.copy(alpha = borderPulse * 0.3f))
                            ),
                            cornerRadius = CornerRadius(20.dp.toPx()),
                            style = Stroke(1.5.dp.toPx())
                        )
                    }
            )

            Text(flag, fontSize = 48.sp, color = Color.White.copy(alpha = 0.1f), modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-8).dp, y = (-4).dp))

            Column(modifier = Modifier.padding(18.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.88f), maxLines = 1)
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("View All", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Box(
                        modifier = Modifier.matchParentSize().offset(x = (btnShimmer * 200).dp)
                            .background(Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent, Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0.15f), Color.Transparent)))
                    )
                }
            }
        }
    }
}

// ── Question Bank ─────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QuestionBankCard(onClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        PremiumWhiteCard(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            gradientBorder = Brush.linearGradient(listOf(BrandPrimary.copy(alpha = 0.5f), BrandSecondary.copy(alpha = 0.5f)))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(16.dp), color = BrandPrimary.copy(alpha = 0.05f), modifier = Modifier.size(56.dp)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.HistoryEdu, null, tint = BrandPrimary, modifier = Modifier.size(32.dp)) } }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("University Question Bank", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("Last 20 years questions with detailed solutions.", style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 1)
                    }
                }
                Spacer(Modifier.height(14.dp))
                Surface(shape = RoundedCornerShape(999.dp), color = BrandPrimary.copy(alpha = 0.04f), border = BorderStroke(0.5.dp, BrandPrimary.copy(alpha = 0.08f))) {
                    val mt = "DU Admission   \u2022   JU Unit B/C   \u2022   RU Admission   \u2022   CU Admission   \u2022   GST Combined   \u2022   JNU Questions   \u2022   " + "DU Admission   \u2022   JU Unit B/C   \u2022   RU Admission   \u2022   CU Admission   \u2022   GST Combined   \u2022   JNU Questions"
                    Text(mt, modifier = Modifier.fillMaxWidth().basicMarquee().padding(horizontal = 14.dp, vertical = 10.dp), style = MaterialTheme.typography.labelMedium, color = BrandPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}

// ── Today's Progress Rings ────────────────────────────
@Composable
private fun TodayProgressRings(practiced: Int, correct: Int, wrong: Int, navController: NavController) {
    val cp = if (practiced > 0) correct.toFloat() / practiced else 0f
    val wp = if (practiced > 0) wrong.toFloat() / practiced else 0f
    PremiumWhiteCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        gradientBorder = Brush.linearGradient(listOf(BrandPrimary.copy(alpha = 0.3f), BrandSecondary.copy(alpha = 0.3f)))
    ) {
        Box {
            Box(modifier = Modifier.size(140.dp).offset(x = 200.dp, y = (-30).dp).background(Brush.radialGradient(listOf(BrandSecondary.copy(alpha = 0.03f), Color.Transparent)), CircleShape))
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Today's Progress", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                    Surface(shape = RoundedCornerShape(999.dp), color = BrandPrimary.copy(alpha = 0.05f)) { Text("View Dashboard", modifier = Modifier.clickable { navController.navigate(DashboardRoute) }.padding(horizontal = 12.dp, vertical = 5.dp), style = MaterialTheme.typography.labelSmall, color = BrandPrimary, fontWeight = FontWeight.Bold, maxLines = 1) }
                }
                Spacer(Modifier.height(18.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ProgressRing(if (practiced > 0) 1f else 0f, practiced.toString(), "Solved", BrandPrimary)
                    ProgressRing(cp, correct.toString(), "Correct", SuccessColor)
                    ProgressRing(wp, wrong.toString(), "Wrong", ErrorColor)
                }
            }
        }
    }
}

// ── Bottom Navigation ─────────────────────────────────
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
