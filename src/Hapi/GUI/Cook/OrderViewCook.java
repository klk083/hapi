package Hapi.GUI.Cook;

import Hapi.GUI.MainMenu.Cook;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    ArrayList<ArrayList<String>> listOrders;
    ArrayList<ArrayList<String>> listCook;


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

        DefaultListModel model = new DefaultListModel();
        DefaultListModel modelCook = new DefaultListModel();




        if(isCourse) {
            listOrders = Methods.listOrdersForCourses();
            listCook = Methods.listOrdersForCookCourse(employeeID);
        } else {

        }

        for (int i = 0; i< listOrders.get(2).size(); i++) {

            model.addElement(listOrders.get(1).get(i) + " - order: " + listOrders.get(2).get(i));
        }
        for (int i = 0; i< listCook.get(2).size(); i++) {

            modelCook.addElement(listOrders.get(1).get(i) + " - order: " + listOrders.get(2).get(i));
        }

        viewOrders.setModel(model);
        viewOrdersOnCook.setModel(modelCook);




        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        continueButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

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
