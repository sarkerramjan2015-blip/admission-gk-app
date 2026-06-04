package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.navigation.MegaQuizIntroRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizRoutineScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Surface(
                color = BrandGradientStart.copy(alpha = 0.95f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.Timer, contentDescription = "Timer", tint = Color.White)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("Mega Quiz", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = BrandGradientStart
                    ) {
                        Text("00:44:18", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "Exams")
        },
        containerColor = BgPrimary
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Hero Calendar Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(BrandGradientStart, BrandGradientEnd, WarningColor)))
                            .padding(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column {
                                Text("June 2026", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("This month: 2 Mega Quiz Exams", fontSize = 18.sp, color = Color(0xFFb1afff))
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Upcoming: Admission GK", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }

            // Routine Title
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Mega Quiz Routine", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = BrandGradientStart)
                    Text("2 Active Items", color = Color(0xFF464555), fontSize = 16.sp)
                }
            }

            // Quiz List Bento Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Card 1 (Live)
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val pulseAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseAlpha"
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color.Transparent),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Surface(
                                    color = BrandGradientStart.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Filled.Quiz, contentDescription = null, tint = Color(0xFF1e00a9), modifier = Modifier.padding(12.dp))
                                }
                                Surface(
                                    color = Color(0xFFffdad6),
                                    shape = CircleShape
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                                        Box(modifier = Modifier.size(8.dp).background(Color(0xFFba1a1a).copy(alpha = pulseAlpha), CircleShape))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Live Now", color = Color(0xFF93000a), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Mega Quiz 1: History & Civics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color(0xFF1b1a28))
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("June 12, 2026", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("8:00 PM (Duration: 20 Min)", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.FormatListNumbered, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("30 MCQ Questions", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { navController.navigate(MegaQuizIntroRoute("live_exam_1")) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFffb95f), contentColor = Color(0xFF653e00)),
                                    shape = CircleShape,
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Join Now", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                                    }
                                }
                                OutlinedButton(
                                    onClick = { },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1e00a9)),
                                    border = BorderStroke(1.dp, Color(0xFF777587)),
                                    shape = CircleShape,
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text("View Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                            }
                        }
                    }

                    // Card 2 (Upcoming)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color.Transparent),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Surface(
                                    color = BrandGradientEnd.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Filled.PendingActions, contentDescription = null, tint = Color(0xFF712ae2), modifier = Modifier.padding(12.dp))
                                }
                                Surface(
                                    color = Color(0xFFe3e0f4),
                                    shape = CircleShape
                                ) {
                                    Text("Upcoming", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color(0xFF464555), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Mega Quiz 2: Geography & Science", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color(0xFF1b1a28))
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("June 26, 2026", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("8:00 PM (Duration: 20 Min)", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.FormatListNumbered, contentDescription = null, tint = Color(0xFF464555), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("30 MCQ Questions", color = Color(0xFF464555), fontSize = 16.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFdbd8ec), contentColor = Color(0xFF464555)),
                                    shape = CircleShape,
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    enabled = false
                                ) {
                                    Text("Locked", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                                OutlinedButton(
                                    onClick = { },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF712ae2)),
                                    border = BorderStroke(1.dp, Color(0xFF777587)),
                                    shape = CircleShape,
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text("View Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Info Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFf5f2ff)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f/9f)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://lh3.googleusercontent.com/aida-public/AB6AXuBOprTSSKzA1QzrOWUpR6VptUVe2hS-6ES18-NmVeYgXRD0qkoDJjMjU9Ye6PPCTkHS6GGZ3Ut_9SO5rda8OQVWKRJu5dgbljKFBGGL9b2WMhohDvg7_6cGbOYdnsHe-jFFTijXAG8c6xLNSPm-FtjLSXjcEVF3d3LsZJGzIpdFlsySNETgU1FbRrBsZHKt5wmG8teg3aBYjE1udstNe4gObB8FE_6pBroFwaiyrDQKIQ4VvkOt5c9HljAfeg9S39NMgtJ7J2mUxp78")
                                    .crossfade(true).build(),
                                contentDescription = "Exam Preparation",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1e00a9).copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Prepare Smarter", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF1b1a28))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Master General Knowledge for university admissions with our curated Mega Quiz series. Real-time ranking, detailed analysis, and expert-reviewed solutions.",
                            color = Color(0xFF464555),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("15k+", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = BrandGradientStart)
                                Text("Active Students", color = Color(0xFF464555), fontSize = 14.sp)
                            }
                            Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFc7c4d8)))
                            Column {
                                Text("98%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = BrandGradientEnd)
                                Text("Satisfaction", color = Color(0xFF464555), fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
