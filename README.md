# TripWise — Smart Vehicle & Trip Cost Planner

A Flutter recreation of the TripWise UI design: home dashboard, side drawer,
and the full 4-step "Plan Your Trip" wizard (Trip Details → Select Vehicle →
Costs & Expenses → Review & Summary) with live cost calculation.

## Project structure

```
lib/
  main.dart                          # App entry point + routes
  theme/app_theme.dart               # Colors, gradients, text theme
  models/trip_data.dart              # Shared trip state (ChangeNotifier)
  widgets/
    app_drawer.dart                  # Side menu (screen 3)
    bottom_nav.dart                  # Bottom nav bar (screen 1)
    step_indicator.dart              # Horizontal 1-2-3-4 stepper + vertical timeline
  screens/
    home_screen.dart                 # Screen 1 — dashboard
    plan_trip_overview_screen.dart   # Screen 2 — vertical step overview
    step1_trip_details_screen.dart   # Screen 5 — From/To/Distance/Passengers
    step2_select_vehicle_screen.dart # Screen 6 — Car/Motorcycle/CNG
    step3_costs_expenses_screen.dart # Screen 7 — Fuel/Toll/Parking/Food
    step4_review_summary_screen.dart # Screen 8 — Summary + Total Cost
```

## How to run this in VS Code

1. **Install prerequisites** (if you haven't already):
   - [Flutter SDK](https://docs.flutter.dev/get-started/install)
   - [VS Code](https://code.visualstudio.com/) with the **Flutter** and **Dart** extensions
     (search "Flutter" in the Extensions panel — installing it also installs the Dart extension).

2. **Open the project**
   - Unzip this project.
   - In VS Code: `File → Open Folder…` → select the `tripwise` folder.

3. **Get packages**
   Open a terminal in VS Code (`` Ctrl+` ``) and run:
   ```bash
   flutter pub get
   ```

4. **Generate native platform folders** (this project ships only `lib/` + config;
   VS Code/Flutter needs `android/`, `ios/`, etc. generated for your machine):
   ```bash
   flutter create .
   ```
   This safely fills in the platform folders without touching your `lib/` code.

5. **Run it**
   - Press `F5`, or use the Run/Debug panel, or run:
     ```bash
     flutter run
     ```
   - Pick a connected device, emulator, or Chrome (web) when prompted.

## Notes

- Colors/gradients (blue, green, orange, purple) map 1:1 to the four wizard
  steps and dashboard cards in the original design.
- `TripData` is a single `ChangeNotifier` instance created in
  `PlanTripOverviewScreen` and passed down through each step, so values
  entered in Step 1–3 automatically appear in the Step 4 summary and the
  **Total Trip Cost** is calculated live (`distance ÷ vehicle mileage × fuel price + toll + parking + food`).
- Currency symbol used is ৳ (BDT), matching the mockup.
- Fonts use Google Fonts "Poppins" to match the clean, rounded look of the design.
