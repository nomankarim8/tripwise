import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

/// Horizontal 1-2-3-4 stepper shown at the top of each wizard step,
/// matching the bottom row of mockups (purple/green/orange/purple headers).
class StepIndicator extends StatelessWidget {
  final int activeStep; // 1-based: 1,2,3,4
  final Color activeColor;

  const StepIndicator({
    super.key,
    required this.activeStep,
    required this.activeColor,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: List.generate(4, (i) {
        final stepNum = i + 1;
        final isDone = stepNum < activeStep;
        final isActive = stepNum == activeStep;
        final circleColor = isDone || isActive ? activeColor : Colors.white24;
        return Row(
          children: [
            Container(
              width: 30,
              height: 30,
              alignment: Alignment.center,
              decoration: BoxDecoration(
                color: isActive || isDone ? Colors.white : Colors.white24,
                shape: BoxShape.circle,
                border: Border.all(color: Colors.white, width: isActive ? 2 : 0),
              ),
              child: isDone
                  ? Icon(Icons.check, size: 16, color: activeColor)
                  : Text(
                      '$stepNum',
                      style: TextStyle(
                        color: isActive ? activeColor : Colors.white70,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
            ),
            if (stepNum != 4)
              Container(
                width: 34,
                height: 2,
                color: Colors.white38,
              ),
          ],
        );
      }),
    );
  }
}

/// Vertical timeline stepper used on the "Plan Your Trip" overview screen.
class VerticalStepTile extends StatelessWidget {
  final int number;
  final Color color;
  final IconData icon;
  final String title;
  final String subtitle;
  final bool isLast;
  final VoidCallback onTap;

  const VerticalStepTile({
    super.key,
    required this.number,
    required this.color,
    required this.icon,
    required this.title,
    required this.subtitle,
    required this.onTap,
    this.isLast = false,
  });

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Column(
            children: [
              Container(
                width: 34,
                height: 34,
                decoration: BoxDecoration(color: color, shape: BoxShape.circle),
                alignment: Alignment.center,
                child: Text(
                  '$number',
                  style: const TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
                ),
              ),
              if (!isLast)
                Expanded(
                  child: Container(width: 2, color: AppColors.borderGrey),
                ),
            ],
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(bottom: 20),
              child: InkWell(
                onTap: onTap,
                borderRadius: BorderRadius.circular(16),
                child: Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: AppColors.borderGrey),
                  ),
                  child: Row(
                    children: [
                      Container(
                        width: 44,
                        height: 44,
                        decoration: BoxDecoration(
                          color: color.withValues(alpha: 0.12),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Icon(icon, color: color),
                      ),
                      const SizedBox(width: 14),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(title,
                                style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 15,
                                    color: color)),
                            const SizedBox(height: 2),
                            Text(subtitle,
                                style: const TextStyle(
                                    fontSize: 12, color: AppColors.textGrey)),
                          ],
                        ),
                      ),
                      const Icon(Icons.chevron_right, color: AppColors.textGrey),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
