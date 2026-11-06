package carRentalExercise.services;

import carRentalExercise.Vehicle;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VehicleService {
    private Set<Vehicle> vehicleList = new HashSet<>();

    public VehicleService() {
    }

    public void addVehicleToDatabase(Vehicle vehicle) {
        if (isVehicleExistInDatabase(vehicle)) {
            throw new IllegalArgumentException("Pojazd o takim nr rejestracyjnym juz istnieje.");
        }
        vehicleList.add(vehicle);

    }

    public void removeVehicleFromDatabase(Vehicle vehicle) {
        if (vehicleList.contains(vehicle)) {
            vehicleList.remove(vehicle);
        } else {
            System.out.println("Brak takiego pojazdu w bazie.");
        }
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

    public void setVehicleList(Set<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
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
