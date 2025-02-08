package de.fuhrpark.ui.model;

public class VehicleData {
    private String type;
    private String brand;
    private String model;
    private String licensePlate;
    private String price;

    public VehicleData(String type, String brand, String model, String licensePlate, String price) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.price = price;
    }

    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public String getPrice() { return price; }

    public void setType(String type) { this.type = type; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setPrice(String price) { this.price = price; }
} 