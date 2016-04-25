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
    public void getOrderInfo() throws Exception {
        int orderID = 1;
        String expOrderID = "1", expCustomerID = "2", expDelivery = "2008-11-11", expReady = "true";
        ArrayList<String> search = Methods.getOrderInfo(orderID);

        assertEquals(expOrderID, search.get(0));
        assertEquals(expCustomerID, search.get(1));
        assertEquals(expDelivery, search.get(2));
        assertEquals(expReady, search.get(3));


        // Test with negative orderID
        orderID = -1;
        search = Methods.getOrderInfo(orderID);

        assertEquals(null, search);
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

    @Test
    public void changeIngredient() throws Exception {
        String name = "Ost", unit = "g", ingredientID = "2";
        int price = 2;
        boolean testRes1 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(true, testRes1);

        // Test negative price
        price = -1;
        boolean testRes2 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(false, testRes2);

        // Test blank name
        price = 1;
        name = "";
        boolean testRes3 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(false, testRes3);

        // Test blank unit
        name = "Ost";
        unit = "";
        boolean testRes4 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(false, testRes4);

        // Test blank ingredientID
        unit = "g";
        ingredientID = "";
        boolean testRes5 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(false, testRes5);

        // Test negative ingredientID
        ingredientID = "-1";
        boolean testRes6 = Methods.changeIngredient(ingredientID, name, price, unit);

        assertEquals(false, testRes6);
    }

    @Test
    public void getIngredientInfo() throws Exception {
        String ingredientID = "1", expName = "Burger", expUnit = "stk", expPrice = "100";
        ArrayList<String> search = Methods.getIngredientInfo(ingredientID);


        // Test for correct name
        assertEquals(expName, search.get(0));
        // Test for correct ingredientID
        assertEquals(ingredientID, search.get(1));
        // Test for correct unit
        assertEquals(expUnit, search.get(2));
        // Test for correct price
        assertEquals(expPrice, search.get(3));

    }

    @Test
    public void listSubs() throws Exception {
        // Tests both listSubs-methods

        // Test for listSubs(String part1Name)
        String name = "Burger Abonnement";
        int expID = 1;
        ArrayList<ArrayList<String>> search = Methods.listSubscriptions(name);

        assertEquals(name, search.get(0).get(0));
        assertEquals(expID, Integer.parseInt(search.get(1).get(0)));


        // Test for listSubs(int customerID)
        int customerID = 1, expID2 = 1;
        ArrayList<Integer> search2 = Methods.listSubs(customerID);
        int testRes = search2.get(0);

        assertEquals(expID2, testRes);


        // Test listSubs(int customerID) with negative ID
        customerID = -1;
        search2 = Methods.listSubs(customerID);

        assertEquals(null, search2);
    }

    @Test
    public void changeMenu() throws Exception {
        int menuID = 1, price = 1;
        String name = "Dummymat", description = "Dummy";
        boolean testRes1 = Methods.changeMenu(menuID, name, price, description);

        assertEquals(true, testRes1);

        // Test with negative menuID
        menuID = -1;
        boolean testRes2 = Methods.changeMenu(menuID, name, price, description);

        assertEquals(false, testRes2);

        // Test with negative price
        menuID = 1;
        price = -1;

        boolean testRes3 = Methods.changeMenu(menuID, name, price, description);

        assertEquals(false, testRes3);

        // Test with blank name
        price = 1;
        name = "";

        boolean testRes4 = Methods.changeMenu(menuID, name, price, description);

        assertEquals(false, testRes4);

        // Test with blank description
        name = "Dummymat";
        description = "";

        boolean testRes5 = Methods.changeMenu(menuID, name, price, description);

        assertEquals(false, testRes5);
    }

    @Test
    public void getMenuInfo() throws Exception {
        String expName = "Dummymat", expDescription = "Dummy", expPrice = "1", expID = "1";
        int menuID = 1;
        ArrayList<String> search = Methods.getMenuInfo(menuID);

        assertEquals(expName, search.get(0));
        assertEquals(expDescription, search.get(1));
        assertEquals(expPrice, search.get(2));
        assertEquals(expID, search.get(3));

        // Test with negative menuID
        menuID = -1;
        search = Methods.getMenuInfo(menuID);

        assertEquals(null, search);
    }

    @Test
    public void addIngredientToMenu() throws Exception {
        int menuID = 1, ingredientID = 1, quantity = 1;
        boolean testRes1 = Methods.addIngredientToMenu(menuID, ingredientID, quantity);

        assertEquals(true, testRes1);

        // Remove addition
        boolean testRes2 = Methods.removeIngredientFromMenu(menuID, ingredientID);

        assertEquals(true, testRes2);


        // Test with negative menuID
        menuID = -1;
        boolean testRes3 = Methods.addIngredientToMenu(menuID, ingredientID, quantity);

        assertEquals(false, testRes3);

        // Test with negative ingredientID
        menuID = 1;
        ingredientID = -1;
        boolean testRes4 = Methods.addIngredientToMenu(menuID, ingredientID, quantity);

        assertEquals(false, testRes4);

        // Test with negative quantity
        ingredientID = 1;
        quantity = -1;
        boolean testRes5 = Methods.addIngredientToMenu(menuID, ingredientID, quantity);

        assertEquals(false, testRes5);
    }

    @Test
    public void removeIngredientFromMenu() throws Exception {
        int menuID = 1, ingredientID = 1, quantity = 1;
        boolean testRes1 = Methods.addIngredientToMenu(menuID, ingredientID, quantity);

        assertEquals(true, testRes1);

        // Test of removal of ingredient from menu
        boolean testRes2 = Methods.removeIngredientFromMenu(menuID, ingredientID);

        assertEquals(true, testRes2);


        // Test of removal with negative menuID
        menuID = -1;
        boolean testRes3 = Methods.removeIngredientFromMenu(menuID, ingredientID);

        assertEquals(false, testRes3);

        // Test of removal with negative ingredientID
        menuID = 1;
        ingredientID = -1;
        boolean testRes4 = Methods.removeIngredientFromMenu(menuID, ingredientID);

        assertEquals(false, testRes4);
    }

    @Test
    public void createSub() throws Exception {
        String name = "Test subscription", description = "Test";
        int price = 100;
        int testRes1 = Methods.createSub(name, description, price);
        ArrayList<ArrayList<String>> search = Methods.listSubscriptions(name);
        int expRes = Integer.parseInt(search.get(1).get(0));

        assertEquals(expRes, testRes1);

        // Remove test subscription
        boolean testRes2 = Methods.deleteSub(expRes);

        assertEquals(true, testRes2);

        // Test with blank name
        name = "";
        expRes = -1;
        int testRes3 = Methods.createSub(name, description, price);

        assertEquals(expRes, testRes3);

        // Test with blank description
        name = "Test Subscription";
        description = "";
        int testRes4 = Methods.createSub(name, description, price);

        assertEquals(expRes, testRes4);

        // Test with negative price
        description = "Test";
        price = -1;
        int testRes5 = Methods.createSub(name, description, price);

        assertEquals(expRes, testRes5);
    }

    @Test
    public void deleteSub() throws Exception {
        // Create subscription to delete
        String name = "Test subscription", description = "Test";
        int price = 100;
        int testRes1 = Methods.createSub(name, description, price);
        ArrayList<ArrayList<String>> search = Methods.listSubscriptions(name);
        int expRes = Integer.parseInt(search.get(1).get(0));

        assertEquals(expRes, testRes1);

        // Remove test subscription
        boolean testRes2 = Methods.deleteSub(expRes);

        assertEquals(true, testRes2);

        // Test with negative subscriptionID
        int subscriptionID = -1;
        boolean testRes3 = Methods.deleteSub(subscriptionID);

        assertEquals(false, testRes3);

    }

    @Test
    public void getSubInfo() throws Exception {
        int subscriptionID = 1;
        String expName = "Burger Abonnement", expDescription = "For den som vil automatisk få levert burger", expPrice = "100", expCostPrice = "100";
        ArrayList<String> search = Methods.getSubInfo(subscriptionID);

        assertEquals(expName, search.get(0));
        assertEquals(expDescription, search.get(1));
        assertEquals(expPrice, search.get(2));
        assertEquals(expCostPrice, search.get(3));


        // Test with negative subscriptionID
        subscriptionID = -1;
        search = Methods.getSubInfo(subscriptionID);

        assertEquals(null, search);
    }

    @Test
    public void addMenuToSub() throws Exception {
        int menuID = 2, subID = 1, quantity = 2;
        boolean testRes1 = Methods.addMenuToSub(menuID, subID, quantity);

        assertEquals(true, testRes1);


        // Remove test addition
        boolean testRes2 = Methods.removeMenuFromSub(menuID, subID);

        assertEquals(true, testRes2);


        // Test with negative menuID
        menuID = -1;
        boolean testRes3 = Methods.addMenuToSub(menuID, subID, quantity);

        assertEquals(false, testRes3);


        // Test with negative subID
        menuID = 2;
        subID = -1;
        boolean testRes4 = Methods.addMenuToSub(menuID, subID, quantity);

        assertEquals(false, testRes4);


        // Test with negative quantity
        subID = 1;
        quantity = -1;
        boolean testRes5 = Methods.addMenuToSub(menuID, subID, quantity);

        assertEquals(false, testRes5);
    }

    @Test
    public void removeMenuFromSub() throws Exception {
        // Add menu to remove
        int menuID = 2, subID = 1, quantity = 2;
        boolean testRes1 = Methods.addMenuToSub(menuID, subID, quantity);

        assertEquals(true, testRes1);


        // Remove menu
        boolean testRes2 = Methods.removeMenuFromSub(menuID, subID);

        assertEquals(true, testRes2);


        // Test with negative menuID
        menuID = -1;
        boolean testRes3 = Methods.removeMenuFromSub(menuID, subID);

        assertEquals(false, testRes3);


        // Test with negative subID
        menuID = 2;
        subID = -1;
        boolean testRes4 = Methods.removeMenuFromSub(menuID, subID);

        assertEquals(false, testRes4);
    }

    @Test
    public void addSubtoCustomer() throws Exception {
        int subID = 1, customerID = 1;
        String fromTime = "2016-03-10", toTime = "2016-05-10";
        ArrayList<Boolean> deliveryDays = new ArrayList<Boolean>();
        for (int i = 1; i < 7; i++) {
            deliveryDays.add(true);
        }

        boolean testRes1 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(true, testRes1);


        // Remove test sub from customer
        boolean testRes2 = Methods.removeSubFromCustomer(subID, customerID);

        assertEquals(true, testRes2);


        // Test with negative subID
        subID = -1;
        boolean testRes3 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(false, testRes3);


        // Test with negative customerID
        subID = 1;
        customerID = -1;
        boolean testRes4 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(false, testRes4);


        // Test with blank fromTime
        customerID = 1;
        fromTime = "";
        boolean testRes5 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(false, testRes5);


        // Test with blank toTime
        fromTime = "2016-03-10";
        toTime = "";
        boolean testRes6 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(false, testRes6);


        // Test with deliveryDays less than 7 items
        toTime = "2016-05-10";
        deliveryDays.remove(true);
        boolean testRes7 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(false, testRes7);
    }

    @Test
    public void removeSubFromCustomer() throws Exception {
        // Create test addition
        int subID = 1, customerID = 1;
        String fromTime = "2016-03-10", toTime = "2016-05-10";
        ArrayList<Boolean> deliveryDays = new ArrayList<Boolean>();
        for (int i = 1; i < 7; i++) {
            deliveryDays.add(true);
        }

        boolean testRes1 = Methods.addSubToCustomer(subID, customerID, fromTime, toTime, deliveryDays);

        assertEquals(true, testRes1);


        // Remove test sub from customer
        boolean testRes2 = Methods.removeSubFromCustomer(subID, customerID);

        assertEquals(true, testRes2);


        // Test with negative subID
        subID = -1;
        boolean testRes3 = Methods.removeSubFromCustomer(subID, customerID);

        assertEquals(false, testRes3);


        // Test with negative customerID
        subID = 1;
        customerID = -1;
        boolean testRes4 = Methods.removeSubFromCustomer(subID, customerID);

        assertEquals(false, testRes4);
    }

    @Test
    public void listCoursesInSub() throws Exception {
        int subID = 1;
        ArrayList<ArrayList<String>> search = Methods.listCoursesInSub(subID);
        String expName = "Dummymat", expID = "1";

        assertEquals(expName, search.get(0).get(0));
        assertEquals(expID, search.get(1).get(0));


        // Test with negative subID
        search = Methods.listCoursesInSub(subID);

        assertEquals(null, search);
    }

    @Test
    public void listOrdersForDelivery() throws Exception {
        ArrayList<ArrayList<String>> search = Methods.listOrdersForDeliveries();
        String expAddress = "Bukta 12", expID = "3";

        assertEquals(expAddress, search.get(0).get(0));
        assertEquals(expID, search.get(1).get(0));
    }

    @Test
    public void listOrdersForChauffeur() throws  Exception {
        int employeeID = 2;
        ArrayList<ArrayList<String>> search = Methods.listOrdersForChauffeur(employeeID);
        String expAddress = "Bukta 12",expID = "2" ;

        assertEquals(expAddress, search.get(0).get(0));
        assertEquals(expID, search.get(1).get(0));


        // Test with negative employeeID
        search = Methods.listOrdersForChauffeur(-1);

        assertEquals(null, search);
    }

    @Test
    public void addOrderToChauffeur() throws Exception {
        int orderID = 1, employeeID = 2;
        boolean testRes1 = Methods.addOrderToChauffeur(orderID, employeeID);

        assertEquals(true, testRes1);


        // Remove test order from chauffeur

        boolean testRes2 = Methods.removeOrderFromChauffeur(orderID, employeeID);

        assertEquals(true, testRes2);


        // Test with negative orderID
        orderID = -1;
        boolean testRes3 = Methods.addOrderToChauffeur(orderID, employeeID);

        assertEquals(false, testRes3);


        // Test with negative employeeID
        orderID = 1;
        employeeID = -1;
        boolean testRes4 = Methods.addOrderToChauffeur(orderID, employeeID);

        assertEquals(false, testRes4);
    }

    @Test
    public void removeOrderFromChauffeur() throws Exception {
        int orderID = 1, employeeID = 2;
        boolean testRes1 = Methods.addOrderToChauffeur(orderID, employeeID);

        assertEquals(true, testRes1);


        // Remove test order from chauffeur

        boolean testRes2 = Methods.removeOrderFromChauffeur(orderID, employeeID);

        assertEquals(true, testRes2);


        // Test with negative orderID
        orderID = -1;
        boolean testRes3 = Methods.removeOrderFromChauffeur(orderID, employeeID);

        assertEquals(false, testRes3);


        // Test with negative employeeID
        orderID = 1;
        employeeID = -1;
        boolean testRes4 = Methods.removeOrderFromChauffeur(orderID, employeeID);

        assertEquals(false, testRes4);
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