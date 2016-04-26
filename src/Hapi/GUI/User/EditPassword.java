package Hapi.GUI.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Hapi.SQLMethods.Methods.changePassword;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 11.03.2016.
 */
public class EditPassword extends JFrame {
    private JTextField passwordField;
    private JTextField repeatPasswordField;
    private JButton changeButton;
    private JButton cancelButton;
    private JPanel editPasswordPanel;
    private JLabel userN;


    public EditPassword(String username) {
        super("EditPassword");
        userN.setText("Username: " + username);
        setContentPane(editPasswordPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(passwordField.getText().equals(repeatPasswordField.getText())){
                     if(changePassword(username, repeatPasswordField.getText())) {
                         showMessageDialog(null, "Password changed");
                         dispose();
                         ManageUsers users = new ManageUsers();
                     }
                }
                else showMessageDialog(null, "Invalid");
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageUsers users = new ManageUsers();
            }
        });
    }
}


