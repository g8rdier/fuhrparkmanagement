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
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug getFahrzeug(String kennzeichen);

    // Fahrtenbuch operations
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege();
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen);

    // Reparaturbuch operations
    void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag);
    List<ReparaturBuchEintrag> getReparaturBuchEintraege();
    List<ReparaturBuchEintrag> getReparaturen(String kennzeichen);

    // Persistence operations
    void save(String path, Object obj) throws Exception;
    Object load(String path) throws Exception;
} 