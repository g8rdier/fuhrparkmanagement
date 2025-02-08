package de.fuhrpark.service;

import de.fuhrpark.model.Fahrzeug;
import java.util.List;

/**
 * Interface für die Fahrzeug-Verwaltung.
 * Definiert grundlegende Operationen für Fahrzeuge.
 */
public interface FahrzeugService {
    void speichereFahrzeug(Fahrzeug fahrzeug);
    void aktualisiereFahrzeug(Fahrzeug fahrzeug);
    void loescheFahrzeug(String kennzeichen);
    Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen);
    List<Fahrzeug> findeAlleFahrzeuge();
}