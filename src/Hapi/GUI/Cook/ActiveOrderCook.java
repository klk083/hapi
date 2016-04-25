package Hapi.GUI.Cook;

import Hapi.GUI.Chauffeur.OrderViewChauffeur;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 20.04.2016.
 */
public class ActiveOrderCook extends JFrame{
    private JList view;
    private JButton doneButton;
    private JButton removeButton;
    private JButton backButton;
    private JButton lookAtOrderButton;
    private JLabel loggedInAs;
    private JPanel panel;

    int employeeID = Methods.getID();

    private DefaultListModel model = new DefaultListModel();
    ArrayList<String> list = Methods.listOrdersForCookCourse(employeeID);

    public ActiveOrderCook(boolean isCourse) {
        super("eFood");
        loggedInAs.setText("Active orders for: "+ Methods.getEmployeeName(employeeID));
        setContentPane(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        list = Methods.listOrdersForCookCourse(employeeID);
        for (int i=0;i<list.size();i++) {
            model.addElement(list.get(i));
        }
        view.setModel(model);

        removeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(view.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to remove");
                } else {
                    if (Methods.removeOrderFromCookCourse(Integer.parseInt(list.get(view.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order removed");
                        ActiveOrderCook temp = new ActiveOrderCook(isCourse);
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not removed");
                    }
                }
            }
        });
        doneButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(view.isSelectionEmpty()) {
                    showMessageDialog(null,"You didnt select any orders to set ready");
                } else {
                    if (Methods.setOrderToReadyCourse(Integer.parseInt(list.get(view.getSelectedIndex())), employeeID,isCourse)) {
                        showMessageDialog(null, "Order set to ready");
                        ActiveOrderCook temp = new ActiveOrderCook(isCourse);
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not set to ready");
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderViewCook temp = new OrderViewCook(isCourse);
                dispose();
            }
        });
        lookAtOrderButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(view.isSelectionEmpty() ) {
                    showMessageDialog(null, "You forgot to select an order");
                }
                else {
                    ArrayList<ArrayList<String>> listMenues =
                            Methods.listMenusInOrder(Integer.parseInt(list.get(view.getSelectedIndex())));

                    DetailOrder viewOrder = new DetailOrder(Integer.parseInt(list.get(view.getSelectedIndex())),listMenues);
                }
            }
        });
    }


}


