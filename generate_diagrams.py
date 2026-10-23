import os
import matplotlib.pyplot as plt
import matplotlib.patches as patches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch, Circle, Ellipse

os.makedirs("report/figures", exist_ok=True)
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['axes.edgecolor'] = '#CBD5E1'

# -------------------------------------------------------------
# 1. SYSTEM ARCHITECTURE DIAGRAM
# -------------------------------------------------------------
def generate_architecture_diagram():
    fig, ax = plt.subplots(figsize=(11, 7.5), dpi=300)
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.axis('off')

    # Background canvas
    fig.patch.set_facecolor('#F8FAFC')
    ax.set_facecolor('#F8FAFC')

    # Title
    ax.text(50, 96, "TripWise: Clean System Architecture & Layered Interaction", 
            ha='center', va='center', fontsize=15, fontweight='bold', color='#0F172A')
    ax.text(50, 92.5, "Modern Android (Kotlin 2.1.0 + Jetpack Compose) & Google Cloud Firestore", 
            ha='center', va='center', fontsize=10, color='#64748B')

    # Layer 1: Presentation Layer
    pres_box = FancyBboxPatch((5, 63), 90, 26, boxstyle="round,pad=0.5,rounding_size=2",
                              facecolor='#E0F2FE', edgecolor='#0284C7', linewidth=1.8)
    ax.add_patch(pres_box)
    ax.text(8, 86, "PRESENTATION LAYER (Jetpack Compose & Material 3)", 
            fontsize=11, fontweight='bold', color='#0369A1')

    # UI Components sub-box
    ui_box = FancyBboxPatch((8, 66), 46, 17, boxstyle="round,pad=0.4,rounding_size=1.5",
                            facecolor='#FFFFFF', edgecolor='#38BDF8', linewidth=1.2)
    ax.add_patch(ui_box)
    ax.text(31, 80, "Declarative UI Composables (M3)", ha='center', fontsize=10, fontweight='bold', color='#0284C7')
    ax.text(31, 75, "• HomeScreen (Dashboard & Quick Utilities)\n• 4-Step Trip Wizard (Steps 1–4)\n• CompareVehiclesScreen & AnalyticsScreen\n• Highway Utilities (RouteMap, QrTicket, VideoGuide)",
            ha='center', va='center', fontsize=8.2, color='#334155')

    # ViewModel sub-box
    vm_box = FancyBboxPatch((58, 66), 34, 17, boxstyle="round,pad=0.4,rounding_size=1.5",
                            facecolor='#FFFFFF', edgecolor='#38BDF8', linewidth=1.2)
    ax.add_patch(vm_box)
    ax.text(75, 80, "TripViewModel (StateFlow)", ha='center', fontsize=10, fontweight='bold', color='#0284C7')
    ax.text(75, 74.5, "• MutableStateFlow<TripUiState>\n• Immutable TripDraft state\n• Coroutine viewModelScope\n• Unidirectional Data Flow (UDF)",
            ha='center', va='center', fontsize=8.2, color='#334155')

    # Arrow between UI and VM
    ax.annotate("", xy=(58, 74.5), xytext=(54, 74.5),
                arrowprops=dict(arrowstyle="<->", color="#0284C7", lw=1.8))

    # Layer 2: Domain Layer
    dom_box = FancyBboxPatch((5, 41), 90, 17, boxstyle="round,pad=0.5,rounding_size=2",
                             facecolor='#ECFDF5', edgecolor='#10B981', linewidth=1.8)
    ax.add_patch(dom_box)
    ax.text(8, 55, "DOMAIN LAYER (Deterministic Business & Mathematical Logic)", 
            fontsize=11, fontweight='bold', color='#047857')

    # Domain math components
    math_box1 = FancyBboxPatch((8, 43.5), 26, 9.5, boxstyle="round,pad=0.3,rounding_size=1",
                               facecolor='#FFFFFF', edgecolor='#34D399', linewidth=1.2)
    ax.add_patch(math_box1)
    ax.text(21, 49.5, "Fuel Engine\nV = D / M\nCost = V × Price", ha='center', va='center', fontsize=8.2, fontweight='bold', color='#065F46')

    math_box2 = FancyBboxPatch((37, 43.5), 26, 9.5, boxstyle="round,pad=0.3,rounding_size=1",
                               facecolor='#FFFFFF', edgecolor='#34D399', linewidth=1.2)
    ax.add_patch(math_box2)
    ax.text(50, 49.5, "Expense Ledger\nTotal = Fuel + Tolls + Food\nSplit = Total / max(1, N)", ha='center', va='center', fontsize=8.2, fontweight='bold', color='#065F46')

    math_box3 = FancyBboxPatch((66, 43.5), 26, 9.5, boxstyle="round,pad=0.3,rounding_size=1",
                               facecolor='#FFFFFF', edgecolor='#34D399', linewidth=1.2)
    ax.add_patch(math_box3)
    ax.text(79, 49.5, "Comparison Engine\nCar vs Bike vs CNG\nSavings Delta Δ and %", ha='center', va='center', fontsize=8.2, fontweight='bold', color='#065F46')

    # Layer 3: Data Layer
    data_box = FancyBboxPatch((5, 19), 90, 17, boxstyle="round,pad=0.5,rounding_size=2",
                              facecolor='#FEF3C7', edgecolor='#F59E0B', linewidth=1.8)
    ax.add_patch(data_box)
    ax.text(8, 33, "DATA LAYER (Repository Pattern & Dual Persistence)", 
            fontsize=11, fontweight='bold', color='#B45309')

    repo_box1 = FancyBboxPatch((8, 21.5), 41, 9.5, boxstyle="round,pad=0.3,rounding_size=1",
                               facecolor='#FFFFFF', edgecolor='#FBBF24', linewidth=1.2)
    ax.add_patch(repo_box1)
    ax.text(28.5, 27.5, "TripRepository (Single Source of Truth)\nIn-Memory MutableStateFlow Cache (Zero-latency offline)", 
            ha='center', va='center', fontsize=8.2, fontweight='bold', color='#78350F')

    repo_box2 = FancyBboxPatch((53, 21.5), 39, 9.5, boxstyle="round,pad=0.3,rounding_size=1",
                               facecolor='#FFFFFF', edgecolor='#FBBF24', linewidth=1.2)
    ax.add_patch(repo_box2)
    ax.text(72.5, 27.5, "Google Cloud Firestore SDK\nAsynchronous Coroutine Dispatch (Dispatchers.IO)", 
            ha='center', va='center', fontsize=8.2, fontweight='bold', color='#78350F')

    # Layer 4: Cloud Infrastructure
    cloud_box = FancyBboxPatch((5, 2), 90, 13, boxstyle="round,pad=0.5,rounding_size=2",
                               facecolor='#F1F5F9', edgecolor='#64748B', linewidth=1.8)
    ax.add_patch(cloud_box)
    ax.text(8, 12, "CLOUD INFRASTRUCTURE (Google Cloud Platform & Firebase)", 
            fontsize=11, fontweight='bold', color='#334155')
    ax.text(50, 6.5, "• Firebase Authentication (Google Sign-In / Credential Manager)\n• Cloud Firestore (Named DB: tripwise-db | /users, /trips, /emergency_contacts)\n• Zero-Trust Security Rules (Isolated User Read/Write Access Enforcement)",
            ha='center', va='center', fontsize=8.2, color='#1E293B')

    # Inter-layer connecting arrows
    ax.annotate("", xy=(50, 58), xytext=(50, 63), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=2))
    ax.annotate("", xy=(50, 36), xytext=(50, 41), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=2))
    ax.annotate("", xy=(50, 15), xytext=(50, 19), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=2))

    plt.tight_layout()
    plt.savefig("report/figures/fig_architecture.png", dpi=300)
    plt.close()
    print("Created report/figures/fig_architecture.png")


