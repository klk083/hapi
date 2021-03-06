package Hapi.GUI.MainMenu;

import Hapi.GUI.Course.Courses;
import Hapi.GUI.Customer.ManageCustomers;
import Hapi.GUI.General.Login;
import Hapi.GUI.Order.AddOrder;
import Hapi.GUI.Order.ManageCustomerOrders;
import Hapi.GUI.Order.ManageOrders;
import Hapi.GUI.Subscription.ManageSub;
import Hapi.GUI.User.ManageUsers;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 11.03.2016.
 */
public class CEO extends JFrame {
    private JButton manageUsersButton;
    private JButton customersButton;
    private JButton ordersButton;
    private JPanel CEO;
    private JButton signOut;
    private JButton coursesButton;
    private JButton subscriptionButton;
    private JLabel nameL;

    public CEO() {
        super("eFood");
        setContentPane(CEO);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));


        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageUsers user = new ManageUsers();

            }
        });

        customersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomers customers = new ManageCustomers();
            }
        });

        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomerOrders customers = new ManageCustomerOrders();
            }
        });

        coursesButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Courses courses = new Courses();
            }
        });
        subscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageSub sub = new ManageSub();
            }
        });
        signOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
            }
        });
    }



 
}

