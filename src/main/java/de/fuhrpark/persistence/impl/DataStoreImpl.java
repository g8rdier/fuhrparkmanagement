package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.persistence.DataStore;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataStoreImpl implements DataStore {
    private final List<Fahrzeug> fahrzeuge = new ArrayList<>();
    private final List<FahrtenbuchEintrag> fahrtenbuchEintraege = new ArrayList<>();
    private final List<ReparaturBuchEintrag> reparaturBuchEintraege = new ArrayList<>();

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.add(fahrzeug);
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        return new ArrayList<>(fahrzeuge);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeuge.stream()
                .filter(f -> f.getKennzeichen().equals(kennzeichen))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        deleteFahrzeug(fahrzeug.getKennzeichen());
        addFahrzeug(fahrzeug);
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.removeIf(f -> f.getKennzeichen().equals(kennzeichen));
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuchEintraege.add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege() {
        return new ArrayList<>(fahrtenbuchEintraege);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuchEintraege.stream()
                .filter(e -> e.getFahrzeugKennzeichen().equals(kennzeichen))
                .collect(Collectors.toList());
    }

    @Override
    public void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag) {
        reparaturBuchEintraege.add(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturBuchEintraege() {
        return new ArrayList<>(reparaturBuchEintraege);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        return reparaturBuchEintraege.stream()
                .filter(e -> e.getFahrzeugKennzeichen().equals(kennzeichen))
                .collect(Collectors.toList());
    }

    @Override
    public void save(String path, Object obj) {
        // Implementation for saving to file
    }

    @Override
    public Object load(String path) {
        // Implementation for loading from file
        return null;
    }
} 