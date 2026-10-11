package com.example.tripwise.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.ui.components.FeatureCard
import com.example.tripwise.ui.components.HeaderBanner
import com.example.tripwise.ui.components.TripWiseBottomNav
import com.example.tripwise.ui.components.TripWiseDrawer
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.ui.theme.AppGradients
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { msg ->
            if (msg != null) {
                snackbarHostState.showSnackbar(msg)
                viewModel.clearSnackbar()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            TripWiseDrawer(
                currentRoute = "home",
                onNavigateHome = { viewModel.navigateTo(Screen.Home) },
                onNavigatePlan = { viewModel.navigateTo(Screen.PlanOverview) },
                onNavigateCompare = { viewModel.navigateTo(Screen.CompareVehicles) },
                onNavigateHistory = { viewModel.navigateTo(Screen.History) },
                onNavigateAnalytics = { viewModel.navigateTo(Screen.Analytics) },
                onNavigateMarketplace = { viewModel.navigateTo(Screen.Marketplace) },
                onNavigateRouteMap = { viewModel.navigateTo(Screen.RouteMap) },
                onNavigateQrTicket = { viewModel.navigateTo(Screen.QrTicket) },
                onNavigateVideoGuide = { viewModel.navigateTo(Screen.VideoGuide) },
                onNavigateEmergency = { viewModel.navigateTo(Screen.EmergencyContacts) },
                onNavigateSettings = { viewModel.navigateTo(Screen.Settings) },
                onNavigateNotifications = { viewModel.navigateTo(Screen.Notifications) },
                onLogout = { viewModel.logout() },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("home_screen"),
            containerColor = AppColors.Background,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                TripWiseBottomNav(
                    currentIndex = 0,
                    onTabSelected = { tab -> viewModel.selectTab(tab) },
                    onPlanTrip = { viewModel.navigateTo(Screen.PlanOverview) }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                item {
                    HeaderBanner(
                        onMenuTap = { scope.launch { drawerState.open() } },
                        onNotificationsTap = { viewModel.navigateTo(Screen.Notifications) }
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.greeting_hello),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.greeting_sub),
                            color = AppColors.TextGrey,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Row 1: Plan New Trip & Compare Vehicles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            FeatureCard(
                                gradient = AppGradients.BlueBar,
                                icon = Icons.Default.Route,
                                title = "Plan New Trip",
                                subtitle = "Estimate trip cost step by step",
                                onTap = { viewModel.navigateTo(Screen.PlanOverview) },
                                modifier = Modifier.weight(1f)
                            )

                            FeatureCard(
                                gradient = AppGradients.GreenBar,
                                icon = Icons.Default.DirectionsCar,
                                title = "Compare Vehicles",
                                subtitle = "Find the best & cheapest option",
                                onTap = { viewModel.navigateTo(Screen.CompareVehicles) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Row 2: Trip History & Analytics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            FeatureCard(
                                gradient = AppGradients.OrangeBar,
                                icon = Icons.Default.History,
                                title = "Trip History",
                                subtitle = "View your previous trips",
                                onTap = { viewModel.navigateTo(Screen.History) },
                                modifier = Modifier.weight(1f)
                            )

                            FeatureCard(
                                gradient = AppGradients.PurpleBar,
                                icon = Icons.Default.BarChart,
                                title = "Analytics",
                                subtitle = "Track your travel insights",
                                onTap = { viewModel.navigateTo(Screen.Analytics) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(26.dp))

                        // Quick Travel Tools & Smart Utilities
                        Text(
                            text = "Smart Travel Utilities",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextDark
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            UtilityListTile(
                                icon = Icons.Default.ShoppingBag,
                                iconBg = Color(0xFFE8F5E9),
                                iconTint = AppColors.Green,
                                title = "Travel Marketplace",
                                subtitle = "Rentals, tour packages & highway passes",
                                tag = "Book & Buy",
                                onClick = { viewModel.navigateTo(Screen.Marketplace) },
                                testTag = "home_utility_marketplace"
                            )

                            UtilityListTile(
                                icon = Icons.Default.Map,
                                iconBg = Color(0xFFE3F2FD),
                                iconTint = AppColors.PrimaryBlueDark,
                                title = "Interactive Route & Toll Map",
                                subtitle = "Live waypoint navigation, toll counters & fuel stops",
                                tag = "Live GPS",
                                onClick = { viewModel.navigateTo(Screen.RouteMap) },
                                testTag = "home_utility_routemap"
                            )

                            UtilityListTile(
                                icon = Icons.Default.QrCode,
                                iconBg = Color(0xFFFFF3E0),
                                iconTint = AppColors.Orange,
                                title = "QR Boarding Pass & Toll Ticket",
                                subtitle = "Scan-ready digital pass for express toll lanes",
                                tag = "Fast Pass",
                                onClick = { viewModel.navigateTo(Screen.QrTicket) },
                                testTag = "home_utility_qrticket"
                            )

                            UtilityListTile(
                                icon = Icons.Default.OndemandVideo,
                                iconBg = Color(0xFFF3E5F5),
                                iconTint = AppColors.Purple,
                                title = "Padma Bridge & Express Guide",
                                subtitle = "Speed camera rules, emergency lanes & video walkthrough",
                                tag = "Video Guide",
                                onClick = { viewModel.navigateTo(Screen.VideoGuide) },
                                testTag = "home_utility_videoguide"
                            )

                            UtilityListTile(
                                icon = Icons.Default.PhoneInTalk,
                                iconBg = Color(0xFFFFEBEE),
                                iconTint = Color(0xFFD32F2F),
                                title = "Emergency SOS & Highway Helpline",
                                subtitle = "Direct speed-dial to 999, highway police & wrecker support",
                                tag = "24/7 SOS",
                                onClick = { viewModel.navigateTo(Screen.EmergencyContacts) },
                                testTag = "home_utility_emergency"
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun UtilityListTile(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    tag: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
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

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(iconBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = iconTint
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = AppColors.TextGrey,
                    maxLines = 1
                )
            }
        }
    }
}
