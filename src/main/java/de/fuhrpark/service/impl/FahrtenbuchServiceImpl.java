package de.fuhrpark.service.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.service.base.FahrtenbuchService;
import java.util.List;

public class FahrtenbuchServiceImpl implements FahrtenbuchService {
    private final DataStore dataStore;

    public FahrtenbuchServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        dataStore.addFahrtenbuchEintrag(kennzeichen, eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        return dataStore.getFahrtenForFahrzeug(kennzeichen);
    }

    @Override
    public List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen) {
        return dataStore.getFahrtenForFahrzeug(kennzeichen);
    }
}