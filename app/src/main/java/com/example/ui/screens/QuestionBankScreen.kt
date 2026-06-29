package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.R
import com.example.data.UniversityEntity
import com.example.data.UniversityQuestionEntity
import com.example.ui.GKViewModel
import com.example.ui.navigation.QuestionBankRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankScreen(uniId: String?, year: Int?, viewModel: GKViewModel, navController: NavController) {
    if (uniId != null && year != null) {
        val questions by remember(uniId, year) { viewModel.getUniversityQuestions(uniId, year) }.collectAsStateWithLifecycle(initialValue = emptyList())
        ExamPaperLayout(
            uniId = uniId,
            year = year,
            questions = questions,
            onFinish = { navController.popBackStack() }
        )
    } else {
        UniversitySelectionLayout(viewModel, navController)
    }
}

@Composable
fun UniversitySelectionLayout(viewModel: GKViewModel, navController: NavController) {
    val universities by viewModel.getUniversities().collectAsStateWithLifecycle(initialValue = emptyList())
    var showYearPicker by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().background(
                    Brush.verticalGradient(listOf(Color(0xFF1E0047), Color(0xFF312E81), Color(0xFF4338CA)))
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp).statusBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha = 0.15f), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(R.drawable.logo), contentDescription = "Admission GK", modifier = Modifier.size(28.dp), contentScale = ContentScale.Fit)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("Admission GK", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.ExtraBold)
                                Text("Question Bank", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        },
        containerColor = AppBackground
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Question Bank", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text("সর্বশেষ ৬ বছরের ভর্তি পরীক্ষার প্রশ্ন ও সমাধান", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }

            Spacer(Modifier.height(20.dp))

            if (universities.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("কোন বিশ্ববিদ্যালয় পাওয়া যায়নি", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(universities) { uni ->
                        UniversityCardCompact(
                            uni = uni,
                            delay = universities.indexOf(uni) * 50,
                            onClick = { showYearPicker = uni.id }
                        )
                    }
                }
            }
        }

        if (showYearPicker != null) {
            YearPickerDialog(
                uniId = showYearPicker!!,
                uniShortName = universities.find { it.id == showYearPicker }?.shortName ?: "",
                onDismiss = { showYearPicker = null },
                onYearSelected = { uId, year ->
                    showYearPicker = null
                    navController.navigate(QuestionBankRoute(uId, year))
                }
            )
        }
    }
}

