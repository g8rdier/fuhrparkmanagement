package de.fuhrpark.model.enums;

public enum FahrzeugTyp {
    PKW("Personenkraftwagen"),
    LKW("Lastkraftwagen");

    private final String bezeichnung;

    FahrzeugTyp(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
} 