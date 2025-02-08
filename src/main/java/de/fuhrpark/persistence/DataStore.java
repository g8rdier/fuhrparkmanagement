package de.fuhrpark.persistence;

import de.fuhrpark.model.Fahrzeug;
import java.util.List;

public interface DataStore {
    void saveFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void deleteFahrzeug(String kennzeichen);
}