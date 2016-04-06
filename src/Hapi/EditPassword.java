package Hapi;

import javax.swing.*;

/**
 * Created by klk94 on 11.03.2016. fcgfhg
 */
public class EditPassword extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton changeButton;
    private JButton cancelButton;
    private JPanel editPasswordPanel;

    public EditPassword() {
        super("EditPassword");
        setContentPane(editPasswordPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}


