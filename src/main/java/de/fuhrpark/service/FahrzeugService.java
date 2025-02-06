package de.fuhrpark.service;

import de.fuhrpark.model.Fahrzeug;
import java.util.List;

public interface FahrzeugService {
    void addFahrzeug(Fahrzeug fahrzeug);
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);
    void updateFahrzeug(Fahrzeug fahrzeug);
}