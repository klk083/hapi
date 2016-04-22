package Hapi.GUI.MainMenu;

import Hapi.GUI.Course.Courses;
import Hapi.GUI.General.Login;
import Hapi.GUI.Subscription.ManageSub;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 16.03.2016.
 */
public class Expert extends JFrame {
        private JButton coursesButton;
        private JButton subscriptionsButton;
        private JLabel nameL;
    private JPanel expert;
    private JButton signOut;

    public Expert() {
            super("eFood");
            setContentPane(expert);
            pack();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            setVisible(true);
            nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));
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
        subscriptionsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageSub sub = new ManageSub();
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
                dispose();
                Login login = new Login();
            }
        });
    }

}
