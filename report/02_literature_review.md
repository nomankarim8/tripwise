# CHAPTER 2: LITERATURE REVIEW AND EXISTING SYSTEM ANALYSIS

## 2.1 Introduction

The convergence of intelligent transportation systems (ITS), mobile geospatial computation, and personal financial management tools has sparked substantial academic and commercial interest over the past two decades. As urban congestion increases and highway infrastructure expands globally, the economic calculation of vehicular transit has become an essential dimension of journey planning. Traveling from an origin to a destination is no longer evaluated solely on the basis of physical distance or temporal latency; modern commuters, logistics managers, and private tourists must evaluate journeys within a multi-constrained optimization space encompassing fuel consumption thermodynamics, variable toll tariffs, terminal parking overheads, vehicle longevity, and equitable group expense apportionment.

This chapter presents a systematic review of the prevailing literature and state-of-the-art commercial software applications in the domains of trip planning, navigation routing, fuel consumption forecasting, and vehicular cost computation. It examines the architectural paradigms, computational models, and user interaction metaphors adopted by existing platforms, evaluates their structural limitations—specifically within the context of emerging markets and regional multi-modal transit ecosystems—and establishes the theoretical and practical motivation for the proposed **TripWise: Smart Vehicle & Trip Cost Planner** platform.

---

## 2.2 Review of Existing Trip Planning Solutions

Contemporary digital trip planning platforms have evolved primarily from geographic information systems (GIS) and global satellite positioning infrastructure. The dominant market solutions include:

### 2.2.1 Google Maps
Developed by Google LLC, Google Maps is the preeminent geospatial navigation platform globally. Its core strength lies in its expansive worldwide road mapping, live crowd-sourced traffic telemetry, dynamic rerouting algorithms, and multi-modal transit routing (walking, cycling, public transit, and driving). In recent iterations, Google Maps has introduced "eco-friendly routing," which leverages machine learning models developed with the U.S. Department of Energy’s National Renewable Energy Laboratory (NREL) to predict fuel consumption curves based on road grade, traffic stop-and-go patterns, and vehicle powertrain type (combustion, hybrid, or electric).

*Limitations in Trip Budgeting:* While Google Maps excels at route discovery and spatial navigation, it does not function as a comprehensive personal financial planner. It does not provide an integrated, deterministic ledger for inputting retail fuel prices, customizing engine fuel economy ($KM/L$), itemizing bridge and expressway tolls, factoring in parking tickets, or dividing aggregate journey costs among a group of passengers. Furthermore, its toll alerts in South Asian jurisdictions remain largely qualitative, alerting users to the existence of a toll plaza without calculating the precise monetary toll tariff corresponding to the specific vehicle classification.

### 2.2.2 Apple Maps
Integrated deeply into the Apple iOS ecosystem, Apple Maps provides vector-based cartographic rendering, turn-by-turn spoken directions, and curated city guides. Similar to Google Maps, it provides estimated arrival times (ETA) influenced by historical and real-time traffic telemetry.

*Limitations in Trip Budgeting:* Apple Maps is structurally bound to proprietary hardware platforms, preventing cross-platform accessibility. Its financial capabilities are minimal; it lacks any native calculation engine for fuel expenditure modeling, parking fee estimation, or passenger cost partitioning, treating vehicular travel purely as a spatio-temporal phenomenon rather than an economic transaction.

### 2.2.3 Waze
Waze, a subsidiary of Google, distinguishes itself through active crowd-sourced telematics. Drivers proactively log road hazards, police speed traps, traffic accidents, construction choke points, and real-time fuel prices at nearby gas stations.

*Limitations in Trip Budgeting:* Although Waze captures localized fuel prices via crowd-sourced reports, it fails to link these retail fuel rates to a structured, multi-variable journey planner. A traveler cannot construct a multi-step itinerary that computes total fuel volume required, aggregates statutory tolls, logs subsistence fees, and archives the financial summary to a persistent cloud profile with historical analytics.

---

## 2.3 Review of Vehicle Cost Estimation Solutions

To address the economic voids left by primary navigation apps, specialized cost estimation and vehicle fleet management applications have emerged:

