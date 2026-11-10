package carrental.model;

import java.io.Serial;
import java.math.BigDecimal;

public class Truck extends Vehicle {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int capacity;

    public Truck(String brand, String model, String licensePlate, BigDecimal pricePerDay, int capacity ) {
        super(brand, model, licensePlate, pricePerDay);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
