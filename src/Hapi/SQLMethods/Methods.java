package Hapi.SQLMethods;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

public class Methods {
    private static Connection con = SQLConnection.openConnection();
    private static PreparedStatement stm = null;
    private static ResultSet res = null;

    private static String generateHash(String password, String salt) {
        // requires Apache Commons Codec 1.10, check internet or Password Hashing page in OneNote for info and download
        String hash = org.apache.commons.codec.digest.Crypt.crypt(password, salt);

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
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);
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
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

            return ok;
        }
    }

    public static boolean deleteUser(String username) {
        boolean ok = false;

        if (username.equals("admin")) {
            return false;
        }

        try {
            String deleteSQL = "DELETE FROM employee WHERE username = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setString(1, username);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during user deleting, Code: 8000004";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

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
            SQLConnection.writeMessage(e, errorMessage);

            output = -1;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

            return output;
        }
    }

    public static boolean createOrder(int customerID, String deliveryTime) {
        if (customerID == null || customerID < 0) {
            return false;
        }

        boolean ok = false;
        try {
            String insertSQL = "INSERT INTO orders VALUES(DEFAULT, ?, ?, false)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, customerID);
            stm.setString(2, deliveryTime);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during order creation, Code: 8000012";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            SQLConnection.closeResSet(res);
            SQLConnection.closePreparedStatement(stm);
            SQLConnection.closeConnection(con);

            return ok;
        }
    }


}