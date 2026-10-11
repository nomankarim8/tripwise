package com.example.tripwise.model

enum class VehicleType(
    val label: String,
    val mileage: Double,
    val unit: String
) {
    CAR(
        label = "Car",
        mileage = 14.0,
        unit = "KM/L"
    ),
    MOTORCYCLE(
        label = "Motorcycle",
        mileage = 40.0,
        unit = "KM/L"
    ),
    CNG_AUTO(
        label = "CNG / Auto",
        mileage = 25.0,
        unit = "KM/KG"
    );

    val mileageLabel: String
        get() = "Mileage: ${mileage.toInt()} $unit"
}
