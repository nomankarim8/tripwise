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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TwoWheeler
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.model.TripHistoryItem
import com.example.tripwise.model.VehicleType
import com.example.tripwise.ui.components.TripWiseBottomNav
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TripHistoryScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val historyList by viewModel.history.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    fun formatMoney(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }
        return "৳ ${formatter.format(amount)}"
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("trip_history_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.Orange)
                    .statusBarsPadding()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "Trip History",
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
                currentIndex = 1,
                onTabSelected = { tab -> viewModel.selectTab(tab) },
                onPlanTrip = { viewModel.navigateTo(Screen.PlanOverview) }
            )
        }
    ) { innerPadding ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = AppColors.TextGrey,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No saved trips yet",
                        fontSize = 16.sp,
                        color = AppColors.TextGrey,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(historyList, key = { it.id }) { item ->
                    HistoryCard(item = item, formatMoney = ::formatMoney)
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(
    item: TripHistoryItem,
    formatMoney: (Double) -> String,
    modifier: Modifier = Modifier
) {
    val vehicleIcon = when (item.vehicle) {
        VehicleType.CAR -> Icons.Default.DirectionsCar
        VehicleType.MOTORCYCLE -> Icons.Default.TwoWheeler
        VehicleType.CNG_AUTO -> Icons.Default.ElectricRickshaw
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.from,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AppColors.TextDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AppColors.TextGrey,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.to,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AppColors.TextDark
                    )
                }

                Text(
                    text = formatMoney(item.totalCost),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColors.Purple
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = vehicleIcon,
                        contentDescription = item.vehicle.label,
                        tint = AppColors.PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.vehicle.label} • ${item.distanceKm.toInt()} KM",
                        fontSize = 12.sp,
                        color = AppColors.TextGrey
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = AppColors.TextGrey,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.date,
                        fontSize = 11.sp,
                        color = AppColors.TextGrey
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = AppColors.BorderGrey
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Fuel: ${formatMoney(item.fuelCost)}",
                    fontSize = 11.sp,
                    color = AppColors.TextGrey
                )
                Text(
                    text = "Toll: ${formatMoney(item.tollCost)}",
                    fontSize = 11.sp,
                    color = AppColors.TextGrey
                )
                Text(
                    text = "${item.passengers} Passengers",
                    fontSize = 11.sp,
                    color = AppColors.TextGrey
                )
            }
        }
    }
}
