package carRentalExercise.services;

import carRentalExercise.Customer;
import carRentalExercise.Rental;
import carRentalExercise.Vehicle;
import carRentalExercise.database.AppState;
import carRentalExercise.database.DataManager;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class RentalService {
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final Set<Rental> rentalList;
    private final AppState appState;
    private final DataManager dataManager;

    public RentalService(VehicleService vehicleService, CustomerService customerService, AppState appState, DataManager dataManager) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.appState = appState;
        this.dataManager = dataManager;
        rentalList = appState.getRentals();
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate startDate, LocalDate endDate) {
        Rental rental = new Rental(startDate, endDate, customer, vehicle);
        addRentalToDatabase(rental);
        dataManager.saveFile(appState);
        System.out.println("Wypozyczono pojazd.");
    }

    public void returnVehicle(Vehicle vehicle, Customer customer) {
        Rental rental = getRentalByVehicleAndCustomer(vehicle, customer);
        rental.setReturnDate(LocalDate.now());
        dataManager.saveFile(appState);
        System.out.println("Zwrocono pojazd.");

    }

    public Set<Rental> getRentalList() {
        return new HashSet<>(rentalList);
    }

    public Vehicle getVehicle(String vehicleLicensePlate) {
        return vehicleService.getVehicleList().stream()
                .filter(vehicle -> vehicle.getLicensePlate().equals(vehicleLicensePlate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego pojazdu."));
    }

    public Customer getCustomer(String customerEmail) {
        return customerService.getCustomersList().stream()
                .filter(customer -> customer.getEmail().equals(customerEmail))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego klienta."));
    }

    private void addRentalToDatabase(Rental rental) {
        Vehicle vehicle = vehicleService.getVehicleById(rental.getVehicleId());
        if (!vehicle.checkAvailability(rental.getStartDate(), rental.getEndDate())) {
            throw new IllegalArgumentException("Pojazd niedostepny");
        }
        vehicle.addRental(rental);
        rentalList.add(rental);
    }

    private Rental getRentalByVehicleAndCustomer(Vehicle vehicle, Customer customer) {
        return rentalList.stream()
                .filter(rental -> rental.getCustomer().equals(customer))
                .filter(rental -> rental.getVehicleId().equals(vehicle.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego pojazdu w wynajmie lub brak takiego klienta"));

    }

}
