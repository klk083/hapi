package Hapi;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 10.03.2016.
 */
public class LoggIn extends JFrame {
    private JPanel panel1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JButton signInButton;

    public LoggIn() {
        super("Sign In");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField user = textField2;
        JPasswordField pass = passwordField1;

        signInButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {



                if(Methods2.login(user.getText(), pass.getText())) {

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



                if(Methods2.login(user.getText(), pass.getText())) {

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
