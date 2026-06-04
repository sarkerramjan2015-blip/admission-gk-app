import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\QuizScreens.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

package_and_imports = """package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import com.example.ui.GKViewModel
import com.example.ui.navigation.MCQQuizRoute
import com.example.ui.navigation.MegaQuizLiveRoute
"""

mega_quiz_intro_screen = """
val SnMegaQuizGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF3525cd), Color(0xFF712ae2), Color(0xFFffb95f))
)
val SnSuccessEmerald = Color(0xFF10b981)
val SnErrorRed = Color(0xFFba1a1a)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaQuizIntroScreen(examId: String, viewModel: GKViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Rules", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("00:20:00", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SnMegaQuizGradient)
            )
            
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(32.dp))
                
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Mega Quiz Rules", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = SnPrimaryColor)
                        Spacer(Modifier.height(16.dp))
                        
                        RuleItem(Icons.Filled.List, "30 Multiple Choice Questions")
                        RuleItem(Icons.Filled.Timer, "20 Minutes Time Limit")
                        RuleItem(Icons.Filled.CheckCircle, "1 Mark for each correct answer")
                        RuleItem(Icons.Filled.Cancel, "0.25 Negative mark for wrong answer")
                        
                        Spacer(Modifier.height(32.dp))
                        
                        var agreed by remember { mutableStateOf(false) }
                        
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { agreed = !agreed }) {
                            Checkbox(checked = agreed, onCheckedChange = { agreed = it })
                            Text("I agree to the rules of the quiz")
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Button(
                            onClick = { 
                                navController.navigate(MegaQuizLiveRoute(examId))
                            },
                            enabled = agreed,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SnPrimaryColor)
                        ) {
                            Text("Start Exam", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RuleItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = SnSecondaryColor, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 16.sp, color = SnOnSurfaceVariantColor)
    }
}
"""

content = package_and_imports + "\n" + content + "\n" + mega_quiz_intro_screen

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
