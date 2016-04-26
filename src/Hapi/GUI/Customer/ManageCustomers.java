package Hapi.GUI.Customer;

import Hapi.GUI.MainMenu.CEO;
import Hapi.GUI.MainMenu.Expert;
import Hapi.GUI.MainMenu.Sale;
import Hapi.GUI.Order.ListeElement;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Hapi.SQLMethods.Methods.deleteCustomer;
import static javax.swing.JOptionPane.MESSAGE_PROPERTY;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

/**
 * Created by klk94 on 16.03.2016.
 */
public class ManageCustomers extends JFrame {
    private JList displayList;
    private JTextField searchField1;
    private JButton searchButton;
    private JButton editButton;
    private JButton createButton;
    private JButton mainMenuButton;
    private JButton deleteButton;
    private JPanel customerPanel;


    public ManageCustomers() {

        super("Manage customers");
        setContentPane(customerPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<ArrayList<String>> list = Methods.listCustomers("");

        //    String[] user = list;
        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();


        for (int i = 0; i < list.get(1).size(); i++) {
            String name = list.get(0).get(i);
            String id = list.get(1).get(i);
            listModel.addElement(new ListeElement(id, name));
        }
        displayList.setModel(listModel);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField1;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();


                for (int i = 0; i < list.get(1).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id, name));
                }
                displayList.setModel(listModel);

            }
        });

        searchField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField1;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();


                for (int i = 0; i < list.get(1).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id, name));
                }
                displayList.setModel(listModel);
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateCustomer customer = new CreateCustomer(2);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayList.isSelectionEmpty()) {
                    showMessageDialog(null, "You need to select a customer");
                } else {
                    int choice = showOptionDialog(null,
                            "You really want to delete that customer?",
                            "Quit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, null, null);
                    if (choice == JOptionPane.YES_OPTION) {
                        ListeElement selected = (ListeElement) displayList.getSelectedValuesList().get(0);
                        deleteCustomer(Integer.parseInt(selected.getId()));
                        dispose();
                        ManageCustomers customers = new ManageCustomers();

                    }
                }

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayList.isSelectionEmpty()) {
                    showMessageDialog(null, "You need to select a customer");
                } else {
                    ArrayList<String> info = Methods.getCustomerContactInfo(Integer.parseInt(list.get(1).get(displayList.getSelectedIndex())));
                    EditCustomer edit = new EditCustomer(info.get(0), info.get(1),Integer.parseInt(list.get(1).get(displayList.getSelectedIndex())));
                    dispose();
                }
            }
        });
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idL = Methods.getRoleID(Methods.getEmployeeName(Methods.getID()));
                if(idL==1 || idL ==2){
                    dispose();
                    CEO ceo = new CEO();
                } else if(idL==5){
                    dispose();
                    Sale sale = new Sale();
                }

            }
        });
    }
}
