package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: GKViewModel, navController: NavController, onSignOut: () -> Unit) {
    // ── Dashboard data ──────────────────────────────
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
    val subscription by viewModel.subscription.collectAsStateWithLifecycle()
    val isPremium = remember(subscription) { viewModel.isPremiumActive(subscription) }
    val subStatus = remember(subscription) { viewModel.getSubscriptionStatus(subscription) }
    val trialDaysLeft = remember(subscription) { viewModel.getTrialRemainingDays(subscription) }
    val subDaysLeft = remember(subscription) { viewModel.getSubscriptionRemainingDays(subscription) }

    val today = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val monthName = remember { currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) }
    val monthDaysTotal = currentMonth.lengthOfMonth()

    val accuracy = if (totalPracticed > 0) (totalCorrect.toFloat() / totalPracticed * 100).toInt() else 0
    val hour = runCatching { java.time.LocalTime.now().hour }.getOrDefault(12)
    val greeting = when { hour < 12 -> "Good Morning"; hour < 17 -> "Good Afternoon"; hour < 20 -> "Good Evening"; else -> "Good Night" }

    // Week chart
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

    Scaffold(
        topBar = {
            Surface(color = AppBackground, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(40.dp)) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(26.dp), contentScale = ContentScale.Fit)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Student Dashboard", style = MaterialTheme.typography.titleLarge, color = BrandPrimary, fontWeight = FontWeight.Bold)
                            Text("Your complete admission prep overview", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Sign Out", tint = ErrorColor)
                    }
                }
            }
        },
        bottomBar = { PremiumBottomBar(navController) },
        containerColor = AppBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Hero ──────────────────────────────────
            item {
                DashboardHero(
                    greeting = greeting,
                    totalPracticed = totalPracticed,
                    accuracy = accuracy,
                    activeDays = currentMonthActiveDays,
                    totalDays = monthDaysTotal,
                    monthName = monthName,
                    quizAttempts = totalQuizAttempts,
                    megaQuizAttempts = totalMegaQuizAttempts,
                    totalActiveDays = totalActiveDays
                )
            }

            // ── Quick Actions ─────────────────────────
            item {
                QuickActionsRow(navController = navController)
            }

            // ── Weekly Activity ───────────────────────
            item {
                WeeklyActivityCard(weekDays = weekDays, weekMax = weekMax)
            }

            // ── Subject Progress ──────────────────────
            item {
                SubjectProgressSection(
                    bdTopicCount = bdTopics.size,
                    intTopicCount = intTopics.size,
                    totalPracticed = totalPracticed,
                    navController = navController
                )
            }

            // ── Month Consistency + Heatmap ────────────
            item {
                MonthlyConsistencyCard(
                    activeDays = currentMonthActiveDays,
                    totalDays = monthDaysTotal,
                    monthName = monthName,
                    monthProgress = currentMonthProgress
                )
            }

            // ── Recent Quiz Results ───────────────────
            if (recentQuizResults.isNotEmpty()) {
                item { SectionTitle("Latest Quiz Results") }
                items(recentQuizResults.take(5)) { result ->
                    QuizResultRow(result = result)
                }
            }

            // ── Recent Mega Quiz Results ──────────────
            if (recentMegaQuizResults.isNotEmpty()) {
                item { SectionTitle("Latest Mega Quiz Results") }
                items(recentMegaQuizResults.take(5)) { result ->
                    MegaQuizResultRow(result = result)
                }
            }

            // ── Achievements ──────────────────────────
            item {
                AchievementsCard(
                    totalPracticed = totalPracticed,
                    totalActiveDays = totalActiveDays,
                    totalQuizAttempts = totalQuizAttempts,
                    totalMegaQuizAttempts = totalMegaQuizAttempts
                )
            }

            // ── Subscription Card ────────────────────
            item {
                SubscriptionCard(
                    isPremium = isPremium,
                    subStatus = subStatus,
                    trialDaysLeft = trialDaysLeft,
                    subDaysLeft = subDaysLeft,
                    subscription = subscription,
                    viewModel = viewModel
                )
            }

            // ── Motivational Card ─────────────────────
            // ── Motivational Card ─────────────────────
            item {
                PremiumWhiteCard(modifier = Modifier.fillMaxWidth(), gradientBorder = Brush.linearGradient(listOf(BrandPrimary.copy(alpha = 0.3f), BrandSecondary.copy(alpha = 0.3f)))) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(14.dp), color = BrandPrimary.copy(alpha = 0.08f), modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.MilitaryTech, null, tint = BrandPrimary, modifier = Modifier.size(28.dp))
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Keep Going!", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("Regular practice builds confidence. Attempt a Mega Quiz to unlock exclusive achievements.", style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 18.sp)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// ════════════════════════════════════════════════════════
//  COMPONENTS
// ════════════════════════════════════════════════════════

