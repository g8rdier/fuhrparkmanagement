package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DataStoreImpl implements DataStore {
    private static final String DB_URL = "jdbc:sqlite:fuhrpark.db";
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();

    public DataStoreImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS fahrzeuge (" +
                        "kennzeichen TEXT PRIMARY KEY," +
                        "typ TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge.values());
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.remove(kennzeichen);
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        String kennzeichen = rs.getString("kennzeichen");
        String typ = rs.getString("typ");
        if ("PKW".equals(typ)) {
            return new PKW(kennzeichen);
        }
        throw new IllegalStateException("Unknown vehicle type: " + typ);
    }
}