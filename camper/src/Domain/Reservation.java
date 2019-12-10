package Domain;

import Persistance.ToolBox;

import java.util.Date;

public class Reservation {

    private AutoCamper autoCamper;
    private Customer customer;
    private Date startDate;
    private Date endDate;
    private int numberOfDrivers;
    private String typeOfInsutance;
    private double price;
    private double deposit;
    private int weeks;
    private final int FINAL_highSeasonStartMonth = 4;
    private final int FINAL_highSeasonEndMonth = 9;
    private final double FINAL_highSeasonFactor = 1.5;

    public Reservation(AutoCamper autoCamper, Customer customer, Date startDate, Date endDate, int numberOfDrivers,
                       String typeOfInsutance) {
        this.autoCamper = autoCamper;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDrivers = numberOfDrivers;
        this.typeOfInsutance = typeOfInsutance;
        calculatecosts();
    }

    public void calculatecosts() {
        String startDateString = ToolBox.dateToString(startDate);
        String startMonth = startDateString.substring(5,7);

        weeks = ToolBox.getWeeksBetween(startDate, endDate);

        if (Integer.parseInt(startMonth) >= FINAL_highSeasonStartMonth && Integer.parseInt(startMonth) <= FINAL_highSeasonEndMonth) {
            price = autoCamper.getPrice() * FINAL_highSeasonFactor;
        } else {
            price = autoCamper.getPrice();
        }
        price = price * weeks;
        deposit = price * 0.10;


    }

    public AutoCamper getAutoCamper() {
        return autoCamper;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getWeeks() {
        return weeks;
    }

    public double getDeposit() {
        return deposit;
    }

    public double getPrice() {
        return price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfDrivers() {
        return numberOfDrivers;
    }

    public void setNumberOfDrivers(int numberOfDrivers) {
        this.numberOfDrivers = numberOfDrivers;
    }

    public String getTypeOfInsutance() {
        return typeOfInsutance;
    }

    public void setTypeOfInsutance(String typeOfInsutance) {
        this.typeOfInsutance = typeOfInsutance;
    }


}