// ── Subscription Card ──────────────────────────────
@Composable
private fun SubscriptionCard(
    isPremium: Boolean,
    subStatus: String,
    trialDaysLeft: Long,
    subDaysLeft: Long,
    subscription: GKViewModel.LocalSubscription?,
    viewModel: GKViewModel
) {
    val isFirstTime = subscription == null
    val isTrialActive = subStatus == "TRIAL"
    val isExpired = subStatus == "EXPIRED" || subStatus == "FREE_USER"
    val planLabel = when (subStatus) {
        "MONTHLY" -> "Monthly Plan"
        "QUARTERLY" -> "3-Month Plan"
        "HALF_YEARLY" -> "6-Month Plan"
        "YEARLY" -> "Yearly Plan"
        "LIFETIME" -> "Lifetime Plan"
        "TRIAL" -> "Free Trial"
        else -> "Free Tier"
    }

    Column {
        Text("Subscription", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        if (isPremium) {
            // ── Active Premium Card ──────────────
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF1E0047), Color(0xFF312E81), Color(0xFF5B21B6))))
                    .padding(24.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Verified, null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("PREMIUM ACTIVE", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f), fontWeight = FontWeight.Bold)
                        }
                        Surface(shape = RoundedCornerShape(999.dp), color = Color.White.copy(alpha = 0.18f), border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))) {
                            Text(planLabel, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PremiumFeatureBadge("\uD83C\uDFAF", "No Ads")
                        PremiumFeatureBadge("\uD83C\uDF1F", "Mega Quiz Free")
                        PremiumFeatureBadge("\u221E", "Unlimited Practice")
                        PremiumFeatureBadge("\uD83C\uDFC5", "Merit List")
                    }

                    if (subStatus != "LIFETIME" && subStatus != "TRIAL") {
                        Spacer(Modifier.height(14.dp))
                        Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.12f)) {
                            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("$subDaysLeft days remaining", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
        } else if (isTrialActive) {
            // ── Trial Active Card ────────────────
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF065F46), Color(0xFF059669), Color(0xFF34D399))))
                    .padding(24.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("Free Trial Active", style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
                                Text("$trialDaysLeft days remaining", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f))
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Text("Enjoy all premium features for free during your trial period!", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
                }
            }
        } else if (isFirstTime) {
            // ── Start Free Trial ─────────────────
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF7C3AED))))
                    .clickable { viewModel.startTrial() }
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocalFireDepartment, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Start Your 1-Month Free Trial", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Unlock premium features: No ads, free Mega Quiz, unlimited practice, central merit list & more.", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f), lineHeight = 18.sp)
                    Spacer(Modifier.height(14.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.2f), modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                            Text("Tap to Start Free Trial", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // ── Expired / Plans ──────────────────
            PlanSelectorCard(viewModel = viewModel)
        }
    }
}

