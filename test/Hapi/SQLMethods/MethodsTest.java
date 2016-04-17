package Hapi.SQLMethods;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by magnu on 11.04.2016.
 */
public class MethodsTest {
    // All tests are supposed to be run on a database built with our database.sql and test data given in database-insert.sql

    @Before
    public void before() throws Exception {
        // Setup of test user
        String username = "testuser", password = "test", name = "Tester McTest";
        int role = 1;

        Methods.createUser(username, password, name, role);

        // Setup of test customer
        String custName = "Billy Bob", address = "Bobgata 4", tlf = "0";
        boolean isCompany = false;

        Methods.createCustomer(custName, address, tlf, isCompany);

        // Setup of test orders
        ArrayList<ArrayList<String>> search = Methods.listCustomers(custName);
        int customerId = Integer.parseInt(search.get(1).get(0));
        String deliveryTime = "2016-11-11";

        Methods.createOrder(customerId, deliveryTime);
    }


    @Test
    public void createUser() throws Exception {
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        boolean testRes1 = Methods.createUser(username, password, name, role);
        assertEquals(true, testRes1);

        boolean testRes2 = Methods.deleteUser(username);
        assertEquals(true, testRes2);
    }

    @Test
    public void login() throws Exception {
        boolean testRes1 = Methods.login("testuser", "test");

        assertEquals(true, testRes1);

        //-----------------------------------------------------

        // This login should fail, unless someone explicitly created this user before running the test
        boolean testRes2 = Methods.login("xxxxxxxxxx", "xxxxxxxxxxx");

        assertEquals(false, testRes2);

        //-----------------------------------------------------

        // This login tries to login with wrong password for username "testuser"
        boolean testRes3 = Methods.login("testuser", "12345678");

        assertEquals(false, testRes3);

        //-----------------------------------------------------

        // This login test creates a new user, changes its password and checks if the old and new password works
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        Methods.createUser(username, password, name, role);

        String password2 = "test-test";
        Methods.changePassword(username, password2);

        boolean testRes4 = Methods.login(username, password2);

        assertEquals(true, testRes4);

        boolean testRes5 = Methods.login(username, password);

        Methods.deleteUser(username);
        assertEquals(false, testRes5);
    }

    @Test
    public void changePassword() throws Exception {
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        Methods.createUser(username, password, name, role);

        String password2 = "test-test";
        boolean test = Methods.changePassword(username, password2);

        Methods.deleteUser(username);
        assertEquals(true, test);
    }

    @Test
    public void listEmployees1() throws Exception {
        // This test checks if username is found in the database
        String username = "testuser";       // User from @Before

        ArrayList<ArrayList<String>> list = Methods.listEmployees(username);

        assertEquals("testuser", list.get(0).get(0));
    }

    @Test
    public void listEmployees2() throws Exception {
        // This test checks if a users employee_id is found in the database
        String username = "testuser";       // User from @Before
        int employeeID = Methods.getEmployeeID(username);

        ArrayList<ArrayList<String>> list = Methods.listEmployees(username);
        int testRes = Integer.parseInt(list.get(1).get(0));

        assertEquals(employeeID, testRes);   // User made in tests gets ID no. 2, this is because admin-user always gets ID no. 1
    }

    @Test
    public void getRoleID() throws Exception {
        // This test checks if a users role_id is found in the database
        String username = "testuser";       // User from @Before

        int testRes = Methods.getRoleID(username);

        assertEquals(1, testRes);
    }

    @Test
    public void deleteUser() throws Exception {
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        boolean testRes1 = Methods.createUser(username, password, name, role);
        boolean testRes2 = Methods.deleteUser(username);
        assertEquals(true, testRes1);       // createUser test
        assertEquals(true, testRes2);       // deleteUser test
    }

    @Test
    public void createCustomer() throws Exception {
        String name = "Billy Bob 2", address = "Bobgata 4", tlf = "1";
        boolean isCompany = false;

        boolean testRes1 = Methods.createCustomer(name, address, tlf, isCompany);

        ArrayList<ArrayList<String>> list = Methods.listCustomers(name);

        int temp = Integer.parseInt(list.get(1).get(0));
        boolean testRes2 = Methods.deleteCustomer(temp);

        assertEquals(true, testRes1);
        assertEquals(true, testRes2);
    }

    @Test
    public void listCustomers() throws Exception {
        String name = "Billy Bob";

        ArrayList<ArrayList<String>> testRes1 = Methods.listCustomers(name);

        assertEquals("Billy Bob", testRes1.get(0).get(0));
    }

    @Test
    public void setCustomerDiscount() throws Exception {
        String name = "Billy Bob";
        int discount = 20;      // Discount is given in percentage

        ArrayList<ArrayList<String>> list = Methods.listCustomers(name);

        int id = Integer.parseInt(list.get(1).get(0));

        boolean testRes1 = Methods.setCustomerDiscount(id, discount);

        assertEquals(true, testRes1);

        discount = 101;         // Above limit defined in discount-method

        boolean testRes2 = Methods.setCustomerDiscount(id, discount);

        assertEquals(false, testRes2);
    }

    @Test
    public void createOrder() throws Exception {
        // Getting customerID of customer "Billy Bob"
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerId = Integer.parseInt(search.get(1).get(0));

        String deliveryTime = "2016-11-11";

        boolean testRes1 = Methods.createOrder(customerId, deliveryTime);
        assertEquals(true, testRes1);

        // Delete test order
        ArrayList<String> orderSearch = Methods.listOrders(name);

        boolean testRes2 = Methods.deleteOrder(Integer.parseInt(orderSearch.get(1));
        assertEquals(true, testRes2);
    }

    @Test
    public void listOrders() throws Exception {
        String name = "Billy Bob";

        ArrayList<String> testRes1 = Methods.listOrders(name);

        int testResSize = testRes1.size();

        assertEquals(true, (testResSize > 0));

    }

    @Test
    public void deleteOrder() throws Exception {
        // Getting customerID of customer "Billy Bob"
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerId = Integer.parseInt(search.get(1).get(0));

        String deliveryTime = "2016-11-11";

        boolean testRes1 = Methods.createOrder(customerId, deliveryTime);
        assertEquals(true, testRes1);

        // Delete test order
        ArrayList<String> orderSearch = Methods.listOrders(name);

        boolean testRes2 = Methods.deleteOrder(Integer.parseInt(orderSearch.get(1));
        assertEquals(true, testRes2);
    }
/*
    @Test
    public void addMenuToOrder() throws Exception {

    }

    @Test
    public void addSubToOrder() throws Exception {

    }

    @Test
    public void removeMenuFromOrder() throws Exception {

    }

    @Test
    public void addIngredient() throws Exception {

    }

    @Test
    public void removeIngredient() throws Exception {

    }

    @Test
    public void createMenu() throws Exception {

    }


*/
    @After
    public void after() throws Exception {
        // Removal of test user
        String username = "testuser";
        Methods.deleteUser(username);

        // Removal of test customer
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerID = Integer.parseInt(search.get(1).get(0));
        Methods.deleteCustomer(customerID);

        // Removal of test order
        ArrayList<Integer> orderSearch = Methods.listOrders(customerID);
        Methods.deleteOrder(orderSearch.get(0));
    }
}