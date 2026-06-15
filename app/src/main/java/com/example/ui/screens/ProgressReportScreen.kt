package com.example.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.MCQPracticeProgressEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionBottomBar
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

enum class ProgressTab { MONTHLY, DAILY, YEARLY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressReportScreen(viewModel: GKViewModel, navController: NavController) {
    val today = remember { LocalDate.now() }
    val currentYearMonth = remember { YearMonth.now() }
    val monthPrefix = remember { currentYearMonth.toString() + "-" }
    val yearPrefix = remember { today.year.toString() + "-" }

    val monthlyProgress by viewModel.getMonthlyProgress(monthPrefix)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val yearlyProgress by viewModel.getYearlyProgress(yearPrefix)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val allProgress by viewModel.getAllProgress()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedTab by remember { mutableStateOf(ProgressTab.MONTHLY) }

    val monthTotalPracticed = monthlyProgress.sumOf { it.totalPracticed }
    val monthTotalCorrect = monthlyProgress.sumOf { it.correctCount }
    val monthTotalWrong = monthlyProgress.sumOf { it.wrongCount }
    val monthAccuracy = if (monthTotalPracticed > 0)
        (monthTotalCorrect.toFloat() / monthTotalPracticed * 100).toInt() else 0
    val monthActiveDays = monthlyProgress.count { it.totalPracticed > 0 }

    val yearTotalPracticed = yearlyProgress.sumOf { it.totalPracticed }
    val yearTotalCorrect = yearlyProgress.sumOf { it.correctCount }
    val yearTotalWrong = yearlyProgress.sumOf { it.wrongCount }
    val yearAccuracy = if (yearTotalPracticed > 0)
        (yearTotalCorrect.toFloat() / yearTotalPracticed * 100).toInt() else 0
    val yearActiveDays = yearlyProgress.count { it.totalPracticed > 0 }

    val dailyData = if (selectedTab == ProgressTab.DAILY) monthlyProgress
        else if (selectedTab == ProgressTab.YEARLY) yearlyProgress
        else emptyList()

    val totalPracticed = when (selectedTab) {
        ProgressTab.MONTHLY -> monthTotalPracticed
        ProgressTab.DAILY -> monthTotalPracticed
        ProgressTab.YEARLY -> yearTotalPracticed
    }
    val totalCorrect = when (selectedTab) {
        ProgressTab.MONTHLY -> monthTotalCorrect
        ProgressTab.DAILY -> monthTotalCorrect
        ProgressTab.YEARLY -> yearTotalCorrect
    }
    val totalWrong = when (selectedTab) {
        ProgressTab.MONTHLY -> monthTotalWrong
        ProgressTab.DAILY -> monthTotalWrong
        ProgressTab.YEARLY -> yearTotalWrong
    }
    val accuracy = when (selectedTab) {
        ProgressTab.MONTHLY -> monthAccuracy
        ProgressTab.DAILY -> monthAccuracy
        ProgressTab.YEARLY -> yearAccuracy
    }
    val activeDays = when (selectedTab) {
        ProgressTab.MONTHLY -> monthActiveDays
        ProgressTab.DAILY -> monthActiveDays
        ProgressTab.YEARLY -> yearActiveDays
    }

