package Hapi.GUI.Order;

import Hapi.GUI.Customer.CreateCustomer;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 19.04.2016.
 */
public class ManageCustomerOrders extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JList list1;
    private JButton createCustomerButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JButton backButton;
    private JButton nextButton;
    private JPanel manageCustomerOrderPannel;

    public ManageCustomerOrders() {
        super("eFood");
        setContentPane(manageCustomerOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);


        ArrayList<ArrayList<String>> list = Methods.listCustomers("");


        DefaultListModel listModel = new DefaultListModel();


        for (String enCourse : list.get(0)) {
            listModel.addElement(enCourse);
        }
        list1.setModel(listModel);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageOrders orders = new ManageOrders();
            }
        });


        searchButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enCourse : list.get(0)) {
                    listModel.addElement(enCourse);
                }
                list1.setModel(listModel);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty() && (!checkBox1.isSelected() || !checkBox2.isSelected() )) {
                    showMessageDialog(null, "DO ar dum din tolling");
                } else if (checkBox1.isSelected() ){
                    dispose();
                    AddOrder order = new AddOrder((String) list1.getSelectedValue());
                }
                else if( checkBox2.isSelected()){
                    dispose();
                    AddSubscription order = new AddSubscription((String) list1.getSelectedValue());

                }
                }

        });
        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateCustomer customer = new CreateCustomer(3);
            }
        });
    }
}
