package de.fuhrpark.persistence.repository;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.persistence.DataStore;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementierung des DataStore für Datenbankzugriffe
 */
public class DatabaseDataStoreImpl implements DataStore {
    private final Connection connection;

    public DatabaseDataStoreImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection darf nicht null sein");
        }
        this.connection = connection;
    }

    @Override
    public void speichereFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO fahrzeuge (kennzeichen, marke, modell, typ) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, fahrzeug.getKennzeichen());
            stmt.setString(2, fahrzeug.getMarke());
            stmt.setString(3, fahrzeug.getModell());
            stmt.setString(4, fahrzeug.getTyp().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Fahrzeugs: " + e.getMessage(), e);
        }
    }

    @Override
    public void aktualisiereFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE fahrzeuge SET marke = ?, modell = ?, typ = ? WHERE kennzeichen = ?"
        )) {
            stmt.setString(1, fahrzeug.getMarke());
            stmt.setString(2, fahrzeug.getModell());
            stmt.setString(3, fahrzeug.getTyp().toString());
            stmt.setString(4, fahrzeug.getKennzeichen());
            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalStateException("Fahrzeug nicht gefunden: " + fahrzeug.getKennzeichen());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Fahrzeugs: " + e.getMessage(), e);
        }
    }

    @Override
    public void loescheFahrzeug(String kennzeichen) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM fahrzeuge WHERE kennzeichen = ?"
        )) {
            stmt.setString(1, kennzeichen);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Fahrzeugs", e);
        }
    }

    @Override
    public Fahrzeug findeFahrzeugNachKennzeichen(String kennzeichen) {
        if (kennzeichen == null) {
            throw new IllegalArgumentException("Kennzeichen darf nicht null sein");
        }
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM fahrzeuge WHERE kennzeichen = ?"
        )) {
            stmt.setString(1, kennzeichen);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? createFahrzeugFromResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Laden des Fahrzeugs: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Fahrzeug> getAlleFahrzeuge() {
        List<Fahrzeug> fahrzeuge = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM fahrzeuge")) {
            while (rs.next()) {
                fahrzeuge.add(createFahrzeugFromResultSet(rs));
            }
            return fahrzeuge;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Laden aller Fahrzeuge: " + e.getMessage(), e);
        }
    }

    @Override
    public void addFahrtenbuchEintrag(String kennzeichen, FahrtenbuchEintrag eintrag) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO fahrtenbuch (kennzeichen, datum, start_ort, ziel_ort, kilometer, fahrer) VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, kennzeichen);
            stmt.setDate(2, Date.valueOf(eintrag.getDatum()));
            stmt.setString(3, eintrag.getStartOrt());
            stmt.setString(4, eintrag.getZielOrt());
            stmt.setDouble(5, eintrag.getKilometer());
            stmt.setString(6, eintrag.getFahrer());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Fahrtenbucheintrags: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FahrtenbuchEintrag> getFahrtenForFahrzeug(String kennzeichen) {
        List<FahrtenbuchEintrag> eintraege = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM fahrtenbuch WHERE kennzeichen = ? ORDER BY datum"
        )) {
            stmt.setString(1, kennzeichen);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    eintraege.add(new FahrtenbuchEintrag(
                        rs.getDate("datum").toLocalDate(),
                        rs.getString("start_ort"),
                        rs.getString("ziel_ort"),
                        rs.getDouble("kilometer"),
                        rs.getString("kennzeichen"),
                        rs.getString("fahrer")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Laden der Fahrtenbucheinträge: " + e.getMessage(), e);
        }
        return eintraege;
    }

    private Fahrzeug createFahrzeugFromResultSet(ResultSet rs) throws SQLException {
        String typ = rs.getString("typ");
        String kennzeichen = rs.getString("kennzeichen");
        String marke = rs.getString("marke");
        String modell = rs.getString("modell");

        return switch (FahrzeugTyp.valueOf(typ)) {
            case PKW -> new PKW(marke, modell, kennzeichen, 0.0);
            case LKW -> new LKW(marke, modell, kennzeichen, 0.0);
            default -> throw new IllegalStateException("Unbekannter Fahrzeugtyp: " + typ);
        };
    }
}