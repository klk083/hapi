package Hapi.GUI.Cook;

import Hapi.GUI.Course.ViewCourse;
import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by klk94 on 20.04.2016.
 */
public class DetailOrder extends JFrame{
    private JList view;
    private JButton viewButton;
    private JButton closeButton;
    private JPanel panel;

    public DetailOrder(int orderID, ArrayList<ArrayList<String>> listMenus) {
        super("eFood");

        setContentPane(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        DefaultListModel model = new DefaultListModel();
        for (int i=0;i<listMenus.get(0).size();i++) {
            model.addElement(listMenus.get(2).get(i) + "Stk " + listMenus.get(0).get(i));
        }
        view.setModel(model);

        viewButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ArrayList<String>> ingridentList =
                        Methods.listIngredientsInMenu(Integer.parseInt(listMenus.get(1).get(view.getSelectedIndex())));

                ArrayList<String> Info =  Methods.getMenuInfo(Integer.parseInt(listMenus.get(1).get(view.getSelectedIndex())));
                ViewCourse viewCourse = new ViewCourse(Info.get(0),Info.get(1),Info.get(2),Info.get(3),ingridentList);
            }
        });
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
