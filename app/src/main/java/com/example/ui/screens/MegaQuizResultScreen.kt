package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.MegaQuizLiveRoute
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizResultScreen(score: Double, total: Int, right: Int, wrong: Int, navController: NavController) {
    val unanswered = total - (right + wrong)
    val accuracy = if (right + wrong > 0) ((right.toDouble() / (right + wrong)) * 100).roundToInt() else 0
    val rank = "45th" // Mock rank for now

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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0xFF3525cd),
                        ) {
                            Text(
                                "00:45:00", 
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White, 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 14.sp
                            )
                        }
                        Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 24.dp)) {
                Text("Quiz Result", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                Spacer(Modifier.height(8.dp))
                Text("Congratulations on completing Mega Quiz 1!", fontSize = 16.sp, color = Color(0xFF464555))
            }

            // Main Score Card
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF3525cd), Color(0xFF712ae2), Color(0xFFffb95f))
                            )
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("FINAL SCORE", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color.White.copy(alpha = 0.8f))
                        Text("%.2f".format(score), fontSize = 64.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Rank", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                Text(rank, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.White.copy(alpha = 0.2f)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Accuracy", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                Text("$accuracy%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color(0xFFffb95f))
                            Spacer(Modifier.width(8.dp))
                            Text("Good! Keep practicing.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Detailed Stats Grid
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Correct
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Surface(color = Color(0xFF10b981).copy(alpha = 0.1f), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF10b981))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Correct", fontSize = 14.sp, color = Color(0xFF464555))
                        Text("$right", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                    }
                }
                
                // Wrong
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Surface(color = Color(0xFFba1a1a).copy(alpha = 0.1f), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.Cancel, contentDescription = null, tint = Color(0xFFba1a1a))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Wrong", fontSize = 14.sp, color = Color(0xFF464555))
                        Text("$wrong", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                    }
                }
                
                // Unanswered
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Surface(color = Color(0xFFe9e6fa), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.Block, contentDescription = null, tint = Color(0xFF777587))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Unanswered", fontSize = 14.sp, color = Color(0xFF464555))
                        Text("$unanswered", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                    }
                }
            }

            // Score Formula Explanation
            Surface(
                color = Color(0xFFf5f2ff),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFc7c4d8)), // Should be dashed but Compose doesn't support dashed borders easily on rounded corners without custom drawing. Using solid.
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                        Icon(Icons.Filled.Calculate, contentDescription = null, tint = Color(0xFF1e00a9))
                        Spacer(Modifier.width(12.dp))
                        Text("Scoring Logic", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                    }
                    Text(
                        "The final score is calculated based on the standardized competitive exam marking scheme:",
                        fontSize = 16.sp, color = Color(0xFF464555)
                    )
                    Spacer(Modifier.height(16.dp))
                    Surface(color = Color.White, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Final Score = Correct - (Wrong x 0.25)",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF25005a)
                        )
                    }
                }
            }

            // Action CTAs
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /* TODO View Answers */ },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFffb95f)),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Icon(Icons.Filled.Visibility, contentDescription = null, tint = Color(0xFF653e00))
                    Spacer(Modifier.width(8.dp))
                    Text("View Answers", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF653e00))
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { navController.navigate(HomeRoute) { popUpTo(0) } },
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFeaddff)),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF25005a))
                        Spacer(Modifier.width(8.dp))
                        Text("Back to Home", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25005a))
                    }
                    
                    Button(
                        onClick = { /* Default action, practice more */ },
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFeaddff)),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color(0xFF25005a))
                        Spacer(Modifier.width(8.dp))
                        Text("Practice More", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25005a))
                    }
                }
            }
            
            // Visual Asset (Course Suggestion)
            Surface(
                color = Color(0xFFe3e0f4),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("NEXT STEP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                    Spacer(Modifier.height(8.dp))
                    Text("Personalized Study Plan", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Based on your performance, we recommend focusing on 'Advanced Calculus' and 'Applied Physics'.",
                        fontSize = 16.sp, color = Color(0xFF464555)
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Explore Study Guide", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.TrendingFlat, contentDescription = null, tint = Color(0xFF1e00a9))
                    }
                    Spacer(Modifier.height(24.dp))
                    
                    // The generated image
                    AsyncImage(
                        model = "file:///C:/Users/User/.gemini/antigravity-ide/brain/020f2663-f2ea-48de-a844-6540365a77c2/study_plan_banner_1780514386867.png",
                        contentDescription = "Study Plan Banner",
                        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(Modifier.height(48.dp))
        }
    }
}
