package Hapi.SQLMethods;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by magnu on 11.04.2016.
 */
public class MethodsTest {
    @Test
    public void login() throws Exception {
        System.out.println("login");
        boolean test = Methods.login("admin", "admin");

        assertEquals(true, test);
    }

    @Test
    public void changePassword() throws Exception {

    }

    @Test
    public void createUser() throws Exception {

    }

    @Test
    public void deleteUser() throws Exception {

    }

    @Test
    public void listCustomers() throws Exception {

    }

    @Test
    public void listCustomers1() throws Exception {

    }

    @Test
    public void listEmployees() throws Exception {

    }

    @Test
    public void listEmployees1() throws Exception {

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