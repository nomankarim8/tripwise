import 'package:flutter/foundation.dart';

enum VehicleType { car, motorcycle, cngAuto }

extension VehicleTypeX on VehicleType {
  String get label {
    switch (this) {
      case VehicleType.car:
        return 'Car';
      case VehicleType.motorcycle:
        return 'Motorcycle';
      case VehicleType.cngAuto:
        return 'CNG / Auto';
    }
  }

  String get mileageLabel {
    switch (this) {
      case VehicleType.car:
        return 'Mileage: 14 KM/L';
      case VehicleType.motorcycle:
        return 'Mileage: 40 KM/L';
      case VehicleType.cngAuto:
        return 'Mileage: 25 KM/KG';
    }
  }

  double get mileage {
    switch (this) {
      case VehicleType.car:
        return 14;
      case VehicleType.motorcycle:
        return 40;
      case VehicleType.cngAuto:
        return 25;
    }
  }
}

/// Holds all data collected across the 4-step "Plan Your Trip" wizard.
/// Uses ChangeNotifier so screens can share/update a single instance.
class TripData extends ChangeNotifier {
  String from = 'Dhaka, Bangladesh';
  String to = "Cox's Bazar, Bangladesh";
  double distanceKm = 393;
  int passengers = 4;

  VehicleType vehicle = VehicleType.car;

  double fuelPrice = 130;
  double tollCost = 450;
  double parkingCost = 100;
  double foodOthers = 800;

  double get fuelCost => (distanceKm / vehicle.mileage) * fuelPrice;

  double get totalCost => fuelCost + tollCost + parkingCost + foodOthers;

  void updateTripDetails({
    required String from,
    required String to,
    required double distanceKm,
    required int passengers,
  }) {
    this.from = from;
    this.to = to;
    this.distanceKm = distanceKm;
    this.passengers = passengers;
    notifyListeners();
  }

  void updateVehicle(VehicleType v) {
    vehicle = v;
    notifyListeners();
  }

  void updateCosts({
    required double fuelPrice,
    required double tollCost,
    required double parkingCost,
    required double foodOthers,
  }) {
    this.fuelPrice = fuelPrice;
    this.tollCost = tollCost;
    this.parkingCost = parkingCost;
    this.foodOthers = foodOthers;
    notifyListeners();
  }

  void reset() {
    from = 'Dhaka, Bangladesh';
    to = "Cox's Bazar, Bangladesh";
    distanceKm = 393;
    passengers = 4;
    vehicle = VehicleType.car;
    fuelPrice = 130;
    tollCost = 450;
    parkingCost = 100;
    foodOthers = 800;
    notifyListeners();
  }
}
