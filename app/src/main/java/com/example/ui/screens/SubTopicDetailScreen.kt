package com.example.ui.screens

import android.graphics.Color as AndroidColor
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.GKSubTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionTopBar
import com.example.ui.components.LoadingState
import com.example.ui.navigation.MCQQuizRoute
import com.example.ui.navigation.MCQPracticeRoute
import com.example.ui.theme.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin

// ── Markwon singleton (lazy per-context) ─────────────────────
@Composable
private fun rememberMarkwon(): Markwon {
    val context = LocalContext.current
    return remember(context) {
        Markwon.builder(context)
            .usePlugin(TablePlugin.create(context))
            .usePlugin(HtmlPlugin.create())
            .usePlugin(io.noties.markwon.image.ImagesPlugin.create())
            .build()
    }
}

// ── Main Screen ──────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTopicDetailScreen(
    subTopicId: String,
    viewModel: GKViewModel,
    navController: NavController,
) {
    val subTopic by remember(subTopicId) { viewModel.getSubTopicById(subTopicId) }
        .collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admission GK",
                subtitle = subTopic?.title ?: "Study note",
                showBack = true,
                onBack = { navController.popBackStack() },
            )
        },
        containerColor = AppBackground,
    ) { padding ->
        if (subTopic == null) {
            LoadingState(
                message = "Loading study note...",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 48.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { BookHeroSection(subTopic!!) }
                item { StudyContentCard(subTopic!!) }
                item { ActionButtonsSection(navController, subTopicId) }
            }
        }
    }
}

// ── Hero Banner ──────────────────────────────────────────────
@Composable
private fun BookHeroSection(subTopic: GKSubTopicEntity) {
    val imageRegex = Regex("""!\[.*?\]\((.*?)\)""")
    val firstImageMatch = imageRegex.find(subTopic.note)
    val heroImageUrl = firstImageMatch?.groupValues?.get(1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (heroImageUrl == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GradientStudyNote)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(heroImageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = ColorPainter(AppSurfaceAlt),
                    error = ColorPainter(AppSurfaceAlt),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                BrandPrimary.copy(alpha = 0.9f)
                            ),
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
                    color = BrandSecondary,
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
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "১৫ মি. রিডিং",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "১২০টি MCQ",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// ── Study Content Card (Markwon-powered) ─────────────────────
@Composable
private fun StudyContentCard(subTopic: GKSubTopicEntity) {
    val markwon = rememberMarkwon()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFDF6E3), // Paper color
        border = BorderStroke(1.dp, AppOutline),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = BrandPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "গুরুত্বপূর্ণ তথ্য",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(16.dp))

            // Parse important facts
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val jsonAdapter = moshi.adapter<List<String>>(listType)
            val facts = try { jsonAdapter.fromJson(subTopic.importantFacts) } catch (_: Exception) { emptyList() }

            // Render note with Markwon
            val noteText = subTopic.note.trimIndent()
            if (noteText.isNotBlank()) {
                MarkwonContent(
                    markwon = markwon,
                    markdown = noteText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Important Facts section
            if (!facts.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "✦ গুরুত্বপূর্ণ তথ্য",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrandPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    facts.forEach { fact ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .size(8.dp)
                                    .background(BrandPrimary, CircleShape)
                            )
                            Spacer(Modifier.width(14.dp))
                            Text(
                                fact,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary.copy(alpha = 0.9f),
                                lineHeight = 26.sp,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Confusion Corner
            if (subTopic.confusionClearance.isNotBlank()) {
                Spacer(Modifier.height(20.dp))
                ConfusionClearanceCard(subTopic.confusionClearance)
            }
        }
    }
}

// ── Markwon Content Renderer (AndroidView wrapper) ───────────
@Composable
private fun MarkwonContent(
    markwon: Markwon,
    markdown: String,
    modifier: Modifier = Modifier,
) {
    val paperBg = Color(0xFFFDF6E3).toArgb()
    val textColor = TextPrimary.toArgb()
    val accentColor = BrandPrimary.toArgb()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setBackgroundColor(AndroidColor.TRANSPARENT)
                setPadding(0, 0, 0, 0)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setLineSpacing(0f, 1.4f)
                setTextColor(textColor)

                // Table styling via Markwon
                markwon.setMarkdown(this, markdown)
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        }
    )
}

// ── Confusion Clearance Card ─────────────────────────────────
@Composable
private fun ConfusionClearanceCard(text: String) {
    val markwon = rememberMarkwon()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BrandAccent.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(BrandAccent)
            )
            Box(modifier = Modifier.weight(1f).padding(20.dp)) {
                Icon(
                    Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = BrandAccent.copy(alpha = 0.1f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(72.dp)
                        .offset(x = 16.dp, y = (-16).dp)
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = BrandAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Confusion Clearance",
                            style = MaterialTheme.typography.titleMedium,
                            color = BrandAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    MarkwonContent(
                        markwon = markwon,
                        markdown = text,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ── Action Buttons ───────────────────────────────────────────
@Composable
private fun ActionButtonsSection(navController: NavController, subTopicId: String) {
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
                containerColor = BrandPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text("MCQ Quiz শুরু করুন", fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 0.5.sp)
        }

        OutlinedButton(
            onClick = { navController.navigate(MCQPracticeRoute(subTopicId)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = AppSurface,
                contentColor = BrandPrimary
            ),
            border = BorderStroke(2.dp, BrandPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text("MCQ Practice করুন", fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 0.5.sp)
        }
    }
}
