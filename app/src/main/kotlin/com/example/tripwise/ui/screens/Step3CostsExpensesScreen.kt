package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.ui.components.StepHeader
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel

@Composable
fun Step3CostsExpensesScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()

    var fuelPriceText by remember { mutableStateOf(tripData.fuelPrice.toInt().toString()) }
    var tollCostText by remember { mutableStateOf(tripData.tollCost.toInt().toString()) }
    var parkingCostText by remember { mutableStateOf(tripData.parkingCost.toInt().toString()) }
    var foodOthersText by remember { mutableStateOf(tripData.foodOthers.toInt().toString()) }

    BackHandler {
        viewModel.navigateBack()
    }

    fun onNext() {
        val fuel = fuelPriceText.toDoubleOrNull() ?: tripData.fuelPrice
        val toll = tollCostText.toDoubleOrNull() ?: tripData.tollCost
        val parking = parkingCostText.toDoubleOrNull() ?: tripData.parkingCost
        val food = foodOthersText.toDoubleOrNull() ?: tripData.foodOthers

        viewModel.updateCosts(
            fuelPrice = fuel,
            tollCost = toll,
            parkingCost = parking,
            foodOthers = food
        )
        viewModel.navigateTo(Screen.Step4Summary)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("step_3_costs_screen"),
        containerColor = AppColors.Background,
        topBar = {
            StepHeader(
                color = AppColors.Orange,
                activeStep = 3,
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
                        .testTag("step3_back_button"),
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
                    onClick = { onNext() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("step3_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Orange),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.btn_next),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
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
                text = "Costs & Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Orange
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Add fuel price, toll, parking and other costs",
                color = AppColors.TextGrey,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            CostInputField(
                label = "Fuel Price (৳/L)",
                value = fuelPriceText,
                onValueChange = { fuelPriceText = it },
                testTag = "input_fuel_price"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CostInputField(
                label = "Toll Cost (৳)",
                value = tollCostText,
                onValueChange = { tollCostText = it },
                testTag = "input_toll_cost"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CostInputField(
                label = "Parking Cost (৳)",
                value = parkingCostText,
                onValueChange = { parkingCostText = it },
                testTag = "input_parking_cost"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CostInputField(
                label = "Food & Others (৳)",
                value = foodOthersText,
                onValueChange = { foodOthersText = it },
                testTag = "input_food_cost"
            )
        }
    }
}

@Composable
private fun CostInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AppColors.TextGrey,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = AppColors.Orange,
                unfocusedBorderColor = AppColors.BorderGrey,
                focusedTextColor = AppColors.TextDark,
                unfocusedTextColor = AppColors.TextDark
            ),
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}
