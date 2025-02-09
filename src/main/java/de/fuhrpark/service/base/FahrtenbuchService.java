package de.fuhrpark.service.base;

import de.fuhrpark.model.FahrtenbuchEintrag;
import java.util.List;

public interface FahrtenbuchService {
    void addEintrag(String kennzeichen, FahrtenbuchEintrag eintrag);
    List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen);
    void deleteEintraegeForFahrzeug(String kennzeichen);
} 