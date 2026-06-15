package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.R
import com.example.ui.navigation.HomeRoute
import com.example.ui.navigation.MegaQuizRoutineRoute
import com.example.ui.navigation.ProfileRoute
import com.example.ui.navigation.QuestionBankRoute
import com.example.ui.navigation.CategoryRoute
import com.example.ui.theme.AppBackground
import com.example.ui.theme.AppOutline
import com.example.ui.theme.AppOutlineSoft
import com.example.ui.theme.AppSurface
import com.example.ui.theme.AppSurfaceAlt
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDeep
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.BrandAccent
import com.example.ui.theme.ErrorColor
import com.example.ui.theme.SuccessColor
import com.example.ui.theme.GradientBrandHero
import com.example.ui.theme.GradientStudyNote
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdmissionTopBar(
    title: String = "Admission GK",
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    useGradient: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val contentColor = if (useGradient) Color.White else BrandPrimary
    val subColor = if (useGradient) Color.White.copy(alpha = 0.78f) else TextSecondary

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        shadowElevation = if (useGradient) 6.dp else 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (useGradient) GradientBrandHero
                    else Brush.verticalGradient(
                        listOf(AppBackground.copy(alpha = 0.98f), AppBackground.copy(alpha = 0.94f))
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (showBack && onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = contentColor
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (useGradient) Color.White.copy(alpha = 0.18f)
                        else BrandPrimary.copy(alpha = 0.1f),
                        border = BorderStroke(
                            1.dp,
                            if (useGradient) Color.White.copy(alpha = 0.22f)
                            else BrandPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Admission GK Logo",
                                modifier = Modifier.size(26.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = contentColor,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = subColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}

@Composable
fun AdmissionBottomBar(navController: NavController, currentRoute: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding(),
        color = Color(0xD9FFFFFF),
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp
    ) {
        Box {
            Box(modifier = Modifier.matchParentSize().border(0.5.dp, Color(0x33C7C0EE), RoundedCornerShape(28.dp)))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AdmissionNavItem(Icons.Filled.Home, "Home", currentRoute == "Home") {
                    if (currentRoute != "Home") navController.navigate(HomeRoute) { launchSingleTop = true }
                }
                AdmissionNavItem(Icons.Filled.Public, "GK", currentRoute == "Category" || currentRoute == "TopicList") {
                    if (currentRoute != "Category" && currentRoute != "TopicList")
                        navController.navigate("category/BANGLADESH/Bangladesh GK") { launchSingleTop = true }
                }
                AdmissionNavItem(Icons.AutoMirrored.Filled.MenuBook, "Study", currentRoute == "Archive") {
                    if (currentRoute != "Archive") navController.navigate(QuestionBankRoute()) { launchSingleTop = true }
                }
                AdmissionNavItem(Icons.Filled.Quiz, "Quiz", currentRoute == "MegaQuiz") {
                    if (currentRoute != "MegaQuiz") navController.navigate(MegaQuizRoutineRoute) { launchSingleTop = true }
                }
                AdmissionNavItem(Icons.Filled.Person, "Profile", currentRoute == "Profile") {
                    if (currentRoute != "Profile") navController.navigate(ProfileRoute) { launchSingleTop = true }
                }
            }
        }
    }
}

@Composable
fun AdmissionNavItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    val contentColor = if (selected) BrandPrimary else TextMuted
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (selected) 1.15f else 1f,
        animationSpec = androidx.compose.animation.core.tween(300),
        label = "navScale"
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) BrandPrimary.copy(alpha = 0.06f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(if (selected) 24.dp else 20.dp).scale(scale))
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = contentColor, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, maxLines = 1)
        if (selected) { Spacer(Modifier.height(2.dp)); Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BrandPrimary)) }
    }
}

