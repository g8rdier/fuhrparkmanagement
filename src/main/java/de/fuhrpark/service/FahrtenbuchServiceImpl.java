package de.fuhrpark.service.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.service.FahrtenbuchService;
import java.util.List;

public class FahrtenbuchServiceImpl implements FahrtenbuchService {
    private final DataStore dataStore;

    public FahrtenbuchServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addFahrt(String kennzeichen, FahrtenbuchEintrag fahrt) {
        dataStore.saveFahrt(kennzeichen, fahrt);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        return dataStore.getFahrten(kennzeichen);
    }

    @Override
    public List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen) {
        return getFahrtenForFahrzeug(kennzeichen);
    }
} 