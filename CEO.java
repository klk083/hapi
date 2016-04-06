package Hapi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 11.03.2016.
 */
public class CEO extends JFrame {
    private JButton manageUsersButton;
    private JButton manageTablesButton;
    private JButton manageCustomersButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JButton lookAtOrdersButton;
    private JButton changeOrderStatusButton;
    private JCheckBox checkBox4;
    private JPanel CEO;

    public CEO() {
        super("Sign In");
        setContentPane(CEO);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        manageTablesButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


}
