package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDataStoreImpl implements DataStore {
    private static final String DB_URL = "jdbc:sqlite:fuhrpark.db";

    public DatabaseDataStoreImpl() {
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
        String sql = "INSERT OR REPLACE INTO fahrzeuge (kennzeichen, typ) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fahrzeug.getKennzeichen());
            pstmt.setString(2, fahrzeug.getTyp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        String sql = "SELECT kennzeichen, typ FROM fahrzeuge WHERE kennzeichen = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kennzeichen);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return createFahrzeugFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        String sql = "SELECT kennzeichen, typ FROM fahrzeuge";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fahrzeuge.add(createFahrzeugFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fahrzeuge;
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        String sql = "DELETE FROM fahrzeuge WHERE kennzeichen = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kennzeichen);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        String kennzeichen = rs.getString("kennzeichen");
        return new PKW(kennzeichen); // For now, we only support PKW
    }
}