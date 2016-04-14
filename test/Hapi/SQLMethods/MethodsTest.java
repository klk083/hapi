package Hapi.SQLMethods;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by magnu on 11.04.2016.
 */
public class MethodsTest {
    @Test
    public void createUser() throws Exception {
        String username = "test-test", password = "test", name = "Tester McTest";
        int role = 1;
        boolean test = Methods.createUser(username, password, name, role);

        assertEquals(true, test);
    }

    @Test
    public void login() throws Exception {
        boolean test = Methods.login("test-test", "test");

        assertEquals(true, test);
    }

    @Test
    public void changePassword() throws Exception {
        String username ="test-test", password = "test-test";
        boolean test = Methods.changePassword(username, password);

        assertEquals(true, test);
    }

    @Test
    public void login2() throws Exception {
        boolean test = Methods.login("test-test", "test-test");

        assertEquals(true, test);
    }

    @Test
    public void deleteUser() throws Exception {

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
    public void getRoleID() throws Exception {

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