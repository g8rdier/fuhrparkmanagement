package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;
import java.util.List;

/**
 * Interface für die Fahrzeug-Verwaltung.
 * Definiert grundlegende Operationen für Fahrzeuge.
 */
public interface FahrzeugService {
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen);
    void speichereFahrzeug(Fahrzeug fahrzeug);
    void loescheFahrzeug(String kennzeichen);
    void aktualisiereFahrzeug(Fahrzeug fahrzeug);
}