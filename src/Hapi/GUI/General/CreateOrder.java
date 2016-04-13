package Hapi.GUI.General;

import Hapi.GUI.Customer.CreateCustomer;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by klk94 on 11.03.2016.
 */
public class CreateOrder extends JFrame {
    private JList list1;
    private JTextField textField1;
    private JButton createMenyOrderButton;
    private JButton backButton;
    private JButton searchButton;
    private JButton createNewCustomerButton;
    private JButton createSubOrderButton;
    private JPanel createOrderPannel;

    public CreateOrder() {
        super("Create order");
        setContentPane(createOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        ArrayList<ArrayList<String>> list = Methods.listCustomers("");


        DefaultListModel listModel = new DefaultListModel();

        //    String[] user = list;
        for (String enuser : list.get(0)) {
            listModel.addElement(enuser);
        }
        list1.setModel(listModel);

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageOrders orders = new ManageOrders();
            }
        });
        createNewCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateCustomer customer = new CreateCustomer();
            }
        });
        createSubOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        createMenyOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
