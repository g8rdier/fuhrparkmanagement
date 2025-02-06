package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.persistence.DataStore;
import java.util.*;
import java.io.*;

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
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        for (Fahrzeug f : fahrzeuge) {
            if (f.getKennzeichen().equals(kennzeichen)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        delete(fahrzeug.getKennzeichen());
        addFahrzeug(fahrzeug);
    }

    @Override
    public void delete(String kennzeichen) {
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
        List<FahrtenbuchEintrag> result = new ArrayList<>();
        for (FahrtenbuchEintrag e : fahrtenbuchEintraege) {
            if (e.getFahrzeugKennzeichen().equals(kennzeichen)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag eintrag) {
        reparaturBuchEintraege.add(eintrag);
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
        List<ReparaturBuchEintrag> result = new ArrayList<>();
        for (ReparaturBuchEintrag e : reparaturBuchEintraege) {
            if (e.getFahrzeugKennzeichen().equals(kennzeichen)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public void save(String path, Object obj) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        }
    }

    @Override
    public Object load(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return ois.readObject();
        }
    }
} 