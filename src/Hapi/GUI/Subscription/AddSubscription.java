package Hapi.GUI.Subscription;

import javax.swing.*;
import Hapi.SQLMethods.Methods;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Håkon on 19.04.2016.
 */
public class AddSubscription extends JFrame {
    private JPanel AddSubPanel;
    private JButton backButton;
    private JButton OKButton;
    private JButton searchCourseButton;
    private JTextField searchField;
    private JList existingCourses;
    private JList subscriptionCourses;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton addButton;
    private JButton removeButton;

   private DefaultListModel listModel = new DefaultListModel();

    ArrayList<ArrayList<String>> list = Methods.listIngredients("");

    public AddSubscription() {
        super("eFood");
        setContentPane(AddSubPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);


      /* searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

               list = Methods.listOrders(searchField.getText());

               listModel.removeAllElements();

                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                existingCourses.setModel(listModel);
            }
        });*/

        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                ManageSub mansub = new ManageSub();
                }
            });



        }
    }