@Composable
private fun PremiumFeatureBadge(emoji: String, text: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.12f), border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
private fun PlanSelectorCard(viewModel: GKViewModel) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 4.dp, border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Choose Your Plan", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Trial expired. Subscribe to continue enjoying premium features.", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.height(16.dp))

            // Plan list
            data class Plan(val type: String, val name: String, val price: Int, val perMonth: String, val color: Color, val days: Long)

            val plans = listOf(
                Plan("MONTHLY", "Monthly", 50, "৳50/month", BrandPrimary, 30),
                Plan("QUARTERLY", "3 Months", 120, "৳40/month", Color(0xFF0D9488), 90),
                Plan("HALF_YEARLY", "6 Months", 200, "৳33/month", Color(0xFF7C3AED), 180),
                Plan("YEARLY", "Yearly", 350, "৳29/month", Color(0xFFDC2626), 365),
                Plan("LIFETIME", "Lifetime", 2000, "One-time", Color(0xFFF59E0B), -1)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                plans.forEach { plan ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable { viewModel.purchasePlan(plan.type, plan.price, plan.days) },
                        shape = RoundedCornerShape(14.dp),
                        color = plan.color.copy(alpha = 0.04f),
                        border = BorderStroke(1.dp, plan.color.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(plan.name, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                Text(plan.perMonth, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = plan.color
                            ) {
                                Text(
                                    "৳${plan.price}",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Benefits list
            Surface(shape = RoundedCornerShape(12.dp), color = BrandPrimary.copy(alpha = 0.03f), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BenefitRow("\u2705", "No Advertisement")
                    BenefitRow("\u2705", "Mega Quiz Exams — Totally Free")
                    BenefitRow("\u2705", "Unlimited MCQ Practice")
                    BenefitRow("\u2705", "Central Merit List Access")
                    BenefitRow("\u2705", "Detailed Performance Analytics")
                    BenefitRow("\u2705", "Priority Question Bank Access")
                    BenefitRow("\u2705", "Data Persists After Reinstall")
                }
            }
        }
    }
}

@Composable
private fun BenefitRow(emoji: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 13.sp)
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = TextPrimary)
    }
}

// ── Standard White Card ────────────────────────────
@Composable
private fun PremiumWhiteCard(modifier: Modifier, gradientBorder: Brush? = null, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier) {
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 3.dp) { Box(content = content) }
        if (gradientBorder != null) {
            Box(modifier = Modifier.matchParentSize().drawBehind { drawRoundRect(gradientBorder, size = size, cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(1.5.dp.toPx())) })
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, modifier = Modifier.padding(horizontal = 4.dp), style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
}

// ── Hero ──────────────────────────────────────────────
@Composable
private fun DashboardHero(
    greeting: String, totalPracticed: Int, accuracy: Int,
    activeDays: Int, totalDays: Int, monthName: String,
    quizAttempts: Int, megaQuizAttempts: Int, totalActiveDays: Int
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(600), label = "dh")

    val streakEmoji = when {
        totalActiveDays >= 30 -> "\uD83D\uDD25"
        totalActiveDays >= 14 -> "\uD83D\uDE80"
        totalActiveDays >= 7 -> "\u2B50"
        totalActiveDays >= 1 -> "\uD83C\uDF31"
        else -> "\uD83D\uDC4B"
    }

    Box(modifier = Modifier.fillMaxWidth().alpha(entAlpha).clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(BrandPrimaryDeep, BrandPrimary, BrandSecondary))).padding(24.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("$greeting $streakEmoji", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                    Text("Admission Aspirant", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
                }
                Surface(shape = RoundedCornerShape(999.dp), color = Color.White.copy(alpha = 0.18f), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f))) {
                    Text("$streakEmoji $totalActiveDays Days", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HeroStatBox(totalPracticed.toString(), "MCQs Solved", Modifier.weight(1f))
                HeroStatBox("$accuracy%", "Accuracy", Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HeroStatBox("$activeDays/$totalDays", "$monthName Active", Modifier.weight(1f))
                HeroStatBox("$quizAttempts", "Quiz Attempts", Modifier.weight(1f))
            }
        }
        Text("\uD83D\uDCDA", fontSize = 72.sp, color = Color.White.copy(alpha = 0.05f), modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-6).dp, y = 6.dp))
    }
}

@Composable
private fun HeroStatBox(value: String, label: String, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha = 0.18f), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.78f), fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

