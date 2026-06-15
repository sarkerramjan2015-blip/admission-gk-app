package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun LoginScreen(
    onGoogleSignIn: () -> Unit,
    onFingerprintLogin: (() -> Unit)? = null,
    fingerprintAvailable: Boolean = false,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "login")

    // Floating orbs animation
    val orb1X by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 30f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "orb1"
    )
    val orb2Y by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -25f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Reverse),
        label = "orb2"
    )
    val orb3Scale by infiniteTransition.animateFloat(
        initialValue = 0.85f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "orb3"
    )

    // Entrance animation
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val entAlpha by animateFloatAsState(if (appeared) 1f else 0f, tween(800), label = "entA")
    val entOffset by animateFloatAsState(if (appeared) 0f else 60f, tween(800, easing = FastOutSlowInEasing), label = "entO")
    val pulseScale by animateFloatAsState(if (isLoading) 0.95f else 1f, tween(400, easing = FastOutSlowInEasing), label = "pulse")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F0728),
                        Color(0xFF1A0F3C),
                        Color(0xFF241552),
                        Color(0xFF312E81)
                    )
                )
            )
    ) {
        // ── Animated Background Orbs ──────────────────
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-60).dp, y = orb1X.dp)
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF7C3AED).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = orb2Y.dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF6366F1).copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 60.dp)
                .size(240.dp)
                .scale(orb3Scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFFA855F7).copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )

        // ── Content ───────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(entAlpha)
                .offset(y = entOffset.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Admission GK",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // App Name
            Text(
                "Admission GK",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(8.dp))

            // Tagline
            Surface(
                color = Color(0xFF7C3AED).copy(alpha = 0.3f),
                shape = RoundedCornerShape(999.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Text(
                    "Your Ultimate Admission Preparation Partner",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(48.dp))

            // ── Features preview ──────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeaturePill("\uD83D\uDCDA", "Study")
                FeaturePill("\uD83C\uDFAF", "Practice")
                FeaturePill("\uD83C\uDFC6", "Compete")
            }

            Spacer(Modifier.height(36.dp))

            // ── Google Sign-In Button ─────────────────
            Button(
                onClick = onGoogleSignIn,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(pulseScale),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1A0F3C)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF1A0F3C),
                        strokeWidth = 2.dp
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Continue with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── Fingerprint Login Button ──────────────
            if (fingerprintAvailable && onFingerprintLogin != null) {
                Spacer(Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onFingerprintLogin,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(pulseScale),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Icon(
                        Icons.Filled.Fingerprint,
                        contentDescription = "Fingerprint",
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Login with Fingerprint",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Terms text ─────────────────────────────
            Text(
                "By continuing, you agree to our Terms of Service\nand Privacy Policy.",
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            // ── Error message ─────────────────────────
            if (errorMessage != null) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    color = Color(0xFFEF4444).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f))
                ) {
                    Text(
                        errorMessage,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        color = Color(0xFFFCA5A5),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Bottom branding ───────────────────────────
        Text(
            "NOMAN LL • Premium EdTech",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            color = Color.White.copy(alpha = 0.25f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FeaturePill(emoji: String, text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(999.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
            Text(text, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
        }
    }
}