@Composable
fun GradientHero(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    gradient: Brush = GradientBrandHero,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .padding(22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            if (eyebrow != null) {
                StatusPill(
                    text = eyebrow,
                    containerColor = Color.White.copy(alpha = 0.18f),
                    contentColor = Color.White,
                    borderColor = Color.White.copy(alpha = 0.25f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.82f),
                        lineHeight = 22.sp
                    )
                }
                if (trailing != null) {
                    Spacer(Modifier.width(16.dp))
                    trailing()
                }
            }
            if (content != null) content()
        }
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    containerColor: Color = AppSurface,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    elevation: Dp = 2.dp,
    borderColor: Color = AppOutlineSoft.copy(alpha = 0.75f),
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(modifier = Modifier.padding(contentPadding), content = content)
    }
}

@Composable
fun StatusPill(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = BrandPrimary.copy(alpha = 0.1f),
    contentColor: Color = BrandPrimary,
    borderColor: Color = BrandPrimary.copy(alpha = 0.18f),
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) Icon(
                icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = BrandPrimary,
    supportingText: String? = null,
) {
    PremiumCard(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(
                    value,
                    style = MaterialTheme.typography.displaySmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
                if (supportingText != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
            if (icon != null) {
                Surface(
                    color = accentColor.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        if (actionText != null && onActionClick != null) {
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPrimary.copy(alpha = 0.08f),
                    contentColor = BrandPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(actionText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search topics, MCQ or questions",
    enabled: Boolean = true,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, AppOutlineSoft.copy(alpha = 0.7f)),
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextMuted) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = BrandPrimary)
            },
            shape = RoundedCornerShape(999.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.MenuBook,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = BrandPrimary.copy(alpha = 0.08f),
            shape = CircleShape,
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = BrandPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingState(message: String = "Loading Admission GK...", modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = BrandPrimary)
        Spacer(Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}

@Composable
fun QuizOptionCard(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    correct: Boolean = false,
    wrong: Boolean = false,
    locked: Boolean = false,
    onClick: () -> Unit = {},
) {
    val accent = when {
        correct -> SuccessColor
        wrong -> ErrorColor
        selected -> BrandPrimary
        else -> TextSecondary
    }
    val container = when {
        correct -> SuccessColor.copy(alpha = 0.08f)
        wrong -> ErrorColor.copy(alpha = 0.08f)
        selected -> BrandPrimary.copy(alpha = 0.08f)
        else -> AppSurface
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !locked, onClick = onClick),
        color = container,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            if (selected || correct || wrong) 2.dp else 1.dp,
            accent.copy(alpha = if (selected || correct || wrong) 1f else 0.35f)
        )
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = accent.copy(alpha = 0.13f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(label, color = accent, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (locked || correct) {
                Icon(
                    if (correct) Icons.Filled.CheckCircle else Icons.Filled.HourglassEmpty,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    questionNumber: String,
    questionText: String,
    modifier: Modifier = Modifier,
    status: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    PremiumCard(modifier = modifier, contentPadding = PaddingValues(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            StatusPill(
                text = questionNumber,
                containerColor = BrandPrimary.copy(alpha = 0.1f),
                contentColor = BrandPrimary
            )
            if (status != null) StatusPill(
                text = status,
                containerColor = SuccessColor.copy(alpha = 0.1f),
                contentColor = SuccessColor
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            questionText,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp
        )
        Spacer(Modifier.height(18.dp))
        content()
    }
}

@Composable
fun AcademicImagePlaceholder(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String = "Admission GK study focus",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GradientStudyNote)
            .padding(20.dp)
    ) {
        Surface(
            color = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Admission GK Logo",
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = BrandPrimaryDeep,
                fontWeight = FontWeight.Bold
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        Surface(
            modifier = Modifier.align(Alignment.TopEnd),
            color = BrandAccent.copy(alpha = 0.18f),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                "Premium Prep",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = BrandPrimaryDeep,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ResultScoreCard(
    score: String,
    label: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    gradient: Brush = GradientBrandHero,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
            Text(
                score,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            if (subtitle != null) Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.82f),
                textAlign = TextAlign.Center
            )
        }
    }
}
