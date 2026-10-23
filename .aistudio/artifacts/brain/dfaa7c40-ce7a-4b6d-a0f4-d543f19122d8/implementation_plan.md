# TripWise: Smart Vehicle & Trip Cost Planner — Software Engineering Project Report Plan

A comprehensive, university-level academic Software Engineering project report covering the design, architecture, implementation, mathematical models, verification, and documentation of the **TripWise** application.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following key decisions were confirmed during the interactive clarification phase:
> - **Platform Architecture**: The report strictly documents the verified, implemented native **Android (Kotlin 2.1.0 + Jetpack Compose + Material 3)** codebase and its **Firebase Authentication / Cloud Firestore** backend integration, maintaining total truth-in-code fidelity.
> - **Deliverable Format**: Delivered as an exhaustive, publication-ready **Markdown report (`TripWise_Software_Engineering_Project_Report.md`)** alongside an automated **DOCX generator script (`build_docx_report.py`)** for Microsoft Word export and university formatting compliance.
> - **Institutional Details**: Formatted using clean, standardized brackets (`[UNIVERSITY NAME]`, `[DEPARTMENT NAME]`, `[STUDENT NAME]`, `[STUDENT ID]`, `[SUPERVISOR NAME]`) for instant student personalization.

---

## 1. Overview & Core Concept

**TripWise: Smart Vehicle & Trip Cost Planner** is an Android mobile application engineered using Kotlin and Jetpack Compose to solve the problem of unpredictable travel expenses, inefficient vehicle selection, and fragmented travel budgeting across inter-city and highway journeys in Bangladesh.