@Composable
private fun UniversityCardCompact(uni: UniversityEntity, delay: Int, onClick: () -> Unit) {
    val (icon, _, gradient) = getUniStyle(uni.id)
    val logoRes = getUniLogoRes(uni.id)

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(400, delayMillis = delay, easing = FastOutSlowInEasing), label = "ea")
    val entScale by animateFloatAsState(if (appeared) 1f else 0.85f, tween(400, delayMillis = delay, easing = FastOutSlowInEasing), label = "es")

    Card(
        modifier = Modifier
            .scale(entScale)
            .alpha(entAlpha)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.95f)) {
            Box(modifier = Modifier.matchParentSize().clip(RoundedCornerShape(18.dp)).background(gradient))

            // Watermark
            Text(
                uni.shortName,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.07f),
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 4.dp),
                maxLines = 1
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (logoRes != 0) {
                            Image(painter = painterResource(logoRes), contentDescription = uni.shortName, modifier = Modifier.size(28.dp), contentScale = ContentScale.Fit)
                        } else {
                            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    uni.shortName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    uni.fullName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.75f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
private fun YearPickerDialog(
    uniId: String,
    uniShortName: String,
    onDismiss: () -> Unit,
    onYearSelected: (String, Int) -> Unit,
) {
    val years = listOf(2024, 2023, 2022, 2021, 2020, 2019)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF7C3AED))))
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("বছর নির্বাচন করুন", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("$uniShortName Admission ${years.last()}-${(years.first() + 1).toString().substring(2)}", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.75f))
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.background(Color.White.copy(alpha = 0.15f), CircleShape).size(36.dp)
                        ) {
                            Icon(Icons.Filled.Close, "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Year grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(years) { year ->
                        val nextYr = (year + 1).toString().substring(2)
                        Card(
                            modifier = Modifier.height(72.dp).clickable { onYearSelected(uniId, year) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = AppSurfaceAlt),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.1f))
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "$year-$nextYr",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandPrimaryDeep,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Dual buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onYearSelected(uniId, years.first()) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF312E81))
                    ) {
                        Text("সর্বশেষ প্রশ্ন", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    }
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, BrandPrimary.copy(alpha = 0.3f))
                    ) {
                        Text("বাতিল", fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 14.sp)
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

private fun getUniStyle(id: String): Triple<ImageVector, Color, Brush> {
    return when (id) {
        "du" -> Triple(Icons.Filled.School, Color.White, Brush.linearGradient(listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF6366F1))))
        "ju" -> Triple(Icons.Filled.AutoStories, Color.White, Brush.linearGradient(listOf(Color(0xFF5B21B6), Color(0xFF7C3AED), Color(0xFFA855F7))))
        "ru" -> Triple(Icons.Filled.HistoryEdu, Color.White, Brush.linearGradient(listOf(Color(0xFF92400E), Color(0xFFD97706), Color(0xFFF59E0B))))
        "cu" -> Triple(Icons.Filled.LocationCity, Color.White, Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF059669), Color(0xFF34D399))))
        "gst" -> Triple(Icons.Filled.Hub, Color.White, Brush.linearGradient(listOf(Color(0xFF1E3A5F), Color(0xFF2563EB), Color(0xFF60A5FA))))
        "ku" -> Triple(Icons.Filled.AccountBalance, Color.White, Brush.linearGradient(listOf(Color(0xFF0C4A6E), Color(0xFF0284C7), Color(0xFF38BDF8))))
        "iu" -> Triple(Icons.Filled.Mosque, Color.White, Brush.linearGradient(listOf(Color(0xFF14532D), Color(0xFF16A34A), Color(0xFF4ADE80))))
        "sust" -> Triple(Icons.Filled.Science, Color.White, Brush.linearGradient(listOf(Color(0xFF164E63), Color(0xFF0891B2), Color(0xFF22D3EE))))
        "buet" -> Triple(Icons.Filled.Engineering, Color.White, Brush.linearGradient(listOf(Color(0xFF7F1D1D), Color(0xFFDC2626), Color(0xFFF87171))))
        "bup" -> Triple(Icons.Filled.Shield, Color.White, Brush.linearGradient(listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF818CF8))))
        "jnu" -> Triple(Icons.Filled.School, Color.White, Brush.linearGradient(listOf(Color(0xFF134E4A), Color(0xFF0D9488), Color(0xFF5EEAD4))))
        "cou" -> Triple(Icons.Filled.School, Color.White, Brush.linearGradient(listOf(Color(0xFF4A1942), Color(0xFF9333EA), Color(0xFFC084FC))))
        "hstu" -> Triple(Icons.Filled.AccountBalance, Color.White, Brush.linearGradient(listOf(Color(0xFF701A75), Color(0xFFA21CAF), Color(0xFFD946EF))))
        "mbstu" -> Triple(Icons.Filled.AutoStories, Color.White, Brush.linearGradient(listOf(Color(0xFF1E3A5F), Color(0xFF1D4ED8), Color(0xFF3B82F6))))
        "nstu" -> Triple(Icons.Filled.Science, Color.White, Brush.linearGradient(listOf(Color(0xFF3B0764), Color(0xFF7C3AED), Color(0xFFA78BFA))))
        "just" -> Triple(Icons.Filled.LocationCity, Color.White, Brush.linearGradient(listOf(Color(0xFF0F4C5C), Color(0xFF06B6D4), Color(0xFF22D3EE))))
        "jkkniu" -> Triple(Icons.Filled.Mosque, Color.White, Brush.linearGradient(listOf(Color(0xFF1A1A40), Color(0xFF4F46E5), Color(0xFF818CF8))))
        "brur" -> Triple(Icons.Filled.HistoryEdu, Color.White, Brush.linearGradient(listOf(Color(0xFF831843), Color(0xFFDB2777), Color(0xFFF472B6))))
        "bu" -> Triple(Icons.Filled.AutoStories, Color.White, Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF10B981), Color(0xFF34D399))))
        "pust" -> Triple(Icons.Filled.Engineering, Color.White, Brush.linearGradient(listOf(Color(0xFF78350F), Color(0xFFF59E0B), Color(0xFFFCD34D))))
        else -> Triple(Icons.Filled.School, Color.White, Brush.linearGradient(listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF6366F1))))
    }
}

private fun getUniLogoRes(id: String): Int {
    return when (id) {
        "du" -> R.drawable.du
        "ju" -> R.drawable.ju
        "ru" -> R.drawable.ru
        "cu" -> R.drawable.cu
        "ku" -> R.drawable.ku
        "iu" -> R.drawable.islami_university
        "sust" -> R.drawable.sust
        "bup" -> R.drawable.bup
        "jnu" -> R.drawable.jnu
        "cou" -> R.drawable.cou
        "jkkniu" -> R.drawable.jkkniu
        "brur" -> R.drawable.brur
        "bu" -> R.drawable.bu
        else -> 0 // Use icon fallback by default
    }
}
