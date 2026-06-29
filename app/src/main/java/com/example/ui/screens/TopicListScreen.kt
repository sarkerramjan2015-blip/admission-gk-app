package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.data.GKSubTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.MCQPracticeRoute
import com.example.ui.navigation.MCQQuizRoute
import com.example.ui.navigation.MegaQuizRoutineRoute
import com.example.ui.navigation.SubTopicDetailRoute
import com.example.ui.theme.*

private val Accent = Color(0xFF00BFA5)
private val AccentDark = Color(0xFF00897B)
private val AccentLight = Color(0xFFB2DFDB)

private val AccentColors = listOf(
    Color(0xFF4DB6AC), Color(0xFF7986CB), Color(0xFFFF8A65),
    Color(0xFF9575CD), Color(0xFF64B5F6), Color(0xFF81C784),
    Color(0xFFFFB74D), Color(0xFFE57373), Color(0xFF4DD0E1),
    Color(0xFFBA68C8), Color(0xFFAED581), Color(0xFFFFD54F),
    Color(0xFF4FC3F7), Color(0xFFF06292), Color(0xFFA1887F),
    Color(0xFF90A4AE)
)

private val topicIcons = listOf(
    "🗺️", "💧", "🌤️", "🌾", "🏛️", "🚇",
    "👥", "📜", "⚖️", "🗳️", "🏠", "🌍",
    "📖", "📌", "🎭", "📡"
)

// ───────────── NAV HELPERS ─────────────

fun nav(controller: NavController, route: Any) {
    controller.navigate(route) { launchSingleTop = true }
}

// ────────────── HEADER ──────────────

