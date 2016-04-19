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
import static javax.swing.JOptionPane.showOptionDialog;

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
    private JButton searchCustomerButton;
    private JButton viewSubButton;
    private JPanel ManageSubPannel;

    public ManageSub() {
        super("eFood");
        setContentPane(ManageSubPannel);
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

        searchCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listCustomers(text.getText());


                DefaultListModel listModel = new DefaultListModel();

                //    String[] user = list;
                for (String enuser : list.get(0)) {
                    listModel.addElement(enuser);
                }
                list1.setModel(listModel);

            }
        });


        editSubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list1.isSelectionEmpty()) {
                    showMessageDialog(null, "Please select a customer");
                } else {
                    dispose();
                    EditSubscription editsub = new EditSubscription();
                }
            }
            });

        }

    }
