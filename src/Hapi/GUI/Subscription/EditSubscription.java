package Hapi.GUI.Subscription;

import Hapi.GUI.Subscription.EditSubscription;
import Hapi.SQLMethods.Methods;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//import static Hapi.SQLMethods.Methods.deleteSubscription;
import static javax.swing.JOptionPane.showMessageDialog;
//import static javax.swing.JOptionPane.showOptionDialog;
/**
 * Created by Håkon on 19.04.2016.
 */
public class EditSubscription extends JFrame {
    private JButton cancelButton;
    private JButton OKButton;
    private JButton searchButton;
    private JTextField textField1;
    private JList list1;
    private JList list2;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton addButton;
    private JButton removeButton;
    private JPanel EditSubscriptionPannel;


    public EditSubscription() {
        super("eFood");
        setContentPane(EditSubscriptionPannel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        ArrayList<ArrayList<String>> list = Methods.listCustomers("");

        DefaultListModel listModel = new DefaultListModel();

        for (String enuser : list.get(0)) {
            listModel.addElement(enuser);
        }
        list1.setModel(listModel);
    }
}