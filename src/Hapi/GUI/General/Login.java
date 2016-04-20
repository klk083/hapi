package Hapi.GUI.General;


import Hapi.GUI.Chauffeur.OrderViewChauffeur;
import Hapi.GUI.Cook.OrderViewCook;
import Hapi.GUI.MainMenu.CEO;
import Hapi.GUI.MainMenu.Expert;
import Hapi.GUI.MainMenu.Sale;
import Hapi.SQLMethods.Methods;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        super("eFood");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField user = textField2;
        JPasswordField pass = passwordField1;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);




        signInButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {



                if(signIn(user.getText(),pass.getText())) {

                } else {
                    showMessageDialog(null, "Feil brukernavn eller passord.");
                }


            }
        });
        passwordField1.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(signIn(user.getText(),pass.getText())) {

                } else {
                    showMessageDialog(null, "Feil brukernavn eller passord.");
                }
            }
        });
        user.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(signIn(user.getText(),pass.getText())) {

                } else {
                    showMessageDialog(null, "Feil brukernavn eller passord.");
                }
            }
        });
    }
    public boolean signIn(String userN,String password){
        if(Methods.login(userN, password)) {
            switch (Methods.getRoleID(userN)) {
                case 1:
                    CEO ceo = new CEO();
                    break;
                case 2:
                    ceo = new CEO();
                    break;
                case 3:
                    OrderViewCook cook = new OrderViewCook();
                    break;
                case 4:
                    OrderViewChauffeur chauffeur = new OrderViewChauffeur();
                    break;
                case 5:
                    Sale sale = new Sale();
                    break;
                case 6:
                    Expert expert = new Expert();
                    break;
            }
            dispose();
            return true;
        }
        else {
            return false;
        }
    }
}

