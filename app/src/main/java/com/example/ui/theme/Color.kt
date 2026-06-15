package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ── Premium Brand Identity ──────────────────────────
val BrandPrimary = Color(0xFF4F46E5)
val BrandPrimaryDeep = Color(0xFF4338CA)
val BrandSecondary = Color(0xFF7C3AED)
val BrandAccent = Color(0xFF10B981)

// ── Background & Surface ────────────────────────────
val AppBackground = Color(0xFFFAFBFF)
val AppSurface = Color(0xFFFFFFFF)
val AppSurfaceAlt = Color(0xFFF8F7FF)

val GlassCardBg = Color(0xB3FFFFFF)
val GlassCardBorder = Color(0x33C7C0EE)
val GlassShadow = Color(0x141E1B4B)

// ── Text ────────────────────────────────────────────
val TextPrimary = Color(0xFF1E1B4B)
val TextSecondary = Color(0xFF6B6785)
val TextMuted = Color(0xFF9B98B4)

// ── Semantic Feedback ───────────────────────────────
val SuccessColor = Color(0xFF10B981)
val ErrorColor = Color(0xFFEF4444)
val WarningColor = Color(0xFFF59E0B)

// ── Category Accent Colors ──────────────────────────
val BdGkColor = Color(0xFF10B981)
val IntGkColor = Color(0xFF7C3AED)
val MegaQuizColor = Color(0xFF5A67D8)
val QuestionBankColor = Color(0xFF5A67D8)

// ── Outline ─────────────────────────────────────────
val AppOutline = Color(0xFFDED8F2)
val AppOutlineSoft = Color(0xFFE3E0F4)

// ── Premium Card Gradients ──────────────────────────
// Mega Quiz: Rich Dark Purple → Deep Indigo → Brand Primary
val GradientMegaQuizBg = Brush.linearGradient(
    listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF6366F1))
)

// BD GK: Pastel Teal → Neon Mint Green
val GradientBdGkBg = Brush.linearGradient(
    listOf(Color(0xFFE0F2FE), Color(0xFF6EE7B7), Color(0xFF10B981))
)

// INT GK: Soft Orchid Pink → Electric Violet
val GradientIntlGkBg = Brush.linearGradient(
    listOf(Color(0xFFFAE8FF), Color(0xFFC084FC), Color(0xFF7C3AED))
)

// Recent GK BD gradient border
val GradientBdRecentBorder = Brush.linearGradient(
    listOf(Color(0xFF10B981), Color(0xFF0D9488))
)

// Recent GK INT gradient border
val GradientIntRecentBorder = Brush.linearGradient(
    listOf(Color(0xFF7C3AED), Color(0xFF6366F1))
)

// ── Shimmer / Metallic Sweep ────────────────────────
val GradientShimmer = Brush.horizontalGradient(
    listOf(
        Color(0x00FFFFFF),
        Color(0x66FFFFFF),
        Color(0x00FFFFFF)
    )
)

val GradientMetalSweep = Brush.horizontalGradient(
    listOf(
        Color(0x00FFFFFF),
        Color(0x00FFFFFF),
        Color(0x80FFFFFF),
        Color(0x30FFFFFF),
        Color(0x00FFFFFF)
    )
)

// ── Legacy Aliases (backward compat) ────────────────
val GradientBrand = Brush.linearGradient(listOf(BrandPrimaryDeep, BrandPrimary))
val GradientBrandHero = GradientMegaQuizBg
val GradientBdGk = GradientBdGkBg
val GradientInternational = GradientIntlGkBg
val GradientMegaQuiz = GradientMegaQuizBg
val GradientQuestionBank = Brush.linearGradient(listOf(Color(0xFF4338CA), Color(0xFF5A67D8), Color(0xFF7C3AED)))
val GradientStudyNote = Brush.linearGradient(listOf(Color(0xFFEEF2FF), Color(0xFFF5F2FF)))
val GradientSuccess = Brush.linearGradient(listOf(Color(0xFF059669), SuccessColor))
val GradientRecentBd = Brush.linearGradient(listOf(Color(0xFF0D9488), BdGkColor))
val GradientRecentInternational = Brush.linearGradient(listOf(Color(0xFF6366F1), IntGkColor))

// backward compat gradient brush aliases
val PremiumHeroGradient = GradientMegaQuizBg
val PremiumBdGkGradient = GradientBdGkBg
val PremiumIntlGkGradient = GradientIntlGkBg
val PremiumMegaQuizGradient = GradientMegaQuizBg
val PremiumQuestionBankGradient = GradientQuestionBank
val PremiumDailyQuizGradient = Brush.linearGradient(listOf(Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFFBBF24)))
val PremiumStatBdGradient = Brush.linearGradient(listOf(Color(0xFF14B8A6), Color(0xFF0D9488)))
val PremiumStatIntlGradient = Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF3B82F6)))
val PremiumStatStreakGradient = Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFFEF4444)))

val BrandGradientStart = BrandPrimaryDeep
val BrandGradientEnd = BrandSecondary
val BgPrimary = AppBackground
