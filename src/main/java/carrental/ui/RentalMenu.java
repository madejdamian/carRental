package carrental.ui;

import carrental.model.*;
import carrental.service.CustomerService;
import carrental.service.RentalService;
import carrental.service.VehicleService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RentalMenu {
    private final ConsoleReader consoleReader;
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final RentalService rentalService;

    public RentalMenu(ConsoleReader consoleReader, VehicleService vehicleService, CustomerService customerService, RentalService rentalService) {
        this.consoleReader = consoleReader;
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.rentalService = rentalService;
    }


    public void run() {
        boolean programIsRunning = true;

        while (programIsRunning) {
            showMenu();
            try {
                int choice = consoleReader.readInt("Wybierz opcje:");

                switch (choice) {
                    case 1 -> registerVehicle();
                    case 2 -> registerCustomer();
                    case 3 -> rentalOperations();
                    case 4 -> dataRaportOperations();
                    case 5 -> {
                        programIsRunning = false;
                    }
                    default -> System.out.println("Niewłaściwy wybór\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowy format danych.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (CancelOperationException e) {
                System.out.println("Anulowanie operacji.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===MENU===");
        System.out.println("1. Zarejestruj pojazd.");
        System.out.println("2. Zarejestruj klienta.");
        System.out.println("3. Obsługa wypożyczeń.");
        System.out.println("4. Przegląd danych i generowanie raportów.");
        System.out.println("5. Wyjscie.");
    }

    private void registerVehicle() {
        System.out.println("===Podaj dane pojazdu===");
        System.out.println("Rodzaj: ");
        System.out.println("1. Motocykl.");
        System.out.println("2. Samochod osobowy.");
        System.out.println("3. Samochod ciezarowy.");

        int choice = consoleReader.readInt("Wybierz opcje:");
        switch (choice) {
            case 1 -> registerMotorcycle();
            case 2 -> registerCar();
            case 3 -> registerTruck();
            default -> System.out.println("Niewłasciwy wybór");
        }
    }

    private void registerMotorcycle() {
        System.out.println("===Podaj dane motocykla ===");
        String brand = consoleReader.readString("Marka:");
        String model = consoleReader.readString("Model:");
        String licensePlate = consoleReader.readString("Nr. rejestracyjny:").toUpperCase();
        BigDecimal price = consoleReader.readPrice("Cena za dzien:");
        Vehicle motorcycle = new Motorcycle(brand, model, licensePlate, price);
        vehicleService.addVehicleToDatabase(motorcycle);
        System.out.println("Dodano pojazd do bazy.");
    }


    private void registerCar() {
        System.out.println("===Podaj dane samochodu ===");
        String brand = consoleReader.readString("Marka:");
        String model = consoleReader.readString("Model:");
        String licensePlate = consoleReader.readString("Nr. rejestracyjny:").toUpperCase();
        BigDecimal price = consoleReader.readPrice("Cena za dzien:");
        int seatsNumber = consoleReader.readInt("Ilosc miejsc:");
        Vehicle car = new Car(brand, model, licensePlate, price, seatsNumber);
        vehicleService.addVehicleToDatabase(car);
        System.out.println("Dodano pojazd do bazy.");
    }

    private void registerTruck() {
        System.out.println("===Podaj dane ciezarowki ===");
        String brand = consoleReader.readString("Marka:");
        String model = consoleReader.readString("Model:");
        String licensePlate = consoleReader.readString("Nr. rejestracyjny:").toUpperCase();
        BigDecimal price = consoleReader.readPrice("Cena za dzien:");
        int capacity = consoleReader.readInt("Ładownosc:");
        Vehicle car = new Truck(brand, model, licensePlate, price, capacity);
        vehicleService.addVehicleToDatabase(car);
        System.out.println("Dodano pojazd do bazy.");
    }

    private void registerCustomer() {
        System.out.println("=== Podaj dane klienta ===");
        String name = consoleReader.readString("Imie:");
        String surname = consoleReader.readString("Nazwisko:");
        String email = readAndCheckEmailInDatabase("Email:");
        Customer customer = new Customer(name, surname, email);
        customerService.addCustomerToDatabase(customer);
        System.out.println("Dodano klienta do bazy.");
    }

    private String readAndCheckEmailInDatabase(String info) {
        while (true) {
            String email = consoleReader.readEmail(info);
            if (!customerService.isCustomerExistInDatabase(email)) {
                return email;
            }
            System.out.println("Klient o takim adresie juz istnieje.");
        }
    }

    private void rentalOperations() {
        System.out.println("Wybierz opcje:");
        System.out.println("1. Wypożycz pojazd.");
        System.out.println("2. Zwróć pojazd.");

        int choice = consoleReader.readInt("Wybierz opcje:");

        switch (choice) {
            case 1 -> rentVehicle();
            case 2 -> returnVehicle();
            default -> System.out.println("Niewlasciwy wybór.");
        }
    }

    private void rentVehicle() {
        System.out.println("Wybierz pojazd ktory chcesz wynajac:");
        showAllVehicles();
        Vehicle vehicle = readVehicle();
        Customer customer = readCustomer();
        LocalDate startDate = consoleReader.readDate("Data startu(dd-mm-rrrr):");
        LocalDate endDate = consoleReader.readEndDate("Data zakonczenia(dd-mm-rrrr):", startDate);
        rentalService.rentVehicle(vehicle, customer, startDate, endDate);
    }


    private void returnVehicle() {
        System.out.println("Wybierz pojazd ktory chcesz zwrocic:");
        showAllVehicles();
        Vehicle vehicle = readVehicle();
        Customer customer = readCustomer();
        rentalService.returnVehicle(vehicle, customer);
    }

    private void dataRaportOperations() {
        System.out.println("Wybierz opcje:");
        System.out.println("1. Lista wszystkich pojazdów.");
        System.out.println("2. Lista wszystkich klientów.");
        System.out.println("3. Lista wszystkich wypożyczeń.");
        System.out.println("4. Pokaż łączny przychód.");
        System.out.println("5. Pokaż aktualnie dostepne pojazdy.");
        System.out.println("6. Pokaż historie klienta.");
        System.out.println("7. Pokaż najaktywniejszych klientow.");


        int choice = consoleReader.readInt("Wybierz opcje:");
        switch (choice) {
            case 1 -> showAllVehicles();
            case 2 -> showAllCustomers();
            case 3 -> showAllRentals();
            case 4 -> showTotalIncome();
            case 5 -> showAvailableVehicles();
            case 6 -> showCustomerHistory();
            case 7 -> showMostActiveUsers();
            default -> System.out.println("Niewlasciwy wybor:");
        }
    }

    private void showMostActiveUsers() {
        System.out.println("Najaktywniejsi klienci, wedlug liczby wypozyczen:");
        for (Customer c : rentalService.getMostActiveUsersList()) {
            System.out.println("- " + c.getName() + " " + c.getSurname());
        }
    }

    private void showCustomerHistory() {
        List<Rental> history = rentalService.getCustomerHistory(readCustomer());
        System.out.println("Historia wypozyczen klienta: ");
        for (Rental rental : history) {
            System.out.println("- " + rental);
        }
    }

    private void showAvailableVehicles() {
        System.out.println("Lista aktualnie dostepnych pojazdow:");
        List<Vehicle> vehicles = rentalService.getAvailableVehicles();
        for (Vehicle v : vehicles) {
            System.out.println("- " + v.getBrand() + " " + v.getModel());
        }
    }

    private void showTotalIncome() {
        System.out.println("Łączny przychód to: " + rentalService.getTotalIncome());
    }

    private void showAllVehicles() {
        Set<Vehicle> vehicles = vehicleService.getVehicleList();
        int counter = 1;
        System.out.println("\n=== LISTA WSZYSTKICH POJAZDÓW ===");
        if (vehicles.isEmpty()) {
            System.out.println("Brak pojazdów w bazie danych.");
            return;
        }
        for (Vehicle vehicle : vehicles) {
            System.out.println(counter + ". " + vehicle.getBrand() + " " + vehicle.getModel() + ", " + vehicle.getLicensePlate() + ", cena za dzien: " + vehicle.getPricePerDay());
            counter++;
        }
    }

    private void showAllCustomers() {
        Set<Customer> customers = customerService.getCustomersList();
        int counter = 1;
        System.out.println("\n=== LISTA WSZYSTKICH KLIENTÓW ===");
        if (customers.isEmpty()) {
            System.out.println("Brak klientów w bazie danych.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(counter + ". " + customer + ", " + customer.getEmail());
            counter++;
        }
    }

    private void showAllRentals() {
        int counter = 1;
        System.out.println("\n=== LISTA WSZYSTKICH WYNAJMÓW ===");
        if (rentalService.getRentalList().isEmpty()) {
            System.out.println("Brak wynajmów w bazie danych.");
            return;
        }
        for (Rental rental : rentalService.getRentalList()) {
            Customer customer = rental.getCustomer();
            System.out.println(counter + ". " + customer + ", " + rental);
            counter++;
        }
    }

    private Customer readCustomer() {
        while (true) {
            try {
                return rentalService.getCustomer(consoleReader.readEmail("Podaj klienta(email):"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Podaj nowy adres email:");
            }
        }
    }

    private Vehicle readVehicle() {
        while (true) {
            try {
                return rentalService.getVehicle(consoleReader.readString("Podaj nr rejestracyjny:"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Podaj nowy numer rejestracyjny:");
            }
        }
    }
}

