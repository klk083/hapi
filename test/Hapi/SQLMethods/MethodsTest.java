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
    // All tests are supposed to be run on a database built with our database.sql and test data given in the same file

    @Before
    public void before() throws Exception {
        String username = "testuser", password = "test", name = "Tester McTest";
        int role = 1;
        Methods.createUser(username, password, name, role);
    }


    @Test
    public void createUser() throws Exception {
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        boolean test = Methods.createUser(username, password, name, role);
        Methods.deleteUser(username);

        assertEquals(true, test);
    }

    @Test
    public void login1() throws Exception {
        boolean test = Methods.login("testuser", "test");

        assertEquals(true, test);
    }

    @Test
    public void login2() throws Exception {
        // This login should fail, unless someone explicitly created this user before running the test
        boolean test = Methods.login("xxxxxxxxxx", "xxxxxxxxxxx");

        assertEquals(false, test);
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
    public void login3() throws Exception {
        // This login tries to login with wrong password for username "testuser"
        boolean test = Methods.login("testuser", "12345678");

        assertEquals(false, test);
    }

    @Test
    public void login4() throws Exception {
        String username = "tester", password = "test", name = "Tester McTest";
        int role = 1;
        Methods.createUser(username, password, name, role);

        String password2 = "test-test";
        Methods.changePassword(username, password2);

        boolean testRes = Methods.login(username, password2);

        Methods.deleteUser(username);
        assertEquals(true, testRes);
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
        Methods.createUser(username, password, name, role);

        boolean testRes = Methods.deleteUser(username);

        assertEquals(true, testRes);
    }

    @Test
    public void createCustomer1() throws Exception {
        String name = "Billy Bob", address = "Bobgata 4", tlf = "12345678";
        boolean isCompany = false;

        boolean testRes = Methods.createCustomer(name, address, tlf, isCompany);


    }

    @Test
    public void createCustomer2() throws Exception {

    }

    @Test
    public void listCustomers() throws Exception {
        String name = "Billy Bob", address = "Bobgata 4", tlf = "12345678";
        boolean isCompany = false;

        Methods.createCustomer(name, address, tlf, isCompany);

        ArrayList<ArrayList<String>> testRes = Methods.listCustomers(name);

        assertEquals("Billy Bob", testRes.get(0).get(0));
    }

    @Test
    public void setCustomerDiscount() throws Exception {

    }

    @Test
    public void createOrder() throws Exception {

    }

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



    @After
    public void after() throws Exception {
        String username = "testuser";
        Methods.deleteUser(username);
    }
}