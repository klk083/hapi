package Hapi.GUI.User;

import Hapi.GUI.MainMenu.CEO;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.util.ArrayList;

import static Hapi.SQLMethods.Methods.deleteUser;
import static javax.swing.JOptionPane.*;

/**
 * Created by klk94 on 11.03.2016.
 */
public class ManageUsers extends JFrame{
    private JButton editPasswordButton;
    private JButton deleteUserButton;
    private JButton createUserButton;
    private JButton backButton;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel userPanel;
    private JList displayList;
    private JScrollPane scroll;


    public ManageUsers() {
        super("Manage users");
        setContentPane(userPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        searchField.setText("Search");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        ArrayList<ArrayList<String>> list = Methods.listEmployees("");


        DefaultListModel listModel = new DefaultListModel();

        //    String[] user = list;
        for (String enuser : list.get(0)) {
            listModel.addElement(enuser);
        }
        displayList.setModel(listModel);



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
                if(displayList.isSelectionEmpty() ) {
                    showMessageDialog(null, "DO ar dum din tolling");
                }
                else {
                    EditPassword edit = new EditPassword((String) displayList.getSelectedValue());
                    dispose();
                }
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(displayList.isSelectionEmpty() ) {
                    showMessageDialog(null, "DO ar dum din tolling");
                }
                else {
                    int choice = showOptionDialog(null,
                            "You really want to delete that user?",
                            "Quit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, null, null);
                    if (choice == JOptionPane.YES_OPTION) {
                        deleteUser((String) displayList.getSelectedValue());
                        dispose();
                        ManageUsers users = new ManageUsers();

                    }
                }

            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField;




                ArrayList<ArrayList<String>> list = Methods.listEmployees(text.getText());


                    DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                    for (String enuser : list.get(0)) {
                        listModel.addElement(enuser);
                    }
                    displayList.setModel(listModel);



            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField;

                ArrayList<ArrayList<String>> list = Methods.listEmployees(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                displayList.setModel(listModel);

            }
        });

        displayList.addContainerListener(new ContainerAdapter() {

        });
        displayList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });

    }

}
