package carRentalExercise;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Vehicle implements Serializable {
    private final UUID id = UUID.randomUUID();
    private final String brand;
    private final String model;
    private final double pricePerDay;
    private final List<Rental> rentalList;
    private final String licensePlate;

    public Vehicle(String brand, String model, String licensePlate, double pricePerDay) {
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

    public double getPricePerDay() {
        return pricePerDay;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public List<Rental> getRentalList() {
        return new ArrayList<>(rentalList);
    }

    public void addRental(Rental rental){
            rentalList.add(rental);
    }

    public boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        for (Rental rental : rentalList) {
            LocalDate returnDate = rental.getReturnDate();
            if (returnDate == null){
                return false;
            }
            if (!startDate.isAfter(rental.getEndDate()) && !endDate.isBefore(rental.getStartDate()) && !returnDate.isBefore(rental.getStartDate())) {
                return false;
            }
        }
        return true;
    }


}
