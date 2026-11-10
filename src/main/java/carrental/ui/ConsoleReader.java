package carrental.ui;

import carrental.model.Customer;
import carrental.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleReader {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDate readEndDate(String info, LocalDate startDate) {
        while (true) {
            LocalDate endDate = readDate(info);
            if (endDate.isBefore(startDate)) {
                System.out.println("Data konca wypożyczenia nie moze byc wczesniej niz startu. Podaj date ponownie.");
                continue;
            }
            return endDate;
        }
    }

    public LocalDate readDate(String info) {
        while (true) {
            try {
                LocalDate date = LocalDate.parse(readString(info), FORMATTER);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Nie możesz podac daty z przeszłości, podaj nowa date.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Data nieprawidlowa, wpisz date w formacie dd-MM-yyyy: ");
            }
        }
    }

    public BigDecimal readPrice(String info) {
        while (true) {
            try {
                BigDecimal converted = new BigDecimal(readString(info).replace(",", "."));
                if (!Vehicle.isPriceValid(converted)) {
                    System.out.println("Cena nie moze byc zerowa lub ujemna, wpisz jeszcze raz.");
                    continue;
                }
                return converted;
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowa liczba, wpisz jeszcze raz.");
            }
        }
    }

    public String readEmail(String info) {
        while (true) {
            String email = readString(info).toLowerCase();
            if (!Customer.validateEmail(email)) {
                System.out.println("Nieprawidlowy format email, wpisz jeszcze raz.");
                continue;
            }
            return email;
        }
    }

    public String readString(String info) {
        System.out.println(info);
        String input = SCANNER.nextLine();
        if (input.equalsIgnoreCase("exit")) {
            throw new CancelOperationException("Wyjscie");
        }
        return input;
    }

    public int readInt(String info) {
        return Integer.parseInt(readString(info));
    }
}