### 2.3.1 TollGuru
TollGuru is a specialized routing engine developed to calculate toll rates and fuel consumption across commercial trucking and passenger routes, primarily operating across North America, Europe, and select international corridors. It factors in vehicle axle counts, transponder discounts (e.g., E-ZPass), time-of-day toll variations, and estimated fuel usage.

*Limitations in Regional Context:* TollGuru is architected primarily for Western highway corridors characterized by standardized transponder electronic toll collection (ETC) matrices. It does not natively cater to developing regional vehicle demographics—such as Compressed Natural Gas (CNG) paratransit auto-rickshaws or small-displacement commuter motorcycles (100cc–150cc)—which dominate South Asian road networks. Furthermore, TollGuru operates predominantly as a subscription-based enterprise Web API rather than a localized, community-centric mobile utility with offline capabilities and passenger expense sharing.

### 2.3.2 Fuelly and GasBuddy
Fuelly focuses on long-term vehicle maintenance and empirical fuel economy tracking (fuel logging at gas stations to calculate true historical miles-per-gallon). GasBuddy focuses on crowd-sourced retail fuel station price locators with promotional discount cards.

*Limitations in Regional Context:* Both platforms operate retrospectively (logging fuel purchases post-consumption) or locally (finding the cheapest gas pump in a 5-mile radius). Neither provides a prospective trip planning engine where a user can simulate an upcoming $300\text{ km}$ journey, evaluate the cost delta between a sedan and a motorcycle, attach estimated toll and parking tariffs, and instantly review per-seat expense quotas before embarking.

### 2.3.3 Splitwise
While not a transportation application, Splitwise is universally deployed by travelers to manage group expenses and calculate equitable settlements across shared trips.

*Limitations in Transport Integration:* Splitwise is a generic double-entry peer-to-peer ledger. It possesses zero intelligence regarding road distance, vehicle thermodynamic mileage benchmarks, fuel volume math, or statutory toll classifications. Users must independently calculate the fuel and toll costs on external scratchpads or calculators before manually entering the final aggregate sum into Splitwise.

---

## 2.4 Analysis of Existing Systems

To evaluate the operational paradigms of existing solutions systematically, their architectural properties, algorithmic dependencies, and primary failure modes are classified in Table 2.1.

### Table 2.1: Analytical Breakdown of Existing Systems

| Application / Platform | Primary Domain | Core Architectural Paradigm | Strengths | Primary Failure Modes & Limitations |
| :--- | :--- | :--- | :--- | :--- |
| **Google Maps** | Navigation & GIS | Distributed Cloud Microservices, Android/iOS Client | Real-time traffic, global maps, turn-by-turn guidance, eco-routing. | No vehicle mileage customization; no itemized toll/parking/food budget ledger; no passenger split math. |
| **Waze** | Social Telematics | Crowd-Sourced Event Streaming, Cloud Backend | Real-time hazard reporting, crowd-sourced fuel station prices. | Ephemeral data; no structured 4-step trip planning wizard; no multi-modal comparative analytics. |
| **TollGuru** | Toll & Fleet Logistics | Cloud GIS Routing Engine with Toll Tariff Database | Granular truck axle/transponder toll rates for North American/EU corridors. | Enterprise focus; lack of South Asian vehicle types (CNG three-wheelers); complex paywalled UI. |
| **Fuelly** | Fleet Maintenance | Client-Server Relational Database (Mobile App) | High-precision historical fuel economy tracking; maintenance reminders. | Retrospective logging only; cannot predict prospective trip budgets or perform multi-vehicle simulations. |
| **Splitwise** | Financial Settlement | Cloud RDBMS, Graph Debt Simplification Algorithm | Robust group bill splitting, multi-currency support, debt ledger. | Zero transportation intelligence; requires manual external calculation of fuel, tolls, and parking costs. |

---

## 2.5 Limitations of Existing Approaches

A thorough synthesis of existing commercial applications reveals five acute structural deficiencies:

1. **Fragmentation of the Traveler Workflow:** At present, an individual organizing an inter-district road journey must consult at least three distinct, uncoordinated software tools: a mapping application (e.g., Google Maps) to determine geographic distance; a standalone calculator or spreadsheet to multiply distance by fuel consumption and add toll tariffs; and a group financial tool (e.g., Splitwise) to divide the gross cost among passengers. This fragmented workflow induces cognitive friction, transcription errors, and planning fatigue.
2. **Neglect of Regional Multi-Modal Vehicle Demographics:** Existing international platforms are designed around Western transportation taxonomies (petrol/diesel SUVs, sedans, trucks, and electric vehicles). In South Asian and developing Southeast Asian nations, regional travel relies heavily on distinct vehicular classes:
   - High-efficiency commuter motorcycles ($100\text{--}160\text{ cc}$) with fuel economies exceeding $40\text{ km/L}$.
   - Paratransit three-wheelers operating on Compressed Natural Gas (CNG auto-rickshaws), measured in cubic meters ($\text{m}^3$) rather than liquid liters, operating under regulated tariff structures.
   - Private passenger cars and microbuses subject to vehicle-category-specific toll rates at national river bridges (e.g., Bangabandhu Bridge, Padma Bridge).
3. **Absence of Real-Time Multi-Modal Cost Delta Simulation:** Travelers frequently face the dilemma of choosing the most economical travel modality for a specific itinerary (e.g., *"Should I take my motorcycle or drive the family sedan for this 250 km trip? How much money will I save?"*). Existing applications do not provide a synchronized side-by-side comparative simulation matrix that computes fuel volume, total financial outlay, and percentage savings across modalities simultaneously.
4. **Disconnection from Vital Highway Transit and Safety Utilities:** Navigation engines prioritize path optimization while ignoring vital en-route utilities. Drivers entering expressways need immediate access to digital toll RFID passes, official speed limits, highway lane regulations, and direct one-touch emergency response helplines (such as national emergency response 999 or Highway Police divisions).
5. **Lack of Localized Cloud Persistence and Real-Time Sync:** Most casual calculation tools rely on volatile in-memory storage or local text scratchpads. When a traveler switches devices or experiences application termination, their meticulously calculated trip itineraries and expense breakdowns are lost. Existing systems lack a light, reactive, cloud-synchronized repository with automated statistical aggregations (total expenditure, fuel consumption trends, distance traveled).

---

## 2.6 The Proposed TripWise System

To resolve the structural deficiencies identified in existing platforms, this project develops **TripWise: Smart Vehicle & Trip Cost Planner**. TripWise is architected from the ground up as a native Android application built using **Kotlin 2.1.0** and **Jetpack Compose (Material Design 3)**, backed by **Google Cloud Firestore** and **Firebase Authentication**.

The foundational innovations of TripWise include:
- **Integrated 4-Step Trip Planning Wizard:** A guided progressive disclosure stepper that seamlessly transitions the user through:
  - *Step 1 (Trip Details):* Origin, destination, distance in kilometers, passenger count, and optional notes.
  - *Step 2 (Vehicle Selection):* Modality selection (Sedan Car, Commuter Motorcycle, CNG Auto-Rickshaw) with pre-calibrated default mileage benchmarks ($14\text{ km/L}$, $40\text{ km/L}$, and $28\text{ km/L}$ respectively) and adjustable retail fuel prices.
  - *Step 3 (Itemized Expenses):* Granular entry of statutory tolls, terminal parking, meals/refreshments, and contingency expenditures.
  - *Step 4 (Review & Apportionment):* Comprehensive financial ledger detailing fuel liters required, fuel expenditure, toll overhead, total trip budget, and exact per-passenger cost quota, with instant persistence to Cloud Firestore.
- **Side-by-Side Multi-Modal Comparative Engine:** An interactive simulation screen allowing instant comparative modeling across Cars, Motorcycles, and CNG auto-rickshaws for any arbitrary distance, detailing exact fuel volume deltas, financial outlays, and net savings.
- **Embedded Highway Utilities Suite:** A suite of travel utilities including a Dhaka–Chittagong–Cox's Bazar Waypoint Route Map, a digital RFID/QR toll gate boarding pass, an interactive Padma Bridge speed camera and safety advisory module, and one-touch emergency dispatch helplines.
- **Reactive Cloud Architecture:** Cloud Firestore integration with offline cache-first capabilities, secured through zero-trust security rules, guaranteeing that user itineraries, historical analytics, and profile data remain synchronized across devices.

---

## 2.7 Comprehensive Comparative Analysis

Table 2.2 provides an extensive, multi-parameter comparative evaluation of the proposed TripWise application against existing market solutions across ten core software and domain criteria.

