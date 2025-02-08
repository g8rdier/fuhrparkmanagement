public class PKW extends Fahrzeug {
    public PKW(String kennzeichen) {
        super(kennzeichen);
    }
    
    @Override
    public String getTyp() {
        return "PKW";
    }
} 