package com.trezanix.mytreza.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Trust Finance Palette (Blue = Trust)
val BrandPrimary = Color(0xFF0F62FE) // Royal Blue (Trust, Professional)
val BrandSecondary = Color(0xFF3981F7) // Lighter Blue (Active State)
val BrandAccent = Color(0xFF00D2A0) // Success/Growth Green (Teal-ish)
val BrandDarkText = Color(0xFF0F172A) // Slate 900 (Sharp, Official)

val BrandBackground = Color(0xFFF5F9FF) // Very Light Blue Tint (Clean, Corporate)
val ShapeColor1 = Color(0xFFDCECFF) // Pale Sky Blue (Subtle)
val ShapeColor2 = Color(0xFFE6F0FF) // Soft Cloud Blue

val SurfaceColor = Color.White
val BackgroundGray = Color(0xFFF1F5F9) // Slate 50
val TextHint = Color(0xFF64748B) // Slate 500
val TextPrimary = Color(0xFF0F172A) // Slate 900
val BorderColor = Color(0xFFE2E8F0) // Slate 200

// Components
val InputBackground = Color(0xFFFFFFFF) // White (Clean for Finance)
val InputBorder = Color(0xFFE2E8F0) // Slate 200 (Defined Border for Security feel)

// Gradients
val BrandGradient = Brush.horizontalGradient(
    colors = listOf(BrandPrimary, BrandSecondary)
) // Professional Blue Gradient
