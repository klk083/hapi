package Hapi.GUI.General;


import Hapi.GUI.MainMenu.CEO;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 10.03.2016.
 */
public class Login extends JFrame {
    private JPanel panel1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JButton signInButton;

    public Login() {
        super("Sign In");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField user = textField2;
        JPasswordField pass = passwordField1;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);



        signInButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {



                if(Methods.login(user.getText(), pass.getText())) {

                    CEO test = new CEO();
                    dispose();
                }
                else {
                    showMessageDialog(null, "Feil brukernavn eller passord.");
                }

            }
        });
        setVisible(true);

        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                if(Methods.login(user.getText(), pass.getText())) {

                    CEO test = new CEO();
                    dispose();
                }
                else {
                    showMessageDialog(null, "Feil brukernavn eller passord.");
                }

            }
        });
    }

}
