package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

import java.io.Serial;
import java.math.BigDecimal;

public class Motorcycle extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final DriverLicenseCategory driverLicenseCategory;

    public Motorcycle(String brand, String model, String licensePlate, BigDecimal pricePerDay, DriverLicenseCategory driverLicenseCategory) {
        super(brand, model,licensePlate, pricePerDay);
        this.driverLicenseCategory = driverLicenseCategory;
    }

    public DriverLicenseCategory getDriverLicenseCategory() {
        return driverLicenseCategory;
    }
}
