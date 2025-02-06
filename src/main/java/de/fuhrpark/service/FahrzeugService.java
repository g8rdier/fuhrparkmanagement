package de.fuhrpark.service;

import de.fuhrpark.model.Fahrzeug;
import java.util.List;

public interface FahrzeugService {
    void addFahrzeug(Fahrzeug fahrzeug);
    void updateFahrzeug(Fahrzeug fahrzeug);
    void deleteFahrzeug(String kennzeichen);
    List<Fahrzeug> getAlleFahrzeuge();
    Fahrzeug getFahrzeugByKennzeichen(String kennzeichen);
}