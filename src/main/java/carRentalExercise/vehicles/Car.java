package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

public class Car extends Vehicle {
    private final int seatsNumber;
    private final DriverLicenseCategory driverLicenseCategory;

    public Car(String brand, String model,String licensePlate, double pricePerDay, int seatsNumber, DriverLicenseCategory driverLicenseCategory) {
        super(brand, model,licensePlate, pricePerDay);
        this.seatsNumber = seatsNumber;
        this.driverLicenseCategory = driverLicenseCategory;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public DriverLicenseCategory getDriverLicenseCategory() {
        return driverLicenseCategory;
    }
}
