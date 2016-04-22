package Hapi.GUI.Course;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.IllegalFormatException;

import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 13.04.2016.
 */
public class CreateCourse extends JFrame{
    private JTextField searchField;
    private JButton searchButton;
    private JList ingredientsIsNotInCourse;
    private JList ingredientsIsInCourse;
    private JButton addButton;
    private JButton removeButton;
    private JTextField priceF;
    private JTextField courseNameF;
    private JButton backButton;
    private JTextField descriptionF;
    private JTextField quantity;
    private JButton createButton;
    private JPanel createCourse;
    private JButton createIngredientButton;
    private JButton changeIngredientButton;
    private JLabel costP;
    private JLabel unit;
    private JButton deleteIngredientButton;


  int menuId;

    private DefaultListModel listModel = new DefaultListModel();
    private DefaultListModel listModel1 = new DefaultListModel();

    ArrayList<ArrayList<String>> list = Methods.listIngredients("");
    ArrayList<ArrayList<String>> list1 = Methods.listIngredientsInMenu(menuId);

    public CreateCourse(int menuId, boolean isNew) {
        super("eFood");
       this.menuId = menuId;

        setContentPane(createCourse);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<String> info = Methods.getMenuInfo(menuId);
        priceF.setText(info.get(2));
        courseNameF.setText(info.get(0));
        descriptionF.setText(info.get(1));
        costP.setText(info.get(3));



        for (String anIngredient : list.get(0)) {
            listModel.addElement(anIngredient);
        }
        ingredientsIsNotInCourse.setModel(listModel);

        list1 = Methods.listIngredientsInMenu(menuId);

        for (String anIngredient : list1.get(0)) {
            listModel1.addElement(anIngredient);
        }
        ingredientsIsInCourse.setModel(listModel1);



        searchButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

               list = Methods.listIngredients(searchField.getText());


                listModel.removeAllElements();


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
                if(isNew) {
                    if(Methods.deleteMenu(menuId)) {
                        Courses temp = new Courses();
                        dispose();
                    }else{
                        showMessageDialog(null, "Something wrong with deleting of the course");
                    }
                } else{
                    Courses temp = new Courses();
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
                if (ingredientsIsNotInCourse.isSelectionEmpty()) {
                    showMessageDialog(null, "You didnt select an ingredient to add to the course");
                } else {
                    if (quantity.getText().equals(null) || quantity.getText().equals("")) {
                        showMessageDialog(null, "You didnt set a quantity");
                    } else {
                        try {
                            if (Methods.addIngredientToMenu(
                                    menuId,
                                    Integer.parseInt(list.get(1).get(ingredientsIsNotInCourse.getSelectedIndex())), Integer.parseInt(quantity.getText()))) {
                                CreateCourse temp = new CreateCourse(menuId,isNew);
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

        removeButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ingredientsIsInCourse.isSelectionEmpty()) {
                    showMessageDialog(null,"You forgot to select an ingredient to remove");
                } else{
                    if(Methods.removeIngredientFromMenu(
                            menuId, Integer.parseInt(list1.get(1).get(ingredientsIsInCourse.getSelectedIndex())) )) {

                        CreateCourse temp = new CreateCourse(menuId, isNew);
                        dispose();
                    }else{
                        showMessageDialog(null, "Something went wrong");
                    }
                }

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
                CreateIngredient temp = new CreateIngredient(menuId, isNew);
                dispose();
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
                CreateIngredient temp = new CreateIngredient(menuId,list.get(1).get(ingredientsIsNotInCourse.getSelectedIndex()));
            }
        });

        deleteIngredientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showConfirmDialog(null,"You sure u want to delete this ingredient?")==JOptionPane.YES_OPTION){
                    if(Methods.removeIngredient(Integer.parseInt(list.get(1).get(ingredientsIsNotInCourse.getSelectedIndex())))) {
                        showMessageDialog(null,"Ingredient deleted");
                        CreateCourse temp = new CreateCourse(menuId, isNew);
                    } else{
                        showMessageDialog(null, "Ingredient not deleted, something went wrong");
                    }


                }
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
                if(ingredientsIsNotInCourse.isSelectionEmpty()) {

                } else{

                    unit.setText(list.get(2).get(ingredientsIsNotInCourse.getSelectedIndex()));
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
                if(Methods.changeMenu(menuId,courseNameF.getText(),Integer.parseInt(priceF.getText()),descriptionF.getText() )) {
                    Courses temp = new Courses();
                    dispose();
                } else {
                    showMessageDialog(null, "Something wrong when creating/changing course");
                }
            }
        });

    }



}
