package Hapi.GUI.Chauffeur;

import Hapi.GUI.MainMenu.CEO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by klk94 on 20.04.2016.
 */
public class ActiveOrderChauffeur extends JFrame {
    private JPanel ActiveOrderW;
    private JList list1;
    private JButton doneButton;
    private JButton backButton;
    private JButton removeButton;

    public ActiveOrderChauffeur(int ChauffeurID) {
        super("eFood");
        setContentPane(ActiveOrderW);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


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
        doneButton.addActionListener(new ActionListener() {
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
                OrderViewChauffeur temp = new OrderViewChauffeur();
                dispose();
            }
        });
    }

}
