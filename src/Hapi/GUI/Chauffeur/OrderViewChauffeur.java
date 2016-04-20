package Hapi.GUI.Chauffeur;

import javax.swing.*;
import java.awt.*;

/**
 * Created by klk94 on 19.04.2016.
 */
public class OrderViewChauffeur extends JFrame {
    private JButton nextButton;
    private JList list1;
    private JList list2;
    private JButton addButton;
    private JButton removeButton;
    private JButton signOutButton;
    private JPanel View;
    private JButton howAwesomeAmIButton;

    public OrderViewChauffeur () {
        super("eFood");
        setContentPane(View);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);
    }
}
