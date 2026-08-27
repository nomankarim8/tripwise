import 'package:flutter/material.dart';
import '../models/trip_data.dart';
import '../theme/app_theme.dart';
import 'step1_trip_details_screen.dart';
import 'step4_review_summary_screen.dart';

class Step3CostsExpensesScreen extends StatefulWidget {
  final TripData tripData;
  const Step3CostsExpensesScreen({super.key, required this.tripData});

  @override
  State<Step3CostsExpensesScreen> createState() => _Step3CostsExpensesScreenState();
}

class _Step3CostsExpensesScreenState extends State<Step3CostsExpensesScreen> {
  late final TextEditingController _fuelCtrl;
  late final TextEditingController _tollCtrl;
  late final TextEditingController _parkingCtrl;
  late final TextEditingController _foodCtrl;

  @override
  void initState() {
    super.initState();
    _fuelCtrl = TextEditingController(text: widget.tripData.fuelPrice.toStringAsFixed(0));
    _tollCtrl = TextEditingController(text: widget.tripData.tollCost.toStringAsFixed(0));
    _parkingCtrl = TextEditingController(text: widget.tripData.parkingCost.toStringAsFixed(0));
    _foodCtrl = TextEditingController(text: widget.tripData.foodOthers.toStringAsFixed(0));
  }

  @override
  void dispose() {
    _fuelCtrl.dispose();
    _tollCtrl.dispose();
    _parkingCtrl.dispose();
    _foodCtrl.dispose();
    super.dispose();
  }

  void _onNext() {
    widget.tripData.updateCosts(
      fuelPrice: double.tryParse(_fuelCtrl.text.trim()) ?? widget.tripData.fuelPrice,
      tollCost: double.tryParse(_tollCtrl.text.trim()) ?? widget.tripData.tollCost,
      parkingCost: double.tryParse(_parkingCtrl.text.trim()) ?? widget.tripData.parkingCost,
      foodOthers: double.tryParse(_foodCtrl.text.trim()) ?? widget.tripData.foodOthers,
    );
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => Step4ReviewSummaryScreen(tripData: widget.tripData)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const StepHeader(color: AppColors.orange, activeStep: 3),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Costs & Expenses',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.orange)),
                    const SizedBox(height: 4),
                    const Text('Add fuel price, toll, parking and other costs',
                        style: TextStyle(color: AppColors.textGrey, fontSize: 13)),
                    const SizedBox(height: 22),
                    _CostField(label: 'Fuel Price (৳/L)', controller: _fuelCtrl),
                    const SizedBox(height: 16),
                    _CostField(label: 'Toll Cost (৳)', controller: _tollCtrl),
                    const SizedBox(height: 16),
                    _CostField(label: 'Parking Cost (৳)', controller: _parkingCtrl),
                    const SizedBox(height: 16),
                    _CostField(label: 'Food & Others (৳)', controller: _foodCtrl),
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
                        onPressed: _onNext,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: AppColors.orange,
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
          ],
        ),
      ),
    );
  }
}

class _CostField extends StatelessWidget {
  final String label;
  final TextEditingController controller;

  const _CostField({required this.label, required this.controller});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(label, style: const TextStyle(color: AppColors.textGrey, fontSize: 12)),
        const SizedBox(height: 6),
        TextField(
          controller: controller,
          keyboardType: const TextInputType.numberWithOptions(decimal: true),
          style: const TextStyle(fontWeight: FontWeight.w600, color: AppColors.textDark),
          decoration: InputDecoration(
            filled: true,
            fillColor: Colors.white,
            contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(color: AppColors.borderGrey),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(color: AppColors.borderGrey),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(color: AppColors.orange, width: 1.5),
            ),
          ),
        ),
      ],
    );
  }
}
