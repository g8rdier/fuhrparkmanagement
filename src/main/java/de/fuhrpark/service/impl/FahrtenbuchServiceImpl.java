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
    public void saveFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        dataStore.saveFahrt(kennzeichen, eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        return dataStore.getFahrten(kennzeichen);
    }
}