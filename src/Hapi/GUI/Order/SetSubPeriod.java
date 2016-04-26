package Hapi.GUI.Order;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Knut on 25.04.2016.
 */
public class SetSubPeriod extends JFrame {


    private JComboBox dayBoxFrom;
    private JComboBox monthBoxFrom;
    private JComboBox yearBoxFrom;
    private JComboBox dayBoxTo;
    private JComboBox monthBoxTo;
    private JComboBox yearBoxTo;
    private JButton continueButton;
    private JButton backButton;
    private JPanel setSubPeriodPanel;

    public SetSubPeriod(String selected, int selectedId, boolean isNew) {
        super("eFood");

        setContentPane(setSubPeriodPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        dayBoxFrom.addPopupMenuListener(new PopupMenuListener() {
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
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        monthBoxFrom.addPopupMenuListener(new PopupMenuListener() {
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
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        yearBoxFrom.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 2016; i < 2018; i++) {
                    comboBox.addItem(i);

                }

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        dayBoxTo.addPopupMenuListener(new PopupMenuListener() {
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
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        monthBoxTo.addPopupMenuListener(new PopupMenuListener() {
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
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        yearBoxTo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                JComboBox comboBox = (JComboBox)e.getSource();
                comboBox.removeAllItems();

                for(int i = 2016; i < 2018; i++) {
                    comboBox.addItem(i);

                }

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String year =  yearBoxFrom.getSelectedItem().toString() ;
                String month = monthBoxFrom.getSelectedItem().toString();
                String day = dayBoxFrom.getSelectedItem().toString();
                String year2 =  yearBoxTo.getSelectedItem().toString() ;
                String month2 = monthBoxTo.getSelectedItem().toString();
                String day2 = dayBoxTo.getSelectedItem().toString();

                String from = year + "-" + month + "-" + day;
                String to = year2 + "-" + month2 + "-" + day2;

                AddSub suborder = new AddSub(selected, selectedId, isNew, from, to);
                dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageOrders orders = new ManageOrders(selected, selectedId);
            }
        });
    }

}
