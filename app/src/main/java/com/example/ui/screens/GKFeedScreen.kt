package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ui.GKViewModel
import com.example.ui.navigation.HomeRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GKFeedScreen(viewModel: GKViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFFfcf8ff).copy(alpha = 0.95f),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().zIndex(10f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFe2dfff),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("GK", fontWeight = FontWeight.ExtraBold, color = Color(0xFF1e00a9))
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("GK Elite", style = MaterialTheme.typography.titleLarge, color = Color(0xFF1e00a9), fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color(0xFF1e00a9))
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFe2dfff),
                            modifier = Modifier.size(40.dp),
                            border = BorderStroke(1.dp, Color(0xFF1e00a9).copy(alpha = 0.1f))
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color(0xFF0f0069))
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, "Home")
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFfcf8ff))
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = padding.calculateTopPadding() + 24.dp, bottom = padding.calculateBottomPadding() + 48.dp, start = 20.dp, end = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Section
                item {
                    Column(modifier = Modifier.padding(bottom = 24.dp)) {
                        Text("GK Feed", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                        Text("Your daily essential current affairs capsule.", fontSize = 16.sp, color = Color(0xFF464555), modifier = Modifier.padding(top = 4.dp))
                        
                        Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFFe9e6fa),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Icon(Icons.Filled.FilterList, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF464555))
                                    Spacer(Modifier.width(8.dp))
                                    Text("All Categories", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF464555))
                                }
                            }
                            
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFF1e00a9),
                                shadowElevation = 4.dp,
                                modifier = Modifier.height(40.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Recommended", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
                                }
                            }
                        }
                    }
                }

                // Timeline container starts here
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // The vertical line for timeline
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .offset(x = 10.dp) // align with center of the dot (24.dp width dot, center is 12, offset = 12-1=11)
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(Color(0xFFc7c4d8).copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.height(1000.dp)) // ensure line goes all the way down, wait, LazyColumn item height is intrinsic
                            }
                        }
                        
                        // Let's draw timeline using nested layout or just absolute offset for dots
                        // Actually, doing a line using drawBehind on the Column is better
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 2.dp.toPx()
                                    val startX = 12.dp.toPx()
                                    drawLine(
                                        color = Color(0xFFc7c4d8).copy(alpha = 0.3f),
                                        start = Offset(startX, 24.dp.toPx()),
                                        end = Offset(startX, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                }
                        ) {
                            // Date Group: Today
                            DateGroupHeader(
                                dateText = "Today, 24 May 2024",
                                isToday = true,
                                dotColor = Color(0xFF3525cd),
                                showNewBadge = true
                            )
                            
                            // Feed Snippet 1
                            FeedCard(
                                category = "International",
                                categoryColor = Color(0xFF673f00),
                                categoryBg = Color(0xFF673f00).copy(alpha = 0.1f),
                                time = "10:45 AM",
                                title = "বিশ্বের প্রথম এআই চালিত যুদ্ধবিমান ‘ভিস্তা’ সফলভাবে আকাশে উড়ল",
                                description = "মার্কিন বিমান বাহিনী প্রথমবার কৃত্রিম বুদ্ধিমত্তা সম্পন্ন এক্স-৬২এ ভিস্তা (VISTA) বিমানের সফল পরীক্ষা সম্পন্ন করেছে। এটি ভবিষ্যতে আকাশযুদ্ধের গতিপথ বদলে দেবে বলে ধারণা করা হচ্ছে।",
                                isMustRead = true,
                                isSaved = false
                            )
                            
                            // Feed Snippet 2
                            FeedCard(
                                category = "Bangladesh",
                                categoryColor = Color(0xFF8b4bfc),
                                categoryBg = Color(0xFF8b4bfc).copy(alpha = 0.1f),
                                time = "08:20 AM",
                                title = "পদ্মা সেতুর রেলপথ দিয়ে নিয়মিত পণ্যবাহী ট্রেন চলাচল শুরু",
                                description = "বাণিজ্যিকভাবে পদ্মা সেতুর ওপর দিয়ে নিয়মিত পণ্য পরিবহন শুরু হয়েছে। এর ফলে দেশের দক্ষিণাঞ্চলের সঙ্গে ঢাকার যোগাযোগ ব্যবস্থা আরও গতিশীল হবে এবং ব্যবসা-বাণিজ্যে ইতিবাচক প্রভাব পড়বে।",
                                isMustRead = false,
                                isSaved = false
                            )
                            
                            Spacer(Modifier.height(24.dp))
                            
                            // Date Group: Yesterday
                            DateGroupHeader(
                                dateText = "Yesterday, 23 May 2024",
                                isToday = false,
                                dotColor = Color(0xFFc7c4d8),
                                showNewBadge = false
                            )
                            
                            // Feed Snippet 3
                            FeedCard(
                                category = "Economy",
                                categoryColor = Color(0xFF653e00),
                                categoryBg = Color(0xFFffb95f).copy(alpha = 0.2f),
                                time = "Yesterday",
                                title = "বৈশ্বিক জিডিপিতে ভারতের অবস্থান পঞ্চম থেকে চতুর্থ হবার পথে",
                                description = "আইএমএফ-এর সাম্প্রতিক প্রতিবেদন অনুযায়ী, ভারত বিশ্বের দ্রুততম ক্রমবর্ধমান অর্থনীতি হিসেবে নিজেকে প্রতিষ্ঠিত করছে এবং শীঘ্রই জার্মানিকে অতিক্রম করে চতুর্থ স্থানে উঠে আসবে।",
                                isMustRead = false,
                                isSaved = true
                            )
                        }
                    }
                }
                
                // Load More Button
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            border = BorderStroke(1.dp, Color(0xFF1e00a9)),
                            color = Color.Transparent,
                            modifier = Modifier.height(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 32.dp)) {
                                Text("Load Previous Facts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateGroupHeader(dateText: String, isToday: Boolean, dotColor: Color, showNewBadge: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dot
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(dotColor, CircleShape)
                .padding(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFfcf8ff), CircleShape))
            Box(modifier = Modifier.fillMaxSize().background(dotColor, CircleShape))
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = dateText, 
            fontSize = 20.sp, 
            fontWeight = FontWeight.SemiBold, 
            color = if (isToday) Color(0xFF1e00a9) else Color(0xFF777587)
        )
        if (showNewBadge) {
            Spacer(Modifier.width(8.dp))
            Surface(
                color = Color(0xFF10b981).copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "NEW", 
                    color = Color(0xFF10b981), 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 12.sp, 
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FeedCard(
    category: String,
    categoryColor: Color,
    categoryBg: Color,
    time: String,
    title: String,
    description: String,
    isMustRead: Boolean,
    isSaved: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Surface(
        modifier = Modifier.fillMaxWidth().padding(start = 40.dp, bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFc7c4d8).copy(alpha = 0.5f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Meta Row
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(color = categoryBg, shape = RoundedCornerShape(999.dp), border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.2f))) {
                        Text(category, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = categoryColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                    }
                    if (isMustRead) {
                        Surface(color = Color(0xFFffdad6).copy(alpha = 0.5f), shape = RoundedCornerShape(999.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(8.dp)) {
                                    Box(modifier = Modifier.size(8.dp).scale(pulseScale).background(Color(0xFFba1a1a).copy(alpha = pulseAlpha), CircleShape))
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFba1a1a), CircleShape))
                                }
                                Spacer(Modifier.width(6.dp))
                                Text("MUST READ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFba1a1a), letterSpacing = 0.5.sp)
                            }
                        }
                    }
                }
                Text(time, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587))
            }
            
            // Content
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28), modifier = Modifier.padding(bottom = 12.dp), lineHeight = 32.sp)
            Text(description, fontSize = 16.sp, color = Color(0xFF464555), modifier = Modifier.padding(bottom = 24.dp), lineHeight = 24.sp)
            
            HorizontalDivider(color = Color(0xFFc7c4d8).copy(alpha = 0.3f))
            
            // Actions
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Bookmark, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isSaved) Color(0xFF1e00a9) else Color(0xFF777587))
                        Spacer(Modifier.width(4.dp))
                        Text(if (isSaved) "Saved" else "Save", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isSaved) Color(0xFF1e00a9) else Color(0xFF777587))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF777587))
                        Spacer(Modifier.width(4.dp))
                        Text("Share", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF777587))
                    }
                }
                
                if (isMustRead) {
                    Text("Read Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e00a9))
                }
            }
        }
    }
}