# -------------------------------------------------------------
# 2. USE CASE DIAGRAM
# -------------------------------------------------------------
def generate_use_case_diagram():
    fig, ax = plt.subplots(figsize=(11, 8.5), dpi=300)
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.axis('off')

    fig.patch.set_facecolor('#FFFFFF')
    ax.set_facecolor('#FFFFFF')

    # Title
    ax.text(50, 97, "TripWise: Formal UML Use Case Diagram", 
            ha='center', va='center', fontsize=15, fontweight='bold', color='#0F172A')

    # System Boundary Box
    system_boundary = FancyBboxPatch((24, 4), 54, 90, boxstyle="round,pad=0.5,rounding_size=2",
                                     facecolor='#F8FAFC', edgecolor='#006874', linewidth=2.2)
    ax.add_patch(system_boundary)
    ax.text(51, 91.5, "TripWise Mobile Application Boundary", 
            ha='center', va='center', fontsize=12, fontweight='bold', color='#006874')

    # Actor Left: Traveler
    ax.plot([10, 10], [55, 63], color='#0F172A', lw=2.2) # body
    ax.add_patch(Circle((10, 67), 3, facecolor='#E2E8F0', edgecolor='#0F172A', lw=2.2)) # head
    ax.plot([4, 16], [60, 60], color='#0F172A', lw=2.2) # arms
    ax.plot([10, 4], [55, 47], color='#0F172A', lw=2.2) # left leg
    ax.plot([10, 16], [55, 47], color='#0F172A', lw=2.2) # right leg
    ax.text(10, 43, "Traveler / User\n(Primary Actor)", ha='center', fontsize=10, fontweight='bold', color='#0F172A')

    # Actor Right: Cloud Backend
    ax.plot([90, 90], [55, 63], color='#0F172A', lw=2.2) # body
    ax.add_patch(Circle((90, 67), 3, facecolor='#E2E8F0', edgecolor='#0F172A', lw=2.2)) # head
    ax.plot([84, 96], [60, 60], color='#0F172A', lw=2.2) # arms
    ax.plot([90, 84], [55, 47], color='#0F172A', lw=2.2) # left leg
    ax.plot([90, 96], [55, 47], color='#0F172A', lw=2.2) # right leg
    ax.text(90, 43, "Firebase Backend\n(Secondary Actor)", ha='center', fontsize=10, fontweight='bold', color='#0F172A')

    # Use cases
    use_cases = [
        (51, 84, "UC-01: Plan Journey via 4-Step Wizard\n(Origin, Vehicle, Expenses, Split)"),
        (51, 72, "UC-02: Compare Multi-Modal Vehicle Costs\n(Sedan Car, Motorcycle, CNG Auto-Rickshaw)"),
        (51, 60, "UC-03: View & Manage Trip History\n(Browse, Edit, Delete Saved Records)"),
        (51, 48, "UC-04: View Aggregated Spending Analytics\n(Total Cost, Fuel Volume, Distance Charts)"),
        (51, 36, "UC-05: Access Highway & Toll Utilities\n(Route Map, Toll Gate Pass, Padma Bridge Guide)"),
        (51, 24, "UC-06: Dispatch Emergency Helplines\n(One-Touch Dial: 999, Highway Police)"),
        (51, 12, "UC-07: Authenticate via Google Sign-In\n(Credential Manager & Profile Sync)")
    ]

    for x, y, label in use_cases:
        ellipse = FancyBboxPatch((x-21, y-4.2), 42, 8.4, boxstyle="round,pad=0.4,rounding_size=4.2",
                                 facecolor='#E0F2FE', edgecolor='#0284C7', linewidth=1.5)
        ax.add_patch(ellipse)
        ax.text(x, y, label, ha='center', va='center', fontsize=8.2, fontweight='bold', color='#0F172A')

    # Association lines: User to all use cases
    for _, y, _ in use_cases:
        ax.annotate("", xy=(30, y), xytext=(15, 57),
                    arrowprops=dict(arrowstyle="-", color="#334155", lw=1.3))

    # Association lines: Backend to persistent use cases (UC-01, UC-03, UC-07)
    for y in [84, 60, 12]:
        ax.annotate("", xy=(72, y), xytext=(85, 57),
                    arrowprops=dict(arrowstyle="-", color="#006874", lw=1.5, ls="--"))

    plt.tight_layout()
    plt.savefig("report/figures/fig_use_case.png", dpi=300)
    plt.close()
    print("Created report/figures/fig_use_case.png")


