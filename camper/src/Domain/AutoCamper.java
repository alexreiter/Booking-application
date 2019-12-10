package Domain;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class AutoCamper {

    private SimpleStringProperty model;
    private SimpleStringProperty licensePlate;
    private SimpleIntegerProperty numberOfBeds;
    private SimpleStringProperty heating;
    private double lowSeasonPrice;
    private SimpleDoubleProperty price;
    private final double FINAL_basicPrice = 3000;
    private final double FINAL_standardPrice = 4000;
    private final double FINAL_luxuryPrice = 5000;


    public AutoCamper(String model, String licensePlate, int numberofBeds, String heating) {
        this.model = new SimpleStringProperty(model);
        this.licensePlate = new SimpleStringProperty(licensePlate);
        this.numberOfBeds = new SimpleIntegerProperty(numberofBeds);
        this.price = new SimpleDoubleProperty(2);
        this.heating = new SimpleStringProperty(heating);
        this.generatePrice();

    }

    private void generatePrice() {
        switch (model.getValue().toLowerCase()) {
            case "basic":
                this.price = new SimpleDoubleProperty(FINAL_basicPrice);
                break;
            case "standard":

                this.price = new SimpleDoubleProperty(FINAL_standardPrice);
                break;

            case "luxury":
                this.price = new SimpleDoubleProperty(FINAL_luxuryPrice);

                break;
        }
    }

    public String getLicensePlate() {
        return licensePlate.get();
    }

    public SimpleStringProperty licensePlateProperty() {
        return licensePlate;
    }
    public SimpleStringProperty heatingProperty() {
        return heating;
    }


    public void setLicensePlate(String licensePlate) {
        this.licensePlate.set(licensePlate);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public double getLowSeasonPrice() {
        return lowSeasonPrice;
    }



    public void setLowSeasonPrice(int lowSeasonPrice) {
        this.lowSeasonPrice = lowSeasonPrice;
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public int getNumberOfBeds() {
        return numberOfBeds.get();
    }

    public SimpleIntegerProperty numberOfBedsProperty() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds.set(numberOfBeds);
    }

}
