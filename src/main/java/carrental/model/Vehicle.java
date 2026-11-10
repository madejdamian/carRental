package carrental.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Vehicle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final String brand;
    private final String model;
    private final BigDecimal pricePerDay;
    private final List<Rental> rentalList;
    private final String licensePlate;

    public Vehicle(String brand, String model, String licensePlate, BigDecimal pricePerDay) {
        if (!isPriceValid(pricePerDay)) {
            throw new IllegalArgumentException("Cena nie moze byc zerowa lub ujemna.");
        }
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.pricePerDay = pricePerDay;
        this.rentalList = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public List<Rental> getRentalList() {
        return new ArrayList<>(rentalList);
    }

    public void addRental(Rental rental) {
        rentalList.add(rental);
    }

    public boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        for (Rental rental : rentalList) {
            if (!(startDate.isAfter(rental.getOccupiedUntil()) ||
                    endDate.isBefore(rental.getStartDate()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPriceValid(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
