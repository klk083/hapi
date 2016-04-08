package Hapi.GUI.User;

import Hapi.GUI.MainMenu.CEO;
import Hapi.GUI.User.CreateUser;
import Hapi.GUI.User.EditPassword;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 11.03.2016.
 */
public class ManageUsers extends JFrame{
    private JButton editPasswordButton;
    private JButton deleteUserButton;
    private JButton createUserButton;
    private JButton backButton;
    private JTextField textf1;
    private JButton searchButton;
    private JPanel Userpannel;
    private JList list1;
    private JScrollPane scroll;

    public ManageUsers() {
        super("Manage users");
        setContentPane(Userpannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        ArrayList<String> list = Methods.listEmployees();


        DefaultListModel listModel = new DefaultListModel();

        //    String[] user = list;
        for (String enuser : list) {
            listModel.addElement(enuser);
        }
        list1.setModel(listModel);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CEO ceo = new CEO();
            }
        });
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateUser user = new CreateUser();

            }
        });
        editPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty() ) {
                    showMessageDialog(null, "DO ar dum din tolling");
                }
                else {
                    EditPassword edit = new EditPassword((String) list1.getSelectedValue());
                    dispose();
                }
            }
        });
        textf1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textf1;




                ArrayList<String> list = Methods.listEmployees(text.getText());


                    DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                    for (String enuser : list) {
                        listModel.addElement(enuser);
                    }
                    list1.setModel(listModel);



            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textf1;

                ArrayList<String> list = Methods.listEmployees(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);

            }
        });

        list1.addContainerListener(new ContainerAdapter() {

        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });


    }

}
