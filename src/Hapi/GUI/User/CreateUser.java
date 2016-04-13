package Hapi.GUI.User;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 16.03.2016.
 */
public class CreateUser extends JFrame {
    private JTextField textField1;
    private JTextField textField4;
    private JPasswordField ffsPasswordField;
    private JPasswordField passwordField2;
    private JButton cancelButton;
    private JButton OKButton;
    private JPanel CreateUserPannel;
    private JTextField textField2;
    private JLabel RoleID;

    public CreateUser() {
        super("Manage users");
        setContentPane(CreateUserPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField role = textField2;
                JTextField user = textField1;
                JTextField name = textField4;
                JPasswordField passord = ffsPasswordField;
                JPasswordField rpassord = passwordField2;





                try {
                    if (Methods.createUser(user.getText(), passord.getText(), name.getText(), Integer.parseInt(role.getText())) && passord.getText().equals(rpassord.getText()
                    )) {
                        showMessageDialog(null, "Brukeren din er registrert: " + name.getText());
                        dispose();
                        ManageUsers users = new ManageUsers();
                    } else {
                        showMessageDialog(null, "Ugyldig innfylling");
                    }
                } catch (NumberFormatException e1) {
                    showMessageDialog(null, "Invalid role");
                }



            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageUsers user = new ManageUsers();
            }
        });

    }
}