### Key Value & Objectives
1. **Dynamic 4-Step Trip Wizard**: Guides travelers through Step 1 (From, To, Distance KM, Passengers), Step 2 (Vehicle Category: Sedan, Motorcycle, CNG Auto Rickshaw), Step 3 (Fuel Price, Highway Tolls, Parking, Food & Miscellaneous), and Step 4 (Review, Total Cost Breakdown in ৳ BDT, and Per-Passenger Expense Split).
2. **Vehicle Efficiency & Cost Benchmarking**: Provides an instant comparative analysis tool across vehicle categories with real-time fuel efficiency formulas ($KM/L$).
3. **Trip Expense Lifecycle & Analytics**: Captures complete historical journey data, computes distance and expense statistics, and visualizes expense distribution through Compose canvas charts.
4. **Smart Travel Utilities & Highway Integration**: Features an interactive GPS waypoint route map for expressways (e.g., Dhaka–Padma Bridge–Chittagong–Cox's Bazar), digital RFID/QR toll boarding passes, an interactive Padma Bridge speed-camera safety guide, and 24/7 highway emergency speed-dial hotlines (999, highway police).
5. **Cloud Synchronization & Secure Auth**: Synchronizes journey records, marketplace bookings, and emergency contacts with Firebase Cloud Firestore backed by Google Sign-In via Jetpack Credential Manager and zero-trust Firestore security rules.

---

## 2. System Architecture & Diagram Strategy

The report includes complete, verified UML and system diagrams rendered in Mermaid format with accompanying technical rationale.

```
┌────────────────────────────────────────────────────────────────────────┐
│                   TRIPWISE HIGH-LEVEL ARCHITECTURE                     │
└────────────────────────────────────────────────────────────────────────┘

 [ Presentation Layer (Jetpack Compose / M3) ]
  ├── Screens: Splash, Onboarding, Auth, Home, 4-Step Wizard, Compare,
  │            History, Analytics, Marketplace, RouteMap, QrTicket, VideoGuide,
  │            EmergencyContacts, Settings, Profile
  ├── State Observation: StateFlow / collectAsState()
  └── Components: FeatureCard, StepIndicator, TripWiseDrawer, BottomNav, HeaderBanner
                           │
                           ▼
 [ Domain & Presentation Logic Layer ]
  ├── TripViewModel: Navigation Stack, Active Tab, Reactive UI State, Form Actions
  └── GoogleAuthHelper: Jetpack CredentialManager + GetSignInWithGoogleOption
                           │
                           ▼
 [ Data Layer (Repository Pattern) ]
  ├── TripRepository: In-Memory Flow State + Dual-Channel Cloud Sync
  │    ├── TripData, TripHistoryItem, User, AppSettings
  │    ├── TravelItem, CartItem, BookingOrder, EmergencyContact
  │    └── Offline-First Fallback Engine
                           │
                           ▼
 [ Backend & Infrastructure Layer ]
  ├── Firebase Authentication (Google Sign-In Provider)
  ├── Cloud Firestore Database (Named: ai-studio-android-tripwise-...)
  └── Zero-Trust Security Rules (Role/Ownership Verification + Diff Guards)
```

### Diagrams Covered in Chapter 4:
1. **Use Case Diagram**: Actor (Traveler/Driver) interactions with Trip Wizard, Vehicle Comparison, History, Analytics, Marketplace, Toll Pass, and Emergency SOS.
2. **System Architecture Diagram**: Clean 3-tier layering (UI Compose Layer $\rightarrow$ ViewModel/StateFlow $\rightarrow$ Repository $\rightarrow$ Local Cache / Cloud Firestore).
3. **Activity Diagram**: 4-Step Trip Planning workflow from origin/destination validation through vehicle selection, live cost calculation, and history persistence.
4. **Sequence Diagram**: User interaction during Step 3 & 4 calculation, repository persistence, and Firestore document write with security rule validation.
5. **Data Flow Diagram (Level 0 & Level 1 DFD)**: Input stream processing (distance, mileage, tariffs) yielding intermediate fuel costs, aggregate trip cost, per-passenger split, and historical records.
6. **Trip Cost Calculation Flowchart**: Detailed conditional flowchart capturing zero/negative input validation, mileage division, and expense summation.
7. **Class Diagram**: `TripViewModel`, `TripRepository`, `TripData`, `TripHistoryItem`, `VehicleType`, `User`, `BookingOrder`, `EmergencyContact`, and `GoogleAuthHelper`.
8. **Entity-Relationship Diagram (ERD)**: Firestore collections (`users`, `trips`, `orders`, `emergency_contacts`) with primary keys, foreign keys (`userId`), attributes, and cardinality.

---

## 3. Mathematical Model & Cost Calculation Algorithm

The report provides formal mathematical formulations, pseudocode, and boundary condition proofs for all calculations implemented in the codebase:

### 1. Fuel Consumption & Cost
$$\text{Fuel Volume (L)} = \frac{\text{Distance } (km)}{\text{Vehicle Mileage } (km/L)}$$
$$\text{Fuel Cost } (\text{৳}) = \text{Fuel Volume (L)} \times \text{Fuel Price per Liter } (\text{৳}/L)$$

### 2. Total Trip Cost
$$\text{Total Trip Cost } (\text{৳}) = \text{Fuel Cost} + \text{Toll Cost} + \text{Parking Cost} + \text{Food \& Other Costs}$$

### 3. Per-Passenger Split
$$\text{Cost Per Passenger } (\text{৳}) = \frac{\text{Total Trip Cost}}{\max(1, \text{Passengers})}$$

### 4. Vehicle Efficiency Comparison Metric
$$\text{Cost Difference } (\Delta C) = \text{Cost}_{\text{Vehicle A}} - \text{Cost}_{\text{Vehicle B}}$$
$$\text{Savings Percentage } (\%) = \left( \frac{\text{Cost}_{\text{Reference}} - \text{Cost}_{\text{Alternative}}}{\text{Cost}_{\text{Reference}}} \right) \times 100$$

Edge cases such as zero distance ($D = 0$), negative inputs ($D < 0$), zero passengers ($P = 0$), and division-by-zero guards on vehicle mileage are formally documented with code references to `TripData.kt` and `TripViewModel.kt`.

---

## 4. Academic Report Chapter Structure (35+ Pages Target)

The report adheres strictly to undergraduate Computer Science & Software Engineering thesis and capstone standards:

### PRELIMINARY PAGES
- Cover Page / Title Page (with institutional placeholders)
- Approval / Certification Page
- Declaration of Originality
- Acknowledgement
- Abstract (Background, Problem, Proposed Solution, Methodology, Results)
- Table of Contents
- List of Figures (with exact numbering)
- List of Tables (with exact numbering)
- List of Abbreviations & Acronyms (SRS, M3, MVVM, API, DFD, ERD, CRUD, etc.)

### CHAPTER 1: INTRODUCTION
- 1.1 Introduction
- 1.2 Background of the Project (Road travel in Bangladesh, highway expansion, Padma Bridge, fuel price dynamics)
- 1.3 Problem Statement (Lack of pre-trip budgeting, unexpected tolls, difficulty comparing vehicle options)
- 1.4 Project Aim and Objectives
- 1.5 Project Scope (In-scope features vs. out-of-scope boundaries)
- 1.6 Motivation
- 1.7 Target Users (Solo drivers, family travelers, tour organizers, fleet operators)
- 1.8 Significance of the Project
- 1.9 Expected Outcomes
- 1.10 Report Organization

### CHAPTER 2: LITERATURE REVIEW AND EXISTING SYSTEM ANALYSIS
- 2.1 Introduction
- 2.2 Review of Existing Trip Planning Solutions (Google Maps, TollGuru, TripIt)
- 2.3 Review of Vehicle Cost Estimation Solutions
- 2.4 Analysis of Existing Systems (Strengths and weaknesses)
- 2.5 Limitations of Existing Approaches (Lack of localized BDT currency, toll booth specifics, CNG/Bike fuel profiles)
- 2.6 Proposed TripWise System
- 2.7 Comprehensive Comparative Feature Matrix (Table 2.1)
- 2.8 Advantages and Technical Contributions of TripWise
- 2.9 Chapter Summary

### CHAPTER 3: SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
- 3.1 Purpose of the SRS
- 3.2 Product Scope & Perspective
- 3.3 Intended Users & Characteristics
- 3.4 Operating Environment (Android 8.0+ / API 26–36)
- 3.5 Functional Requirements (FR-01 to FR-17 evaluated with Requirement ID, Description, Priority, Inputs, Outputs, and Verification Method)
- 3.6 Non-Functional Requirements (Usability, Performance, Reliability, Security, Maintainability, Accessibility)
- 3.7 User Requirements & Use Scenarios
- 3.8 Software & Hardware Interface Requirements
- 3.9 Data Requirements & Schema Constraints
- 3.10 Assumptions, Dependencies, and Constraints
- 3.11 Acceptance Criteria
- 3.12 Chapter Summary

### CHAPTER 4: SYSTEM ANALYSIS AND DESIGN
- 4.1 System Analysis & Requirements Engineering
- 4.2 Architectural Design (MVVM Pattern, Compose Reactive UI, Repository Pattern)
- 4.3 Use Case Modeling & Detailed Use Case Specifications
- 4.4 Activity Diagram & Workflow Analysis
- 4.5 Sequence Diagrams (Trip Planning & Firestore Sync)
- 4.6 Data Flow Diagrams (Context Level DFD & Level 1 DFD)
- 4.7 Trip Cost Calculation Flowchart
- 4.8 Detailed Class Diagram & Object Design
- 4.9 Entity-Relationship Diagram (ERD) & Database Schema Design
- 4.10 UI/UX Design System (M3 tokens, Color Palette, Typography, Spatial Grids)
- 4.11 Navigation Hierarchy & State Machine
- 4.12 Mathematical Cost Model & Invariants
- 4.13 Design Decisions & Architectural Justifications
- 4.14 Chapter Summary

### CHAPTER 5: SYSTEM IMPLEMENTATION
- 5.1 Introduction & Engineering Methodology (Agile Scrum / Incremental)
- 5.2 Development Environment & Toolchain (Android SDK 36, Kotlin 2.1.0, Gradle 9.3.1)
- 5.3 Technology Stack (Jetpack Compose, Coroutines, Flow, Firebase BoM, CredentialManager)
- 5.4 Directory & Package Hierarchy
- 5.5 UI Implementation (Canvas illustrations, Stepper, Feature Cards, Utility Tiles)
- 5.6 Trip Planning Wizard Implementation (`Step1` to `Step4` code mechanics)
- 5.7 Vehicle Selection & Benchmark Logic
- 5.8 Cost Calculation Algorithm Implementation
- 5.9 Vehicle Comparison Implementation
- 5.10 Trip History & Editing Implementation (`TripEditScreen`)
- 5.11 Analytics & Data Visualization Implementation
- 5.12 Smart Utilities (GPS Route Map, QR Toll Pass, Video Guide, Emergency Contacts)
- 5.13 Firebase Cloud Firestore & Google Sign-In Implementation
- 5.14 Zero-Trust Security Rules Implementation
- 5.15 Error Handling, Form Validation & Exception Policies
- 5.16 Key Source Code Excerpts with Technical Walkthroughs
- 5.17 Build, Packaging & Execution Guidelines (`TripWise.apk`)
- 5.18 Chapter Summary

### CHAPTER 6: SYSTEM TESTING AND RESULTS
- 6.1 Testing Objectives & Quality Assurance Strategy
- 6.2 Testing Environment & Testbeds
- 6.3 Unit Testing & Validation Suite
- 6.4 Security Rules Testing (`firestore.test.js` under `@firebase/rules-unit-testing`)
- 6.5 Functional & Boundary Value Testing (BVT)
- 6.6 User Interface & Accessibility Testing
- 6.7 Comprehensive Test Case Execution Matrix (20+ granular test cases with Pass/Fail status)
- 6.8 Requirements Traceability Matrix (RTM) linking FR-01 through FR-17 to Test Cases
- 6.9 Analysis of Test Results & Quality Metrics
- 6.10 Known Deficiencies & Engineering Limitations
- 6.11 Chapter Summary

### CHAPTER 7: LIMITATIONS AND FUTURE ENHANCEMENTS
- 7.1 Current System Limitations
- 7.2 Technical & Computational Constraints
- 7.3 Data Accuracy & Real-Time Fuel Tariff Constraints
- 7.4 Future Enhancement Roadmap (Google Maps Distance Matrix API, OCR Toll Receipt Scanning, Multi-currency, Automated Expense Sharing via SMS/WhatsApp)
- 7.5 Scalability & Production Cloud Architecture
- 7.6 Chapter Summary

### CHAPTER 8: CONCLUSION
- 8.1 Project Summary
- 8.2 Evaluation of Objectives Achievement
- 8.3 Summary of Delivered Engineering Artifacts
- 8.4 Academic & Software Engineering Learning Outcomes
- 8.5 Concluding Remarks

### REFERENCES & BIBLIOGRAPHY
- Comprehensive IEEE-style citations (official Android developer documentation, Jetpack Compose guides, Firebase security documentation, software engineering literature).

### APPENDICES
- Appendix A: Complete Installation & APK Deployment Guide
- Appendix B: Application User Manual with Walkthrough
- Appendix C: Complete Requirements Traceability Matrix (RTM)
- Appendix D: Full Security Rules Definition (`firestore.rules`)
- Appendix E: Core Kotlin Data Layer Source Code Snippets

---

## 5. Deliverables & Execution Plan

1. **`TripWise_Software_Engineering_Project_Report.md`**: Complete, exhaustive ~35+ page Markdown document covering all 8 chapters, diagrams, tables, and appendices.
2. **`build_docx_report.py`**: A specialized Python script using `python-docx` that converts the report into a styled Word document (`TripWise_Software_Engineering_Project_Report.docx`) with standard academic typography, tables, and borders.
3. **Execution**: Run the Python script to build the DOCX document and verify all artifacts are ready for the student's review and university submission.
