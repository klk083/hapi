package Hapi.SQLMethods;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by magnu on 14.04.2016.
 */
public class DeprecatedMethods {
    private static Connection con = null;
    private static PreparedStatement stm = null;
    private static ResultSet res = null;

    public static ArrayList<String> listEmployees() {
        ArrayList<String> employees = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT username FROM employee";
            stm = con.prepareStatement(selectSQL);

            res = stm.executeQuery();

            while (res.next()) {
                employees.add(res.getString("username"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of employees, Code: 8000007";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return employees;
        }
    }

    public static ArrayList<String> listCustomers() {
        ArrayList<String> customers = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
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
            closeSQL();

            return customers;
        }
    }
}
