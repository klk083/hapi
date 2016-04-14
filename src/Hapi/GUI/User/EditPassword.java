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
    private JTextField textField1;
    private JTextField textField2;
    private JButton changeButton;
    private JButton cancelButton;
    private JPanel editPasswordPanel;
    private String userName;

    public EditPassword(String userName) {
        super("EditPassword");
        setContentPane(editPasswordPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField1.getText().equals(textField2.getText())){
                     if(changePassword(userName, textField2.getText())) {
                         showMessageDialog(null, "Password changed");
                         dispose();
                         ManageUsers users = new ManageUsers();
                     }
                }
                else showMessageDialog(null, "Kenneth sier nei");
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


