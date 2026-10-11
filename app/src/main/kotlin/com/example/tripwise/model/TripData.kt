package com.example.tripwise.model

data class TripData(
    val from: String = "Dhaka, Bangladesh",
    val to: String = "Cox's Bazar, Bangladesh",
    val distanceKm: Double = 393.0,
    val passengers: Int = 4,
    val vehicle: VehicleType = VehicleType.CAR,
    val fuelPrice: Double = 130.0,
    val tollCost: Double = 450.0,
    val parkingCost: Double = 100.0,
    val foodOthers: Double = 800.0
) {
    val fuelCost: Double
        get() = if (vehicle.mileage > 0) (distanceKm / vehicle.mileage) * fuelPrice else 0.0

    val totalCost: Double
        get() = fuelCost + tollCost + parkingCost + foodOthers

    val costPerPassenger: Double
        get() = if (passengers > 0) totalCost / passengers else totalCost

    val fuelNeeded: Double
        get() = if (vehicle.mileage > 0) distanceKm / vehicle.mileage else 0.0
}

data class TripHistoryItem(
    val id: String,
    val from: String,
    val to: String,
    val distanceKm: Double,
    val passengers: Int,
    val vehicle: VehicleType,
    val fuelCost: Double,
    val tollCost: Double,
    val parkingCost: Double,
    val foodOthers: Double,
    val totalCost: Double,
    val date: String
)
