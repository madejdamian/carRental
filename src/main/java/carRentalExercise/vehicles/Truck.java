package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

import java.io.Serial;
import java.math.BigDecimal;

public class Truck extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final DriverLicenseCategory driverLicenseCategory;
    private final int capacity;

    public Truck(String brand, String model, String licensePlate, BigDecimal pricePerDay, int capacity, DriverLicenseCategory driverLicenseCategory ) {
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
