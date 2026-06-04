package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.RecentGKEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: GKViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val bdRecent by viewModel.recentGKBD.collectAsStateWithLifecycle()
    val intRecent by viewModel.recentGKInt.collectAsStateWithLifecycle()

    val primary = Color(0xFF3525cd)
    val primaryContainer = Color(0xFF4f46e5)
    val onPrimaryContainer = Color(0xFFdad7ff)
    val secondary = Color(0xFF712ae2)
    val onSurfaceVariant = Color(0xFF464555)
    val errorColor = Color(0xFFba1a1a)
    val appBackground = Color(0xFFf7fafc)

    Scaffold(
        containerColor = appBackground,
        bottomBar = { BottomNavigationBar(navController, currentRoute = "Home") }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // Header & Search Bar Full Bleed
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(Brush.linearGradient(listOf(BrandGradientStart, BrandGradientEnd)))
                        .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 32.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Admission GK", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Ready for today's practice?", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha=0.8f))
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color.White.copy(alpha = 0.2f),
                            ) {
                                Text("🔥 7 Days", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color.White,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Search topics, MCQ or questions", color = TextMuted) },
                                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = BrandGradientStart) },
                                shape = RoundedCornerShape(999.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // GK Category Cards
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        title = "BD GK",
                        subtitle = "(বাংলাদেশ বিষয়াবলি)",
                        buttonText = "Start Learning",
                        topicsCount = "12 Topics",
                        gradient = Brush.linearGradient(listOf(BdGkStart, BdGkEnd)),
                        onClick = { navController.navigate(CategoryRoute("BANGLADESH", "Bangladesh GK")) }
                    )
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        title = "INT. GK",
                        subtitle = "(আন্তর্জাতিক বিষয়াবলি)",
                        buttonText = "Explore",
                        topicsCount = "10 Topics",
                        gradient = Brush.linearGradient(listOf(IntGkStart, IntGkEnd)),
                        onClick = { navController.navigate(CategoryRoute("INTERNATIONAL", "International GK")) }
                    )
                }
            }

            // Mega Quiz Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    MegaQuizCard(
                        onClick = { navController.navigate(MegaQuizRoutineRoute) },
                        primaryContainer = BrandGradientStart,
                        errorColor = WrongColor
                    )
                }
            }

            // Recent GK Preview
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) { RecentGkSection(title = "Recent GK (BD)", items = bdRecent, gradient = Brush.linearGradient(listOf(RecentBKGkStart, RecentBKGkEnd)), onClickAll = { navController.navigate(RecentGKRoute) }) }
            }
            
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) { RecentGkSection(title = "Recent GK (International)", items = intRecent, gradient = Brush.linearGradient(listOf(RecentIntStart, RecentIntEnd)), onClickAll = { navController.navigate(RecentGKRoute) }) }
            }

            // Question Bank
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) { QuestionBankCard { navController.navigate(QuestionBankRoute(null, null)) } }
            }

            // Dashboard Summary
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) { DashboardSummaryMenu() }
            }
        }
    }
}

