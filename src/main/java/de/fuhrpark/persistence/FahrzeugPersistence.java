package de.fuhrpark.persistence;

import de.fuhrpark.model.base.Fahrzeug;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FahrzeugPersistence {
    private static final String FILE_PATH = "fahrzeuge.dat";

    public static void saveFahrzeuge(List<Fahrzeug> fahrzeuge) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(fahrzeuge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Fahrzeug> loadFahrzeuge() {
        if (!new File(FILE_PATH).exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            return (List<Fahrzeug>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 