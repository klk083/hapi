package Hapi.SQLMethods;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by magnu on 11.04.2016.
 */
public class MethodsTest {
    // All tests are supposed to be run on a database built with our database.sql and test data given in test-data.sql

    @BeforeClass
    public static void before() throws Exception {
        // Setup of test user
        String username = "testuser", password = "test", name = "Tester McTest";
        int role = 1;

        Methods.createUser(username, password, name, role);

        // Setup of test customer
        String custName = "Billy Bob", address = "Bobgata 4", tlf = "0";
        boolean isCompany = false;

        Methods.createCustomer(custName, address, tlf, isCompany);

        // Setup of test order
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
    public void getCustomerContactInfo() throws Exception {
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> list = Methods.listCustomers(name);
        int id = Integer.parseInt(list.get(1).get(0));

        ArrayList<String> search = Methods.getCustomerContactInfo(id);

        // Customer data from @Before
        assertEquals("Bobgata 4", search.get(0));
        assertEquals("0", search.get(1));
    }

    @Test
    public void createOrder() throws Exception {
        // Getting customerID of customer "Billy Bob"
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerId = Integer.parseInt(search.get(1).get(0));

        String deliveryTime = "2016-11-11";

        int orderID = Methods.createOrder(customerId, deliveryTime);
        ArrayList<String> orderSearch = Methods.listOrders(name);
        int temp = Integer.parseInt(orderSearch.get(1));
        boolean testRes1 = (orderID == temp);
        assertEquals(true, testRes1);

        // Delete test order
        boolean testRes2 = Methods.deleteOrder(orderID);
        assertEquals(true, testRes2);
    }

    @Test
    public void listOrders() throws Exception {
        String name = "Billy Bob";
        ArrayList<String> testRes1 = Methods.listOrders(name);
        int testResSize = testRes1.size();

        // Tests listOrders(String partName)
        assertEquals(true, (testResSize > 0));


        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerId = Integer.parseInt(search.get(1).get(0));
        ArrayList<Integer> testRes2 = Methods.listOrders(customerId);
        testResSize = testRes2.size();

        // Tests listOrders(int customerID)
        assertEquals(true, (testResSize > 0));

    }

    @Test
    public void deleteOrder() throws Exception {
        // Getting customerID of customer "Billy Bob"
        String name = "Billy Bob";
        ArrayList<ArrayList<String>> search = Methods.listCustomers(name);
        int customerId = Integer.parseInt(search.get(1).get(0));

        String deliveryTime = "2016-11-12";

        Methods.createOrder(customerId, deliveryTime);


        // Delete test order
        ArrayList<String> orderSearch = Methods.listOrders(name);

        boolean testRes1 = Methods.deleteOrder(Integer.parseInt(orderSearch.get(1)));
        assertEquals(true, testRes1);
    }

    @Test
    public void addMenuToOrder() throws Exception {
        String name = "Billy Bob", description = "";
        int menuID = 1, quantity = 1;

        ArrayList<String> search = Methods.listOrders(name);
        int orderID = Integer.parseInt(search.get(0));
        boolean testRes1 = Methods.addMenuToOrder(orderID, menuID, quantity, description);

        assertEquals(true, testRes1);


        // Test using negative values for orderID, menuID and quantity
        boolean testRes2 = Methods.addMenuToOrder(-1, -1, -1, description);

        assertEquals(false, testRes2);


        // Delete test addition of menu
        boolean testRes3 = Methods.removeMenuFromOrder(orderID, menuID);
        assertEquals(true, testRes3);
    }

    @Test
    public void removeMenuFromOrder() throws Exception {
        String name = "Billy Bob", description = "";
        int menuID = 1, quantity = 1;

        // Creation of test order
        ArrayList<String> search = Methods.listOrders(name);
        int orderID = Integer.parseInt(search.get(0));
        boolean testRes1 = Methods.addMenuToOrder(orderID, menuID, quantity, description);
        assertEquals(true, testRes1);


        // Delete test addition of menu
        boolean testRes2 = Methods.removeMenuFromOrder(orderID, menuID);
        assertEquals(true, testRes2);


        // Test using negative values for orderID, menuID
        boolean testRes3 = Methods.removeMenuFromOrder(-1, -1);
        assertEquals(false, testRes3);
    }

    @Test
    public void addIngredient() throws Exception {
        String name = "Test Potato", unit = "stk";
        int price = 30;
        boolean testRes1 = Methods.addIngredient(name, unit, price);

        assertEquals(true, testRes1);


        // Remove test ingredient
        ArrayList<ArrayList<String>> search = Methods.listIngredients(name);
        int id = Integer.parseInt(search.get(1).get(0));
        boolean testRes2 = Methods.removeIngredient(id);

        assertEquals(true, testRes2);


        // Test for failure in adding new ingredient
        price = -10;
        boolean testRes3 = Methods.addIngredient(name, unit, price);

        assertEquals(false, testRes3);
    }

    @Test
    public void removeIngredient() throws Exception {
        // Create ingredient to remove
        String name = "Test Potato", unit = "stk";
        int price = 40;
        boolean testRes1 = Methods.addIngredient(name, unit, price);

        assertEquals(true, testRes1);


        // Remove test ingredient
        ArrayList<ArrayList<String>> search = Methods.listIngredients(name);
        int id = Integer.parseInt(search.get(1).get(0));
        boolean testRes2 = Methods.removeIngredient(id);

        assertEquals(true, testRes2);


        // Test for failure in removal of ingredient

        id = -10;
        boolean testRes3 = Methods.removeIngredient(id);

        assertEquals(false, testRes3);
    }

    @Test
    public void createMenu() throws Exception {
        String name = "Potetsuppe", description = "Suppe laget av potet";
        int price = 100;
        int testRes1 = Methods.createMenu(name, description, price);
        ArrayList<ArrayList<String>> search = Methods.listMenu(name);
        int expRes = Integer.parseInt(search.get(1).get(0));

        assertEquals(expRes, testRes1);


        // Remove test menu
        boolean testRes2 = Methods.deleteMenu(expRes);

        assertEquals(true, testRes2);
    }

    @Test
    public void listMenu() throws Exception {
        String testmat = "Dummymat";

        // Empty search to list all items in menu
        ArrayList<ArrayList<String>> search = Methods.listMenu("");
        int testRes1 = search.get(0).size();

        // Test data only includes 10 items in menu
        assertEquals(11, testRes1);


        // First search result should be "Dummymat"
        search = Methods.listMenu(testmat);
        String testRes2 = search.get(0).get(0);

        assertEquals(testmat, testRes2);
    }

    @Test
    public void deleteMenu() throws Exception {
        // Create menu for deletion
        String name = "Potetsuppe", description = "Suppe laget av potet";
        int price = 100;
        int testRes1 = Methods.createMenu(name, description, price);
        ArrayList<ArrayList<String>> search = Methods.listMenu(name);
        int expRes = Integer.parseInt(search.get(1).get(0));

        assertEquals(expRes, testRes1);


        // Remove test menu
        boolean testRes2 = Methods.deleteMenu(expRes);

        assertEquals(true, testRes2);
    }

    @Test
    public void listMenusInOrder() throws Exception {
        int orderID = 1;
        ArrayList<ArrayList<String>> search = Methods.listMenusInOrder(orderID);
        String expName = "Dummymat", expID = "1";

        assertEquals(expName, search.get(0).get(0));
        assertEquals(expID, search.get(1).get(0));
    }

    @Test
    public void listIngredients() throws Exception {
        String expName1 = "Burger", expName2 = "Ost";
        int expAmount = 6;

        // Blank search lists all ingredients
        ArrayList<ArrayList<String>> search = Methods.listIngredients("");

        assertEquals(expName1, search.get(0).get(0));
        assertEquals(expName2, search.get(0).get(1));
        assertEquals(expAmount, search.get(0).size());


        // Search for ingredient
        String name = "Potet";
        search = Methods.listIngredients(name);

        assertEquals(name, search.get(0).get(0));
    }

    @Test
    public void listIngredientsInMenu() throws Exception {
        int menuID = 5, expAmount = 2;
        String expName1 = "Ost", expName2 = "Luft";
        ArrayList<ArrayList<String>> search = Methods.listIngredientsInMenu(menuID);

        assertEquals(expName1, search.get(0).get(0));
        assertEquals(expName2, search.get(0).get(1));
        assertEquals(expAmount, search.get(0).size());


        // Search with negative menuID
        search = Methods.listIngredientsInMenu(-1);

        assertEquals(null, search);
    }

    @Test
    public void isMenuInOrder() throws Exception {
        int menuID = 1;
        boolean testRes1 = Methods.isMenuInOrder(menuID);

        assertEquals(true, testRes1);

        // Test with negative menuID
        boolean testRes2 = Methods.isMenuInOrder(-1);

        assertEquals(false, testRes2);

        // Test with menuID which is not in order
        menuID = 10;
        boolean testRes3 = Methods.isMenuInOrder(menuID);

        assertEquals(false, testRes3);
    }


    @AfterClass
    public static void after() throws Exception {
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