@Composable
fun CategoryCard(modifier: Modifier = Modifier, title: String, subtitle: String, buttonText: String, topicsCount: String, gradient: Brush, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = LocalIndication.current) { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha=0.3f))
                ) {
                    Text(topicsCount, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
                
                Column {
                    Text(title, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha=0.8f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF3525cd)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(buttonText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MegaQuizCard(onClick: () -> Unit, primaryContainer: Color, errorColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryContainer)
                .padding(24.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Surface(
                        color = errorColor,
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(Color.White, RoundedCornerShape(999.dp)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                }
                Text("Mega Quiz Exam", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Compete with thousands of students across the country.", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha=0.8f))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TimerBox("02", "Days")
                    Text(":", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    TimerBox("04", "Hrs")
                    Text(":", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    TimerBox("30", "Min")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF3525cd)),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Join Now", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White.copy(alpha=0.3f)),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("View Routine", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    Surface(
        color = Color.White.copy(alpha=0.9f),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        shadowElevation = 24.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(icon = Icons.Filled.Home, label = "Home", isSelected = currentRoute == "Home", onClick = { if (currentRoute != "Home") navController.navigate(HomeRoute) { popUpTo(HomeRoute) { inclusive = true } } })
            NavigationBarItem(icon = Icons.AutoMirrored.Filled.List, label = "Quiz", isSelected = currentRoute == "Quiz" || currentRoute == "Exams", onClick = { if (currentRoute != "Quiz" && currentRoute != "Exams") navController.navigate(MegaQuizRoutineRoute) })
            NavigationBarItem(icon = Icons.Filled.DateRange, label = "Archive", isSelected = currentRoute == "Archive", onClick = { if (currentRoute != "Archive") navController.navigate(QuestionBankRoute()) })
            NavigationBarItem(icon = Icons.Filled.Person, label = "Profile", isSelected = currentRoute == "Profile", onClick = { if (currentRoute != "Profile") navController.navigate(ProfileRoute) })
        }
    }
}

@Composable
fun NavigationBarItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val primary = Color(0xFF3525cd)
    val contentColor = if (isSelected) primary else Color(0xFF464555)
    
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .background(if (isSelected) primary.copy(alpha=0.1f) else Color.Transparent, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = contentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = contentColor, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun TimerBox(value: String, label: String) {
    Surface(
        color = Color.White.copy(alpha=0.1f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha=0.1f)),
        modifier = Modifier.size(64.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha=0.6f))
        }
    }
}

@Composable
fun QuestionBankCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(
                    color = Color(0xFFebeef0),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF3525cd), modifier = Modifier.padding(12.dp))
                }
                Column {
                    Text("University Question Bank", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Last 20 years questions with detailed solutions.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            val tags = listOf("DU Admission", "JU Unit B/C", "RU Admission", "CU Admission", "GST Combined")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(tags) { tag ->
                    Surface(
                        color = Color(0xFFf1f4f6),
                        shape = RoundedCornerShape(999.dp),
                        border = BorderStroke(1.dp, Color(0xFF3525cd).copy(alpha=0.1f))
                    ) {
                        Text(tag, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelMedium, color = Color(0xFF3525cd))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentGkSection(title: String, items: List<RecentGKEntity>, gradient: Brush, onClickAll: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            TextButton(onClick = onClickAll, contentPadding = PaddingValues(0.dp)) {
                Text("See All", color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if(items.isEmpty()) {
             Text("No recent info available.", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        } else {
             LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                 items(items.take(3)) { item ->
                     Card(
                         modifier = Modifier.width(280.dp),
                         colors = CardDefaults.cardColors(containerColor = Color.White),
                         shape = RoundedCornerShape(24.dp),
                         border = BorderStroke(1.dp, BorderColor),
                         elevation = CardDefaults.cardElevation(8.dp)
                     ) {
                         Column(modifier = Modifier.padding(24.dp)) {
                             Text(item.topicTitle, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                             Spacer(modifier = Modifier.height(8.dp))
                             Text(item.specialTopicNote, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, maxLines = 2)
                             Spacer(modifier = Modifier.height(24.dp))
                             Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { /* action */ }) {
                                 Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF3525cd), modifier = Modifier.size(20.dp))
                                 Spacer(modifier = Modifier.width(8.dp))
                                 Text("Add Info", style = MaterialTheme.typography.labelLarge, color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
                             }
                         }
                     }
                 }
             }
        }
    }
}

@Composable
fun DashboardSummaryMenu() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("Today's Progress", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Your learning journey for today", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                }
                Surface(
                    color = Color(0xFF3525cd).copy(alpha = 0.05f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text("View Dashboard", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Color(0xFF3525cd), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f).background(Color(0xFFf7fafc), RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text("25", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
                    Text("Practiced MCQ", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Column(modifier = Modifier.weight(1f).background(Color(0xFFf7fafc), RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text("18", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF00505f), fontWeight = FontWeight.Bold)
                    Text("Correct", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Column(modifier = Modifier.weight(1f).background(Color(0xFFf7fafc), RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text("07", style = MaterialTheme.typography.headlineMedium, color = Color(0xFFba1a1a), fontWeight = FontWeight.Bold)
                    Text("Wrong", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFebeef0), RoundedCornerShape(999.dp))) {
                Box(modifier = Modifier.fillMaxWidth(0.72f).height(8.dp).background(Brush.horizontalGradient(listOf(Color(0xFF4cd7f6), Color(0xFF712ae2))), RoundedCornerShape(999.dp)))
            }
        }
    }
}
