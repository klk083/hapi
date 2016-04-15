package Hapi.GUI.Customer;

import Hapi.GUI.Order.ManageOrders;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
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
    private JCheckBox companyCheckBox;

    public CreateCustomer(int back) {
        super("Create customer");
        setContentPane(CreateCustomerpannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField name = textField1;
                JTextField address = textField2;
                JTextField phone = textField3;
                JCheckBox isCompany = companyCheckBox;

                if (Methods.createCustomer(name.getText(), address.getText(), phone.getText(), isCompany.isSelected())
                ) {
                   if(isCompany.isSelected() == true) {
                       showMessageDialog(null, "Customer(firm) is registered: " + name.getText());
                       dispose();
                       ManageCustomers customers = new ManageCustomers();
                   }
                    else {
                       showMessageDialog(null, "Customer is registered: " + name.getText());
                       dispose();
                       ManageCustomers customers = new ManageCustomers();
                   }
                }

                else {
                    showMessageDialog(null, "Invalid");
                }

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                switch(back) {
                    case 1: ManageOrders orders = new ManageOrders();
                        break;
                    case 2: ManageCustomers customers = new ManageCustomers();
                }

            }
        });
        companyCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }




}


