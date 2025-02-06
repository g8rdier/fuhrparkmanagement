package de.fuhrpark.persistence.impl;

import de.fuhrpark.model.*;
import de.fuhrpark.model.enums.*;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseDataStoreImpl implements DataStore {
    private static final String CONFIG_FILE = "database.properties";
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load database configuration", e);
        }
        DB_URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/fuhrpark");
        DB_USER = props.getProperty("db.user", "root");
        DB_PASSWORD = props.getProperty("db.password", "");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public DatabaseDataStoreImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
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
    public void addFahrzeug(Fahrzeug fahrzeug) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO fahrzeuge (kennzeichen, marke, modell, typ) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fahrzeug.getKennzeichen());
                stmt.setString(2, fahrzeug.getMarke());
                stmt.setString(3, fahrzeug.getModell());
                stmt.setString(4, fahrzeug.getTyp().toString());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Fahrzeug", e);
        }
    }

    @Override
    public void deleteFahrzeug(String kennzeichen) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM fahrzeuge WHERE kennzeichen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kennzeichen);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Fahrzeug", e);
        }
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM fahrzeuge";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    fahrzeuge.add(createFahrzeugFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Fahrzeuge", e);
        }
        return fahrzeuge;
    }

    @Override
    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO fahrtenbuch (datum, start_ort, ziel_ort, kilometer, fahrzeug_kennzeichen, fahrer) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, toSqlDate(eintrag.getDatum()));
                stmt.setString(2, eintrag.getStartOrt());
                stmt.setString(3, eintrag.getZielOrt());
                stmt.setDouble(4, eintrag.getKilometer());
                stmt.setString(5, eintrag.getFahrzeugKennzeichen());
                stmt.setString(6, eintrag.getFahrer());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenbuchEintraege(String kennzeichen) {
        List<FahrtenbuchEintrag> eintraege = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM fahrtenbuch WHERE fahrzeug_kennzeichen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eintraege;
    }

    @Override
    public void addReparatur(ReparaturBuchEintrag reparatur) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO reparaturen (datum, typ, beschreibung, kosten, fahrzeug_kennzeichen, werkstatt) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, toSqlDate(reparatur.getDatum()));
                stmt.setString(2, reparatur.getTyp().toString());
                stmt.setString(3, reparatur.getBeschreibung());
                stmt.setDouble(4, reparatur.getKosten());
                stmt.setString(5, reparatur.getFahrzeugKennzeichen());
                stmt.setString(6, reparatur.getWerkstatt());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReparaturBuchEintrag> getReparaturen(String kennzeichen) {
        List<ReparaturBuchEintrag> reparaturen = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM reparaturen WHERE fahrzeug_kennzeichen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparaturen;
    }

    @Override
    public void updateFahrzeug(Fahrzeug fahrzeug) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE fahrzeuge SET marke = ?, modell = ?, typ = ? WHERE kennzeichen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fahrzeug.getMarke());
                stmt.setString(2, fahrzeug.getModell());
                stmt.setString(3, fahrzeug.getTyp().toString());
                stmt.setString(4, fahrzeug.getKennzeichen());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Fahrzeug", e);
        }
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        return new Fahrzeug(
            rs.getString("kennzeichen"),
            rs.getString("marke"),
            rs.getString("modell"),
            FahrzeugTyp.valueOf(rs.getString("typ"))
        );
    }
} 