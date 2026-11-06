package carRentalExercise.services;

import carRentalExercise.Customer;

import java.util.HashSet;
import java.util.Set;

public class CustomerService   {
    private  Set<Customer> customersList = new HashSet<>();

    public CustomerService() {
    }

    public void addCustomerToDatabase(Customer customer) {
        if (isCustomerExistInDatabase(customer)) {
            throw new IllegalArgumentException("Klient o takim adresie juz istnieje.");
        }
        customersList.add(customer);

    }

    public Set<Customer> getCustomersList() {
        return new HashSet<>(customersList);
    }

    private boolean isCustomerExistInDatabase(Customer customer) {
        for (Customer c : customersList) {
            if (c.getEmail().equals(customer.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public void setCustomersList(Set<Customer> customersList) {
        this.customersList = customersList;
    }
}
