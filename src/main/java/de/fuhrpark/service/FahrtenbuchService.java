package de.fuhrpark.service;

import de.fuhrpark.model.FahrtenbuchEintrag;
import java.util.List;

public interface FahrtenbuchService {
    void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen);
} 