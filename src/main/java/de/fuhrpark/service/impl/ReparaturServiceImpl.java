package de.fuhrpark.service.impl;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.persistence.DataStore;
import java.util.*;

public class ReparaturServiceImpl implements ReparaturService {
    private final DataStore dataStore;
    private final Map<String, List<ReparaturBuchEintrag>> reparaturen = new HashMap<>();

    public ReparaturServiceImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag eintrag) {
        String kennzeichen = eintrag.getFahrzeugKennzeichen();
        reparaturen.computeIfAbsent(kennzeichen, _ -> new ArrayList<>()).add(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturenForFahrzeug(String kennzeichen) {
        return new ArrayList<>(reparaturen.getOrDefault(kennzeichen, new ArrayList<>()));
    }
} 