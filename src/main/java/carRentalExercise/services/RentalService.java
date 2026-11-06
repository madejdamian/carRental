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
    private Set<Rental> rentalList = new HashSet<>();
    private static AppState APP_STATE;
    private static DataManager dataManager = new DataManager();

    public RentalService(VehicleService vehicleService, CustomerService customerService) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate startDate, LocalDate endDate) {
        Rental rental = new Rental(startDate, endDate, customer, vehicle);
        addRentalToDatabase(rental);
        dataManager.saveFile(APP_STATE);
        System.out.println("Wypozyczono pojazd.");
    }

    public void returnVehicle(String vehicleLicensePlate, String customerEmail) {
        Vehicle vehicle = getVehicle(vehicleLicensePlate);
        Customer customer = getCustomer(customerEmail);
        Rental rental = getRentalByVehicleAndCustomer(vehicle, customer);
        rental.setReturnDate(LocalDate.now());
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
        System.out.println("Wynajeto pojazd.");
    }

    private Rental getRentalByVehicleAndCustomer(Vehicle vehicle, Customer customer) {
        return rentalList.stream()
                .filter(rental -> rental.getCustomer().equals(customer))
                .filter(rental -> rental.getVehicleId().equals(vehicle.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego pojazdu w wynajmie lub brak takiego klienta"));

    }

    public void setRentalList(Set<Rental> rentalList) {
        this.rentalList = rentalList;
    }
}
