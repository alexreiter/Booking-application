package Persistance;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class Validation {



    public Validation() {


    }


    public static boolean inputValidation(String input, String type) {
        switch (type) {
            case "nameValidation":
                if (Pattern.matches("^[a-zA-ZÆØÅæøå]+$", input)) {
                    return true;
                } else {
                    return false;
                }

            case "phoneValidation":
                if (Pattern.matches("^[0-9]*$", input) && input.length() == 8) {
                    return true;
                } else {
                    return false;
                }
            case "driverLicenseValidation":
                if (input.length() == 8 && Pattern.matches("^[0-9]*$", input)) {
                    return true;
                } else {
                    return false;
                }
            case "dateValidation":
                if (Pattern.matches("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$", input)) {
                    return true;
                } else {
                    return false;
                }

        }
        return false;
    }
}

