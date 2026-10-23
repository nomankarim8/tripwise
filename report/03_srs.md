# CHAPTER 3: SOFTWARE REQUIREMENTS SPECIFICATION (SRS)

## 3.1 Purpose of the SRS

The purpose of this Software Requirements Specification (SRS) is to provide a complete, authoritative, and unambiguous specification of the functional, non-functional, behavioral, and architectural requirements for **TripWise: Smart Vehicle & Trip Cost Planner**. This document is formulated in accordance with the principles of IEEE Std 830-1998 (Recommended Practice for Software Requirements Specifications) and ISO/IEC/IEEE 29148:2018 (Systems and software engineering — Life cycle processes — Requirements engineering).

This SRS serves as the primary contractual and technical baseline between software engineers, system architects, quality assurance testers, academic evaluators, and end users. It details the precise inputs, outputs, computational formulas, operational constraints, and verification criteria governing the application.

---

## 3.2 Product Scope

**TripWise** is a native Android mobile application engineered using Kotlin 2.1.0, Jetpack Compose, and Google Cloud Firestore. The application automates the end-to-end planning, mathematical forecasting, and financial management of road-based vehicular journeys.

### Core Capabilities within Scope
- **Interactive 4-Step Trip Planning Wizard:** A progressive disclosure workflow guiding users through journey parameters, vehicle selection, itemized expense entry, and comprehensive financial review.
- **Multi-Modal Vehicular Estimation Engine:** Calibrated support for three primary regional vehicle classifications: Private Sedan Car ($14\text{ km/L}$), Commuter Motorcycle ($40\text{ km/L}$), and CNG Auto-Rickshaw ($28\text{ km/L}$).
- **Granular Expense Itemization:** Deterministic computation of fuel volume, gross fuel expenditure, bridge and highway tolls, terminal parking, meals/refreshments, and contingency buffers.
- **Equitable Group Cost Apportionment:** Automated, per-passenger expense partitioning with dynamic allocation formulas.
- **Side-by-Side Multi-Modal Cost Comparison:** Synchronized delta evaluation comparing fuel volume, gross expenditure, and net financial savings across all three vehicle classifications for any arbitrary distance.
- **Cloud-Backed Trip History and Analytics:** Persistent storage in Cloud Firestore with offline cache fallback, providing aggregate metrics (total spending, fuel consumed, distance traveled) and historical trip editing.
- **Integrated Highway Transit Utilities:** Waypoint route map (Dhaka–Chittagong–Cox's Bazar corridor), digital RFID/QR toll gate passes, Padma Bridge speed camera and safety guide, and one-touch emergency helpline dispatch.
- **Zero-Trust Cloud Authentication:** Secure authentication via Google Sign-In powered by Jetpack Credential Manager and protected by declarative Firestore Security Rules.

### Out-of-Scope Capabilities
- Direct real-time banking payments or automated mobile financial service (MFS) balance deductions.
- Physical OBD-II Bluetooth vehicle telemetry streaming.
- International multi-currency conversion at live floating exchange rates (the application standardizes on local currency BDT / standard numeric units).

---

## 3.3 Product Perspective

TripWise operates as a self-contained, client-centric native mobile application operating within the broader Google Android ecosystem and cloud infrastructure, as illustrated in the high-level system perspective context:

```
+-------------------------------------------------------------------------------+
|                             Android Operating System                          |
|  +-------------------------------------------------------------------------+  |
|  |                     TripWise Mobile Application                         |  |
|  |  +---------------------+  +--------------------+  +------------------+  |  |
|  |  | Jetpack Compose UI  |  | TripViewModel      |  | TripRepository   |  |  |
|  |  | (Declarative M3)    |<-| (StateFlow / MVVM) |<-| (Data Sync Layer)|  |  |
|  |  +---------------------+  +--------------------+  +------------------+  |  |
|  +------------------------------------------------------------|------------+  |
|                                                               |               |
|      Android Framework APIs                                   |               |
|      (Location, Connectivity, Insets, Hardware Sensors)       |               |
+---------------------------------------------------------------|---------------+
                                                                | HTTPS / TLS 1.3
                                                                v
+-------------------------------------------------------------------------------+
|                       Google Cloud Platform & Firebase                        |
|  +-------------------------------------------------------------------------+  |
|  | Firebase Authentication (Google Sign-In / Credential Manager)           |  |
|  +-------------------------------------------------------------------------+  |
|  | Google Cloud Firestore (Named Database Instance: tripwise-db)           |  |
|  |   - /users/{userId}                                                     |  |
|  |   - /trips/{tripId}                                                     |  |
|  |   - /orders/{orderId}                                                   |  |
|  |   - /emergency_contacts/{contactId}                                     |  |
|  +-------------------------------------------------------------------------+  |
|  | Cloud Firestore Security Rules (Zero-Trust User Document Isolation)     |  |
|  +-------------------------------------------------------------------------+  |
+-------------------------------------------------------------------------------+
```

---

## 3.4 Intended Users and User Characteristics

TripWise is engineered to accommodate four distinct user personas:

1. **Daily Urban and Inter-District Commuters:** Individuals traveling between administrative and industrial hubs (e.g., Dhaka to Gazipur, Narayanganj, or Comilla) who require rapid calculation of daily or weekly fuel consumption and expressway toll expenses.
2. **Long-Distance Road Tourists and Bikers:** Motorcyclists and weekend travelers exploring national tourist corridors (e.g., Dhaka–Mawa–Bhanga, Sylhet, or Cox's Bazar Marine Drive) requiring route safety rules, toll guidance, and itemized travel budgets.
3. **Carpoolers and Shared Group Travelers:** University students, corporate colleagues, and families embarking on shared journeys who need an objective, unambiguous mathematical basis for dividing aggregate travel expenses.
4. **Transport Operators and Paratransit Drivers:** Drivers and small fleet owners operating three-wheelers (CNG) or private hired cars seeking to benchmark fuel efficiency and verify route overhead costs.

---

## 3.5 Operating Environment

The operational parameters for TripWise are defined as follows:
- **Operating System:** Android 8.0 (API Level 26, Oreo) minimum; fully optimized for Android 14 / Android 15 (API Level 34–35).
- **Target SDK:** Android SDK 34 (Upside Down Cake) / 35.
- **Hardware Architecture:** `arm64-v8a`, `armeabi-v7a`, `x86_64`.
- **Display Configurations:** Compact mobile displays ($360\text{ dp}\times 640\text{ dp}$ minimum), foldables, and medium tablets ($600\text{ dp}+$ adaptive width constraints).
- **Network Connectivity:** Compatible with 3G, 4G LTE, 5G, and Wi-Fi networks; fully functional offline via in-memory caching and Firestore offline persistence.

---

## 3.6 Functional Requirements

This section defines the 17 core Functional Requirements (FR-01 through FR-17) governing the TripWise system. Each requirement is assigned a unique identifier, priority level (High, Medium, Low), detailed input specifications, expected system output, and explicit verification method.

### Table 3.1: Complete Functional Requirements Specification

| Req ID | Requirement Description | Priority | Inputs | Expected Output | Verification Method |
| :--- | :--- | :---: | :--- | :--- | :--- |
| **FR-01** | **Application Launch and Home Screen Presentation** | High | System intent launch event. | Render dynamic Material 3 Home Screen featuring header greeting, 4 quick-action cards, 5 smart utilities, and recent trip overview. | System Integration Test / Automated UI Launch Verification. |
| **FR-02** | **Multi-Screen Navigation and Backstack Management** | High | Navigation events via bottom bar, drawer, and action buttons. | Deterministic transition between Home, Stepper, Comparison, History, Analytics, Profile, and Utilities with correct predictive backstack popping. | Automated Jetpack Compose Navigation Test. |
| **FR-03** | **Trip Origin and Destination Data Capture** | High | Text input for Origin (e.g., "Dhaka") and Destination (e.g., "Cox's Bazar"). | Real-time text validation, visual field error indication if blank, and persistence in `TripDraft` state. | Form Validation Unit Test. |
| **FR-04** | **Trip Distance and Passenger Configuration** | High | Numeric input for Distance ($KM > 0$) and Passenger Count ($N \ge 1$). | Numerical parsing, positive range validation, rejection of non-numeric characters, state update in `TripDraft`. | Boundary Value Analysis (e.g., $0, -5, 1, 5000$). |
| **FR-05** | **Vehicle Modality Selection** | High | Selection click on Car, Motorcycle, or CNG Auto-Rickshaw. | Visual radio/card elevation, automatic population of calibrated default fuel economy ($KM/L$) and price. | ViewModel State Transition Unit Test. |
| **FR-06** | **Fuel Price Parameter Specification** | High | Numeric input for retail fuel tariff (BDT per Liter / $\text{m}^3$). | Validated numeric fuel rate assigned to active draft; defaults to 130 BDT (Car/Bike) or 45 BDT (CNG). | Parameterized Unit Test. |
| **FR-07** | **Engine Fuel Efficiency Tuning** | High | Numeric input for fuel mileage ($KM/L$ or $KM/\text{m}^3$). | Validated positive float assigned to draft; defaults to $14\text{ km/L}$ (Car), $40\text{ km/L}$ (Bike), or $28\text{ km/m}^3$ (CNG). | Parameterized Unit Test. |
| **FR-08** | **Itemized Travel Expense Entry** | High | Numeric inputs for Tolls, Parking, Meals/Food, and Other costs. | Dynamic aggregation of supplementary costs, zero-fill for omitted optional categories, state synchronization. | Arithmetic Aggregation Unit Test. |
| **FR-09** | **Progressive 4-Step Wizard Navigation** | High | "Next Step", "Back", and Step indicator interactions. | Sequential forward validation before advancement; graceful backward editing without data loss. | UI Stepper Workflow Integration Test. |
| **FR-10** | **Deterministic Trip Expense Calculation** | High | Validated trip distance, fuel economy, fuel price, tolls, parking, food. | Deterministic computation of fuel volume, fuel cost, gross cost, and per-passenger cost with 2 decimal precision. | Mathematical Algorithmic Unit Test. |
| **FR-11** | **Multi-Modal Vehicular Cost Comparison** | High | Distance parameter in Comparison screen. | Side-by-side computation of fuel volume, total cost, and savings delta across Car, Bike, and CNG simultaneously. | Comparative Matrix Algorithmic Test. |
| **FR-12** | **Trip Summary Presentation and Persistence** | High | Step 4 "Save Trip" button invocation. | Visual itemized breakdown modal; asynchronous persistence to Cloud Firestore; transition to History screen. | Cloud Firestore Repository Integration Test. |
| **FR-13** | **Historical Trip Retrieval and Management** | High | Navigation to Trip History screen; delete/edit clicks. | Reactive list of saved trips fetched from Firestore; real-time deletion and mutation reflected in UI. | SnapshotListener Cloud Data Test. |
| **FR-14** | **Aggregate Trip Analytics and Visual Metrics** | Medium | Navigation to Analytics screen. | Real-time calculation and display of total expenditure, total distance, total fuel consumed, and modality distribution. | Analytical Aggregation Unit Test. |
| **FR-15** | **Robust Input Validation and Error Sanitization** | High | Malformed, negative, non-numeric, or boundary strings. | Prevention of runtime crashes; display of localized inline error messages and disabled navigation triggers. | Negative Fuzzing & Boundary Testing. |
| **FR-16** | **Session and Offline State Preservation** | Medium | Network disconnection, screen rotation, process death. | Uninterrupted calculation capability using local memory state; seamless cloud sync upon network recovery. | Robolectric Lifecycle & Network Disconnection Test. |
| **FR-17** | **Contextual User Feedback and Status Indicators** | High | Network operations, save events, error alerts. | Display of Material 3 Snackbars, progress spinners, dialogs, and validation badges confirming system operations. | Jetpack Compose Semantics UI Test. |

---

## 3.7 Non-Functional Requirements (NFRs)

The non-functional requirements define the quality attributes, operational thresholds, and structural constraints imposed on TripWise.

### 3.7.1 Usability
- **UR-01 (Touch Target Compliance):** All interactive buttons, cards, text fields, and icon toggles must adhere strictly to the Material Design 3 minimum touch target size of $48\text{ dp}\times 48\text{ dp}$.
- **UR-02 (Cognitive Ergonomics):** The 4-step wizard must adhere to progressive disclosure principles, preventing cognitive overload by requesting no more than 3 to 4 related inputs per screen.
- **UR-03 (Visual Hierarchy):** The interface must enforce clear typographical hierarchy utilizing the Google Inter / Roboto type scale, pairing bold numerical headlines with muted descriptive metadata.

### 3.7.2 Performance
- **PR-01 (Calculation Latency):** Mathematical recalculations of fuel volume, gross expenditure, and passenger quotas must execute in less than $16\text{ ms}$ upon input change, guaranteeing a smooth $60\text{--}120\text{ fps}$ Compose render cycle.
- **PR-02 (Cold Startup Time):** The application cold-boot latency from OS intent launch to interactive Home Screen presentation must not exceed $1.8\text{ seconds}$ on standard reference hardware (e.g., Snapdragon 680 / 4GB RAM).
- **PR-03 (Memory Footprint):** Steady-state resident heap memory consumption must remain below $85\text{ MB}$ during active trip calculation and analytics rendering.

### 3.7.3 Reliability and Robustness
- **RR-01 (Zero Crash Tolerance):** The application must exhibit zero unhandled exceptions (`ArithmeticException`, `NumberFormatException`, `NullPointerException`) when parsing unexpected, empty, or extreme user inputs.
- **RR-02 (Offline Resilience):** The application must maintain complete calculation and historical browsing functionality when internet connectivity is severed, caching operations locally and queuing cloud writes.

### 3.7.4 Maintainability and Clean Code
- **MR-01 (Modular Architecture):** The codebase must strictly adhere to Clean Architecture principles with unidirectional data flow (UDF), isolating data repositories, domain calculations, and Compose UI presentations.
- **MR-02 (File Modularity):** Source files must be modularized into distinct, purpose-driven Kotlin classes (ViewModel, Repository, Screen Composables), preventing monolithic files exceeding 500 lines where practical.

### 3.7.5 Compatibility
- **CR-01 (Android Ecosystem Breadth):** The application must maintain binary and runtime compatibility across Android API Level 26 (Android 8.0 Oreo) through API Level 35 (Android 15).
- **CR-02 (Adaptive Layouts):** UI layouts must adapt responsively across compact mobile screens ($360\text{ dp}$), medium devices ($411\text{ dp}$), and wide tablet viewports ($600\text{ dp}+$) using `widthIn(max = 600.dp)` constraints.

### 3.7.6 Security and Privacy
- **SR-01 (Credential Protection):** All user authentication must execute through Google Identity Services / Jetpack Credential Manager; no plain-text passwords or secret credentials shall be captured or stored locally.
- **SR-02 (Zero-Trust Cloud Rules):** Google Cloud Firestore access must be governed by server-side declarative security rules enforcing that users can only read, write, or delete documents where `request.auth.uid == resource.data.userId`.
- **SR-03 (Encrypted Transit):** All client-to-cloud communications must be strictly encrypted using TLS 1.3 over HTTPS.

### 3.7.7 Accessibility
- **AR-01 (Screen Reader Semantics):** Every image, icon button, and graphical visualization must include meaningful `contentDescription` attributes in Jetpack Compose to support Android TalkBack.
- **AR-02 (Contrast Ratios):** All text elements must satisfy WCAG 2.1 Level AA contrast standards (minimum 4.5:1 for normal text, 3.0:1 for large graphical headings).

---

## 3.8 User Requirements

From the perspective of an end traveler, the system must satisfy the following core expectations:
1. *I must be able to plan a complete trip within 60 seconds without having to do manual arithmetic.*
2. *I must be able to see the exact cost difference between taking my bike, my car, or a CNG auto-rickshaw.*
3. *I must be able to split the total travel cost equitably among all traveling passengers with one glance.*
4. *I must be able to view critical highway toll fees and emergency contact numbers when driving on major bridges and expressways.*
5. *I must have my previous trip itineraries preserved securely so I can review monthly travel spending.*

---

## 3.9 Software Requirements

The software environment utilized to develop, build, and execute TripWise includes:
- **Operating System (Build Environment):** Linux (Ubuntu 22.04 LTS x86_64).
- **Programming Language:** Kotlin 2.1.0.
- **UI Toolkit:** Jetpack Compose (Compose BOM 2024.09.00 / Compose Compiler 1.5.8).
- **Design System:** Android Material Design 3 (M3).
- **Build System:** Gradle 8.9 (Kotlin DSL `build.gradle.kts`).
- **Android Gradle Plugin (AGP):** 8.7.0.
- **Cloud Backend:** Google Cloud Firestore SDK (`com.google.firebase:firebase-firestore-ktx:25.1.0`).
- **Authentication:** Firebase Auth (`com.google.firebase:firebase-auth-ktx:23.0.0`) and AndroidX Credential Manager (`androidx.credentials:credentials:1.3.0`).
- **Asynchronous Concurrency:** Kotlin Coroutines 1.8.0 and Kotlin Flow / StateFlow.

---

## 3.10 Hardware Requirements

### Minimum Development and Host System Requirements
- **CPU:** Quad-Core 64-bit x86_64 or ARM processor (Intel Core i5 / AMD Ryzen 5 or equivalent).
- **RAM:** Minimum 8 GB (16 GB recommended for Gradle daemon concurrency).
- **Disk Storage:** Minimum 10 GB available SSD storage for Android SDK, build caches, and Gradle dependencies.

### Target Mobile Device Hardware Requirements
- **Processor:** 64-bit Dual-Core 1.5 GHz or higher (`arm64-v8a` or `armeabi-v7a`).
- **System Memory (RAM):** 2 GB minimum (3 GB+ recommended for optimal animation fluidity).
- **Storage:** 50 MB available internal storage space for APK binary and cached data.
- **Network Interface:** Cellular modem (3G/4G/5G) or Wi-Fi 802.11 b/g/n adapter.
- **Display Resolution:** Minimum $720 \times 1280$ pixels at $\sim 280\text{ ppi}$.

---

## 3.11 External Interface Requirements

### 3.11.1 User Interfaces (UI)
The TripWise user interface is implemented exclusively using Jetpack Compose, adhering to Material 3 styling. Key visual interface guidelines include:
- Standardized top app bars with consistent navigation back affordances.
- Custom canvas-rendered road graphics depicting vehicular travel paths.
- Step-by-step progress indicator badges (1 through 4) visually marking current workflow state.
- Distinct color-coded categorical chips for Car (Blue), Motorcycle (Green), and CNG (Amber).

### 3.11.2 Software Interfaces
- **Android OS System APIs:** `android.content.Intent` for initiating emergency phone calls (`Intent.ACTION_DIAL`), managing window insets (`WindowInsetsCompat`), and system clipboard operations.
- **Google Play Services:** Integrated Credential Manager framework for Google OAuth2 identity tokens.
- **Google Cloud Firestore REST/gRPC API:** Direct TLS-encrypted channel communicating with Google Cloud Firestore database instance `ai-studio-android-tripwise-dfaa7c40-ce7a-4b6d-a0f4-d543f19122d8`.

### 3.11.3 Communication Interfaces
- **Protocol:** HTTP/2 and gRPC transport protocols layered over TLS 1.3 encryption on outbound TCP port 443.
- **Data Serialization:** Structured JSON encoding for network payloads and Firestore document mappings.

---

## 3.12 Data Requirements

The application manages structured data entities representing users, trips, vehicle benchmarks, and emergency records:
- **`Trip` Entity:** `id` (String), `userId` (String), `title` (String), `origin` (String), `destination` (String), `distanceKm` (Double), `passengerCount` (Int), `vehicleType` (Enum: `CAR`, `MOTORCYCLE`, `CNG`), `fuelPricePerUnit` (Double), `mileageKmPerUnit` (Double), `tollCost` (Double), `parkingCost` (Double), `foodCost` (Double), `otherCost` (Double), `totalFuelVolume` (Double), `totalFuelCost` (Double), `totalTripCost` (Double), `costPerPassenger` (Double), `notes` (String), `timestamp` (Long).
- **`VehicleBenchmark` Entity:** `vehicleType` (Enum), `displayName` (String), `defaultMileage` (Double), `defaultFuelPrice` (Double), `fuelUnit` (String), `iconRes` (String).
- **`EmergencyContact` Entity:** `id` (String), `agencyName` (String), `phoneNumber` (String), `category` (String), `jurisdiction` (String).

---

## 3.13 Assumptions and Dependencies

1. **User Input Accuracy:** The system assumes that users supply reasonably accurate estimates for travel distance and retail fuel prices; while default benchmarks are provided, localized road conditions may cause empirical fuel consumption to vary by $\pm 10\%$.
2. **Standard Gravity and Flat Elevation Assumption:** The primary calculation model utilizes thermodynamic average fuel economies and does not dynamically query topological elevation contour maps.
3. **Google Play Services Availability:** Cloud authentication via Credential Manager assumes the presence of modern Google Play Services on the host Android device.
4. **Cloud Firestore Service Continuity:** Cloud persistence depends upon the operational uptime of Google Cloud Firestore services in the `us-west1` region.

---

## 3.14 Constraints

- **Single Currency Assumption:** Financial calculations operate strictly as pure floating-point decimals displayed with the standard local currency symbol (BDT / ৳) or numeric formatting.
- **Memory Footprint Bounds:** Memory allocations must be strictly managed to prevent background eviction on low-tier 2GB RAM devices.
- **Read-Only Third-Party APIs:** Regulatory toll tariffs and bridge speed limits are modeled as verified static domain entities within the client application rather than scraping unauthenticated government portals at runtime.

---

## 3.15 Acceptance Criteria

The TripWise application shall be accepted as complete and production-ready upon satisfying the following verifiable criteria:
1. **Compilation & Packaging:** The codebase compiles cleanly under Gradle 8.9 / AGP 8.7.0 without errors, producing an installable APK (`TripWise.apk`).
2. **Functional Completeness:** All 17 Functional Requirements (FR-01 to FR-17) pass manual and automated verification suites without regression.
3. **Calculation Accuracy:** The computational engine computes fuel volume, fuel expense, gross trip expense, and per-passenger allocation to within $0.01$ currency units matching mathematical ground truth.
4. **Security Verification:** Cloud Firestore security rules prevent unauthorized cross-user document access, validated via formal security test suites.
5. **UI Responsiveness:** All UI interactions respond without observable stutter or frame dropping, with touch targets conforming to $48\text{ dp}$ minimums.

---

## 3.16 Chapter Summary

This chapter presented the comprehensive Software Requirements Specification for TripWise, detailing 17 prioritized functional requirements, nine non-functional requirement dimensions, operational hardware/software configurations, data schemas, and rigorous acceptance criteria. This specification forms the foundational baseline for the architectural modeling and system design presented in Chapter 4.
