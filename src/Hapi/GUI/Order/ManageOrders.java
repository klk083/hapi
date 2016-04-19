package Hapi.GUI.Order;

import Hapi.GUI.Customer.CreateCustomer;
import Hapi.GUI.MainMenu.CEO;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by klk94 on 11.03.2016.
 */
public class ManageOrders extends JFrame {
    private JList list1;
    private JTextField textField1;
    private JButton editOrderButton;
    private JButton backButton;
    private JButton searchButton;
    private JButton createNewCustomerButton;
    private JButton viewOrderButton;
    private JPanel createOrderPannel;
    private JButton createOrderButton;
    private JButton createCustomerButton;
    private JButton deleteOrderButton;

    public ManageOrders() {
        super("Manage order");
        setContentPane(createOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);



        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<String> list = Methods.listOrders(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CEO ceo = new CEO();
            }
        });

        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateCustomer customer = new CreateCustomer(1);
            }
        });
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomerOrders order = new ManageCustomerOrders();
            }
        });
    }
}
