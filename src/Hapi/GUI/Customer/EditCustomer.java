package Hapi.GUI.Customer;

import Hapi.GUI.Order.ManageCustomerOrders;
import Hapi.GUI.Order.ManageOrders;
import Hapi.GUI.User.ManageUsers;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Hapi.SQLMethods.Methods.changePassword;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 11.04.2016.
 */
public class EditCustomer extends JFrame {
    private JButton cancleButton;
    private JButton changeButton;
    private JTextField textField2;
    private JTextField textField3;
    private JPanel EditCustomerPannel;
    private JLabel oldPhone;
    private JLabel oldAdress;

    public EditCustomer(String address, String tlfNr, int customerId) {
        super("Create customer");
        setContentPane(EditCustomerPannel);
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
                if(oldAdress.getText().equals(textField2.getText())){
                    showMessageDialog(null, "This address is already registered");
                } else {
                    Methods.setCustomerAddress(customerId, textField2.getText());
                }
                if(oldPhone.getText().equals(textField3.getText())) {
                    showMessageDialog(null, "This phonenumber is already registered");
                } else if ((Methods.setCustomerPhone(customerId, textField3.getText()) == false) && !textField3.getText().equals("") ){
                   showMessageDialog(null, "This is not a phonenumber");

                }
                else {
                    Methods.setCustomerPhone(customerId, textField3.getText());
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
