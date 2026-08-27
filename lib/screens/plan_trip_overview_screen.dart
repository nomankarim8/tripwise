import 'package:flutter/material.dart';
import '../models/trip_data.dart';
import '../theme/app_theme.dart';
import '../widgets/step_indicator.dart';
import 'step1_trip_details_screen.dart';
import 'step2_select_vehicle_screen.dart';
import 'step3_costs_expenses_screen.dart';
import 'step4_review_summary_screen.dart';

class PlanTripOverviewScreen extends StatefulWidget {
  const PlanTripOverviewScreen({super.key});

  @override
  State<PlanTripOverviewScreen> createState() => _PlanTripOverviewScreenState();
}

class _PlanTripOverviewScreenState extends State<PlanTripOverviewScreen> {
  final TripData tripData = TripData();

  void _goToStep(int step) {
    Widget screen;
    switch (step) {
      case 1:
        screen = Step1TripDetailsScreen(tripData: tripData);
        break;
      case 2:
        screen = Step2SelectVehicleScreen(tripData: tripData);
        break;
      case 3:
        screen = Step3CostsExpensesScreen(tripData: tripData);
        break;
      default:
        screen = Step4ReviewSummaryScreen(tripData: tripData);
    }
    Navigator.push(context, MaterialPageRoute(builder: (_) => screen));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        backgroundColor: AppColors.primaryBlue,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Plan Your Trip',
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.w600)),
        centerTitle: true,
      ),
      body: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 12),
                child: Column(
                  children: [
                    VerticalStepTile(
                      number: 1,
                      color: AppColors.primaryBlue,
                      icon: Icons.route_rounded,
                      title: 'Trip Details',
                      subtitle: 'Enter basic information about your trip',
                      onTap: () => _goToStep(1),
                    ),
                    VerticalStepTile(
                      number: 2,
                      color: AppColors.green,
                      icon: Icons.directions_car_filled_rounded,
                      title: 'Select Vehicle',
                      subtitle: 'Choose your vehicle type',
                      onTap: () => _goToStep(2),
                    ),
                    VerticalStepTile(
                      number: 3,
                      color: AppColors.orange,
                      icon: Icons.account_balance_wallet_rounded,
                      title: 'Costs & Expenses',
                      subtitle: 'Add fuel price, toll, parking and other costs',
                      onTap: () => _goToStep(3),
                    ),
                    VerticalStepTile(
                      number: 4,
                      color: AppColors.purple,
                      icon: Icons.assignment_rounded,
                      title: 'Review & Summary',
                      subtitle: 'Review your trip details and see total cost',
                      isLast: true,
                      onTap: () => _goToStep(4),
                    ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 20),
              child: SizedBox(
                width: double.infinity,
                height: 52,
                child: ElevatedButton(
                  onPressed: () => _goToStep(1),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.primaryBlue,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                  ),
                  child: const Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text('NEXT',
                          style: TextStyle(
                              color: Colors.white, fontWeight: FontWeight.bold, letterSpacing: 1)),
                      SizedBox(width: 6),
                      Icon(Icons.arrow_forward, color: Colors.white, size: 18),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
