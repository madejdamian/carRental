# 🚗 Car Rental – system wypożyczalni pojazdów

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-build-blue?logo=apachemaven)
![Status](https://img.shields.io/badge/status-w%20trakcie%20rozwoju-yellow)

Konsolowa aplikacja w Javie do zarządzania małą wypożyczalnią pojazdów. Pozwala rejestrować pojazdy i klientów, obsługiwać wypożyczenia i zwroty oraz przeglądać dane. Stan systemu jest zapisywany do pliku i wczytywany przy kolejnym uruchomieniu.

> Projekt edukacyjny powstały w ramach nauki programowania obiektowego w Javie. Nie używa zewnętrznych bibliotek ani bazy danych – tylko Java SE i konsola.

---

## 📑 Spis treści

- [Funkcjonalności](#-funkcjonalności)
- [Technologie](#-technologie)
- [Wymagania](#-wymagania)
- [Uruchomienie](#-uruchomienie)
- [Instrukcja obsługi](#-instrukcja-obsługi)
- [Architektura](#-architektura)
- [Struktura projektu](#-struktura-projektu)
- [Model domenowy](#-model-domenowy)
- [Obsługa błędów](#️-obsługa-błędów)
- [Trwałość danych](#-trwałość-danych)
- [Status i plan rozwoju](#-status-i-plan-rozwoju)
- [Autor](#-autor)

---

## ✨ Funkcjonalności

| Obszar | Opis |
|---|---|
| **Pojazdy** | Rejestracja motocykli, samochodów osobowych i ciężarowych. Każdy pojazd ma unikalny identyfikator (`UUID`), a numer rejestracyjny nie może się powtarzać. |
| **Klienci** | Rejestracja klientów z walidacją formatu adresu e-mail (wyrażenie regularne). Adres e-mail jest unikalny w systemie. |
| **Wypożyczenia** | Wypożyczenie pojazdu na wybrany zakres dat z kontrolą dostępności – zajętego pojazdu nie można wypożyczyć. |
| **Zwroty** | Zwrot pojazdu z automatycznym zapisem daty zwrotu. |
| **Przegląd danych** | Lista wszystkich pojazdów, klientów i wypożyczeń. |
| **Zapis / odczyt** | Automatyczny zapis stanu po każdej zmianie i odczyt przy starcie aplikacji. |
| **Anulowanie operacji** | Wpisanie `exit` w polu tekstowym lub w polu daty przerywa bieżącą operację i wraca do menu głównego. |

---

## 🛠 Technologie

- **Java 17** – programowanie obiektowe, wyrażenia `switch` ze strzałką (`->`), Stream API
- **Maven** – budowanie projektu
- **Serializacja Javy** (`ObjectOutputStream` / `ObjectInputStream`) – zapis danych
- **`java.time`** – obsługa dat (`LocalDate`, `DateTimeFormatter`)
- **`java.util.regex`** – walidacja adresu e-mail

---

## 📋 Wymagania

- JDK 17 lub nowszy
- Maven 3.6+ (opcjonalnie – projekt można też uruchomić bezpośrednio z IntelliJ IDEA)

---

## 🚀 Uruchomienie

### Z terminala

```bash
git clone <adres-repozytorium>
cd carRental
mvn compile
java -cp target/classes carRentalExercise.Main
```

### Z IntelliJ IDEA

1. Otwórz katalog projektu (`File → Open`).
2. Poczekaj, aż projekt Maven się zaimportuje.
3. Uruchom klasę `carRentalExercise.Main`.

> Plik `database.dat` jest tworzony w katalogu roboczym, z którego uruchomiono aplikację.

---

## 📖 Instrukcja obsługi

Po uruchomieniu wyświetla się menu główne:

```
===MENU===
1. Zarejestruj pojazd.
2. Zarejestruj klienta.
3. Obsługa wypożyczeń.
4. Przegląd danych i generowanie raportów.
5. Wyjscie.
```

### Przykładowy scenariusz

1. **Dodanie pojazdu** – wybierz `1`, potem typ pojazdu (motocykl / osobowy / ciężarowy), i podaj markę, model, numer rejestracyjny oraz cenę za dobę.
2. **Dodanie klienta** – wybierz `2` i podaj imię, nazwisko oraz poprawny adres e-mail (np. `jan.kowalski@example.com`).
3. **Wypożyczenie** – wybierz `3 → 1`, podaj numer rejestracyjny pojazdu, e-mail klienta oraz daty w formacie `dd-MM-rrrr` (np. `15-11-2026`).
4. **Zwrot** – wybierz `3 → 2` i wskaż pojazd oraz klienta.
5. **Podgląd danych** – wybierz `4`, aby wyświetlić listę pojazdów, klientów lub wypożyczeń.

Jeśli numer rejestracyjny, e-mail klienta lub data są niepoprawne, aplikacja prosi o ponowne wpisanie. W każdej chwili możesz wpisać `exit`, żeby wrócić do menu.

---

## 🏗 Architektura

Aplikacja ma budowę warstwową:

```
┌───────────────────────────────┐
│ Main (warstwa prezentacji)    │  menu konsolowe, odczyt danych wejściowych
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│ services (logika biznesowa)   │  VehicleService, CustomerService, RentalService
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│ model domenowy                │  Vehicle, Car, Motorcycle, Truck, Customer, Rental
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│ database (trwałość danych)    │  AppState, DataManager → database.dat
└───────────────────────────────┘
```

**Zastosowane zasady:**

- **Dziedziczenie i polimorfizm** – abstrakcyjna klasa `Vehicle` z podklasami `Car`, `Motorcycle`, `Truck`.
- **Hermetyzacja** – pola `private final`, a gettery zwracają kopie kolekcji (*defensive copy*), żeby chronić stan obiektów.
- **Podział odpowiedzialności** – każdy serwis zarządza jednym typem zasobu.
- **Wstrzykiwanie zależności przez konstruktor** – `RentalService` dostaje `VehicleService` i `CustomerService`.
- **Własny wyjątek** – `CancelOperationException` przerywa operację na żądanie użytkownika.

---

## 📂 Struktura projektu

```
carRental/
├── pom.xml
├── database.dat                         # plik z zapisanym stanem aplikacji
└── src/main/java/carRentalExercise/
    ├── Main.java                        # punkt wejścia, menu konsolowe
    ├── Customer.java                    # klient wypożyczalni
    ├── Rental.java                      # wypożyczenie
    ├── Vehicle.java                     # abstrakcyjna klasa pojazdu
    ├── DriverLicenseCategory.java       # kategorie prawa jazdy (A, B, C)
    ├── CancelOperationException.java    # wyjątek anulowania operacji
    ├── vehicles/
    │   ├── Car.java                     # samochód osobowy (liczba miejsc)
    │   ├── Motorcycle.java              # motocykl
    │   └── Truck.java                   # samochód ciężarowy (ładowność)
    ├── services/
    │   ├── VehicleService.java          # zarządzanie pojazdami
    │   ├── CustomerService.java         # zarządzanie klientami
    │   └── RentalService.java           # wypożyczenia i zwroty
    └── database/
        ├── AppState.java                # stan aplikacji (klienci, pojazdy, wypożyczenia)
        └── DataManager.java             # zapis i odczyt pliku
```

---

## 🧩 Model domenowy

| Klasa | Najważniejsze pola | Uwagi |
|---|---|---|
| `Vehicle` *(abstrakcyjna)* | `id: UUID`, `brand`, `model`, `licensePlate`, `pricePerDay`, `rentalList` | Metoda `checkAvailability(start, end)` sprawdza, czy terminy się nie nakładają. |
| `Car` | `seatsNumber`, `driverLicenseCategory` | Wymagana kategoria **B**. |
| `Motorcycle` | `driverLicenseCategory` | Wymagana kategoria **A**. |
| `Truck` | `capacity`, `driverLicenseCategory` | Wymagana kategoria **C**. |
| `Customer` | `name`, `surname`, `email`, `driverLicenseCategories` | Walidacja e-maila w konstruktorze; `equals`/`hashCode` oparte na imieniu, nazwisku i e-mailu. |
| `Rental` | `startDate`, `endDate`, `customer`, `vehicleId`, `returnDate` | `returnDate == null` oznacza aktywne wypożyczenie. |

---

## ⚠️ Obsługa błędów

| Sytuacja | Reakcja aplikacji |
|---|---|
| Niepoprawny wybór w menu lub niepoprawna liczba | Komunikat i ponowne wyświetlenie menu. |
| Niepoprawny format e-maila | `IllegalArgumentException` – klient nie zostaje dodany. |
| Powtórzony numer rejestracyjny lub e-mail | `IllegalArgumentException` z komunikatem. |
| Nieistniejący pojazd lub klient przy wypożyczeniu | Prośba o ponowne wpisanie danych. |
| Niepoprawny format daty | Prośba o ponowne wpisanie daty w formacie `dd-MM-rrrr`. |
| Pojazd niedostępny w podanym terminie | Komunikat „Pojazd niedostepny”. |
| Wpisanie `exit` | `CancelOperationException` – powrót do menu głównego. |

---

## 💾 Trwałość danych

Cały stan aplikacji (klienci, pojazdy, wypożyczenia) jest trzymany w obiekcie `AppState` i serializowany do pliku **`database.dat`**:

- **przy starcie** – `DataManager.readFile()` wczytuje plik; jeśli go nie ma, tworzona jest pusta baza,
- **po każdej zmianie** – `DataManager.saveFile()` zapisuje bieżący stan.

Żeby zacząć od pustej bazy, wystarczy usunąć plik `database.dat`.

---

## 🗺 Status i plan rozwoju

Projekt jest w trakcie rozwoju.

**Zrealizowane:**

- [x] Rejestracja pojazdów różnych typów
- [x] Rejestracja klientów z walidacją e-maila
- [x] Wypożyczenia z kontrolą dostępności terminu
- [x] Zwrot pojazdu
- [x] Lista pojazdów, klientów i wypożyczeń
- [x] Zapis i odczyt stanu z pliku
- [x] Anulowanie operacji poleceniem `exit`

**Planowane:**

- [ ] Obliczanie kosztu wypożyczenia z dokładnością finansową (`BigDecimal`)
- [ ] Lista aktualnie dostępnych pojazdów
- [ ] Historia wypożyczeń wybranego klienta
- [ ] Raport łącznych przychodów
- [ ] Ranking najbardziej aktywnych klientów
- [ ] Sprawdzanie kategorii prawa jazdy klienta przy wypożyczeniu
- [ ] Walidacja zakresu dat (data zakończenia nie wcześniejsza niż data rozpoczęcia)
- [ ] Testy jednostkowe (JUnit 5)

---

## 👤 Autor

**Damian Madej** – [@madejdamian](https://github.com/madejdamian)

Projekt powstał w celach edukacyjnych.
