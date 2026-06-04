package com.example.ui.screens

import androidx.compose.material.icons.automirrored.filled.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.GKSubTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.MCQQuizRoute
import com.example.ui.navigation.MCQPracticeRoute
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

// New Colors based on Tailwind design
val SnSurfaceContainerLowest = Color(0xFFffffff)
val SnHighlightYellow = Color(0xFFfef3c7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTopicDetailScreen(subTopicId: String, viewModel: GKViewModel, navController: NavController) {
    val subTopic by remember(subTopicId) { viewModel.getSubTopicById(subTopicId) }.collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    androidx.compose.material3.Text(
                        text = subTopic?.title ?: "বিস্তারিত", 
                        fontWeight = FontWeight.Bold, 
                        color = SnPrimaryColor,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SnPrimaryColor)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = SnOnSurfaceVariantColor)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .background(SnPrimaryContainerColor, CircleShape)
                            .border(1.dp, SnPrimaryColor.copy(alpha = 0.2f), CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SnBackgroundColor.copy(alpha = 0.9f))
            )
        },
        containerColor = SnBackgroundColor,
    ) { padding ->
        if (subTopic == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = SnPrimaryColor) }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    BookHeroSection(subTopic!!)
                }

                item {
                    StudyContentCard(subTopic!!)
                }
                
                item {
                    ActionButtonsSection(navController, subTopicId)
                }
            }
        }
    }
}

@Composable
fun BookHeroSection(subTopic: GKSubTopicEntity) {
    val imageRegex = Regex("""!\[.*?\]\((.*?)\)""")
    val firstImageMatch = imageRegex.find(subTopic.note)
    val defaultImageUrl = "https://images.unsplash.com/photo-1546422904-90eab23c3d7e?q=80&w=1000&auto=format&fit=crop"
    val heroImageUrl = firstImageMatch?.groupValues?.get(1) ?: defaultImageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(heroImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, SnPrimaryColor.copy(alpha = 0.9f)),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Surface(
                    color = SnSecondaryColor,
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Text(
                        text = "ACADEMIC GK",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subTopic.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("১৫ মি. রিডিং", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("১২০টি MCQ", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StudyContentCard(subTopic: GKSubTopicEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SnSurfaceContainerLowest,
        border = BorderStroke(1.dp, SnOutlineVariantColor.copy(alpha = 0.3f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = SnPrimaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "গুরুত্বপূর্ণ তথ্য",
                    style = MaterialTheme.typography.titleLarge,
                    color = SnPrimaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val jsonAdapter = moshi.adapter<List<String>>(listType)
            val facts = try { jsonAdapter.fromJson(subTopic.importantFacts) } catch(e: Exception) { emptyList() }
            
            fun parseBold(text: String): androidx.compose.ui.text.AnnotatedString {
                return buildAnnotatedString {
                    val boldRegex = Regex("""\*\*(.*?)\*\*""")
                    var lastIndex = 0
                    boldRegex.findAll(text).forEach { match ->
                        append(text.substring(lastIndex, match.range.first))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = SnPrimaryColor)) {
                            append(match.groupValues[1])
                        }
                        lastIndex = match.range.last + 1
                    }
                    append(text.substring(lastIndex))
                }
            }

            val noteText = subTopic.note
            val imageRegex = Regex("""!\[.*?\]\((.*?)\)""")
            val matches = imageRegex.findAll(noteText).toList()

            if (matches.isEmpty()) {
                Text(
                    text = parseBold(noteText),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SnOnSurfaceColor,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 32.sp,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            } else {
                var lastIndex = 0
                matches.forEach { match ->
                    val textPart = noteText.substring(lastIndex, match.range.first).trim()
                    if (textPart.isNotEmpty()) {
                        Text(
                            text = parseBold(textPart),
                            style = MaterialTheme.typography.bodyLarge,
                            color = SnOnSurfaceColor,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 32.sp,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    val imageUrl = match.groupValues[1]
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    lastIndex = match.range.last + 1
                }
                val lastText = noteText.substring(lastIndex).trim()
                if (lastText.isNotEmpty()) {
                    Text(
                        text = parseBold(lastText),
                        style = MaterialTheme.typography.bodyLarge,
                        color = SnOnSurfaceColor,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 32.sp,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
            
            if (!facts.isNullOrEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    facts.forEach { fact ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .size(8.dp)
                                    .background(SnPrimaryColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                fact, 
                                style = MaterialTheme.typography.bodyLarge, 
                                color = SnOnSurfaceColor.copy(alpha = 0.9f), 
                                lineHeight = 28.sp,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            
            if (subTopic.confusionClearance.isNotBlank()) {
                Spacer(modifier = Modifier.height(24.dp))
                ConfusionClearanceCard(subTopic.confusionClearance)
            }
        }
    }
}

@Composable
fun ConfusionClearanceCard(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SnTertiaryFixedColor.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Left border indicator
            Box(modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(SnTertiaryColor)
            )
            Box(modifier = Modifier.weight(1f).padding(20.dp)) {
                // Background icon
                Icon(
                    Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = SnTertiaryColor.copy(alpha = 0.1f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(72.dp)
                        .offset(x = 16.dp, y = (-16).dp)
                )
                
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = SnTertiaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confusion Clearance",
                            style = MaterialTheme.typography.titleMedium,
                            color = SnTertiaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SnOnSurfaceColor,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtonsSection(navController: NavController, subTopicId: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { navController.navigate(MCQQuizRoute(subTopicId)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SnPrimaryColor,
                contentColor = SnOnPrimaryColor
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("MCQ Quiz শুরু করুন", fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 0.5.sp)
        }
        
        OutlinedButton(
            onClick = { navController.navigate(MCQPracticeRoute(subTopicId)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SnSurfaceContainerLowest,
                contentColor = SnPrimaryColor
            ),
            border = BorderStroke(2.dp, SnPrimaryColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("MCQ Practice করুন", fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 0.5.sp)
        }
    }
}
