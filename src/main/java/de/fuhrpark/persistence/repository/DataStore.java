package de.fuhrpark.persistence.repository;

import de.fuhrpark.model.base.Fahrzeug;
import java.util.List;

/**
 * Interface für die Datenpersistenz
 */
public interface DataStore {
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void save();
    void load();
}