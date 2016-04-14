package Hapi.GUI.Course;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by klk94 on 13.04.2016.
 */
public class CreateCourse extends JFrame{
    private JTextField searchField;
    private JButton searchButton;
    private JList ingredientsIsNotInCourse;
    private JList ingredientIsInCourse;
    private JButton addButton;
    private JButton removeButton;
    private JTextField price;
    private JTextField courseName;
    private JButton backButton;
    private JTextField descriptionL;
    private JTextField quantity;
    private JButton createButton;
    private JPanel createCourse;
    private JButton createIngredientButton;
    private JButton changeIngredientButton;
    private JLabel costP;
    private JLabel unit;

    int selectedTest;

    public CreateCourse() {
        super("eFood");
        setContentPane(createCourse);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<ArrayList<String>> list = Methods.listIngredients("");


        DefaultListModel listModel = new DefaultListModel();

        //    String[] user = list;
        for (String enuser : list.get(0)) {
            listModel.addElement(enuser);
        }
        ingredientsIsNotInCourse.setModel(listModel);


        searchButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                ingredientsIsNotInCourse.setModel(listModel);
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

            }
        });
        createIngredientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        changeIngredientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        backButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Courses temp = new Courses();
                dispose();
            }
        });


        ingredientsIsNotInCourse.addListSelectionListener(new ListSelectionListener() {
            /**
             * Called whenever the value of the selection changes.
             *
             * @param e the event that characterizes the change.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
    }

    public CreateCourse(int menuId) {
        super("eFood");
        setContentPane(createCourse);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<ArrayList<String>> list = Methods.listIngredients("");


        DefaultListModel listModel = new DefaultListModel();


        for (String anIngredient : list.get(0)) {
            listModel.addElement(anIngredient);
        }
        ingredientsIsNotInCourse.setModel(listModel);


        ArrayList<String> info = Methods.getMenuInfo(menuId);
        price.setText(info.get(2));
        courseName.setText(info.get(0));
        descriptionL.setText(info.get(1));
        costP.setText(info.get(3));


        ArrayList<ArrayList<String>> list1 = Methods.listIngredientsInMenu(menuId);


        DefaultListModel listModel1 = new DefaultListModel();


        for (String anIngredient : list1.get(0)) {
            listModel1.addElement(anIngredient);
        }
        ingredientIsInCourse.setModel(listModel1);





        searchButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = searchField;

                ArrayList<ArrayList<String>> list = Methods.listIngredients(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                ingredientsIsNotInCourse.setModel(listModel);
            }
        });

        backButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Courses temp = new Courses();
                dispose();
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

            }
        });
        createIngredientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        changeIngredientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        ingredientsIsNotInCourse.addListSelectionListener(new ListSelectionListener() {
            /**
             * Called whenever the value of the selection changes.
             *
             * @param e the event that characterizes the change.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ArrayList<String> info = Methods.getIngredientInfo(list.get(1).get(ingredientsIsNotInCourse.getSelectedIndex()));
                unit.setText(info.get(2));

            }
        });


    }



}
