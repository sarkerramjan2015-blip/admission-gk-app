package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKSubTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.SubTopicDetailRoute
import com.example.ui.navigation.MCQPracticeRoute
import com.example.ui.navigation.MCQQuizRoute

val TlPrimaryColor = Color(0xFF1e00a9)
val TlPrimaryContainerColor = Color(0xFF3525cd)
val TlSecondaryColor = Color(0xFF712ae2)
val TlBackgroundColor = Color(0xFFfcf8ff)
val TlOnSurfaceColor = Color(0xFF1b1a28)
val TlOnSurfaceVariantColor = Color(0xFF464555)
val TlOutlineVariantColor = Color(0xFFc7c4d8)
val TlSuccessColor = Color(0xFF10b981)
val TlQuizColor = Color(0xFFffb95f)
val TlQuizIconColor = Color(0xFF653e00)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicListScreen(topicId: String, title: String, viewModel: GKViewModel, navController: NavController) {
    val subTopicsResult by remember(topicId) { viewModel.getSubTopics(topicId) }.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admission GK", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TlPrimaryColor)
            )
        },
        containerColor = TlBackgroundColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Background Grid
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dotRadius = 1.dp.toPx()
                val spacing = 20.dp.toPx()
                val columns = (size.width / spacing).toInt()
                val rows = (size.height / spacing).toInt()
                for (i in 0..columns) {
                    for (j in 0..rows) {
                        drawCircle(
                            color = Color(0xFFe2dfff),
                            radius = dotRadius,
                            center = Offset(i * spacing, j * spacing)
                        )
                    }
                }
            }
            
            // Background Glow Blobs
            Box(
                modifier = Modifier
                    .offset(x = (-80).dp, y = (-80).dp)
                    .size(320.dp)
                    .background(Color(0xFFe2dfff).copy(alpha = 0.4f), RoundedCornerShape(percent = 50))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 80.dp, y = 80.dp)
                    .size(384.dp)
                    .background(Color(0xFFeaddff).copy(alpha = 0.4f), RoundedCornerShape(percent = 50))
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text = "TOPIC LIST",
                            style = MaterialTheme.typography.labelMedium,
                            color = TlPrimaryColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TlOnSurfaceColor
                        )
                    }
                }

                itemsIndexed(subTopicsResult) { index, subTopic ->
                    var expanded by remember { mutableStateOf(index == 0) }

                    SubTopicCard(
                        subTopic = subTopic,
                        index = index,
                        expanded = expanded,
                        onHeaderClick = { expanded = !expanded },
                        onReadNoteClick = { navController.navigate(SubTopicDetailRoute(subTopic.id, subTopic.title)) },
                        onPracticeClick = { navController.navigate(MCQPracticeRoute(subTopic.id)) },
                        onQuizClick = { navController.navigate(MCQQuizRoute(subTopic.id)) }
                    )
                }

                item {
                    MegaQuizCard()
                }
            }
        }
    }
}

@Composable
fun SubTopicCard(
    subTopic: GKSubTopicEntity,
    index: Int,
    expanded: Boolean,
    onHeaderClick: () -> Unit,
    onReadNoteClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    val (icon, subtitle) = getSubTopicStyle(index)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, TlOutlineVariantColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHeaderClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFe2dfff), RoundedCornerShape(percent = 50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color(0xFF3323cc)) // primary-fixed-variant
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subTopic.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TlOnSurfaceColor,
                        lineHeight = 22.sp
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TlOnSurfaceVariantColor
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = TlOnSurfaceVariantColor
                )
            }

            // Expanded Content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    ActionItem(
                        title = "স্পেশাল নোট",
                        subtitle = "Special Note",
                        icon = Icons.Filled.Description,
                        iconColor = Color.White,
                        iconBgColor = TlSuccessColor,
                        containerBgColor = TlSuccessColor.copy(alpha = 0.05f),
                        borderColor = TlSuccessColor.copy(alpha = 0.2f),
                        textColor = TlSuccessColor,
                        onClick = onReadNoteClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionItem(
                        title = "এমসিকিউ প্র্যাকটিস",
                        subtitle = "MCQ Practice",
                        icon = Icons.Filled.EditNote,
                        iconColor = Color.White,
                        iconBgColor = TlPrimaryColor,
                        containerBgColor = TlPrimaryColor.copy(alpha = 0.05f),
                        borderColor = TlPrimaryColor.copy(alpha = 0.2f),
                        textColor = TlPrimaryColor,
                        onClick = onPracticeClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionItem(
                        title = "এমসিকিউ কুইজ",
                        subtitle = "MCQ Quiz",
                        icon = Icons.Filled.Quiz,
                        iconColor = TlQuizIconColor,
                        iconBgColor = TlQuizColor,
                        containerBgColor = TlQuizColor.copy(alpha = 0.1f),
                        borderColor = TlQuizColor.copy(alpha = 0.4f),
                        textColor = TlQuizIconColor,
                        onClick = onQuizClick
                    )
                }
            }
        }
    }
}

@Composable
fun ActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color,
    containerBgColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerBgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBgColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TlOnSurfaceVariantColor,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = textColor.copy(alpha = 0.4f))
    }
}

@Composable
fun MegaQuizCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF3525cd), Color(0xFF712ae2), Color(0xFFffb95f))))
                .padding(24.dp)
        ) {
            // Dots background for Quiz Card
            Canvas(modifier = Modifier.matchParentSize()) {
                val dotRadius = 1.dp.toPx()
                val spacing = 10.dp.toPx()
                val columns = (size.width / spacing).toInt()
                val rows = (size.height / spacing).toInt()
                for (i in 0..columns) {
                    for (j in 0..rows) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.1f),
                            radius = dotRadius,
                            center = Offset(i * spacing, j * spacing)
                        )
                    }
                }
            }

            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(percent = 50)
                        ) {
                            Text(
                                "LIVE NOW",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mega Quiz Challenge",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ENDS IN", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                            Text("12:45", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Text(
                    text = "আজকের মেগা কুইজে অংশগ্রহণ করে জিতে নিন স্পেশাল রিওয়ার্ড পয়েন্ট!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(vertical = 16.dp),
                    lineHeight = 22.sp
                )
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TlPrimaryColor),
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Text("Start Mega Quiz", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun getSubTopicStyle(index: Int): Pair<ImageVector, String> {
    val options = listOf(
        Pair(Icons.Filled.Public, "Geographic Location"),
        Pair(Icons.Filled.HistoryEdu, "History & Culture"),
        Pair(Icons.Filled.AccountBalance, "Economics & Dev"),
        Pair(Icons.Filled.EditNote, "Important Facts"),
        Pair(Icons.Filled.Description, "Constitution & Law")
    )
    return options[index % options.size]
}
