# 🚗 Car Rental – system wypożyczalni pojazdów

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-build-blue?logo=apachemaven)
![Status](https://img.shields.io/badge/status-ukończony-brightgreen)

Konsolowa aplikacja w Javie do zarządzania małą wypożyczalnią pojazdów. Pozwala rejestrować pojazdy i klientów, obsługiwać wypożyczenia i zwroty, liczyć koszt najmu oraz generować raporty. Stan systemu jest zapisywany do pliku po każdej zmianie i wczytywany przy kolejnym uruchomieniu.

> Projekt edukacyjny powstały w ramach nauki programowania obiektowego w Javie. Nie używa zewnętrznych bibliotek ani bazy danych – tylko Java SE i konsola.

---

## 📑 Spis treści

- [Funkcjonalności](#-funkcjonalności)
- [Technologie](#-technologie)
- [Wymagania](#-wymagania)
- [Uruchomienie](#-uruchomienie)
- [Instrukcja obsługi](#-instrukcja-obsługi)
- [Reguły biznesowe](#-reguły-biznesowe)
- [Architektura](#-architektura)
- [Struktura projektu](#-struktura-projektu)
- [Model domenowy](#-model-domenowy)
- [Obsługa błędów](#️-obsługa-błędów)
- [Trwałość danych](#-trwałość-danych)

---

## ✨ Funkcjonalności

| Obszar | Opis |
|---|---|
| **Pojazdy** | Rejestracja motocykli, samochodów osobowych i ciężarowych. Numer rejestracyjny nie może się powtarzać, a cena za dobę musi być dodatnia. |
| **Klienci** | Rejestracja klientów z walidacją formatu adresu e-mail. E-mail jest unikalny i zapisywany małymi literami. |
| **Wypożyczenia** | Wypożyczenie pojazdu na wybrany zakres dat z kontrolą dostępności – terminy wypożyczeń jednego pojazdu nie mogą się nakładać. |
| **Koszt najmu** | Liczony dokładnie finansowo (`BigDecimal`) jako liczba dni × cena za dobę. |
| **Zwroty** | Zwrot rozpoczętego wypożyczenia z automatycznym zapisem daty zwrotu. |
| **Raporty** | Łączny przychód, aktualnie dostępne pojazdy, historia wypożyczeń klienta, 3 najaktywniejszych klientów oraz listy pojazdów, klientów i wypożyczeń. |
| **Zapis / odczyt** | Automatyczny zapis stanu po każdej zmianie i odczyt przy starcie aplikacji. |
| **Anulowanie operacji** | Wpisanie `exit` w dowolnym polu przerywa bieżącą operację i wraca do menu głównego. |

---

## 🛠 Technologie

- **Java 17** – programowanie obiektowe, wyrażenia `switch` ze strzałką (`->`), Stream API (`groupingBy`, `reduce`)
- **Maven** – budowanie projektu
- **Serializacja Javy** (`ObjectOutputStream` / `ObjectInputStream`) – zapis danych
- **`java.time`** – obsługa dat (`LocalDate`, `DateTimeFormatter`, `ChronoUnit`)
- **`java.math.BigDecimal`** – precyzyjne obliczenia finansowe
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
java -cp target/classes carrental.Main
```

### Z IntelliJ IDEA

1. Otwórz katalog projektu (`File → Open`).
2. Poczekaj, aż projekt Maven się zaimportuje.
3. Uruchom klasę `carrental.Main`.

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

Menu raportów (opcja `4`):

```
1. Lista wszystkich pojazdów.
2. Lista wszystkich klientów.
3. Lista wszystkich wypożyczeń.
4. Pokaż łączny przychód.
5. Pokaż aktualnie dostepne pojazdy.
6. Pokaż historie klienta.
7. Pokaż najaktywniejszych klientow.
```

### Przykładowy scenariusz

1. **Dodanie pojazdu** – wybierz `1`, potem typ pojazdu (motocykl / osobowy / ciężarowy), i podaj markę, model, numer rejestracyjny oraz cenę za dobę (np. `150,50` lub `150.50`).
2. **Dodanie klienta** – wybierz `2` i podaj imię, nazwisko oraz adres e-mail (np. `jan.kowalski@example.com`).
3. **Wypożyczenie** – wybierz `3 → 1`, podaj numer rejestracyjny pojazdu, e-mail klienta oraz daty w formacie `dd-MM-rrrr` (np. `15-11-2026`).
4. **Zwrot** – wybierz `3 → 2` i wskaż pojazd oraz klienta.
5. **Raporty** – wybierz `4` i jedną z opcji raportu.

Jeśli numer rejestracyjny, e-mail, cena lub data są niepoprawne, aplikacja prosi o ponowne wpisanie tylko tej wartości. W każdej chwili możesz wpisać `exit`, żeby wrócić do menu.

---

## 📐 Reguły biznesowe

- **Dni najmu** – dzień startu i dzień zakończenia są wliczone: wypożyczenie `10-11` → `12-11` to 3 doby, a ta sama data startu i końca to 1 doba.
- **Daty** – nie można wypożyczyć pojazdu z datą z przeszłości, a data zakończenia nie może być wcześniejsza niż data startu.
- **Dostępność** – wypożyczenie zajmuje pojazd od daty startu do daty zwrotu (jeśli pojazd zwrócono) albo do planowanej daty zakończenia. Dzień zwrotu jest dniem zajętym.
- **Zwrot** – można zwrócić tylko wypożyczenie, które już się rozpoczęło i nie zostało zwrócone. Przy kilku pasujących wybierane jest najwcześniejsze.
- **Wcześniejszy zwrot** – klient płaci za cały zarezerwowany okres; koszt jest liczony w chwili wypożyczenia.
- **Przychód** – liczą się wyłącznie zwrócone wypożyczenia.
- **Najaktywniejsi klienci** – ranking według liczby wypożyczeń, pokazywane są 3 pierwsze miejsca.

---

## 🏗 Architektura

Aplikacja ma budowę warstwową:

```
┌────────────────────────────────┐
│ Main                           │  składa obiekty i uruchamia menu
└───────────────┬────────────────┘
                │
┌───────────────▼────────────────┐
│ ui (warstwa prezentacji)       │  RentalMenu, ConsoleReader
└───────────────┬────────────────┘
                │
┌───────────────▼────────────────┐
│ service (logika biznesowa)     │  VehicleService, CustomerService, RentalService
└───────────────┬────────────────┘
                │
┌───────────────▼────────────────┐
│ model (model domenowy)         │  Vehicle, Car, Motorcycle, Truck, Customer, Rental
└───────────────┬────────────────┘
                │
┌───────────────▼────────────────┐
│ persistence (trwałość danych)  │  AppState, DataManager → database.dat
└────────────────────────────────┘
```

**Zastosowane zasady:**

- **Podział odpowiedzialności** – `ConsoleReader` odpowiada za *jak* wczytać poprawną daną z klawiatury, `RentalMenu` za *czego* potrzebuje wypożyczalnia, serwisy za logikę i zapis, a `Main` tylko składa obiekty.
- **Wstrzykiwanie zależności przez konstruktor** – serwisy dostają `AppState` i `DataManager`, a `RentalMenu` serwisy i `ConsoleReader`.
- **Reguły w modelu domenowym** – walidacja ceny (`Vehicle.isPriceValid`) i e-maila (`Customer.validateEmail`) mieszka w klasach domenowych i jest używana zarówno przez konstruktory, jak i przez konsolę.
- **Dziedziczenie i polimorfizm** – abstrakcyjna klasa `Vehicle` z podklasami `Car`, `Motorcycle`, `Truck`.
- **Hermetyzacja** – pola `private final`, a gettery kolekcji zwracają kopie (*defensive copy*).
- **Własny wyjątek** – `CancelOperationException` przerywa operację na żądanie użytkownika.

---

## 📂 Struktura projektu

```
carRental/
├── pom.xml
├── database.dat                          # plik z zapisanym stanem aplikacji
└── src/main/java/carrental/
    ├── Main.java                         # punkt wejścia, składanie obiektów
    ├── model/
    │   ├── Vehicle.java                  # abstrakcyjna klasa pojazdu, kontrola dostępności
    │   ├── Car.java                      # samochód osobowy (liczba miejsc)
    │   ├── Motorcycle.java               # motocykl
    │   ├── Truck.java                    # samochód ciężarowy (ładowność)
    │   ├── Customer.java                 # klient, walidacja e-maila
    │   └── Rental.java                   # wypożyczenie, koszt najmu
    ├── service/
    │   ├── VehicleService.java           # zarządzanie pojazdami
    │   ├── CustomerService.java          # zarządzanie klientami
    │   └── RentalService.java            # wypożyczenia, zwroty i raporty
    ├── persistence/
    │   ├── AppState.java                 # stan aplikacji (klienci, pojazdy, wypożyczenia)
    │   └── DataManager.java              # zapis i odczyt pliku
    └── ui/
        ├── RentalMenu.java               # menu konsolowe i scenariusze użytkownika
        ├── ConsoleReader.java            # wczytywanie i walidacja danych z konsoli
        └── CancelOperationException.java # wyjątek anulowania operacji
```

---

## 🧩 Model domenowy

| Klasa | Najważniejsze pola | Uwagi |
|---|---|---|
| `Vehicle` *(abstrakcyjna)* | `id: UUID`, `brand`, `model`, `licensePlate`, `pricePerDay: BigDecimal`, `rentalList` | `checkAvailability(start, end)` sprawdza, czy nowy termin nie nakłada się na żadne wypożyczenie. |
| `Car` | `seatsNumber` | Samochód osobowy. |
| `Motorcycle` | – | Motocykl. |
| `Truck` | `capacity` | Samochód ciężarowy. |
| `Customer` | `name`, `surname`, `email` | Walidacja e-maila w konstruktorze. |
| `Rental` | `startDate`, `endDate`, `customer`, `vehicle`, `returnDate`, `totalPrice: BigDecimal` | `returnDate == null` oznacza aktywne wypożyczenie; `getOccupiedUntil()` zwraca koniec okresu zajętości pojazdu. |

---

## ⚠️ Obsługa błędów

| Sytuacja | Reakcja aplikacji |
|---|---|
| Niepoprawny wybór w menu lub niepoprawna liczba | Komunikat i ponowne wyświetlenie menu. |
| Niepoprawna, zerowa lub ujemna cena | Prośba o ponowne wpisanie ceny. |
| Niepoprawny format e-maila lub e-mail już zarejestrowany | Prośba o ponowne wpisanie e-maila. |
| Powtórzony numer rejestracyjny | Komunikat i powrót do menu. |
| Nieistniejący pojazd lub klient przy wypożyczeniu | Prośba o ponowne wpisanie danych. |
| Niepoprawny format daty, data z przeszłości lub koniec przed startem | Prośba o ponowne wpisanie daty. |
| Pojazd niedostępny w podanym terminie | Komunikat „Pojazd niedostepny”. |
| Zwrot wypożyczenia, które się nie rozpoczęło lub zostało już zwrócone | Komunikat „Brak takiego najmu w bazie.” |
| Wpisanie `exit` | `CancelOperationException` – powrót do menu głównego. |

---

## 💾 Trwałość danych

Cały stan aplikacji (klienci, pojazdy, wypożyczenia) jest trzymany w obiekcie `AppState` i serializowany do pliku **`database.dat`**:

- **przy starcie** – `DataManager.readFile()` wczytuje plik; jeśli go nie ma, tworzona jest pusta baza,
- **po każdej zmianie** – serwis, który zmienił dane, wywołuje `DataManager.saveFile()`.

Klasy zapisywane do pliku mają zadeklarowany `serialVersionUID`. Zmiana typu pola, nazwy klasy albo pakietu sprawia jednak, że stary plik przestaje się wczytywać – wtedy wystarczy usunąć `database.dat`, żeby zacząć od pustej bazy.

---

Projekt powstał w celach edukacyjnych.
