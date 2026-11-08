package carRentalExercise;


import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String surname;
    private final String email;
    private final Set<DriverLicenseCategory> driverLicenseCategories;

    public Customer(String name, String surname, String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Email nieprawidlowy");
        }
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.driverLicenseCategories = new HashSet<>();
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

    public List<DriverLicenseCategory> getDriverLicenseCategories() {
        return new ArrayList<>(driverLicenseCategories);
    }

    public void addDriverLicenseCategory(DriverLicenseCategory category) {
        driverLicenseCategories.add(category);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
