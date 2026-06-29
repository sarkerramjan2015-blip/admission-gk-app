package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.R

@Composable
fun MegaQuizRoutineDetailScreen(navController: NavController, viewModel: com.example.ui.GKViewModel, examId: String? = null) {
    val serial = remember(examId) {
        if (examId != null) examId.filter { it.isDigit() }.take(2).padStart(2, '0') else "০১"
    }
    val serialNum = remember(serial) { serial.toIntOrNull() ?: 1 }
    val examTitle = "মেগা কুইজ $serial"

    // Get dynamic topic list and goal description for this exam serial
    val examTopics by viewModel.getMegaQuizSubTopics(examId ?: "").collectAsState(initial = emptyList())
    val goalDescription = remember(serialNum) { getExamGoal(serialNum) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F9FF))) {
        // ── Header ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(Brush.linearGradient(listOf(Color(0xFF2E2395), Color(0xFF3F36D9), Color(0xFF6C4DFF))))
        ) {
            Box(Modifier.size(200.dp).offset(x = (-60).dp, y = (-60).dp).background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.06f), Color.Transparent)), CircleShape))

            Column(modifier = Modifier.fillMaxSize().padding(top = 44.dp, start = 16.dp, end = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(24.dp), contentScale = ContentScale.Fit)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(examTitle, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("রুটিন ও টপিকসমূহ", style = MaterialTheme.typography.bodySmall, color = Color(0xFFDDE3FF))
                    }
                    // Badge
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color.White.copy(alpha = 0.10f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.20f))
                    ) {
                        Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("যেকোনো সময় দেখা যাবে", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        // ── Content ──
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 160.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Summary Stats Card ──
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().drawBehind {
                        drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                    }) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatBox(icon = Icons.Filled.CalendarMonth, iconColor = Color(0xFF6C4DFF), label = "রুটিনের দিন", value = "দিন $serial", subtext = "মোট দিন: ৩০")
                            StatBox(icon = Icons.AutoMirrored.Filled.MenuBook, iconColor = Color(0xFF2434D8), label = "বিষয়ের সংখ্যা", value = "${examTopics.size} টি", subtext = "মোট টপিক")
                            StatBox(icon = Icons.Filled.Schedule, iconColor = Color(0xFF00A878), label = "সময়", value = "২ ঘন্টা", subtext = "প্রায় সময়")
                            StatBox(icon = Icons.Filled.TrackChanges, iconColor = Color(0xFFF6B93B), label = "উদ্দেশ্য", value = "মেগা কুইজ", subtext = "সর্বোচ্চ প্রস্তুতি")
                        }
                    }
                }
            }

            // ── Goal Card ──
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F0FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF6C4DFF).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Flag, null, tint = Color(0xFF6C4DFF), modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("দিন $serial এর লক্ষ্য", style = MaterialTheme.typography.titleSmall, color = Color(0xFF07113F), fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(goalDescription, style = MaterialTheme.typography.bodySmall, color = Color(0xFF5E6480), lineHeight = 18.sp)
                        }
                    }
                }
            }

            // ── Section Heading ──
            item {
                Column {
                    Text("আজকের টপিকসমূহ", style = MaterialTheme.typography.titleLarge, color = Color(0xFF07113F), fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF6C4DFF))
                    )
                }
            }

            // ── Topic List ──
            item {
                val badgeColors = listOf(
                    Color(0xFF6C4DFF), Color(0xFF2434D8), Color(0xFF00A878),
                    Color(0xFFF6B93B), Color(0xFFE87D9C), Color(0xFF00BCD4),
                    Color(0xFF5C6BC0)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().drawBehind {
                        drawRoundRect(color = Color(0xFFE5E9F8), cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(0.5.dp.toPx()))
                    }) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            examTopics.forEachIndexed { index, topic ->
                                TopicRow(
                                    serial = index + 1,
                                    title = topic.title,
                                    subtitle = topic.note.ifBlank { "টপিক সম্পর্কে বিস্তারিত পড়তে ক্লিক করুন" },
                                    badgeColor = badgeColors[index % badgeColors.size],
                                    onClick = {
                                        navController.navigate(com.example.ui.navigation.SubTopicDetailRoute(topic.id, topic.title))
                                    }
                                )
                                if (index < examTopics.size - 1) {
                                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).padding(horizontal = 16.dp).background(Color(0xFFE5E9F8)))
                                }
                            }
                        }
                    }
                }
            }

            // ── Bottom Info Strip ──
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF4F0FF),
                    border = BorderStroke(0.5.dp, Color(0xFF6C4DFF).copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Info, null, tint = Color(0xFF6C4DFF), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "একটি টপিকে ক্লিক করে বিস্তারিত পড়ুন ও কুইজ দিন",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF5E6480),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFF6C4DFF), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}



@Composable
private fun StatBox(icon: ImageVector, iconColor: Color, label: String, value: String, subtext: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(iconColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 11.sp, color = Color(0xFF07113F), fontWeight = FontWeight.ExtraBold, maxLines = 1)
        Text(label, fontSize = 9.sp, color = Color(0xFF8A90A8), maxLines = 1)
        Text(subtext, fontSize = 7.sp, color = Color(0xFFB0B6C8), maxLines = 1)
    }
}

