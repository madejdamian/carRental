package carRentalExercise.services;

import carRentalExercise.Customer;
import carRentalExercise.database.AppState;
import carRentalExercise.database.DataManager;

import java.util.HashSet;
import java.util.Set;

public class CustomerService   {
    private final Set<Customer> customersList;
    private final AppState appState;
    private final DataManager dataManager;

    public CustomerService(AppState appState, DataManager dataManager) {
        this.appState = appState;
        this.dataManager = dataManager;
        customersList = appState.getCustomers();
    }

    public void addCustomerToDatabase(Customer customer) {
        if (isCustomerExistInDatabase(customer)) {
            throw new IllegalArgumentException("Klient o takim adresie juz istnieje.");
        }
        customersList.add(customer);
        dataManager.saveFile(appState);
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
}
