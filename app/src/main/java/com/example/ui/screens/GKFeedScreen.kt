package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.RecentGKEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionBottomBar
import com.example.ui.components.AdmissionTopBar
import com.example.ui.navigation.HomeRoute
import com.example.ui.theme.*
import com.example.ui.util.parseHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GKFeedScreen(viewModel: GKViewModel, navController: NavController) {
    val bdItems by viewModel.recentGKBD.collectAsStateWithLifecycle(initialValue = emptyList())
    val intItems by viewModel.recentGKInt.collectAsStateWithLifecycle(initialValue = emptyList())
    val allItems = bdItems + intItems

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var expandedCardId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admission GK",
                subtitle = "Recent GK",
                showBack = true,
                onBack = { navController.popBackStack() },
                useGradient = true
            )
        },
        bottomBar = { AdmissionBottomBar(navController = navController, currentRoute = "RecentGK") },
        containerColor = AppBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BrandPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Submit GK", modifier = Modifier.size(26.dp))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = BrandPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("সব", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("বাংলাদেশ (\uD83C\uDDE7\uD83C\uDDE9)", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("আন্তর্জাতিক (\uD83C\uDF0D)", fontWeight = FontWeight.Bold) }
                )
            }

            val displayItems = when (selectedTab) {
                1 -> bdItems
                2 -> intItems
                else -> allItems
            }

            if (displayItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Public, null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("কোন Recent GK নেই", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                        Text("FAB button ব্যবহার করে তথ্য যোগ করুন", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayItems) { item ->
                        FeedCard(
                            item = item,
                            isExpanded = expandedCardId == item.id,
                            onTap = { expandedCardId = if (expandedCardId == item.id) null else item.id },
                            onAddInfo = { showAddDialog = true }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            SubmitGkDialog(
                onDismiss = { showAddDialog = false },
                onSubmit = { category, title, note, confusionCorner, source ->
                    viewModel.submitRecentGK(category, title, note, confusionCorner, source, "User")
                    showAddDialog = false
                }
            )
        }
    }
}

// ── Feed Card with expand/collapse ────────────────────
@Composable
private fun FeedCard(item: RecentGKEntity, isExpanded: Boolean, onTap: () -> Unit, onAddInfo: () -> Unit) {
    val isBd = item.category == "BD"
    val accentColor = if (isBd) BdGkColor else IntGkColor
    val borderGradient = if (isBd) GradientBdRecentBorder else GradientIntRecentBorder
    val catLabel = if (isBd) "বাংলাদেশ" else "আন্তর্জাতিক"
    val catFlag = if (isBd) "\uD83C\uDDE7\uD83C\uDDE9" else "\uD83C\uDF0D"

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(400, easing = FastOutSlowInEasing), label = "ea")
    val entSlide by animateFloatAsState(if (appeared) 0f else 10f, tween(400, easing = FastOutSlowInEasing), label = "es")
    val expandAlpha by animateFloatAsState(if (isExpanded) 1f else 0f, tween(300), label = "exa")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(entAlpha)
            .offset(y = entSlide.dp)
            .clickable(onClick = onTap)
            .drawBehind {
                drawRoundRect(
                    brush = borderGradient,
                    cornerRadius = CornerRadius(18.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 6.dp else 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = accentColor.copy(alpha = 0.08f),
                    border = BorderStroke(0.5.dp, accentColor.copy(alpha = 0.12f))
                ) {
                    Text(
                        "$catFlag $catLabel",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("bn")).format(java.util.Date(item.createdAt)),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                item.topicTitle.parseHtml(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
            )

            if (item.specialTopicNote.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                if (!isExpanded) {
                    Text(
                        item.specialTopicNote.parseHtml(),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    // Book-like point-wise boxes
                    val points = item.specialTopicNote.split("।").map { it.trim() }.filter { it.isNotEmpty() }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        points.forEach { point ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFAFBFF),
                                border = BorderStroke(1.dp, Color(0xFFE1E6F5))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "$point।",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Expanded detail ──
            if (isExpanded) {
                // Possible Question / Exam Focus
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("\uD83D\uDCA1", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Text("Possible Question & Exam Focus", style = MaterialTheme.typography.titleSmall, color = accentColor, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(8.dp))
                        
                        val rawQText = if (item.confusionCorner.isNotBlank()) {
                            item.confusionCorner
                        } else {
                            "<b>Q:</b> ${item.topicTitle} সম্পর্কিত গুরুত্বপূর্ণ তথ্যগুলো কী কী?<br><b>Ans:</b> উপরের পয়েন্টগুলো থেকে পরীক্ষার জন্য প্রস্তুতি নিন।"
                        }
                        val qText = rawQText.parseHtml()
                        
                        Text(
                            text = qText,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Source & Contributor detail
                if (item.sourceText.isNotBlank() || item.contributorName.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AppSurfaceAlt
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (item.sourceText.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Source, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("সোর্স: ${item.sourceText}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                            if (item.contributorName.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Person, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("অবদানকারী: ${item.contributorName}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                            Text(
                                java.text.SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a", java.util.Locale("bn")).format(java.util.Date(item.createdAt)),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // ── Add Info Button ──────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrandPrimary.copy(alpha = 0.06f))
                    .clickable { onAddInfo() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Info", tint = BrandPrimary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Add Info", style = MaterialTheme.typography.labelMedium, color = BrandPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Submit Dialog ──────────────────────────────────────
@Composable
private fun SubmitGkDialog(
    onDismiss: () -> Unit,
    onSubmit: (category: String, title: String, note: String, confusionCorner: String, source: String) -> Unit,
) {
    var category by remember { mutableStateOf("BD") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var confusionCorner by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFFF0EDFF), shape = CircleShape, modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.PostAdd, null, tint = BrandPrimary, modifier = Modifier.size(22.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Recent GK যোগ করুন", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("আপনার তথ্য অ্যাডমিন রিভিউর পর প্রকাশ হবে", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AppSurfaceAlt,
                        border = BorderStroke(1.dp, AppOutlineSoft),
                        modifier = Modifier.fillMaxWidth().clickable { expandedCategory = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (category == "BD") "\uD83C\uDDE7\uD83C\uDDE9 বাংলাদেশ GK" else "\uD83C\uDF0D আন্তর্জাতিক GK",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Icon(Icons.Filled.ArrowDropDown, null, tint = TextMuted)
                        }
                    }
                    DropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        DropdownMenuItem(text = { Text("\uD83C\uDDE7\uD83C\uDDE9 বাংলাদেশ GK") }, onClick = { category = "BD"; expandedCategory = false })
                        DropdownMenuItem(text = { Text("\uD83C\uDF0D আন্তর্জাতিক GK") }, onClick = { category = "INT"; expandedCategory = false })
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("টপিক টাইটেল *") },
                    placeholder = { Text("যেমন: পদ্মা সেতুর নতুন তথ্য", color = TextMuted) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = AppOutlineSoft,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = BrandPrimary
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("বিশেষ তথ্য") },
                    placeholder = { Text("বিস্তারিত তথ্য লিখুন...", color = TextMuted) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = AppOutlineSoft,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = BrandPrimary
                    ),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = confusionCorner,
                    onValueChange = { confusionCorner = it },
                    label = { Text("কনফিউশন কর্নার (ঐচ্ছিক)") },
                    placeholder = { Text("ট্রিকি বা কনফিউজিং পয়েন্ট লিখুন...", color = TextMuted) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = AppOutlineSoft,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = BrandPrimary
                    ),
                    maxLines = 4
                )

                OutlinedTextField(
                    value = source,
                    onValueChange = { source = it },
                    label = { Text("সোর্স (ঐচ্ছিক)") },
                    placeholder = { Text("যেমন: দৈনিক ইত্তেফাক", color = TextMuted) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = AppOutlineSoft,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = BrandPrimary
                    ),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSubmit(category, title, note, confusionCorner, source)
                    }
                },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text("সাবমিট করুন", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("বাতিল", color = TextMuted) }
        }
    )
}
