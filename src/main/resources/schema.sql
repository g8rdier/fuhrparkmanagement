CREATE DATABASE IF NOT EXISTS fuhrpark;
USE fuhrpark;

CREATE TABLE IF NOT EXISTS fahrzeuge (
    kennzeichen VARCHAR(20) PRIMARY KEY,
    marke VARCHAR(50) NOT NULL,
    modell VARCHAR(50) NOT NULL,
    typ VARCHAR(20) NOT NULL,
    baujahr INT,
    kilometerstand DOUBLE
);

CREATE TABLE IF NOT EXISTS fahrtenbuch (
    id IDENTITY PRIMARY KEY,
    datum DATE NOT NULL,
    start_ort VARCHAR(100) NOT NULL,
    ziel_ort VARCHAR(100) NOT NULL,
    kilometer DOUBLE NOT NULL,
    kennzeichen VARCHAR(20) NOT NULL,
    FOREIGN KEY (kennzeichen) REFERENCES fahrzeuge(kennzeichen)
);

CREATE TABLE IF NOT EXISTS reparaturbuch (
    id IDENTITY PRIMARY KEY,
    datum DATE NOT NULL,
    beschreibung TEXT,
    kosten DOUBLE NOT NULL,
    kennzeichen VARCHAR(20) NOT NULL,
    werkstatt VARCHAR(100) NOT NULL,
    FOREIGN KEY (kennzeichen) REFERENCES fahrzeuge(kennzeichen)
); 