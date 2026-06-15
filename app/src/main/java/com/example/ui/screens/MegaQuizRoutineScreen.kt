package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionBottomBar
import com.example.ui.components.AdmissionTopBar
import com.example.ui.navigation.MegaQuizIntroRoute
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizRoutineScreen(viewModel: GKViewModel, navController: NavController) {
    val exams by viewModel.megaQuizExams.collectAsStateWithLifecycle(initialValue = emptyList())
    val latestScore by viewModel.getLatestMegaQuizScore().collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admission GK",
                subtitle = "মেগা কুইজ",
                showBack = true,
                onBack = { navController.popBackStack() },
                useGradient = true
            )
        },
        bottomBar = { AdmissionBottomBar(navController = navController, currentRoute = "MegaQuiz") },
        containerColor = AppBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Hero Score Card
            if (latestScore != null) {
                item { ScoreHeroCard(latestScore!!) }
            }

            // Section header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "মেগা কুইজ রুটিন",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "তিনটি পরীক্ষার সিডিউল",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = BrandPrimary.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.12f))
                    ) {
                        Text(
                            "${exams.size}টি পরীক্ষা",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            // Exam Cards
            itemsIndexed(exams) { index, exam ->
                val isLive = exam.status == "LIVE"
                val isCompleted = exam.status == "COMPLETED"
                val rem = remember(exam.examDate) { exam.examDate - Instant.now().epochSecond }
                val daysLeft = (rem / 86400).toInt()

                var countdownDisplay by remember { mutableStateOf(computeCountdown(rem)) }
                LaunchedEffect(rem) {
                    while (rem > 0 && !isCompleted) {
                        delay(60000)
                        val r = exam.examDate - Instant.now().epochSecond
                        countdownDisplay = computeCountdown(r)
                    }
                }

                PolishedExamCard(
                    index = index + 1,
                    title = exam.title,
                    isLive = isLive,
                    isCompleted = isCompleted,
                    daysLeft = daysLeft,
                    countdownText = countdownDisplay,
                    onClick = { navController.navigate(MegaQuizIntroRoute(exam.id)) }
                )
            }

            // Strategy card
            item { StrategyCard() }

            // View Results
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("results") },
                    shape = RoundedCornerShape(16.dp),
                    color = BrandPrimary.copy(alpha = 0.04f),
                    border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Assessment, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "আগের রেজাল্ট দেখুন",
                            style = MaterialTheme.typography.labelLarge,
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

private fun computeCountdown(rem: Long): String {
    if (rem <= 0) return "চলছে..."
    val days = rem / 86400
    val hrs = (rem % 86400) / 3600
    val min = (rem % 3600) / 60
    return if (days > 0) "আর ${days} দিন ${hrs} ঘন্টা বাকি" else "আর ${hrs} ঘন্টা ${min} মিনিট বাকি"
}

@Composable
private fun ScoreHeroCard(score: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(GradientMegaQuizBg).padding(22.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Text(
                        "আপনার শেষ স্কোর",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "%.2f".format(score),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("পয়েন্ট", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun PolishedExamCard(
    index: Int,
    title: String,
    isLive: Boolean,
    isCompleted: Boolean,
    daysLeft: Int,
    countdownText: String,
    onClick: () -> Unit,
) {
    val inf = rememberInfiniteTransition(label = "ec_$index")
    val borderPulse by inf.animateFloat(0.08f, 0.2f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse), label = "bp")
    val btnShimmer by inf.animateFloat(-2f, 3f, infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart), label = "bs")

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(400, delayMillis = index * 120, easing = FastOutSlowInEasing), label = "ea")
    val entSlide by animateFloatAsState(if (appeared) 0f else 20f, tween(400, delayMillis = index * 120, easing = FastOutSlowInEasing), label = "es")

    val cardColor = when {
        isLive -> Color(0xFFF0EDFF)
        isCompleted -> Color(0xFFF5F5F5)
        else -> Color.White
    }
    val accentColor = when {
        isLive -> ErrorColor
        isCompleted -> TextMuted
        else -> BrandPrimary
    }
    val statusText = when {
        isCompleted -> "সম্পন্ন"
        isLive -> "লাইভ"
        else -> "আসন্ন"
    }
    val btnText = when {
        isCompleted -> "রিভিউ দেখুন"
        isLive -> "পরীক্ষা দিন"
        else -> "বিস্তারিত দেখুন"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(entAlpha)
            .offset(y = entSlide.dp)
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(
                            accentColor.copy(alpha = borderPulse),
                            accentColor.copy(alpha = borderPulse * 0.4f)
                        )
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLive) 6.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        "পরীক্ষা #$index",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = accentColor.copy(alpha = if (isLive) 0.15f else 0.08f),
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isLive) {
                            val dotAlpha by inf.animateFloat(0.5f, 1f, infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse), label = "da")
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ErrorColor.copy(alpha = dotAlpha)))
                        }
                        Text(
                            statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailChip(Icons.Filled.FormatListNumbered, "৩০টি MCQ")
                DetailChip(Icons.Filled.Schedule, "২০ মিনিট")
                DetailChip(Icons.Filled.Star, "+১, −০.২৫")
            }

            Spacer(Modifier.height(10.dp))

            if (!isCompleted) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = accentColor.copy(alpha = 0.06f),
                    border = BorderStroke(0.5.dp, accentColor.copy(alpha = 0.12f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        countdownText,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(12.dp))
            } else {
                Spacer(Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor)
                    .clickable(onClick = onClick)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(btnText, style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                if (isLive) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = (btnShimmer * 300).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.35f),
                                        Color.White.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }

            if (!isLive && !isCompleted && daysLeft > 0) {
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = BrandPrimary.copy(alpha = 0.04f),
                    border = BorderStroke(0.5.dp, BrandPrimary.copy(alpha = 0.06f))
                ) {
                    Text(
                        "📅 আর ${daysLeft} দিন বাকি",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = TextMuted, maxLines = 1)
    }
}

@Composable
private fun StrategyCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandPrimary.copy(alpha = 0.08f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.TipsAndUpdates, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text("প্রস্তুতির কৌশল", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Spacer(Modifier.height(12.dp))
            StrategyItem("প্রথমে স্পেশাল নোট ভালো করে পড়ুন")
            StrategyItem("MCQ প্র্যাকটিস করে নিজেকে যাচাই করুন")
            StrategyItem("টাইমড মেগা কুইজে অংশগ্রহণ করুন")
            StrategyItem("প্রতিটি পরীক্ষার রেজাল্ট রেকর্ড থাকবে")
            StrategyItem("আপনার প্রোগ্রেস রিপোর্ট দেখতে পারবেন")
        }
    }
}

@Composable
private fun StrategyItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}
