package carrental.service;

import carrental.model.Customer;
import carrental.model.Rental;
import carrental.model.Vehicle;
import carrental.persistence.AppState;
import carrental.persistence.DataManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        Rental rental = getStartedRentalByVehicleAndCustomer(vehicle, customer);
        rental.setReturnDate(LocalDate.now());
        dataManager.saveFile(appState);
        System.out.println("Zwrocono pojazd.");
    }

    public Set<Rental> getRentalList() {
        return new HashSet<>(rentalList);
    }

    public Vehicle getVehicle(String vehicleLicensePlate) {
        return vehicleService.getVehicleList().stream()
                .filter(vehicle -> vehicle.getLicensePlate().equalsIgnoreCase(vehicleLicensePlate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego pojazdu."));
    }

    public Customer getCustomer(String customerEmail) {
        return customerService.getCustomersList().stream()
                .filter(customer -> customer.getEmail().equals(customerEmail))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego klienta."));
    }

    public List<Vehicle> getAvailableVehicles() {
        return appState.getVehicles().stream()
                .filter(vehicle -> vehicle.checkAvailability(LocalDate.now(), LocalDate.now()))
                .toList();
    }

    public List<Rental> getCustomerHistory(Customer customer) {
        return rentalList.stream()
                .filter(rental -> rental.getCustomer().equals(customer))
                .toList();
    }

    public BigDecimal getTotalIncome() {
        return rentalList.stream()
                .filter(rental -> rental.getReturnDate() != null)
                .map(Rental::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<Customer> getMostActiveUsersList() {
        Map<Customer, Long> map = rentalList.stream()
                .collect(Collectors.groupingBy(Rental::getCustomer, Collectors.counting()));

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    private void addRentalToDatabase(Rental rental) {
        Vehicle vehicle = rental.getVehicle();
        if (!vehicle.checkAvailability(rental.getStartDate(), rental.getEndDate())) {
            throw new IllegalArgumentException("Pojazd niedostepny w tym terminie. ");
        }
        vehicle.addRental(rental);
        rentalList.add(rental);
    }

    private Rental getStartedRentalByVehicleAndCustomer(Vehicle vehicle, Customer customer) {
        return rentalList.stream()
                .filter(rental -> rental.getCustomer().equals(customer))
                .filter(rental -> rental.getVehicle().equals(vehicle))
                .filter(rental -> rental.getReturnDate() == null)
                .filter(rental -> !rental.getStartDate().isAfter(LocalDate.now()))
                .min(Comparator.comparing(Rental::getStartDate))
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego najmu w bazie."));
    }


}
