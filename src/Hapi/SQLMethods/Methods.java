package Hapi.SQLMethods;

import java.io.*;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

public class Methods {
    private static Connection con = null;
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

        String salt = "$6$" + bigInt.toString(32);		// "$6$" for crypt()-method, used to specify SHA512

        return salt;
    }

    private static void closeSQL() {
        SQLConnection.closeResSet(res);
        SQLConnection.closePreparedStatement(stm);
        SQLConnection.closeConnection(con);
    }

    private static int getMenuCostPrice(int menuID) {
        if (menuID < 1) {
            return -1;
        }

        int output = 0;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);

            String selectSQL = "SELECT price, quantity FROM ingredient NATURAL JOIN menu_ingredient WHERE menu_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, menuID);
            res = stm.executeQuery();

            while (res.next()) {
                output += res.getInt("price") * res.getInt("quantity");
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of menu cost price, Code: 8000027";
            SQLConnection.writeMessage(e, errorMessage);

            output = -1;
        } finally {
            closeSQL();

            return output;
        }
    }

    private static int getSubCostPrice(int subscriptionId) {
        if (subscriptionId < 1) {
            return -1;
        }

        int output = 0;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);

            String selectSQL = "SELECT menu_price, quantity FROM menu NATURAL JOIN subscription_menu WHERE subscription_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, subscriptionId);
            res = stm.executeQuery();

            while (res.next()) {
                output += res.getInt("menu_price") * res.getInt("quantity");
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of subscription cost price, Code: 8000049";
            SQLConnection.writeMessage(e, errorMessage);

            output = -1;
        } finally {
            closeSQL();

            return output;
        }
    }

    public static boolean setDeliveryDays(int subID, ArrayList<Boolean> days) {
        if (days == null || days.size() != 7) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "INSERT INTO sub_delivery_days VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1,subID);
            for (int i = 1; i < days.size()+1; i++) {
                stm.setBoolean((i + 1), days.get(i - 1));
            }
            stm.executeUpdate();

            ok = true;
        } catch (SQLException e) {
           try {
               con = SQLConnection.openConnection();
               String selectSQL = "UPDATE sub_delivery_days SET monday =? , tuesday =? , wednesday =? , thursday =? , friday =? , saturday =? , sunday=? WHERE subscription_id=?";
               stm = con.prepareStatement(selectSQL);
               for (int i = 0; i < days.size(); i++) {
                   stm.setBoolean((i+1), days.get(i));
               }
               stm.setInt(8, subID);
               stm.executeUpdate();
               ok=true;

           } catch (SQLException f) {
               String errorMessage = "SQL Exception during insertion of delivery days, ID Code: 8000041";
               SQLConnection.writeMessage(f, errorMessage);
               ok = false;
           }

        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean login(String username, String password) {
        username = username.trim();
        if (username.equals("")) {
            return false;
        }
        String hashFromDatabase = "", saltFromDatabase = "";
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT password_hash, password_salt FROM employee WHERE username = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, username.toLowerCase());

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
            closeSQL();
        }

        String hash = generateHash(password, saltFromDatabase);

        if (hash.equals(hashFromDatabase)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean changePassword(String username, String password) {
        if (username.equals("") || password.equals("")) {
            return false;
        }
        String salt = generateSalt();
        String hash = generateHash(password, salt);
        boolean ok = false;

        // check if hash and salt in database is correct after update (is this needed?)

        // return true if password is changed

        // return false if password is not changed for some reason (should not be possible, except for SQLException)

        try {
            con = SQLConnection.openConnection();
            String updateSQL = "UPDATE employee SET employee.password_hash = ?, employee.password_salt = ? WHERE username = ?";
            stm = con.prepareStatement(updateSQL);
            stm.setString(1, hash);
            stm.setString(2, salt);
            stm.setString(3, username.toLowerCase());

            stm.executeUpdate();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during password change, Code: 8000002";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean createUser(String username, String password, String name, int roleID) {
        name = name.trim();
        username = username.trim();
        String salt = generateSalt();
        String hash = generateHash(password, salt);
        boolean ok = false;

        if (username.equals("") || name.equals("") || password.equals("")) {
            return false;
        }

        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO employee VALUES(DEFAULT, ?, ?, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, roleID);
            stm.setString(2, name);
            stm.setString(3, username.toLowerCase());
            stm.setString(4, hash);
            stm.setString(5, salt);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during user creation, Code: 8000003";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean deleteUser(String username) {
        if (username.equals("admin")) {
            return false;
        }


        int employeeID = getEmployeeID(username);
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM order_chauffeur WHERE employee_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, employeeID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                deleteSQL = "DELETE FROM order_cook WHERE employee_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, employeeID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            deleteSQL = "DELETE FROM employee WHERE username = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setString(1, username.toLowerCase());

            stm.executeUpdate();
            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during user deletion, Code: 8000004";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static int getRoleID(String username) {
        if (username.equals("")) {
            return -1;
        }

        int output = -1;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT role_id FROM employee WHERE username = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, username.toLowerCase());
            res = stm.executeQuery();

            res.next();

            output = res.getInt("role_id");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of role ID, Code: 8000011";
            SQLConnection.writeMessage(e, errorMessage);

            output = -1;
        } finally {
            closeSQL();

            return output;
        }
    }

    public static int getEmployeeID(String username) {
        if (username.equals("")) {
            return -1;
        }

        int output = -1;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT employee_id FROM employee WHERE username = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, username.toLowerCase());
            res = stm.executeQuery();

            res.next();

            output = res.getInt("employee_id");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of employee ID, Code: 8000032";
            SQLConnection.writeMessage(e, errorMessage);

            output = -1;
        } finally {
            closeSQL();

            return output;
        }
    }

    public static String getEmployeeName(int id) {
        if (id==-1) {
            return null;
        }

        String output = null;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name FROM employee WHERE employee_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, id);
            res = stm.executeQuery();

            res.next();

            output = res.getString("name");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of employee name, Code: 8000048";
            SQLConnection.writeMessage(e, errorMessage);

            output = null;
        } finally {
            closeSQL();

            return output;
        }
    }

    public static boolean createCustomer(String name, String address, String tlf, boolean isCompany) {
        name = name.trim();
        if (name.equals("") || address.equals("") || tlf.equals("")) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO customer VALUES(DEFAULT, ?, ?, ?, 0, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setString(2, address);
            stm.setString(3, tlf);
            stm.setBoolean(4, isCompany);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during customer creation, Code: 8000009";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean deleteCustomer(int customerID) {
        if (customerID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            try {
                String updateSQL = "UPDATE orders SET customer_id = 1 WHERE customer_id = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setInt(1, customerID);
                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                String updateSQL = "UPDATE subscription_customer SET customer_id = 1 WHERE customer_id = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setInt(1, customerID);
                stm.executeUpdate();
            } catch (SQLException e) {}

            String deleteSQL = "DELETE FROM customer WHERE customer_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, customerID);

            stm.executeUpdate();
            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during customer deletion, Code: 8000019";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setCustomerDiscount(int customerID, int discount) {
        if (customerID < 1 || discount < 0 || discount > 100) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String updateSQL = "UPDATE customer SET customer_discount = ? WHERE customer_id = ?";
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
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<String> getCustomerContactInfo(int customerID) {
        if (customerID < 1) {
            return null;
        }

        ArrayList<String> info = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT customer_address, customer_tlf FROM customer WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);

            res = stm.executeQuery();
            res.next();

            info.add(res.getString("customer_address"));
            info.add(res.getString("customer_tlf"));
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of customer info, Code: 8000022";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return info;
        }
    }

    public static boolean setCustomerAddress(int customerID, String address) {
        if (customerID < 1 || address.equals("")) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "UPDATE customer SET customer_address = ? WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, address);
            stm.setInt(2, customerID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during update of customer address, Code: 8000063";
            SQLConnection.writeMessage(e, errorMessage);
            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setCustomerPhone(int customerID, String phone) {
        if (customerID < 1 || phone.equals("")) {
            return false;
        }

        int number;
        try {
            number = Integer.parseInt(phone);
        } catch (NumberFormatException e) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "UPDATE customer SET customer_tlf = ? WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, number);
            stm.setInt(2, customerID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during update of customer phone number, Code: 8000064";
            SQLConnection.writeMessage(e, errorMessage);
            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<String> listOrders(String partName) {
        ArrayList<String> orders = new ArrayList<String>();
        String forSQL = "%" + partName + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id FROM orders NATURAL JOIN customer WHERE customer_name LIKE ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();



            while (res.next()) {
                orders.add(res.getString("order_id"));
            }


        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of customers by search, Code: 8000006";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return orders;
        }
    }

    public static ArrayList<Integer> listOrders(int customerID) {
        if (customerID < 1) {
            return null;
        }

        ArrayList<Integer> orders = new ArrayList<Integer>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id FROM orders WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);
            res = stm.executeQuery();

            while (res.next()) {
                orders.add(res.getInt("order_id"));
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of orders, Code: 8000034";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return orders;
        }
    }

    public static ArrayList<ArrayList<String>> listMenu(String partName) {
        partName.toLowerCase();
        ArrayList<ArrayList<String>> menu = new ArrayList<ArrayList<String>>();
        String forSQL = "%" + partName + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_name, menu_id FROM menu WHERE menu_name LIKE ? ORDER BY menu_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("menu_name"));
                temp = res.getInt("menu_id");
                id.add(Integer.toString(temp));
            }

            menu.add(navn);
            menu.add(id);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of menus by search, Code: 8000023";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return menu;
        }
    }

    public static ArrayList<ArrayList<String>> listMenusInOrder(int orderID) {
        if (orderID < 1) {
            return null;
        }

        ArrayList<ArrayList<String>> menus = new ArrayList<ArrayList<String>>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_name, menu_id, quantity, menu_price FROM menu_order NATURAL JOIN menu WHERE order_id = ? ORDER BY menu_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, orderID);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>(), quantity = new ArrayList<String>(),menu_price = new ArrayList<String>();
            while (res.next()) {
                navn.add(res.getString("menu_name"));
                id.add(res.getString("menu_id"));
                quantity.add(res.getString("quantity"));
                menu_price.add(res.getString("menu_price"));

            }

            menus.add(navn);
            menus.add(id);
            menus.add(quantity);
            menus.add(menu_price);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of menus in order, Code: 8000036";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return menus;
        }
    }

    public static ArrayList<ArrayList<String>> listIngredients(String partName) {
        partName.toLowerCase();
        ArrayList<ArrayList<String>> ingredients = new ArrayList<ArrayList<String>>();
        String forSQL = "%" + partName + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, ingredient_id, unit FROM ingredient WHERE name LIKE ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>(), unit = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("name"));
                temp = res.getInt("ingredient_id");
                id.add(Integer.toString(temp));
                unit.add(res.getString("unit"));
            }

            ingredients.add(navn);
            ingredients.add(id);
            ingredients.add(unit);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of ingredients by search, Code: 8000028";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return ingredients;
        }
    }

    public static ArrayList<ArrayList<String>> listIngredientsInMenu(int menuID) {
        if (menuID < 1) {
            return null;
        }

        ArrayList<ArrayList<String>> ingredients = new ArrayList<ArrayList<String>>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, ingredient_id, unit, quantity FROM menu_ingredient NATURAL JOIN ingredient WHERE menu_id = ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, menuID);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>(), unit = new ArrayList<String>(), quantity = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("name"));
                temp = res.getInt("ingredient_id");
                id.add(Integer.toString(temp));
                unit.add(res.getString("unit"));
                temp = res.getInt("quantity");
                quantity.add(Integer.toString(temp));
            }

            ingredients.add(navn);
            ingredients.add(id);
            ingredients.add(unit);
            ingredients.add(quantity);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of ingredients in menu, Code: 8000026";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return ingredients;
        }
    }

    public static ArrayList<ArrayList<String>> listCustomers(String partName) {
        ArrayList<ArrayList<String>> customers = new ArrayList<ArrayList<String>>();
        String forSQL = "%" + partName + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT customer_name, customer_id FROM customer WHERE customer_name LIKE ? ORDER BY customer_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("customer_name"));
                temp = res.getInt("customer_id");
                id.add(Integer.toString(temp));
            }

            navn.remove("Dummybruker");
            id.remove("1");

            customers.add(navn);
            customers.add(id);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of customers by search, Code: 8000006";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return customers;
        }
    }

    public static ArrayList<ArrayList<String>> listEmployees(String partName) {
        ArrayList<ArrayList<String>> employees = new ArrayList<ArrayList<String>>();
        String forSQL = "%" + partName + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT username, employee_id FROM employee WHERE username LIKE ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("username"));
                temp = res.getInt("employee_id");
                id.add(Integer.toString(temp));
            }

            navn.remove("admin");
            id.remove("1");

            employees.add(navn);
            employees.add(id);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of employees by search, Code: 8000008";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return employees;
        }
    }

    public static int createOrder(int customerID, String deliveryTime) {
        if (deliveryTime.equals("") || customerID < 1) {
            return -1;
        }

        int orderID = -1;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String insertSQL = "INSERT INTO orders VALUES(DEFAULT, ?, ?, false, false)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, customerID);
            stm.setString(2, deliveryTime);

            stm.executeUpdate();

            String selectSQL = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
            stm = con.prepareStatement(selectSQL);
            res = stm.executeQuery();
            res.next();

            orderID = res.getInt("order_id");

            con.commit();
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during order creation, Code: 8000012";
            SQLConnection.writeMessage(e, errorMessage);

            orderID = -1;
        } finally {
            closeSQL();

            return orderID;
        }
    }

    public static boolean deleteOrder(int orderID) {
        if (orderID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM menu_order WHERE order_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, orderID);

                stm.executeUpdate();
            } catch (SQLException e) {
            }

            deleteSQL = "DELETE FROM orders WHERE order_id = ?";
            try {
                deleteSQL = "DELETE FROM subscription_order WHERE order_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, orderID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            deleteSQL =  "DELETE FROM orders WHERE order_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, orderID);

            stm.executeUpdate();

            con.commit();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during order deletion, Code: 8000029";
            SQLConnection.writeMessage(e, errorMessage);
            SQLConnection.rollback(con);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setOrderDeliveryTime(int orderID, String deliveryTime) {
        if (deliveryTime.equals("") || orderID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String updateSQL = "UPDATE orders SET delivery_time = ? WHERE order_id = ?";
            stm = con.prepareStatement(updateSQL);
            stm.setString(1, deliveryTime);
            stm.setInt(2, orderID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during update of order delivery time, Code: 8000058";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean addMenuToOrder(int orderID, int menuID, int quantity, String description) {
        if (orderID < 1 || menuID < 1 || quantity < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO menu_order VALUES(?, ?, ?, ?, false)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, menuID);
            stm.setInt(3, quantity);
            stm.setString(4, description);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of menu to order, Code: 8000013";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean removeMenuFromOrder(int orderID, int menuID) {
        if(orderID < 1 || menuID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String deleteSQL = "DELETE FROM menu_order WHERE order_id = ? AND menu_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, menuID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during removal of menu from order, Code: 8000015";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean isMenuInOrder(int menuID) {
        if (menuID < 1) {
            return false;
        }
        boolean result=false;

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT * FROM menu NATURAL JOIN menu_order NATURAL JOIN orders WHERE delivered = FALSE AND menu_id = ?  ";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, menuID);
            res = stm.executeQuery();

            if(res.next()) {
                result=true;
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during check if menu in active order, Code: 8000027";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return result;
        }
    }

    public static boolean addIngredient(String name, String unit, int price) {
        name = name.trim();
        if (name.equals("") || unit.equals("") || price < 0) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO ingredient VALUES(DEFAULT, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setString(2, unit);
            stm.setInt(3, price);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of new ingredient, Code: 8000016";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean removeIngredient(int ingredientID) {
        if (ingredientID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM menu_ingredient WHERE ingredient_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, ingredientID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            deleteSQL = "DELETE FROM ingredient WHERE ingredient_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, ingredientID);

            stm.executeUpdate();
            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during removal of ingredient, Code: 8000017";
            SQLConnection.writeMessage(e, errorMessage);
            SQLConnection.rollback(con);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<String> getIngredientInfo (String ingredientID) {
        ArrayList<String> info = new ArrayList();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, ingredient_id, unit,price FROM ingredient WHERE ingredient_id LIKE ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, ingredientID);
            res = stm.executeQuery();


            while (res.next()) {
                info.add(res.getString("name"));
                info.add(ingredientID);
                info.add(res.getString("unit"));
                info.add(res.getString("price"));
            }


        } catch (SQLException e) {
            String errorMessage = "SQL Exception during info collection of ingredient by id, Code: 8000030";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return info;
        }

    }

    public static int createMenu(String name, String description, int price) {
        name = name.trim();
        if (name.equals("") || description.equals("") || price < 0) {
            return -1;
        }

        int menuID = -1;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO menu VALUES(DEFAULT, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setInt(2, price);
            stm.setString(3, description);

            stm.executeUpdate();


            String selectSQL = "SELECT menu_id FROM menu WHERE menu_name = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, name);

            res = stm.executeQuery();
            res.next();
            menuID = res.getInt("menu_id");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during creation of menu, Code: 8000018";
            SQLConnection.writeMessage(e, errorMessage);

        } finally {
            closeSQL();

            return menuID;
        }
    }

    public static boolean deleteMenu(int menuID) {
        if(menuID < 1) {
            return false;
        }
        boolean ok =false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM menu_ingredient WHERE menu_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, menuID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                String updateSQL = "UPDATE menu_order SET menu_id = 0 WHERE menu_id = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setInt(1, menuID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                deleteSQL = "DELETE FROM subscription_menu WHERE menu_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, menuID);

                stm.executeUpdate();
            } catch (SQLException e) {}

            deleteSQL = "DELETE FROM menu WHERE menu_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, menuID);

            stm.executeUpdate();

            con.commit();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during menu deletion, Code: 8000029";
            SQLConnection.writeMessage(e, errorMessage);
            SQLConnection.rollback(con);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean changeMenu(int menuID, String name, int price, String description) {
        name = name.trim();
        description = description.trim();
        if (menuID < 1 || name.equals("") || description.equals("") || price < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "UPDATE menu SET menu.menu_name = ?, menu.menu_description = ?, menu.menu_price = ? WHERE menu_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setString(2, description);
            stm.setInt(3, price);
            stm.setInt(4, menuID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during change of menu, Code: 8000024";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<String> getMenuInfo(int menuID) {
        if (menuID < 1) {
            return null;
        }

        ArrayList<String> info = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_name, menu_description, menu_price FROM menu WHERE menu_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, menuID);

            res = stm.executeQuery();
            res.next();

            info.add(res.getString("menu_name"));
            info.add(res.getString("menu_description"));
            info.add(res.getString("menu_price"));
            info.add(Integer.toString(getMenuCostPrice(menuID)));
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of menu info, Code: 8000025";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return info;
        }
    }

    public static boolean addIngredientToMenu(int menuID, int ingredientID, int quantity) {
        if (menuID < 1 || ingredientID < 1 || quantity < 1) {
            return false;
        }

        boolean ok = false;
        try {

            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            try{
                String insertSQL = "DELETE FROM menu_ingredient WHERE menu_id = ? AND ingredient_id = ?";
                stm = con.prepareStatement(insertSQL);
                stm.setInt(1, menuID);
                stm.setInt(2, ingredientID);
                stm.executeUpdate();


            } catch (SQLException e){}

            String insertSQL = "INSERT INTO menu_ingredient VALUES(?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, ingredientID);
            stm.setInt(2, menuID);
            stm.setInt(3, quantity);

            stm.executeUpdate();
            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of ingredient to menu, Code: 8000020";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean removeIngredientFromMenu(int menuID, int ingredientID) {
        if(menuID < 1 || ingredientID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String deleteSQL = "DELETE FROM menu_ingredient WHERE ingredient_id = ? AND menu_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, ingredientID);
            stm.setInt(2, menuID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during removal of ingredient from menu, Code: 8000031";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<ArrayList<String>> listSubscriptions(String part1Name) {
        ArrayList<ArrayList<String>> subscription = new ArrayList<ArrayList<String>>();
        String forSQL1 = "%" + part1Name + "%";

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, subscription_id FROM subscription WHERE name LIKE ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, forSQL1);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("name"));
                temp = res.getInt("subscription_id");
                id.add(Integer.toString(temp));
            }

            subscription.add(navn);
            subscription.add(id);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of subscriptions by search, Code: 8000037";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return subscription;
        }
    }

    public static ArrayList<ArrayList<String>> listSubOnCustomer(int customerID) {
        if (customerID < 1) {
            return null;
        }

        ArrayList<ArrayList<String>> subList = new ArrayList<ArrayList<String>>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, subscription_id FROM subscription_customer NATURAL JOIN subscription WHERE customer_id = ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            while (res.next()) {
                navn.add(res.getString("name"));
                id.add(res.getString("subscription_id"));

            }

            subList.add(navn);
            subList.add(id);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of subscription on customer, Code: 8000060";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return subList;
        }
    }

    public static ArrayList<Integer> listSubs(int customerId) {
        if (customerId < 1) {
            return null;
        }

        ArrayList<Integer> subscriptions = new ArrayList<Integer>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT subscription_id FROM subscription_customer WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerId);
            res = stm.executeQuery();

            while (res.next()) {
                subscriptions.add(res.getInt("subscription_id"));
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of orders, Code: 8000034";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return subscriptions;
        }
    }

    public static ArrayList<String> getSubInfo(int subscriptionId) {
        if (subscriptionId < 1) {
            return null;
        }

        ArrayList<String> info = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT name, price, description FROM subscription WHERE subscription_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, subscriptionId);

            res = stm.executeQuery();
            res.next();

            info.add(res.getString("name"));
            info.add(res.getString("description"));
            info.add(res.getString("price"));
            info.add(Integer.toString(getSubCostPrice(subscriptionId)));
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of subscription info, Code: 8000038";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return info;
        }
    }

    public static ArrayList<String> getOrderInfo(int orderId) {
        if (orderId < 1) {
            return null;
        }

        ArrayList<String> info = new ArrayList<String>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id, customer_id, delivery_time, ready FROM orders  WHERE order_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, orderId);

            res = stm.executeQuery();
            res.next();

            info.add(res.getString("order_id"));
            info.add(res.getString("customer_id"));
            info.add(res.getString("delivery_time"));
            info.add(res.getString("ready"));

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of order info, Code: 8000038";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return info;
        }
    }

    public static boolean removeMenuFromSub(int menuID, int subscriptionId) {
        if( menuID < 1 || subscriptionId < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String deleteSQL = "DELETE FROM subscription_menu WHERE subscription_id = ? AND menu_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, subscriptionId);
            stm.setInt(2, menuID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during removal of menu from subscription, Code: 8000045";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean addMenuToSub(int menuID, int subscriptionId, int quantity) {
        if (menuID < 1 || subscriptionId < 1 || quantity < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            try{
                String insertSQL = "DELETE FROM subscription_menu WHERE menu_id = ? AND subscription_id =?";
                stm = con.prepareStatement(insertSQL);
                stm.setInt(1, menuID);
                stm.setInt(2, subscriptionId);
                stm.executeUpdate();

            } catch (SQLException e){}

            String insertSQL = "INSERT INTO subscription_menu VALUES(?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, subscriptionId);
            stm.setInt(2, menuID);
            stm.setInt(3, quantity);

            stm.executeUpdate();
            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of menu to Subscription, Code: 8000050";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static int createSub() {
        int subscriptionId = -1;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO subscription VALUES(DEFAULT, 'StartN', 0, 'descrip')";
            stm = con.prepareStatement(insertSQL);
            stm.executeUpdate();

            String selectSQL = "SELECT subscription_id FROM subscription WHERE name = 'StartN'";
            stm = con.prepareStatement(selectSQL);

            res = stm.executeQuery();
            res.next();
            subscriptionId = res.getInt("subscription_id");
            ArrayList<Boolean> days = new ArrayList<Boolean>();
            for(int i=0;i<7;i++) {
                days.add(false);
            }

            if (!setDeliveryDays(subscriptionId, days)) {
                subscriptionId = -1;

            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during creation of subscription, Code: 8000021";
            SQLConnection.writeMessage(e, errorMessage);

        } finally {
            closeSQL();

            return subscriptionId;
        }
    }

    public static boolean deleteSub(int subscriptionId) {
        if(subscriptionId < 1) {
            return false;
        }
        boolean ok =false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM subscription_menu WHERE subscription_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, subscriptionId);

                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                String updateSQL = "DELETE FROM subscription_customer WHERE subscription_id = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setInt(1, subscriptionId);

                stm.executeUpdate();
            } catch (SQLException e) {}

            try {
                deleteSQL = "DELETE FROM sub_delivery_days WHERE subscription_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, subscriptionId);

                stm.executeUpdate();
            } catch (SQLException e) {}

            deleteSQL = "DELETE FROM subscription WHERE subscription_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, subscriptionId);

            stm.executeUpdate();

            con.commit();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during subscription deletion, Code: 8000041";
            SQLConnection.writeMessage(e, errorMessage);
            SQLConnection.rollback(con);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<ArrayList<String>> listCoursesInSub(int subscriptionId) {
        if (subscriptionId < 1) {
            return null;
        }

        ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_name, menu_id, quantity, menu_price FROM subscription_menu NATURAL JOIN menu WHERE subscription_id = ? ORDER BY menu_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, subscriptionId);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>(), quantity = new ArrayList<String>(), price = new ArrayList();
            int temp;
            while (res.next()) {
                navn.add(res.getString("menu_name"));
                temp = res.getInt("menu_id");
                id.add(Integer.toString(temp));
                temp = res.getInt("quantity");
                quantity.add(Integer.toString(temp));
                price.add(res.getString("menu_price"));
            }

            courses.add(navn);
            courses.add(id);
            courses.add(quantity);
            courses.add(price);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of courses in sub, Code: 8000044";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return courses;
        }
    }

    public static boolean addSubToCustomer (int subID, int customerID, String fromTime, String toTime) {
        if (subID < 1 || customerID < 1 || fromTime.equals("") || toTime.equals("")) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO subscription_customer VALUES(?, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, subID);
            stm.setInt(2, customerID);
            stm.setString(3, fromTime);
            stm.setString(4, toTime);

            stm.executeUpdate();

            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of subscription to customer, Code: 8000040";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean changeIngredient(String ingredientID, String name, int price, String unit) {
        name = name.trim();
        if (ingredientID.equals("") || name.equals("") || unit.equals("") || price < 0) {
            return false;
        }

        int ingredientID2 = Integer.parseInt(ingredientID);

        if (ingredientID2 < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "UPDATE ingredient SET ingredient.name = ?, ingredient.price = ?, ingredient.unit = ? WHERE ingredient_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setInt(2, price);
            stm.setString(3, unit);
            stm.setString(4, ingredientID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during change of ingredient, Code: 8000033";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<ArrayList<String>> listOrdersForDeliveries() {

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id,customer_address FROM orders NATURAL JOIN  customer WHERE ready = true AND orders.order_id NOT IN (SELECT  order_id FROM order_chauffeur) ORDER BY customer_address ASC";
            stm = con.prepareStatement(selectSQL);
            res = stm.executeQuery();

            ArrayList<String> address = new ArrayList<String>(), id = new ArrayList<String>();
            int temp=0;
            while (res.next()) {
                address.add(res.getString("customer_address"));
                temp = res.getInt("order_id");
                id.add(Integer.toString(temp));
            }

            list.add(address);
            list.add(id);

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of ready orders, Code: 8000043";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return list;
        }
    }

    public static ArrayList<ArrayList<String>> listOrdersForChauffeur(int employeeID) {
        if (employeeID < 0) {
            return null;
        }

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id,customer_address FROM orders NATURAL JOIN  customer NATURAL JOIN order_chauffeur WHERE ready = true AND employee_id = ? ORDER BY customer_address ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, employeeID);
            res = stm.executeQuery();

            ArrayList<String> adress = new ArrayList<String>(), id = new ArrayList<String>();
            int temp = 0;
            while (res.next()) {
                adress.add(res.getString("customer_address"));
                temp = res.getInt("order_id");
                id.add(Integer.toString(temp));
            }

            list.add(adress);
            list.add(id);

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of orders on chauffeur, Code: 8000042";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return list;
        }
    }

    public static boolean addOrderToChauffeur(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 0) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO order_chauffeur VALUES (?,?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_chauffeur table, Code: 8000046";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }

    }

    public static boolean removeOrderFromChauffeur(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "DELETE FROM order_chauffeur WHERE order_id = ? AND employee_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_chauffeur table, Code: 8000046";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setOrderToDelivered(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String insertSQL = "DELETE FROM order_chauffeur WHERE order_id = ? AND employee_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);
            stm.executeUpdate();
            String insertSQL1 = "UPDATE orders SET orders.ready = FALSE, orders.delivered = TRUE WHERE order_id = ?";
            stm = con.prepareStatement(insertSQL1);
            stm.setInt(1, orderID);
            stm.executeUpdate();

            con.commit();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception while setting order to delivered, Code: 8000049";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean writeID(int id) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("./employeeID.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(id);
            out.close();
            fileOut.close();
            return true;
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
    }

    public static int getID() {
        int id = -1;
        try {
            FileInputStream fileIn = new FileInputStream("./employeeID.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            id = (Integer) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return -1;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return -1;
        }
        return id;
    }

    public static int findTotalPriceOrder(int orderID) {
        if (orderID < 1) {
            return -1;
        }


        int sum = 0;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_price, quantity FROM menu_order NATURAL JOIN menu WHERE order_id = ? ORDER BY menu_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, orderID);
            res = stm.executeQuery();


            while (res.next()) {
                sum += (res.getInt("menu_price") * res.getInt("quantity"));
            }


        } catch (SQLException e) {
            String errorMessage = "SQL Exception during calculation of total price for menu in order, Code: 8000050";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return sum;

        }
    }

    public static int findTotalPriceSub(int customerID) {
        if (customerID < 1) {
            return -1;
        }


        int sum = 0;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT price FROM subscription NATURAL JOIN subscription_customer WHERE customer_id = ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);
            res = stm.executeQuery();


            while (res.next()) {
                sum += (res.getInt("price"));
            }


        } catch (SQLException e) {
            String errorMessage = "SQL Exception during calculation of total price for menu in order, Code: 8000050";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return sum;

        }
    }

    public static boolean changeSub(int subscriptionId, String name, int price, String description) {
        if (subscriptionId < 1 || name.equals("") || description.equals("") || price < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "UPDATE subscription SET subscription.name = ?, subscription.description = ?, subscription.price = ? WHERE subscription_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setString(2, description);
            stm.setInt(3, price);
            stm.setInt(4, subscriptionId);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during change of subscription, Code: 8000051";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean removeSubFromCustomer(int subID, int customerID) {
        if (subID < 1 || customerID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "DELETE FROM subscription_customer WHERE subscription_id = ? AND customer_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, subID);
            stm.setInt(2, customerID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during removal of subscription from customer, Code: 8000052";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static ArrayList<String> listOrdersForCourses() {

        ArrayList<String> list = new ArrayList<String>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id FROM orders WHERE ready = false AND delivered = false AND orders.order_id NOT IN (SELECT order_id FROM order_cook) ORDER BY delivery_time ASC";
            stm = con.prepareStatement(selectSQL);
            res = stm.executeQuery();

            while (res.next()) {
                list.add(res.getString("order_id"));
            }

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of ready for cooking, Code: 8000054";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();
            return list;
        }
    }

    public static ArrayList<String> listOrdersForCookCourse(int employeeID) {
        if (employeeID < 1) {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id FROM order_cook NATURAL JOIN orders WHERE  employee_id = ? ORDER BY delivery_time ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, employeeID);
            res = stm.executeQuery();
            while (res.next()) {
                list.add(res.getString("order_id"));
            }
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of orders on cook, Code: 8000053";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return list;
        }
    }

    public static ArrayList<String> listOrdersForCookSubs(int employeeID) {

        ArrayList<String> list = new ArrayList<String>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT order_id FROM  order_cook NATURAL JOIN orders  WHERE  employee_id = ? ORDER BY delivery_time ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, employeeID);
            res = stm.executeQuery();

            while (res.next()) {
                list.add(res.getString("order_id"));
            }



        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of orders on cook, Code: 8000053";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return list;
        }
    }

    public static boolean addOrderToCookCourse(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO order_cook VALUES (?,?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);
            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_cook table, Code: 8000055";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }

    }

    public static boolean removeOrderFromCookCourse(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "DELETE FROM order_cook WHERE order_id = ? AND employee_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_cook table, Code: 8000056";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setOrderToReadyCourse(int orderID, int employeeID, boolean isCourse) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }

        boolean ok = false;
        if(isCourse) {
            try {
                con = SQLConnection.openConnection();
                SQLConnection.setAutoCommitOff(con);
                String insertSQL = "DELETE FROM order_cook WHERE order_id = ? AND employee_id = ?";
                stm = con.prepareStatement(insertSQL);
                stm.setInt(1, orderID);
                stm.setInt(2, employeeID);
                stm.executeUpdate();
                String insertSQL1 = "UPDATE orders SET orders.ready = true WHERE order_id = ?";
                stm = con.prepareStatement(insertSQL1);
                stm.setInt(1, orderID);
                stm.executeUpdate();

                con.commit();
                ok = true;
            } catch (SQLException e) {
                String errorMessage = "SQL Exception while setting order to ready, Code: 8000057";
                SQLConnection.writeMessage(e, errorMessage);

                ok = false;
            } finally {
                closeSQL();

                return ok;
            }
        } else {
            return  ok;
        }
    }

    public static boolean addOrderToCookSub(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 1) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO order_cook VALUES (?,?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);
            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_cook table, Code: 8000055";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }

    }

    public static boolean removeOrderFromCookSube(int orderID, int employeeID) {
        if (orderID < 1 || employeeID < 0) {
            return false;
        }
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "DELETE FROM order_cook WHERE order_id = ? AND employee_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, orderID);
            stm.setInt(2, employeeID);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during adding order to employee in order_cook table, Code: 8000056";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static boolean setOrderToReadySub(int orderID, int employeeID, boolean isCourse) {
        if (orderID < 1 || employeeID < 0) {
            return false;
        }

        boolean ok = false;
        if(isCourse) {
            try {
                con = SQLConnection.openConnection();
                SQLConnection.setAutoCommitOff(con);
                String insertSQL = "DELETE FROM order_cook WHERE order_id = ? AND employee_id = ?";
                stm = con.prepareStatement(insertSQL);
                stm.setInt(1, orderID);
                stm.setInt(2, employeeID);
                stm.executeUpdate();
                String insertSQL1 = "UPDATE orders SET orders.ready = true WHERE order_id = ?";
                stm = con.prepareStatement(insertSQL1);
                stm.setInt(1, orderID);
                stm.executeUpdate();

                con.commit();
                ok = true;
            } catch (SQLException e) {
                String errorMessage = "SQL Exception while setting order to ready, Code: 8000057";
                SQLConnection.writeMessage(e, errorMessage);

                ok = false;
            } finally {
                closeSQL();

                return ok;
            }
        } else {
            return  ok;
        }
    }

    public static ArrayList<Boolean> getSubDeliveryDays(int subID) {
        ArrayList<Boolean> list = new ArrayList<Boolean>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT * FROM sub_delivery_days WHERE subscription_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, subID);
            res = stm.executeQuery();
            res.next();

            list.add(res.getBoolean("monday"));
            list.add(res.getBoolean("tuesday"));
            list.add(res.getBoolean("wednesday"));
            list.add(res.getBoolean("thursday"));
            list.add(res.getBoolean("friday"));
            list.add(res.getBoolean("saturday"));
            list.add(res.getBoolean("sunday"));

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of subscription delivery days, Code: 8000059";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return list;
        }
    }

    public static int getSubIDFromCustID(int customerID) {
        if (customerID < 0) {
            return -1;
        }
        int subscriptionID = -1;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT subscription_id FROM subscription_customer WHERE customer_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);
            res = stm.executeQuery();
            res.next();

            subscriptionID = res.getInt("subscription_id");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of subscription ID by customer ID, Code: 8000061";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return subscriptionID;
        }
    }

    public static ArrayList<String> getSubDates(int customerID, int subID) {
        if (customerID < 1) {
            return null;
        }

        ArrayList<String> dates = new ArrayList<String>();
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT from_date, to_date FROM subscription_customer WHERE customer_id = ? AND subscription_id = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, customerID);
            stm.setInt(2, subID);
            res = stm.executeQuery();
            res.next();

            dates.add(res.getString("from_date"));
            dates.add(res.getString("to_date"));

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of subscription dates, Code: 8000062";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return dates;
        }
    }
}