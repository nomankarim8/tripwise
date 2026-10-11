package com.example.tripwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.tripwise.ui.screens.*
import com.example.tripwise.ui.theme.AppColors
import com.example.tripwise.ui.theme.TripWiseTheme
import com.example.tripwise.viewmodel.Screen
import com.example.tripwise.viewmodel.TripViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TripWiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColors.Background
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()

                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            Screen.Splash -> SplashScreen(viewModel = viewModel)
                            Screen.Onboarding -> OnboardingScreen(viewModel = viewModel)
                            Screen.Login -> LoginScreen(viewModel = viewModel)
                            Screen.Signup -> SignupScreen(viewModel = viewModel)
                            Screen.ForgotPassword -> LoginScreen(viewModel = viewModel)
                            Screen.Home -> HomeScreen(viewModel = viewModel)
                            Screen.PlanOverview -> PlanTripOverviewScreen(viewModel = viewModel)
                            Screen.Step1Details -> Step1TripDetailsScreen(viewModel = viewModel)
                            Screen.Step2Vehicle -> Step2SelectVehicleScreen(viewModel = viewModel)
                            Screen.Step3Costs -> Step3CostsExpensesScreen(viewModel = viewModel)
                            Screen.Step4Summary -> Step4ReviewSummaryScreen(viewModel = viewModel)
                            is Screen.TripEdit -> TripEditScreen(tripId = screen.tripId, viewModel = viewModel)
                            Screen.CompareVehicles -> CompareVehiclesScreen(viewModel = viewModel)
                            Screen.History -> TripHistoryScreen(viewModel = viewModel)
                            Screen.Analytics -> AnalyticsScreen(viewModel = viewModel)
                            Screen.Profile -> ProfileScreen(viewModel = viewModel)
                            Screen.EditProfile -> EditProfileScreen(viewModel = viewModel)
                            Screen.Marketplace -> MarketplaceScreen(viewModel = viewModel)
                            is Screen.ProductDetail -> ProductDetailScreen(itemId = screen.itemId, viewModel = viewModel)
                            Screen.Cart -> CartScreen(viewModel = viewModel)
                            Screen.Checkout -> CheckoutScreen(viewModel = viewModel)
                            Screen.OrderHistory -> OrderHistoryScreen(viewModel = viewModel)
                            Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                            Screen.Settings -> SettingsScreen(viewModel = viewModel)
                            Screen.Notifications -> NotificationsScreen(viewModel = viewModel)
                            Screen.RouteMap -> RouteMapScreen(viewModel = viewModel)
                            Screen.QrTicket -> QrTicketScreen(viewModel = viewModel)
                            Screen.VideoGuide -> VideoGuideScreen(viewModel = viewModel)
                            Screen.EmergencyContacts -> EmergencyContactsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
