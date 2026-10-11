package com.example.tripwise.model

data class User(
    val name: String = "Noman Karim",
    val email: String = "karimnoman97@gmail.com",
    val phone: String = "+880 1712-345678",
    val bio: String = "Frequent road-tripper & smart travel enthusiast.",
    val avatarUrl: String = "",
    val isLoggedIn: Boolean = true
)

data class TravelItem(
    val id: String,
    val name: String,
    val category: String, // "Rental Cars", "Bike Tours", "CNG Express", "Toll Passes", "Tour Packages", "Hotels"
    val price: Double,
    val rating: Double,
    val reviewsCount: Int,
    val description: String,
    val features: List<String>,
    val location: String = "Bangladesh"
)

data class CartItem(
    val item: TravelItem,
    val quantity: Int = 1
)

data class BookingOrder(
    val orderId: String,
    val title: String,
    val totalAmount: Double,
    val date: String,
    val status: String, // "Confirmed", "Completed", "Processing"
    val paymentMethod: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val phone: String,
    val role: String // "Highway Police", "Roadside Assistance", "Ambulance", "Personal Contact"
)

data class AppSettings(
    val isDarkMode: Boolean = false,
    val language: String = "en", // "en" for English, "bn" for Bengali
    val currency: String = "৳ (BDT)",
    val pushNotificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true
)
