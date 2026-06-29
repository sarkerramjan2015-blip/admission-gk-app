package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.getValue

fun Modifier.shiningEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shining")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shiningAnim"
    )
    this.drawWithContent {
        drawContent()
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.2f),
                Color.White.copy(alpha = 0.5f),
                Color.White.copy(alpha = 0.2f),
                Color.Transparent
            ),
            start = Offset(translateAnim, translateAnim - 500f),
            end = Offset(translateAnim + 500f, translateAnim)
        )
        drawRect(brush = brush)
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    secondary = BrandSecondary,
    tertiary = BrandAccent,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    primaryContainer = BrandPrimaryDeep,
    secondaryContainer = Color(0xFF3730A3),
    tertiaryContainer = Color(0xFF78350F),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF3B1F00),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF64748B)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    secondary = BrandSecondary,
    tertiary = BrandAccent,
    background = AppBackground,
    surface = AppSurface,
    surfaceVariant = AppSurfaceAlt,
    primaryContainer = Color(0xFFE0E0FF),
    secondaryContainer = Color(0xFFEADDFF),
    tertiaryContainer = Color(0xFFFFDDB8),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF3B1F00),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = AppOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
