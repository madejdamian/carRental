package carRentalExercise.vehicles;

import carRentalExercise.DriverLicenseCategory;
import carRentalExercise.Vehicle;

import java.io.Serial;
import java.math.BigDecimal;

public class Car extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int seatsNumber;
    private final DriverLicenseCategory driverLicenseCategory;

    public Car(String brand, String model, String licensePlate, BigDecimal pricePerDay, int seatsNumber, DriverLicenseCategory driverLicenseCategory) {
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
