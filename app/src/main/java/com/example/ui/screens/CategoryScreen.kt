package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKMainTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.TopicListRoute

val PrimaryColor = Color(0xFF3525cd)
val PrimaryContainerColor = Color(0xFF4f46e5)
val SecondaryColor = Color(0xFF712ae2)
val BackgroundColor = Color(0xFFf8f9ff)
val OnSurfaceColor = Color(0xFF0b1c30)
val OnSurfaceVariantColor = Color(0xFF464555)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(category: String, title: String, viewModel: GKViewModel, navController: NavController) {
    val bdTopics by viewModel.bdTopics.collectAsStateWithLifecycle(initialValue = emptyList())
    val intTopics by viewModel.intTopics.collectAsStateWithLifecycle(initialValue = emptyList())
    val topics = if (category == "BANGLADESH") bdTopics else intTopics
    
    val subtitle = if (category == "BANGLADESH") "বাংলাদেশ বিষয়াবলি প্রস্তুতি" else "আন্তর্জাতিক বিষয়াবলি প্রস্তুতি"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Admission GK", 
                            fontWeight = FontWeight.Bold, 
                            color = PrimaryColor,
                            fontSize = 20.sp
                        ) 
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryColor)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = PrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderSection(title, subtitle, topics.size)
            }
            
            itemsIndexed(topics) { index, topic ->
                TopicCardItem(
                    topic = topic, 
                    index = index,
                    category = category,
                    onClick = { navController.navigate(TopicListRoute(topic.id, topic.title)) }
                )
            }
        }
    }
}