// ── Quick Actions Row ─────────────────────────────────
@Composable
private fun QuickActionsRow(navController: NavController) {
    val actions = listOf(
        Triple(Icons.Filled.Public, "BD GK", BdGkColor) to { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) },
        Triple(Icons.Filled.Language, "INT GK", IntGkColor) to { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) },
        Triple(Icons.Filled.EmojiEvents, "Mega Quiz", MegaQuizColor) to { navController.navigate(MegaQuizRoutineRoute) },
        Triple(Icons.Filled.HistoryEdu, "Q-Bank", QuestionBankColor) to { navController.navigate(QuestionBankRoute(null, null)) { launchSingleTop = true } },
        Triple(Icons.Filled.Quiz, "Practice", BrandAccent) to { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) },
        Triple(Icons.Filled.RssFeed, "GK Feed", BrandPrimary) to { navController.navigate(RecentGKRoute) }
    )
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        actions.forEach { (action, onClick) ->
            Column(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White, RoundedCornerShape(16.dp)).border(1.dp, action.third.copy(alpha = 0.12f), RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(shape = CircleShape, color = action.third.copy(alpha = 0.08f), modifier = Modifier.size(40.dp)) { Box(contentAlignment = Alignment.Center) { Icon(action.first, null, tint = action.third, modifier = Modifier.size(20.dp)) } }
                Spacer(Modifier.height(6.dp))
                Text(action.second, style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}

// ── Weekly Activity Card ──────────────────────────────
@Composable
private fun WeeklyActivityCard(weekDays: List<Triple<String, Int, Boolean>>, weekMax: Int) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 3.dp, border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Weekly Activity", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("Last 7 days", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                weekDays.forEach { (dayLabel, count, isToday) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                        Text(if (count > 0) count.toString() else "", style = MaterialTheme.typography.labelSmall, color = if (count > 0) BrandPrimary else Color.Transparent, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        val barH by animateFloatAsState(if (count > 0) ((count.toFloat() / weekMax) * 80.dp.value).coerceAtLeast(4f) else 4f, tween(800, easing = FastOutSlowInEasing), label = "bar$dayLabel")
                        Box(modifier = Modifier.width(24.dp).height(barH.dp).clip(RoundedCornerShape(6.dp)).let { mod ->
                            if (count > 0) mod.background(Brush.verticalGradient(listOf(BrandPrimary, BrandSecondary)))
                            else mod.background(Color(0xFFE8E8F0))
                        })
                        if (isToday) { Spacer(Modifier.height(2.dp)); Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandAccent)) }
                        Spacer(Modifier.height(6.dp))
                        Text(dayLabel, style = MaterialTheme.typography.labelSmall, color = if (isToday) BrandPrimary else TextMuted, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

// ── Subject Progress ──────────────────────────────────
@Composable
private fun SubjectProgressSection(bdTopicCount: Int, intTopicCount: Int, totalPracticed: Int, navController: NavController) {
    Column {
        Text("Subject Progress", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SubjectCard("BD GK", bdTopicCount, GradientBdGkBg, BdGkColor, "\uD83C\uDDE7\uD83C\uDDE9") { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
            SubjectCard("INT GK", intTopicCount, GradientIntlGkBg, IntGkColor, "\uD83C\uDF0D") { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) }
        }
        Spacer(Modifier.height(12.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = BrandAccent.copy(alpha = 0.08f), modifier = Modifier.size(40.dp)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.TrendingUp, null, tint = BrandAccent, modifier = Modifier.size(22.dp)) } }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Overall Progress", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                    val goal = 50; val frac = (totalPracticed.toFloat() / goal).coerceIn(0f, 1f)
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFFE8E8F0))) { Box(modifier = Modifier.fillMaxWidth(frac).height(6.dp).clip(RoundedCornerShape(999.dp)).background(Brush.horizontalGradient(listOf(BrandAccent, BrandPrimary)))) }
                    Spacer(Modifier.height(2.dp))
                    Text("$totalPracticed / $goal MCQs", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun RowScope.SubjectCard(title: String, count: Int, gradient: Brush, color: Color, emoji: String, onClick: () -> Unit) {
    Surface(modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), color = Color.Transparent, shadowElevation = 4.dp) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.matchParentSize().clip(RoundedCornerShape(20.dp)).background(gradient))
            Text(emoji, fontSize = 44.sp, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp), color = Color.White.copy(alpha = 0.12f))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(count.toString(), style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
                Text(title, style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) { Text("Explore", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold); Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
            }
        }
    }
}

