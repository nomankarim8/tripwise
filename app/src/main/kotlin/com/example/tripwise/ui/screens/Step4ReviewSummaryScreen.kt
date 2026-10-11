package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.ui.components.StepHeader
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.TripViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Step4ReviewSummaryScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()

    BackHandler {
        viewModel.navigateBack()
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
            .testTag("step_4_summary_screen"),
        containerColor = AppColors.Background,
        topBar = {
            StepHeader(
                color = AppColors.Purple,
                activeStep = 4,
                onBack = { viewModel.navigateBack() }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("step4_back_button"),
                    border = BorderStroke(1.dp, AppColors.BorderGrey),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_back),
                        color = AppColors.TextDark,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Button(
                    onClick = { viewModel.finishAndSaveTrip() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("step4_finish_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Purple),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.btn_finish),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Review & Summary",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Purple
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Review your trip details and see total cost",
                color = AppColors.TextGrey,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // White Details Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(width = 1.dp, color = AppColors.BorderGrey, shape = RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    // From - To row
                    SummaryItem(
                        label = "From - To",
                        valueWidget = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = tripData.from.split(",").first().trim(),
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppColors.TextDark,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
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
                                    text = tripData.to.split(",").first().trim(),
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppColors.TextDark,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )

                    SummaryItem(
                        label = "Distance",
                        value = "${tripData.distanceKm.toInt()} KM"
                    )

                    SummaryItem(
                        label = "Vehicle",
                        value = tripData.vehicle.label
                    )

                    SummaryItem(
                        label = "Passengers",
                        value = "${tripData.passengers} People"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = AppColors.BorderGrey
                    )

                    SummaryItem(
                        label = "Fuel Cost",
                        value = formatMoney(tripData.fuelCost)
                    )

                    SummaryItem(
                        label = "Toll Cost",
                        value = formatMoney(tripData.tollCost)
                    )

                    SummaryItem(
                        label = "Parking Cost",
                        value = formatMoney(tripData.parkingCost)
                    )

                    SummaryItem(
                        label = "Food & Others",
                        value = formatMoney(tripData.foodOthers)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Cost Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.Purple.copy(alpha = 0.08f))
                    .border(
                        width = 1.dp,
                        color = AppColors.Purple.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 18.dp)
                    .testTag("summary_total_cost_box")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Trip Cost",
                            color = AppColors.Purple,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Per person: ${formatMoney(tripData.costPerPassenger)}",
                            color = AppColors.TextGrey,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = formatMoney(tripData.totalCost),
                        color = AppColors.Purple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String? = null,
    valueWidget: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AppColors.TextGrey,
            fontSize = 13.sp
        )

        valueWidget?.invoke() ?: Text(
            text = value ?: "",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = AppColors.TextDark
        )
    }
}
