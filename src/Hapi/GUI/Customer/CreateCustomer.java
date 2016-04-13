package Hapi.GUI.Customer;

import Hapi.GUI.User.ManageUsers;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 13.03.2016.
 */
public class CreateCustomer extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton cancelButton;
    private JButton OKButton;
    private JPanel CreateCustomerpannel;

    public CreateCustomer() {
        super("Create customer");
        setContentPane(CreateCustomerpannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField name = textField1;
                JTextField address = textField2;
                JTextField phone = textField3;

                if (Methods.createCustomer(name.getText(), address.getText(), phone.getText())
                ) {
                    showMessageDialog(null, "Customer is registered: " + name.getText());
                    dispose();
                    ManageCustomers customers = new ManageCustomers();
                } else {
                    showMessageDialog(null, "Invalid");
                }

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomers customers = new ManageCustomers();
            }
        });
    }




}


