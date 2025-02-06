package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.util.*;

public class DatabaseDataStoreImpl implements DataStore {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DatabaseDataStoreImpl(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        // Implementation
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        // Implementation
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        // Implementation
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        // Implementation
        return new ArrayList<>();
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return getFahrzeuge();
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return getFahrzeugByKennzeichen(kennzeichen);
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        // Implementation
        return null;
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        // Implementation
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege() {
        // Implementation
        return new ArrayList<>();
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        // Implementation
        return new ArrayList<>();
    }

    @Override
    public void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag) {
        // Implementation
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag eintrag) {
        addReparaturBuchEintrag(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturBuchEintraege() {
        // Implementation
        return new ArrayList<>();
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        // Implementation
        return new ArrayList<>();
    }

    @Override
    public void save(String path, Object obj) {
        // Not needed for database implementation
    }

    @Override
    public Object load(String path) {
        // Not needed for database implementation
        return null;
    }
}