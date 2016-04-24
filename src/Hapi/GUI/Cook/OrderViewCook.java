package Hapi.GUI.Cook;

import Hapi.GUI.MainMenu.Cook;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 19.04.2016.
 */

public class OrderViewCook extends JFrame{
    private JList viewOrders;
    private JList viewOrdersOnCook;
    private JButton addButton;
    private JButton removeButton;
    private JButton mainMenu;
    private JButton continueButton;
    private JPanel orderViewCook;
    private JLabel nameL;
    private int employeeID;

    ArrayList<String> listOrders;
    ArrayList<String> listCook;


    public OrderViewCook(boolean isCourse) {
        super("eFood");
        setContentPane(orderViewCook);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);
        employeeID=Methods.getID();
        nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));

        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        if(isCourse) {
            listOrders = Methods.listOrdersForCourses();
            listCook = Methods.listOrdersForCookCourse(employeeID);
        } else {

        }

        for (int i = 0; i< listOrders.size(); i++) {

            model1.addElement(listOrders.get(i));
        }
        viewOrders.setModel(model1);

        for (int i = 0; i< listCook.size(); i++) {

            model2.addElement(listCook.get(i));
        }
        viewOrdersOnCook.setModel(model2);




        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewOrders.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to add");
                } else {
                    if (Methods.addOrderToCookCourse(Integer.parseInt(listOrders.get(viewOrders.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order added");
                        OrderViewCook temp = new OrderViewCook(isCourse);
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not added");
                    }
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewOrdersOnCook.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to remove");
                } else {
                    if (Methods.removeOrderFromCookCourse(Integer.parseInt(listCook.get(viewOrdersOnCook.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order removed");
                        OrderViewCook temp = new OrderViewCook(isCourse);
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not removed");
                    }
                }
            }
        });
        continueButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveOrderCook temp = new ActiveOrderCook(isCourse);
                dispose();
            }
        });
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Cook cook = new Cook();
            }
        });

    }
}
