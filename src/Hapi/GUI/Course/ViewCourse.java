package Hapi.GUI.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by klk94 on 14.04.2016.
 */
public class ViewCourse extends JFrame{
    private JList displayList;
    private JButton closeButton;
    private JPanel viewCourses;
    private JLabel nameL;
    private JLabel descL;
    private JLabel costPL;
    private JLabel priceL;

    public ViewCourse(String name,String desc, String price,String costP,ArrayList<ArrayList<String>> ingridientList) {
        super("eFood");
        setContentPane(viewCourses);
        pack();

        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        nameL.setText(name);
        descL.setText(desc);
        priceL.setText(price+" kr");
        costPL.setText(costP+" kr");


        DefaultListModel listModel = new DefaultListModel();

        double cosP =0.0;

        for(int i =0; i<ingridientList.get(0).size();i++) {
            listModel.addElement(ingridientList.get(0).get(i) +" " + ingridientList.get(3).get(i)
                    + " " + ingridientList.get(2).get(i));

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
