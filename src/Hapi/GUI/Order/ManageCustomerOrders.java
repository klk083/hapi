package Hapi.GUI.Order;

import Hapi.GUI.Customer.CreateCustomer;
import Hapi.GUI.MainMenu.CEO;
import Hapi.GUI.MainMenu.Sale;
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
    private JTextField searchField;
    private JButton searchButton;
    private JList displayList;
    private JButton createCustomerButton;
    private JButton backButton;
    private JButton nextButton;
    private JPanel manageCustomerOrderPanel;

    public ManageCustomerOrders() {
        super("eFood");
        setContentPane(manageCustomerOrderPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);


        ArrayList<ArrayList<String>> list = Methods.listCustomers("");


        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();


        for (int i = 0; i < list.get(1).size(); i++) {
            String name = list.get(0).get(i);
            String id = list.get(1).get(i);
            listModel.addElement(new ListeElement(id, name));
        }
        displayList.setModel(listModel);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idL = Methods.getRoleID(Methods.getEmployeeName(Methods.getID()));
                if(idL==1){
                    dispose();
                    CEO ceo = new CEO();
                } else if(idL==5){
                    dispose();
                    Sale sale = new Sale();
                }
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
                JTextField text = searchField;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();

                //    String[] user = list;
                for (int i = 0; i < list.get(1).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id, name));
                }
                displayList.setModel(listModel);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //int orderId = Methods.createOrder(1,"1000-01-01 00:00:00");

                if(displayList.isSelectionEmpty()) {
                    showMessageDialog(null, "You need to select a customer");
                    } else {

                    dispose();
                    ListeElement selected = (ListeElement) displayList.getSelectedValuesList().get(0);
                    ManageOrders order = new ManageOrders(selected.getName(),Integer.parseInt(selected.getId()));

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
