import 'package:flutter/material.dart';
import '../models/trip_data.dart';
import '../theme/app_theme.dart';
import '../widgets/step_indicator.dart';
import 'step2_select_vehicle_screen.dart';

class Step1TripDetailsScreen extends StatefulWidget {
  final TripData tripData;
  const Step1TripDetailsScreen({super.key, required this.tripData});

  @override
  State<Step1TripDetailsScreen> createState() => _Step1TripDetailsScreenState();
}

class _Step1TripDetailsScreenState extends State<Step1TripDetailsScreen> {
  late final TextEditingController _fromCtrl;
  late final TextEditingController _toCtrl;
  late final TextEditingController _distanceCtrl;
  late final TextEditingController _passengersCtrl;

  @override
  void initState() {
    super.initState();
    _fromCtrl = TextEditingController(text: widget.tripData.from);
    _toCtrl = TextEditingController(text: widget.tripData.to);
    _distanceCtrl = TextEditingController(text: widget.tripData.distanceKm.toStringAsFixed(0));
    _passengersCtrl = TextEditingController(text: widget.tripData.passengers.toString());
  }

  @override
  void dispose() {
    _fromCtrl.dispose();
    _toCtrl.dispose();
    _distanceCtrl.dispose();
    _passengersCtrl.dispose();
    super.dispose();
  }

  void _onNext() {
    widget.tripData.updateTripDetails(
      from: _fromCtrl.text.trim().isEmpty ? widget.tripData.from : _fromCtrl.text.trim(),
      to: _toCtrl.text.trim().isEmpty ? widget.tripData.to : _toCtrl.text.trim(),
      distanceKm: double.tryParse(_distanceCtrl.text.trim()) ?? widget.tripData.distanceKm,
      passengers: int.tryParse(_passengersCtrl.text.trim()) ?? widget.tripData.passengers,
    );
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => Step2SelectVehicleScreen(tripData: widget.tripData)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const StepHeader(color: AppColors.primaryBlue, activeStep: 1),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Trip Details',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.textDark)),
                    const SizedBox(height: 4),
                    const Text('Enter basic information about your trip',
                        style: TextStyle(color: AppColors.textGrey, fontSize: 13)),
                    const SizedBox(height: 22),
                    _LabeledField(
                      label: 'From',
                      controller: _fromCtrl,
                      icon: Icons.location_on_outlined,
                    ),
                    const SizedBox(height: 16),
                    _LabeledField(
                      label: 'To',
                      controller: _toCtrl,
                      icon: Icons.location_on_outlined,
                    ),
                    const SizedBox(height: 16),
                    _LabeledField(
                      label: 'Distance (KM)',
                      controller: _distanceCtrl,
                      suffix: 'KM',
                      keyboardType: TextInputType.number,
                    ),
                    const SizedBox(height: 16),
                    _LabeledField(
                      label: 'Passengers',
                      controller: _passengersCtrl,
                      suffix: 'People',
                      keyboardType: TextInputType.number,
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
                  onPressed: _onNext,
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

/// Shared colored header used by all 4 step screens (Back button optional).
class StepHeader extends StatelessWidget {
  final Color color;
  final int activeStep;
  final VoidCallback? onBack;

  const StepHeader({super.key, required this.color, required this.activeStep, this.onBack});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(16, 12, 16, 22),
      decoration: BoxDecoration(color: color),
      child: Column(
        children: [
          Row(
            children: [
              IconButton(
                onPressed: onBack ?? () => Navigator.pop(context),
                icon: const Icon(Icons.arrow_back, color: Colors.white),
              ),
              const Expanded(
                child: Text(
                  'Plan Your Trip',
                  textAlign: TextAlign.center,
                  style: TextStyle(color: Colors.white, fontSize: 17, fontWeight: FontWeight.w600),
                ),
              ),
              const SizedBox(width: 48),
            ],
          ),
          const SizedBox(height: 10),
          StepIndicator(activeStep: activeStep, activeColor: color),
        ],
      ),
    );
  }
}

class _LabeledField extends StatelessWidget {
  final String label;
  final TextEditingController controller;
  final IconData? icon;
  final String? suffix;
  final TextInputType? keyboardType;

  const _LabeledField({
    required this.label,
    required this.controller,
    this.icon,
    this.suffix,
    this.keyboardType,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(label, style: const TextStyle(color: AppColors.textGrey, fontSize: 12)),
        const SizedBox(height: 6),
        TextField(
          controller: controller,
          keyboardType: keyboardType,
          style: const TextStyle(fontWeight: FontWeight.w600, color: AppColors.textDark),
          decoration: InputDecoration(
            filled: true,
            fillColor: Colors.white,
            suffixIcon: icon != null ? Icon(icon, color: AppColors.textGrey) : null,
            suffixText: suffix,
            suffixStyle: const TextStyle(color: AppColors.textGrey, fontWeight: FontWeight.w500),
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
              borderSide: const BorderSide(color: AppColors.primaryBlue, width: 1.5),
            ),
          ),
        ),
      ],
    );
  }
}
