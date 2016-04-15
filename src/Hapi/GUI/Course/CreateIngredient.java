package Hapi.GUI.Course;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 15.04.2016.
 */
public class CreateIngredient extends JFrame {
    private JTextField nameF;
    private JTextField unitF;
    private JTextField priceF;
    private JButton OKButton;
    private JPanel createIP;

    public CreateIngredient() {
        super("eFood");

        setContentPane(createIP);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                if(Methods.addIngredient(nameF.getText(),unitF.getText(),Integer.parseInt(priceF.getText()))) {

                }
                dispose();

            }
        });
    }
}
