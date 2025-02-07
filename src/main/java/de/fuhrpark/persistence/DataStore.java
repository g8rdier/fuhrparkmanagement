package de.fuhrpark.persistence;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import java.util.List;

public interface DataStore {
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getFahrzeuge();
    
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege();
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen);
    
    void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag);
    void addReparatur(String kennzeichen, ReparaturBuchEintrag reparatur);
    List<ReparaturBuchEintrag> getReparaturBuchEintraege();
    
    void save(String filename, Object data);
    Object load(String filename);
}