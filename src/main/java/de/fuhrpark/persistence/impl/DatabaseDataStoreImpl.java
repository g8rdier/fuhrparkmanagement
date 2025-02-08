package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.DatabaseConfig;
import de.fuhrpark.model.enums.FahrzeugTyp;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseDataStoreImpl implements DataStore {
    private final Map<String, Fahrzeug> fahrzeuge = new HashMap<>();
    private final Map<String, List<FahrtenbuchEintrag>> fahrtenbuch = new HashMap<>();
    private final Map<String, List<ReparaturBuchEintrag>> reparaturen = new HashMap<>();

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        System.out.println("Attempting to save vehicle: " + fahrzeug.getKennzeichen());
        
        // First try to find if the vehicle exists
        String checkSql = "SELECT COUNT(*) FROM fahrzeuge WHERE kennzeichen = ?";
        String insertSql = "INSERT INTO fahrzeuge (kennzeichen, marke, modell, typ, baujahr, kilometerstand) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE fahrzeuge SET marke = ?, modell = ?, typ = ?, baujahr = ?, kilometerstand = ? WHERE kennzeichen = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Check if vehicle exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, fahrzeug.getKennzeichen());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                boolean exists = rs.getInt(1) > 0;
                
                // Prepare the appropriate statement
                PreparedStatement stmt;
                if (exists) {
                    stmt = conn.prepareStatement(updateSql);
                    stmt.setString(1, fahrzeug.getMarke());
                    stmt.setString(2, fahrzeug.getModell());
                    stmt.setString(3, fahrzeug.getTyp().toString());
                    stmt.setInt(4, fahrzeug.getBaujahr());
                    stmt.setDouble(5, fahrzeug.getKilometerstand());
                    stmt.setString(6, fahrzeug.getKennzeichen());
                } else {
                    stmt = conn.prepareStatement(insertSql);
                    stmt.setString(1, fahrzeug.getKennzeichen());
                    stmt.setString(2, fahrzeug.getMarke());
                    stmt.setString(3, fahrzeug.getModell());
                    stmt.setString(4, fahrzeug.getTyp().toString());
                    stmt.setInt(5, fahrzeug.getBaujahr());
                    stmt.setDouble(6, fahrzeug.getKilometerstand());
                }
                
                // Execute the statement
                int result = stmt.executeUpdate();
                System.out.println("Database update successful. Rows affected: " + result);
                
                // Update local cache
                fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public Fahrzeug getFahrzeug(String kennzeichen) {
        String sql = "SELECT * FROM fahrzeuge WHERE kennzeichen = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kennzeichen);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return createFahrzeugFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicle", e);
        }
        return null;
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> result = new ArrayList<>();
        String sql = "SELECT * FROM fahrzeuge";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Fahrzeug fahrzeug = new Fahrzeug(
                    rs.getString("kennzeichen"),
                    rs.getString("marke"),
                    rs.getString("modell"),
                    FahrzeugTyp.valueOf(rs.getString("typ")),
                    rs.getInt("baujahr"),
                    rs.getDouble("kilometerstand")
                );
                result.add(fahrzeug);
                // Also update the cache
                fahrzeuge.put(fahrzeug.getKennzeichen(), fahrzeug);
            }
            System.out.println("Database query returned " + result.size() + " vehicles");
            
        } catch (SQLException e) {
            System.err.println("Error fetching vehicles: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching vehicles", e);
        }
        return result;
    }

    @Override
    public Fahrzeug getFahrzeugByKennzeichen(String kennzeichen) {
        return fahrzeuge.get(kennzeichen);
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuch.computeIfAbsent(eintrag.getKennzeichen(), k -> new ArrayList<>())
                  .add(eintrag);
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege() {
        List<FahrtenbuchEintrag> alleEintraege = new ArrayList<>();
        fahrtenbuch.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        return fahrtenbuch.getOrDefault(kennzeichen, new ArrayList<>());
    }

    @Override
    public void addReparaturBuchEintrag(ReparaturBuchEintrag eintrag) {
        reparaturen.computeIfAbsent(eintrag.getKennzeichen(), k -> new ArrayList<>())
                  .add(eintrag);
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturBuchEintraege() {
        List<ReparaturBuchEintrag> alleEintraege = new ArrayList<>();
        reparaturen.values().forEach(alleEintraege::addAll);
        return alleEintraege;
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        List<ReparaturBuchEintrag> reparaturen = new ArrayList<>();
        String sql = """
            SELECT id, fahrzeug_kennzeichen, datum, beschreibung, kosten, werkstatt 
            FROM reparaturen 
            WHERE fahrzeug_kennzeichen = ?
            ORDER BY datum DESC
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kennzeichen);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReparaturBuchEintrag reparatur = ReparaturBuchEintrag.builder()
                        .id(rs.getLong("id"))
                        .kennzeichen(rs.getString("fahrzeug_kennzeichen"))
                        .datum(rs.getDate("datum").toLocalDate())
                        .beschreibung(rs.getString("beschreibung"))
                        .kosten(rs.getDouble("kosten"))
                        .werkstatt(rs.getString("werkstatt"))
                        .build();
                    reparaturen.add(reparatur);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching repairs for vehicle: " + kennzeichen, e);
        }
        
        return reparaturen;
    }

    @Override
    public void save(String filename, Object data) {
        // Implementation needed
    }

    @Override
    public Object load(String filename) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Delete repairs first
            String deleteSql = "DELETE FROM reparaturbuch WHERE kennzeichen = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, kennzeichen);
                deleteStmt.executeUpdate();
            }
            
            // Then delete the vehicle
            String sql = "DELETE FROM fahrzeuge WHERE kennzeichen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kennzeichen);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle", e);
        }
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen() {
        List<ReparaturBuchEintrag> alleReparaturen = new ArrayList<>();
        reparaturen.values().forEach(alleReparaturen::addAll);
        return alleReparaturen;
    }

    @Override
    public void saveReparatur(String kennzeichen, ReparaturBuchEintrag reparatur) {
        String sql = """
            INSERT INTO reparaturen (fahrzeug_kennzeichen, datum, beschreibung, kosten, werkstatt)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kennzeichen);
            stmt.setDate(2, Date.valueOf(reparatur.getDatum()));
            stmt.setString(3, reparatur.getBeschreibung());
            stmt.setDouble(4, reparatur.getKosten());
            stmt.setString(5, reparatur.getWerkstatt());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving repair for vehicle: " + kennzeichen, e);
        }
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        return getAlleFahrzeuge();  // Always get fresh data from database
    }

    @Override
    public void saveFahrt(String kennzeichen, FahrtenbuchEintrag fahrt) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO fahrtenbuch (kennzeichen, datum, start_ort, ziel_ort, kilometer, fahrer_typ, fahrer_name, grund) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, kennzeichen);
            stmt.setDate(2, Date.valueOf(fahrt.getDatum()));
            stmt.setString(3, fahrt.getStartOrt());
            stmt.setString(4, fahrt.getZielOrt());
            stmt.setDouble(5, fahrt.getKilometer());
            stmt.setString(6, fahrt.getFahrerTyp());
            stmt.setString(7, fahrt.getFahrerName());
            stmt.setString(8, fahrt.getGrund());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving fahrt: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrten(String kennzeichen) {
        List<FahrtenbuchEintrag> fahrten = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM fahrtenbuch WHERE kennzeichen = ? ORDER BY datum DESC")) {
            
            stmt.setString(1, kennzeichen);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FahrtenbuchEintrag fahrt = new FahrtenbuchEintrag(
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("start_ort"),
                    rs.getString("ziel_ort"),
                    rs.getDouble("kilometer"),
                    rs.getString("kennzeichen"),
                    rs.getString("fahrer_typ"),
                    rs.getString("fahrer_name"),
                    rs.getString("grund")
                );
                fahrten.add(fahrt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching fahrten: " + e.getMessage(), e);
        }
        return fahrten;
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        return new Fahrzeug(
            rs.getString("kennzeichen"),
            rs.getString("marke"),
            rs.getString("modell"),
            FahrzeugTyp.valueOf(rs.getString("typ")),
            rs.getInt("baujahr"),
            rs.getDouble("kilometerstand")
        );
    }
}