package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.GKViewModel
import com.example.ui.navigation.QuestionBankRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankScreen(uniId: String?, year: Int?, viewModel: GKViewModel, navController: NavController) {
    if (uniId != null && year != null) {
        val questions by remember(uniId, year) { viewModel.getUniversityQuestions(uniId, year) }.collectAsStateWithLifecycle(initialValue = emptyList())
        ExamPaperLayout(
            uniId = uniId,
            year = year,
            questions = questions,
            onFinish = { navController.popBackStack() }
        )
    } else {
        UniversitySelectionLayout(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversitySelectionLayout(navController: NavController) {
    var selectedUni by remember { mutableStateOf<String?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFFf8f9ff).copy(alpha = 0.8f),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4f46e5),
                            modifier = Modifier.size(40.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://lh3.googleusercontent.com/aida-public/AB6AXuCd7AMXN0LE1pO-0xFw4ztpz8sFSdVsT0XvaDwoD476qlK8JdEttCtsEAVocLGsgwm8aATRXoOMjPOy12eeF1GIQcGE9e3zFkWY5Jnj8_22PNBRHuj1YAXSR8PH5VpfTNOfexhQQkBRhvLcCxhXoH9VmdqczXAKtB3WcvwDQumjdsOad4Ur5lJoXH5GOvU2P0v9FiPftEDA16gX95JZuEUcu3h2Tt1okIwvbQt167LXKBHvkOEGcsi6h9msHKTXrCFtXhPMG1GLHPgy")
                                    .crossfade(true).build(),
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Admission GK Hub", style = MaterialTheme.typography.titleLarge, color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color(0xFF3525cd))
                    }
                }
            }
        },
        containerColor = Color(0xFFf8f9ff)
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Spacer(Modifier.height(32.dp))
            
            // Header & Search Section
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Question Bank", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                Spacer(Modifier.height(8.dp))
                Text("Access previous years' admission questions with detailed solutions.", color = Color(0xFF464555), fontSize = 16.sp)
                
                Spacer(Modifier.height(24.dp))
                
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search University or Unit...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF777587)) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
            
            Spacer(Modifier.height(32.dp))
            
            // Marquee
            val infiniteTransition = rememberInfiniteTransition(label = "marquee")
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "offset"
            )
            
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                // Repeating the content to simulate marquee
                repeat(4) {
                    Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                        Text("DHAKA UNIVERSITY", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                        Text("RAJSHAHI UNIVERSITY", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                        Text("CHITTAGONG UNIVERSITY", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                        Text("JAHANGIRNAGAR UNIVERSITY", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                        Text("GST CLUSTER", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp, color = Color(0xFF777587).copy(alpha = 0.5f))
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Bento Grid
            val unis = defaultUnis
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(unis) { uni ->
                    Card(
                        modifier = Modifier.height(160.dp).clickable {
                            selectedUni = uni.id
                            coroutineScope.launch { bottomSheetState.show() }
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = uni.iconBgColor,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(uni.icon, contentDescription = null, tint = uni.iconColor, modifier = Modifier.size(32.dp))
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Surface(
                                color = uni.badgeBgColor,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(uni.badge, color = uni.badgeTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(uni.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                        }
                    }
                }
            }
        }
        
        // Year Selection Bottom Sheet
        if (selectedUni != null) {
            val selectedUniData = defaultUnis.find { it.id == selectedUni }
            
            ModalBottomSheet(
                onDismissRequest = { selectedUni = null },
                sheetState = bottomSheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Select Year", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
                            Text("Choose an academic year to start", color = Color(0xFF464555), fontSize = 16.sp)
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion { selectedUni = null }
                            },
                            modifier = Modifier.background(Color(0xFFdce9ff), CircleShape)
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color(0xFF0b1c30))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFc7c4d8).copy(alpha = 0.5f))
                    
                    val years = listOf(2023, 2022, 2021, 2020, 2019, 2018)
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        items(years) { year ->
                            Card(
                                modifier = Modifier.height(72.dp).clickable {
                                    coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                        val uId = selectedUni
                                        selectedUni = null
                                        navController.navigate(QuestionBankRoute(uId, year))
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, Color(0xFFc7c4d8))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val nextYear = (year + 1).toString().substring(2)
                                    Text("$year-$nextYear", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF3525cd))
                                }
                            }
                        }
                    }
                    
                    Surface(color = Color(0xFFe5eeff), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3525cd))
                        ) {
                            Text("VIEW ARCHIVE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}

data class UniCardData(
    val id: String,
    val title: String,
    val badge: String,
    val iconColor: Color,
    val icon: ImageVector,
    val iconBgColor: Color,
    val badgeTextColor: Color = iconColor,
    val badgeBgColor: Color = iconBgColor
)

val defaultUnis = listOf(
    UniCardData("du", "DU", "GK", Color(0xFF3525cd), Icons.Filled.School, Color(0xFF3525cd).copy(alpha = 0.1f)),
    UniCardData("ju", "JU", "GK ONLY", Color(0xFF712ae2), Icons.Filled.AutoStories, Color(0xFF712ae2).copy(alpha = 0.1f)),
    UniCardData("ru", "RU", "GK ONLY", Color(0xFF684000), Icons.Filled.HistoryEdu, Color(0xFF684000).copy(alpha = 0.1f)),
    UniCardData("cu", "CU", "GK ONLY", Color(0xFF059669), Icons.Filled.LocationCity, Color(0xFF059669).copy(alpha = 0.1f)),
    UniCardData("gst", "GST", "GK", Color.White, Icons.Filled.Hub, Color(0xFF4f46e5), Color(0xFF4f46e5), Color(0xFF4f46e5).copy(alpha = 0.1f))
)
