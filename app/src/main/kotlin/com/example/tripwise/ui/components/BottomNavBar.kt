package com.example.tripwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.theme.AppColors

@Composable
fun TripWiseBottomNav(
    currentIndex: Int,
    onTabSelected: (Int) -> Unit,
    onPlanTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, spotColor = Color(0x1F000000)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentIndex == 0,
                onTap = { onTabSelected(0) },
                testTag = "nav_home"
            )

            NavItem(
                icon = Icons.Default.History,
                label = "History",
                selected = currentIndex == 1,
                onTap = { onTabSelected(1) },
                testTag = "nav_history"
            )

            // Center Floating Action Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape, spotColor = AppColors.PrimaryBlueDark)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryBlue)
                    .clickable(onClick = onPlanTrip)
                    .testTag("nav_plan_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plan New Trip",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            NavItem(
                icon = Icons.Default.BarChart,
                label = "Analytics",
                selected = currentIndex == 2,
                onTap = { onTabSelected(2) },
                testTag = "nav_analytics"
            )

            NavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentIndex == 3,
                onTap = { onTabSelected(3) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onTap: () -> Unit,
    testTag: String
) {
    val color = if (selected) AppColors.PrimaryBlue else AppColors.TextGrey
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = color,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}
