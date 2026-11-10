package carrental.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Customer customer;
    private final Vehicle vehicle;
    private final BigDecimal totalPrice;
    private LocalDate returnDate;


    public Rental(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle vehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.vehicle = vehicle;
        this.returnDate = null;
        this.totalPrice = calculateTotalPrice(startDate, endDate, vehicle.getPricePerDay());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getOccupiedUntil() {
        return returnDate == null ? endDate : returnDate;
    }

    @Override
    public String toString() {
        return vehicle.getBrand() + " " + vehicle.getModel() + ", " + startDate.format(FORMATTER) + " - " + endDate.format(FORMATTER);
    }

    private BigDecimal calculateTotalPrice(LocalDate startDate, LocalDate endDate, BigDecimal vehiclePrice) {
        return vehiclePrice.multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate) + 1));
    }

}
