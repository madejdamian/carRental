package carRentalExercise.database;

import carRentalExercise.Customer;
import carRentalExercise.Rental;
import carRentalExercise.Vehicle;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class AppState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Set<Customer> customers;
    private final Set<Rental> rentals;
    private final Set<Vehicle> vehicles;

    public AppState(Set<Customer> customers, Set<Rental> rentals, Set<Vehicle> vehicles) {
        this.customers = customers;
        this.rentals = rentals;
        this.vehicles = vehicles;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }
}
