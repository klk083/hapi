package Hapi.SQLMethods;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by magnu on 11.04.2016.
 */
public class MethodsTest {
    // All tests are supposed to be run on a database built with our database.sql and test data given in the same file

    @Test
    public void createUser() throws Exception {
        String username = "test-test", password = "test", name = "Tester McTest";
        int role = 1;
        boolean test = Methods.createUser(username, password, name, role);

        assertEquals(true, test);
    }

    @Test
    public void login1() throws Exception {
        boolean test = Methods.login("test-test", "test");

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
        String username ="test-test", password = "test-test";
        boolean test = Methods.changePassword(username, password);

        assertEquals(true, test);
    }

    @Test
    public void login3() throws Exception {
        // This login tries to login with old password for username "test-test"
        boolean test = Methods.login("test-test", "test");

        assertEquals(false, test);
    }

    @Test
    public void login4() throws Exception {
        boolean test = Methods.login("test-test", "test-test");

        assertEquals(true, test);
    }

    @Test
    public void listEmployees1() throws Exception {
        // This test checks if user is found in the database
        String username = "test-test";

        ArrayList<ArrayList<String>> list = Methods.listEmployees(username);

        assertEquals("test-test", list.get(0).get(0));
    }

    @Test
    public void listEmployees2() throws Exception {
        // This test checks if a users employee_id is found in the database
        String username = "test-test";

        ArrayList<ArrayList<String>> list = Methods.listEmployees(username);
        int temp = Integer.parseInt(list.get(1).get(0));
        assertEquals(2, temp);      // User made in tests gets ID no. 2, this is because admin-user always gets ID no. 1
    }

    @Test
    public void getRoleID() throws Exception {
        String username = "test-test";

        int temp = Methods.getRoleID(username);

        assertEquals(1, temp);
    }

    @Test
    public void deleteUser() throws Exception {
        String username = "test-test";
        boolean test = Methods.deleteUser(username);

        assertEquals(true, test);
    }

    @Test
    public void listCustomers() throws Exception {

    }


    @Test
    public void listEmployees() throws Exception {

    }

    @Test
    public void createCustomer() throws Exception {

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

}