package Hapi.GUI.Subscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewSub extends JFrame {
    private JPanel viewSub;
    private JPanel contentPane;
    private JLabel nameL;
    private JLabel descL;
    private JLabel costPL;
    private JLabel priceL;
    private JList list1;
    private JButton closeButton;

    public ViewSub(String name, String desc, String price, String costP, ArrayList<ArrayList<String>> menuList) {
        super("eFood");
        setContentPane(viewSub);
        pack();

        setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        nameL.setText(name);
        descL.setText(desc);
        priceL.setText(price + " kr");
        costPL.setText(costP + " kr");


        DefaultListModel listModel = new DefaultListModel();

        double cosP =0.0;

        for(int i =0; i<menuList.get(0).size();i++) {
            listModel.addElement(menuList.get(0).get(i) + "            " + menuList.get(2).get(i)
                    + "                    " + menuList.get(3).get(i) + "   kr/pr.");

        }


        list1.setModel(listModel);

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