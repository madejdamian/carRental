package carrental.model;


import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$");
    private final String name;
    private final String surname;
    private final String email;


    public Customer(String name, String surname, String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Email nieprawidlowy");
        }
        this.email = email;
        this.name = name;
        this.surname = surname;
            }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }


    public static boolean validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) && Objects.equals(surname, customer.surname) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email);
    }
}
