package Hapi.GUI.Customer;

import Hapi.GUI.User.EditPassword;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Hapi.SQLMethods.Methods.deleteCustomer;
import static Hapi.SQLMethods.Methods.deleteUser;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

/**
 * Created by klk94 on 16.03.2016.
 */
public class ManageCustomers extends JFrame {
    private JList list1;
    private JTextField textField1;
    private JButton searchButton;
    private JButton editButton;
    private JButton createButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JPanel Customerpannel;


    public ManageCustomers() {

        super("Manage customers");
        setContentPane(Customerpannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        ArrayList<String> list = Methods.listCustomers("");


        DefaultListModel listModel = new DefaultListModel();

        //    String[] user = list;
        for (String enuser : list) {
            listModel.addElement(enuser);
        }
        list1.setModel(listModel);



        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<String> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);

            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<String> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateCustomer customer = new CreateCustomer();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty() ) {
                    showMessageDialog(null, "DO ar dum din tolling");
                }
                else {
                    int choice = showOptionDialog(null,
                            "You really want to delete that customer?",
                            "Quit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, null, null);
                    if (choice == JOptionPane.YES_OPTION) {
                        deleteCustomer((String) list1.getSelectedValue());
                        dispose();
                        ManageCustomers customers = new ManageCustomers();

                    }
                }

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty() ) {
                    showMessageDialog(null, "DO ar dum din tolling");
                }
                else {
                    EditCustomer edit = new EditCustomer((String) list1.getSelectedValue());
                    dispose();
                }
            }
        });
    }


}
