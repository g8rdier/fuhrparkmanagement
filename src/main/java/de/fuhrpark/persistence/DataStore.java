package de.fuhrpark.persistence;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import java.util.List;

public interface DataStore {
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void saveFahrzeug(Fahrzeug fahrzeug);
    Fahrzeug getFahrzeug(String kennzeichen);
    List<Fahrzeug> getFahrzeuge();
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);
    
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege();
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen);
    
    void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag);
    List<ReparaturBuchEintrag> getReparaturBuchEintraege();
    List<ReparaturBuchEintrag> getReparaturen(String kennzeichen);
    
    void save(String filename, Object data);
    Object load(String filename);
}