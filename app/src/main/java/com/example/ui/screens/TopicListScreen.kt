package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.GKSubTopicEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionTopBar
import com.example.ui.navigation.*
import com.example.ui.theme.*

// ── Main Screen ──────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicListScreen(
    topicId: String, title: String, viewModel: GKViewModel, navController: NavController,
) {
    val subTopicsResult by remember(topicId) { viewModel.getSubTopics(topicId) }
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            AdmissionTopBar(title = "Admission GK", subtitle = title, showBack = true, onBack = { navController.popBackStack() }, useGradient = true)
        },
        containerColor = AppBackground
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val d = 1.dp.toPx(); val sp = 20.dp.toPx()
                val cs = (size.width / sp).toInt(); val rs = (size.height / sp).toInt()
                for (i in 0..cs) for (j in 0..rs) drawCircle(BrandPrimary.copy(alpha = 0.04f), d, Offset(i * sp, j * sp))
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(3) }) { CompactListHeader(title, subTopicsResult.size) }
                itemsIndexed(subTopicsResult) { i, st ->
                    SubTopicGridCard(subTopic = st, index = i, nav = navController)
                }
                item(span = { GridItemSpan(3) }) { MiniQuizBanner { navController.navigate(MegaQuizRoutineRoute) } }
            }
        }
    }
}

// ── Compact Header ───────────────────────────────────────────
@Composable
private fun CompactListHeader(title: String, count: Int) {
    var vis by remember { mutableStateOf(false) }; LaunchedEffect(Unit) { vis = true }
    val a by animateFloatAsState(if (vis) 1f else 0f, tween(400), label = "ha")
    Column(modifier = Modifier.alpha(a).padding(bottom = 4.dp)) {
        Surface(color = BrandPrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(percent = 50), modifier = Modifier.padding(bottom = 6.dp)) {
            Text("$count টি উপ-বিষয়", color = BrandPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp))
        }
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
    }
}

// ── Sub-topic Grid Card with Premium Popup ───────────────────
@Composable
private fun SubTopicGridCard(
    subTopic: GKSubTopicEntity,
    index: Int,
    nav: NavController,
) {
    var expanded by remember { mutableStateOf(false) }
    var vis by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { vis = true }

    val a by animateFloatAsState(
        targetValue = if (vis) 1f else 0f,
        animationSpec = tween(350, delayMillis = index * 60),
        label = "sa"
    )
    val s by animateFloatAsState(
        targetValue = if (vis) 1f else 0.85f,
        animationSpec = tween(350, delayMillis = index * 60),
        label = "ss"
    )
    val (icon, tint) = subTopicStyle(index)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(a)
            .scale(s)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 6.dp else 1.5.dp),
        border = BorderStroke(if (expanded) 1.2.dp else 0.5.dp, if (expanded) tint.copy(alpha = 0.4f) else AppOutlineSoft)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(tint.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(6.dp))

            // Title
            Text(
                subTopic.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            // ── Premium Popup Action Card ──────────────────
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(tween(250)),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeOut(tween(180))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    // Divider line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        tint.copy(alpha = 0.3f),
                                        tint.copy(alpha = 0.5f),
                                        tint.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Spacer(Modifier.height(8.dp))

                    // Action buttons with shimmer
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        ShimmerActionButton(
                            label = "✦ স্পেশাল নোট",
                            subtitle = "বিস্তারিত পড়ুন",
                            icon = Icons.Filled.Description,
                            accentColor = Color(0xFF0D9488),
                            gradientColors = listOf(Color(0xFF0D9488), Color(0xFF059669)),
                            onClick = { nav.navigate(SubTopicDetailRoute(subTopic.id, subTopic.title)) }
                        )

                        ShimmerActionButton(
                            label = "✧ MCQ প্র্যাকটিস",
                            subtitle = "অনুশীলন করুন",
                            icon = Icons.Filled.Quiz,
                            accentColor = Color(0xFF4F46E5),
                            gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)),
                            onClick = { nav.navigate(MCQPracticeRoute(subTopic.id)) }
                        )

                        ShimmerActionButton(
                            label = "◈ MCQ কুইজ",
                            subtitle = "সময়সহ কুইজ",
                            icon = Icons.Filled.Timer,
                            accentColor = Color(0xFFEA580C),
                            gradientColors = listOf(Color(0xFFEA580C), Color(0xFFDC2626)),
                            onClick = { nav.navigate(MCQQuizRoute(subTopic.id)) }
                        )
                    }
                }
            }
        }
    }
}

