package de.fuhrpark.persistence;

import java.util.Optional;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
import java.util.List;

public interface DataStore {
    void save(String key, Object data);
    Optional<Object> load(String key);
    void delete(String key);

    // Fahrzeug methods
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();

    // Fahrtenbuch methods
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen);

    // Reparatur methods
    void addReparatur(ReparaturBuchEintrag reparatur);
    List<ReparaturBuchEintrag> getReparaturen(String kennzeichen);
} 