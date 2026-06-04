package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFf5f2ff),
                modifier = Modifier.width(280.dp)
            ) {
                AdminSidebarContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                AdminTopBar(onMenuClick = { scope.launch { drawerState.open() } })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color(0xFFffddb8),
                    contentColor = Color(0xFF2a1700),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFfcf8ff))
                    .padding(paddingValues)
            ) {
                BoxWithConstraints {
                    if (maxWidth > 800.dp) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .width(280.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFFf5f2ff))
                                    .border(BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f)))
                            ) {
                                AdminSidebarContent()
                            }
                            AdminMainContent(modifier = Modifier.weight(1f))
                        }
                    } else {
                        AdminMainContent(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
fun AdminTopBar(onMenuClick: () -> Unit) {
    Surface(
        color = Color(0xFFfcf8ff).copy(alpha = 0.95f),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().zIndex(10f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoxWithConstraints {
                    if (maxWidth < 800.dp) { // Show hamburger only on small screens
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color(0xFF1e00a9))
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text("GK Elite", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
            }
            
            // Desktop Nav
            BoxWithConstraints {
                if (maxWidth > 800.dp) {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Text("Dashboard", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1e00a9))
                        Text("Moderation", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF464555))
                        Text("Analytics", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF464555))
                    }
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color(0xFF1e00a9))
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFe2dfff),
                    modifier = Modifier.size(40.dp),
                    border = BorderStroke(2.dp, Color(0xFF3525cd))
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("AD", fontWeight = FontWeight.Bold, color = Color(0xFF3323cc))
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSidebarContent() {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Spacer(Modifier.height(16.dp))
        Text("CONTENT CONTROL", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
        Spacer(Modifier.height(16.dp))
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFe2dfff)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Icon(Icons.Filled.Dashboard, contentDescription = null, tint = Color(0xFF3323cc))
                Spacer(Modifier.width(16.dp))
                Text("Overview", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF3323cc))
            }
        }
        Spacer(Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Icon(Icons.Filled.RateReview, contentDescription = null, tint = Color(0xFF464555))
            Spacer(Modifier.width(16.dp))
            Text("Submissions", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF464555))
            Spacer(Modifier.weight(1f))
            Surface(color = Color(0xFFffdad6), shape = RoundedCornerShape(999.dp)) {
                Text("12", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF93000a), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Icon(Icons.Filled.History, contentDescription = null, tint = Color(0xFF464555))
            Spacer(Modifier.width(16.dp))
            Text("Audit Logs", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF464555))
        }
        
        Spacer(Modifier.height(24.dp))
        Text("SYSTEM", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
        Spacer(Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFF464555))
            Spacer(Modifier.width(16.dp))
            Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF464555))
        }
    }
}

