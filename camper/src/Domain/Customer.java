package Domain;

import java.util.Date;

public class Customer {
    private String name;
    private int phone;
    private String street;
    private int zip;
    private String email;
    private int driverLicenseNr;
    private Date dateOfIssue;
    private Date dateOfExpiry;

    public Customer(String name, int phone, String street, int zip, String email, int driverLicenseNr, Date dateOfIssue, Date dateOfExpiry) {
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.zip = zip;
        this.email = email;
        this.driverLicenseNr = driverLicenseNr;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpiry = dateOfExpiry;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDriverLicenseNr(int driverLicenseNr) {
        this.driverLicenseNr = driverLicenseNr;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getName() {
        return name;
    }

    public int getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public int getZip() {
        return zip;
    }

    public String getEmail() {
        return email;
    }

    public int getDriverLicenseNr() {
        return driverLicenseNr;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", street='" + street + '\'' +
                ", zip=" + zip +
                ", email='" + email + '\'' +
                ", driverLicenseNr=" + driverLicenseNr +
                ", dateOfIssue='" + dateOfIssue + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                '}';
    }
}
