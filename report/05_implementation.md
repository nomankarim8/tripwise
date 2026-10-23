# CHAPTER 5: IMPLEMENTATION WALKTHROUGH AND ARCHITECTURE

## 5.1 Introduction

The implementation phase represents the systematic realization of the architectural designs, mathematical models, and functional specifications defined in previous chapters into production-ready software code. **TripWise: Smart Vehicle & Trip Cost Planner** was constructed entirely as a native Android mobile application leveraging modern software development standards.

This chapter provides an exhaustive technical walkthrough of the codebase. It details the technology stack, project directory hierarchy, declarative UI mechanics in Jetpack Compose, unidirectional state management via Kotlin StateFlow and ViewModel, asynchronous database synchronization with Google Cloud Firestore, and the concrete implementation of core features including the 4-step trip planning wizard, multi-modal vehicle comparison, and highway transit utilities.

---

## 5.2 Technology Stack Justification

The technical foundation of TripWise was chosen to maximize runtime execution performance, developer productivity, memory safety, and cross-device compatibility:

### Table 5.1: Technology Stack and Architectural Rationale

| Layer / Tool | Technology Selected | Version | Justification & Architectural Rationale |
| :--- | :--- | :---: | :--- |
| **Language** | Kotlin | 2.1.0 | Official language for modern Android development. Provides concise syntax, complete null-safety, functional primitives, and native coroutine primitives. |
| **UI Framework** | Jetpack Compose | 2024.09.00 (BOM) | Modern declarative UI toolkit. Eliminates XML boilerplate, facilitates fine-grained state-driven recomposition, and natively supports dynamic theme switching. |
| **Design System** | Material Design 3 (M3) | 1.2.1 | Implements Google's latest design system, including elevated surface tokens, dynamic color schemes, adaptive touch targets, and accessible typography scales. |
| **Concurrency** | Kotlin Coroutines & Flow | 1.8.0 | Lightweight, structured concurrency framework. Enables non-blocking background I/O operations (Firestore network calls) with lifecycle-aware UI state collection. |
| **State Management** | AndroidX ViewModel & StateFlow | 2.8.4 | Maintains UI state across configuration changes (e.g., screen rotations). Exposes thread-safe, hot observable state streams to Jetpack Compose. |
| **Cloud Database** | Google Cloud Firestore | 25.1.0 | Highly scalable NoSQL document database. Provides real-time synchronization, automatic local offline caching, and sub-second document reads and writes. |
| **Authentication** | Jetpack Credential Manager | 1.3.0 | Modern unified Android authentication API supporting seamless Google Sign-In without deprecated OAuth intent activities. |
| **Image Loading** | Coil Compose | 2.6.0 | Fast, lightweight image loading library backed by Kotlin Coroutines, providing disk and memory caching for network assets. |
| **Build System** | Gradle (Kotlin DSL) | 8.9 | Industry-standard build automation system paired with Android Gradle Plugin (AGP) 8.7.0 for incremental compilation and resource shrinking. |

---

## 5.3 Project Structure and Codebase Organization

The TripWise codebase is organized according to Clean Architecture and package-by-feature conventions under the package namespace `com.example.tripwise` (Application ID: `com.aistudio.tripwise.qrzvkm`).

