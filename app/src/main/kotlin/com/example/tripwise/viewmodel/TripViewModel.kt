package com.example.tripwise.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tripwise.data.TripRepository
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Login : Screen()
    object Signup : Screen()
    object ForgotPassword : Screen()
    object Home : Screen()
    object PlanOverview : Screen()
    object Step1Details : Screen()
    object Step2Vehicle : Screen()
    object Step3Costs : Screen()
    object Step4Summary : Screen()
    data class TripEdit(val tripId: String) : Screen()
    object CompareVehicles : Screen()
    object History : Screen()
    object Analytics : Screen()
    object Profile : Screen()
    object EditProfile : Screen()
    object Marketplace : Screen()
    data class ProductDetail(val itemId: String) : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    object OrderHistory : Screen()
    object Favorites : Screen()
    object Settings : Screen()
    object Notifications : Screen()
    object RouteMap : Screen()
    object QrTicket : Screen()
    object VideoGuide : Screen()
    object EmergencyContacts : Screen()
}

class TripViewModel(
    private val repository: TripRepository = TripRepository()
) : ViewModel() {

    // Repositories & StateFlows
    val currentTrip: StateFlow<TripData> = repository.currentTrip
    val history: StateFlow<List<TripHistoryItem>> = repository.history
    val user: StateFlow<User> = repository.user
    val hasCompletedOnboarding: StateFlow<Boolean> = repository.hasCompletedOnboarding
    val appSettings: StateFlow<AppSettings> = repository.appSettings
    val travelItems: StateFlow<List<TravelItem>> = repository.travelItems
    val favoriteIds: StateFlow<Set<String>> = repository.favoriteIds
    val cart: StateFlow<List<CartItem>> = repository.cart
    val orders: StateFlow<List<BookingOrder>> = repository.orders
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val emergencyContacts: StateFlow<List<EmergencyContact>> = repository.emergencyContacts

    // Screen navigation
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf<Screen>(Screen.Splash)

    // Bottom Navigation Bar Active Tab (0=Home, 1=History, 2=Analytics, 3=Profile)
    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    // Snackbar notifications
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected Marketplace Category filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    fun navigateTo(screen: Screen) {
        _navigationStack.add(screen)
        _currentScreen.value = screen
        updateActiveTabForScreen(screen)
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.lastIndex)
            val previous = _navigationStack.last()
            _currentScreen.value = previous
            updateActiveTabForScreen(previous)
            return true
        }
        return false
    }

    private fun updateActiveTabForScreen(screen: Screen) {
        when (screen) {
            Screen.Home -> _activeTab.value = 0
            Screen.History -> _activeTab.value = 1
            Screen.Analytics -> _activeTab.value = 2
            Screen.Profile -> _activeTab.value = 3
            else -> {}
        }
    }

    fun selectTab(tab: Int) {
        _activeTab.value = tab
        when (tab) {
            0 -> navigateTo(Screen.Home)
            1 -> navigateTo(Screen.History)
            2 -> navigateTo(Screen.Analytics)
            3 -> navigateTo(Screen.Profile)
        }
    }

    // SEARCH & FILTER
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    // TRIP OPERATIONS
    fun updateTripDetails(from: String, to: String, distanceKm: Double, passengers: Int) {
        repository.updateTripDetails(from, to, distanceKm, passengers)
    }

    fun updateVehicle(vehicle: VehicleType) {
        repository.updateVehicle(vehicle)
    }

    fun updateCosts(fuelPrice: Double, tollCost: Double, parkingCost: Double, foodOthers: Double) {
        repository.updateCosts(fuelPrice, tollCost, parkingCost, foodOthers)
    }

    fun finishAndSaveTrip() {
        repository.saveCurrentTrip()
        _snackbarMessage.value = "Trip saved to history!"
        _navigationStack.clear()
        _navigationStack.add(Screen.Home)
        _currentScreen.value = Screen.Home
        _activeTab.value = 0
    }

    fun updateHistoryTrip(updated: TripHistoryItem) {
        repository.updateHistoryItem(updated)
        _snackbarMessage.value = "Trip updated successfully!"
        navigateBack()
    }

    fun deleteHistoryTrip(id: String) {
        repository.deleteHistoryItem(id)
        _snackbarMessage.value = "Trip deleted from history"
    }

    fun resetTrip() {
        repository.resetTrip()
    }

    // AUTH
    fun login(email: String, name: String = "Noman Karim") {
        repository.login(email, name)
        _snackbarMessage.value = "Welcome back, ${repository.user.value.name}!"
        navigateTo(Screen.Home)
    }

    fun signup(name: String, email: String, phone: String) {
        repository.signup(name, email, phone)
        _snackbarMessage.value = "Account created successfully!"
        navigateTo(Screen.Home)
    }

    fun updateProfile(name: String, email: String, phone: String, bio: String) {
        repository.updateProfile(name, email, phone, bio)
        _snackbarMessage.value = "Profile updated!"
        navigateBack()
    }

    fun logout() {
        repository.logout()
        _snackbarMessage.value = "Signed out successfully"
        navigateTo(Screen.Login)
    }

    fun completeOnboarding() {
        repository.completeOnboarding()
        navigateTo(Screen.Home)
    }

    // FAVORITES
    fun toggleFavorite(itemId: String) {
        val isFav = repository.favoriteIds.value.contains(itemId)
        repository.toggleFavorite(itemId)
        _snackbarMessage.value = if (isFav) "Removed from Wishlist" else "Added to Wishlist ❤️"
    }

    // CART
    fun addToCart(item: TravelItem) {
        repository.addToCart(item)
        _snackbarMessage.value = "Added to Cart 🛒"
    }

    fun updateCartQuantity(itemId: String, qty: Int) {
        repository.updateCartQuantity(itemId, qty)
    }

    fun removeFromCart(itemId: String) {
        repository.removeFromCart(itemId)
        _snackbarMessage.value = "Removed from cart"
    }

    // CHECKOUT
    fun checkout(paymentMethod: String) {
        val order = repository.placeOrder(paymentMethod)
        _snackbarMessage.value = "Booking Confirmed! Order #${order.orderId}"
        navigateTo(Screen.OrderHistory)
    }

    // NOTIFICATIONS
    fun markAllNotificationsRead() {
        repository.markAllNotificationsRead()
        _snackbarMessage.value = "All notifications marked as read"
    }

    // SETTINGS
    fun toggleDarkMode(enabled: Boolean) {
        repository.toggleDarkMode(enabled)
    }

    fun setLanguage(lang: String) {
        repository.setLanguage(lang)
        _snackbarMessage.value = if (lang == "bn") "ভাষা পরিবর্তন করা হয়েছে (বাংলা)" else "Language set to English"
    }

    fun setCurrency(currency: String) {
        repository.setCurrency(currency)
    }

    fun togglePushNotifications(enabled: Boolean) {
        repository.togglePushNotifications(enabled)
    }

    fun toggleSound(enabled: Boolean) {
        repository.toggleSound(enabled)
    }

    // EMERGENCY CONTACTS
    fun addEmergencyContact(name: String, phone: String, role: String) {
        repository.addEmergencyContact(name, phone, role)
        _snackbarMessage.value = "Emergency contact added"
    }

    fun deleteEmergencyContact(id: String) {
        repository.deleteEmergencyContact(id)
        _snackbarMessage.value = "Contact removed"
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
