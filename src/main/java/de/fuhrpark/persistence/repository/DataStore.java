package de.fuhrpark.persistence.repository;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.base.Fahrzeug;
import java.util.List;

/**
 * Interface für die Datenpersistenz
 */
public interface DataStore {
    void addFahrtenbuchEintrag(String kennzeichen, FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen);
    
    // Fahrzeug methods
    void speichereFahrzeug(Fahrzeug fahrzeug);
    void loescheFahrzeug(String kennzeichen);
    Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void aktualisiereFahrzeug(Fahrzeug fahrzeug);
}