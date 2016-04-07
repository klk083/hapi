package Hapi.SQLMethodes;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

class Methods extends SQLConnection {
    private static Connection con = openConnection();
    private static PreparedStatement stm = null;
    private static ResultSet res = null;

    private static String generateHash(String password, String salt) {
        // requires Apache Commons Codec 1.10, check internet or Password Hashing page in OneNote for info and download
        String hash = org.apache.commons.codec.digest.Crypt.crypt(password + salt);

        return hash;
    }

    private static String generateSalt() {
		/*	requires:
		*	import java.security.SecureRandom;
		*	import java.math.BigInteger;
		*/

        SecureRandom random = new SecureRandom();

        BigInteger bigInt = new BigInteger(65, random);

        String salt = "$6$" + bigInt.toString(32);		// "$6$" for crypt()-function, used to specify SHA512

        return salt;
    }

    public static boolean login(String username, String password) {
        String hashFromDatabase = "", saltFromDatabase = "";
        try {
            String selectSQL = "SELECT password_hash, password_salt FROM employee WHERE username = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, username);

            res = stm.executeQuery();

            if (!res.isBeforeFirst() ) {
                return false;
            }

            res.next();

            hashFromDatabase = res.getString("password_hash");
            saltFromDatabase = res.getString("password_salt");
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during login, Code: 8000001";
            writeMessage(e, errorMessage);
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);
        }

        String hash = generateHash(password, saltFromDatabase);

        if (hash.equals(hashFromDatabase)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean changePassword(String username, String password) {
        String salt = generateSalt();
        String hash = generateHash(password, salt);
        boolean ok = false;

        // check if hash and salt in database is correct after update (is this needed?)

        // return true if password is changed

        // return false if password is not changed for some reason (should not be possible, except for SQLException)

        try {
            String updateSQL = "UPDATE employee SET employee.password_hash = ?, employee.password_salt = ? WHERE username = ?";
            stm = con.prepareStatement(updateSQL);
            stm.setString(1, hash);
            stm.setString(2, salt);
            stm.setString(3, username);

            stm.executeUpdate();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during password change, Code: 8000002";
            writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return ok;
        }
    }

    public static boolean createUser(String username, String password, String name, int roleID) {
        String salt = generateSalt();
        String hash = generateHash(password, salt);
        boolean ok = false;

        if (username.equals("") || name.equals("") || password.equals("")) {
            return false;
        }

        try {
            String insertSQL = "INSERT INTO employee VALUES(DEFAULT, ?, ?, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, roleID);
            stm.setString(2, name);
            stm.setString(3, username);
            stm.setString(4, hash);
            stm.setString(5, salt);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during user creation, Code: 8000003";
            writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return ok;
        }
    }

    public static boolean deleteUser(String username) {
        boolean ok = false;

        try {
            String deleteSQL = "DELETE FROM employee WHERE username = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setString(1, username);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during user deleting, Code: 8000004";
            writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return ok;
        }
    }

    public static ArrayList<String> listCustomers() {
        ArrayList<String> customers = new ArrayList<String>();

        try {
            String selectSQL = "SELECT customer_name FROM customer";
            stm = con.prepareStatement(selectSQL);

            res = stm.executeQuery();

            while (res.next()) {
                customers.add(res.getString("customer_name"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of customers, Code: 8000005";
            writeMessage(e, errorMessage);
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return customers;
        }
    }

    public static ArrayList<String> listCustomers(String partName) {
        ArrayList<String> customers = new ArrayList<String>();
        String forSQL = "%" + partName + "%";

        try {
            String selectSQL = "SELECT customer_name FROM customer WHERE customer_name LIKE ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            while (res.next()) {
                customers.add(res.getString("customer_name"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of customers by search, Code: 8000006";
            writeMessage(e, errorMessage);
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return customers;
        }
    }

    public static ArrayList<String> listEmployees() {
        ArrayList<String> employees = new ArrayList<String>();

        try {
            String selectSQL = "SELECT name FROM employee";
            stm = con.prepareStatement(selectSQL);

            res = stm.executeQuery();

            while (res.next()) {
                employees.add(res.getString("name"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of employees, Code: 8000007";
            writeMessage(e, errorMessage);
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return employees;
        }
    }

    public static ArrayList<String> listEmployees(String partName) {
        ArrayList<String> employees = new ArrayList<String>();
        String forSQL = "%" + partName + "%";

        try {
            String selectSQL = "SELECT name FROM employee WHERE name LIKE ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            while (res.next()) {
                employees.add(res.getString("name"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of employees by search, Code: 8000008";
            writeMessage(e, errorMessage);
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return employees;
        }
    }

    public static boolean createCustomer(String name, String address, String tlf) {
        if (name.equals("") || address.equals("") || tlf.equals("")) {
            return false;
        }

        boolean ok = false;
        try {
            String insertSQL = "INSERT INTO customer VALUES(DEFAULT, ?, ?, ?, 0)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setString(2, address);
            stm.setString(3, tlf);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during customer creation, Code: 8000009";
            writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return ok;
        }
    }

    public static boolean setCustomerDiscount(int customerID, int discount) {
        if (customerID < 0 || discount < 0) {
            return false;
        }

        boolean ok = false;
        try {
            String updateSQL = "UPDATE customer SET customer.discount = ? WHERE customer.customer_id = ?";
            stm = con.prepareStatement(updateSQL);
            stm.setInt(1, discount);
            stm.setInt(2, customerID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during change of customer discount, Code: 8000010";
            writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return ok;
        }
    }

    public static int getRoleID(String username) {
        if (username.equals("")) {
            return -1;
        }

        int output = -1;
        try {
            String selectSQL = "SELECT role FROM employee WHERE username = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, username);
            res = stm.executeQuery();

            res.next();

            output = res.getInt("role");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of role ID, Code: 8000011";
            writeMessage(e, errorMessage);

            output = -1;
        } finally {
            closeResSet(res);
            closePreparedStatement(stm);
            closeConnection(con);

            return output;
        }
    }

    public static boolean createOrder(int orderID, int customerID, String deliveryTime) {

    }
}

class SQLConnection {

    public static Connection openConnection() {
        String databaseDriver = "com.mysql.jdbc.Driver";
        String username = "kehildre", password = "3kMBJrQ2";

        Connection con = null;
        try {
            Class.forName(databaseDriver);
            String databaseName = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/" + username + "?user=" + username + "&password=" + password;
            con = DriverManager.getConnection(databaseName);
        } catch (Exception e) {
            String errorMessage = "Exception during connection to database, Code: 8000000";
            writeMessage(e, errorMessage);
        }
        return con;
    }

    public static void closeResSet(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeResSet()");
        }
    }

    public static void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeStatement()");
        }
    }

    public static void closePreparedStatement(PreparedStatement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closePreparedStatement()");
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeConnection()");
        }
    }

    public static void rollback(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.rollback();
            }
        } catch (SQLException e) {
            writeMessage(e, "rollback()");
        }
    }

    public static void setAutoCommitOn(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            writeMessage(e, "setAutoCommit()");
        }
    }

    public static void setAutoCommitOff(Connection con) {
        try {
            if (con != null && con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            writeMessage(e, "setAutoCommit()");
        }
    }

    public static void writeMessage(Exception e, String message) {
        System.err.println("*** Error occured: " + message + ". ***");
        e.printStackTrace(System.err);
    }
}