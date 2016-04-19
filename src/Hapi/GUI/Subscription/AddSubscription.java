package Hapi.GUI.Subscription;

import javax.swing.*;
import Hapi.SQLMethods.Methods;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.IllegalFormatException;

import static javax.swing.JOptionPane.showMessageDialog;

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
    private JTextField quantity;
    private JTextField pricefield;
    private JTextField subName;
    private JTextField description;
    private JButton addButton;
    private JButton removeButton;
    private JLabel costP;

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



        searchCourseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

               list = Methods.listIngredients(searchField.getText());

               listModel.removeAllElements();

                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                existingCourses.setModel(listModel);
            }
        });

        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                ManageSub mansub = new ManageSub();
                }
            });

        /* addButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
           /* @Override
            public void actionPerformed(ActionEvent e) {
                if (existingCourses.isSelectionEmpty()) {
                    showMessageDialog(null, "You didnt select an ingredient to add to the course");
                } else {
                    if (quantity.getText().equals(null) || quantity.getText().equals("")) {
                        showMessageDialog(null, "You didnt set a quantity");
                    } else {
                        try {
                            if (Methods.addIngredientToMenu(
                                    menuId,
                                    Integer.parseInt(list.get(1).get(subscriptionCourses.getSelectedIndex())), Integer.parseInt(quantity.getText()))) {
                                AddSubscription temp = new AddSubscription();
                                dispose();



                            } else {
                                showMessageDialog(null, "something wrong with removing existing ingredient");
                            }
                        } catch (IllegalFormatException t) {
                            System.out.println(t + "Wrong input in quantity");
                        }
                    }
                }
            }

        });
        /*removeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
          /*  @Override
            public void actionPerformed(ActionEvent e) {
                if(ex.isSelectionEmpty()) {
                    showMessageDialog(null,"You forgot to select an ingredient to remove");
                } else{
                    if(Methods.removeIngredientFromMenu(
                            menuId, Integer.parseInt(list1.get(1).get(ingredientsIsInCourse.getSelectedIndex())) )) {

                        AddSubscription temp = new AddSubscription();
                        dispose();
                    }else{
                        showMessageDialog(null, "Something went wrong");
                    }
                }

            }
        });*/





        }
    }
