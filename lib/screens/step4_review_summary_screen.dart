import 'package:flutter/material.dart';
import '../models/trip_data.dart';
import '../theme/app_theme.dart';
import 'step1_trip_details_screen.dart';
import 'home_screen.dart';

class Step4ReviewSummaryScreen extends StatelessWidget {
  final TripData tripData;
  const Step4ReviewSummaryScreen({super.key, required this.tripData});

  String _money(double v) => '৳ ${v.toStringAsFixed(0).replaceAllMapped(
        RegExp(r'(\d)(?=(\d{3})+(?!\d))'),
        (m) => '${m[1]},',
      )}';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const StepHeader(color: AppColors.purple, activeStep: 4),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Review & Summary',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.purple)),
                    const SizedBox(height: 4),
                    const Text('Review your trip details and see total cost',
                        style: TextStyle(color: AppColors.textGrey, fontSize: 13)),
                    const SizedBox(height: 20),
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(18),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(16),
                        border: Border.all(color: AppColors.borderGrey),
                      ),
                      child: Column(
                        children: [
                          _SummaryRow(
                            label: 'From - To',
                            valueWidget: Row(
                              children: [
                                Flexible(
                                    child: Text(tripData.from.split(',').first,
                                        overflow: TextOverflow.ellipsis,
                                        style: const TextStyle(fontWeight: FontWeight.w600))),
                                const Padding(
                                  padding: EdgeInsets.symmetric(horizontal: 6),
                                  child: Icon(Icons.arrow_forward, size: 14, color: AppColors.textGrey),
                                ),
                                Flexible(
                                    child: Text(tripData.to.split(',').first,
                                        overflow: TextOverflow.ellipsis,
                                        style: const TextStyle(fontWeight: FontWeight.w600))),
                              ],
                            ),
                          ),
                          _SummaryRow(label: 'Distance', value: '${tripData.distanceKm.toStringAsFixed(0)} KM'),
                          _SummaryRow(label: 'Vehicle', value: tripData.vehicle.label),
                          _SummaryRow(label: 'Passengers', value: '${tripData.passengers} People'),
                          const Padding(
                            padding: EdgeInsets.symmetric(vertical: 10),
                            child: Divider(color: AppColors.borderGrey, height: 1),
                          ),
                          _SummaryRow(label: 'Fuel Cost', value: _money(tripData.fuelCost)),
                          _SummaryRow(label: 'Toll Cost', value: _money(tripData.tollCost)),
                          _SummaryRow(label: 'Parking Cost', value: _money(tripData.parkingCost)),
                          _SummaryRow(label: 'Food & Others', value: _money(tripData.foodOthers)),
                        ],
                      ),
                    ),
                    const SizedBox(height: 16),
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 18),
                      decoration: BoxDecoration(
                        color: AppColors.purple.withValues(alpha: 0.08),
                        borderRadius: BorderRadius.circular(16),
                        border: Border.all(color: AppColors.purple.withValues(alpha: 0.3)),
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('Total Trip Cost',
                              style: TextStyle(
                                  color: AppColors.purple, fontWeight: FontWeight.w600, fontSize: 14)),
                          Text(
                            _money(tripData.totalCost),
                            style: const TextStyle(
                                color: AppColors.purple, fontWeight: FontWeight.bold, fontSize: 20),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 20),
              child: Row(
                children: [
                  Expanded(
                    child: SizedBox(
                      height: 52,
                      child: OutlinedButton(
                        onPressed: () => Navigator.pop(context),
                        style: OutlinedButton.styleFrom(
                          side: const BorderSide(color: AppColors.borderGrey),
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                        ),
                        child: const Text('BACK',
                            style: TextStyle(
                                color: AppColors.textDark, fontWeight: FontWeight.bold, letterSpacing: 1)),
                      ),
                    ),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: SizedBox(
                      height: 52,
                      child: ElevatedButton(
                        onPressed: () {
                          Navigator.pushAndRemoveUntil(
                            context,
                            MaterialPageRoute(builder: (_) => const HomeScreen()),
                            (route) => false,
                          );
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Trip saved successfully!')),
                          );
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: AppColors.purple,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                        ),
                        child: const Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text('FINISH',
                                style: TextStyle(
                                    color: Colors.white, fontWeight: FontWeight.bold, letterSpacing: 1)),
                            SizedBox(width: 6),
                            Icon(Icons.check, color: Colors.white, size: 18),
                          ],
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _SummaryRow extends StatelessWidget {
  final String label;
  final String? value;
  final Widget? valueWidget;

  const _SummaryRow({required this.label, this.value, this.valueWidget});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: AppColors.textGrey, fontSize: 13)),
          valueWidget ??
              Text(value ?? '',
                  style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13, color: AppColors.textDark)),
        ],
      ),
    );
  }
}
