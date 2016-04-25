package Hapi.GUI.Order;

import Hapi.GUI.Course.Courses;
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
    private JLabel customerLabel;

    int orderId;





    public AddOrder(String selected, int orderId, int selectedId, boolean isNew) {
        super("eFood");
        this.orderId = orderId;
        customerLabel.setText(selected);

        setContentPane(addOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();
        DefaultListModel listModel2 = new DefaultListModel();


        ArrayList<ArrayList<String>> list = Methods.listMenu("");
        ArrayList<ArrayList<String>> list2 = Methods.listMenusInOrder(orderId);




        for (int i = 0; i < list.get(0).size(); i++) {
            String name = list.get(0).get(i);
            String id = list.get(1).get(i);
            listModel.addElement(new ListeElement(id, name));
        }
        menuNotInOrder.setModel(listModel);


        for (String anOrder : list2.get(0)) {
            listModel2.addElement(anOrder);
        }
        menuInOrder.setModel(listModel2);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(isNew) {
                    if(Methods.deleteOrder(orderId)) {
                        ManageOrders orders = new ManageOrders(selected, selectedId );
                        dispose();
                    }else{
                        showMessageDialog(null, "Something wrong with deleting of the course");
                    }
                } else{
                    ManageOrders orders = new ManageOrders(selected, selectedId );
                    dispose();
                }
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


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();

                //    String[] user = list;
                for (int i = 0; i < list.get(0).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id, name));
                }
                menuNotInOrder.setModel(listModel);
            }
        });


        removeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(menuInOrder.isSelectionEmpty()) {
                    showMessageDialog(null,"You forgot to select an ingredient to remove");
                } else{
                    if(Methods.removeMenuFromOrder(
                            orderId, Integer.parseInt(list2.get(1).get(menuInOrder.getSelectedIndex())) )) {

                        AddOrder temp = new AddOrder(selected,orderId, selectedId, isNew);
                        dispose();
                    }else{
                        showMessageDialog(null, "Something went wrong");
                    }
                }

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
                        ListeElement selectedOrder = (ListeElement) menuNotInOrder.getSelectedValuesList().get(0);
                        try {
                            if (Methods.addMenuToOrder(
                                    orderId, Integer.parseInt(selectedOrder.getId()), Integer.parseInt(quantity.getText()), description.getText())) {
                                AddOrder temp = new AddOrder(selected,orderId, selectedId, isNew );
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
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ((comboBox1.getSelectedIndex() > -1) && (comboBox2.getSelectedIndex() > -1) && (comboBox3.getSelectedIndex() > -1) && (comboBox4.getSelectedIndex() > -1) && (comboBox5.getSelectedIndex() > -1)) {


                    String year =  comboBox3.getSelectedItem().toString() ;
                    String month = comboBox2.getSelectedItem().toString();
                    String day = comboBox1.getSelectedItem().toString();
                    String hour =  comboBox4.getSelectedItem().toString();
                    String minute =  comboBox5.getSelectedItem().toString();

                    String delivery = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    showMessageDialog(null, delivery);
                    Methods.setOrderDeliveryTime(orderId, delivery);
                    ManageOrders orders = new ManageOrders(selected, selectedId);
                    dispose();

                }else showMessageDialog(null, "You must fill in full dateinformation");
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


        createCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