```
app/src/main/java/com/example/tripwise/
├── MainActivity.kt                # Single-activity container with edge-to-edge support
├── TripWiseApp.kt                 # Top-level composable managing navigation routing
├── data/                          # Data Layer
│   ├── model/
│   │   ├── Trip.kt                # Core Trip entity data class
│   │   ├── VehicleType.kt         # Enum: CAR, MOTORCYCLE, CNG
│   │   ├── VehicleBenchmark.kt    # Benchmarks (mileage, default fuel price)
│   │   └── EmergencyContact.kt    # Emergency directory model
│   └── repository/
│       └── TripRepository.kt      # Cloud Firestore & memory sync implementation
├── viewmodel/                     # Presentation State Management
│   ├── TripViewModel.kt           # Central ViewModel coordinating business logic
│   └── TripUiState.kt             # Immutable UI state class and draft representations
├── ui/                            # Presentation UI Composables
│   ├── theme/                     # Material 3 Theme, Color, Type, and Shapes
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── screens/                   # Distinct Screen Composables
│   │   ├── HomeScreen.kt          # Main dashboard, quick actions, utilities grid
│   │   ├── Step1TripDetailsScreen.kt  # Wizard Step 1: Origin, destination, distance
│   │   ├── Step2SelectVehicleScreen.kt# Wizard Step 2: Modality and fuel tuning
│   │   ├── Step3CostsExpensesScreen.kt# Wizard Step 3: Itemized tolls, parking, food
│   │   ├── Step4ReviewSummaryScreen.kt# Wizard Step 4: Final ledger & passenger split
│   │   ├── CompareVehiclesScreen.kt   # Multi-modal side-by-side cost delta engine
│   │   ├── TripHistoryScreen.kt       # Saved trips list with delete/edit
│   │   ├── TripEditScreen.kt          # Trip modification screen
│   │   ├── AnalyticsScreen.kt         # Spending, fuel, and distance aggregations
│   │   ├── ProfileScreen.kt           # User profile and stats
│   │   ├── SettingsScreen.kt          # Unit preferences and app configuration
│   │   ├── RouteMapScreen.kt          # Dhaka-Chittagong-Cox's Bazar Waypoints
│   │   ├── QrTicketScreen.kt          # Digital RFID / QR Toll Gate Pass
│   │   ├── VideoGuideScreen.kt        # Padma Bridge speed camera & safety rules
│   │   └── EmergencyContactsScreen.kt # One-touch emergency dispatch dialer
│   └── components/                # Reusable UI widgets (cards, buttons, banners)
└── navigation/
    └── NavRoutes.kt               # Centralized route destination constants
```

---

## 5.4 Implementation of the Core 4-Step Trip Planning Wizard

The core functional journey of TripWise is implemented through a progressive 4-step wizard designed to simplify complex calculations into intuitive steps.

### 5.4.1 Step 1: Journey Parameters Capture
`Step1TripDetailsScreen.kt` prompts the user for geographic and occupancy parameters: Origin, Destination, Travel Distance ($KM$), and Passenger Count ($N$). The UI enforces strict validation: non-numeric distance strings are rejected, and the forward progression button remains disabled until valid values are entered.

```kotlin
// Listing 5.1: Distance and Passenger Input with Real-Time Validation
OutlinedTextField(
    value = draft.distanceString,
    onValueChange = { viewModel.updateDistanceString(it) },
    label = { Text("Trip Distance (km)") },
    leadingIcon = { Icon(Icons.Default.Route, contentDescription = "Distance") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    isError = draft.isDistanceError,
    supportingText = {
        if (draft.isDistanceError) Text("Please enter a valid positive distance in km")
    },
    modifier = Modifier.fillMaxWidth().testTag("distance_input")
)
```

### 5.4.2 Step 2: Vehicle Modality Selection & Benchmark Calibration
`Step2SelectVehicleScreen.kt` provides three elevated selectable cards corresponding to Sedan Car, Motorcycle, and CNG Auto-Rickshaw. Selecting a card triggers the ViewModel to auto-populate default empirical benchmarks:
- **Sedan Car:** Fuel economy = $14.0\text{ km/L}$, Retail Price = $130.0\text{ BDT/L}$ (Octane).
- **Motorcycle:** Fuel economy = $40.0\text{ km/L}$, Retail Price = $130.0\text{ BDT/L}$ (Petrol).
- **CNG Auto-Rickshaw:** Fuel economy = $28.0\text{ km/m}^3$, Retail Price = $45.0\text{ BDT/m}^3$ (CNG).

Users retain full capability to expand the "Custom Calibration" accordion to fine-tune mileage or price to match their specific vehicle engine.

