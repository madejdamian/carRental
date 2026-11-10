package carrental.persistence;

import carrental.model.Customer;
import carrental.model.Rental;
import carrental.model.Vehicle;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class DataManager {
    private static final String FILE_PATH = "database.dat";

    public AppState readFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            System.out.println("=== Wczytano baze danych ===");
            return (AppState) ois.readObject();
        }catch (FileNotFoundException e){
            Set<Vehicle> vehicles = new HashSet<>();
            Set<Customer> customers = new HashSet<>();
            Set<Rental> rentals = new HashSet<>();
            System.out.println("Brak bazy danych, utworzono nową baze.");
            return new AppState(customers,rentals,vehicles);
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile(AppState appState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(appState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
