package Hapi.GUI.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Knut on 20.04.2016.
 */
public class ViewOrder extends JFrame{
    private JList displayList;
    private JButton closeButton;
    private JPanel viewOrder;

    private JLabel deliveryId;
    private JLabel readyL;
    private JLabel totalPrice;

    public ViewOrder(String order, String customer, String delivery, String ready, ArrayList<ArrayList<String>> menuList, int sum) {
        super("eFood");
        setContentPane(viewOrder);
        pack();

        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        deliveryId.setText(delivery);
        totalPrice.setText(String.valueOf(sum + "kr"));
        if(Integer.parseInt(ready) > 0) {
            readyL.setText("Yes");
        } else {
            readyL.setText("No");
        }





        DefaultListModel listModel = new DefaultListModel();

        for(int i =0; i<menuList.get(0).size();i++) {
            listModel.addElement(menuList.get(0).get(i) + " " + menuList.get(2).get(i) + " stk " +
                    (Integer.parseInt(menuList.get(3).get(i)) * (Integer.parseInt(menuList.get(2).get(i)))) + " kr ");


        }

        displayList.setModel(listModel);



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
