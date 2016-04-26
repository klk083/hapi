package Hapi.GUI.Order;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Hapi.SQLMethods.Methods.*;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

/**
 * Created by klk94 on 11.03.2016.
 */
public class ManageOrders extends JFrame {
    private JList ordersList;
    private JTextField textField1;
    private JButton editOrderButton;
    private JButton backButton;
    private JButton searchButton;
    private JButton createNewCustomerButton;
    private JButton viewOrderButton;
    private JPanel createOrderPannel;
    private JButton createOrderButton;
    private JButton createCustomerButton;
    private JButton deleteOrderButton;
    private JButton createSubscriptionButton;
    private JList subList;
    private JLabel customerNameLabel;

    public ManageOrders(String selected, int selectedInt) {
        super("Manage order");
        setContentPane(createOrderPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        customerNameLabel.setText(selected + "'s" + " orders");




        DefaultListModel listModel1 = new DefaultListModel();
        DefaultListModel<ListeElement2> listModel2 = new DefaultListModel<ListeElement2>();

        ArrayList<Integer> list1 = Methods.listOrders(selectedInt);
        ArrayList<Integer> list2 = Methods.listSubs(selectedInt);



        for (int enuser : list1) {
            listModel1.addElement(enuser);
        }
        ordersList.setModel(listModel1);



        for (int i = 0; i < list2.size(); i++) {
            String id = Integer.toString(list2.get(i));
            listModel2.addElement(new ListeElement2(id));
        }
        subList.setModel(listModel2);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManageCustomerOrders cOrders = new ManageCustomerOrders();

            }
        });

        createSubscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(selectedInt != -1) {
                    dispose();
                    SetSubPeriod order = new SetSubPeriod(selected, selectedInt, true);
                }
                else {
                    showMessageDialog(null, "Something with the creation of the course went wrong");
                }
            }
        });


        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int orderId = Methods.createOrder(selectedInt,"1000-01-01 00:00:00");

                if(selectedInt != -1) {
                    dispose();
                    AddOrder order = new AddOrder(selected, orderId, selectedInt, true);
                }
                else {
                    showMessageDialog(null, "Something with the creation of the course went wrong");
                }

            }
        });
        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (subList.isSelectionEmpty() && ordersList.isSelectionEmpty()) {
                    showMessageDialog(null, "You need to select an order");
                } else if(subList.isSelectionEmpty() && ((ordersList.getSelectedIndex())) > -1)   {
                    int choice = showOptionDialog(null,
                            "You really want to delete that order?",
                            "Quit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, null, null);
                    if (choice == JOptionPane.YES_OPTION) {
                        deleteOrder((list1.get(ordersList.getSelectedIndex())));
                        dispose();
                        ManageOrders customers = new ManageOrders(selected, selectedInt);

                    }
                } else if (ordersList.isSelectionEmpty() && ((subList.getSelectedIndex())) > -1) {
                    ListeElement2 selectedSub = (ListeElement2) subList.getSelectedValuesList().get(0);
                    removeSubFromCustomer(Integer.parseInt(selectedSub.getId()),selectedInt );
                    dispose();
                    ManageOrders customers = new ManageOrders(selected, selectedInt);
                }



            }
        });
        viewOrderButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(subList.isSelectionEmpty() && ordersList.isSelectionEmpty() ) {
                    showMessageDialog(null, "You forgot to select an order");
                }
                else if (subList.isSelectionEmpty() && (((ordersList.getSelectedIndex())) > -1)){

                    ArrayList<ArrayList<String>> menuList =
                            Methods.listMenusInOrder((list1.get(ordersList.getSelectedIndex())));

                    ArrayList<String> info =  Methods.getOrderInfo(list1.get(ordersList.getSelectedIndex()));
                    int sum = Methods.findTotalPriceOrder((list1.get(ordersList.getSelectedIndex())));

                    ViewOrder viewOrder = new ViewOrder(info.get(0),info.get(1),info.get(2), info.get(3), menuList, sum);


                }else {
                    if (ordersList.isSelectionEmpty() && ((subList.getSelectedIndex())) > -1) {
                        ListeElement2 selectedSub = (ListeElement2) subList.getSelectedValuesList().get(0);

                        ArrayList<ArrayList<String>> listSubs =
                                Methods.listSubOnCustomer(selectedInt);

                        ArrayList<String> info = Methods.getSubInfo(Integer.parseInt(selectedSub.getId()));

                        ViewSub viewSubs = new ViewSub(selectedInt, Integer.parseInt(selectedSub.getId()), info, listSubs);



                    }
                }


            }
        });


        editOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (subList.isSelectionEmpty() && ordersList.isSelectionEmpty()) {
                    showMessageDialog(null, "You forgot to select an order");
                } else if (subList.isSelectionEmpty() && ((ordersList.getSelectedIndex())) > -1) {
                    int orderId = list1.get(ordersList.getSelectedIndex());
                    dispose();
                    EditOrder order = new EditOrder(selected, orderId, selectedInt, true);
                } else if (ordersList.isSelectionEmpty() && ((subList.getSelectedIndex())) > -1) {
                    ListeElement2 selectedSub = (ListeElement2) subList.getSelectedValuesList().get(0);
                    dispose();
                    EditSub order = new EditSub(selected, selectedInt, true, getSubDates((selectedInt), Integer.parseInt(selectedSub.getId())));
                }
            }
        });
    }
}
