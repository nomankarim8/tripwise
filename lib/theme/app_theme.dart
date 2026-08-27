import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

/// Central color & style palette matching the TripWise design.
class AppColors {
  AppColors._();

  // Primary blue (header gradients, primary buttons, step 1)
  static const Color primaryBlueDark = Color(0xFF3B5CF5);
  static const Color primaryBlue = Color(0xFF4E7CFB);
  static const Color primaryBlueLight = Color(0xFF6FA1FF);

  // Accent colors for each step / feature card
  static const Color green = Color(0xFF22B14C);
  static const Color greenLight = Color(0xFF2ECC71);
  static const Color orange = Color(0xFFF7941D);
  static const Color orangeLight = Color(0xFFFFB74D);
  static const Color purple = Color(0xFF9B4DE0);
  static const Color purpleLight = Color(0xFFB266E8);

  static const Color background = Color(0xFFF5F6FA);
  static const Color cardWhite = Color(0xFFFFFFFF);
  static const Color textDark = Color(0xFF1E2233);
  static const Color textGrey = Color(0xFF8A8FA3);
  static const Color borderGrey = Color(0xFFE3E6F0);
}

class AppTheme {
  AppTheme._();

  static ThemeData get theme {
    final base = ThemeData(
      useMaterial3: true,
      scaffoldBackgroundColor: AppColors.background,
      colorScheme: ColorScheme.fromSeed(
        seedColor: AppColors.primaryBlue,
        primary: AppColors.primaryBlue,
      ),
      fontFamily: GoogleFonts.poppins().fontFamily,
    );
    return base.copyWith(
      textTheme: GoogleFonts.poppinsTextTheme(base.textTheme),
      appBarTheme: const AppBarTheme(
        backgroundColor: Colors.transparent,
        elevation: 0,
        centerTitle: true,
      ),
    );
  }
}

/// Reusable gradient definitions.
class AppGradients {
  AppGradients._();

  static const LinearGradient blueHeader = LinearGradient(
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
    colors: [AppColors.primaryBlueDark, AppColors.primaryBlueLight],
  );

  static const LinearGradient blueBar = LinearGradient(
    colors: [AppColors.primaryBlueDark, AppColors.primaryBlue],
  );

  static const LinearGradient greenBar = LinearGradient(
    colors: [Color(0xFF16A34A), AppColors.green],
  );

  static const LinearGradient orangeBar = LinearGradient(
    colors: [Color(0xFFEA7C0C), AppColors.orange],
  );

  static const LinearGradient purpleBar = LinearGradient(
    colors: [Color(0xFF8B2FD1), AppColors.purple],
  );
}
