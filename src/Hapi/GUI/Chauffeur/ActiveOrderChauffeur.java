package Hapi.GUI.Chauffeur;

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



public class ActiveOrderChauffeur extends JFrame {
    private JPanel ActiveOrderW;
    private JList view;
    private JButton doneButton;
    private JButton backButton;
    private JButton removeButton;
    private JLabel loggedinas;

    int employeeID = Methods.getID();

    private DefaultListModel model = new DefaultListModel();
    ArrayList<ArrayList<String>> list = Methods.listOrdersForChauffeur(employeeID);

    public ActiveOrderChauffeur() {
        super("eFood");
        this.employeeID = employeeID;
        loggedinas.setText("Active orders for: "+ Methods.getEmployeeName(employeeID));
        setContentPane(ActiveOrderW);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        list = Methods.listOrdersForChauffeur(employeeID);
        for (int i=0;i<list.get(0).size();i++) {

            model.addElement(list.get(1).get(i) + " - " + list.get(0).get(i));
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
                    if (Methods.removeOrderFromChauffeur(Integer.parseInt(list.get(1).get(view.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order removed");
                        ActiveOrderChauffeur temp = new ActiveOrderChauffeur();
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
                    showMessageDialog(null,"You didnt select any orders to set deliver");
                } else {
                    if (Methods.setOrderToDelivered(Integer.parseInt(list.get(1).get(view.getSelectedIndex())), employeeID)) {
                        showMessageDialog(null, "Order derlivered");
                        ActiveOrderChauffeur temp = new ActiveOrderChauffeur();
                        dispose();
                    } else {
                        showMessageDialog(null, "Something went wrong, order not delivered");
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
                OrderViewChauffeur temp = new OrderViewChauffeur();
                dispose();
            }
        });
    }


}
