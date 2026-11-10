package carrental.model;

import java.io.Serial;
import java.math.BigDecimal;

public class Car extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int seatsNumber;

    public Car(String brand, String model, String licensePlate, BigDecimal pricePerDay, int seatsNumber) {
        super(brand, model,licensePlate, pricePerDay);
        this.seatsNumber = seatsNumber;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

}
