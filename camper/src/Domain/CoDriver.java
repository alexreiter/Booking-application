package Domain;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class CoDriver {



    private String CodriverName;
    private String CodriverCity;
    private String CodriverZip;
    private String CodriverStreet;
    private String CodriverDriverLicense;
    private String CodriverDLExp;
    private String CodriverDLDoI;

    public CoDriver(String CodriverName, String CodriverCity, String CodriverZip, String CodriverStreet, String CodriverDriverLicense, String CodriverDLExp, String CodriverDLDoI){
        this.CodriverName = CodriverName;
        this.CodriverCity = CodriverCity;
        this.CodriverZip = CodriverZip;
        this.CodriverStreet = CodriverStreet;
        this.CodriverDriverLicense = CodriverDriverLicense;
        this.CodriverDLExp = CodriverDLExp;
        this.CodriverDLDoI = CodriverDLDoI;
    }


    public String getCodriverName() {
        return CodriverName;
    }

    public void setCodriverName(String codriverName) {
        CodriverName = codriverName;
    }

    public String getCodriverCity() {
        return CodriverCity;
    }

    public void setCodriverCity(String codriverCity) {
        CodriverCity = codriverCity;
    }

    public String getCodriverZip() {
        return CodriverZip;
    }

    public void setCodriverZip(String codriverZip) {
        CodriverZip = codriverZip;
    }

    public String getCodriverStreet() {
        return CodriverStreet;
    }

    public void setCodriverStreet(String codriverStreet) {
        CodriverStreet = codriverStreet;
    }

    public String getCodriverDriverLicense() {
        return CodriverDriverLicense;
    }

    public void setCodriverDriverLicense(String codriverDriverLicense) {
        CodriverDriverLicense = codriverDriverLicense;
    }

    public String getCodriverDLExp() {
        return CodriverDLExp;
    }

    public void setCodriverDLExp(String codriverDLExp) {
        CodriverDLExp = codriverDLExp;
    }

    public String getCodriverDLDoI() {
        return CodriverDLDoI;
    }

    public void setCodriverDLDoI(String codriverDLDoI) {
        CodriverDLDoI = codriverDLDoI;
    }
}
