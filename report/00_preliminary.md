# PRELIMINARY PAGES

---

## COVER PAGE / TITLE PAGE

```
                                  [UNIVERSITY NAME]
                   [FACULTY OF ENGINEERING AND APPLIED SCIENCES]
                 DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING

                                 PROJECT REPORT
                                       ON
                 TRIPWISE: SMART VEHICLE & TRIP COST PLANNER
             An Intelligent Native Android Decision-Support System
                         for Multi-Modal Travel Budgeting

                               COURSE CODE: [CSE-3200]
                         COURSE TITLE: SOFTWARE ENGINEERING

SUBMITTED BY:
[STUDENT NAME(S)]
Student ID(s): [STUDENT ID(S)]
Program: Bachelor of Science in Computer Science and Engineering (B.Sc. in CSE)
Batch / Semester: [SEMESTER / BATCH / ACADEMIC YEAR]

SUPERVISED BY:
[SUPERVISOR NAME]
[SUPERVISOR DESIGNATION / TITLE]
Department of Computer Science and Engineering
[UNIVERSITY NAME]

DATE OF SUBMISSION:
[SUBMISSION DATE, e.g., October 2026]
```

---

## APPROVAL / CERTIFICATION PAGE

This is to certify that the project entitled **“TripWise: Smart Vehicle & Trip Cost Planner”**, submitted by **[STUDENT NAME(S)]**, bearing Student ID(s) **[STUDENT ID(S)]**, to the Department of Computer Science and Engineering, **[UNIVERSITY NAME]**, is a bona fide record of the work carried out under my supervision in partial fulfillment of the academic requirements for the course **Software Engineering ([COURSE CODE])**.

The results embodied in this report have been verified through rigorous static source code analysis, software requirements inspection, architectural modeling, and functional verification against the compiled native Android application. To the best of my knowledge, this report reflects genuine engineering methodology and original implementation.


\
__________________________________  
**[SUPERVISOR NAME]**  
[SUPERVISOR DESIGNATION]  
Department of Computer Science and Engineering  
[UNIVERSITY NAME]  
Date: ________________________  


\
__________________________________  
**[HEAD OF DEPARTMENT / COURSE COORDINATOR NAME]**  
Professor and Head of the Department  
Department of Computer Science and Engineering  
[UNIVERSITY NAME]  
Date: ________________________  

---

## DECLARATION OF ORIGINALITY

We, the undersigned, hereby solemnly declare that the project work presented in this report entitled **“TripWise: Smart Vehicle & Trip Cost Planner”** is our own original work completed under the academic supervision of **[SUPERVISOR NAME]**, [SUPERVISOR DESIGNATION], Department of Computer Science and Engineering, **[UNIVERSITY NAME]**.

We explicitly confirm that:
1. This report and its corresponding software implementation have been prepared solely for academic credit in the course **Software Engineering ([COURSE CODE])** and have not been submitted previously to any other university, institute, or organization for the award of any degree, diploma, or certificate.
2. All substantive architectural concepts, algorithmic formulations, graphical layouts, source code, and evaluation metrics derived from the contributions of other individuals, open-source libraries, or scholarly publications have been distinctly acknowledged, properly quoted, and comprehensively cited in the References section in strict accordance with academic ethics and the IEEE referencing style.
3. No proprietary credentials, API private keys, authentication tokens, or institutional credentials have been exposed in this document.
4. All empirical metrics, code excerpts, mathematical formulas, and test cases reported herein correspond to the verified native Android project codebase (`com.example.tripwise`).


\
__________________________________  
**[STUDENT NAME(S)]**  
Student ID(s): [STUDENT ID(S)]  
Department of Computer Science and Engineering  
[UNIVERSITY NAME]  
Date: ________________________  

---

## ACKNOWLEDGEMENT

First and foremost, we express our profound gratitude to Almighty Creator for providing us with the health, perseverance, and intellectual capability required to conceptualize, design, implement, and document this project successfully.

