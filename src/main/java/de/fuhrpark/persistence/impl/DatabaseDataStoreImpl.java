package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;
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
        String sql = "INSERT INTO fahrzeuge (kennzeichen, marke, modell, typ, baujahr, kilometerstand) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fahrzeug.getKennzeichen());
            stmt.setString(2, fahrzeug.getMarke());
            stmt.setString(3, fahrzeug.getModell());
            stmt.setString(4, fahrzeug.getTyp().toString());
            stmt.setInt(5, fahrzeug.getBaujahr());
            stmt.setDouble(6, fahrzeug.getKilometerstand());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle", e);
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
        String sql = "SELECT * FROM fahrzeuge";
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fahrzeuge.add(createFahrzeugFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicles", e);
        }
        return fahrzeuge;
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
        String sql = "SELECT * FROM reparaturbuch WHERE kennzeichen = ? ORDER BY datum DESC";
        List<ReparaturBuchEintrag> reparaturen = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kennzeichen);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ReparaturBuchEintrag eintrag = new ReparaturBuchEintrag(
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("beschreibung"),
                    rs.getDouble("kosten"),
                    rs.getString("werkstatt")
                );
                reparaturen.add(eintrag);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching repairs", e);
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
    public void saveReparatur(String kennzeichen, ReparaturBuchEintrag eintrag) {
        String sql = "INSERT INTO reparaturbuch (kennzeichen, datum, beschreibung, kosten, werkstatt) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kennzeichen);
            stmt.setDate(2, Date.valueOf(eintrag.getDatum()));
            stmt.setString(3, eintrag.getBeschreibung());
            stmt.setDouble(4, eintrag.getKosten());
            stmt.setString(5, eintrag.getWerkstatt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving repair", e);
        }
    }

    @Override
    public List<Fahrzeug> getFahrzeuge() {
        return getAlleFahrzeuge();
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