package de.fuhrpark.model.enums;

public enum ReparaturTyp {
    REPARATUR("Reparatur"),
    WARTUNG("Wartung"),
    AUSTAUSCH("Teileaustausch");

    private final String bezeichnung;

    ReparaturTyp(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
} 