We extend our sincere and heartfelt appreciation to our respected supervisor, **[SUPERVISOR NAME]**, [SUPERVISOR DESIGNATION], Department of Computer Science and Engineering, **[UNIVERSITY NAME]**, for their invaluable academic guidance, constructive critique, and continuous encouragement throughout every phase of the software engineering life cycle. Their insightful perspectives on software architecture, clean MVVM principles, and zero-trust cloud security have fundamentally shaped the engineering rigor of this project.

We also convey our sincere thanks to **[HEAD OF DEPARTMENT NAME]**, Head of the Department of Computer Science and Engineering, as well as the esteemed faculty members and laboratory technical officers of **[UNIVERSITY NAME]**, who provided the computational facilities, academic curriculum, and development infrastructure necessary for mobile software engineering.

Our deep appreciation goes to our families, fellow students, and peers whose moral support, constructive feedback during user interface evaluation, and patient encouragement remained a vital source of motivation throughout long development and testing sessions.

Finally, we acknowledge the global open-source and developer communities behind the Kotlin programming language, the Jetpack Compose UI toolkit, and the Google Firebase ecosystem, whose robust documentation and tooling made it possible to build and deliver a high-quality, modern native mobile application.

\
**[STUDENT NAME(S)]**  
Department of Computer Science and Engineering  
[UNIVERSITY NAME]  

---

## ABSTRACT

Accurate, pre-trip expenditure estimation and intelligent vehicle selection represent significant challenges for daily commuters, highway road-trippers, and inter-city travelers. In developing transportation networks—such as the rapidly modernizing national highway corridors in Bangladesh—travelers routinely face unpredictable journey expenses due to fluctuating fuel tariffs, variable expressway toll regimes (e.g., the Padma Multipurpose Bridge and Bangabandhu Expressway), and disparate vehicle mileage profiles across cars, motorcycles, and CNG auto-rickshaws. Existing mapping solutions provide navigational routing but omit localized micro-expense modeling, multi-vehicle financial comparisons, and per-passenger budget splitting.

To resolve these systemic inefficiencies, this project presents **TripWise: Smart Vehicle & Trip Cost Planner**, an intelligent, native Android mobile application engineered following modern Software Engineering principles, Clean Architecture, and the Model-View-ViewModel (MVVM) design pattern. Implemented in Kotlin 2.1.0 using the Jetpack Compose declarative UI toolkit (Material Design 3), TripWise delivers a structured 4-Step Trip Planning Wizard: (1) Origin, Destination, Distance, and Passenger enumeration; (2) Vehicle Category Selection with calibrated fuel-efficiency benchmarks ($14\text{ km/L}$ for Sedan Cars, $40\text{ km/L}$ for Motorcycles, and $28\text{ km/L}$ for CNG Auto-Rickshaws); (3) Dynamic Expense Itemization capturing real-time fuel tariffs, toll gate fees, parking costs, and incidental expenses; and (4) an Aggregate Review Summary displaying total trip expenditure in Bangladeshi Taka ($\text{৳}$ BDT) alongside an automated per-passenger cost split.

