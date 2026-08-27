import 'package:flutter/material.dart';
import 'theme/app_theme.dart';
import 'screens/home_screen.dart';
import 'screens/plan_trip_overview_screen.dart';

void main() {
  runApp(const TripWiseApp());
}

class TripWiseApp extends StatelessWidget {
  const TripWiseApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TripWise',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.theme,
      initialRoute: '/',
      routes: {
        '/': (context) => const HomeScreen(),
        '/plan': (context) => const PlanTripOverviewScreen(),
      },
    );
  }
}
