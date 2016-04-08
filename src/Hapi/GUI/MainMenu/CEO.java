package Hapi.GUI.MainMenu;

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
    private JButton ordersButton;
    private JPanel CEO;

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

    }



 
}

