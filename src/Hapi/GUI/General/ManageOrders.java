package Hapi.GUI.General;

import Hapi.GUI.MainMenu.CEO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 11.03.2016.
 */
public class ManageOrders extends JFrame {
    private JButton createNewOrderButton;
    private JList list1;
    private JButton changeOrderButton;
    private JButton backButton;
    private JPanel ordersPannel;

    public ManageOrders() {
        super("Manage orders");
        setContentPane(ordersPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        createNewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateOrder order = new CreateOrder();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CEO ceo = new CEO();
            }
        });
    }
}