# -------------------------------------------------------------
# 3. ACTIVITY DIAGRAM
# -------------------------------------------------------------
def generate_activity_diagram():
    fig, ax = plt.subplots(figsize=(10, 10.5), dpi=300)
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.axis('off')

    fig.patch.set_facecolor('#F8FAFC')
    ax.set_facecolor('#F8FAFC')

    # Title
    ax.text(50, 97.5, "TripWise: 4-Step Trip Planning Activity Workflow", 
            ha='center', va='center', fontsize=14, fontweight='bold', color='#0F172A')

    # Start Node
    ax.add_patch(Circle((50, 93), 2.2, facecolor='#0F172A', edgecolor='#0F172A'))
    ax.annotate("", xy=(50, 88.5), xytext=(50, 90.8), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Activity 1: Step 1
    box1 = FancyBboxPatch((25, 81.5), 50, 7, boxstyle="round,pad=0.3,rounding_size=1.5",
                          facecolor='#FFFFFF', edgecolor='#0284C7', linewidth=1.5)
    ax.add_patch(box1)
    ax.text(50, 85, "Step 1: Enter Origin, Destination, Distance (km), & Passengers (N)", 
            ha='center', va='center', fontsize=8.8, fontweight='bold', color='#0F172A')

    ax.annotate("", xy=(50, 77.5), xytext=(50, 81.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Decision Diamond 1
    d1 = patches.Polygon([[50, 77.5], [56, 74], [50, 70.5], [44, 74]], 
                         facecolor='#FEF3C7', edgecolor='#D97706', lw=1.5)
    ax.add_patch(d1)
    ax.text(50, 74, "Valid\nInputs?", ha='center', va='center', fontsize=7.5, fontweight='bold', color='#78350F')

    # Loop back on Invalid
    ax.text(62, 75, "[No: Missing/D<=0]", fontsize=7.5, color='#DC2626')
    ax.plot([56, 82, 82, 75], [74, 74, 85, 85], color='#DC2626', lw=1.4, ls="--")
    ax.annotate("", xy=(75, 85), xytext=(82, 85), arrowprops=dict(arrowstyle="->", color="#DC2626", lw=1.4))

    # Valid -> Activity 2
    ax.annotate("", xy=(50, 64.5), xytext=(50, 70.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))
    ax.text(51.5, 67.5, "[Yes]", fontsize=7.8, color='#16A34A', fontweight='bold')

    # Activity 2: Step 2
    box2 = FancyBboxPatch((22, 57.5), 56, 7, boxstyle="round,pad=0.3,rounding_size=1.5",
                          facecolor='#FFFFFF', edgecolor='#0284C7', linewidth=1.5)
    ax.add_patch(box2)
    ax.text(50, 61, "Step 2: Select Vehicle Modality (Sedan Car, Motorcycle, CNG)\n& Auto-Populate Fuel Mileage / Retail Price Benchmarks", 
            ha='center', va='center', fontsize=8.5, fontweight='bold', color='#0F172A')

    ax.annotate("", xy=(50, 52.5), xytext=(50, 57.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Activity 3: Step 3
    box3 = FancyBboxPatch((24, 45.5), 52, 7, boxstyle="round,pad=0.3,rounding_size=1.5",
                          facecolor='#FFFFFF', edgecolor='#0284C7', linewidth=1.5)
    ax.add_patch(box3)
    ax.text(50, 49, "Step 3: Itemize Supplementary Expenses\n(Bridge Tolls, Terminal Parking, Food/Meals, Other)", 
            ha='center', va='center', fontsize=8.5, fontweight='bold', color='#0F172A')

    ax.annotate("", xy=(50, 40.5), xytext=(50, 45.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Activity 4: Mathematical Calculation
    box_calc = FancyBboxPatch((20, 31.5), 60, 9, boxstyle="round,pad=0.4,rounding_size=1.8",
                              facecolor='#ECFDF5', edgecolor='#10B981', linewidth=1.8)
    ax.add_patch(box_calc)
    ax.text(50, 36.5, "Execute Deterministic Calculation Engine\nFuel Vol = D / M  |  Fuel Cost = Vol × Price\nTotal = Fuel + Tolls + Parking + Food  |  Split = Total / max(1, N)", 
            ha='center', va='center', fontsize=8.5, fontweight='bold', color='#065F46')

    ax.annotate("", xy=(50, 26.5), xytext=(50, 31.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Activity 5: Step 4 Review
    box4 = FancyBboxPatch((24, 19.5), 52, 7, boxstyle="round,pad=0.3,rounding_size=1.5",
                          facecolor='#FFFFFF', edgecolor='#0284C7', linewidth=1.5)
    ax.add_patch(box4)
    ax.text(50, 23, "Step 4: Display Comprehensive Review Ledger\n(Total Cost & Equitable Per-Passenger Share)", 
            ha='center', va='center', fontsize=8.5, fontweight='bold', color='#0F172A')

    ax.annotate("", xy=(50, 14.5), xytext=(50, 19.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # Activity 6: Save & Cloud Sync
    box_save = FancyBboxPatch((26, 7.5), 48, 7, boxstyle="round,pad=0.3,rounding_size=1.5",
                              facecolor='#FEF3C7', edgecolor='#D97706', linewidth=1.5)
    ax.add_patch(box_save)
    ax.text(50, 11, "Save Trip to Local StateFlow & Asynchronously Sync to Firestore", 
            ha='center', va='center', fontsize=8.5, fontweight='bold', color='#78350F')

    ax.annotate("", xy=(50, 3.8), xytext=(50, 7.5), arrowprops=dict(arrowstyle="->", color="#0F172A", lw=1.8))

    # End Node (Bullseye)
    ax.add_patch(Circle((50, 2.5), 2.2, facecolor='none', edgecolor='#0F172A', lw=2))
    ax.add_patch(Circle((50, 2.5), 1.2, facecolor='#0F172A', edgecolor='#0F172A'))

    plt.tight_layout()
    plt.savefig("report/figures/fig_activity.png", dpi=300)
    plt.close()
    print("Created report/figures/fig_activity.png")


# -------------------------------------------------------------
# 4. SEQUENCE DIAGRAM
# -------------------------------------------------------------
def generate_sequence_diagram():
    fig, ax = plt.subplots(figsize=(11, 8.5), dpi=300)
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.axis('off')

    fig.patch.set_facecolor('#FFFFFF')
    ax.set_facecolor('#FFFFFF')

    # Title
    ax.text(50, 97, "TripWise: End-to-End Sequence Diagram (Trip Calculation & Persistence)", 
            ha='center', va='center', fontsize=14, fontweight='bold', color='#0F172A')

    # Participants
    actors = [
        (12, "Traveler\n(User)"),
        (31, "Compose UI\n(StepScreens)"),
        (50, "TripViewModel\n(StateFlow)"),
        (69, "CalculationEngine\n(Domain Math)"),
        (88, "Cloud Firestore\n(Data Layer)")
    ]

    for x, label in actors:
        # Header box
        box = FancyBboxPatch((x-8, 88), 16, 6, boxstyle="round,pad=0.3,rounding_size=1",
                             facecolor='#E0F2FE', edgecolor='#0284C7', linewidth=1.5)
        ax.add_patch(box)
        ax.text(x, 91, label, ha='center', va='center', fontsize=8.2, fontweight='bold', color='#0F172A')

        # Lifeline dashed
        ax.plot([x, x], [88, 8], color='#94A3B8', lw=1.2, ls="--")

    # Sequence Messages
    messages = [
        (1, 12, 31, 83, "1: Enter Distance, Vehicle, Tolls", "#0F172A", False),
        (2, 31, 50, 76, "2: updateTripDraft(draft)", "#0284C7", False),
        (3, 50, 69, 69, "3: calculateExpenses(draft)", "#047857", False),
        (4, 69, 50, 62, "4: return ComputedExpenses(total, perPass)", "#047857", True),
        (5, 50, 31, 55, "5: emit TripUiState with calculation", "#0284C7", True),
        (6, 31, 12, 48, "6: Render Real-Time Ledger Preview", "#0F172A", True),
        (7, 12, 31, 41, "7: Click 'Save Trip to Cloud'", "#0F172A", False),
        (8, 31, 50, 34, "8: saveCurrentTrip()", "#0284C7", False),
        (9, 50, 88, 27, "9: firestore.collection('trips').add(trip)", "#D97706", False),
        (10, 88, 50, 20, "10: Snapshot confirmation (SUCCESS)", "#D97706", True),
        (11, 50, 31, 14, "11: Navigate to History & Show Snackbar", "#0284C7", True)
    ]

    for step_num, x_from, x_to, y, text, color, is_return in messages:
        ls = "--" if is_return else "-"
        ax.annotate("", xy=(x_to, y), xytext=(x_from, y),
                    arrowprops=dict(arrowstyle="->", color=color, lw=1.5, ls=ls))
        mid_x = (x_from + x_to) / 2
        ax.text(mid_x, y + 1.6, text, ha='center', va='center', fontsize=7.8, fontweight='bold', color=color)

    plt.tight_layout()
    plt.savefig("report/figures/fig_sequence.png", dpi=300)
    plt.close()
    print("Created report/figures/fig_sequence.png")


# -------------------------------------------------------------
# 5. ENTITY RELATIONSHIP DIAGRAM (ERD)
# -------------------------------------------------------------
def generate_erd_diagram():
    fig, ax = plt.subplots(figsize=(10, 7.5), dpi=300)
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.axis('off')

    fig.patch.set_facecolor('#F8FAFC')
    ax.set_facecolor('#F8FAFC')

    ax.text(50, 96, "TripWise: Google Cloud Firestore NoSQL Entity-Relationship Schema", 
            ha='center', va='center', fontsize=13, fontweight='bold', color='#0F172A')

    # USERS table
    u_box = FancyBboxPatch((8, 54), 38, 36, boxstyle="round,pad=0.3,rounding_size=1.5",
                           facecolor='#FFFFFF', edgecolor='#006874', linewidth=1.8)
    ax.add_patch(u_box)
    header_u = FancyBboxPatch((8, 83), 38, 7, boxstyle="round,pad=0.3,rounding_size=1",
                              facecolor='#006874', edgecolor='#006874', linewidth=1.8)
    ax.add_patch(header_u)
    ax.text(27, 86.5, "USERS Collection (/users)", ha='center', va='center', fontsize=10, fontweight='bold', color='#FFFFFF')
    ax.text(10, 68, "• userId: String (PK / Auth UID)\n• email: String\n• displayName: String\n• photoUrl: String\n• createdAt: Timestamp\n• preferredVehicle: String",
            fontsize=8.5, color='#1E293B')

    # TRIPS table
    t_box = FancyBboxPatch((54, 30), 40, 60, boxstyle="round,pad=0.3,rounding_size=1.5",
                           facecolor='#FFFFFF', edgecolor='#0284C7', linewidth=1.8)
    ax.add_patch(t_box)
    header_t = FancyBboxPatch((54, 83), 40, 7, boxstyle="round,pad=0.3,rounding_size=1",
                              facecolor='#0284C7', edgecolor='#0284C7', linewidth=1.8)
    ax.add_patch(header_t)
    ax.text(74, 86.5, "TRIPS Collection (/trips)", ha='center', va='center', fontsize=10, fontweight='bold', color='#FFFFFF')
    ax.text(56, 56, "• id: String (PK / DocumentId)\n• userId: String (FK -> Users.userId)\n• origin: String\n• destination: String\n• distanceKm: Double\n• passengerCount: Int\n• vehicleType: String (CAR/BIKE/CNG)\n• fuelPricePerUnit: Double\n• mileageKmPerUnit: Double\n• tollCost: Double\n• parkingCost: Double\n• foodCost: Double\n• totalFuelVolume: Double\n• totalTripCost: Double\n• costPerPassenger: Double\n• timestamp: Timestamp",
            fontsize=8.2, color='#1E293B')

    # 1:N Relationship connector
    ax.annotate("", xy=(54, 72), xytext=(46, 72),
                arrowprops=dict(arrowstyle="->", color="#0F172A", lw=2))
    ax.text(50, 74.5, "1 : N", ha='center', fontsize=9, fontweight='bold', color='#006874')

    # EMERGENCY CONTACTS table
    e_box = FancyBboxPatch((8, 10), 38, 36, boxstyle="round,pad=0.3,rounding_size=1.5",
                           facecolor='#FFFFFF', edgecolor='#D97706', linewidth=1.8)
    ax.add_patch(e_box)
    header_e = FancyBboxPatch((8, 39), 38, 7, boxstyle="round,pad=0.3,rounding_size=1",
                              facecolor='#D97706', edgecolor='#D97706', linewidth=1.8)
    ax.add_patch(header_e)
    ax.text(27, 42.5, "EMERGENCY REGISTRY", ha='center', va='center', fontsize=10, fontweight='bold', color='#FFFFFF')
    ax.text(10, 24, "• id: String (PK)\n• agencyName: String\n• phoneNumber: String\n• category: String\n• jurisdiction: String\n• status: String (Verified)",
            fontsize=8.5, color='#1E293B')

    plt.tight_layout()
    plt.savefig("report/figures/fig_erd.png", dpi=300)
    plt.close()
    print("Created report/figures/fig_erd.png")

if __name__ == "__main__":
    generate_architecture_diagram()
    generate_use_case_diagram()
    generate_activity_diagram()
    generate_sequence_diagram()
    generate_erd_diagram()
    print("All diagrams generated successfully in report/figures/!")
