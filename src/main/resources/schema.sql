DROP TABLE IF EXISTS reparaturbuch;
DROP TABLE IF EXISTS fahrtenbuch;
DROP TABLE IF EXISTS fahrzeuge;

CREATE TABLE fahrzeuge (
    kennzeichen VARCHAR(20) PRIMARY KEY,
    marke VARCHAR(50),
    modell VARCHAR(50),
    typ VARCHAR(20),
    baujahr INTEGER,
    kilometerstand DOUBLE
);

CREATE TABLE fahrtenbuch (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    kennzeichen VARCHAR(20),
    datum DATE,
    start_ort VARCHAR(100),
    ziel_ort VARCHAR(100),
    kilometer DOUBLE,
    fahrer_typ VARCHAR(20),  -- 'PRIVAT' or 'FIRMA'
    fahrer_name VARCHAR(100),
    grund VARCHAR(200),
    FOREIGN KEY (kennzeichen) REFERENCES fahrzeuge(kennzeichen)
);

CREATE TABLE reparaturbuch (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    kennzeichen VARCHAR(20),
    datum DATE,
    beschreibung VARCHAR(500),
    kosten DOUBLE,
    werkstatt VARCHAR(100),
    FOREIGN KEY (kennzeichen) REFERENCES fahrzeuge(kennzeichen)
);