package Hapi.GUI.MainMenu;

import Hapi.GUI.Customer.ManageCustomers;
import Hapi.GUI.General.Login;
import Hapi.GUI.Order.ManageCustomerOrders;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 16.03.2016.
 */
public class Sale extends JFrame {
    private JButton customersButton;
    private JButton orders;
    private JButton statisitcs;
    private JButton signOut;
    private JPanel sale;
    private JLabel nameL;

    public Sale() {
        super("eFood");
        setContentPane(sale);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);
        nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));
        orders.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomerOrders customers = new ManageCustomerOrders();
            }
        });
        customersButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomers customers = new ManageCustomers();
            }
        });
        signOut.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                dispose();
            }
        });
    }
}
