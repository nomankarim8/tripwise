package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
fun Step1TripDetailsScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()

    var fromText by remember { mutableStateOf(tripData.from) }
    var toText by remember { mutableStateOf(tripData.to) }
    var distanceText by remember { mutableStateOf(tripData.distanceKm.toInt().toString()) }
    var passengersText by remember { mutableStateOf(tripData.passengers.toString()) }

    BackHandler {
        viewModel.navigateBack()
    }

    fun onNext() {
        val dist = distanceText.toDoubleOrNull() ?: tripData.distanceKm
        val pass = passengersText.toIntOrNull() ?: tripData.passengers
        viewModel.updateTripDetails(
            from = fromText.trim(),
            to = toText.trim(),
            distanceKm = dist,
            passengers = pass
        )
        viewModel.navigateTo(Screen.Step2Vehicle)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("step_1_details_screen"),
        containerColor = AppColors.Background,
        topBar = {
            StepHeader(
                color = AppColors.PrimaryBlue,
                activeStep = 1,
                onBack = { viewModel.navigateBack() }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = { onNext() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("step1_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
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
                text = "Trip Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Enter basic information about your trip",
                color = AppColors.TextGrey,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            // From Field
            LabeledInputField(
                label = "From",
                value = fromText,
                onValueChange = { fromText = it },
                testTag = "input_from",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "From Location",
                        tint = AppColors.TextGrey
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // To Field
            LabeledInputField(
                label = "To",
                value = toText,
                onValueChange = { toText = it },
                testTag = "input_to",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "To Location",
                        tint = AppColors.TextGrey
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Distance Field
            LabeledInputField(
                label = "Distance (KM)",
                value = distanceText,
                onValueChange = { distanceText = it },
                testTag = "input_distance",
                suffix = "KM",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Passengers Field
            LabeledInputField(
                label = "Passengers",
                value = passengersText,
                onValueChange = { passengersText = it },
                testTag = "input_passengers",
                suffix = "People",
                keyboardType = KeyboardType.Number
            )
        }
    }
}

@Composable
fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
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
                focusedBorderColor = AppColors.PrimaryBlue,
                unfocusedBorderColor = AppColors.BorderGrey,
                focusedTextColor = AppColors.TextDark,
                unfocusedTextColor = AppColors.TextDark
            ),
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        color = AppColors.TextGrey,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        )
    }
}
