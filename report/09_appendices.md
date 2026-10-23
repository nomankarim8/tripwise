# APPENDICES

## APPENDIX A: USER MANUAL AND OPERATIONAL GUIDE

This guide provides end-to-end instructions for installing, configuring, and operating **TripWise: Smart Vehicle & Trip Cost Planner** on an Android device.

### A.1 Initial Setup and Authentication
1. **Launch the Application:** Tap the **TripWise** app icon on your Android home screen or app drawer.
2. **Onboarding:** First-time users are greeted with an onboarding carousel introducing the 4-step trip planning wizard, multi-modal vehicle comparison, and highway utilities. Tap **Get Started**.
3. **Sign-In:** Tap **Sign in with Google** to authenticate securely via your Google account, or continue to browse the core calculation utilities in guest mode.

### A.2 Planning a Journey (4-Step Wizard)
1. **Initiate Wizard:** On the Home Screen dashboard, tap the primary card labeled **"Plan New Trip"**.
2. **Step 1 (Trip Details):**
   - Enter your **Origin City** (e.g., *Dhaka*).
   - Enter your **Destination City** (e.g., *Cox's Bazar*).
   - Enter the estimated **Trip Distance** in kilometers (e.g., *390*).
   - Specify the total **Passenger Count** traveling in the vehicle (e.g., *4*).
   - Tap **Next: Select Vehicle**.
3. **Step 2 (Vehicle Selection & Fuel Tuning):**
   - Select your vehicle modality by tapping on **Car (Sedan)**, **Motorcycle**, or **CNG (Auto-Rickshaw)**.
   - The system automatically loads pre-calibrated average fuel efficiency and standard fuel prices.
   - *(Optional)* Tap **Custom Fuel Calibration** to adjust your vehicle's specific mileage ($KM/L$) or enter a custom retail fuel price.
   - Tap **Next: Add Expenses**.
4. **Step 3 (Itemized Expenses):**
   - Enter estimated **Bridge & Expressway Tolls** (e.g., *750 BDT* for Padma Bridge).
   - Enter estimated **Parking Fees** (e.g., *200 BDT*).
   - Enter anticipated **Food & Refreshment Costs** (e.g., *800 BDT*).
   - Enter any **Contingency / Other Expenses** (e.g., *150 BDT*).
   - Tap **Next: Review Summary**.
5. **Step 4 (Review Summary & Apportionment):**
   - Review the generated financial ledger: Total Fuel Volume required, Total Fuel Expenditure, Supplementary Costs, and **Total Estimated Trip Cost**.
   - Check the **Cost Per Passenger** card to view the exact split quota for each traveler.
   - Tap **Save Trip to Cloud** to archive the itinerary in your persistent trip history.

### A.3 Comparing Vehicle Modalities
1. From the Home Screen or navigation drawer, select **"Compare Vehicles"**.
2. Use the interactive distance slider or numeric text field to enter an itinerary distance (e.g., *250 km*).
3. Inspect the three side-by-side cards:
   - **Car:** Higher comfort, standard fuel cost.
   - **Motorcycle:** Maximum fuel savings (up to 65% cheaper than car).
   - **CNG Auto-Rickshaw:** Highly economical paratransit option.
4. The screen highlights the most economical option and shows total financial savings.

### A.4 Accessing Highway Transit Utilities
- **Waypoint Route Map:** Tap **"Route Map"** to inspect major navigation waypoints, distances, and toll junctions along the Dhaka–Chittagong–Cox's Bazar highway corridor.
- **Digital Toll Pass:** Tap **"Toll Pass"** to view a simulated digital RFID / QR barcode for electronic toll plaza passage.
- **Padma Bridge Safety Guide:** Tap **"Padma Bridge"** to review statutory speed limits ($60\text{ km/h}$), CCTV camera locations, overtaking rules, and toll matrices.
- **Emergency Helplines:** Tap **"Emergency Contacts"** to access direct, one-tap dialing for National Emergency Service (999), Highway Police, and Fire & Rescue.

---

## APPENDIX B: SYSTEM INSTALLATION, BUILD, AND EXECUTION GUIDE

### B.1 Prerequisites
To build TripWise from source, ensure your development workstation meets the following requirements:
- **Operating System:** Linux (Ubuntu 20.04+), macOS (12+), or Windows 10/11 (64-bit).
- **JDK:** OpenJDK 17 or Oracle JDK 17 (pre-configured in modern Android build toolchains).
- **Android SDK:** Android SDK Platform 34 (API Level 34) and Build-Tools 34.0.0.
- **Gradle:** Gradle 8.9 (or automated via Gradle build scripts).

### B.2 Building the Project via Gradle
Open a terminal in the project root directory and execute the compilation command:

```bash
# Verify Gradle compilation and assemble debug APK
gradle :app:assembleDebug
```

Upon successful compilation, the generated APK binary will be available at:
`app/build/outputs/apk/debug/app-debug.apk`

Additionally, a pre-compiled, production-ready binary is available at the root of the project:
`/TripWise.apk` (Size: $\approx 22\text{ MB}$).

### B.3 Installing the APK on an Android Device
You can install TripWise on any physical Android device or emulator running Android 8.0 (API Level 26) or higher:

**Method 1: Direct File Transfer (USB / Cloud Drive / Messaging):**
1. Transfer `TripWise.apk` to your Android device via USB cable, Google Drive, WhatsApp, or Bluetooth.
2. On your Android device, open the **Files** or **Downloads** app and tap `TripWise.apk`.
3. If prompted, toggle **"Allow installation from this source"** in Android Settings.
4. Tap **Install** and then tap **Open**.

**Method 2: Android Debug Bridge (ADB):**
Connect your Android device via USB with USB Debugging enabled, and execute:
```bash
adb install -r TripWise.apk
```

---

## APPENDIX C: CLOUD FIRESTORE SECURITY RULES SPECIFICATION

The production security rules governing the Google Cloud Firestore instance are defined below:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // User profile isolation
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Trip records: full CRUD only by document owner
    match /trips/{tripId} {
      allow read, update, delete: if request.auth != null && resource.data.userId == request.auth.uid;
      allow create: if request.auth != null && request.resource.data.userId == request.auth.uid;
    }
    
    // Emergency contacts registry: public read-only for authenticated travelers
    match /emergency_contacts/{contactId} {
      allow read: if request.auth != null;
      allow write: if false; // Read-only public registry
    }
    
    // Default security posture: deny all unmatched document paths
    match /{document=**} {
      allow read, write: if false;
    }
  }
}
```

---

## APPENDIX D: REQUIREMENTS TRACEABILITY MATRIX (RTM)

| Req ID | Requirement Description | Architecture Module | Test ID | Verification Status |
| :---: | :--- | :--- | :---: | :---: |
| **FR-01** | Application Launch & Home Dashboard | `MainActivity.kt`, `HomeScreen.kt` | TC-01 | **VERIFIED (PASS)** |
| **FR-02** | Multi-Screen Backstack Navigation | `TripWiseApp.kt`, `NavRoutes.kt` | TC-02 | **VERIFIED (PASS)** |
| **FR-03** | Trip Origin & Destination Entry | `Step1TripDetailsScreen.kt` | TC-03, TC-04 | **VERIFIED (PASS)** |
| **FR-04** | Trip Distance & Passenger Count | `Step1TripDetailsScreen.kt` | TC-05, TC-06 | **VERIFIED (PASS)** |
| **FR-05** | Vehicle Modality Selection | `Step2SelectVehicleScreen.kt` | TC-07, TC-08 | **VERIFIED (PASS)** |
| **FR-06** | Fuel Price Specification | `Step2SelectVehicleScreen.kt` | TC-09 | **VERIFIED (PASS)** |
| **FR-07** | Engine Fuel Economy Tuning | `Step2SelectVehicleScreen.kt` | TC-10 | **VERIFIED (PASS)** |
| **FR-08** | Itemized Travel Expenses Entry | `Step3CostsExpensesScreen.kt` | TC-11 | **VERIFIED (PASS)** |
| **FR-09** | 4-Step Stepper Navigation | `TripWiseApp.kt`, `TripViewModel.kt` | TC-03, TC-07, TC-11 | **VERIFIED (PASS)** |
| **FR-10** | Deterministic Cost Calculation | `TripViewModel.kt` | TC-12 | **VERIFIED (PASS)** |
| **FR-11** | Multi-Modal Cost Comparison | `CompareVehiclesScreen.kt` | TC-13 | **VERIFIED (PASS)** |
| **FR-12** | Trip Summary Ledger & Cloud Save | `Step4ReviewSummaryScreen.kt` | TC-14 | **VERIFIED (PASS)** |
| **FR-13** | Trip History Retrieval & Deletion | `TripHistoryScreen.kt`, `TripRepository.kt` | TC-15, TC-16 | **VERIFIED (PASS)** |
| **FR-14** | Aggregate Trip Analytics & Charts | `AnalyticsScreen.kt` | TC-17 | **VERIFIED (PASS)** |
| **FR-15** | Input Validation & Error Handling | `TripDraft.kt`, Compose Form Checkers | TC-04, TC-06, TC-18 | **VERIFIED (PASS)** |
| **FR-16** | Session & Offline State Preservation | `TripRepository.kt` (StateFlow & Cache) | TC-14, TC-16 | **VERIFIED (PASS)** |
| **FR-17** | Contextual Feedback & Helplines | `EmergencyContactsScreen.kt`, M3 Snackbars | TC-19, TC-20 | **VERIFIED (PASS)** |

---

## APPENDIX E: KEY SOURCE CODE LISTINGS

### E.1 Trip Model (`app/src/main/java/com/example/tripwise/data/model/Trip.kt`)
```kotlin
package com.example.tripwise.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Trip(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val origin: String = "",
    val destination: String = "",
    val distanceKm: Double = 0.0,
    val passengerCount: Int = 1,
    val vehicleType: String = "CAR",
    val fuelPricePerUnit: Double = 130.0,
    val mileageKmPerUnit: Double = 14.0,
    val tollCost: Double = 0.0,
    val parkingCost: Double = 0.0,
    val foodCost: Double = 0.0,
    val otherCost: Double = 0.0,
    val totalFuelVolume: Double = 0.0,
    val totalFuelCost: Double = 0.0,
    val totalTripCost: Double = 0.0,
    val costPerPassenger: Double = 0.0,
    val notes: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
```

### E.2 Core Calculation Engine (`TripViewModel.kt`)
```kotlin
package com.example.tripwise.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tripwise.data.model.Trip
import com.example.tripwise.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TripViewModel(
    private val repository: TripRepository = TripRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    fun calculateTripBudget(draft: TripDraft): ComputedExpenses {
        // Defensive check against division by zero
        val safeMileage = if (draft.mileageKmPerUnit > 0.0) draft.mileageKmPerUnit else 1.0
        val fuelVolume = draft.distanceKm / safeMileage
        val fuelCost = fuelVolume * draft.fuelPricePerUnit

        val supplementaryCost = draft.tollCost + draft.parkingCost + draft.foodCost + draft.otherCost
        val totalTripCost = fuelCost + supplementaryCost

        // Defensive check against non-positive passenger count
        val safePassengers = if (draft.passengerCount > 0) draft.passengerCount else 1
        val costPerPassenger = totalTripCost / safePassengers

        return ComputedExpenses(
            fuelVolume = fuelVolume,
            fuelCost = fuelCost,
            supplementaryCost = supplementaryCost,
            totalTripCost = totalTripCost,
            costPerPassenger = costPerPassenger
        )
    }

    fun computeComparison(distanceKm: Double): ComparisonResult {
        val carFuel = distanceKm / 14.0
        val carCost = carFuel * 130.0

        val bikeFuel = distanceKm / 40.0
        val bikeCost = bikeFuel * 130.0

        val cngFuel = distanceKm / 28.0
        val cngCost = cngFuel * 45.0

        val savings = carCost - bikeCost
        val savingsPct = if (carCost > 0.0) (savings / carCost) * 100.0 else 0.0

        return ComparisonResult(
            carCost = carCost,
            bikeCost = bikeCost,
            cngCost = cngCost,
            carFuel = carFuel,
            bikeFuel = bikeFuel,
            cngFuel = cngFuel,
            savingsAmount = savings,
            savingsPercentage = savingsPct
        )
    }
}
```
