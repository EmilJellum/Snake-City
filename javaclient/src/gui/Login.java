package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Login extends JPanel {
    private JLabel lblNewLabel;
    private JTextField txtUsername;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtPassword;
    private JButton btnLogin;
    private JButton btnCreateUser;

    /**
     * Create the panel.
     */
    public Login() {
        setLayout(null);

        lblNewLabel = new JLabel("Snake City");
        lblNewLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 40));
        lblNewLabel.setBounds(122, 35, 205, 49);
        add(lblNewLabel);

        txtUsername = new JTextField();
        txtUsername.setBounds(241, 119, 134, 35);
        add(txtUsername);
        txtUsername.setColumns(10);

        lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        lblUsername.setBounds(82, 125, 79, 20);
        add(lblUsername);

        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        lblPassword.setBounds(82, 175, 79, 20);
        add(lblPassword);

        txtPassword = new JTextField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(241, 169, 134, 35);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        btnLogin.setBounds(82, 230, 117, 35);
        add(btnLogin);

        btnCreateUser = new JButton("Create User");
        btnCreateUser.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));

        btnCreateUser.setBounds(258, 230, 117, 35);
        add(btnCreateUser);

    }

    public void addActionListener (ActionListener e){
        btnLogin.addActionListener(e);
        btnCreateUser.addActionListener(e);
    }

    /***
     * Clearer username og password-felterne på loginskærmen
     */

    public void clearLogin(){
        txtUsername.setText("");
        txtPassword.setText("");
    }

    public void setLblNewLabel(JLabel lblNewLabel) {
        this.lblNewLabel = lblNewLabel;
    }

    public void setLblUsername(JLabel lblUsername) {
        this.lblUsername = lblUsername;
    }

    public void setLblPassword(JLabel lblPassword) {
        this.lblPassword = lblPassword;
    }


    public JLabel getLblNewLabel() {

        return lblNewLabel;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JLabel getLblUsername() {
        return lblUsername;
    }

    public JLabel getLblPassword() {
        return lblPassword;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnCreateUser() {
        return btnCreateUser;
    }

}
