package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.model.EmergencyContact
import com.example.tripwise.model.TripHistoryItem
import com.example.tripwise.model.VehicleType
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import kotlinx.coroutines.delay

@Composable
fun RouteMapScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()
    var isLiveTracking by remember { mutableStateOf(true) }
    var speedKmH by remember { mutableIntStateOf(68) }
    var currentProgress by remember { mutableFloatStateOf(0.42f) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    LaunchedEffect(isLiveTracking) {
        while (isLiveTracking) {
            delay(1200)
            currentProgress = (currentProgress + 0.02f) % 1.0f
            speedKmH = (60..85).random()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("route_map_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text("Live Route Navigation", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Simulated Map Canvas View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE8ECEF))
                    .border(1.dp, AppColors.BorderGrey, RoundedCornerShape(20.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid streets
                    drawLine(Color(0xFFD1D8E0), Offset(0f, h * 0.3f), Offset(w, h * 0.3f), strokeWidth = 4f)
                    drawLine(Color(0xFFD1D8E0), Offset(0f, h * 0.7f), Offset(w, h * 0.7f), strokeWidth = 4f)
                    drawLine(Color(0xFFD1D8E0), Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), strokeWidth = 4f)
                    drawLine(Color(0xFFD1D8E0), Offset(w * 0.7f, 0f), Offset(w * 0.7f, h), strokeWidth = 4f)

                    // Curving Highway Route Path
                    val routePath = Path().apply {
                        moveTo(w * 0.15f, h * 0.85f)
                        cubicTo(w * 0.3f, h * 0.5f, w * 0.5f, h * 0.6f, w * 0.6f, h * 0.35f)
                        lineTo(w * 0.88f, h * 0.2f)
                    }
                    drawPath(routePath, color = Color(0xFF4E7CFB), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f))

                    // Origin pin
                    drawCircle(Color(0xFF22B14C), radius = 14f, center = Offset(w * 0.15f, h * 0.85f))
                    // Destination pin
                    drawCircle(Color(0xFFE53935), radius = 16f, center = Offset(w * 0.88f, h * 0.2f))

                    // Vehicle position on route
                    val carX = w * (0.15f + currentProgress * 0.7f)
                    val carY = h * (0.85f - currentProgress * 0.65f)
                    drawCircle(Color.White, radius = 14f, center = Offset(carX, carY))
                    drawCircle(Color(0xFF3B5CF5), radius = 9f, center = Offset(carX, carY))
                }

                // GPS Live HUD overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = Color.Green, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$speedKmH km/h", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("• GPS Active", color = Color.Green, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Waypoints & Route Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Route Itinerary", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AppColors.TextDark)
                    Spacer(modifier = Modifier.height(12.dp))

                    WaypointRow(step = "1", title = tripData.from, sub = "Origin point", isCompleted = true)
                    WaypointRow(step = "2", title = "Comilla Highway Junction", sub = "Toll Booth 1 (RFID lane)", isCompleted = true)
                    WaypointRow(step = "3", title = "Chittagong Bypass", sub = "Fuel stop & rest area", isCompleted = false)
                    WaypointRow(step = "4", title = tripData.to, sub = "Destination", isCompleted = false, isLast = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { isLiveTracking = !isLiveTracking },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isLiveTracking) AppColors.Orange else AppColors.PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isLiveTracking) "Pause Simulated GPS" else "Resume Live Navigation", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun WaypointRow(
    step: String,
    title: String,
    sub: String,
    isCompleted: Boolean,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(if (isCompleted) AppColors.Green else AppColors.BorderGrey),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextDark)
            Text(sub, fontSize = 11.sp, color = AppColors.TextGrey)
        }
    }
}

