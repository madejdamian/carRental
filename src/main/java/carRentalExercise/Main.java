package carRentalExercise;

import carRentalExercise.database.AppState;
import carRentalExercise.database.DataManager;
import carRentalExercise.services.CustomerService;
import carRentalExercise.services.RentalService;
import carRentalExercise.services.VehicleService;

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
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        ConsoleReader consoleReader = new ConsoleReader();
        AppState appState = dataManager.readFile();
        VehicleService vehicleService = new VehicleService(appState, dataManager);
        CustomerService customerService = new CustomerService(appState, dataManager);
        RentalService rentalService = new RentalService(vehicleService, customerService, appState, dataManager);
        RentalMenu menu = new RentalMenu(consoleReader, vehicleService, customerService, rentalService);
        menu.run();
    }

}

