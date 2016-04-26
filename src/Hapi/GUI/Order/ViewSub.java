package Hapi.GUI.Order;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 20.04.2016.
 */
public class ViewSub extends JFrame{
    private JButton closeButton;
    private JPanel viewOrder;
    private JLabel nameLabel;
    private JLabel deliveryFrom;
    private JLabel deliveryTo;
    private JLabel priceLabel;
    private JLabel descrLabel;

    public ViewSub(int selectedInt, int subId,  ArrayList<String> info, ArrayList<ArrayList<String>> listSubs) {
        super("eFood");
        setContentPane(viewOrder);
        pack();

        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        ArrayList<String> date = Methods.getSubDates(selectedInt, subId);
        ArrayList<ArrayList<String>> subList = Methods.listSubOnCustomer(selectedInt);

        DefaultListModel listModel = new DefaultListModel();

        deliveryFrom.setText(date.get(0));
        deliveryTo.setText(date.get(1));
        nameLabel.setText(info.get(0));
        descrLabel.setText(info.get(1));
        priceLabel.setText(info.get(2));










        closeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }





}
