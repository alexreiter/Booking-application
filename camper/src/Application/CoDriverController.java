package Application;

import Domain.Customer;
import Persistance.DB;
import Persistance.Validation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class CoDriverController implements Initializable {
    private Customer customer;
    private DB db;

    @FXML
    private AnchorPane pane_codriver;

    @FXML
    private JFXButton btn_back2, btn_register;

    @FXML
    private JFXTextField nameTextfield, phoneTextfield, emailTextfield, streetTextfield, zipTextfield, licenseTextfield, issueTextfield, expiryTextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = DB.getInstance();


    }

    /**
     * Closes the "pop-up" scene.
     *
     * @param event
     * @throws java.io.IOException
     */

    @FXML
    void btnBackToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) btn_back2.getScene().getWindow();
        stage.close();
    }

    @FXML
    @SuppressWarnings("Duplicates")
    void registerCo(ActionEvent event) throws ParseException {
        boolean allValid = true;
        String errorMessage = "";
        if (!Validation.inputValidation(nameTextfield.getText(), "nameValidation")) {
            allValid = false;
            errorMessage = errorMessage + "Only letters are accepted in name \n";

        }
        if (!Validation.inputValidation(phoneTextfield.getText(), "phoneValidation")) {
            allValid = false;
            errorMessage = errorMessage + "Phone number can only be 8 digits \n";

        }
        try {
            if (!db.checkZipCode(Integer.parseInt(zipTextfield.getText()))) {
                allValid = false;
                errorMessage = errorMessage + "Not a valid zip code \n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException g) {
            allValid = false;
            errorMessage = errorMessage + "Not a valid zip code \n";

        }

        if (!Validation.inputValidation(licenseTextfield.getText(), "driverLicenseValidation")) {
            allValid = false;
            errorMessage = errorMessage + "Driver license nr. has to be 8 digits \n";

        }
        if (!Validation.inputValidation(issueTextfield.getText(), "dateValidation")) {
            allValid = false;
            errorMessage = errorMessage + "Not a valid date - use (\"yyyy-mm-dd\" format \n";

        }
        if (!Validation.inputValidation(expiryTextfield.getText(), "dateValidation")) {
            allValid = false;
            errorMessage = errorMessage + "Not a valid date - use (\"yyyy-mm-dd\" format \n";

        }

        if (allValid) {

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date issueDateConverted = sdf1.parse(issueTextfield.getText());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date expirationDateConverted = sdf2.parse(expiryTextfield.getText());
            customer = new Customer(nameTextfield.getText(), Integer.parseInt(phoneTextfield.getText()),
                    streetTextfield.getText(), Integer.parseInt(zipTextfield.getText()), emailTextfield.getText(),
                    Integer.parseInt(licenseTextfield.getText()), issueDateConverted, expirationDateConverted);

            db.transferCustomerInfo(customer.getDriverLicenseNr(), customer.getZip(), customer.getName(), customer.getPhone(),
                    customer.getStreet(), customer.getEmail(), 0, customer.getDateOfIssue(), customer.getDateOfExpiry());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
            alert.show();

        }
    }


}

