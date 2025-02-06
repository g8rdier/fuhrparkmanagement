package de.fuhrpark.persistence;

import java.util.Optional;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import java.util.List;

public interface DataStore {
    // Fahrzeug operations
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    List<Fahrzeug> getFahrzeuge();
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug getFahrzeug(String kennzeichen);
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);

    // Fahrtenbuch operations
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege();
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen);

    // Reparaturbuch operations
    void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag);
    void addReparatur(ReparaturBuchEintrag eintrag);
    List<ReparaturBuchEintrag> getReparaturBuchEintraege();
    List<ReparaturBuchEintrag> getReparaturen(String kennzeichen);

    // Persistence operations
    void save(String path, Object obj);
    Object load(String path);
} 