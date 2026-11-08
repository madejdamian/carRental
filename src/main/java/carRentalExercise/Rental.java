package carRentalExercise;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Rental implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Customer customer;
    private final UUID vehicleId;
    private LocalDate returnDate;

    public Rental(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle vehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.vehicleId = vehicle.getId();
        this.returnDate = null;
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
}
