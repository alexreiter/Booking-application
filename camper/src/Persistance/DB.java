package Persistance;

import java.sql.*;
import java.util.Date;

/**
 * @author tha
 */
public class DB {
    private static DB instance;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private String port;
    private String databaseName;
    private String userName;
    private String password;

    public final String NOMOREDATA = "|ND|";
    private int numberOfColumns;
    private int currentColumnNumber = 1;
    private ResultSet resultSet;
    private int driverCounter;

    /**
     * STATES
     */
    private boolean moreData = false;  // from Resultset
    private boolean pendingData = false; // from select statement
    private boolean terminated = false;

    private DB() {
        driverCounter = 1;
        port = "1433";
        databaseName = "autoCamper";
        userName = "sa";
        password = "1234";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Database Ready");



    }

    private void connect() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:" + port + ";databaseName=" + databaseName, userName, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            PreparedStatement ps = con.prepareStatement("ff");
            ps.execute();
        }
    }


    private void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @param sql the sql string to be executed in SQLServer
     */
    public void selectSQL(String sql) {
        if (terminated) {
            System.exit(0);
        }
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            pendingData = true;
            moreData = rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            numberOfColumns = rsmd.getColumnCount();
        } catch (Exception e) {
            System.err.println("Error in the sql parameter, please test this in SQLServer first");
            System.err.println(e.getMessage());
        }
    }

    /**
     * @return The next single value (formatted) from previous select
     */
    public String getDisplayData() {
        if (terminated) {
            System.exit(0);
        }
        if (!pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! No previous select, communication with the database is lost!");
        } else if (!moreData) {
            disconnect();
            pendingData = false;
            return NOMOREDATA;
        } else {
            return getNextValue(true);
        }
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * @return The next single value (without formatting) from previous select
     */
    public String getData() {
        if (terminated) {
            System.exit(0);
        }
        if (!pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! No previous select, communication with the database is lost!");
        } else if (!moreData) {
            disconnect();
            pendingData = false;
            return NOMOREDATA;
        } else {
            return getNextValue(false).trim();
        }
    }

    private String getNextValue(boolean view) {
        StringBuilder value = new StringBuilder();
        try {
            value.append(rs.getString(currentColumnNumber));
            if (currentColumnNumber >= numberOfColumns) {
                currentColumnNumber = 1;
                if (view) {
                    value.append("\n");
                }
                moreData = rs.next();
            } else {
                if (view) {
                    value.append(" ");
                }
                currentColumnNumber++;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return value.toString();
    }

    public boolean insertSQL(String sql) {
        return executeUpdate(sql);
    }

    public boolean updateSQL(String sql) {
        return executeUpdate(sql);
    }

    public boolean deleteSQL(String sql) {
        return executeUpdate(sql);
    }

    private boolean executeUpdate(String sql) {
        if (terminated) {
            System.exit(0);
        }
        if (pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! There were pending data from previous select, communication with the database is lost! ");
        }
        try {
            if (ps != null) {
                ps.close();
            }
            connect();
            ps = con.prepareStatement(sql);
            int rows = ps.executeUpdate();
            ps.close();
            if (rows > 0) {
                return true;
            }
        } catch (RuntimeException | SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            disconnect();
        }
        return false;

    }

    public Boolean transferCustomerInfo(int driverLicenseNr, int zip, String name, int phoneNo, String address, String email, int customerCount
    , Date issueDate, Date expirationDate) {
        try {
            connect();
            ps = con.prepareStatement("EXECUTE handleCustomerInfo ?,?,?,?,?,?,?,?,?");
            ps.setInt(1, driverLicenseNr);
            ps.setInt(2, zip);
            ps.setString(3, name);
            ps.setInt(4, phoneNo);
            ps.setString(5, address);
            ps.setString(6, email);
            ps.setInt(7, customerCount);
            ps.setDate(8, new java.sql.Date(issueDate.getTime()));
            ps.setDate(9, new java.sql.Date(expirationDate.getTime()));
            int rows = ps.executeUpdate();
            ps.close();

            driverCounter++;


            if (rows > 0) {
                driverCounter++;
                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public ResultSet getCustomerInfo(int driverLicenseNr) throws SQLException {
        connect();
        ps = con.prepareStatement("EXECUTE getExistingCustomerInfo ?");
        ps.setInt(1, driverLicenseNr);
        resultSet = ps.executeQuery();

        return resultSet;


    }

    public ResultSet getVacantAutocampers(java.util.Date startDate, Date endDate) throws SQLException {
        connect();
        ps = con.prepareStatement("EXECUTE getvacantAutocampers ?,?");
        java.sql.Date startDateConverted = new java.sql.Date(startDate.getTime());
        java.sql.Date endDateConverted = new java.sql.Date(endDate.getTime());
        ps.setDate(1, startDateConverted);
        ps.setDate(2, endDateConverted);
        resultSet = ps.executeQuery();

        return resultSet;

    }

    public boolean checkExistingCustomer(int driverLicenseNr) throws SQLException {
        connect();
        ps = con.prepareStatement("SELECT dbo.checkExistingCustomer2 (?)");
        ps.setInt(1, driverLicenseNr);
        resultSet = ps.executeQuery();

        resultSet.next();
        return resultSet.getBoolean(1);


    }

    public boolean checkZipCode(int zipCode) throws SQLException {
        connect();
        ps = con.prepareStatement("SELECT dbo.checkZipCode (?)");
        ps.setInt(1, zipCode);
        resultSet = ps.executeQuery();

        resultSet.next();
        return resultSet.getBoolean(1);
    }

    public static synchronized DB getInstance() {

        if (instance == null)
            instance = new DB();

        return instance;

    }

    public void createReservation(String licensePlate, int driverLicenseNr, Date startdate, Date enddate,
                                  int numberOfBeds, String typeOfInsurance) {

        try {
            connect();

            java.sql.Date startDateConverted = new java.sql.Date(startdate.getTime());
            java.sql.Date endDateConverted = new java.sql.Date(enddate.getTime());
            ps = con.prepareStatement("EXECUTE createReservation ?,?,?,?,?,?");
            ps.setString(1, licensePlate);
            ps.setInt(2, driverLicenseNr);
            ps.setDate(3, startDateConverted);
            ps.setDate(4, endDateConverted);
            ps.setInt(5, numberOfBeds);
            ps.setString(6, typeOfInsurance);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int customerCount (int driverLicenseNr) {
        int count = 0;
        try {
            connect();
            ps = con.prepareStatement("SELECT dbo.customerCount (?)");
            ps.setInt(1, driverLicenseNr);
            resultSet = ps.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;


    }

    public int getDriverCounter() {
        return driverCounter;
    }
}



