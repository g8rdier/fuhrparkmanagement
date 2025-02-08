package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDataStoreImpl implements DataStore {
    private static final String DB_URL = "jdbc:h2:./fuhrpark";
    private static final String USER = "sa";
    private static final String PASS = "";

    public DatabaseDataStoreImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "CREATE TABLE IF NOT EXISTS fahrzeuge " +
                        "(kennzeichen VARCHAR(255) PRIMARY KEY)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        String sql = "MERGE INTO fahrzeuge (kennzeichen) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fahrzeug.getKennzeichen());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save vehicle", e);
        }
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        String sql = "SELECT kennzeichen FROM fahrzeuge WHERE kennzeichen = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kennzeichen);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createFahrzeugFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get vehicle", e);
        }
        return null;
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        String sql = "SELECT kennzeichen FROM fahrzeuge";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fahrzeuge.add(createFahrzeugFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all vehicles", e);
        }
        return fahrzeuge;
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        String sql = "DELETE FROM fahrzeuge WHERE kennzeichen = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kennzeichen);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vehicle", e);
        }
    }

    @Override
    public void saveFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        // For BANF0-2, we'll implement a simple version that just logs
        System.out.println("DatabaseDataStoreImpl: Would save fahrt for " + kennzeichen);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        // For BANF0-2, return empty list
        return new ArrayList<FahrtenbuchEintrag>();
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        String kennzeichen = rs.getString("kennzeichen");
        return new PKW(kennzeichen);
    }
}