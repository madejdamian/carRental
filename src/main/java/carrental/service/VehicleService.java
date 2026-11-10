package carrental.service;

import carrental.model.Vehicle;
import carrental.persistence.AppState;
import carrental.persistence.DataManager;

import java.util.HashSet;
import java.util.Set;

public class VehicleService {
    private final Set<Vehicle> vehicleList;
    private final AppState appState;
    private final DataManager dataManager;

    public VehicleService(AppState appState, DataManager dataManager) {
        this.appState = appState;
        this.dataManager = dataManager;
        vehicleList = appState.getVehicles();
    }

    public void addVehicleToDatabase(Vehicle vehicle) {
        if (isVehicleExistInDatabase(vehicle)) {
            throw new IllegalArgumentException("Pojazd o takim nr rejestracyjnym juz istnieje.");
        }
        vehicleList.add(vehicle);
        dataManager.saveFile(appState);

    }

    public Set<Vehicle> getVehicleList() {
        return new HashSet<>(vehicleList);
    }

    private boolean isVehicleExistInDatabase(Vehicle vehicle) {
        for (Vehicle v : vehicleList) {
            if (v.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
                return true;
            }
        }
        return false;
    }
}
