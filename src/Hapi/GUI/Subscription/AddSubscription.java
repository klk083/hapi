package Hapi.GUI.Subscription;

import javax.swing.*;
import Hapi.SQLMethods.Methods;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Håkon on 19.04.2016.
 */
public class AddSubscription extends JFrame {
    private JPanel AddSubPanel;
    private JButton cancelButton;
    private JButton OKButton;
    private JButton searchButton;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JList list1;
    private JList list2;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton addButton;
    private JButton removeButton;


    public AddSubscription() {
        super("eFood");
        setContentPane(AddSubPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
}
