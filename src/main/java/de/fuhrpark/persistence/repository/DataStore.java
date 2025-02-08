package de.fuhrpark.persistence.repository;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import java.util.List;

/**
 * Interface für die Datenpersistenz
 */
public interface DataStore {
    // Fahrzeug methods
    void saveFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    
    // Fahrtenbuch methods
    void saveFahrt(String kennzeichen, FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrten(String kennzeichen);
}