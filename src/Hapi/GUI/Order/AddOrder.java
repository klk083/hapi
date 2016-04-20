package Hapi.GUI.Order;

import Hapi.GUI.Course.CreateCourse;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.IllegalFormatException;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 14.04.2016.
 */
public class AddOrder extends JFrame {
    private JButton searchCourseButton;
    private JList menuNotInOrder;
    private JList menuInOrder;
    private JButton createCourseButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton createOrderButton;
    private JButton backButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JTextField quantity;
    private JTextField textField1;
    private JPanel addOrderPannel;
    private JTextField description;

    int orderId;





    public AddOrder(String selected, int orderId, int selectedId, boolean isNew) {
        super("eFood");
        this.orderId = orderId;

        setContentPane(addOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        DefaultListModel listModel = new DefaultListModel();
        DefaultListModel listModel2 = new DefaultListModel();


        ArrayList<ArrayList<String>> list = Methods.listMenu("");
        ArrayList<ArrayList<String>> list2 = Methods.listMenusInOrder(orderId);




        for (String enCourse : list.get(0)) {
            listModel.addElement(enCourse);
        }
        menuNotInOrder.setModel(listModel);



        list2 = Methods.listMenusInOrder(orderId);

        for (String anIngredient : list2.get(0)) {
            listModel2.addElement(anIngredient);
        }
        menuInOrder.setModel(listModel2);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageOrders orders = new ManageOrders(selected, selectedId );
            }
        });

        searchCourseButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listMenu(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enCourse : list.get(0)) {
                    listModel.addElement(enCourse);
                }
                menuNotInOrder.setModel(listModel);
            }
        });



        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 1; i < 32; i++) {
                    if(i < 10) {
                        comboBox.addItem("0" + i);
                    }else {
                        comboBox.addItem(i);
                    }

                }

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
        });
        comboBox2.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 1; i < 13; i++) {
                    if(i < 10) {
                        comboBox.addItem("0" + i);
                    }else {
                        comboBox.addItem(i);
                    }

                }

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
        });
        comboBox3.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 2016; i < 2018; i++) {
                    comboBox.addItem(i);

                }

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
        });
        comboBox4.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 1; i < 24; i++) {
                    if(i < 10) {
                        comboBox.addItem("0" + i);
                    }else {
                        comboBox.addItem(i);
                    }

                }

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
        });

        comboBox5.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 00; i < 60; i++) {
                    if(i < 10) {
                        comboBox.addItem("0" + i);
                    } else {
                        comboBox.addItem(i);
                    }
                }

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
        });
        addButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuNotInOrder.isSelectionEmpty()) {
                    showMessageDialog(null, "You didnt select an ingredient to add to the course");
                } else {
                    if (quantity.getText().equals(null) || quantity.getText().equals("")) {
                        showMessageDialog(null, "You didnt set a quantity");
                    } else {
                        try {
                            if (Methods.addMenuToOrder(
                                        orderId, Integer.parseInt(list.get(1).get(menuNotInOrder.getSelectedIndex())), Integer.parseInt(quantity.getText()), description.getText())) {
                                    AddOrder temp = new AddOrder(selected,orderId, selectedId, true );
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
    }
}
