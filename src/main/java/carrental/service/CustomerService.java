package carrental.service;

import carrental.model.Customer;
import carrental.persistence.AppState;
import carrental.persistence.DataManager;

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
        if (isCustomerExistInDatabase(customer.getEmail())) {
            throw new IllegalArgumentException("Klient o takim adresie juz istnieje.");
        }
        customersList.add(customer);
        dataManager.saveFile(appState);
    }

    public Set<Customer> getCustomersList() {
        return new HashSet<>(customersList);
    }

    public boolean isCustomerExistInDatabase(String customerEmail) {
        for (Customer c : customersList) {
            if (c.getEmail().equals(customerEmail)) {
                return true;
            }
        }
        return false;
    }
}
