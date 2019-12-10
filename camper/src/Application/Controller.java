package Application;

import Domain.Customer;
import Persistance.ToolBox;
import Persistance.Validation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import Domain.AutoCamper;
import Persistance.DB;
import Domain.Reservation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.*;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class Controller implements Initializable {

    private DB db;
    private ObservableList<AutoCamper> autoCampers;
    private Reservation reservation;
    private ResultSet resultSet;
    private Customer customer;
    private Boolean validCustomerInfo;
    private Boolean validDate;
    private Date pickUpDate;
    private Date dropOffDate;
    private CoDriverController coDriverController;

    @FXML
    private Pane customerPage, autoCamperPage, pricePage;

    @FXML
    private JFXButton btn_next, btn_back, btn_codriver, btnCheck, btn_pric, btn_book, btn_back3;


    @FXML
    private JFXTextField checkTextfield, nameTextfield, phoneTextfield, emailTextfield, streetTextfield, zipTextfield,
            licenseTextfield, issueTextfield, expiryTextfield, weekTextfield, typeTextfield, discountTextfield,
            insuranceTextfield, priceTextfield, depositTextfield;


    @FXML
    private JFXDatePicker dp, dp_checkout;


    @FXML
    private TableView<AutoCamper> autoCamperTableView;

    @FXML
    private TableColumn<AutoCamper, String> col1;

    @FXML
    private TableColumn<AutoCamper, Integer> col2;

    @FXML
    private TableColumn<AutoCamper, Integer> col3;

    @FXML
    private TableColumn<AutoCamper, String> col4;

    @FXML
    private TableColumn<AutoCamper, Double> col5;


    @FXML
    private JFXRadioButton btn_radio1, btn_radio2;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = DB.getInstance();
        coDriverController = new CoDriverController();
        validCustomerInfo = false;

        // Factory to create Cell of DatePicker
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Show Weekends in blue color
                        DayOfWeek day = DayOfWeek.from(item);
                        if (item.getDayOfWeek() == DayOfWeek.MONDAY //
                                || item.getDayOfWeek() == DayOfWeek.TUESDAY //
                                || item.getDayOfWeek() == DayOfWeek.WEDNESDAY
                                || item.getDayOfWeek() == DayOfWeek.THURSDAY //
                                || item.getDayOfWeek() == DayOfWeek.FRIDAY //
                                || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };


            }

        };
        dp.setDayCellFactory(dayCellFactory);
        dp_checkout.setDayCellFactory(dayCellFactory);


        //creating table view for autocamper data and prices


    }

    private int createTableview() {
        autoCamperTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 11);
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 16);
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 24);
        col5.setMaxWidth(1f * Integer.MAX_VALUE * 24);


        autoCampers = FXCollections.observableArrayList();
        int counter = 0;
        try {
            resultSet = db.getVacantAutocampers(pickUpDate, dropOffDate);
            ResultSetMetaData rsmd = resultSet.getMetaData();


            while (resultSet.next()) {
                counter++;
                autoCampers.add(new AutoCamper(resultSet.getString(3), resultSet.getString(1),
                        resultSet.getInt(4), resultSet.getString(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        col1.setCellValueFactory(new PropertyValueFactory<AutoCamper, String>("model"));
        col2.setCellValueFactory(new PropertyValueFactory<AutoCamper, Integer>("licensePlate"));
        col3.setCellValueFactory(new PropertyValueFactory<AutoCamper, Integer>("numberOfBeds"));
        col4.setCellValueFactory(new PropertyValueFactory<AutoCamper, String>("heating"));
        col5.setCellValueFactory(new PropertyValueFactory<AutoCamper, Double>("price"));


        autoCamperTableView.setItems(autoCampers);
        autoCamperTableView.setFixedCellSize(40);


        //radio button: only one can be selected
        ToggleGroup radioGroup = new ToggleGroup();

        btn_radio1.setToggleGroup(radioGroup);
        btn_radio2.setToggleGroup(radioGroup);


        return counter;

    }


    @FXML
    @SuppressWarnings("Duplicates")
    void btn_save(ActionEvent event) throws ParseException {
        //TODO add interaction with DB. (Saving data from first scene in GUI).
        validCustomerInfo = true;
        String errorMessage = "";
        if (!Validation.inputValidation(nameTextfield.getText(), "nameValidation")) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Only letters are accepted in name \n";

        }
        if (!Validation.inputValidation(phoneTextfield.getText(), "phoneValidation")) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Phone number can only be 8 digits \n";

        }
        try {
            if (!db.checkZipCode(Integer.parseInt(zipTextfield.getText()))) {
                validCustomerInfo = false;
                errorMessage = errorMessage + "Not a valid zip code \n";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException g) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Not a valid zip code \n";

        }

        if (!Validation.inputValidation(licenseTextfield.getText(), "driverLicenseValidation")) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Driver license nr. has to be 8 digits \n";

        }
        if (!Validation.inputValidation(issueTextfield.getText(), "dateValidation")) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Not a valid date - use (\"yyyy-mm-dd\" format \n";

        }
        if (!Validation.inputValidation(expiryTextfield.getText(), "dateValidation")) {
            validCustomerInfo = false;
            errorMessage = errorMessage + "Not a valid date - use (\"yyyy-mm-dd\" format \n";

        }

        if (validCustomerInfo) {
            Date issueDateConverted = ToolBox.stringToDate(issueTextfield.getText());

            Date expirationDateConverted = ToolBox.stringToDate(expiryTextfield.getText());

            customer = new Customer(nameTextfield.getText(), Integer.parseInt(phoneTextfield.getText()),
                    streetTextfield.getText(), Integer.parseInt(zipTextfield.getText()), emailTextfield.getText(),
                    Integer.parseInt(licenseTextfield.getText()), issueDateConverted, expirationDateConverted);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
            alert.show();

        }
    }


    @FXML
    void btnNextPage(ActionEvent event) {
        validDate = true;

        try {
            LocalDate localDate = dp.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            pickUpDate = Date.from(instant);

            localDate = dp_checkout.getValue();
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            dropOffDate = Date.from(instant);



            } catch(NullPointerException e){
                validDate = false;
            }
            if (pickUpDate.compareTo(dropOffDate) == -1) {
            if (validCustomerInfo) {
                if (validDate) {
                    if (createTableview() > 0) {
                        autoCamperPage.setVisible(true);
                        customerPage.setVisible(false);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "There is no campers available in the " +
                                "selected period");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You have to pick a pick up and drop off date");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have to save valid customer information in order to proceed");
                alert.show();
            }
        } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The drop off date has to be after the pick up date");
                alert.show();
        }
    }


    @FXML
    void btnBack(ActionEvent event) {
        autoCamperPage.setVisible(false);
        customerPage.setVisible(true);
    }

    @FXML
    void btnCodriver(ActionEvent event) throws IOException {
        Stage homeScreen = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../UI/fxml/codriver.fxml"));
        Scene scene = new Scene(root);
        homeScreen.setScene(scene);
        homeScreen.show();
    }

    @FXML
    void btnCheck(ActionEvent event) {
        if (checkTextfield.getText().length() == 8 && Pattern.matches("^[0-9]*$", checkTextfield.getText())) {
            getCustomerInfo(Integer.parseInt(checkTextfield.getText()));
            try {
                if (resultSet.next()) {
                    nameTextfield.setText(resultSet.getString(3));
                    phoneTextfield.setText("" + resultSet.getInt(4));
                    streetTextfield.setText(resultSet.getString(5));
                    emailTextfield.setText(resultSet.getString(6));
                    zipTextfield.setText("" + resultSet.getInt(2));
                    licenseTextfield.setText("" + resultSet.getInt(1));
                    issueTextfield.setText("" + resultSet.getString(8));
                    expiryTextfield.setText("" + resultSet.getString(9));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "There is no customer registered " +
                            "with that driver license number");
                    alert.show();


                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only an 8 digit driver license nr. is accepted");
            alert.show();

        }

    }


    private void getCustomerInfo(int driverLicenseNr) {
        try {
            resultSet = db.getCustomerInfo(driverLicenseNr);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void btnPriceCalc(ActionEvent event) throws IOException {


        String selectedInsurance = "Basic";
        if (btn_radio2.isSelected()) {
            selectedInsurance = "Super Cover Plus";
        }
        try {
            reservation = new Reservation(autoCamperTableView.getSelectionModel().getSelectedItem(), customer,
                    pickUpDate, dropOffDate,
                    db.getDriverCounter(), selectedInsurance);

            fillPriceInfo();

            autoCamperPage.setVisible(false);
            pricePage.setVisible(true);


        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have to click on your desired auto camper");
            alert.show();
        }
    }

    @FXML
    void book() {
        db.transferCustomerInfo(customer.getDriverLicenseNr(), customer.getZip(), customer.getName(), customer.getPhone(),
                customer.getStreet(), customer.getEmail(), 0, customer.getDateOfIssue(), customer.getDateOfExpiry());


        db.createReservation(reservation.getAutoCamper().getLicensePlate(), reservation.getCustomer().getDriverLicenseNr(),
                reservation.getStartDate(), reservation.getEndDate(), reservation.getNumberOfDrivers(), reservation.getTypeOfInsutance());

        btn_book.setDisable(true);
        btn_back3.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking successful");
        alert.show();

    }

    private void fillPriceInfo() {
        weekTextfield.setText("" + reservation.getWeeks());
        typeTextfield.setText(autoCamperTableView.getSelectionModel().getSelectedItem().getModel());
        insuranceTextfield.setText(reservation.getTypeOfInsutance());
        discountTextfield.setText(checkDiscount() + "%");
        priceTextfield.setText("" + reservation.getPrice());
        depositTextfield.setText("" + reservation.getDeposit());


    }

    private int checkDiscount() {
        int customerCount = db.customerCount(customer.getDriverLicenseNr());
        int discount = 0;
        if (customerCount < 2) {
            discount = 0;
        } else if (customerCount < 5) {
            discount = 5;
        } else if (customerCount >= 6) {
            discount = 10;
        }
        return discount;
    }
    @FXML
    void btn_back3 (ActionEvent event) {
        pricePage.setVisible(false);
        autoCamperPage.setVisible(true);
    }



}