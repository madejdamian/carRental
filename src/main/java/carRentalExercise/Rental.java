package carRentalExercise;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Customer customer;
    private final UUID vehicleId;
    private LocalDate returnDate;
    private final BigDecimal totalPrice;

    public Rental(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle vehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.vehicleId = vehicle.getId();
        this.returnDate = null;
        this.totalPrice = calculateTotalPrice(startDate,endDate,vehicle.getPricePerDay());
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

    public UUID getVehicleId() {
        return vehicleId;
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

    private BigDecimal calculateTotalPrice(LocalDate startDate, LocalDate endDate, BigDecimal vehiclePrice){
        return vehiclePrice.multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate,endDate) + 1));
    }
    public LocalDate getOccupiedUntil(){
        return returnDate == null ? endDate : returnDate;
    }
}
