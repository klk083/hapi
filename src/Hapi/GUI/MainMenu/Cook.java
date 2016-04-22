package Hapi.GUI.MainMenu;

import Hapi.GUI.Cook.OrderViewCook;
import Hapi.GUI.Course.Courses;
import Hapi.GUI.General.Login;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 22.04.2016.
 */
public class Cook extends JFrame{
    private JButton coursesButton;
    private JButton subcriptionsButton;
    private JButton signOutButton;
    private JLabel nameL;
    private JPanel cook;

    public Cook() {

        super("eFood");
        setContentPane(cook);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));

        coursesButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderViewCook temp = new OrderViewCook(true);
                dispose();
            }
        });
        subcriptionsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderViewCook temp = new OrderViewCook(false);
                dispose();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
            }
        });
    }
}
