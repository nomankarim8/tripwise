package com.example.tripwise.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    // Primary Blue (headers, primary buttons, step 1)
    val PrimaryBlueDark = Color(0xFF3B5CF5)
    val PrimaryBlue = Color(0xFF4E7CFB)
    val PrimaryBlueLight = Color(0xFF6FA1FF)

    // Step / accent colors
    val Green = Color(0xFF22B14C)
    val GreenLight = Color(0xFF2ECC71)
    val Orange = Color(0xFFF7941D)
    val OrangeLight = Color(0xFFFFB74D)
    val Purple = Color(0xFF9B4DE0)
    val PurpleLight = Color(0xFFB266E8)

    // Neutral backgrounds and text
    val Background = Color(0xFFF5F6FA)
    val CardWhite = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF1E2233)
    val TextGrey = Color(0xFF8A8FA3)
    val BorderGrey = Color(0xFFE3E6F0)
}

object AppGradients {
    val BlueHeader = Brush.linearGradient(
        colors = listOf(AppColors.PrimaryBlueDark, AppColors.PrimaryBlueLight)
    )

    val BlueBar = Brush.horizontalGradient(
        colors = listOf(AppColors.PrimaryBlueDark, AppColors.PrimaryBlue)
    )

    val GreenBar = Brush.horizontalGradient(
        colors = listOf(Color(0xFF16A34A), AppColors.Green)
    )

    val OrangeBar = Brush.horizontalGradient(
        colors = listOf(Color(0xFFEA7C0C), AppColors.Orange)
    )

    val PurpleBar = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8B2FD1), AppColors.Purple)
    )
}
