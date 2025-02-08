package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.service.base.FahrzeugService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FahrzeugServiceImpl implements FahrzeugService {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }

    @Override
    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }
} 