@Composable
private fun TopicRow(serial: Int, title: String, subtitle: String, badgeColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Serial badge
        Box(
            modifier = Modifier.size(34.dp).clip(CircleShape).background(badgeColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${"%02d".format(serial)}",
                fontSize = 12.sp,
                color = badgeColor,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(Modifier.width(12.dp))

        // Title + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF07113F), fontWeight = FontWeight.Bold, maxLines = 1)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF5E6480), maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 16.sp)
        }

        Spacer(Modifier.width(8.dp))

        // Read button
        Surface(
            modifier = Modifier.height(30.dp),
            shape = RoundedCornerShape(999.dp),
            color = Color(0xFF2638D9).copy(alpha = 0.06f),
            border = BorderStroke(0.5.dp, Color(0xFF2638D9).copy(alpha = 0.15f))
        ) {
            Row(
                Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Book, null, tint = Color(0xFF2638D9), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("Read This Topic", fontSize = 9.sp, color = Color(0xFF2638D9), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.width(4.dp))

        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFFB0B6C8), modifier = Modifier.size(16.dp))
    }
}



private fun getExamGoal(serial: Int): String {
    val goals = mapOf(
        1 to "আজকের দিনে বাংলাদেশের ইতিহাস ও মুক্তিযুদ্ধ এর গুরুত্বপূর্ণ টপিকগুলো পড়ুন ও কুইজ দিন।",
        2 to "আজকের দিনে আন্তর্জাতিক সংস্থা ও জোট সম্পর্কে বিস্তারিত পড়ুন ও কুইজ দিন।",
        3 to "আজকের দিনে বাংলাদেশের সংবিধান ও সরকার কাঠামো সম্পর্কে পড়ুন ও কুইজ দিন।",
        4 to "আজকের দিনে বিশ্ব সভ্যতা ও ইতিহাসের গুরুত্বপূর্ণ অধ্যায়গুলো পড়ুন ও কুইজ দিন।",
        5 to "আজকের দিনে বাংলাদেশের ভূগোল ও প্রাকৃতিক বৈশিষ্ট্য সম্পর্কে পড়ুন ও কুইজ দিন।",
        6 to "আজকের দিনে বিশ্ব ভূগোল ও গুরুত্বপূর্ণ ভৌগোলিক তথ্য পড়ুন ও কুইজ দিন।",
        7 to "আজকের দিনে বাংলাদেশের অর্থনীতি ও আর্থিক খাত সম্পর্কে পড়ুন ও কুইজ দিন।",
        8 to "আজকের দিনে আন্তর্জাতিক অর্থনীতি ও আর্থিক প্রতিষ্ঠান সম্পর্কে পড়ুন ও কুইজ দিন।",
        9 to "আজকের দিনে বাংলাদেশের সরকার ও প্রশাসনিক কাঠামো পড়ুন ও কুইজ দিন।",
        10 to "আজকের দিনে আন্তর্জাতিক রাজনীতি ও কূটনীতি সম্পর্কে পড়ুন ও কুইজ দিন।",
        11 to "আজকের দিনে বাংলা সাহিত্য, সংস্কৃতি ও ভাষা সম্পর্কে পড়ুন ও কুইজ দিন।",
        12 to "আজকের দিনে বিশ্বের দেশ, রাজধানী ও জাতীয় প্রতীক সম্পর্কে পড়ুন ও কুইজ দিন।",
        13 to "আজকের দিনে বাংলাদেশের সম্পদ ও যোগাযোগ ব্যবস্থা পড়ুন ও কুইজ দিন।",
        14 to "আজকের দিনে বিজ্ঞান, প্রযুক্তি ও আবিষ্কার সম্পর্কে পড়ুন ও কুইজ দিন।",
        15 to "আজকের দিনে বাংলাদেশের শিক্ষা, স্বাস্থ্য ও সামাজিক উন্নয়ন পড়ুন ও কুইজ দিন।",
        16 to "আজকের দিনে পরিবেশ, জলবায়ু ও দুর্যোগ ব্যবস্থাপনা পড়ুন ও কুইজ দিন।",
        17 to "আজকের দিনে বাংলাদেশের সাম্প্রতিক ঘটনা ও উন্নয়ন পড়ুন ও কুইজ দিন।",
        18 to "আজকের দিনে আন্তর্জাতিক সাম্প্রতিক তথ্য ও ঘটনা পড়ুন ও কুইজ দিন।",
        19 to "আজকের দিনে সকল বিষয়ের পূর্ণাঙ্গ রিভিশন ও মডেল MCQ প্র্যাকটিস করুন।",
        20 to "আজকের দিনে চূড়ান্ত মডেল টেস্ট ও পূর্ণাঙ্গ রিভিশন সম্পন্ন করুন।"
    )
    return goals[serial] ?: goals[1]!!
}
