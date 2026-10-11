package com.example.tripwise.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.model.CartItem
import com.example.tripwise.model.TravelItem
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MarketplaceScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.travelItems.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    val categories = listOf("All", "Rental Cars", "Bike Tours", "Toll Passes", "Tour Packages", "Hotels", "CNG Express")

    val filteredItems = items.filter { item ->
        val matchesCategory = selectedCategory == null || selectedCategory == "All" || item.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("marketplace_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Text(
                        text = "Travel Marketplace",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    // Cart Icon with Badge
                    IconButton(onClick = { viewModel.navigateTo(Screen.Cart) }) {
                        BadgedBox(
                            badge = {
                                if (cart.isNotEmpty()) {
                                    Badge { Text("${cart.sumOf { it.quantity }}") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search packages, cars, passes...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppColors.TextGrey) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("marketplace_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
            }

            // Categories horizontal filter row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == null && cat == "All") || selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategoryFilter(if (cat == "All") null else cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppColors.PrimaryBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Item count label
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Available Offers (${filteredItems.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark
                    )
                }
            }

            // Items list
            items(filteredItems, key = { it.id }) { travelItem ->
                TravelItemCard(
                    item = travelItem,
                    isFavorite = favoriteIds.contains(travelItem.id),
                    onTap = { viewModel.navigateTo(Screen.ProductDetail(travelItem.id)) },
                    onAddToCart = { viewModel.addToCart(travelItem) },
                    onToggleFavorite = { viewModel.toggleFavorite(travelItem.id) }
                )
            }
        }
    }
}

@Composable
fun TravelItemCard(
    item: TravelItem,
    isFavorite: Boolean,
    onTap: () -> Unit,
    onAddToCart: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
            .testTag("travel_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.category.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else AppColors.TextGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.description,
                fontSize = 12.sp,
                color = AppColors.TextGrey,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${item.rating} (${item.reviewsCount})", fontSize = 12.sp, color = AppColors.TextDark, fontWeight = FontWeight.SemiBold)
                }

                Text(
                    text = "৳ ${NumberFormat.getNumberInstance(Locale.US).format(item.price)}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Green
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Booking Cart", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    itemId: String,
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.travelItems.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val item = items.find { it.id == itemId } ?: items.first()
    val isFavorite = favoriteIds.contains(item.id)

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("product_detail_screen"),
        containerColor = AppColors.Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.PrimaryBlue)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.toggleFavorite(item.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.addToCart(item) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add to Cart", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.addToCart(item)
                            viewModel.navigateTo(Screen.Cart)
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book Now", fontWeight = FontWeight.Bold, color = Color.White)
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
            // Hero card banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppColors.PrimaryBlueDark),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Luggage,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.category,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = item.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${item.rating} • ${item.reviewsCount} Traveler Reviews", fontSize = 13.sp, color = AppColors.TextGrey)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "৳ ${NumberFormat.getNumberInstance(Locale.US).format(item.price)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Green
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Description", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            Text(item.description, fontSize = 14.sp, color = AppColors.TextGrey, lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Features Included", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            item.features.forEach { feat ->
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.Green, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(feat, fontSize = 13.sp, color = AppColors.TextDark)
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val cart by viewModel.cart.collectAsState()

    BackHandler {
        viewModel.navigateBack()
    }

    val totalCost = cart.sumOf { it.item.price * it.quantity }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("cart_screen"),
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
                    Text("Booking Cart", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", fontSize = 12.sp, color = AppColors.TextGrey)
                            Text(
                                "৳ ${NumberFormat.getNumberInstance(Locale.US).format(totalCost)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Purple
                            )
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.Checkout) },
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cart.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AppColors.TextGrey, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your cart is empty", fontSize = 16.sp, color = AppColors.TextGrey, fontWeight = FontWeight.Medium)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cart, key = { it.item.id }) { item ->
                    CartItemCard(
                        cartItem = item,
                        onIncrease = { viewModel.updateCartQuantity(item.item.id, item.quantity + 1) },
                        onDecrease = { viewModel.updateCartQuantity(item.item.id, item.quantity - 1) },
                        onDelete = { viewModel.removeFromCart(item.item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AppColors.TextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text("৳ ${NumberFormat.getNumberInstance(Locale.US).format(cartItem.item.price)} each", fontSize = 12.sp, color = AppColors.TextGrey)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AppColors.TextDark)
                }
                Text("${cartItem.quantity}", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 6.dp))
                IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = AppColors.TextDark)
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    var passengerName by remember { mutableStateOf("Noman Karim") }
    var phone by remember { mutableStateOf("+880 1712-345678") }
    var departureDate by remember { mutableStateOf("15 Oct 2026") }
    var selectedPayment by remember { mutableStateOf("bKash") }
    val cart by viewModel.cart.collectAsState()
    val totalCost = cart.sumOf { it.item.price * it.quantity }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("checkout_screen"),
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
                    Text("Checkout & Booking", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.checkout(selectedPayment) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm Order (৳ ${NumberFormat.getNumberInstance(Locale.US).format(totalCost)})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
            Text("Passenger Details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = passengerName,
                onValueChange = { passengerName = it },
                label = { Text("Passenger Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Contact Phone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = departureDate,
                onValueChange = { departureDate = it },
                label = { Text("Trip / Travel Date") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Payment Method", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColors.TextDark)
            Spacer(modifier = Modifier.height(10.dp))

            val methods = listOf("bKash", "Nagad", "Credit / Debit Card", "Cash on Trip")
            methods.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPayment = method }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPayment == method,
                        onClick = { selectedPayment = method }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(method, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AppColors.TextDark)
                }
            }
        }
    }
}

@Composable
fun OrderHistoryScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("order_history_screen"),
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
                    Text("Order & Booking History", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders, key = { it.orderId }) { order ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(order.orderId, fontWeight = FontWeight.Bold, color = AppColors.PrimaryBlue)
                            Text(
                                order.status,
                                color = if (order.status == "Confirmed") AppColors.Green else AppColors.TextGrey,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(order.title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = AppColors.TextDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${order.date} • ${order.paymentMethod}", fontSize = 12.sp, color = AppColors.TextGrey)
                            Text("৳ ${NumberFormat.getNumberInstance(Locale.US).format(order.totalAmount)}", fontWeight = FontWeight.Bold, color = AppColors.Purple)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    viewModel: TripViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.travelItems.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val favoriteItems = items.filter { favoriteIds.contains(it.id) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("favorites_screen"),
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
                    Text("My Favorites & Wishlist", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        if (favoriteItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No items added to favorites yet ❤️", color = AppColors.TextGrey)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(favoriteItems, key = { it.id }) { item ->
                    TravelItemCard(
                        item = item,
                        isFavorite = true,
                        onTap = { viewModel.navigateTo(Screen.ProductDetail(item.id)) },
                        onAddToCart = { viewModel.addToCart(item) },
                        onToggleFavorite = { viewModel.toggleFavorite(item.id) }
                    )
                }
            }
        }
    }
}
