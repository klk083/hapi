package Hapi.GUI.Customer;

import Hapi.GUI.User.ManageUsers;

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

    public EditCustomer(String adress, String tlfNr) {
        super("Create customer");
        setContentPane(EditCustomerPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        oldAdress.setText(adress);
        oldPhone.setText(tlfNr);
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(oldAdress.getText().equals(textField2.getText())){
                    showMessageDialog(null, "This address is already registered");
                }
                if(oldPhone.getText().equals(textField3.getText())) {
                    showMessageDialog(null, "This phonenumber is already registered");
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