@Composable
fun QrTicketScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val tripData by viewModel.currentTrip.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var scanSuccess by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("qr_ticket_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text("QR Ticket & Pass Scanner", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AppColors.PrimaryBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Trip QR Pass", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("QR Scanner", fontWeight = FontWeight.Bold) }
                )
            }

            if (selectedTab == 0) {
                // QR Generator View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("TRIP BOARDING PASS", fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 12.sp, color = AppColors.PrimaryBlue)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("${tripData.from} ➔ ${tripData.to}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AppColors.TextDark, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${tripData.vehicle.label} • ${tripData.distanceKm.toInt()} KM", fontSize = 12.sp, color = AppColors.TextGrey)

                            Spacer(modifier = Modifier.height(20.dp))

                            // Simulated QR Code Graphic Canvas
                            Box(
                                modifier = Modifier
                                    .size(190.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF1F3F9))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val sizePx = size.width
                                    val blockSize = sizePx / 12f

                                    // Draw QR finder corners
                                    fun drawCorner(x: Float, y: Float) {
                                        drawRect(Color.Black, Offset(x, y), androidx.compose.ui.geometry.Size(blockSize * 3, blockSize * 3))
                                        drawRect(Color.White, Offset(x + blockSize * 0.6f, y + blockSize * 0.6f), androidx.compose.ui.geometry.Size(blockSize * 1.8f, blockSize * 1.8f))
                                        drawRect(Color.Black, Offset(x + blockSize * 1.0f, y + blockSize * 1.0f), androidx.compose.ui.geometry.Size(blockSize, blockSize))
                                    }
                                    drawCorner(0f, 0f)
                                    drawCorner(sizePx - blockSize * 3, 0f)
                                    drawCorner(0f, sizePx - blockSize * 3)

                                    // Random decorative QR blocks
                                    val pattern = listOf(
                                        Pair(4, 2), Pair(5, 2), Pair(6, 4), Pair(7, 5), Pair(8, 2),
                                        Pair(4, 5), Pair(5, 7), Pair(6, 7), Pair(8, 6), Pair(2, 6),
                                        Pair(5, 9), Pair(7, 9), Pair(8, 10), Pair(9, 8), Pair(10, 5)
                                    )
                                    pattern.forEach { (bx, by) ->
                                        drawRect(Color.Black, Offset(bx * blockSize, by * blockSize), androidx.compose.ui.geometry.Size(blockSize * 0.9f, blockSize * 0.9f))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text("PASS ID: TW-2026-9842X", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AppColors.TextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Scan at highway toll lanes and fuel stations", fontSize = 11.sp, color = AppColors.TextGrey)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.showMessage("QR Ticket downloaded to device gallery!") },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Download Pass Image / PDF")
                    }
                }
            } else {
                // QR Scanner simulation
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Align QR code within the viewfinder to scan", fontSize = 13.sp, color = AppColors.TextGrey)
                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.85f))
                            .border(2.dp, AppColors.PrimaryBlue, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = AppColors.PrimaryBlue, modifier = Modifier.size(80.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { scanSuccess = true },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simulate Camera QR Scan")
                    }

                    AnimatedVisibility(visible = scanSuccess) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = AppColors.Green.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = AppColors.Green)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pass Verified: Dhaka-Sylhet Highway Toll Access (Valid)", color = AppColors.Green, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoGuideScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableFloatStateOf(0.25f) }
    var isMuted by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            currentProgress = (currentProgress + 0.01f).coerceAtMost(1f)
            if (currentProgress >= 1f) isPlaying = false
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("video_guide_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text("Travel & Highway Video Guide", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // Simulated Video Player Canvas / Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E2233)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f))
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        if (isPlaying) "Streaming Highway Safety Guide..." else "Tap to Play Guide",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }

                // Bottom player controls bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%02d:%02d", (currentProgress * 120).toInt() / 60, (currentProgress * 120).toInt() % 60),
                            color = Color.White,
                            fontSize = 11.sp
                        )
                        Slider(
                            value = currentProgress,
                            onValueChange = { currentProgress = it },
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            colors = SliderDefaults.colors(thumbColor = AppColors.PrimaryBlue, activeTrackColor = AppColors.PrimaryBlue)
                        )
                        IconButton(onClick = { isMuted = !isMuted }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Mute", tint = if (isMuted) Color.Red else Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Padma Bridge & Cox's Bazar Express Guide", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Learn the newest speed camera regulations, emergency lanes, and toll payment tips.", fontSize = 13.sp, color = AppColors.TextGrey)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Guide Chapters", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(8.dp))

            val chapters = listOf(
                "00:00 - Departure from Dhaka via Postogola Bridge",
                "00:45 - Padma Bridge Toll Plaza automated lane pass",
                "01:20 - Fuel refueling stations on Bhanga Highway",
                "02:00 - Mountain safety driving in Chittagong-Cox's Bazar"
            )

            chapters.forEach { chap ->
                Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = AppColors.PrimaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(chap, fontSize = 12.sp, color = AppColors.TextDark)
                }
            }
        }
    }
}