@Composable
fun AdminMainContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Stats Bento Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                // Pending MCQs
                Surface(
                    modifier = Modifier.weight(1f).height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f)),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFe2dfff), modifier = Modifier.size(48.dp)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(Icons.Filled.Quiz, contentDescription = null, tint = Color(0xFF1e00a9))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF10b981), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("+5 today", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF10b981))
                            }
                        }
                        Column {
                            Text("PENDING MCQS", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Text("08", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1e00a9))
                        }
                    }
                }
                
                // GK Snippets
                Surface(
                    modifier = Modifier.weight(1f).height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f)),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFeaddff), modifier = Modifier.size(48.dp)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(Icons.Filled.Description, contentDescription = null, tint = Color(0xFF712ae2))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null, tint = Color(0xFFba1a1a), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("-2 today", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFba1a1a))
                            }
                        }
                        Column {
                            Text("GK SNIPPETS", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Text("04", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF712ae2))
                        }
                    }
                }
                
                // Mega Quiz Timer Card
                BoxWithConstraints {
                    if (maxWidth > 1000.dp) { // Show on very wide screens in the same row, else it can wrap or we just use a fixed row and scroll
                        // Not using flow layout to avoid complex dependencies, just assuming wide enough or we do a vertical stack.
                    }
                }
            }
        }
        
        // Timer Card on its own row for better responsiveness
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(listOf(Color(0xFF3525cd), Color(0xFF712ae2), Color(0xFFffb95f))))
                        .padding(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("NEXT AUTOMATED PUBLISH", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.8f), letterSpacing = 1.sp)
                            Text("02:45:12", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text("Force Sync Now", color = Color(0xFF1e00a9), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Submissions Queue Header
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text("Pending Submissions Queue", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                Surface(color = Color(0xFFe9e6fa), shape = RoundedCornerShape(999.dp)) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Surface(color = Color.White, shape = RoundedCornerShape(999.dp), shadowElevation = 1.dp) {
                            Text("Card View", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1e00a9), modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                        }
                        Text("Compact", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF464555), modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                    }
                }
            }
        }

        // Fact Submission Card 1
        item {
            FactSubmissionCard()
        }

        // MCQ Submission Card 2
        item {
            MCQSubmissionCard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactSubmissionCard() {
    var title by remember { mutableStateOf("RBI projects 7% GDP growth for FY25") }
    var content by remember { mutableStateOf("The Reserve Bank of India (RBI) maintained its GDP growth projection for 2024-25 at 7 percent during the recent Monetary Policy Committee meeting, citing robust domestic demand and resilient industrial activity.") }
    var isApproved by remember { mutableStateOf(false) }
    
    if (isApproved) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f))
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Editorial Column
                Column(modifier = Modifier.weight(1f).padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                        Surface(color = Color(0xFF673f00), shape = RoundedCornerShape(999.dp)) {
                            Text("Economics", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFeda951), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF777587), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Submitted 2h ago by Editor Sam", fontSize = 12.sp, color = Color(0xFF777587))
                    }
                    
                    Text("FACT HEADLINE", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    Text("GK SNIPPET CONTENT", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                    )
                }
                
                // Live Preview Sidebar
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                        .border(BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.2f)))
                        .padding(24.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("LIVE PREVIEW", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Active", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF464555))
                            Spacer(Modifier.width(8.dp))
                            Surface(color = Color(0xFF3525cd), shape = RoundedCornerShape(999.dp), modifier = Modifier.width(32.dp).height(16.dp)) {
                                Box(modifier = Modifier.fillMaxSize().padding(2.dp), contentAlignment = Alignment.CenterEnd) {
                                    Box(modifier = Modifier.size(12.dp).background(Color.White, CircleShape))
                                }
                            }
                        }
                    }
                    
                    // Mock App Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFf8f9ff),
                        border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                                Surface(color = Color(0xFF1e00a9), shape = CircleShape, modifier = Modifier.size(24.dp)) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("GK UPDATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9), letterSpacing = 1.sp)
                            }
                            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28), lineHeight = 20.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(content, fontSize = 12.sp, color = Color(0xFF464555), maxLines = 2, overflow = TextOverflow.Ellipsis)
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFc7c4d8).copy(alpha = 0.5f))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Read more • 2 min", fontSize = 10.sp, color = Color(0xFF777587))
                                Icon(Icons.Filled.Bookmark, contentDescription = null, tint = Color(0xFF1e00a9), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
            
            // Action Footer
            Surface(
                color = Color(0xFFf5f2ff),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFba1a1a)),
                            border = BorderStroke(1.dp, Color(0xFFba1a1a))
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Reject")
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF777587)),
                            border = BorderStroke(1.dp, Color(0xFF777587))
                        ) {
                            Icon(Icons.Filled.HistoryEdu, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Request Changes")
                        }
                    }
                    Button(
                        onClick = { isApproved = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e00a9))
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Approve & Publish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MCQSubmissionCard() {
    var question by remember { mutableStateOf("Which space agency recently launched the Europa Clipper mission to study Jupiter's moon?") }
    var isApproved by remember { mutableStateOf(false) }
    
    if (isApproved) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f))
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Editorial Column
                Column(modifier = Modifier.weight(1f).padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                        Surface(color = Color(0xFF8b4bfc), shape = RoundedCornerShape(999.dp)) {
                            Text("Space & Tech", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFfffbff), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF777587), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Submitted 4h ago by Editor Mia", fontSize = 12.sp, color = Color(0xFF777587))
                    }
                    
                    Text("QUESTION", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("OPTION A (CORRECT)", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Spacer(Modifier.height(8.dp))
                            Surface(color = Color(0xFF10b981).copy(alpha = 0.1f), border = BorderStroke(1.dp, Color(0xFF10b981)), shape = RoundedCornerShape(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF10b981))
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedTextField(
                                        value = "NASA",
                                        onValueChange = { },
                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("OPTION B", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = "ESA",
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("OPTION C", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = "ISRO",
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("OPTION D", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = "JAXA",
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFefebff), unfocusedContainerColor = Color(0xFFefebff), unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                            )
                        }
                    }
                }
                
                // Attached Media Sidebar
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                        .border(BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.2f)))
                        .padding(24.dp)
                ) {
                    Text("ATTACHED MEDIA", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587), letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 16.dp))
                    
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f)
                    ) {
                        AsyncImage(
                            model = "file:///C:/Users/User/.gemini/antigravity-ide/brain/020f2663-f2ea-48de-a844-6540365a77c2/europa_clipper_1780515667412.png",
                            contentDescription = "Europa Clipper",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Text("Reference: NASA Press Release Oct 2024", fontSize = 10.sp, color = Color(0xFF777587), modifier = Modifier.padding(top = 16.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
            
            // Action Footer
            Surface(
                color = Color(0xFFf5f2ff),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFba1a1a)),
                            border = BorderStroke(1.dp, Color(0xFFba1a1a))
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Reject")
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF777587)),
                            border = BorderStroke(1.dp, Color(0xFF777587))
                        ) {
                            Icon(Icons.Filled.HistoryEdu, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Request Changes")
                        }
                    }
                    Button(
                        onClick = { isApproved = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e00a9))
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Approve & Publish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
