package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKMainTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionTopBar
import com.example.ui.components.EmptyState
import com.example.ui.navigation.TopicListRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: String,
    title: String,
    viewModel: GKViewModel,
    navController: NavController,
) {
    val bdTopics by viewModel.bdTopics.collectAsStateWithLifecycle(initialValue = emptyList())
    val intTopics by viewModel.intTopics.collectAsStateWithLifecycle(initialValue = emptyList())
    val topics = if (category == "BANGLADESH") bdTopics else intTopics
    val accentColor = if (category == "BANGLADESH") BdGkColor else IntGkColor
    val subtitle = if (category == "BANGLADESH") "বাংলাদেশ বিষয়াবলি প্রস্তুতি" else "আন্তর্জাতিক বিষয়াবলি প্রস্তুতি"

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admission GK",
                subtitle = title,
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (topics.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState(
                    title = "কোন টপিক পাওয়া যায়নি",
                    message = "ডাটাবেসে এখনো টপিক যোগ করা হয়নি। অনুগ্রহ করে পরে আবার চেষ্টা করুন।",
                    icon = Icons.Filled.Public
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(3) }) {
                    CompactHeader(title, topics.size, accentColor)
                }
                itemsIndexed(topics) { index, topic ->
                    CompactTopicCard(topic, index, category, accentColor) {
                        navController.navigate(TopicListRoute(topic.id, topic.title))
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactHeader(title: String, count: Int, accent: Color) {
    var vis by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { vis = true }
    val a by animateFloatAsState(if (vis) 1f else 0f, tween(400), label = "ha")
    Column(modifier = Modifier.alpha(a).padding(bottom = 4.dp)) {
        Surface(
            color = accent.copy(alpha = 0.1f),
            shape = RoundedCornerShape(percent = 50),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                "$count টি টপিক", color = accent, fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
            )
        }
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
    }
}

@Composable
private fun CompactTopicCard(
    topic: GKMainTopicEntity,
    index: Int,
    category: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    var vis by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { vis = true }
    val a by animateFloatAsState(if (vis) 1f else 0f, tween(350, delayMillis = index * 60), label = "ca")
    val s by animateFloatAsState(if (vis) 1f else 0.85f, tween(350, delayMillis = index * 60), label = "cs")

    val (icon, tint) = when {
        topic.title.contains("পরিচিতি") || topic.title.contains("ভূ-প্রকৃতি") -> Icons.Filled.Map to Color(0xFF0D9488)
        topic.title.contains("নদ") || topic.title.contains("জলাশয়") -> Icons.Filled.Water to Color(0xFF0284C7)
        topic.title.contains("আবহাওয়া") || topic.title.contains("পরিবেশ") -> Icons.Filled.WbSunny to Color(0xFFD97706)
        topic.title.contains("কৃষি") || topic.title.contains("শিল্প") -> Icons.Filled.Agriculture to Color(0xFF65A30D)
        topic.title.contains("অর্থনীতি") || topic.title.contains("বাণিজ্য") -> Icons.Filled.AccountBalance to Color(0xFF4F46E5)
        topic.title.contains("যোগাযোগ") || topic.title.contains("প্রযুক্তি") -> Icons.Filled.Commute to Color(0xFF7C3AED)
        topic.title.contains("উপজাতি") || topic.title.contains("নৃ-গোষ্ঠী") -> Icons.Filled.Groups to Color(0xFFDB2777)
        topic.title.contains("ইতিহাস") || topic.title.contains("শাসন") -> Icons.Filled.HistoryEdu to Color(0xFFB45309)
        topic.title.contains("ব্রিটিশ") || topic.title.contains("আন্দোলন") -> Icons.Filled.Gavel to Color(0xFF9333EA)
        topic.title.contains("পাকিস্তান") -> Icons.Filled.Flag to Color(0xFF15803D)
        topic.title.contains("মুক্তিযুদ্ধ") || topic.title.contains("অভ্যুদয়") -> Icons.Filled.MilitaryTech to Color(0xFFDC2626)
        topic.title.contains("রাজনৈতিক") || topic.title.contains("সংবিধান") -> Icons.Filled.HowToVote to Color(0xFF2563EB)
        topic.title.contains("প্রত্নতাত্ত্বিক") || topic.title.contains("পর্যটন") -> Icons.Filled.Tour to Color(0xFF0891B2)
        topic.title.contains("সংস্কৃতি") || topic.title.contains("ক্রীড়া") -> Icons.Filled.Palette to Color(0xFFC026D3)
        else -> Icons.Filled.AutoAwesome to accentColor
    }

    Card(
        modifier = Modifier.fillMaxWidth().alpha(a).scale(s).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = BorderStroke(0.5.dp, AppOutlineSoft)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon chip
            Box(
                modifier = Modifier.size(44.dp)
                    .background(tint.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = tint, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(8.dp))
            // Title
            Text(
                topic.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(6.dp))
            // Bottom line
            Box(
                modifier = Modifier.width(24.dp).height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(tint.copy(alpha = 0.5f))
            )
        }
    }
}
