package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.model.enums.*;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseDataStoreImpl implements DataStore {
    private final String dbUrl = "jdbc:h2:./fuhrparkdb";
    
    public DatabaseDataStoreImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            createTables(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Fahrzeuge table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fahrzeuge (
                    kennzeichen VARCHAR(20) PRIMARY KEY,
                    marke VARCHAR(50),
                    modell VARCHAR(50),
                    typ VARCHAR(20),
                    baujahr INTEGER,
                    wert DOUBLE
                )
            """);

            // Fahrtenbuch table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fahrtenbuch (
                    id IDENTITY PRIMARY KEY,
                    datum DATE,
                    start_ort VARCHAR(100),
                    ziel_ort VARCHAR(100),
                    kilometer DOUBLE,
                    fahrzeug_kennzeichen VARCHAR(20),
                    fahrer VARCHAR(100),
                    FOREIGN KEY (fahrzeug_kennzeichen) REFERENCES fahrzeuge(kennzeichen)
                )
            """);

            // Reparaturen table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reparaturen (
                    id IDENTITY PRIMARY KEY,
                    datum DATE,
                    typ VARCHAR(20),
                    beschreibung VARCHAR(200),
                    kosten DOUBLE,
                    fahrzeug_kennzeichen VARCHAR(20),
                    werkstatt VARCHAR(100),
                    FOREIGN KEY (fahrzeug_kennzeichen) REFERENCES fahrzeuge(kennzeichen)
                )
            """);
        }
    }

    @Override
    public void save(String key, Object data) {
        // Generic save not needed for database implementation
    }

    @Override
    public Optional<Object> load(String key) {
        // Generic load not needed for database implementation
        return Optional.empty();
    }

    @Override
    public void delete(String key) {
        // Generic delete not needed for database implementation
    }

    @Override
    public void saveFahrzeug(Fahrzeug fahrzeug) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(
                 "MERGE INTO fahrzeuge (kennzeichen, marke, modell, typ, baujahr, wert) VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            stmt.setString(1, fahrzeug.getKennzeichen());
            stmt.setString(2, fahrzeug.getMarke());
            stmt.setString(3, fahrzeug.getModell());
            stmt.setString(4, fahrzeug.getTyp().toString());
            stmt.setInt(5, fahrzeug.getBaujahr());
            stmt.setDouble(6, fahrzeug.getAktuellerWert());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM fahrzeuge WHERE kennzeichen = ?")) {
            stmt.setString(1, kennzeichen);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM fahrzeuge")) {
            
            while (rs.next()) {
                fahrzeuge.add(new Fahrzeug(
                    rs.getString("kennzeichen"),
                    rs.getString("marke"),
                    rs.getString("modell"),
                    FahrzeugTyp.valueOf(rs.getString("typ")),
                    rs.getInt("baujahr"),
                    rs.getDouble("wert")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fahrzeuge;
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO fahrtenbuch (datum, start_ort, ziel_ort, kilometer, fahrzeug_kennzeichen, fahrer) VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            stmt.setDate(1, toSqlDate(eintrag.getDatum()));
            stmt.setString(2, eintrag.getStartOrt());
            stmt.setString(3, eintrag.getZielOrt());
            stmt.setDouble(4, eintrag.getKilometer());
            stmt.setString(5, eintrag.getFahrzeugKennzeichen());
            stmt.setString(6, eintrag.getFahrer());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        List<FahrtenbuchEintrag> eintraege = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM fahrtenbuch WHERE fahrzeug_kennzeichen = ?")) {
            stmt.setString(1, kennzeichen);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FahrtenbuchEintrag eintrag = new FahrtenbuchEintrag(
                    rs.getDate("datum").toLocalDate(),
                    rs.getString("start_ort"),
                    rs.getString("ziel_ort"),
                    rs.getDouble("kilometer"),
                    rs.getString("fahrzeug_kennzeichen")
                );
                eintrag.setFahrer(rs.getString("fahrer"));
                eintraege.add(eintrag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eintraege;
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO reparaturen (datum, typ, beschreibung, kosten, fahrzeug_kennzeichen, werkstatt) VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            stmt.setDate(1, toSqlDate(reparatur.getDatum()));
            stmt.setString(2, reparatur.getTyp().toString());
            stmt.setString(3, reparatur.getBeschreibung());
            stmt.setDouble(4, reparatur.getKosten());
            stmt.setString(5, reparatur.getFahrzeugKennzeichen());
            stmt.setString(6, reparatur.getWerkstatt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        List<ReparaturBuchEintrag> reparaturen = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reparaturen WHERE fahrzeug_kennzeichen = ?")) {
            stmt.setString(1, kennzeichen);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reparaturen.add(new ReparaturBuchEintrag(
                    rs.getDate("datum").toLocalDate(),
                    ReparaturTyp.valueOf(rs.getString("typ")),
                    rs.getString("beschreibung"),
                    rs.getDouble("kosten"),
                    rs.getString("fahrzeug_kennzeichen"),
                    rs.getString("werkstatt")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparaturen;
    }

    @Override
    public void addFahrzeug(Fahrzeug fahrzeug) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO fahrzeuge (kennzeichen, marke, modell, typ, baujahr, wert) VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            stmt.setString(1, fahrzeug.getKennzeichen());
            stmt.setString(2, fahrzeug.getMarke());
            stmt.setString(3, fahrzeug.getModell());
            stmt.setString(4, fahrzeug.getTyp().toString());
            stmt.setInt(5, fahrzeug.getBaujahr());
            stmt.setDouble(6, fahrzeug.getAktuellerWert());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }
} 