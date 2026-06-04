package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.MegaQuizRoutineRoute
import com.example.ui.navigation.MegaQuizLiveRoute
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizSubmittedScreen(total: Int, answered: Int, unanswered: Int, navController: NavController) {

    // Animation for success blob
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF1e00a9).copy(alpha = 0.95f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().zIndex(10f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Timer, contentDescription = "Timer", tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Mega Quiz", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFF3525cd).copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            "00:45:00", 
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, "Exams")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFfcf8ff))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Hero Success Section
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                // Success Blob
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF10b981).copy(alpha = 0.15f), Color.Transparent),
                                radius = 300f
                            ),
                            shape = CircleShape
                        )
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color(0xFF10b981).copy(alpha = 0.1f),
                        shape = CircleShape,
                        border = BorderStroke(8.dp, Color(0xFF10b981).copy(alpha = 0.05f)),
                        modifier = Modifier.size(96.dp).padding(bottom = 24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF10b981), modifier = Modifier.size(60.dp))
                        }
                    }
                    
                    Text("Exam Submitted", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Result will be published tomorrow.", style = MaterialTheme.typography.titleMedium, color = Color(0xFF464555))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("আপনার উত্তর জমা হয়েছে। ফলাফল আগামীকাল প্রকাশ করা হবে।", color = Color(0xFF464555).copy(alpha = 0.7f), fontSize = 16.sp, textAlign = TextAlign.Center)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Summary Bento Grid Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CONFIRMATION SUMMARY", fontSize = 12.sp, color = Color(0xFF712ae2), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mega Quiz 1", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                        }
                        Surface(
                            color = Color(0xFF1e00a9).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.Analytics, contentDescription = null, tint = Color(0xFF1e00a9))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Total
                        Surface(
                            color = Color(0xFFefebff),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total", fontSize = 12.sp, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$total", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                            }
                        }
                        
                        // Answered
                        Surface(
                            color = Color(0xFF10b981).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF10b981).copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Answered", fontSize = 12.sp, color = Color(0xFF10b981), fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$answered", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                            }
                        }
                        
                        // Unanswered
                        Surface(
                            color = Color(0xFFba1a1a).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFba1a1a).copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Unanswered", fontSize = 12.sp, color = Color(0xFFba1a1a), fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$unanswered", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Action Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { 
                        navController.navigate(HomeRoute) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e00a9))
                ) {
                    Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Back to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                
                Button(
                    onClick = { 
                        navController.navigate(MegaQuizRoutineRoute) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe9e6fa))
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color(0xFF1e00a9))
                    Spacer(Modifier.width(8.dp))
                    Text("View Routine", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Decorative Marquee Section
            val marqueeText = "OXFORD UNIVERSITY   CAMBRIDGE ACADEMY   MIT INSTITUTE   HARVARD GLOBAL   STANFORD PREP   "
            Text(
                text = marqueeText.repeat(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(iterations = Int.MAX_VALUE)
                    .padding(bottom = 24.dp),
                maxLines = 1,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF777587).copy(alpha = 0.2f)
            )
        }
    }
}
