package com.example.tripwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(
    activeStep: Int,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (stepNum in 1..4) {
            val isDone = stepNum < activeStep
            val isActive = stepNum == activeStep

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = if (isActive || isDone) Color.White else Color.White.copy(alpha = 0.24f),
                        shape = CircleShape
                    )
                    .then(
                        if (isActive) {
                            Modifier.border(width = 2.dp, color = Color.White, shape = CircleShape)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = activeColor,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "$stepNum",
                        color = if (isActive) activeColor else Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            if (stepNum != 4) {
                Box(
                    modifier = Modifier
                        .width(34.dp)
                        .height(2.dp)
                        .background(Color.White.copy(alpha = 0.38f))
                )
            }
        }
    }
}
