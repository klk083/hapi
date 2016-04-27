package Hapi.GUI.Order;

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
public class EditOrder extends JFrame {
    private JButton searchCourseButton;
    private JList menuNotInOrder;
    private JList menuInOrder;
    private JButton addButton;
    private JButton removeButton;
    private JButton confirmOrderButton;
    private JButton backButton;
    private JComboBox dayBox;
    private JComboBox monthBox;
    private JComboBox yearBox;
    private JComboBox hourBox;
    private JComboBox minuteBox;
    private JTextField quantity;
    private JTextField searchField;
    private JPanel editOrderPanel;
    private JTextField description;
    private JLabel customerLabel;

    int orderId;





    public EditOrder(String selected, int orderId, int selectedId, boolean isNew) {
        super("eFood");
        this.orderId = orderId;
        customerLabel.setText(selected);

        setContentPane(editOrderPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();
        DefaultListModel listModel2 = new DefaultListModel();


        ArrayList<ArrayList<String>> list = Methods.listMenu("");
        ArrayList<ArrayList<String>> list2 = Methods.listMenusInOrder(orderId);




        for (int i = 0; i < list.get(1).size(); i++) {
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
                dispose();
                ManageOrders orders = new ManageOrders(selected, selectedId);
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
                JTextField text = searchField;

                ArrayList<ArrayList<String>> list = Methods.listMenu(text.getText());


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();

                //    String[] user = list;
                for (int i = 0; i < list.get(1).size(); i++) {
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

                        EditOrder temp = new EditOrder(selected,orderId, selectedId, isNew);
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
                                EditOrder temp = new EditOrder(selected,orderId, selectedId, isNew );
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
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ((dayBox.getSelectedIndex() > -1) && (monthBox.getSelectedIndex() > -1) && (yearBox.getSelectedIndex() > -1) && (hourBox.getSelectedIndex() > -1) && (minuteBox.getSelectedIndex() > -1)) {


                    String year =  yearBox.getSelectedItem().toString() ;
                    String month = monthBox.getSelectedItem().toString();
                    String day = dayBox.getSelectedItem().toString();
                    String hour =  hourBox.getSelectedItem().toString();
                    String minute =  minuteBox.getSelectedItem().toString();

                    String delivery = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    Methods.setOrderDeliveryTime(orderId, delivery);
                    ManageOrders orders = new ManageOrders(selected, selectedId);
                    dispose();

                }else showMessageDialog(null, "You must fill in full dateinformation");
            }
        });


        dayBox.addPopupMenuListener(new PopupMenuListener() {
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
        monthBox.addPopupMenuListener(new PopupMenuListener() {
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
        yearBox.addPopupMenuListener(new PopupMenuListener() {
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
        hourBox.addPopupMenuListener(new PopupMenuListener() {
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

        minuteBox.addPopupMenuListener(new PopupMenuListener() {
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

    }
}