@Composable
fun EmergencyContactsScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val contacts by viewModel.emergencyContacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newRole by remember { mutableStateOf("Personal Emergency") }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    if (showAddDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Emergency Contact", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Contact Name") }, singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newPhone, onValueChange = { newPhone = it }, label = { Text("Phone Number") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newName.isNotBlank() && newPhone.isNotBlank()) {
                        viewModel.addEmergencyContact(newName, newPhone, newRole)
                        showAddDialog = false
                    }
                }) { Text("Save Contact") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("emergency_contacts_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text("Emergency Contacts & Highway Help", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AppColors.PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contacts, key = { it.id }) { contact ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AppColors.Orange.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = AppColors.Orange)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AppColors.TextDark)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${contact.role} • ${contact.phone}", fontSize = 12.sp, color = AppColors.TextGrey)
                        }

                        Button(
                            onClick = { viewModel.showMessage("Calling ${contact.name} (${contact.phone})...") },
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Call", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripEditScreen(
    tripId: String,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val history by viewModel.history.collectAsState()
    val trip = history.find { it.id == tripId } ?: history.firstOrNull()

    if (trip == null) {
        viewModel.navigateBack()
        return
    }

    var fromText by remember { mutableStateOf(trip.from) }
    var toText by remember { mutableStateOf(trip.to) }
    var distanceText by remember { mutableStateOf(trip.distanceKm.toInt().toString()) }
    var passengersText by remember { mutableStateOf(trip.passengers.toString()) }
    var fuelCostText by remember { mutableStateOf(trip.fuelCost.toInt().toString()) }
    var tollCostText by remember { mutableStateOf(trip.tollCost.toInt().toString()) }
    var parkingCostText by remember { mutableStateOf(trip.parkingCost.toInt().toString()) }
    var foodCostText by remember { mutableStateOf(trip.foodOthers.toInt().toString()) }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("trip_edit_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text("Edit Trip (CRUD)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.deleteHistoryTrip(trip.id)
                        viewModel.navigateBack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Trip", tint = Color.White)
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
                .padding(20.dp)
        ) {
            OutlinedTextField(value = fromText, onValueChange = { fromText = it }, label = { Text("From") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = toText, onValueChange = { toText = it }, label = { Text("To") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = distanceText, onValueChange = { distanceText = it }, label = { Text("Distance (KM)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = passengersText, onValueChange = { passengersText = it }, label = { Text("Passengers") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = fuelCostText, onValueChange = { fuelCostText = it }, label = { Text("Fuel Cost (৳)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = tollCostText, onValueChange = { tollCostText = it }, label = { Text("Toll Cost (৳)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = parkingCostText, onValueChange = { parkingCostText = it }, label = { Text("Parking Cost (৳)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = foodCostText, onValueChange = { foodCostText = it }, label = { Text("Food Cost (৳)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val dist = distanceText.toDoubleOrNull() ?: trip.distanceKm
                    val pass = passengersText.toIntOrNull() ?: trip.passengers
                    val fCost = fuelCostText.toDoubleOrNull() ?: trip.fuelCost
                    val tCost = tollCostText.toDoubleOrNull() ?: trip.tollCost
                    val pCost = parkingCostText.toDoubleOrNull() ?: trip.parkingCost
                    val foodCost = foodCostText.toDoubleOrNull() ?: trip.foodOthers
                    val total = fCost + tCost + pCost + foodCost

                    viewModel.updateHistoryTrip(
                        trip.copy(
                            from = fromText,
                            to = toText,
                            distanceKm = dist,
                            passengers = pass,
                            fuelCost = fCost,
                            tollCost = tCost,
                            parkingCost = pCost,
                            foodOthers = foodCost,
                            totalCost = total
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
