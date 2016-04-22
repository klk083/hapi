package Hapi.GUI.Subscription;

import Hapi.GUI.Order.ListeElement;
import Hapi.GUI.Order.ManageOrders;
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

        ArrayList<ArrayList<String>> list = Methods.listSubscriptions("");

        DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();

        for (int i = 0; i < list.get(0).size(); i++) {
            String name = list.get(0).get(i);
            String id = list.get(1).get(i);
            listModel.addElement(new ListeElement(id, name));
        }
        list1.setModel(listModel);

        searchSubButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField text = textField1;

                ArrayList<ArrayList<String>> list = Methods.listSubscriptions(text.getText());


                DefaultListModel<ListeElement> listModel = new DefaultListModel<ListeElement>();

                //    String[] user = list;
                for (int i = 0; i < list.get(0).size(); i++) {
                    String name = list.get(0).get(i);
                    String id = list.get(1).get(i);
                    listModel.addElement(new ListeElement(id,name));
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
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e){


                if(list1.isSelectionEmpty()){
                    showMessageDialog(null, "Please select a subscription");
                }else{
                    ListeElement selected = (ListeElement) list1.getSelectedValuesList().get(0);
                    ArrayList<ArrayList<String>> menuList =
                            Methods.listCoursesInSub(Integer.parseInt(selected.getId()));

                    ArrayList<String> Info =  Methods.getSubInfo(Integer.parseInt(selected.getId()));
                    ViewSub viewSub = new ViewSub(Info.get(0),Info.get(1),Info.get(2),Info.get(3),menuList);
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
                    showMessageDialog(null, "You have not selected a subscription");
                } else {
                    if(Methods.isMenuInOrder(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                        showMessageDialog(null,"The subscription you are trying to delete has active orders");
                    }
                    if(showConfirmDialog(null,"You sure you want to delete the subscription")==JOptionPane.YES_OPTION) {
                        if(Methods.deleteSub(Integer.parseInt(list.get(1).get(list1.getSelectedIndex())))) {
                            showMessageDialog(null,"Subscription deleted");
                            dispose();
                            ManageSub temp = new ManageSub();
                        } else {
                            showMessageDialog(null,"Subscription not deleted");
                        }
                    }
                }
            }
        });
    }
}
