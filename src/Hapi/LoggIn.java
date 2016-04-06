package Hapi;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

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
        signInButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                CEO test = new CEO();
                showMessageDialog(null, "hei, " + textField2.getText());
                dispose();

            }
        });
        setVisible(true);

    }

}
