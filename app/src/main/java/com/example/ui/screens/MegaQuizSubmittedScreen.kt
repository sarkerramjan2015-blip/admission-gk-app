package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.R
import com.example.ui.components.AdmissionBottomBar
import com.example.ui.navigation.HomeRoute
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MegaQuizSubmittedScreen(total: Int, answered: Int, unanswered: Int, navController: NavController) {
    val scale = remember { mutableStateOf(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            scale.value = if (scale.value == 1f) 1.08f else 1f
        }
    }

    val submissionTime = remember {
        try {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("bn"))
            val timeSdf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val now = Date()
            "${sdf.format(now)} • ${timeSdf.format(now)}"
        } catch (e: Exception) { "—" }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F9FF))) {
        // ── Header ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Brush.linearGradient(listOf(Color(0xFF2E2395), Color(0xFF3F36D9), Color(0xFF6C4DFF))))
        ) {
            Box(Modifier.size(200.dp).offset(x = (-60).dp, y = (-60).dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)), CircleShape))

            Column(modifier = Modifier.fillMaxSize().padding(top = 44.dp, start = 16.dp, end = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(24.dp), contentScale = ContentScale.Fit)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("মেগা কুইজ", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFF6C4DFF).copy(alpha = 0.25f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.20f))
                    ) {
                        Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(5.dp))
                            Text("ফলাফল অপেক্ষায়", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ── Content ──
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 140.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Success confirmation ──
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated success icon
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .scale(scale.value)
                                .background(Brush.radialGradient(listOf(Color(0xFF00A878).copy(alpha = 0.10f), Color.Transparent)), CircleShape)
                        )
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF00A878).copy(alpha = 0.10f),
                            border = BorderStroke(6.dp, Color(0xFF00A878).copy(alpha = 0.05f)),
                            modifier = Modifier.size(88.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF00A878), modifier = Modifier.size(54.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "পরীক্ষা জমা হয়েছে!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF07113F),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "ফলাফল আগামীকাল প্রকাশ করা হবে।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5E6480)
                    )
                    Spacer(Modifier.height(12.dp))

                    // Info card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF0FFF4),
                        border = BorderStroke(0.5.dp, Color(0xFF00A878).copy(alpha = 0.20f))
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Info, null, tint = Color(0xFF00A878), modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "আপনার উত্তর সফলভাবে জমা নেওয়া হয়েছে। ধন্যবাদ।",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF5E6480)
                            )
                        }
                    }
                }
            }

            // ── Confirmation Summary Card ──
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize().drawBehind {
                        drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                    }) {
                        Column(modifier = Modifier.padding(22.dp)) {
                            // Header row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "CONFIRMATION SUMMARY",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6C4DFF),
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "মেগা কুইজ",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color(0xFF07113F),
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                Surface(
                                    modifier = Modifier.size(46.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF2638D9).copy(alpha = 0.06f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Filled.BarChart, null, tint = Color(0xFF2638D9), modifier = Modifier.size(24.dp))
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            // Summary boxes
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SummaryBox(
                                    modifier = Modifier.weight(1f),
                                    label = "মোট প্রশ্ন",
                                    value = "$total",
                                    valueColor = Color(0xFF2638D9),
                                    bgColor = Color(0xFFEEF0FF)
                                )
                                SummaryBox(
                                    modifier = Modifier.weight(1f),
                                    label = "উত্তর দিয়েছেন",
                                    value = "$answered",
                                    valueColor = Color(0xFF00A878),
                                    bgColor = Color(0xFFEFFFF9)
                                )
                                SummaryBox(
                                    modifier = Modifier.weight(1f),
                                    label = "উত্তর দেননি",
                                    value = "$unanswered",
                                    valueColor = Color(0xFFEF4444),
                                    bgColor = Color(0xFFFFF7F8)
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            // Submission time
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, null, tint = Color(0xFF8A90A8), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "জমা দেওয়ার সময়: $submissionTime",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5E6480)
                                )
                            }
                        }
                    }
                }
            }

            // ── Back to Home Button ──
            item {
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF2638D9), Color(0xFF3F36D9))))
                        .clickable {
                            navController.navigate(HomeRoute) { popUpTo(0) }
                        },
                    shape = RoundedCornerShape(30.dp),
                    color = Color.Transparent,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Home, null, tint = Color.White, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("হোমে ফিরে যান", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(80.dp))
            }
        }

        // ── Bottom Nav ──
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AdmissionBottomBar(navController = navController, currentRoute = "MegaQuiz")
        }
    }
}

@Composable
private fun SummaryBox(
    modifier: Modifier,
    label: String,
    value: String,
    valueColor: Color,
    bgColor: Color
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
            Text(label, fontSize = 10.sp, color = valueColor.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
        }
    }
}
