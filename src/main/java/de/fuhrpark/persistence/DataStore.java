package de.fuhrpark.persistence;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import java.util.List;

public interface DataStore {
    // Fahrzeug methods
    void saveFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    void deleteFahrzeug(String kennzeichen);
    
    // Fahrtenbuch methods
    void saveFahrt(String kennzeichen, FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrten(String kennzeichen);
}