    Scaffold(
        topBar = {
            Surface(
                color = AppBackground,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BrandPrimary
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Progress Report",
                        style = MaterialTheme.typography.titleLarge,
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        bottomBar = { AdmissionBottomBar(navController, "Profile") },
        containerColor = AppBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Hero
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(BrandPrimary, BrandPrimaryDeep, BrandSecondary)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + today.year,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Your admission prep journey at a glance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.18f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "$totalPracticed",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text("MCQs", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.18f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "$accuracy%",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text("Accuracy", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.18f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "$activeDays",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text("Active Days", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Tab Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProgressTab.entries.forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = {
                                Text(
                                    when (tab) {
                                        ProgressTab.MONTHLY -> "Monthly"
                                        ProgressTab.DAILY -> "Daily"
                                        ProgressTab.YEARLY -> "Yearly"
                                    }
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(999.dp)
                        )
                    }
                }
            }

            // Correct vs Wrong Visual Bar
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Performance Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        val totalAttempts = totalCorrect + totalWrong
                        val correctFraction = if (totalAttempts > 0) totalCorrect.toFloat() / totalAttempts else 0f
                        val wrongFraction = if (totalAttempts > 0) totalWrong.toFloat() / totalAttempts else 0f

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFFE8E8F0))
                        ) {
                            if (totalAttempts > 0) {
                                Row(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(correctFraction)
                                            .fillMaxHeight()
                                            .background(
                                                Brush.linearGradient(listOf(SuccessColor, Color(0xFF22C55E)))
                                            )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .weight(wrongFraction)
                                            .fillMaxHeight()
                                            .background(
                                                Brush.linearGradient(listOf(ErrorColor, Color(0xFFEF4444)))
                                            )
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No attempts yet", color = TextMuted, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SuccessColor))
                                Spacer(Modifier.width(6.dp))
                                Text("$totalCorrect Correct", fontSize = 13.sp, color = SuccessColor, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(ErrorColor))
                                Spacer(Modifier.width(6.dp))
                                Text("$totalWrong Wrong", fontSize = 13.sp, color = ErrorColor, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Active Days Progress
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, Color(0xFFE8E8F0).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Study Consistency",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "$activeDays days active",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = BrandPrimary.copy(alpha = 0.08f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        "$activeDays",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        val totalDays = when (selectedTab) {
                            ProgressTab.MONTHLY, ProgressTab.DAILY -> currentYearMonth.lengthOfMonth()
                            ProgressTab.YEARLY -> today.dayOfYear
                        }
                        val fraction = if (totalDays > 0) activeDays.toFloat() / totalDays else 0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFFE8E8F0))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(SuccessColor, BrandPrimary, BrandSecondary)
                                        )
                                    )
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$activeDays of $totalDays days",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            // Day List Header
            item {
                Text(
                    when (selectedTab) {
                        ProgressTab.MONTHLY -> "Daily Activity"
                        ProgressTab.DAILY -> "Day-by-Day Breakdown"
                        ProgressTab.YEARLY -> "Monthly Summary"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (dailyData.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = AppSurface,
                        border = BorderStroke(1.dp, AppOutlineSoft)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.HourglassEmpty,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No practice data yet",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Start solving MCQs to track your progress here!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(dailyData.sortedByDescending { it.dateStr }) { progress ->
                    DailyProgressCard(progress = progress)
                }
            }
        }
    }
}

@Composable
private fun DailyProgressCard(progress: MCQPracticeProgressEntity) {
    val total = progress.totalPracticed
    val correct = progress.correctCount
    val wrong = progress.wrongCount
    val accuracy = if (total > 0) (correct.toFloat() / total * 100).toInt() else 0

    val dateStr = runCatching {
        LocalDate.parse(progress.dateStr).format(DateTimeFormatter.ofPattern("dd MMM, EEEE"))
    }.getOrDefault(progress.dateStr)

    val accentColor = when {
        accuracy >= 70 -> SuccessColor
        accuracy >= 40 -> BrandAccent
        else -> ErrorColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.1f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "$accuracy%",
                        style = MaterialTheme.typography.titleSmall,
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    dateStr,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = SuccessColor.copy(alpha = 0.1f),
                            modifier = Modifier.size(8.dp)
                        ) {
                            Box(modifier = Modifier.background(SuccessColor, CircleShape))
                        }
                        Spacer(Modifier.width(4.dp))
                        Text("$correct", fontSize = 13.sp, color = SuccessColor, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ErrorColor.copy(alpha = 0.1f),
                            modifier = Modifier.size(8.dp)
                        ) {
                            Box(modifier = Modifier.background(ErrorColor, CircleShape))
                        }
                        Spacer(Modifier.width(4.dp))
                        Text("$wrong", fontSize = 13.sp, color = ErrorColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$total",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BrandPrimary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("MCQs", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
        }
    }
}
