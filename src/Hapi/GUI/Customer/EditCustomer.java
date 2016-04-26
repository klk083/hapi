package Hapi.GUI.Customer;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 11.04.2016.
 */
public class EditCustomer extends JFrame {
    private JButton cancleButton;
    private JButton changeButton;
    private JTextField newAddressField;
    private JTextField newPhoneField;
    private JPanel editCustomerPanel;
    private JLabel oldPhone;
    private JLabel oldAdress;

    public EditCustomer(String address, String tlfNr, int customerId) {
        super("Create customer");
        setContentPane(editCustomerPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        oldAdress.setText(address);
        oldPhone.setText(tlfNr);
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(oldAdress.getText().equals(newAddressField.getText())){
                    showMessageDialog(null, "This address is already registered");
                } else {
                    Methods.setCustomerAddress(customerId, newAddressField.getText());
                }
                if(oldPhone.getText().equals(newPhoneField.getText())) {
                    showMessageDialog(null, "This phonenumber is already registered");
                } else if ((Methods.setCustomerPhone(customerId, newPhoneField.getText()) == false) && !newPhoneField.getText().equals("") ){
                   showMessageDialog(null, "This is not a phonenumber");

                }
                else {
                    Methods.setCustomerPhone(customerId, newPhoneField.getText());
                    dispose();
                    ManageCustomers customer = new ManageCustomers();
                }

            }
        });

        cancleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageCustomers customers = new ManageCustomers();
                dispose();
            }
        });

    }
}