// ── Shimmer Action Button (Premium) ──────────────────────────
@Composable
private fun ShimmerActionButton(
    label: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    gradientColors: List<Color>,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale down on press with bouncy spring
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "btnScale"
    )

    // Shimmer sweep
    val inf = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by inf.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    val glowAlpha by inf.animateFloat(
        initialValue = 0.0f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(10.dp),
        color = accentColor.copy(alpha = 0.06f),
        border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.2f))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon box with shimmer glow
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .drawBehind {
                            // Base gradient
                            drawRoundRect(
                                brush = Brush.linearGradient(gradientColors.map { it.copy(alpha = 0.12f + glowAlpha) }),
                                cornerRadius = CornerRadius(8.dp.toPx())
                            )
                            // Shimmer sweep highlight
                            val sweepW = size.width * 0.6f
                            val x = shimmerOffset * (size.width + sweepW) - sweepW * 0.5f
                            drawRoundRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = glowAlpha * 2.5f),
                                        Color.Transparent
                                    ),
                                    startX = x - sweepW * 0.5f,
                                    endX = x + sweepW * 0.5f
                                ),
                                cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = accentColor, modifier = Modifier.size(16.dp))
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = accentColor
                    )
                    Text(
                        subtitle,
                        fontSize = 9.sp,
                        color = accentColor.copy(alpha = 0.6f)
                    )
                }

                // Arrow with subtle glow
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = accentColor.copy(alpha = 0.4f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ── Mini Quiz Banner ─────────────────────────────────────────
@Composable
private fun MiniQuizBanner(onClick: () -> Unit) {
    val inf = rememberInfiniteTransition()
    val bg by inf.animateFloat(0.2f, 0.5f, infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse), label = "bg")
    val sh by inf.animateFloat(-2f, 3.5f, infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Restart), label = "sh")
    var vis by remember { mutableStateOf(false) }; LaunchedEffect(Unit) { vis = true }
    val a by animateFloatAsState(if (vis) 1f else 0f, tween(500, delayMillis = 300), label = "ea")
    Card(modifier = Modifier.fillMaxWidth().padding(top = 4.dp).alpha(a), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), border = BorderStroke(1.5.dp, Color.White.copy(alpha = bg * 0.6f))) {
        Box(modifier = Modifier.fillMaxWidth().background(GradientMegaQuiz).padding(20.dp)) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column {
                        Surface(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(percent = 50)) { Text("লাইভ", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)) }
                        Spacer(Modifier.height(6.dp)); Text("মেগা কুইজ চ্যালেঞ্জ", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("অংশগ্রহণ করে জিতে নিন স্পেশাল পয়েন্ট!", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(10.dp)).background(Color.White).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
                    Text("শুরু করুন", fontWeight = FontWeight.Bold, color = BrandPrimary)
                    Box(modifier = Modifier.matchParentSize().offset(x = (sh * 300).dp).background(Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent, BrandPrimary.copy(alpha = 0.2f), BrandPrimary.copy(alpha = 0.05f), Color.Transparent))))
                }
            }
        }
    }
}

// ── Icon/Color rotation ──────────────────────────────────────
private fun subTopicStyle(index: Int): Pair<ImageVector, Color> = listOf(
    Icons.Filled.Public to Color(0xFF0D9488), Icons.Filled.HistoryEdu to Color(0xFFB45309),
    Icons.Filled.AccountBalance to Color(0xFF4F46E5), Icons.Filled.EditNote to Color(0xFF7C3AED),
    Icons.Filled.Description to Color(0xFFDB2777), Icons.Filled.Gavel to Color(0xFF9333EA),
    Icons.Filled.Flag to Color(0xFFDC2626), Icons.Filled.MilitaryTech to Color(0xFF15803D),
    Icons.Filled.Tour to Color(0xFF0891B2), Icons.Filled.Palette to Color(0xFFC026D3),
    Icons.Filled.Water to Color(0xFF0284C7), Icons.Filled.Groups to Color(0xFF2563EB),
    Icons.Filled.Commute to Color(0xFF7C3AED), Icons.Filled.WbSunny to Color(0xFFD97706),
    Icons.Filled.Agriculture to Color(0xFF65A30D), Icons.Filled.HowToVote to Color(0xFFEA580C)
)[index % 16]