### 5.4.3 Step 3: Itemized Travel Expenses
`Step3CostsExpensesScreen.kt` captures supplementary expenses across four distinct categories:
1. **Bridge & Expressway Tolls ($T$):** User enters anticipated bridge fees (e.g., 750 BDT for Padma Bridge).
2. **Parking Costs ($K$):** Terminal, airport, or hotel parking charges.
3. **Food & Refreshments ($F$):** En-route highway restaurant expenditures.
4. **Other Miscellaneous ($O$):** Vehicle servicing, tire puncture repair, or emergency contingencies.

Each input is sanitized to guarantee non-negative values.

### 5.4.4 Step 4: Review Summary & Passenger Apportionment Ledger
`Step4ReviewSummaryScreen.kt` executes the final mathematical calculation, displaying an itemized summary card:
- Total Fuel Consumed (Liters or $\text{m}^3$).
- Gross Fuel Expenditure.
- Subtotal of Supplementary Costs (Tolls + Parking + Food).
- **Total Estimated Trip Cost.**
- **Equitable Cost Per Passenger:** Calculated dynamically as $\text{Total} / N$.

```kotlin
// Listing 5.2: Step 4 Review Calculation and Apportionment Ledger
Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Total Estimated Cost", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "৳ ${"%.2f".format(calculated.totalTripCost)}",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Cost Per Passenger (${draft.passengerCount} people):")
            Text(
                text = "৳ ${"%.2f".format(calculated.costPerPassenger)}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
```

---

## 5.5 Multi-Modal Vehicle Comparison Implementation

`CompareVehiclesScreen.kt` implements the side-by-side comparative simulation engine. Users can input any arbitrary trip distance (or utilize a responsive slider from $10\text{ km}$ to $1,000\text{ km}$) and instantly inspect how fuel volume, total financial expenditure, and economic efficiency vary across all three vehicle classifications.

```kotlin
// Listing 5.3: Comparative Engine Calculation Logic in TripViewModel
fun computeMultiModalComparison(distanceKm: Double): ComparisonResult {
    val carLiters = distanceKm / 14.0
    val carCost = carLiters * 130.0

    val bikeLiters = distanceKm / 40.0
    val bikeCost = bikeLiters * 130.0

    val cngUnits = distanceKm / 28.0
    val cngCost = cngUnits * 45.0

    val maxCost = carCost
    val bikeSavings = maxCost - bikeCost
    val bikeSavingsPct = if (maxCost > 0) (bikeSavings / maxCost) * 100.0 else 0.0

    return ComparisonResult(
        carCost = carCost,
        bikeCost = bikeCost,
        cngCost = cngCost,
        carLiters = carLiters,
        bikeLiters = bikeLiters,
        cngUnits = cngUnits,
        bikeSavingsAmount = bikeSavings,
        bikeSavingsPercentage = bikeSavingsPct
    )
}
```

---

## 5.6 State Management with Kotlin StateFlow & ViewModel

TripWise enforces a strict **Unidirectional Data Flow (UDF)** architecture. `TripViewModel.kt` serves as the centralized state orchestrator, encapsulating all mutable application state within private `MutableStateFlow` streams and exposing read-only `StateFlow` primitives to the UI layer.

```kotlin
// Listing 5.4: Unidirectional UI State Management in TripViewModel
class TripViewModel(
    private val repository: TripRepository = TripRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    init {
        observeSavedTrips()
    }

    fun updateDistance(km: Double) {
        _uiState.update { currentState ->
            val updatedDraft = currentState.draft.copy(distanceKm = km)
            val computed = calculateExpenses(updatedDraft)
            currentState.copy(draft = updatedDraft, calculated = computed)
        }
    }

    private fun calculateExpenses(draft: TripDraft): ComputedExpenses {
        val mileage = if (draft.mileageKmPerUnit > 0) draft.mileageKmPerUnit else 1.0
        val fuelVol = draft.distanceKm / mileage
        val fuelCost = fuelVol * draft.fuelPricePerUnit
        val suppCost = draft.tollCost + draft.parkingCost + draft.foodCost + draft.otherCost
        val total = fuelCost + suppCost
        val passengers = if (draft.passengerCount > 0) draft.passengerCount else 1
        val perPerson = total / passengers

        return ComputedExpenses(
            fuelVolume = fuelVol,
            fuelCost = fuelCost,
            supplementaryCost = suppCost,
            totalTripCost = total,
            costPerPassenger = perPerson
        )
    }
}
```

