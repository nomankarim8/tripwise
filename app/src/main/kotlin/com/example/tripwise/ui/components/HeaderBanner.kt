package com.example.tripwise.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.ui.theme.AppGradients

@Composable
fun HeaderBanner(
    onMenuTap: () -> Unit,
    onNotificationsTap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(AppGradients.BlueHeader)
    ) {
        // Clouds
        Cloud(modifier = Modifier.offset(x = 24.dp, y = 60.dp), width = 42.dp, height = 20.dp)
        Cloud(modifier = Modifier.offset(x = 76.dp, y = 42.dp), width = 58.dp, height = 28.dp)
        Cloud(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-40).dp, y = 68.dp), width = 36.dp, height = 18.dp)

        // Mountain and road illustration drawn using Canvas
        MountainRoadIllustration(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.BottomCenter)
        )

        // Vehicles on the road
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = "Car",
            tint = Color(0xFFBCE0FD),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 32.dp, y = (-18).dp)
                .size(46.dp)
        )

        Icon(
            imageVector = Icons.Default.TwoWheeler,
            contentDescription = "Motorcycle",
            tint = Color(0xFFFFCC80),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-40).dp, y = (-18).dp)
                .size(38.dp)
        )

        // Header Text, Menu & Notifications Button
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onMenuTap,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Navigation Menu",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(
                    onClick = onNotificationsTap,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("header_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TripWise",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Smart Vehicle &\nTrip Cost Planner",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun Cloud(
    modifier: Modifier = Modifier,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.35f))
    )
}

@Composable
private fun MountainRoadIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Mountains path
        val mountainPath = Path().apply {
            moveTo(0f, h)
            lineTo(0f, h * 0.45f)
            lineTo(w * 0.22f, h * 0.12f)
            lineTo(w * 0.42f, h * 0.48f)
            lineTo(w * 0.62f, h * 0.08f)
            lineTo(w * 0.84f, h * 0.42f)
            lineTo(w, h * 0.22f)
            lineTo(w, h)
            close()
        }
        drawPath(
            path = mountainPath,
            color = Color(0xFF3F51B5).copy(alpha = 0.35f)
        )

        // Road strip
        val roadHeight = 26f
        val roadTop = h - roadHeight
        drawRect(
            color = Color(0xFF37474F),
            topLeft = Offset(0f, roadTop),
            size = Size(w, roadHeight)
        )

        // Dashed centerline
        var currentX = 0f
        val dashWidth = 16f
        val dashGap = 16f
        val lineY = roadTop + roadHeight / 2f
        while (currentX < w) {
            drawLine(
                color = Color.White,
                start = Offset(currentX, lineY),
                end = Offset(currentX + dashWidth, lineY),
                strokeWidth = 3f
            )
            currentX += dashWidth + dashGap
        }
    }
}