Beyond core cost estimation, TripWise incorporates: a side-by-side Vehicle Comparison Engine; a historical Trip Log with in-place mutation and deletion; an Analytics Dashboard featuring graphical expense distributions; and localized Smart Travel Utilities including an interactive GPS Waypoint Route Map (Dhaka–Padma Bridge–Chittagong–Cox's Bazar), digital RFID/QR toll gate boarding passes, an interactive expressway safety guide, and 24/7 one-touch emergency helpline speed-dialers (999, Highway Police). The system implements an offline-first data persistence architecture synchronized with Google Cloud Firestore Enterprise via authenticated Google Sign-In (Jetpack Credential Manager) and zero-trust Firestore Security Rules.

The software was validated through a comprehensive quality assurance regimen incorporating formal Software Requirements Specification (SRS with 17 Functional Requirements and 8 Non-Functional Requirement categories), automated JavaScript security rules unit testing (`firestore.test.js` under `@firebase/rules-unit-testing`), boundary-value testing, and end-to-end user journey verification. The application successfully compiles into a standalone, production-ready Android Package (`TripWise.apk`, 22 MB) compatible with Android API 26 through API 36. This report provides an exhaustive, academically rigorous record of the project's requirements engineering, system analysis, architectural modeling, algorithmic derivation, implementation mechanics, test execution, and future roadmap.

**Keywords:** Mobile Software Engineering, Trip Cost Estimation, Android, Kotlin, Jetpack Compose, MVVM Architecture, Cloud Firestore, Google Sign-In, Material Design 3, Zero-Trust Security Rules.

---

## TABLE OF CONTENTS

- **PRELIMINARY PAGES**
  - Cover Page / Title Page
  - Approval / Certification Page
  - Declaration of Originality
  - Acknowledgement
  - Abstract
  - Table of Contents
  - List of Figures
  - List of Tables
  - List of Abbreviations and Acronyms
- **CHAPTER 1: INTRODUCTION**
  - 1.1 Introduction
  - 1.2 Background of the Project
  - 1.3 Problem Statement
  - 1.4 Project Aim and Objectives
  - 1.5 Project Scope
  - 1.6 Motivation
  - 1.7 Target Users
  - 1.8 Significance of the Project
  - 1.9 Expected Outcomes
  - 1.10 Report Organization
- **CHAPTER 2: LITERATURE REVIEW AND EXISTING SYSTEM ANALYSIS**
  - 2.1 Introduction
  - 2.2 Review of Existing Trip Planning Solutions
  - 2.3 Review of Vehicle Cost Estimation Solutions
  - 2.4 Analysis of Existing Systems
  - 2.5 Limitations of Existing Approaches
  - 2.6 Proposed TripWise System
  - 2.7 Comparative Feature Analysis
  - 2.8 Advantages and Technical Contributions of TripWise
  - 2.9 Chapter Summary
- **CHAPTER 3: SOFTWARE REQUIREMENTS SPECIFICATION (SRS)**
  - 3.1 Purpose of the SRS
  - 3.2 Product Scope
  - 3.3 Product Perspective
  - 3.4 Intended Users and User Characteristics
  - 3.5 Operating Environment
  - 3.6 Functional Requirements (FR-01 to FR-17)
  - 3.7 Non-Functional Requirements
  - 3.8 User Requirements
  - 3.9 Software Requirements
  - 3.10 Hardware Requirements
  - 3.11 External Interface Requirements
  - 3.12 Data Requirements & Schema Constraints
  - 3.13 Assumptions and Dependencies
  - 3.14 Constraints
  - 3.15 Acceptance Criteria
  - 3.16 Chapter Summary
- **CHAPTER 4: SYSTEM ANALYSIS AND DESIGN**
  - 4.1 System Analysis
  - 4.2 Proposed System Architecture
  - 4.3 Architectural Design & Pattern (MVVM)
  - 4.4 Use Case Diagram
  - 4.5 Detailed Use Case Specifications
  - 4.6 Activity Diagram
  - 4.7 Sequence Diagrams (Calculation & Persistence)
  - 4.8 Data Flow Diagrams (Context Level & Level 1 DFD)
  - 4.9 Flowchart of Trip Cost Calculation
  - 4.10 Class Diagram
  - 4.11 Entity-Relationship Diagram (ERD)
  - 4.12 Data Model and Storage Design
  - 4.13 UI/UX Design System (Material Design 3 Tokens)
  - 4.14 Navigation Structure and Finite State Machine
  - 4.15 Mathematical Trip Cost Calculation Model
  - 4.16 Design Decisions and Technical Justifications
  - 4.17 Chapter Summary
- **CHAPTER 5: SYSTEM IMPLEMENTATION**
  - 5.1 Introduction
  - 5.2 Development Methodology (Agile Scrum / Incremental)
  - 5.3 Development Environment & Toolchain
  - 5.4 Technology Stack
  - 5.5 Project Directory Structure
  - 5.6 Application Architecture Implementation
  - 5.7 User Interface Implementation (Jetpack Compose Canvas & Cards)
  - 5.8 Navigation Drawer and Screen Routing
  - 5.9 Trip Planning Stepper Implementation
  - 5.10 Vehicle Selection Implementation
  - 5.11 Trip Information Input and Validation
  - 5.12 Trip Cost Calculation Algorithm & Pseudocode
  - 5.13 Vehicle Comparison Implementation
  - 5.14 Trip Summary & Per-Passenger Split Implementation
  - 5.15 Trip History Implementation (CRUD)
  - 5.16 Analytics and Data Visualization Implementation
  - 5.17 Data Persistence and Cloud Synchronization
  - 5.18 Error Handling and Exception Boundaries
  - 5.19 Important Source Code Excerpts
  - 5.20 Build and Execution Instructions (`TripWise.apk`)
  - 5.21 Chapter Summary
- **CHAPTER 6: SYSTEM TESTING AND RESULTS**
  - 6.1 Testing Objectives
  - 6.2 Testing Strategy & Phased Test Execution
  - 6.3 Testing Environment
  - 6.4 Unit Testing
  - 6.5 Security Rules Unit Testing (`firestore.test.js`)
  - 6.6 Integration Testing
  - 6.7 Functional Testing
  - 6.8 User Interface & Accessibility Testing
  - 6.9 Input Validation and Boundary Value Testing (BVT)
  - 6.10 Regression Testing
  - 6.11 Usability Evaluation
  - 6.12 Detailed Test Case Execution Matrix (TC-01 to TC-20)
  - 6.13 Requirements Traceability Matrix (RTM)
  - 6.14 Discussion of Test Results
  - 6.15 Known Defects and Engineering Limitations
  - 6.16 Chapter Summary
- **CHAPTER 7: LIMITATIONS AND FUTURE ENHANCEMENTS**
  - 7.1 Current System Limitations
  - 7.2 Technical Limitations
  - 7.3 Data Accuracy Limitations
  - 7.4 User Experience Limitations
  - 7.5 Future Enhancement Opportunities
  - 7.6 Scalability and Integration Opportunities
  - 7.7 Chapter Summary
- **CHAPTER 8: CONCLUSION**
  - 8.1 Project Summary
  - 8.2 Achievement of Objectives
  - 8.3 Summary of Implemented Features
  - 8.4 Software Engineering Learning Outcomes
  - 8.5 Overall Evaluation
  - 8.6 Final Conclusion
- **REFERENCES**
- **APPENDICES**
  - Appendix A: Installation and Setup Guide
  - Appendix B: Application User Manual
  - Appendix C: Complete Requirements Traceability Matrix
  - Appendix D: Full Hardened Security Rules (`firestore.rules`)
  - Appendix E: Core Data Layer Source Code Snippets

---

## LIST OF FIGURES

- Figure 2.1: Conceptual Positioning of TripWise within the Mobile Travel Ecosystem
- Figure 4.1: TripWise High-Level Three-Tier MVVM System Architecture
- Figure 4.2: Comprehensive Use Case Diagram for TripWise System
- Figure 4.3: Activity Diagram for the 4-Step Trip Planning and Cost Estimation Workflow
- Figure 4.4: Sequence Diagram: Interactive Cost Calculation and Review
- Figure 4.5: Sequence Diagram: Cloud Firestore Sync and Security Rule Validation
- Figure 4.6: Level 0 Data Flow Diagram (Context Level)
- Figure 4.7: Level 1 Data Flow Diagram (Detailed Functional Decomposition)
- Figure 4.8: Flowchart of Input Validation and Multi-Tier Trip Cost Calculation Algorithm
- Figure 4.9: Object-Oriented Class Diagram of Presentation, Domain, and Data Entities
- Figure 4.10: Entity-Relationship Diagram (ERD) of Cloud Firestore Schema
- Figure 4.11: Material Design 3 Design System Palette and Typography Hierarchy
- Figure 4.12: Navigation Finite State Machine and Routing Transitions
- Figure 5.1: Directory and File Structure of the Native Android Project
- Figure 5.2: Mathematical Graph: Comparative Vehicle Cost vs. Travel Distance
- Figure 6.1: Quality Assurance Testing Pyramid and Security Rule Verification Workflow

---

## LIST OF TABLES

- Table 2.1: Comparative Feature Matrix: TripWise vs. Existing Travel Tools
- Table 3.1: Complete Functional Requirements Specification (FR-01 to FR-17)
- Table 3.2: Non-Functional Requirements and System Quality Attributes
- Table 3.3: Hardware and Software Environment Specifications
- Table 4.1: Use Case Specification: UC-01 Plan New Journey
- Table 4.2: Use Case Specification: UC-02 Compare Vehicle Categories
- Table 4.3: Use Case Specification: UC-03 Authenticate via Google Sign-In
- Table 4.4: Cloud Firestore Collections and Document Schema
- Table 4.5: Mathematical Variable Dictionary for Cost Calculation
- Table 5.1: Software Development Toolchain and Production Dependency Versions
- Table 5.2: Calibrated Vehicle Consumption and Mileage Benchmarks
- Table 6.1: Comprehensive Test Case Execution Matrix (TC-01 through TC-20)
- Table 6.2: Automated Security Rules Test Results (`firestore.test.js`)
- Table 6.3: Requirements Traceability Matrix (RTM: Requirements to Test Cases)
- Table 8.1: Objective Fulfillment and Deliverable Verification Matrix

---

## LIST OF ABBREVIATIONS AND ACRONYMS

| Acronym | Definition |
| :--- | :--- |
| **AAB** | Android App Bundle |
| **AGP** | Android Gradle Plugin |
| **API** | Application Programming Interface |
| **APK** | Android Package Kit |
| **BDT** | Bangladeshi Taka ($\text{৳}$) |
| **BOM** | Bill of Materials |
| **BVT** | Boundary Value Testing |
| **CNG** | Compressed Natural Gas (Auto-Rickshaw) |
| **CRUD** | Create, Read, Update, Delete |
| **DFD** | Data Flow Diagram |
| **DSL** | Domain Specific Language |
| **ERD** | Entity-Relationship Diagram |
| **FR** | Functional Requirement |
| **FSM** | Finite State Machine |
| **GPS** | Global Positioning System |
| **IEEE** | Institute of Electrical and Electronics Engineers |
| **IR** | Intermediate Representation |
| **JDK** | Java Development Kit |
| **JVM** | Java Virtual Machine |
| **KM** | Kilometer |
| **KM/L** | Kilometers per Liter |
| **M3** | Material Design 3 |
| **MVVM** | Model-View-ViewModel |
| **NFR** | Non-Functional Requirement |
| **NoSQL** | Not Only SQL (Document Database) |
| **OAuth** | Open Authorization Protocol |
| **RFID** | Radio Frequency Identification |
| **RTM** | Requirements Traceability Matrix |
| **SDK** | Software Development Kit |
| **SHA** | Secure Hash Algorithm |
| **SOS** | Save Our Souls (Emergency Distress Signal) |
| **SRS** | Software Requirements Specification |
| **SSL** | Secure Sockets Layer |
| **UI** | User Interface |
| **UML** | Unified Modeling Language |
| **URI** | Uniform Resource Identifier |
| **UX** | User Experience |
| **VM** | ViewModel |
| **XML** | Extensible Markup Language |