// ── Monthly Consistency Card ──────────────────────────
@Composable
private fun MonthlyConsistencyCard(activeDays: Int, totalDays: Int, monthName: String, monthProgress: List<MCQPracticeProgressEntity>) {
    val fraction = if (totalDays > 0) activeDays.toFloat() / totalDays else 0f

    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 3.dp, border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.08f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text("$monthName Consistency", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold); Text("$activeDays of $totalDays days active", style = MaterialTheme.typography.labelSmall, color = TextSecondary) }
                Surface(shape = CircleShape, color = BrandPrimary.copy(alpha = 0.08f), modifier = Modifier.size(44.dp)) { Box(contentAlignment = Alignment.Center) { Text("$activeDays", style = MaterialTheme.typography.titleLarge, color = BrandPrimary, fontWeight = FontWeight.ExtraBold) } }
            }
            Spacer(Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(Color(0xFFE8E8F0))) { Box(modifier = Modifier.fillMaxWidth(fraction.coerceIn(0f, 1f)).height(8.dp).clip(RoundedCornerShape(999.dp)).background(Brush.horizontalGradient(listOf(BrandPrimary, BrandSecondary)))) }

            if (activeDays > 0) {
                Spacer(Modifier.height(12.dp))
                val daysInMonth = monthProgress.associateBy { it.dateStr }
                val firstDay = java.time.LocalDate.of(YearMonth.now().year, YearMonth.now().month, 1)
                val days = (1..totalDays).map { firstDay.withDayOfMonth(it) }
                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    days.forEach { date ->
                        val ds = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        val prog = daysInMonth[ds]
                        val intensity = if (prog != null && prog.totalPracticed > 0) when { prog.totalPracticed >= 20 -> 1f; prog.totalPracticed >= 10 -> 0.7f; else -> 0.4f } else 0.08f
                        Box(modifier = Modifier.size(16.dp).clip(RoundedCornerShape(3.dp)).background(if (prog != null && prog.totalPracticed > 0) BrandPrimary.copy(alpha = intensity) else Color(0xFFE8E8F0)))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Less", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFE8E8F0)))
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary.copy(alpha = 0.4f)))
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary.copy(alpha = 0.7f)))
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(BrandPrimary))
                    Text("More", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

// ── Achievements ──────────────────────────────────────
@Composable
private fun AchievementsCard(totalPracticed: Int, totalActiveDays: Int, totalQuizAttempts: Int, totalMegaQuizAttempts: Int) {
    data class Ach(val emoji: String, val title: String, val unlocked: Boolean, val desc: String, val color: Color)

    val achievements = listOf(
        Ach("\uD83C\uDFC6", "Practice Starter", totalPracticed >= 10, "Solve 10 MCQs", BrandAccent),
        Ach("\uD83D\uDD25", "7-Day Streak", totalActiveDays >= 7, "Active for 7 days", WarningColor),
        Ach("\uD83D\uDE80", "Quiz Pro", totalQuizAttempts >= 5, "Complete 5 quizzes", BrandSecondary),
        Ach("\uD83C\uDF1F", "Mega Master", totalMegaQuizAttempts >= 1, "Attempt 1 Mega Quiz", MegaQuizColor),
        Ach("\uD83D\uDC8E", "100 Club", totalPracticed >= 100, "Solve 100 MCQs", BrandPrimary),
        Ach("\uD83C\uDF0A", "30-Day Champ", totalActiveDays >= 30, "Active for 30 days", Color(0xFFEC4899))
    )

    Column {
        Text("Achievements", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 3.dp, border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))) {
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                achievements.forEach { ach ->
                    Column(modifier = Modifier.width(90.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(shape = CircleShape, color = if (ach.unlocked) ach.color.copy(alpha = 0.12f) else Color(0xFFF2F2F7), modifier = Modifier.size(48.dp)) { Box(contentAlignment = Alignment.Center) { Text(ach.emoji, fontSize = 20.sp, modifier = if (!ach.unlocked) Modifier.alpha(0.35f) else Modifier) } }
                        Spacer(Modifier.height(6.dp))
                        Text(ach.title, style = MaterialTheme.typography.labelSmall, color = if (ach.unlocked) TextPrimary else TextMuted, fontWeight = if (ach.unlocked) FontWeight.Bold else FontWeight.Normal, textAlign = TextAlign.Center, maxLines = 2)
                        Text(ach.desc, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// ── Quiz Result Row ───────────────────────────────────
@Composable
private fun QuizResultRow(result: MCQQuizResultEntity) {
    val dateStr = runCatching { java.time.Instant.ofEpochMilli(result.dateTaken).atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) }.getOrDefault("Unknown")
    val c = when { result.scorePercentage >= 70 -> SuccessColor; result.scorePercentage >= 40 -> BrandAccent; else -> ErrorColor }
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, border = BorderStroke(1.dp, c.copy(alpha = 0.12f))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = c.copy(alpha = 0.1f), modifier = Modifier.size(42.dp)) { Box(contentAlignment = Alignment.Center) { Text("${result.scorePercentage.toInt()}%", style = MaterialTheme.typography.titleSmall, color = c, fontWeight = FontWeight.ExtraBold) } }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text("MCQ Quiz", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold); Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextSecondary) }
            Icon(Icons.Filled.TrendingUp, null, tint = c, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun MegaQuizResultRow(result: MegaQuizResultEntity) {
    val dateStr = runCatching { java.time.Instant.ofEpochMilli(result.dateTaken).atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) }.getOrDefault("Unknown")
    val c = when { result.score >= 70 -> SuccessColor; result.score >= 40 -> BrandAccent; else -> ErrorColor }
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, border = BorderStroke(1.dp, c.copy(alpha = 0.12f))) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = c.copy(alpha = 0.1f), modifier = Modifier.size(42.dp)) { Box(contentAlignment = Alignment.Center) { Text("${result.score.toInt()}%", style = MaterialTheme.typography.titleSmall, color = c, fontWeight = FontWeight.ExtraBold) } }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text("Mega Quiz", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold); Text("$dateStr  •  ${result.correctCount}C/${result.wrongCount}W", style = MaterialTheme.typography.labelSmall, color = TextSecondary) }
            Icon(Icons.Filled.EmojiEvents, null, tint = c, modifier = Modifier.size(18.dp))
        }
    }
}

