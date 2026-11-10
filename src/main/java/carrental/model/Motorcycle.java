package carrental.model;

import java.io.Serial;
import java.math.BigDecimal;

public class Motorcycle extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;

    public Motorcycle(String brand, String model, String licensePlate, BigDecimal pricePerDay) {
        super(brand, model,licensePlate, pricePerDay);
    }

}
