package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.GKViewModel
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.ProfileRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: GKViewModel, navController: NavController) {
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
                                    .data("https://lh3.googleusercontent.com/aida-public/AB6AXuD_acPjtPfmBK68S9tlY4Z4lW8u2o9Jm8CRFqG1PvuyUrChaJeVP9bRJxYIXTWnb9JCyWRh6hckC1W6wxrpySLc28zrfY8kNriO9Eu2KJykM2rn6fVLXe22rY_m31bRo6FY7iwA9lKuIjQzvIqTz0dHDwxehY6vQZrk1oGx1JpA9IevFmaoSdwGmE3yFo3Ad68Lug86AlZbaB0xb2Mpfi8Am0ANYJVQGTFNXwwsXTHTyIvzA6j98z2nxtWTbfAVyG-kgDurln3Lydb3")
                                    .crossfade(true).build(),
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Admission GK", style = MaterialTheme.typography.titleLarge, color = Color(0xFF3525cd), fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color(0xFF3525cd))
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = "Profile")
        },
        containerColor = Color(0xFFf8f9ff)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Welcome Header
            item {
                Column {
                    Text("আপনার অগ্রগতি", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("আপনার প্রস্তুতির বর্তমান চিত্র এক নজরে দেখুন", color = Color(0xFF464555), fontSize = 16.sp)
                }
            }

            // Stats Bento Grid
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Total MCQs
                    Card(
                        modifier = Modifier.weight(1f).height(120.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = Color(0xFF3525cd))
                            Column {
                                Text("মোট MCQ অনুশীলন", fontSize = 12.sp, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                Text("450", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3525cd))
                            }
                        }
                    }
                    // Accuracy
                    Card(
                        modifier = Modifier.weight(1f).height(120.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(Icons.Filled.TrackChanges, contentDescription = null, tint = Color(0xFF712ae2))
                            Column {
                                Text("সঠিকতার হার", fontSize = 12.sp, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                Text("78%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF712ae2))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Best Score
                    Card(
                        modifier = Modifier.weight(1f).height(120.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = Color(0xFF684000))
                            Column {
                                Text("সেরা কুইজ স্কোর", fontSize = 12.sp, color = Color(0xFF464555), fontWeight = FontWeight.Medium)
                                Text("90%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF684000))
                            }
                        }
                    }
                    // Mega Quiz Rank
                    Card(
                        modifier = Modifier.weight(1f).height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF3525cd), Color(0xFF712ae2))))) {
                            Column(modifier = Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = Color.White)
                                Column {
                                    Text("মেগা কুইজ র্যাঙ্ক", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
                                    Text("12তম", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Subjects Progress
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("বিষয় ভিত্তিক অগ্রগতি", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Bangladesh GK
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { 0.65f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF10b981),
                                    trackColor = Color(0xFFe2e8f0),
                                    strokeWidth = 8.dp
                                )
                                Text("65%", fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF10b981), CircleShape))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("বাংলাদেশ বিষয়াবলি", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0b1c30))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("মুক্তিযুদ্ধ ও ইতিহাস বিভাগে চমৎকার উন্নতি করছেন!", fontSize = 14.sp, color = Color(0xFF464555), lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    progress = { 0.65f },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                                    color = Color(0xFF10b981),
                                    trackColor = Color(0xFFe5eeff)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // International GK
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { 0.42f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF4f46e5),
                                    trackColor = Color(0xFFe2e8f0),
                                    strokeWidth = 8.dp
                                )
                                Text("42%", fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF3525cd), CircleShape))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("আন্তর্জাতিক বিষয়াবলি", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0b1c30))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("আন্তর্জাতিক সংস্থা বিভাগে আরও অনুশীলনের প্রয়োজন।", fontSize = 14.sp, color = Color(0xFF464555), lineHeight = 18.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    progress = { 0.42f },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                                    color = Color(0xFF4f46e5),
                                    trackColor = Color(0xFFe5eeff)
                                )
                            }
                        }
                    }
                }
            }

            // Recent Activity
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("সাম্প্রতিক পড়াশোনা", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0b1c30))
                            Text("সবগুলো", color = Color(0xFF3525cd), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Item 1
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = Color(0xFF3525cd).copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
                                Icon(Icons.Filled.HistoryEdu, contentDescription = null, tint = Color(0xFF3525cd), modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("প্রাচীন বাংলা ও সুলতানি আমল", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0b1c30))
                                Text("২০ মিনিট আগে • ১৫টি প্রশ্নের উত্তর", fontSize = 12.sp, color = Color(0xFF464555))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Item 2
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = Color(0xFF712ae2).copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
                                Icon(Icons.Filled.Public, contentDescription = null, tint = Color(0xFF712ae2), modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("আন্তর্জাতিক সংস্থা ও জাতিসংঘ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0b1c30))
                                Text("২ ঘণ্টা আগে • মেগা কুইজ অংশগ্রহণ", fontSize = 12.sp, color = Color(0xFF464555))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Item 3
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = Color(0xFF684000).copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
                                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = Color(0xFF684000), modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("বাংলাদেশের ভৌগোলিক পরিচয়", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0b1c30))
                                Text("গতকাল • অধ্যায় ভিত্তিক অনুশীলন", fontSize = 12.sp, color = Color(0xFF464555))
                            }
                        }
                    }
                }
            }
            
            // Motivation Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF213145)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Blur Element
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 64.dp, y = (-64).dp)
                                .size(200.dp)
                                .background(Color(0xFF3525cd).copy(alpha = 0.3f), CircleShape)
                                .blur(80.dp)
                        )
                        
                        Column(
                            modifier = Modifier.padding(32.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("আপনি সাফল্যের খুব কাছে!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "আপনার বর্তমান অগ্রগতির হার অনুসারে আপনি ঢাকা বিশ্ববিদ্যালয়ের ভর্তি পরীক্ষায় মেধা তালিকায় থাকার উচ্চ সম্ভাবনা রাখছেন। পড়াশোনা চালিয়ে যান!",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3525cd)),
                                shape = RoundedCornerShape(24.dp),
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                            ) {
                                Text("পরবর্তী মডেল টেস্ট শুরু করুন", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, Color(0xFFc7c4d8)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFFba1a1a))
                            Spacer(Modifier.width(12.dp))
                            Text("Developer / Admin Zone", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1b1a28))
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate(com.example.ui.navigation.AdminDashboardRoute) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFba1a1a)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Open Admin Control Panel")
                        }
                    }
                }
            }
        }
    }
}
