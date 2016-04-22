package Hapi.GUI.MainMenu;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;

/**
 * Created by klk94 on 16.03.2016.
 */
public class Expert extends JFrame {
        private JButton coursesButton;
        private JButton subscriptionsButton;
        private JLabel nameL;
    private JPanel expert;

    public Expert() {
            super("eFood");
            setContentPane(expert);
            pack();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            setVisible(true);
            nameL.setText("Logged in as: "+ Methods.getEmployeeName(Methods.getID()));
    }

}