@Composable
fun HeaderSection(title: String, subtitle: String, topicCount: Int) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                Surface(
                    color = PrimaryContainerColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(percent = 50),
                    border = BorderStroke(1.dp, PrimaryContainerColor.copy(alpha = 0.2f)),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.AutoAwesome, 
                            contentDescription = null, 
                            tint = PrimaryContainerColor,
                            modifier = Modifier.size(16.dp).padding(end = 4.dp)
                        )
                        Text(
                            "$topicCount Topics", 
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryContainerColor
                        )
                    }
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariantColor
                )
            }
            
            // Progress / Rank Card
            Surface(
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shadowElevation = 2.dp,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("OVERALL PROGRESS", fontSize = 10.sp, color = OnSurfaceVariantColor, letterSpacing = 1.sp)
                        Text("45%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryColor)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color.Gray.copy(alpha = 0.2f))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("RANK", fontSize = 10.sp, color = OnSurfaceVariantColor, letterSpacing = 1.sp)
                        Text("#1,204", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SecondaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCardItem(topic: GKMainTopicEntity, index: Int, category: String, onClick: () -> Unit) {
    if (index == 0) {
        FeaturedTopicCard(topic, category, onClick)
    } else {
        StandardTopicCard(topic, index, category, onClick)
    }
}

@Composable
fun FeaturedTopicCard(topic: GKMainTopicEntity, category: String, onClick: () -> Unit) {
    val subtitle = if (category == "BANGLADESH") "৮টি উপ-বিষয় • ভূ-প্রকৃতি ও মানচিত্র" else "৭টি মহাদেশ, মহাসাগর ও সীমানা"
    val icon = if (category == "BANGLADESH") Icons.Filled.Map else Icons.Filled.Public
    val bgColor = if (category == "BANGLADESH") Color(0xFF10b981).copy(alpha = 0.1f) else Color(0xFF3b82f6).copy(alpha = 0.1f)
    val tintColor = if (category == "BANGLADESH") Color(0xFF059669) else Color(0xFF2563eb)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF4f46e5), Color(0xFF712ae2))))
                .padding(1.dp) // creates border effect if inner background is differently shaped or similar
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(bgColor, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(28.dp))
                        }
                        
                        Surface(
                            color = PrimaryColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(percent = 50)
                        ) {
                            Text(
                                "Featured", 
                                color = PrimaryColor, 
                                fontSize = 12.sp, 
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceColor
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantColor,
                        modifier = Modifier.padding(bottom = 24.dp, top = 4.dp)
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Progress", fontSize = 12.sp, color = OnSurfaceVariantColor)
                        Text("80%", fontSize = 12.sp, color = PrimaryColor, fontWeight = FontWeight.Medium)
                    }
                    
                    LinearProgressIndicator(
                        progress = { 0.80f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 24.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(percent = 50)),
                        color = Color(0xFF10b981),
                        trackColor = Color(0xFFdce9ff)
                    )
                    
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text("START LEARNING", letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StandardTopicCard(topic: GKMainTopicEntity, index: Int, category: String, onClick: () -> Unit) {
    val (icon, tintColor, bgColor, mustRead) = getTopicIconAndColor(index, category)
    val mockProgress = listOf(0.80f, 0.35f, 0.0f, 0.15f, 0.62f, 0.10f)[index % 6]
    
    val mockSubtitleListBD = listOf(
        "৮টি উপ-বিষয় • ভূ-প্রকৃতি ও মানচিত্র",
        "১২টি উপ-বিষয় • প্রাচীন থেকে আধুনিক",
        "১৫টি উপ-বিষয় • ১৯৭১ ও স্বাধীনতা",
        "৬টি উপ-বিষয় • উৎস ও মোহনা",
        "১০টি উপ-বিষয় • বাজেট ও অর্থনৈতিক সমীক্ষা",
        "৯টি উপ-বিষয় • অনুচ্ছেদ ও সংশোধনী"
    )
    val mockSubtitleListInt = listOf(
        "৭টি মহাদেশ, মহাসাগর ও সীমানা",
        "প্রাচীন সভ্যতা থেকে আধুনিক যুগ",
        "জাতিসংঘ, ইইউ, সার্ক ও অন্যান্য",
        "বৈশ্বিক ভূ-রাজনীতি ও সাম্প্রতিক সংবাদ",
        "গুরুত্বপূর্ণ চুক্তি ও আন্তর্জাতিক আইন",
        "বিবিধ আন্তর্জাতিক বিষয়াবলি"
    )
    
    val mockSubtitleList = if (category == "BANGLADESH") mockSubtitleListBD else mockSubtitleListInt
    val mockSubtitle = mockSubtitleList[index % mockSubtitleList.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(bgColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(28.dp))
                }
                if (mustRead) {
                    Surface(
                        color = Color(0xFFba1a1a).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(percent = 50)
                    ) {
                        Text(
                            "MUST READ",
                            color = Color(0xFFba1a1a),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceColor
            )
            Text(
                text = mockSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariantColor,
                modifier = Modifier.padding(bottom = 24.dp, top = 4.dp)
            )
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Progress", fontSize = 12.sp, color = OnSurfaceVariantColor)
                Text("${(mockProgress * 100).toInt()}%", fontSize = 12.sp, color = PrimaryColor, fontWeight = FontWeight.Medium)
            }
            
            LinearProgressIndicator(
                progress = { mockProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 24.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(percent = 50)),
                color = PrimaryColor,
                trackColor = Color(0xFFdce9ff)
            )
            
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, PrimaryColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryColor)
            ) {
                if (mockProgress == 0f) {
                    Text("START TOPIC", letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                } else {
                    Text("CONTINUE", letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

fun getTopicIconAndColor(index: Int, category: String): data_class_hack {
    if (category == "INTERNATIONAL") {
        return when (index % 6) {
            0 -> data_class_hack(Icons.Filled.Public, Color(0xFF2563eb), Color(0xFF3b82f6).copy(alpha = 0.1f), false) 
            1 -> data_class_hack(Icons.Filled.History, PrimaryColor, PrimaryColor.copy(alpha = 0.1f), false) 
            2 -> data_class_hack(Icons.Filled.Groups, SecondaryColor, SecondaryColor.copy(alpha = 0.1f), true) // Must read
            3 -> data_class_hack(Icons.AutoMirrored.Filled.Article, Color(0xFF2563eb), Color(0xFF3b82f6).copy(alpha = 0.1f), false) 
            4 -> data_class_hack(Icons.Filled.AccountBalance, Color(0xFF4f46e5), Color(0xFF6366f1).copy(alpha = 0.1f), false) 
            5 -> data_class_hack(Icons.Filled.Gavel, Color(0xFF9333ea), Color(0xFFa855f7).copy(alpha = 0.1f), false)
            else -> data_class_hack(Icons.Filled.Public, Color(0xFF2563eb), Color(0xFF3b82f6).copy(alpha = 0.1f), false)
        }
    } else {
        return when (index % 6) {
            0 -> data_class_hack(Icons.Filled.Map, Color(0xFF059669), Color(0xFF10b981).copy(alpha = 0.1f), false) 
            1 -> data_class_hack(Icons.Filled.Edit, PrimaryColor, PrimaryColor.copy(alpha = 0.1f), false) 
            2 -> data_class_hack(Icons.Filled.MilitaryTech, SecondaryColor, SecondaryColor.copy(alpha = 0.1f), false) 
            3 -> data_class_hack(Icons.Filled.Waves, Color(0xFF2563eb), Color(0xFF3b82f6).copy(alpha = 0.1f), false) 
            4 -> data_class_hack(Icons.Filled.Payments, Color(0xFFea580c), Color(0xFFf97316).copy(alpha = 0.1f), false) 
            5 -> data_class_hack(Icons.Filled.Gavel, Color(0xFF9333ea), Color(0xFFa855f7).copy(alpha = 0.1f), false) 
            else -> data_class_hack(Icons.Filled.Map, Color(0xFF059669), Color(0xFF10b981).copy(alpha = 0.1f), false)
        }
    }
}

data class data_class_hack(val icon: ImageVector, val tintColor: Color, val bgColor: Color, val mustRead: Boolean)