---

## 5.7 Data Persistence and Google Cloud Firestore Integration

The data persistence layer is handled by `TripRepository.kt`. The repository manages a dual-tiered storage strategy: an in-memory reactive flow for instant offline UI responsiveness, and Google Cloud Firestore for remote cloud backup and cross-device synchronization.

```kotlin
// Listing 5.5: Cloud Firestore Repository Data Layer
class TripRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val tripsCollection = firestore.collection("trips")

    fun getTripsStream(userId: String): Flow<List<Trip>> = callbackFlow {
        val listener = tripsCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val trips = snapshot?.documents?.mapNotNull { it.toObject(Trip::class.java) } ?: emptyList()
                trySend(trips)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveTrip(trip: Trip): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            tripsCollection.document(trip.id).set(trip).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

---

## 5.8 User Authentication and Google Sign-In via Credential Manager

User identity management is integrated using the modern AndroidX `CredentialManager` framework combined with `firebase-auth-ktx`. The application supports one-tap Google Sign-In, eliminating traditional password vulnerability.

```kotlin
// Listing 5.6: Google Sign-In Integration with Jetpack Credential Manager
suspend fun signInWithGoogle(context: Context): AuthResult {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId)
        .build()
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val response = credentialManager.getCredential(request = request, context = context)
    val googleToken = GoogleIdTokenCredential.createFrom(response.credential.data).idToken
    val authCredential = GoogleAuthProvider.getCredential(googleToken, null)
    
    return FirebaseAuth.getInstance().signInWithCredential(authCredential).await()
}
```

---

## 5.9 Highway Transit Utilities Implementation

To provide a complete travel companion experience, TripWise integrates four localized travel utility modules accessible directly from the home dashboard:

1. **Dhaka–Chittagong–Cox's Bazar Waypoint Route Map (`RouteMapScreen.kt`):** Implements an interactive visual route diagram rendering major highway nodes (Dhaka, Comilla, Feni, Chittagong, Cox's Bazar), mileage intervals, and primary toll junctions.
2. **Digital RFID / QR Toll Gate Pass (`QrTicketScreen.kt`):** Simulates an electronic toll collection (ETC) barcode and QR token for the Meghna, Gumti, and Padma bridge electronic toll lanes.
3. **Padma Bridge Speed Camera & Safety Guide (`VideoGuideScreen.kt`):** Documents statutory driving regulations, maximum speed limits ($60\text{ km/h}$ over bridge main span), overtaking restrictions, and motorcycle lane safety rules.
4. **Emergency Contacts Helpline (`EmergencyContactsScreen.kt`):** Features one-touch calling for National Emergency Service (999), Highway Police Headquarters, Fire Service & Civil Defence, and Road Transport & Highways Division helplines using Android's native `Intent.ACTION_DIAL`.

---

## 5.10 Build Configuration and Packaging

The project build lifecycle is automated using Gradle 8.9 and the Android Gradle Plugin (AGP) 8.7.0.

```kotlin
// Listing 5.7: app/build.gradle.kts Highlights
android {
    namespace = "com.example.tripwise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aistudio.tripwise.qrzvkm"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
```

Executing `compile_applet` triggers Gradle to resolve dependencies, run KSP/annotation processing, compile Kotlin bytecode, optimize resources via AAPT2, and assemble the release and debug binaries. The final compiled APK (`TripWise.apk`, size $\approx 22\text{ MB}$) is packaged at the project root for immediate distribution.

---

## 5.11 Chapter Summary

This chapter detailed the implementation architecture and technical mechanics of TripWise. It presented the modular directory hierarchy, declarative Jetpack Compose UI construction, unidirectional state coordination via Kotlin StateFlow, asynchronous cloud synchronization with Google Cloud Firestore, and the realization of highway transit utilities. Chapter 6 details the comprehensive testing methodology, verification results, security rules validation, and requirements traceability matrix.
