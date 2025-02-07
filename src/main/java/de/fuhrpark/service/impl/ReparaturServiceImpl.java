package de.fuhrpark.service.impl;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.service.ReparaturService;
import java.util.List;

public class ReparaturServiceImpl implements ReparaturService {
    private final DataStore dataStore;

    public ReparaturServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addReparatur(String kennzeichen, ReparaturBuchEintrag reparatur) {
        dataStore.addReparatur(kennzeichen, reparatur);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturenForFahrzeug(String kennzeichen) {
        return dataStore.getReparaturen(kennzeichen);
    }
} 