package Hapi.GUI.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Knut on 11.04.2016.
 */
public class EditCustomer extends JFrame {
    private JButton cancleButton;
    private JButton changeButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel EditCustomerPannel;

    public EditCustomer(String selectedValue) {
        super("Create customer");
        setContentPane(EditCustomerPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        cancleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageCustomers customers = new ManageCustomers();
                dispose();
            }
        });

    }
}
