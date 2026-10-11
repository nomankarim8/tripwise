package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.components.TripWiseBottomNav
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val historyList by viewModel.history.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    val totalTrips = historyList.size
    val totalDistance = historyList.sumOf { it.distanceKm }
    val totalCost = historyList.sumOf { it.totalCost }
    val totalFuel = historyList.sumOf { it.fuelCost }
    val totalToll = historyList.sumOf { it.tollCost }
    val totalParking = historyList.sumOf { it.parkingCost }
    val totalFood = historyList.sumOf { it.foodOthers }

    val avgCostPerKm = if (totalDistance > 0) totalCost / totalDistance else 0.0

    fun formatMoney(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }
        return "৳ ${formatter.format(amount)}"
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.Purple)
                    .statusBarsPadding()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "Travel Analytics",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            TripWiseBottomNav(
                currentIndex = 2,
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
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Trips",
                    value = "$totalTrips",
                    color = AppColors.PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Distance",
                    value = "${totalDistance.toInt()} km",
                    color = AppColors.Green,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Total Cost",
                    value = formatMoney(totalCost),
                    color = AppColors.Purple,
                    modifier = Modifier.weight(1.2f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "Cost Distribution Breakdown",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Average cost per km: ${String.format(Locale.US, "%.1f", avgCostPerKm)} ৳/km",
                        fontSize = 12.sp,
                        color = AppColors.TextGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val grandTotal = if (totalCost > 0) totalCost else 1.0

                    CostBarItem(
                        label = "Fuel Expense",
                        amount = formatMoney(totalFuel),
                        percent = (totalFuel / grandTotal).toFloat(),
                        color = AppColors.PrimaryBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CostBarItem(
                        label = "Tolls & Bridges",
                        amount = formatMoney(totalToll),
                        percent = (totalToll / grandTotal).toFloat(),
                        color = AppColors.Orange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CostBarItem(
                        label = "Parking Fees",
                        amount = formatMoney(totalParking),
                        percent = (totalParking / grandTotal).toFloat(),
                        color = AppColors.Green
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CostBarItem(
                        label = "Food & Incidentals",
                        amount = formatMoney(totalFood),
                        percent = (totalFood / grandTotal).toFloat(),
                        color = AppColors.Purple
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(text = title, fontSize = 11.sp, color = AppColors.TextGrey)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun CostBarItem(
    label: String,
    amount: String,
    percent: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, fontSize = 13.sp, color = AppColors.TextDark, fontWeight = FontWeight.Medium)
            }
            Text(
                text = "$amount (${(percent * 100).toInt()}%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextDark
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppColors.Background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = percent.coerceIn(0.02f, 1f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}
