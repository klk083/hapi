package Hapi.GUI.Course;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by klk94 on 15.04.2016.
 */
public class CreateIngredient extends JFrame {
    private JTextField nameF;
    private JTextField unitF;
    private JTextField priceF;
    private JButton OKButton;
    private JPanel createIP;

    boolean isNew;

    public CreateIngredient(int menuId, boolean isNew) {
        super("eFood");
        this.isNew = isNew;
        setContentPane(createIP);
        pack();

        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        OKButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameF.getText().equals("") || unitF.getText().equals("") || priceF.getText().equals("")) {
                    showMessageDialog(null, "Ingredient not made");
                } else {
                    if(Methods.addIngredient(nameF.getText(),unitF.getText(),Integer.parseInt(priceF.getText()))) {
                        showMessageDialog(null, "Ingredient added");
                    } else {
                        showMessageDialog(null,"Something went wrong when adding ingridient");
                    }
                }

                CreateCourse temp = new CreateCourse(menuId, isNew);
                dispose();

            }
        });
    }

    public CreateIngredient(int menuId, String ingredientID, boolean isNew) {
        super("eFood");

        setContentPane(createIP);
        pack();

        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<String> info = Methods.getIngredientInfo(ingredientID);
        nameF.setText(info.get(0));
        unitF.setText(info.get(2));
        priceF.setText(info.get(3));

        OKButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
           @Override
            public void actionPerformed(ActionEvent e) {
                if(nameF.getText().equals("") || unitF.getText().equals("") || priceF.getText().equals("")) {
                    showMessageDialog(null, "Ingredient not made");
                } else {
                    if(Methods.changeIngredient(ingredientID, nameF.getText(),Integer.parseInt(priceF.getText()),unitF.getText())) {
                        showMessageDialog(null, "Ingredient added");
                    } else {
                        showMessageDialog(null,"Something went wrong when adding ingridient");
                    }
                }

                CreateCourse temp = new CreateCourse(menuId,isNew);
                dispose();

            }
        });
    }
}
