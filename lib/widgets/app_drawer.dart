import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class AppDrawer extends StatelessWidget {
  final String currentRoute;
  const AppDrawer({super.key, this.currentRoute = 'home'});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: AppColors.cardWhite,
      width: 300,
      child: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(
              padding: const EdgeInsets.symmetric(vertical: 32),
              decoration: const BoxDecoration(gradient: AppGradients.blueHeader),
              child: Column(
                children: [
                  Container(
                    width: 72,
                    height: 72,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.directions_car_filled,
                        color: AppColors.primaryBlueDark, size: 36),
                  ),
                  const SizedBox(height: 14),
                  const Text(
                    'TripWise',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Smart Trip Cost Planner',
                    style: TextStyle(color: Colors.white70, fontSize: 12),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 8),
            Expanded(
              child: ListView(
                padding: const EdgeInsets.symmetric(vertical: 8),
                children: [
                  _DrawerItem(
                    icon: Icons.home_rounded,
                    label: 'Home',
                    selected: currentRoute == 'home',
                    onTap: () {
                      Navigator.pop(context);
                      Navigator.popUntil(context, (r) => r.isFirst);
                    },
                  ),
                  _DrawerItem(
                    icon: Icons.route_rounded,
                    label: 'Plan New Trip',
                    selected: currentRoute == 'plan',
                    onTap: () {
                      Navigator.pop(context);
                      Navigator.pushNamed(context, '/plan');
                    },
                  ),
                  _DrawerItem(
                    icon: Icons.directions_car_outlined,
                    label: 'Compare Vehicles',
                    selected: currentRoute == 'compare',
                    onTap: () => Navigator.pop(context),
                  ),
                  _DrawerItem(
                    icon: Icons.access_time_rounded,
                    label: 'Trip History',
                    selected: currentRoute == 'history',
                    onTap: () => Navigator.pop(context),
                  ),
                  _DrawerItem(
                    icon: Icons.bar_chart_rounded,
                    label: 'Analytics',
                    selected: currentRoute == 'analytics',
                    onTap: () => Navigator.pop(context),
                  ),
                  const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 20, vertical: 8),
                    child: Divider(color: AppColors.borderGrey),
                  ),
                  _DrawerItem(
                    icon: Icons.settings_outlined,
                    label: 'Settings',
                    onTap: () => Navigator.pop(context),
                  ),
                  _DrawerItem(
                    icon: Icons.help_outline_rounded,
                    label: 'Help & Support',
                    onTap: () => Navigator.pop(context),
                  ),
                  _DrawerItem(
                    icon: Icons.info_outline_rounded,
                    label: 'About TripWise',
                    onTap: () => Navigator.pop(context),
                  ),
                ],
              ),
            ),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 20, vertical: 4),
              child: Divider(color: AppColors.borderGrey),
            ),
            _DrawerItem(
              icon: Icons.logout_rounded,
              label: 'Logout',
              iconColor: Colors.red,
              textColor: Colors.red,
              onTap: () => Navigator.pop(context),
            ),
            const SizedBox(height: 12),
          ],
        ),
      ),
    );
  }
}

class _DrawerItem extends StatelessWidget {
  final IconData icon;
  final String label;
  final bool selected;
  final Color? iconColor;
  final Color? textColor;
  final VoidCallback onTap;

  const _DrawerItem({
    required this.icon,
    required this.label,
    required this.onTap,
    this.selected = false,
    this.iconColor,
    this.textColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 3),
      decoration: BoxDecoration(
        color: selected ? AppColors.primaryBlue.withValues(alpha: 0.1) : null,
        borderRadius: BorderRadius.circular(12),
      ),
      child: ListTile(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        leading: Icon(
          icon,
          color: iconColor ?? (selected ? AppColors.primaryBlueDark : AppColors.textGrey),
        ),
        title: Text(
          label,
          style: TextStyle(
            color: textColor ?? (selected ? AppColors.primaryBlueDark : AppColors.textDark),
            fontWeight: selected ? FontWeight.w600 : FontWeight.w500,
            fontSize: 15,
          ),
        ),
        onTap: onTap,
      ),
    );
  }
}
