package Hapi.GUI.Chauffeur;

import Hapi.SQLMethods.Methods;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by klk94 on 19.04.2016.
 */
public class OrderViewChauffeur extends JFrame {
    private JButton nextButton;
    private JList viewReady;
    private JList viewOnChaffeur;
    private JButton addButton;
    private JButton removeButton;
    private JButton signOutButton;
    private JPanel View;
    private JButton howAwesomeAmIButton;


    int loginID;
    private DefaultListModel modelReady = new DefaultListModel();
    private DefaultListModel modelChauffeur = new DefaultListModel();

    ArrayList<ArrayList<String>> listReadyOrders = Methods.listOrdersForDeliveries();
    ArrayList<ArrayList<String>> listChauffeur = Methods.listOrdersForChauffeur(loginID);

    public OrderViewChauffeur (int loginID) {
        super("eFood");
        this.loginID = loginID;
        
        setContentPane(View);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);

        listReadyOrders = Methods.listOrdersForDeliveries();
        for (String anOrder : listReadyOrders.get(0)) {
            modelReady.addElement(anOrder);
        }
        viewReady.setModel(modelReady);

        listChauffeur = Methods.listOrdersForChauffeur(loginID);

        for (String anOrder : listChauffeur.get(0)) {
            modelChauffeur.addElement(anOrder);
        }
        viewOnChaffeur.setModel(modelChauffeur);
    }



}
