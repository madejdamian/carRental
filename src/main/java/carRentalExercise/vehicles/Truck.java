package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

public class Truck extends Vehicle {
    private final DriverLicenseCategory driverLicenseCategory;
    private final int capacity;

    public Truck(String brand, String model, String licensePlate, double pricePerDay, int capacity,DriverLicenseCategory driverLicenseCategory ) {
        super(brand, model, licensePlate, pricePerDay);
        this.driverLicenseCategory = driverLicenseCategory;
        this.capacity = capacity;
    }

    public DriverLicenseCategory getDriverLicenseCategory() {
        return driverLicenseCategory;
    }

    public int getCapacity() {
        return capacity;
    }
}
