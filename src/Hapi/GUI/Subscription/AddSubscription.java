package Hapi.GUI.Subscription;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import javax.swing.JComboBox;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Håkon on 19.04.2016.
 */
public class AddSubscription extends JFrame {
    private JPanel content;
    private JButton backButton;
    private JButton createButton;
    private JButton searchCourseButton;
    private JTextField searchField;
    private JList existingCourses;
    private JList subscriptionCourses;
    private JTextField quantity;
    private JTextField pricefield;
    private JTextField subName;
    private JTextField descriptionF;
    private JButton addButton;
    private JButton removeButton;
    private JLabel costP;
    private JPanel AddSubPanel;
    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JCheckBox saturdayCheckBox;
    private JCheckBox sundayCheckBox;

    int subscriptionId;

    private DefaultListModel listModel = new DefaultListModel();
    private DefaultListModel listModel1 = new DefaultListModel();

    ArrayList<ArrayList<String>> list = Methods.listMenu("");
    ArrayList<ArrayList<String>> list1 = Methods.listCoursesInSub(subscriptionId);


    public AddSubscription(int subscriptionId, boolean isNew) {
        super("eFood");
        this.subscriptionId = subscriptionId;

        setContentPane(AddSubPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<String> info = Methods.getSubInfo(subscriptionId);

        pricefield.setText(info.get(2));
        subName.setText(info.get(0));
        descriptionF.setText(info.get(1));
        costP.setText(info.get(3));

        for (String aCourse : list.get(0)) {
            listModel.addElement(aCourse);
        }
        existingCourses.setModel(listModel);

        list1 = Methods.listCoursesInSub(subscriptionId);

        for (String aCourse : list1.get(0)) {
            listModel1.addElement(aCourse);
        }
        subscriptionCourses.setModel(listModel1);



        searchCourseButton.addActionListener(new ActionListener(){
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e){

               list = Methods.listMenu(searchField.getText());


               listModel.removeAllElements();


                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                existingCourses.setModel(listModel);
            }
        });

        backButton.addActionListener(new ActionListener(){
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e){
                if(isNew){
                    if(Methods.deleteSub(subscriptionId)){
                        ManageSub subs = new ManageSub();
                        dispose();
                    }else{
                        showMessageDialog(null, "Something wrong with deleting of the subscription");
                    }
                } else{
                    ManageSub temp = new ManageSub();
                    dispose();
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
                if (existingCourses.isSelectionEmpty()) {
                    showMessageDialog(null, "Please select a course to add to the subscription");
                } else {
                    if (quantity.getText().equals(null) || quantity.getText().equals("")) {
                        showMessageDialog(null, "You didnt set a quantity");
                    } else {
                            try {
                                if (Methods.addMenuToSub(
                                        Integer.parseInt(list.get(1).get(existingCourses.getSelectedIndex())), subscriptionId, Integer.parseInt(quantity.getText()))) {
                                    AddSubscription temp = new AddSubscription(subscriptionId, isNew);
                                    dispose();


                                } else {
                                    showMessageDialog(null, "Something wrong with adding");
                                }
                            } catch (IllegalFormatException t) {
                                System.out.println(t + "Wrong input in quantity");
                            }
                        }
                    }
                }

        });

        removeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e
                 */
                @Override
                public void actionPerformed (ActionEvent e){
                    if (subscriptionCourses.isSelectionEmpty()) {
                        showMessageDialog(null, "You forgot to select a course to remove");
                    } else {
                        if (Methods.removeMenuFromSub(
                                subscriptionId, Integer.parseInt(list1.get(1).get(subscriptionCourses.getSelectedIndex())))) {

                            AddSubscription temp = new AddSubscription(subscriptionId, isNew);
                            dispose();
                        } else {
                            showMessageDialog(null, "Something went wrong");
                        }
                    }

                }
        });

       existingCourses.addListSelectionListener(new ListSelectionListener() {
            /**
             * Called whenever the value of the selection changes.
             *
             * @param e the event that characterizes the change.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(existingCourses.isSelectionEmpty()) {

                }else{

                }

            }
        });


        createButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if (pricefield.getText().equals(null) || pricefield.getText().equals("0") || pricefield.getText().equals("")) {
                    showMessageDialog(null, "Please create a subscription price");
                } else {
                    if (subName.getText().equals(null) || subName.getText().equals("name") || subName.getText().equals("")) {
                        showMessageDialog(null, "Please create a subscription name");
                    } else {
                        if (descriptionF.getText().equals(null) || descriptionF.getText().equals("description") || descriptionF.getText().equals("")) {
                            showMessageDialog(null, "Please create a description for the subscription");
                        } else {
                            if (mondayCheckBox.getSelectedObjects() == null && tuesdayCheckBox.getSelectedObjects() == null && wednesdayCheckBox.getSelectedObjects() == null &&
                                    thursdayCheckBox.getSelectedObjects() == null && fridayCheckBox.getSelectedObjects() == null && saturdayCheckBox.getSelectedObjects() == null &&
                                    sundayCheckBox.getSelectedObjects() == null) {
                                showMessageDialog(null, "Please select  delvery day(s) for the subscription");

                            } else {
                                if (Methods.changeSub(subscriptionId, subName.getText(), Integer.parseInt(pricefield.getText()), descriptionF.getText())) {
                                    ManageSub temp = new ManageSub();
                                    dispose();
                                } else {
                                    showMessageDialog(null, "Something wrong when creating sub");
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