### Table 2.2: Comprehensive Feature Comparison Matrix

| Evaluation Criteria | Google Maps | Waze | TollGuru | Fuelly | Splitwise | Proposed TripWise System |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **Native Android Modern Architecture** | Yes (Proprietary) | Yes (Proprietary) | Hybrid/Web | Hybrid | Yes (Native) | **Yes (Kotlin 2.1.0 + Jetpack Compose M3)** |
| **Guided 4-Step Trip Budgeting Wizard** | No | No | No | No | No | **Yes (Structured Progressive Disclosure)** |
| **Custom Fuel Efficiency ($KM/L$) Tuning** | No | No | Partial | Yes | No | **Yes (Fully Editable with Smart Defaults)** |
| **Multi-Modal Vehicular Comparison** | No | No | No | No | No | **Yes (Sedan Car, Motorcycle, CNG Rickshaw)** |
| **Granular Expense Itemization (Tolls/Parking/Food)** | No | No | Tolls Only | Maintenance Only | Manual Entry | **Yes (Itemized Dedicated Input Ledger)** |
| **Automated Per-Passenger Cost Splitting** | No | No | No | No | Yes (Manual Input) | **Yes (Deterministic Dynamic Allocation)** |
| **Historical Trip Analytics & Charts** | No | No | Enterprise Reports | Maintenance Logs | Debt Graphs | **Yes (Real-Time Aggregate Charts & Stats)** |
| **Localized Highway & Bridge Utilities** | Partial | Road Hazards | No | No | No | **Yes (Padma Bridge Guide, Waypoints, RFID Pass)** |
| **Emergency Highway Dispatch Helplines** | No | Roadside Assist | No | No | No | **Yes (One-Touch Emergency 999/Police Integration)** |
| **Zero-Trust Cloud Database Synchronization** | Cloud Sync | Cloud Sync | Cloud Sync | Cloud Sync | Cloud Sync | **Yes (Cloud Firestore + Google Sign-In)** |

---

## 2.8 Advantages and Potential Contributions of TripWise

The deployment of TripWise introduces substantial functional, economic, and technical contributions to mobile software engineering and personal travel planning:

1. **Elimination of Cognitive Load:** By synthesizing spatial travel parameters (distance, route) with thermodynamic vehicle efficiency and financial variables into a unified 4-step wizard, TripWise reduces trip planning time from minutes of cross-app switching to seconds of guided input.
2. **Empowerment of Regional Travelers:** Integrating South Asian vehicle modalities (specifically the CNG three-wheeler and commuter motorcycle) fills a significant technological void left by Western-centric mapping software, democratizing precision cost planning for millions of commuters in developing economies.
3. **Data Transparency and Conflict Reduction:** The automated per-passenger apportionment ledger establishes unambiguous, mathematical clarity for shared travel expenses, preventing disputes and fostering equitable cost sharing among friends, colleagues, and carpooling groups.
4. **Enhanced Road Safety and Regulatory Compliance:** Incorporating verified speed limit advisories, bridge safety rules, and direct emergency dispatch hotlines directly into the travel dashboard elevates TripWise from a mere financial calculator to an end-to-end highway companion.
5. **Architectural Exemplar for Modern Android Engineering:** TripWise provides a fully realized, production-grade reference implementation demonstrating Clean Architecture, MVVM with Kotlin StateFlow, declarative UI with Material 3, and zero-trust cloud data persistence using Google Cloud Firestore.

---

## 2.9 Chapter Summary

This chapter conducted a systematic literature review and comparative analysis of existing trip planning, navigation, vehicle management, and expense splitting systems. The review demonstrated that while existing platforms excel in isolated domains—such as spatial routing (Google Maps), crowd-sourced telematics (Waze), commercial fleet tolling (TollGuru), or peer-to-peer debt tracking (Splitwise)—none offer a cohesive, unified solution that combines prospective vehicle budgeting, multi-modal cost comparisons, localized highway utilities, and cloud data synchronization. 

To overcome these structural limitations, the proposed TripWise system was conceptualized, combining an intuitive 4-step wizard, regional vehicular calibration, automated passenger apportionment, and reactive cloud infrastructure. The next chapter defines the formal Software Requirements Specification (SRS) governing the functional, non-functional, and interface behaviors of TripWise.
