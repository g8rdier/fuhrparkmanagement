// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.persistence.DataStore;
import java.util.List;

/**
 * Verwaltet die Fahrzeuge des Fuhrparks.
 */
public class FuhrparkManager {
    private final DataStore dataStore;

    public FuhrparkManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        dataStore.saveFahrzeug(fahrzeug);
    }

    public Fahrzeug getFahrzeug(String kennzeichen) {
        return dataStore.getFahrzeug(kennzeichen);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return dataStore.getAlleFahrzeuge();
    }

    public void deleteFahrzeug(String kennzeichen) {
        dataStore.deleteFahrzeug(kennzeichen);
    }
}
