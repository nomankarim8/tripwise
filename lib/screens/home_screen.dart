import 'package:flutter/material.dart';
import '../theme/app_theme.dart';
import '../widgets/app_drawer.dart';
import '../widgets/bottom_nav.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _navIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      drawer: const AppDrawer(currentRoute: 'home'),
      body: Builder(
        builder: (context) => CustomScrollView(
          slivers: [
            SliverToBoxAdapter(child: _HeaderBanner(onMenuTap: () => Scaffold.of(context).openDrawer())),
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(20, 24, 20, 16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Row(
                      children: [
                        Text('Hello, Traveler! ',
                            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.textDark)),
                        Text('👋', style: TextStyle(fontSize: 20)),
                      ],
                    ),
                    const SizedBox(height: 4),
                    const Text('Plan your trip smartly and save more.',
                        style: TextStyle(color: AppColors.textGrey, fontSize: 13)),
                    const SizedBox(height: 20),
                    Row(
                      children: [
                        Expanded(
                          child: _FeatureCard(
                            gradient: AppGradients.blueBar,
                            icon: Icons.route_rounded,
                            title: 'Plan New Trip',
                            subtitle: 'Estimate trip cost step by step',
                            onTap: () => Navigator.pushNamed(context, '/plan'),
                          ),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: _FeatureCard(
                            gradient: AppGradients.greenBar,
                            icon: Icons.directions_car_filled_rounded,
                            title: 'Compare Vehicles',
                            subtitle: 'Find the best & cheapest option',
                            onTap: () {},
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 14),
                    Row(
                      children: [
                        Expanded(
                          child: _FeatureCard(
                            gradient: AppGradients.orangeBar,
                            icon: Icons.history_rounded,
                            title: 'Trip History',
                            subtitle: 'View your previous trips',
                            onTap: () {},
                          ),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: _FeatureCard(
                            gradient: AppGradients.purpleBar,
                            icon: Icons.bar_chart_rounded,
                            title: 'Analytics',
                            subtitle: 'Track your travel insights',
                            onTap: () {},
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: TripWiseBottomNav(
        currentIndex: _navIndex,
        onTap: (i) => setState(() => _navIndex = i),
        onPlanTrip: () => Navigator.pushNamed(context, '/plan'),
      ),
    );
  }
}

class _HeaderBanner extends StatelessWidget {
  final VoidCallback onMenuTap;
  const _HeaderBanner({required this.onMenuTap});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 300,
      width: double.infinity,
      decoration: const BoxDecoration(
        gradient: AppGradients.blueHeader,
        borderRadius: BorderRadius.only(
          bottomLeft: Radius.circular(28),
          bottomRight: Radius.circular(28),
        ),
      ),
      child: ClipRRect(
        borderRadius: const BorderRadius.only(
          bottomLeft: Radius.circular(28),
          bottomRight: Radius.circular(28),
        ),
        child: Stack(
          children: [
            // Decorative clouds
            Positioned(top: 60, left: 24, child: _cloud(40)),
            Positioned(top: 40, left: 60, child: _cloud(56)),
            Positioned(top: 70, right: 40, child: _cloud(30)),
            // Mountains + road illustration
            Positioned(
              bottom: 0,
              left: 0,
              right: 0,
              child: CustomPaint(
                size: const Size(double.infinity, 140),
                painter: _MountainRoadPainter(),
              ),
            ),
            Positioned(
              bottom: 26,
              left: 30,
              child: Icon(Icons.directions_car_filled, size: 60, color: Colors.lightBlue.shade200),
            ),
            Positioned(
              bottom: 26,
              right: 40,
              child: Icon(Icons.two_wheeler_rounded, size: 46, color: Colors.orange.shade300),
            ),
            SafeArea(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    IconButton(
                      onPressed: onMenuTap,
                      icon: const Icon(Icons.menu_rounded, color: Colors.white, size: 28),
                    ),
                    const SizedBox(height: 30),
                    const Text(
                      'TripWise',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 6),
                    const Text(
                      'Smart Vehicle & Trip\nCost Planner',
                      style: TextStyle(color: Colors.white, fontSize: 16, height: 1.3),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _cloud(double size) => Container(
        width: size,
        height: size * 0.5,
        decoration: BoxDecoration(
          color: Colors.white.withValues(alpha: 0.5),
          borderRadius: BorderRadius.circular(size),
        ),
      );
}

class _MountainRoadPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final mountainPaint = Paint()..color = Colors.indigo.shade300.withValues(alpha: 0.6);
    final path = Path()
      ..moveTo(0, size.height)
      ..lineTo(0, size.height * 0.5)
      ..lineTo(size.width * 0.2, size.height * 0.15)
      ..lineTo(size.width * 0.4, size.height * 0.5)
      ..lineTo(size.width * 0.6, size.height * 0.1)
      ..lineTo(size.width * 0.85, size.height * 0.45)
      ..lineTo(size.width, size.height * 0.25)
      ..lineTo(size.width, size.height)
      ..close();
    canvas.drawPath(path, mountainPaint);

    final roadPaint = Paint()..color = Colors.blueGrey.shade700;
    canvas.drawRect(Rect.fromLTWH(0, size.height - 22, size.width, 22), roadPaint);

    final dashPaint = Paint()
      ..color = Colors.white
      ..strokeWidth = 3;
    double x = 0;
    while (x < size.width) {
      canvas.drawLine(
        Offset(x, size.height - 11),
        Offset(x + 14, size.height - 11),
        dashPaint,
      );
      x += 28;
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class _FeatureCard extends StatelessWidget {
  final Gradient gradient;
  final IconData icon;
  final String title;
  final String subtitle;
  final VoidCallback onTap;

  const _FeatureCard({
    required this.gradient,
    required this.icon,
    required this.title,
    required this.subtitle,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(18),
      child: Container(
        height: 150,
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          gradient: gradient,
          borderRadius: BorderRadius.circular(18),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.12),
              blurRadius: 10,
              offset: const Offset(0, 6),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              width: 42,
              height: 42,
              decoration: BoxDecoration(
                color: Colors.white.withValues(alpha: 0.22),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Icon(icon, color: Colors.white, size: 22),
            ),
            const Spacer(),
            Text(title,
                style: const TextStyle(
                    color: Colors.white, fontWeight: FontWeight.bold, fontSize: 15)),
            const SizedBox(height: 4),
            Text(subtitle,
                style: const TextStyle(color: Colors.white70, fontSize: 11, height: 1.3)),
          ],
        ),
      ),
    );
  }
}
