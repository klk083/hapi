package Hapi.GUI.Course;

import Hapi.GUI.Customer.EditCustomer;
import Hapi.GUI.General.Login;
import Hapi.GUI.MainMenu.CEO;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 13.04.2016.
 */
public class Courses extends JFrame{
    private JButton deleteCourseButton;
    private JButton editCourseButton;
    private JButton viewCourseButton;
    private JButton createCourseButton;
    private JButton mainMenuButton;
    private JList list1;
    private JTextField textField1;
    private JButton searchButton;
    private JPanel Courses;

    public Courses() {
        super("eFood");
        setContentPane(Courses);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


        ArrayList<ArrayList<String>> list = Methods.listMenu("");


        DefaultListModel listModel = new DefaultListModel();


        for (String enCourse : list.get(0)) {
            listModel.addElement(enCourse);
        }
        list1.setModel(listModel);

        searchButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listMenu(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enCourse : list.get(0)) {
                    listModel.addElement(enCourse);
                }
                list1.setModel(listModel);
            }
        });

        deleteCourseButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty()) {
                    showMessageDialog(null, "You have not selected a course");
                } else {
                    if(Methods.isMenuInOrder(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                        showMessageDialog(null,"The course you are trying to delete has active orders");
                    }
                    if(showConfirmDialog(null,"You sure you want to delete the course")==JOptionPane.YES_OPTION) {
                       if(Methods.deleteMenu(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                           showMessageDialog(null,"Course deleted");
                           dispose();
                           Courses temp = new Courses();
                       } else {
                           showMessageDialog(null,"Course not deleted");
                       }
                    }
                }
            }
        });

        editCourseButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty()){
                    showMessageDialog(null, "You forgot to select a course");
                } else {
                    CreateCourse editC = new CreateCourse(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())),false);
                    dispose();
                }
            }
        });

        viewCourseButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list1.isSelectionEmpty() ) {
                    showMessageDialog(null, "You forgot to select a course");
                }
                else {
                    ArrayList<ArrayList<String>> ingridentList =
                            Methods.listIngredientsInMenu(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())));

                    ArrayList<String> Info =  Methods.getMenuInfo(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())));
                    ViewCourse viewCourse = new ViewCourse(Info.get(0),Info.get(1),Info.get(2),Info.get(3),ingridentList);
                }
            }
        });

        createCourseButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int menuID = Methods.createMenu("name","description",0);
                if(menuID != -1) {
                    CreateCourse temp = new CreateCourse(menuID,true);
                    dispose();
                } else {
                    showMessageDialog(null, "Something with the creation of the course went wrong");
                }

            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                CEO ceo = new CEO();
                dispose();
            }
        });
    }

}
