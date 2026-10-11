package com.example.tripwise.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel

data class OnboardingPageData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun OnboardingScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPageData(
            title = "Plan Trips Smartly",
            description = "Calculate exact fuel needs, toll booth tariffs, parking fees, and meal budgets with automated calculations.",
            icon = Icons.Default.Savings,
            accentColor = AppColors.PrimaryBlue
        ),
        OnboardingPageData(
            title = "Compare All Vehicles",
            description = "Compare fuel burn between Sedan Cars, Motorbikes, and CNG Autos side-by-side to find the most economical option.",
            icon = Icons.Default.DirectionsCar,
            accentColor = AppColors.Green
        ),
        OnboardingPageData(
            title = "Travel Deals & Navigation",
            description = "Book express highway toll passes, rental vehicles, and access real-time route waypoints and QR boarding tickets.",
            icon = Icons.Default.Navigation,
            accentColor = AppColors.Purple
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(24.dp)
            .testTag("onboarding_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Skip Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Skip",
                color = AppColors.TextGrey,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { viewModel.completeOnboarding() }
                    .padding(8.dp)
                    .testTag("onboarding_skip_button")
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Center Visual Asset / Icon
        val page = pages[currentPage]
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(page.accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = page.title,
                tint = page.accentColor,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Title & Description
        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            fontSize = 14.sp,
            color = AppColors.TextGrey,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Page Indicator Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in pages.indices) {
                val isSelected = i == currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(if (isSelected) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) page.accentColor else AppColors.BorderGrey)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom Action Button
        Button(
            onClick = {
                if (currentPage < pages.lastIndex) {
                    currentPage++
                } else {
                    viewModel.completeOnboarding()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_next_button"),
            colors = ButtonDefaults.buttonColors(containerColor = page.accentColor),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (currentPage == pages.lastIndex) "Get Started" else "Next",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
