import 'package:flutter/material.dart';
import '../models/trip_data.dart';
import '../theme/app_theme.dart';
import 'step1_trip_details_screen.dart';
import 'step3_costs_expenses_screen.dart';

class Step2SelectVehicleScreen extends StatefulWidget {
  final TripData tripData;
  const Step2SelectVehicleScreen({super.key, required this.tripData});

  @override
  State<Step2SelectVehicleScreen> createState() => _Step2SelectVehicleScreenState();
}

class _Step2SelectVehicleScreenState extends State<Step2SelectVehicleScreen> {
  late VehicleType _selected;

  @override
  void initState() {
    super.initState();
    _selected = widget.tripData.vehicle;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const StepHeader(color: AppColors.green, activeStep: 2),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Select Vehicle',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.green)),
                    const SizedBox(height: 4),
                    const Text('Choose your vehicle type',
                        style: TextStyle(color: AppColors.textGrey, fontSize: 13)),
                    const SizedBox(height: 20),
                    _VehicleTile(
                      icon: Icons.directions_car_filled_rounded,
                      type: VehicleType.car,
                      selected: _selected == VehicleType.car,
                      onTap: () => setState(() => _selected = VehicleType.car),
                    ),
                    const SizedBox(height: 14),
                    _VehicleTile(
                      icon: Icons.two_wheeler_rounded,
                      type: VehicleType.motorcycle,
                      selected: _selected == VehicleType.motorcycle,
                      onTap: () => setState(() => _selected = VehicleType.motorcycle),
                    ),
                    const SizedBox(height: 14),
                    _VehicleTile(
                      icon: Icons.electric_rickshaw_rounded,
                      type: VehicleType.cngAuto,
                      selected: _selected == VehicleType.cngAuto,
                      onTap: () => setState(() => _selected = VehicleType.cngAuto),
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
                          widget.tripData.updateVehicle(_selected);
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (_) => Step3CostsExpensesScreen(tripData: widget.tripData)),
                          );
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: AppColors.green,
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

class _VehicleTile extends StatelessWidget {
  final IconData icon;
  final VehicleType type;
  final bool selected;
  final VoidCallback onTap;

  const _VehicleTile({
    required this.icon,
    required this.type,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: selected ? AppColors.green.withValues(alpha: 0.08) : Colors.white,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: selected ? AppColors.green : AppColors.borderGrey,
            width: selected ? 1.6 : 1,
          ),
        ),
        child: Row(
          children: [
            Container(
              width: 48,
              height: 48,
              decoration: BoxDecoration(
                color: AppColors.background,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Icon(icon, color: AppColors.textDark),
            ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(type.label,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                  const SizedBox(height: 2),
                  Text(type.mileageLabel,
                      style: const TextStyle(color: AppColors.textGrey, fontSize: 12)),
                ],
              ),
            ),
            Icon(
              selected ? Icons.check_circle_rounded : Icons.radio_button_off_rounded,
              color: selected ? AppColors.green : AppColors.borderGrey,
              size: 26,
            ),
          ],
        ),
      ),
    );
  }
}
