package de.fuhrpark.service.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.persistence.DataStore;
import java.util.*;

public class FahrtenbuchServiceImpl implements FahrtenbuchService {
    private final DataStore dataStore;
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuecher = new HashMap<>();

    public FahrtenbuchServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addEintrag(FahrtenbuchEintrag eintrag) {
        String kennzeichen = eintrag.getFahrzeugKennzeichen();
        fahrtenbuecher.computeIfAbsent(kennzeichen, _ -> new ArrayList<>()).add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen) {
        return new ArrayList<>(fahrtenbuecher.getOrDefault(kennzeichen, new ArrayList<>()));
    }
} 