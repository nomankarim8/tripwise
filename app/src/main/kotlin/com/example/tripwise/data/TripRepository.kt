package com.example.tripwise.data

import android.content.Context
import android.util.Log
import com.example.tripwise.R
import com.example.tripwise.model.AppSettings
import com.example.tripwise.model.BookingOrder
import com.example.tripwise.model.CartItem
import com.example.tripwise.model.EmergencyContact
import com.example.tripwise.model.NotificationItem
import com.example.tripwise.model.TravelItem
import com.example.tripwise.model.TripData
import com.example.tripwise.model.TripHistoryItem
import com.example.tripwise.model.User
import com.example.tripwise.model.VehicleType
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TripRepository(
    private val firestore: FirebaseFirestore? = null,
    private val auth: FirebaseAuth? = null
) {
    constructor(context: Context) : this(
        try {
            FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
        } catch (e: Exception) {
            Log.w("TripRepository", "Firestore initialization: ${e.message}")
            null
        },
        try {
            Firebase.auth
        } catch (e: Exception) {
            Log.w("TripRepository", "FirebaseAuth initialization: ${e.message}")
            null
        }
    )

    // Current in-progress planned trip
    private val _currentTrip = MutableStateFlow(TripData())
    val currentTrip: StateFlow<TripData> = _currentTrip.asStateFlow()

    // CRUD Trip History
    private val _history = MutableStateFlow<List<TripHistoryItem>>(
        listOf(
            TripHistoryItem(
                id = "trip_1",
                from = "Dhaka",
                to = "Cox's Bazar",
                distanceKm = 393.0,
                passengers = 4,
                vehicle = VehicleType.CAR,
                fuelCost = 3649.0,
                tollCost = 450.0,
                parkingCost = 100.0,
                foodOthers = 800.0,
                totalCost = 4999.0,
                date = "Oct 08, 2026"
            ),
            TripHistoryItem(
                id = "trip_2",
                from = "Dhaka",
                to = "Sylhet",
                distanceKm = 240.0,
                passengers = 2,
                vehicle = VehicleType.MOTORCYCLE,
                fuelCost = 780.0,
                tollCost = 150.0,
                parkingCost = 50.0,
                foodOthers = 400.0,
                totalCost = 1380.0,
                date = "Sep 28, 2026"
            ),
            TripHistoryItem(
                id = "trip_3",
                from = "Chittagong",
                to = "Rangamati",
                distanceKm = 72.0,
                passengers = 3,
                vehicle = VehicleType.CNG_AUTO,
                fuelCost = 374.0,
                tollCost = 0.0,
                parkingCost = 30.0,
                foodOthers = 250.0,
                totalCost = 654.0,
                date = "Aug 15, 2026"
            )
        )
    )
    val history: StateFlow<List<TripHistoryItem>> = _history.asStateFlow()

    // User authentication & profile state
    private val _user = MutableStateFlow(User(isLoggedIn = true))
    val user: StateFlow<User> = _user.asStateFlow()

    // Onboarding persistent state
    private val _hasCompletedOnboarding = MutableStateFlow(false)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    // App Settings (Theme, Language, etc.)
    private val _appSettings = MutableStateFlow(AppSettings())
    val appSettings: StateFlow<AppSettings> = _appSettings.asStateFlow()

    // Marketplace / Travel items
    private val _travelItems = MutableStateFlow<List<TravelItem>>(
        listOf(
            TravelItem(
                id = "pkg_1",
                name = "Cox's Bazar Beach Sedan Rental",
                category = "Rental Cars",
                price = 4500.0,
                rating = 4.8,
                reviewsCount = 124,
                description = "AC Sedan with experienced highway driver, flexible timing, fuel included up to 100km.",
                features = listOf("Unlimited Mileage Option", "Air Conditioned", "Emergency Support")
            ),
            TravelItem(
                id = "pkg_2",
                name = "Sylhet Tea Garden Bike Expedition",
                category = "Bike Tours",
                price = 1800.0,
                rating = 4.9,
                reviewsCount = 89,
                description = "150cc Cruiser motorcycle with helmet, riding gear, and scenic mountain GPS route loaded.",
                features = listOf("Dual Helmets", "Phone Mount", "Roadside Assistance")
            ),
            TravelItem(
                id = "pkg_3",
                name = "Dhaka - Padma Bridge Highway Pass",
                category = "Toll Passes",
                price = 650.0,
                rating = 4.7,
                reviewsCount = 312,
                description = "Prepaid express RFID toll lane access card for frictionless highway transit.",
                features = listOf("Zero Queue Delay", "Valid for 30 Days", "Instant SMS Alerts")
            ),
            TravelItem(
                id = "pkg_4",
                name = "Sajek Valley 4x4 Chander Gari Tour",
                category = "Tour Packages",
                price = 8500.0,
                rating = 4.9,
                reviewsCount = 205,
                description = "Complete 3-day cloud tour in Sajek with 4-wheel drive jeep and local mountain guide.",
                features = listOf("Army Escort Compliant", "Hill Trained Driver", "Hotel Pickup")
            ),
            TravelItem(
                id = "pkg_5",
                name = "Srimangal Eco Resort Weekend Stay",
                category = "Hotels",
                price = 3200.0,
                rating = 4.6,
                reviewsCount = 78,
                description = "Cozy wooden cottage near Lawachara rain forest with organic complimentary breakfast.",
                features = listOf("Free Secure Parking", "Electric Vehicle Charger", "Free Wi-Fi")
            ),
            TravelItem(
                id = "pkg_6",
                name = "Chittagong City CNG Shuttle Pass",
                category = "CNG Express",
                price = 450.0,
                rating = 4.4,
                reviewsCount = 56,
                description = "Day-long prepaid green auto-rickshaw shuttle pass between city center and Patenga beach.",
                features = listOf("Meter Guaranteed", "Fixed Fare", "Verified Drivers")
            )
        )
    )
    val travelItems: StateFlow<List<TravelItem>> = _travelItems.asStateFlow()

    // Wishlist / Favorites
    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf("pkg_1", "pkg_4"))
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // Shopping / Booking Cart
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Orders / Completed Bookings
    private val _orders = MutableStateFlow<List<BookingOrder>>(
        listOf(
            BookingOrder(
                orderId = "ORD-9481",
                title = "Dhaka - Padma Bridge Highway Pass",
                totalAmount = 650.0,
                date = "Oct 05, 2026",
                status = "Confirmed",
                paymentMethod = "bKash"
            ),
            BookingOrder(
                orderId = "ORD-8219",
                title = "Cox's Bazar Beach Sedan Rental",
                totalAmount = 4500.0,
                date = "Sep 19, 2026",
                status = "Completed",
                paymentMethod = "Credit Card"
            )
        )
    )
    val orders: StateFlow<List<BookingOrder>> = _orders.asStateFlow()

    // Notifications Center
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif_1",
                title = "Toll Price Update",
                message = "Padma Bridge toll rates updated. Your travel estimate has been refreshed.",
                time = "10 mins ago",
                isRead = false
            ),
            NotificationItem(
                id = "notif_2",
                title = "Fuel Discount 5%",
                message = "Use code TRIPWISE at Padma Oil stations for 5% cashback on octane & diesel.",
                time = "2 hours ago",
                isRead = false
            ),
            NotificationItem(
                id = "notif_3",
                title = "Trip Saved Successfully",
                message = "Dhaka to Cox's Bazar has been archived to your personal trip history.",
                time = "Yesterday",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Emergency Contacts
    private val _emergencyContacts = MutableStateFlow<List<EmergencyContact>>(
        listOf(
            EmergencyContact("ec_1", "Highway Police Control Room", "999", "Highway Police"),
            EmergencyContact("ec_2", "National Roadside Rescue", "16263", "Roadside Assistance"),
            EmergencyContact("ec_3", "Fire Service & Civil Defence", "102", "Ambulance / Rescue"),
            EmergencyContact("ec_4", "Family Emergency Contact", "+880 1819-000000", "Personal Contact")
        )
    )
    val emergencyContacts: StateFlow<List<EmergencyContact>> = _emergencyContacts.asStateFlow()

    init {
        // Observe auth state changes to synchronize with Cloud Firestore
        auth?.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                _user.value = _user.value.copy(
                    name = currentUser.displayName ?: _user.value.name,
                    email = currentUser.email ?: _user.value.email,
                    isLoggedIn = true
                )
                syncWithFirestore(currentUser.uid)
            }
        }
    }

    private fun syncWithFirestore(uid: String) {
        val fs = firestore ?: return
        try {
            fs.collection("trips")
                .whereEqualTo("userId", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null) return@addSnapshotListener
                    val remoteTrips = snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.getString("id") ?: doc.id
                            val from = doc.getString("from") ?: ""
                            val to = doc.getString("to") ?: ""
                            val dist = doc.getDouble("distanceKm") ?: 0.0
                            val pass = doc.getLong("passengers")?.toInt() ?: 1
                            val vName = doc.getString("vehicleType") ?: "CAR"
                            val vType = when (vName.uppercase()) {
                                "MOTORCYCLE", "BIKE" -> VehicleType.MOTORCYCLE
                                "CNG", "CNG_AUTO" -> VehicleType.CNG_AUTO
                                else -> VehicleType.CAR
                            }
                            val fuel = doc.getDouble("fuelCost") ?: 0.0
                            val toll = doc.getDouble("tollCost") ?: 0.0
                            val parking = doc.getDouble("parkingCost") ?: 0.0
                            val food = doc.getDouble("foodOthers") ?: 0.0
                            val total = doc.getDouble("totalCost") ?: 0.0
                            val date = doc.getString("date") ?: ""
                            TripHistoryItem(id, from, to, dist, pass, vType, fuel, toll, parking, food, total, date)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    if (remoteTrips.isNotEmpty()) {
                        _history.value = remoteTrips
                    }
                }
        } catch (e: Exception) {
            Log.w("TripRepository", "syncWithFirestore error: ${e.message}")
        }
    }

    // TRIP OPERATIONS (CRUD)
    fun updateTripDetails(from: String, to: String, distanceKm: Double, passengers: Int) {
        _currentTrip.value = _currentTrip.value.copy(
            from = from.ifBlank { _currentTrip.value.from },
            to = to.ifBlank { _currentTrip.value.to },
            distanceKm = if (distanceKm > 0) distanceKm else _currentTrip.value.distanceKm,
            passengers = if (passengers > 0) passengers else _currentTrip.value.passengers
        )
    }

    fun updateVehicle(vehicle: VehicleType) {
        _currentTrip.value = _currentTrip.value.copy(vehicle = vehicle)
    }

    fun updateCosts(fuelPrice: Double, tollCost: Double, parkingCost: Double, foodOthers: Double) {
        _currentTrip.value = _currentTrip.value.copy(
            fuelPrice = fuelPrice,
            tollCost = tollCost,
            parkingCost = parkingCost,
            foodOthers = foodOthers
        )
    }

    fun saveCurrentTrip(): TripHistoryItem {
        val trip = _currentTrip.value
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val newItem = TripHistoryItem(
            id = UUID.randomUUID().toString(),
            from = trip.from.split(",").first().trim(),
            to = trip.to.split(",").first().trim(),
            distanceKm = trip.distanceKm,
            passengers = trip.passengers,
            vehicle = trip.vehicle,
            fuelCost = trip.fuelCost,
            tollCost = trip.tollCost,
            parkingCost = trip.parkingCost,
            foodOthers = trip.foodOthers,
            totalCost = trip.totalCost,
            date = dateFormat.format(Date())
        )
        _history.value = listOf(newItem) + _history.value

        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            val tripDoc = mapOf(
                "id" to newItem.id,
                "userId" to uid,
                "from" to newItem.from,
                "to" to newItem.to,
                "distanceKm" to newItem.distanceKm,
                "passengers" to newItem.passengers,
                "vehicleType" to newItem.vehicle.name,
                "fuelCost" to newItem.fuelCost,
                "tollCost" to newItem.tollCost,
                "parkingCost" to newItem.parkingCost,
                "foodOthers" to newItem.foodOthers,
                "totalCost" to newItem.totalCost,
                "date" to newItem.date
            )
            fs.collection("trips").document(newItem.id).set(tripDoc)
        }

        return newItem
    }

    fun updateHistoryItem(updated: TripHistoryItem) {
        _history.value = _history.value.map { if (it.id == updated.id) updated else it }
        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            val tripDoc = mapOf(
                "id" to updated.id,
                "userId" to uid,
                "from" to updated.from,
                "to" to updated.to,
                "distanceKm" to updated.distanceKm,
                "passengers" to updated.passengers,
                "vehicleType" to updated.vehicle.name,
                "fuelCost" to updated.fuelCost,
                "tollCost" to updated.tollCost,
                "parkingCost" to updated.parkingCost,
                "foodOthers" to updated.foodOthers,
                "totalCost" to updated.totalCost,
                "date" to updated.date
            )
            fs.collection("trips").document(updated.id).set(tripDoc)
        }
    }

    fun deleteHistoryItem(id: String) {
        _history.value = _history.value.filter { it.id != id }
        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            fs.collection("trips").document(id).delete()
        }
    }

    fun resetTrip() {
        _currentTrip.value = TripData()
    }

    // USER & AUTH
    fun login(email: String, name: String = "Noman Karim") {
        _user.value = _user.value.copy(
            email = email,
            name = name.ifBlank { "Noman Karim" },
            isLoggedIn = true
        )
    }

    fun signup(name: String, email: String, phone: String) {
        _user.value = User(
            name = name,
            email = email,
            phone = phone,
            isLoggedIn = true
        )
    }

    fun updateProfile(name: String, email: String, phone: String, bio: String) {
        _user.value = _user.value.copy(
            name = name,
            email = email,
            phone = phone,
            bio = bio
        )
        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            val userMap = mapOf(
                "userId" to uid,
                "name" to name,
                "email" to email,
                "phone" to phone,
                "bio" to bio
            )
            fs.collection("users").document(uid).set(userMap)
        }
    }

    fun logout() {
        auth?.signOut()
        _user.value = _user.value.copy(isLoggedIn = false)
    }

    fun completeOnboarding() {
        _hasCompletedOnboarding.value = true
    }

    // FAVORITES
    fun toggleFavorite(itemId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(itemId)) {
            current.remove(itemId)
        } else {
            current.add(itemId)
        }
        _favoriteIds.value = current
    }

    // CART
    fun addToCart(item: TravelItem) {
        val current = _cart.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.item.id == item.id }
        if (existingIndex != -1) {
            current[existingIndex] = current[existingIndex].copy(quantity = current[existingIndex].quantity + 1)
        } else {
            current.add(CartItem(item, 1))
        }
        _cart.value = current
    }

    fun updateCartQuantity(itemId: String, newQty: Int) {
        if (newQty <= 0) {
            removeFromCart(itemId)
        } else {
            _cart.value = _cart.value.map {
                if (it.item.id == itemId) it.copy(quantity = newQty) else it
            }
        }
    }

    fun removeFromCart(itemId: String) {
        _cart.value = _cart.value.filter { it.item.id != itemId }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // CHECKOUT / ORDERS
    fun placeOrder(paymentMethod: String): BookingOrder {
        val cartList = _cart.value
        val title = if (cartList.size == 1) cartList.first().item.name else "${cartList.first().item.name} + ${cartList.size - 1} more"
        val total = cartList.sumOf { it.item.price * it.quantity }
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val newOrder = BookingOrder(
            orderId = "ORD-" + (1000..9999).random(),
            title = title,
            totalAmount = if (total > 0) total else 1200.0,
            date = dateFormat.format(Date()),
            status = "Confirmed",
            paymentMethod = paymentMethod
        )
        _orders.value = listOf(newOrder) + _orders.value
        clearCart()

        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            val orderDoc = mapOf(
                "orderId" to newOrder.orderId,
                "userId" to uid,
                "title" to newOrder.title,
                "totalAmount" to newOrder.totalAmount,
                "date" to newOrder.date,
                "status" to newOrder.status,
                "paymentMethod" to newOrder.paymentMethod
            )
            fs.collection("orders").document(newOrder.orderId).set(orderDoc)
        }

        return newOrder
    }

    // NOTIFICATIONS
    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    // SETTINGS
    fun toggleDarkMode(enabled: Boolean) {
        _appSettings.value = _appSettings.value.copy(isDarkMode = enabled)
    }

    fun setLanguage(lang: String) {
        _appSettings.value = _appSettings.value.copy(language = lang)
    }

    fun setCurrency(currency: String) {
        _appSettings.value = _appSettings.value.copy(currency = currency)
    }

    fun togglePushNotifications(enabled: Boolean) {
        _appSettings.value = _appSettings.value.copy(pushNotificationsEnabled = enabled)
    }

    fun toggleSound(enabled: Boolean) {
        _appSettings.value = _appSettings.value.copy(soundEnabled = enabled)
    }

    // EMERGENCY CONTACTS
    fun addEmergencyContact(name: String, phone: String, role: String) {
        val newContact = EmergencyContact(UUID.randomUUID().toString(), name, phone, role)
        _emergencyContacts.value = _emergencyContacts.value + newContact

        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            val contactDoc = mapOf(
                "id" to newContact.id,
                "userId" to uid,
                "name" to newContact.name,
                "phone" to newContact.phone,
                "role" to newContact.role
            )
            fs.collection("emergency_contacts").document(newContact.id).set(contactDoc)
        }
    }

    fun deleteEmergencyContact(id: String) {
        _emergencyContacts.value = _emergencyContacts.value.filter { it.id != id }
        val uid = auth?.currentUser?.uid
        val fs = firestore
        if (fs != null && uid != null) {
            fs.collection("emergency_contacts").document(id).delete()
        }
    }
}
