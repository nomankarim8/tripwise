package com.example.tripwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.ui.theme.AppGradients

@Composable
fun TripWiseDrawer(
    currentRoute: String,
    onNavigateHome: () -> Unit,
    onNavigatePlan: () -> Unit,
    onNavigateCompare: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateAnalytics: () -> Unit,
    onNavigateMarketplace: () -> Unit = {},
    onNavigateRouteMap: () -> Unit = {},
    onNavigateQrTicket: () -> Unit = {},
    onNavigateVideoGuide: () -> Unit = {},
    onNavigateEmergency: () -> Unit = {},
    onNavigateSettings: () -> Unit = {},
    onNavigateNotifications: () -> Unit = {},
    onLogout: () -> Unit = {},
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(300.dp),
        drawerContainerColor = AppColors.CardWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
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
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "TripWise Logo",
                            tint = AppColors.PrimaryBlueDark,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "TripWise",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Smart Trip Cost Planner",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Navigation
            DrawerMenuItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == "home",
                onTap = {
                    onCloseDrawer()
                    onNavigateHome()
                },
                testTag = "drawer_item_home"
            )

            DrawerMenuItem(
                icon = Icons.Default.Route,
                label = "Plan New Trip",
                selected = currentRoute == "plan",
                onTap = {
                    onCloseDrawer()
                    onNavigatePlan()
                },
                testTag = "drawer_item_plan"
            )

            DrawerMenuItem(
                icon = Icons.Default.DirectionsCar,
                label = "Compare Vehicles",
                selected = currentRoute == "compare",
                onTap = {
                    onCloseDrawer()
                    onNavigateCompare()
                },
                testTag = "drawer_item_compare"
            )

            DrawerMenuItem(
                icon = Icons.Default.History,
                label = "Trip History",
                selected = currentRoute == "history",
                onTap = {
                    onCloseDrawer()
                    onNavigateHistory()
                },
                testTag = "drawer_item_history"
            )

            DrawerMenuItem(
                icon = Icons.Default.BarChart,
                label = "Analytics",
                selected = currentRoute == "analytics",
                onTap = {
                    onCloseDrawer()
                    onNavigateAnalytics()
                },
                testTag = "drawer_item_analytics"
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                color = AppColors.BorderGrey
            )

            // Features & Utilities
            DrawerMenuItem(
                icon = Icons.Default.ShoppingBag,
                label = "Travel Marketplace",
                onTap = {
                    onCloseDrawer()
                    onNavigateMarketplace()
                },
                testTag = "drawer_item_marketplace"
            )

            DrawerMenuItem(
                icon = Icons.Default.Map,
                label = "Route & Toll Map",
                onTap = {
                    onCloseDrawer()
                    onNavigateRouteMap()
                },
                testTag = "drawer_item_routemap"
            )

            DrawerMenuItem(
                icon = Icons.Default.QrCode,
                label = "QR Boarding Pass",
                onTap = {
                    onCloseDrawer()
                    onNavigateQrTicket()
                },
                testTag = "drawer_item_qrticket"
            )

            DrawerMenuItem(
                icon = Icons.Default.OndemandVideo,
                label = "Padma Bridge Guide",
                onTap = {
                    onCloseDrawer()
                    onNavigateVideoGuide()
                },
                testTag = "drawer_item_videoguide"
            )

            DrawerMenuItem(
                icon = Icons.Default.PhoneInTalk,
                label = "Emergency SOS",
                onTap = {
                    onCloseDrawer()
                    onNavigateEmergency()
                },
                testTag = "drawer_item_emergency"
            )

            DrawerMenuItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                onTap = {
                    onCloseDrawer()
                    onNavigateNotifications()
                },
                testTag = "drawer_item_notifications"
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                color = AppColors.BorderGrey
            )

            DrawerMenuItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onTap = {
                    onCloseDrawer()
                    onNavigateSettings()
                },
                testTag = "drawer_item_settings"
            )

            DrawerMenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                label = "Logout / Switch",
                iconColor = Color(0xFFE53935),
                textColor = Color(0xFFE53935),
                onTap = {
                    onCloseDrawer()
                    onLogout()
                },
                testTag = "drawer_item_logout"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onTap: () -> Unit,
    testTag: String,
    selected: Boolean = false,
    iconColor: Color? = null,
    textColor: Color? = null
) {
    val effectiveBg = if (selected) AppColors.PrimaryBlue.copy(alpha = 0.1f) else Color.Transparent
    val effectiveIconColor = iconColor ?: if (selected) AppColors.PrimaryBlueDark else AppColors.TextGrey
    val effectiveTextColor = textColor ?: if (selected) AppColors.PrimaryBlueDark else AppColors.TextDark

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(effectiveBg)
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 11.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = effectiveIconColor,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                color = effectiveTextColor,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