@Composable
fun SubTopicListHeader(total: Int, accent: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        shadowElevation = 3.dp
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(36.dp).background(
                    brush = Brush.linearGradient(listOf(accent, accent.copy(alpha = 0.5f))),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Sub Topics",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "$total topics available",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ────────────── GRID CARD (static, no expansion) ──────────────

@Composable
fun SubTopicGridCard(
    subTopic: GKSubTopicEntity,
    colorIndex: Int,
    onClick: () -> Unit
) {
    val accent = AccentColors[colorIndex % AccentColors.size]
    val icon = topicIcons.getOrElse(colorIndex % topicIcons.size) { "📖" }
    var pressAnim by remember { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(pressAnim, spring(0.6f, 300f), label = "")
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    LaunchedEffect(pressed) { pressAnim = if (pressed) 0.93f else 1f }

    val shape = RoundedCornerShape(14.dp)
    val borderClr = accent.copy(alpha = 0.25f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .scale(scale)
            .shadow(1.5.dp, shape, spotColor = accent.copy(alpha = 0.15f)),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, borderClr),
        onClick = onClick,
        interactionSource = interaction
    ) {
        Column(Modifier.fillMaxSize()) {
            // Gradient header
            Box(
                Modifier.fillMaxWidth().height(3.dp).background(
                    Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.3f)))
                )
            )
            Column(
                Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon
                Box(
                    Modifier.size(38.dp).background(
                        brush = Brush.radialGradient(
                            listOf(accent.copy(alpha = 0.2f), Color.Transparent)
                        ),
                        shape = CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 18.sp)
                }
                Spacer(Modifier.weight(1f))
                // Title
                Text(
                    subTopic.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        "3 actions",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ────────── SHIMMER ACTION BUTTON ──────────

@Composable
fun ShimmerActionButton(
    label: String,
    subLabel: String,
    icon: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "shim")
    val shimmerX by transition.animateFloat(
        -200f, 800f, infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart), label = ""
    )
    val glowAlpha by transition.animateFloat(
        0f, 0.2f, infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = ""
    )

    var pressScale by remember { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(pressScale, spring(0.55f, 400f), label = "")
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    LaunchedEffect(pressed) { pressScale = if (pressed) 0.93f else 1f }

    val shape = RoundedCornerShape(14.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick,
        interactionSource = interaction
    ) {
        Box(Modifier.fillMaxSize().background(gradient, shape)) {
            // Shimmer sweep
            Box(
                Modifier.fillMaxSize().drawBehind {
                    val x = shimmerX.dp.toPx()
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.18f), Color.Transparent),
                            startX = x, endX = x + 250f
                        ),
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                }
            )
            // Glow pulse
            Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = glowAlpha), shape))
            // Left accent strip
            Box(
                Modifier.align(Alignment.CenterStart).width(5.dp).fillMaxHeight().background(
                    Brush.verticalGradient(listOf(Color.White.copy(0.6f), Color.White.copy(0.1f))),
                    RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                )
            )

            Row(
                Modifier.fillMaxSize().padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(36.dp).background(
                        Brush.radialGradient(listOf(Color.White.copy(0.3f), Color.Transparent)),
                        CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) { Text(icon, fontSize = 18.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(subLabel, color = Color.White.copy(0.8f), fontSize = 11.sp)
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White.copy(0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ────────── MEGA QUIZ BANNER ──────────

@Composable
fun MiniQuizBanner(nav: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            Modifier.fillMaxWidth().background(
                Brush.horizontalGradient(listOf(Color(0xFFFF6F00), Color(0xFFFFA000)))
            ).padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Mega Quiz",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Participate in mega quiz", color = Color.White.copy(0.8f), fontSize = 13.sp, maxLines = 3)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { nav(nav, MegaQuizRoutineRoute) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("Enter Quiz", color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ────────── SECTION HEADER ──────────

@Composable
fun SectionHeader(title: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("➤ ", color = color, fontSize = 16.sp)
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// ────────── MAIN SCREEN ──────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicListScreen(
    mainTopicId: String,
    title: String,
    vm: GKViewModel,
    nav: NavController
) {
    val subs by vm.getSubTopics(mainTopicId).collectAsState(initial = emptyList())

    val accent = if (mainTopicId.startsWith("bd_")) AccentDark else Accent

    // Dialog state — selected sub-topic (null = closed)
    var selectedSubTopic by remember { mutableStateOf<GKSubTopicEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (mainTopicId) {
                            "bd_1" -> "ভৌগোলিক অবস্থান"
                            "bd_2" -> "নদ-নদী"
                            "bd_3" -> "আবহাওয়া/জলবায়ু"
                            "bd_4" -> "কৃষি/খনিজ/শিল্প"
                            "bd_5" -> "অর্থনীতি"
                            "bd_6" -> "যোগাযোগ"
                            "bd_7" -> "উপজাতি"
                            "bd_8" -> "প্রাচীন ইতিহাস"
                            "bd_9" -> "ব্রিটিশ আমল"
                            "bd_10" -> "পাকিস্তান আমল"
                            "bd_11" -> "মুক্তিযুদ্ধ"
                            "bd_12" -> "স্বাধীনতা পরবর্তী"
                            "bd_13" -> "সংবিধান"
                            "bd_14" -> "সংস্কৃতি/পর্যটন"
                            "bd_15" -> "বিবিধ"
                            else -> "Sub Topics"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item(span = { GridItemSpan(3) }) {
                SubTopicListHeader(subs.size, accent)
            }

            // Sub-topic cards — static grid, click opens dialog
            itemsIndexed(subs) { idx, st ->
                SubTopicGridCard(
                    subTopic = st,
                    colorIndex = idx,
                    onClick = { selectedSubTopic = st }
                )
            }

            // Spacer
            item(span = { GridItemSpan(3) }) { Spacer(Modifier.height(4.dp)) }

            // Mega quiz banner
            item(span = { GridItemSpan(3) }) { MiniQuizBanner(nav) }
        }
    }

    // ────────── CENTERED DIALOG ──────────

    selectedSubTopic?.let { st ->
        val accentIdx = subs.indexOf(st).coerceAtLeast(0)
        val dialogAccent = AccentColors[accentIdx % AccentColors.size]

        Dialog(onDismissRequest = { selectedSubTopic = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
                tonalElevation = 4.dp
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Close button
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { selectedSubTopic = null },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Accent icon
                    Box(
                        Modifier.size(48.dp).background(
                            brush = Brush.radialGradient(
                                listOf(dialogAccent.copy(alpha = 0.25f), Color.Transparent)
                            ),
                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            topicIcons.getOrElse(accentIdx % topicIcons.size) { "📖" },
                            fontSize = 24.sp
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Title
                    Text(
                        st.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Choose an action",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))

                    // Gradient divider
                    Box(
                        Modifier.fillMaxWidth().height(1.dp).background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, dialogAccent.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                    )
                    Spacer(Modifier.height(16.dp))

                    // 3 shimmer action buttons
                    ShimmerActionButton(
                        label = "✦ Special Note",
                        subLabel = "Read detailed notes",
                        icon = "✦",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFF00897B), Color(0xFF4DB6AC)))
                    ) {
                        selectedSubTopic = null
                        nav(nav, SubTopicDetailRoute(st.id, st.title))
                    }

                    Spacer(Modifier.height(10.dp))

                    ShimmerActionButton(
                        label = "✧ MCQ Practice",
                        subLabel = "Practice unlimited MCQs",
                        icon = "✧",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFF5C6BC0), Color(0xFF7986CB)))
                    ) {
                        selectedSubTopic = null
                        nav(nav, MCQPracticeRoute(st.id))
                    }

                    Spacer(Modifier.height(10.dp))

                    ShimmerActionButton(
                        label = "◈ MCQ Quiz",
                        subLabel = "Timed quiz challenge",
                        icon = "◈",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFFE65100), Color(0xFFFF8F00)))
                    ) {
                        selectedSubTopic = null
                        nav(nav, MCQQuizRoute(st.id))
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
