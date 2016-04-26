package Hapi.GUI.Order;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 14.04.2016.
 */
public class EditSub extends JFrame {
    private JButton searchCourseButton;
    private JList subNotInOrder;
    private JList subInOrder;
    private JButton addButton;
    private JButton removeButton;
    private JButton createOrderButton;
    private JButton backButton;
    private JTextField searchField;
    private JPanel addOrderPanel;
    private JLabel customerLabel;

    public EditSub(String selected, int selectedId, boolean isNew, ArrayList<String> dates) {
        super("eFood");

        customerLabel.setText(selected);

        setContentPane(addOrderPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();
        DefaultListModel<ListeElement> listModel2 = new DefaultListModel<ListeElement>();


        ArrayList<ArrayList<String>> list = Methods.listSubscriptions("");
        ArrayList<ArrayList<String>> list2 = Methods.listSubOnCustomer(selectedId);





        for (int i = 0; i < list.get(0).size(); i++) {
            String name = list.get(0).get(i);
            String id = list.get(1).get(i);
            listModel.addElement(new ListeElement(id, name));
        }
        subNotInOrder.setModel(listModel);

        for (int i = 0; i < list2.get(0).size(); i++) {
            String name = list2.get(0).get(i);
            String id = list2.get(1).get(i);
            listModel2.addElement(new ListeElement(id, name));
        }
        subInOrder.setModel(listModel2);





        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                        ManageOrders orders = new ManageOrders(selected, selectedId);
                        dispose();

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
                for (int i = 0; i < list.get(0).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id, name));
                }
                subNotInOrder.setModel(listModel);
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
                if(subInOrder.isSelectionEmpty()) {
                    showMessageDialog(null,"You forgot to select an ingredient to remove");
                } else{
                    ListeElement selectedSubOrder = (ListeElement) subInOrder.getSelectedValuesList().get(0);


                    if(Methods.removeSubFromCustomer(
                            Integer.parseInt(selectedSubOrder.getId()), selectedId )) {

                        EditSub temp = new EditSub(selected, selectedId, isNew, dates);
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
                if (subNotInOrder.isSelectionEmpty()) {
                    showMessageDialog(null, "You didnt select a subscription menu to add to the subscription");
                } else

                {
                    ListeElement selectedOrder = (ListeElement) subNotInOrder.getSelectedValuesList().get(0);
                    if (Methods.addSubToCustomer(
                            Integer.parseInt(selectedOrder.getId()), selectedId, dates.get(0), dates.get(1))) {
                        EditSub temp = new EditSub(selected, selectedId, isNew, dates );
                        dispose();
                    } else {
                        showMessageDialog(null, "something wrong with removing existing sub");

                    }
                }
            }

        });
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                ManageOrders orders = new ManageOrders(selected, selectedId);
                dispose();

            }
        });



    }
}
