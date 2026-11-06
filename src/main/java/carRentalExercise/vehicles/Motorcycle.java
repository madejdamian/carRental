package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

public class Motorcycle extends Vehicle {
    private final DriverLicenseCategory driverLicenseCategory;

    public Motorcycle(String brand, String model,String licensePlate, double pricePerDay, DriverLicenseCategory driverLicenseCategory) {
        super(brand, model,licensePlate, pricePerDay);
        this.driverLicenseCategory = driverLicenseCategory;
    }

    public DriverLicenseCategory getDriverLicenseCategory() {
        return driverLicenseCategory;
    }
}
