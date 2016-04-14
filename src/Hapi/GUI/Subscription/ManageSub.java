package Hapi.GUI.Subscription;

import javax.swing.*;
import java.awt.*;

/**
 * Created by klk94 on 13.03.2016.
 */
public class ManageSub extends JFrame {
    private JButton deleteSubButton;
    private JButton editSubButton;
    private JButton backButton;
    private JButton createSubscriptionButton;
    private JList list1;
    private JTextField textField1;
    private JButton searchButton;
    private JButton viewSubButton;
    private JPanel ManageSubPannel;

    public ManageSub() {
        super("Create customer");
        setContentPane(ManageSubPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

    }
}
