package Hapi.GUI.Subscription;

import Hapi.GUI.Subscription.EditSubscription;
import Hapi.SQLMethods.Methods;
import Hapi.GUI.MainMenu.CEO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//import static Hapi.SQLMethods.Methods.deleteSubscription;
import static javax.swing.JOptionPane.showConfirmDialog;
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
    private JButton searchSubButton;
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

        searchSubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listSubs("");


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
                    showMessageDialog(null, "Please select a subscription");
                } else {
                    dispose();
                    EditSubscription editsub = new EditSubscription();
                }
            }
        });

        viewSubButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(list1.isSelectionEmpty()){
                    showMessageDialog(null, "Please select a subscription");
                }else{
                    dispose();
                    ViewSub viewsub = new ViewSub();
                }
            }
        });

        createSubscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int subscriptionId = Methods.createSub("name", "description", 0);
                if (subscriptionId != -1) {
                    AddSubscription addsub = new AddSubscription(subscriptionId, true);
                    dispose();
                } else {
                    showMessageDialog(null, "Creation of subscription went wrong");
                }
            }

        });

        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                CEO mainmenu = new CEO();
            }
        });

        deleteSubButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                if(list1.isSelectionEmpty()) {
                    showMessageDialog(null, "You have not selected a course");
                } else {
                    if(Methods.isMenuInOrder(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                        showMessageDialog(null,"The course you are trying to delete has active orders");
                    }
                    if(showConfirmDialog(null,"You sure you want to delete the course")==JOptionPane.YES_OPTION) {
                        if(Methods.deleteMenu(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                            showMessageDialog(null,"Course deleted");
                            dispose();
                            ManageSub temp = new ManageSub();
                        } else {
                            showMessageDialog(null,"Course not deleted");
                        }
                    }
                }
            }
        });
    }
}
