package de.fuhrpark.persistence;

import de.fuhrpark.model.base.Fahrzeug;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FahrzeugPersistence {
    private static final String FILE_PATH = System.getProperty("user.home") 
        + System.getProperty("file.separator") 
        + "fuhrpark-data.ser";

    public void saveFahrzeuge(List<Fahrzeug> fahrzeuge) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<>(fahrzeuge));
        } catch (IOException e) {
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Fahrzeug> loadFahrzeuge() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<Fahrzeug>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
} 