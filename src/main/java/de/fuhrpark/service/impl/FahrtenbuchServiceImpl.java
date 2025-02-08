package de.fuhrpark.service.impl;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.service.base.FahrtenbuchService;
import java.util.List;

public class FahrtenbuchServiceImpl implements FahrtenbuchService {
    private final DataStore dataStore;

    public FahrtenbuchServiceImpl(DataStore dataStore) {
        if (dataStore == null) {
            throw new IllegalArgumentException("DataStore darf nicht null sein");
        }
        this.dataStore = dataStore;
    }

    @Override
    public void addFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        if (kennzeichen == null || eintrag == null) {
            throw new IllegalArgumentException("Kennzeichen und Eintrag dürfen nicht null sein");
        }
        dataStore.addFahrtenbuchEintrag(kennzeichen, eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return dataStore.getFahrtenForFahrzeug(kennzeichen);
    }

    @Override
    public List<FahrtenbuchEintrag> getEintraegeForFahrzeug(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        return dataStore.getFahrtenForFahrzeug(kennzeichen);
    }
}