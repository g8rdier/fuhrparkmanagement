# Fuhrpark-Verwaltung

## Übersicht
Diese Java-Anwendung ermöglicht die grundlegende Verwaltung eines Fuhrparks mit verschiedenen Fahrzeugtypen (PKW, LKW). Sie wurde gemäß den BANF-Anforderungen entwickelt und implementiert.

## Technische Details

### Architektur
Die Anwendung ist in folgende Hauptkomponenten gegliedert:
- **Model**: Fahrzeug-Basisklasse mit PKW/LKW-Implementierungen (Vererbungshierarchie)
- **Persistence**: Datenpersistenz mittels Serialisierung
- **UI**: Swing-basierte Benutzeroberfläche

### Technische Anforderungen (BANF)
- **BANF0**: Vollständig in Java entwickelt
- **BANF1**: Übersichtliche Swing-GUI
- **BANF2**: Vererbungshierarchie (Fahrzeug → PKW/LKW)
- **BANF3**: SOLID-Prinzipien beachtet
- **BANF4**: Persistenzschicht implementiert

## Benutzerhandbuch

### Hauptfunktionen
1. **Fahrzeug hinzufügen**
   - Klicken Sie auf "Neu"
   - Wählen Sie den Fahrzeugtyp (PKW/LKW)
   - Füllen Sie die Pflichtfelder aus:
     - Kennzeichen (Format: XXX-XX1234)
     - Marke
     - Modell
     - Wert (in €)

2. **Fahrzeug bearbeiten**
   - Wählen Sie ein Fahrzeug aus der Liste
   - Klicken Sie auf "Bearbeiten"
   - Ändern Sie die gewünschten Werte

3. **Fahrzeug löschen**
   - Wählen Sie ein Fahrzeug aus der Liste
   - Klicken Sie auf "Löschen"

### Datenpersistenz
- Alle Änderungen werden automatisch gespeichert
- Die Daten werden in einer Datei im Benutzerverzeichnis gespeichert
- Beim Neustart der Anwendung werden alle Daten automatisch geladen

## Installation und Start

### Systemvoraussetzungen
- Java Runtime Environment (JRE) 17 oder höher
- Mindestens 256MB RAM
- 50MB freier Festplattenspeicher

### Installation
1. Entpacken Sie die ZIP-Datei
2. Starten Sie die Anwendung:
   ```bash
   java -jar fuhrpark.jar
   ```

## Technische Dokumentation

### Projektstruktur
```
src/main/java/de/fuhrpark/
├── model/
│   ├── base/
│   │   └── Fahrzeug.java (Basisklasse)
│   └── impl/
│       ├── PKW.java
│       └── LKW.java
├── persistence/
│   └── FahrzeugPersistence.java
├── service/
│   ├── base/
│   │   ├── FahrzeugFactory.java
│   │   └── FahrzeugService.java
│   └── impl/
│       ├── FahrzeugFactoryImpl.java
│       └── FahrzeugServiceImpl.java
└── ui/
    ├── dialog/
    │   └── FahrzeugDialog.java
    ├── model/
    │   └── FahrzeugTableModel.java
    ├── FuhrparkUI.java
    └── App.java
```

### Designentscheidungen
- **Vererbung**: Fahrzeug-Basisklasse mit spezialisierten PKW- und LKW-Implementierungen
- **Persistenz**: Serialisierung für einfache Datenspeicherung
- **UI**: Swing für plattformunabhängige Benutzeroberfläche

### Validierungen
- **Kennzeichen**: Format XXX-XX1234 (Buchstaben-Zahlen-Kombination)
- **Wert**: Maximal 10 Millionen €
- **Pflichtfelder**: Alle Felder müssen ausgefüllt sein

## Lizenz
[Lizenzinformationen]
