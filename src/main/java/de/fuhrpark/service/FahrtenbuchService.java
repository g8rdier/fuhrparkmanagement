package de.fuhrpark.service.base;

import de.fuhrpark.model.FahrtenbuchEintrag;
import java.util.List;

public interface FahrtenbuchService {
    void addFahrt(String kennzeichen, FahrtenbuchEintrag fahrt);
    List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen);
}