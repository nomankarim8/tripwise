# CHAPTER 7: LIMITATIONS AND FUTURE WORK

## 7.1 Introduction

No software engineering endeavor is completely devoid of technical constraints, environmental assumptions, or evolutionary boundaries. While **TripWise: Smart Vehicle & Trip Cost Planner** successfully implements an automated 4-step trip planning wizard, multi-modal vehicular cost comparisons, persistent cloud analytics, and essential highway transit utilities, identifying its current architectural boundaries is essential for framing future research and engineering enhancements.

This chapter provides an objective analysis of the existing limitations of the TripWise system and delineates a strategic technological roadmap for future iterations.

---

## 7.2 Current System Limitations

The current release (v1.0.0) of TripWise exhibits the following operational and architectural limitations:

### 7.2.1 Manual Highway Distance Entry
At present, users manually input travel distance in kilometers (or select pre-configured standard inter-district distances). While this design choice ensures zero-latency offline operation and avoids dependency on external paid map licensing, it introduces the potential for human estimation errors when planning complex itineraries with multiple intermediate waypoints.

### 7.2.2 Flat Topological Energy Modeling
The fuel calculation model employs average thermodynamic engine fuel economy ratings ($KM/L$ or $KM/\text{m}^3$) under standard operating conditions. The algorithm assumes flat terrain and does not account for gravitational resistance when ascending steep topographic gradients (e.g., hill tracts in Bandarban or Sajek) or wind drag variations at high velocities.

### 7.2.3 Static Toll and Regulatory Information
Toll gate tariffs, bridge vehicle classifications, and speed limits are pre-configured based on official gazette tariffs (such as Bangladesh Bridge Authority schedules for the Padma and Bangabandhu bridges). While accurate, these tables require periodic manual application updates when governmental agencies revise statutory toll rates.

### 7.2.4 Absence of Direct Automated Financial Settlement
TripWise accurately calculates equitable per-passenger cost quotas (e.g., ৳837.50 per person). However, the application currently acts as a computational decision-support tool rather than a payment processing gateway. Passengers must execute actual financial transfers through external Mobile Financial Services (MFS) or cash handovers.

### 7.2.5 Single Currency Standardization
The computational engine formats currency in local Bangladeshi Taka (BDT / ৳) and standard decimal units. Cross-border travelers moving between neighboring jurisdictions cannot dynamically toggle live floating foreign exchange rates at runtime.

---

## 7.3 Architectural Roadmap and Future Work

To elevate TripWise from a dedicated mobile utility into an enterprise-grade intelligent mobility ecosystem, the following technical enhancements are prioritized for future releases:

### 7.3.1 Google Maps Distance Matrix and Directions API Integration
Future versions will integrate the **Google Maps Distance Matrix API** and **Directions API**. Users will enter textual origin and destination addresses, and the application will automatically query Google's routing graph to retrieve precise driving distances, polyline paths, and historical traffic congestion profiles.

```kotlin
// Architectural Proposal: Distance Matrix Retrofit Interface
interface GoogleMapsRoutingApi {
    @GET("maps/api/distancematrix/json")
    suspend fun getDistanceMatrix(
        @Query("origins") origin: String,
        @Query("destinations") destination: String,
        @Query("mode") mode: String = "driving",
        @Query("key") apiKey: String
    ): DistanceMatrixResponse
}
```

### 7.3.2 Real-Time Crowd-Sourced Fuel Station Telematics
Incorporating a crowd-sourced telematics network similar to GasBuddy will allow drivers to report real-time retail fuel prices at regional filling stations. A decentralized validation algorithm will verify price updates, ensuring that users always calculate trip budgets using the most up-to-date fuel tariffs.

### 7.3.3 Live Bridge RFID Transponder & Toll API Sync
As national bridge authorities roll out Electronic Toll Collection (ETC) systems linked to vehicle RFID tags, TripWise can interface with authorized toll authority APIs. This will enable travelers to query their prepaid RFID transponder balances directly within the app, alerting them if a recharge is required before approaching a bridge toll plaza.

### 7.3.4 Mobile Financial Services (MFS) Deep Integration
Future iterations will incorporate deep linking and payment gateway integration with major regional Mobile Financial Services platforms, such as **bKash**, **Nagad**, and **Upay**. Upon completing Step 4 of the trip wizard, the app will generate one-tap payment request links or QR payment requests, enabling instant, automated settlement among traveling passengers.

```kotlin
// Architectural Proposal: MFS Intent Deep Linking
fun initiateMfsPayment(context: Context, amount: Double, recipientPhone: String) {
    val uri = Uri.parse("bkash://payment?recipient=$recipientPhone&amount=$amount&ref=TripWise")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
```

### 7.3.5 Bluetooth OBD-II Real-Time Diagnostic Integration
To eliminate estimation inaccuracies in engine fuel economy, TripWise can connect to standard vehicle On-Board Diagnostics (OBD-II) Bluetooth adapters (ELM327). By querying PID `0110` (Mass Air Flow sensor - MAF) and PID `010D` (Vehicle Speed), the app will compute instantaneous, empirical fuel consumption in real time, factoring in actual driving style, air conditioning load, and terrain incline.

### 7.3.6 Cross-Platform Expansion via Compose Multiplatform
While the current implementation leverages native Android Jetpack Compose, the user interface and domain logic can be migrated to **Compose Multiplatform (CMP)**. This will enable 90%+ code sharing across Android, iOS, Desktop (macOS/Windows), and WebAssembly (Wasm), expanding the application's user base.

---

## 7.4 Chapter Summary

This chapter critically examined the current operational boundaries of TripWise, including manual distance entry, static toll schedules, and the absence of direct payment gateways. It then established a concrete architectural roadmap outlining integrations with routing APIs, crowd-sourced fuel networks, automated MFS settlements, vehicle OBD-II telemetry, and Compose Multiplatform expansion. Chapter 8 presents the final conclusions and academic references.
