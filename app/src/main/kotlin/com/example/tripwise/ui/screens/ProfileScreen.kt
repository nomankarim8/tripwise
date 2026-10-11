package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.components.TripWiseBottomNav
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.ui.theme.AppGradients
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel

@Composable
fun ProfileScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        containerColor = AppColors.Background,
        bottomBar = {
            TripWiseBottomNav(
                currentIndex = 3,
                onTabSelected = { tab -> viewModel.selectTab(tab) },
                onPlanTrip = { viewModel.navigateTo(Screen.PlanOverview) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppGradients.BlueHeader)
                    .statusBarsPadding()
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = AppColors.PrimaryBlueDark,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = user.email,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = user.phone,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.navigateTo(Screen.EditProfile) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("edit_profile_btn")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Bio / Info
                if (user.bio.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "\"${user.bio}\"",
                            fontSize = 13.sp,
                            color = AppColors.TextDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                Text(
                    text = "My Activity & Orders",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        InteractiveProfileRow(
                            icon = Icons.Default.BookOnline,
                            iconTint = AppColors.PrimaryBlueDark,
                            title = "My Orders & Bookings",
                            subtitle = "View highway passes & tour bookings",
                            onClick = { viewModel.navigateTo(Screen.OrderHistory) },
                            testTag = "profile_orders"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = AppColors.BorderGrey)

                        InteractiveProfileRow(
                            icon = Icons.Default.Favorite,
                            iconTint = Color(0xFFE91E63),
                            title = "Wishlist & Favorites",
                            subtitle = "Saved rental cars & tour destinations",
                            onClick = { viewModel.navigateTo(Screen.Favorites) },
                            testTag = "profile_wishlist"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = AppColors.BorderGrey)

                        InteractiveProfileRow(
                            icon = Icons.Default.Notifications,
                            iconTint = AppColors.Orange,
                            title = "Notifications & Alerts",
                            subtitle = "Route changes & toll updates",
                            onClick = { viewModel.navigateTo(Screen.Notifications) },
                            testTag = "profile_notifications"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Preferences & Security",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        InteractiveProfileRow(
                            icon = Icons.Default.Settings,
                            iconTint = AppColors.PrimaryBlueDark,
                            title = "App Settings",
                            subtitle = "Dark mode, language (বাংলা/EN), currency",
                            onClick = { viewModel.navigateTo(Screen.Settings) },
                            testTag = "profile_settings"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = AppColors.BorderGrey)

                        InteractiveProfileRow(
                            icon = Icons.Default.PhoneInTalk,
                            iconTint = Color(0xFFD32F2F),
                            title = "Emergency Contacts & SOS",
                            subtitle = "Highway rescue & trusted contacts",
                            onClick = { viewModel.navigateTo(Screen.EmergencyContacts) },
                            testTag = "profile_emergency"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = AppColors.BorderGrey)

                        InteractiveProfileRow(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            iconTint = Color(0xFFE53935),
                            title = "Sign Out / Switch Account",
                            subtitle = "Currently logged in as ${user.email}",
                            onClick = { viewModel.logout() },
                            testTag = "profile_logout"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InteractiveProfileRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.TextDark
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = AppColors.TextGrey
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = AppColors.TextGrey,
            modifier = Modifier.size(14.dp)
        )
    }
}
