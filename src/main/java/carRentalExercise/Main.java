package carRentalExercise;

import carRentalExercise.database.AppState;
import carRentalExercise.database.DataManager;
import carRentalExercise.services.CustomerService;
import carRentalExercise.services.RentalService;
import carRentalExercise.services.VehicleService;
import carRentalExercise.vehicles.Car;
import carRentalExercise.vehicles.Motorcycle;
import carRentalExercise.vehicles.Truck;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;

/*
 * ZADANIE: System wypożyczalni samochodów
 *
 * Napisz konsolową aplikację do zarządzania małą wypożyczalnią aut.
 *
 * System powinien umożliwiać:
 *
 * 1. Rejestrowanie pojazdów w systemie.
 *    Wypożyczalnia posiada różne typy pojazdów (np. osobowe, dostawcze).
 *    Każdy pojazd ma swój unikalny identyfikator, markę, model oraz cenę za dobę.
 *    Pojazd może być dostępny lub aktualnie wypożyczony.
 *
 * 2. Rejestrowanie klientów.
 *    Klient posiada dane osobowe oraz kontaktowe.
 *    Adres e-mail musi mieć poprawny format — niepoprawny powinien być odrzucony.
 *
 * 3. Obsługę wypożyczeń.
 *    Klient może wypożyczyć dostępny pojazd na określony zakres dat.
 *    Nie można wypożyczyć pojazdu, który jest już zajęty.
 *    Zwrot pojazdu oznacza, że staje się on ponownie dostępny.
 *    Koszt wypożyczenia zależy od liczby dni i ceny pojazdu za dobę —
 *    wynik musi być dokładny finansowo.
 *
 * 4. Przeglądanie danych i generowanie raportów.
 *    Możliwe powinno być m.in. sprawdzenie dostępnych pojazdów,
 *    historii wypożyczeń konkretnego klienta, łącznych przychodów
 *    oraz listy najbardziej aktywnych klientów.
 *
 * 5. Zapis i odczyt danych z pliku.
 *    Przy starcie aplikacja wczytuje poprzedni stan systemu (jeśli plik istnieje).
 *    Zapisuje stan systemu przy kazej zmianie w bazie danych.
 *
 * Wymagania ogólne:
 * - Obsługuj błędne dane wejściowe i sytuacje wyjątkowe (np. brak pojazdu,
 *   niedostępny pojazd, niepoprawne daty).
 * - Struktura klas, nazwy, podział na pakiety — Twoja decyzja.
 * - Bez zewnętrznych bibliotek, bez bazy danych, tylko Java i konsola.
 */
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static VehicleService vehicleService;
    private static CustomerService customerService;
    private static RentalService rentalService;
    private static final DataManager dataManager = new DataManager();
    private static AppState APP_STATE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static void main(String[] args) {
        loadDatabase();
        createServices();
        boolean programIsRunning = true;

        while (programIsRunning) {
            showMenu();
            try {
                int choice = readInt("Wybierz opcje:");

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

    private static void showMenu() {
        System.out.println("\n===MENU===");
        System.out.println("1. Zarejestruj pojazd.");
        System.out.println("2. Zarejestruj klienta.");
        System.out.println("3. Obsługa wypożyczeń.");
        System.out.println("4. Przegląd danych i generowanie raportów.");
        System.out.println("5. Wyjscie.");
    }

    private static void registerVehicle() {
        System.out.println("===Podaj dane pojazdu===");
        System.out.println("Rodzaj: ");
        System.out.println("1. Motocykl.");
        System.out.println("2. Samochod osobowy.");
        System.out.println("3. Samochod ciezarowy.");

        int choice = readInt("Wybierz opcje:");
        switch (choice) {
            case 1 -> registerMotorcycle();
            case 2 -> registerCar();
            case 3 -> registerTruck();
            default -> System.out.println("Niewłasciwy wybór");
        }
    }

    private static void registerMotorcycle() {
        System.out.println("===Podaj dane motocykla ===");
        String brand = readString("Marka:");
        String model = readString("Model:");
        String licensePlate = readString("Nr. rejestracyjny:");
        BigDecimal price = readPrice("Cena za dzien:");
        Vehicle motorcycle = new Motorcycle(brand, model, licensePlate, price, DriverLicenseCategory.CATEGORY_A);
        vehicleService.addVehicleToDatabase(motorcycle);
        System.out.println("Dodano pojazd do bazy.");
    }


    private static void registerCar() {
        System.out.println("===Podaj dane samochodu ===");
        String brand = readString("Marka:");
        String model = readString("Model:");
        String licensePlate = readString("Nr. rejestracyjny:");
        BigDecimal price = readPrice("Cena za dzien:");
        int seatsNumber = readInt("Ilosc miejsc:");
        Vehicle car = new Car(brand, model, licensePlate, price, seatsNumber, DriverLicenseCategory.CATEGORY_B);
        vehicleService.addVehicleToDatabase(car);
        System.out.println("Dodano pojazd do bazy.");
    }

    private static void registerTruck() {
        System.out.println("===Podaj dane ciezarowki ===");
        String brand = readString("Marka:");
        String model = readString("Model:");
        String licensePlate = readString("Nr. rejestracyjny:");
        BigDecimal price = readPrice("Cena za dzien:");
        int capacity = readInt("Ładownosc:");
        Vehicle car = new Truck(brand, model, licensePlate, price, capacity, DriverLicenseCategory.CATEGORY_C);
        vehicleService.addVehicleToDatabase(car);
        System.out.println("Dodano pojazd do bazy.");

    }

    private static void registerCustomer() {
        System.out.println("=== Podaj dane klienta ===");
        String name = readString("Imie:");
        String surname = readString("Nazwisko:");
        String email = readString("Email:");
        Customer customer = new Customer(name, surname, email);
        customerService.addCustomerToDatabase(customer);
        System.out.println("Dodano klienta do bazy.");

    }

    private static void rentalOperations() {
        System.out.println("Wybierz opcje:");
        System.out.println("1. Wypożycz pojazd.");
        System.out.println("2. Zwróć pojazd.");

        int choice = readInt("Wybierz opcje:");

        switch (choice) {
            case 1 -> rentVehicle();
            case 2 -> returnVehicle();
            default -> System.out.println("Niewlasciwy wybór.");
        }
    }

    private static void rentVehicle() {
        System.out.println("Wybierz pojazd ktory chcesz wynajac:");
        showAllVehicles();
        Vehicle vehicle = readVehicle();
        Customer customer = readCustomer();
        LocalDate startDate = readDate("Data startu(dd-mm-rrrr):");
        LocalDate endDate = readDate("Data zakonczenia(dd-mm-rrrr):");
        rentalService.rentVehicle(vehicle, customer, startDate, endDate);
    }


    private static void returnVehicle() {
        System.out.println("Wybierz pojazd ktory chcesz zwrocic:");
        showAllVehicles();
        Vehicle vehicle = readVehicle();
        Customer customer = readCustomer();
        rentalService.returnVehicle(vehicle, customer);
    }

    private static void dataRaportOperations() {
        System.out.println("Wybierz opcje:");
        System.out.println("1. Lista wszystkich pojazdów.");
        System.out.println("2. Lista wszystkich klientów.");
        System.out.println("3. Lista wszystkich wypożyczeń.");


        int choice = readInt("Wybierz opcje:");
        switch (choice) {
            case 1 -> showAllVehicles();
            case 2 -> showAllCustomers();
            case 3 -> showAllRentals();
            default -> System.out.println("Niewlasciwy wybor:");
        }
    }

    private static void showAllVehicles() {
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

    private static void showAllCustomers() {
        Set<Customer> customers = customerService.getCustomersList();
        int counter = 1;
        System.out.println("\n=== LISTA WSZYSTKICH KLIENTÓW ===");
        if (customers.isEmpty()) {
            System.out.println("Brak klientów w bazie danych.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(counter + ". " + customer.getName() + " " + customer.getSurname());
            counter++;
        }
    }

    private static void showAllRentals() {
        int counter = 1;
        System.out.println("\n=== LISTA WSZYSTKICH WYNAJMÓW ===");
        if (rentalService.getRentalList().isEmpty()) {
            System.out.println("Brak wynajmów w bazie danych.");
            return;
        }
        for (Rental rental : rentalService.getRentalList()) {
            Vehicle vehicle = vehicleService.getVehicleById(rental.getVehicleId());
            Customer customer = rental.getCustomer();
            System.out.println(counter + ". Auto: " + vehicle.getBrand() + " " + vehicle.getModel() + ", Klient: " + customer.getName() + " "
                    + customer.getSurname() + ", wynajem od: " + rental.getStartDate().format(DateTimeFormatter.ISO_DATE)
                    + " do " + rental.getEndDate().format(DateTimeFormatter.ISO_DATE));
            counter++;
        }
    }

    private static void loadDatabase() {
        APP_STATE = dataManager.readFile();
    }

    private static void createServices() {
        vehicleService = new VehicleService(APP_STATE, dataManager);
        customerService = new CustomerService(APP_STATE, dataManager);
        rentalService = new RentalService(vehicleService, customerService, APP_STATE, dataManager);
    }

    private static String readString(String info) {
        System.out.println(info);
        String input = SCANNER.nextLine();
        if (input.equalsIgnoreCase("exit")) {
            throw new CancelOperationException("Wyjscie");
        }
        return input;
    }

    private static int readInt(String info) {
        return Integer.parseInt(readString(info));
    }

    private static BigDecimal readPrice(String info) {
        while (true) {
            try {
                BigDecimal converted = new BigDecimal(readString(info).replace(",", "."));
                if (!Vehicle.isPriceValid(converted)){
                    System.out.println("Cena nie moze byc zerowa lub ujemna, wpisz jeszcze raz.");
                    continue;
                }
                return converted;
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowa liczba, wpisz jeszcze raz.");
            }
        }
    }

    private static LocalDate readDate(String info) {
        while (true) {
            String date = readString(info);
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data nieprowidlowa, wpisz date w formacie dd-MM-yyyy: ");
            }
        }
    }

    private static Vehicle readVehicle() {
        while (true) {
            try {
                return rentalService.getVehicle(readString("Podaj nr rejestracyjny:"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Podaj nowy adres rejestracyjny:");
            }
        }
    }

    private static Customer readCustomer() {
        while (true) {
            try {
                return rentalService.getCustomer(readString("Podaj klienta(email):"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Podaj nowy adres email:");
            }
        }
    }
}

