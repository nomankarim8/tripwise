package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.model.VehicleType
import com.example.tripwise.ui.components.StepHeader
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel

@Composable
fun Step2SelectVehicleScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()
    var selectedVehicle by remember { mutableStateOf(tripData.vehicle) }

    BackHandler {
        viewModel.navigateBack()
    }

    fun onNext() {
        viewModel.updateVehicle(selectedVehicle)
        viewModel.navigateTo(Screen.Step3Costs)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("step_2_vehicle_screen"),
        containerColor = AppColors.Background,
        topBar = {
            StepHeader(
                color = AppColors.Green,
                activeStep = 2,
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
                        .testTag("step2_back_button"),
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
                        .testTag("step2_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
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
                text = "Select Vehicle",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Green
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose your vehicle type",
                color = AppColors.TextGrey,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            VehicleSelectionCard(
                icon = Icons.Default.DirectionsCar,
                vehicle = VehicleType.CAR,
                isSelected = selectedVehicle == VehicleType.CAR,
                onTap = { selectedVehicle = VehicleType.CAR },
                testTag = "vehicle_car"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VehicleSelectionCard(
                icon = Icons.Default.TwoWheeler,
                vehicle = VehicleType.MOTORCYCLE,
                isSelected = selectedVehicle == VehicleType.MOTORCYCLE,
                onTap = { selectedVehicle = VehicleType.MOTORCYCLE },
                testTag = "vehicle_motorcycle"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VehicleSelectionCard(
                icon = Icons.Default.ElectricRickshaw,
                vehicle = VehicleType.CNG_AUTO,
                isSelected = selectedVehicle == VehicleType.CNG_AUTO,
                onTap = { selectedVehicle = VehicleType.CNG_AUTO },
                testTag = "vehicle_cng_auto"
            )
        }
    }
}

@Composable
private fun VehicleSelectionCard(
    icon: ImageVector,
    vehicle: VehicleType,
    isSelected: Boolean,
    onTap: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) AppColors.Green else AppColors.BorderGrey
    val borderWidth = if (isSelected) 1.6.dp else 1.dp
    val bgColor = if (isSelected) AppColors.Green.copy(alpha = 0.08f) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onTap)
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.Background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = vehicle.label,
                    tint = AppColors.TextDark,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehicle.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AppColors.TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = vehicle.mileageLabel,
                    color = AppColors.TextGrey,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = if (isSelected) "Selected" else "Not selected",
                tint = if (isSelected) AppColors.Green else AppColors.BorderGrey,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
