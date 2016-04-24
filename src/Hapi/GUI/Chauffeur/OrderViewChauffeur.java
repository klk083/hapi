package Hapi.GUI.Chauffeur;

import Hapi.GUI.General.Login;
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
public class OrderViewChauffeur extends JFrame {
    private JButton nextButton;
    private JList viewReady;
    private JList viewOnChauffeur;
    private JButton addButton;
    private JButton removeButton;
    private JButton signOutButton;
    private JPanel View;
    private JLabel employee;


    int employeeID;
    private DefaultListModel modelReady = new DefaultListModel();
    private DefaultListModel modelChauffeur = new DefaultListModel();

    ArrayList<ArrayList<String>> listReadyOrders = Methods.listOrdersForDeliveries();
    ArrayList<ArrayList<String>> listChauffeur = Methods.listOrdersForChauffeur(employeeID);

    public OrderViewChauffeur () {
        super("eFood");
        employeeID = Methods.getID();
        employee.setText("Logged in as: "+ Methods.getEmployeeName(employeeID));
        setContentPane(View);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);

        listReadyOrders = Methods.listOrdersForDeliveries();
        for (int i=0;i<listReadyOrders.get(0).size();i++) {

            modelReady.addElement(listReadyOrders.get(1).get(i) + " - " + listReadyOrders.get(0).get(i));
        }
        viewReady.setModel(modelReady);

        listChauffeur = Methods.listOrdersForChauffeur(employeeID);

        for (int i=0;i<listChauffeur.get(0).size();i++) {

            modelChauffeur.addElement(listChauffeur.get(1).get(i) + " - " + listChauffeur.get(0).get(i));
        }
        viewOnChauffeur.setModel(modelChauffeur);
        addButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewReady.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to add");
                } else {
                    if (Methods.addOrderToChauffeur(Integer.parseInt(listReadyOrders.get(1).get(viewReady.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order added");
                        OrderViewChauffeur temp = new OrderViewChauffeur();
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not added");
                    }
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewOnChauffeur.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to remove");
                } else {
                    if (Methods.removeOrderFromChauffeur(Integer.parseInt(listChauffeur.get(1).get(viewOnChauffeur.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order removed");
                        OrderViewChauffeur temp = new OrderViewChauffeur();
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not removed");
                    }
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveOrderChauffeur temp = new ActiveOrderChauffeur();
                dispose();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Login temp = new Login();
                dispose();
            }
        });
    }



}
