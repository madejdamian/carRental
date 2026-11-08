package carRentalExercise.services;

import carRentalExercise.Vehicle;
import carRentalExercise.database.AppState;
import carRentalExercise.database.DataManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    public void removeVehicleFromDatabase(Vehicle vehicle) {
        if (vehicleList.contains(vehicle)) {
            vehicleList.remove(vehicle);
        } else {
            System.out.println("Brak takiego pojazdu w bazie.");
        }
        dataManager.saveFile(appState);
        System.out.println("Usunieto pojazd z bazy danych");
    }

    public Vehicle getVehicleById(UUID id) {
        return vehicleList.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak takiego pojazdu"));

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