// ── Bottom Navigation ─────────────────────────────────
@Composable
private fun PremiumBottomBar(navController: NavController) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).navigationBarsPadding(), shape = RoundedCornerShape(28.dp), color = Color(0xE6FFFFFF), shadowElevation = 8.dp) {
        Box {
            Box(modifier = Modifier.matchParentSize().border(0.5.dp, Color(0x33C7C0EE), RoundedCornerShape(28.dp)))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                NavItem(Icons.Filled.Home, "Home", false) { navController.navigate(HomeRoute) { launchSingleTop = true } }
                NavItem(Icons.Filled.Public, "GK", false) { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
                NavItem(Icons.AutoMirrored.Filled.MenuBook, "Study", false) { navController.navigate(QuestionBankRoute(null, null)) { launchSingleTop = true } }
                NavItem(Icons.Filled.EmojiEvents, "Quiz", false) { navController.navigate(MegaQuizRoutineRoute) { launchSingleTop = true } }
                NavItem(Icons.Filled.Person, "Profile", true) { navController.navigate(ProfileRoute) { launchSingleTop = true } }
            }
        }
    }
}

@Composable
private fun NavItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    val cc = if (selected) BrandPrimary else TextMuted
    val sc by animateFloatAsState(if (selected) 1.15f else 1f, tween(300, easing = FastOutSlowInEasing), label = "ns")
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (selected) BrandPrimary.copy(alpha = 0.05f) else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = cc, modifier = Modifier.size(if (selected) 24.dp else 20.dp).scale(sc))
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = cc, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, maxLines = 1)
        if (selected) { Spacer(Modifier.height(2.dp)); Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandPrimary)) }
    }
}
