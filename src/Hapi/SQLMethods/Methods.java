package Hapi.SQLMethods;

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

    private static int getDeliveryDaysID(ArrayList<Boolean> days) {
        if (days == null || days.size() < 7) {
            return -1;
        }

        int daysID = -1;
        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT delivery_id FROM sub_delivery_days WHERE monday = ? AND tuesday = ? AND wednesday = ? AND thursday = ? AND friday = ? AND saturday = ? AND sunday = ?";
            stm = con.prepareStatement(selectSQL);
            for (int i = 0; i < days.size(); i++) {
                stm.setBoolean((i + 1), days.get(0));
            }
            res = stm.executeQuery();
            res.next();

            daysID = res.getInt("delivery_id");
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of delivery days ID Code: 8000041";
            SQLConnection.writeMessage(e, errorMessage);

            daysID = -1;
        } finally {
            closeSQL();

            return daysID;
        }
    }

    public static boolean login(String username, String password) {
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

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String deleteSQL = "DELETE FROM employee WHERE username = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setString(1, username.toLowerCase());

            stm.executeUpdate();
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

    public static boolean createCustomer(String name, String address, String tlf, boolean isCompany) {
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
            String deleteSQL = "DELETE FROM customer WHERE customer_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, customerID);

            stm.executeUpdate();
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
            String selectSQL = "SELECT menu_name, menu_id FROM menu_order NATURAL JOIN menu WHERE order_id = ? ORDER BY menu_name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, orderID);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>();
            while (res.next()) {
                navn.add(res.getString("menu_name"));
                id.add(res.getString("menu_id"));
            }

            menus.add(navn);
            menus.add(id);
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
            String selectSQL = "SELECT customer_name, customer_id FROM customer WHERE customer_name LIKE ?";
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
        if(orderID < 1) {
            return false;
        }
        boolean ok =false;
        try {
            con = SQLConnection.openConnection();
            SQLConnection.setAutoCommitOff(con);
            String deleteSQL = "";

            try {
                deleteSQL = "DELETE FROM menu_order WHERE order_id = ?";
                stm = con.prepareStatement(deleteSQL);
                stm.setInt(1, orderID);

                stm.executeUpdate();
            } catch (SQLException e) {}

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
            String errorMessage = "SQL Exception during retrieval of customer info, Code: 8000025";
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
            String insertSQL = "SELECT quantity FROM menu_ingredient WHERE menu_id = ? AND ingredient_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, menuID);
            stm.setInt(2, ingredientID);
            stm.executeUpdate();
            int antall=0;
            if(res.next()) {
                antall = Integer.parseInt(res.getString("quantity"));
            }
            insertSQL = "INSERT INTO menu_ingredient VALUES(?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, ingredientID);
            stm.setInt(2, menuID);
            stm.setInt(3, quantity+antall);

            stm.executeUpdate();
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

    public static boolean createSubscription(String description) {
        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO subscription VALUES(DEFAULT, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, description);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during subscription creation, Code: 8000021";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }
//public static boolean deleteSubscription(String )
    public static ArrayList<ArrayList<String>> listSubs(String part1Name) {
        part1Name.toLowerCase();
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
            String errorMessage = "SQL Exception during listing of menus by search, Code: 8000023";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return subscription;
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
            info.add(res.getString("price"));
            info.add(res.getString("description"));
            info.add(Integer.toString(getMenuCostPrice(subscriptionId)));
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during retrieval of customer info, Code: 8000038";
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
            String deleteSQL = "DELETE FROM menu_subscription WHERE subscription_id = ? AND menu_id = ?";
            stm = con.prepareStatement(deleteSQL);
            stm.setInt(1, subscriptionId);
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

    public static boolean addMenuToSub(int menuID, int subscriptionId, int quantity) {
        if (menuID < 1 || subscriptionId < 1 || quantity < 1) {
            return false;
        }

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "SELECT quantity FROM subscription_menu WHERE menu_id = ? AND subscription_id = ?";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, menuID);
            stm.setInt(2, subscriptionId);
            stm.executeUpdate();
            int antall=0;
            if(res.next()) {
                antall = Integer.parseInt(res.getString("quantity"));
            }
            insertSQL = "INSERT INTO subscription_menu VALUES(?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, subscriptionId);
            stm.setInt(2, menuID);
            stm.setInt(3, quantity+antall);

            stm.executeUpdate();
            ok = true;
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during addition of ingredient to menu, Code: 8000039";
            SQLConnection.writeMessage(e, errorMessage);

            ok = false;
        } finally {
            closeSQL();

            return ok;
        }
    }

    public static int createSub(String name, String description, int price) {
        if (name.equals("") || description.equals("") || price < 0) {
            return -1;
        }

        int subscriptionId = -1;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO subscription VALUES(DEFAULT, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setString(1, name);
            stm.setInt(2, price);
            stm.setString(3, description);

            stm.executeUpdate();


            String selectSQL = "SELECT subscription_id FROM subscription WHERE name = ?";
            stm = con.prepareStatement(selectSQL);
            stm.setString(1, name);

            res = stm.executeQuery();
            res.next();
            subscriptionId = res.getInt("subscription_id");

        } catch (SQLException e) {
            String errorMessage = "SQL Exception during creation of menu, Code: 8000018";
            SQLConnection.writeMessage(e, errorMessage);

        } finally {
            closeSQL();

            return subscriptionId;
        }
    }

    public static ArrayList<ArrayList<String>> listCoursesInSub(int subscriptionId) {
        if (subscriptionId < 1) {
            return null;
        }

        ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();

        try {
            con = SQLConnection.openConnection();
            String selectSQL = "SELECT menu_name, menu_id, quantity FROM subscription_menu NATURAL JOIN menu WHERE subscription_id = ? ORDER BY name ASC";
            stm = con.prepareStatement(selectSQL);
            stm.setInt(1, subscriptionId);
            res = stm.executeQuery();

            ArrayList<String> navn = new ArrayList<String>(), id = new ArrayList<String>(), quantity = new ArrayList<String>();
            int temp;
            while (res.next()) {
                navn.add(res.getString("menu_name"));
                temp = res.getInt("menu_id");
                id.add(Integer.toString(temp));
                temp = res.getInt("quantity");
                quantity.add(Integer.toString(temp));
            }

            courses.add(navn);
            courses.add(id);
            courses.add(quantity);
        } catch (SQLException e) {
            String errorMessage = "SQL Exception during listing of courses in sub, Code: 8000040";
            SQLConnection.writeMessage(e, errorMessage);
        } finally {
            closeSQL();

            return courses;
        }
    }



    public static boolean addSubToCustomer (int subID, int customerID, String fromTime, String toTime, ArrayList<Boolean> days) {
        if (subID < 1 || customerID < 1) {
            return false;
        }

        int daysID = getDeliveryDaysID(days);

        boolean ok = false;
        try {
            con = SQLConnection.openConnection();
            String insertSQL = "INSERT INTO subscription_customer VALUES(?, ?, ?, ?, ?)";
            stm = con.prepareStatement(insertSQL);
            stm.setInt(1, subID);
            stm.setInt(2, customerID);
            stm.setString(3, fromTime);
            stm.setString(4, toTime);
            stm.setInt(5, daysID);

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
        if (ingredientID.equals("") || name.equals("") || unit.equals("") || price < 0) {
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
}

