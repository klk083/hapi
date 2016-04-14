package Hapi.GUI.MainMenu;

import Hapi.GUI.Course.Courses;
import Hapi.GUI.Customer.ManageCustomers;
import Hapi.GUI.General.Login;
import Hapi.GUI.General.ManageOrders;
import Hapi.GUI.User.ManageUsers;

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

    public CEO() {
        super("Sign In");
        setContentPane(CEO);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

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
                ManageOrders customers = new ManageOrders();
            }
        });
        signOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
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
    }



 
}

