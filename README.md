# Fuhrpark-Verwaltung

Eine Java-Desktop-Anwendung zur Verwaltung eines Fuhrparks mit Fahrtenbuch und Reparaturverwaltung.

## Funktionen

- **Fahrzeugverwaltung**
  - Hinzufügen neuer Fahrzeuge
  - Suche nach Fahrzeugen über Kennzeichen
  - Anzeige aller Fahrzeugdetails
  - Übersicht aller Fahrzeuge in Tabellenform

- **Fahrtenbuch**
  - Erfassung von Fahrten pro Fahrzeug
  - Dokumentation von Start- und Zielort
  - Kilometerzähler-Erfassung
  - Chronologische Übersicht aller Fahrten

- **Reparaturverwaltung**
  - Erfassung von Reparaturen und Wartungen
  - Dokumentation der Werkstattbesuche
  - Kostenerfassung
  - Reparaturhistorie pro Fahrzeug

## Installation

1. Sicherstellen, dass Java 17 oder höher installiert ist
2. Repository klonen:
   ```bash
   git clone https://github.com/g8rdier/fuhrparkmanagement.git
   cd fuhrpark
   ```
3. Anwendung bauen:
   ```bash
   mvn clean install
   ```
4. Anwendung starten:
   ```bash
   mvn exec:java -Dexec.mainClass="de.fuhrpark.App"
   ```

## Technische Details

- **Entwickelt mit:**
  - Java 17
  - Swing (GUI)
  - H2 Datenbank (Persistenz)
  - Maven (Build-Management)

- **Datenspeicherung:**
  - Lokale H2-Datenbank
  - Automatische Schemaerstellung beim ersten Start
  - Persistente Datenspeicherung zwischen Programmstarts

## Benutzung

### Neues Fahrzeug anlegen
1. Auf "Neues Fahrzeug" klicken
2. Fahrzeugdaten eingeben (Kennzeichen, Marke, Modell, etc.)
3. Mit "Speichern" bestätigen

### Fahrtenbuch führen
1. Fahrzeug in der Liste auswählen
2. "Fahrtenbuch öffnen" klicken
3. Neue Fahrt mit Start- und Zielort sowie Kilometern erfassen

### Reparatur erfassen
1. Fahrzeug auswählen
2. "Reparatur hinzufügen" klicken
3. Reparaturdetails und Kosten eingeben

## Datenbank

Die Anwendung nutzt eine H2-Datenbank mit folgenden Tabellen:
- `fahrzeuge`: Grunddaten aller Fahrzeuge
- `fahrtenbuch`: Dokumentation aller Fahrten
- `reparaturbuch`: Erfassung aller Reparaturen und Wartungen

Die Datenbank wird automatisch im Projektverzeichnis erstellt und bei jedem Start der Anwendung geprüft und ggf. initialisiert.

## Fehlerbehebung

### Bekannte Probleme und Lösungen
1. **Datenbank nicht erreichbar**
   - Prüfen, ob die Datei `fuhrparkdb.mv.db` im Projektverzeichnis existiert
   - Bei Beschädigung die Datei löschen - sie wird beim nächsten Start neu erstellt

2. **UI reagiert nicht**
   - Anwendung neu starten
   - Logs im Projektverzeichnis prüfen

## Support

Bei Fragen oder Problemen:
1. Issue im GitHub Repository erstellen
2. Detaillierte Fehlerbeschreibung bereitstellen
3. Log-Dateien anhängen

## Lizenz

[Ihre gewählte Lizenz]
