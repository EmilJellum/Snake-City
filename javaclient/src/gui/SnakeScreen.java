package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SnakeScreen extends JFrame {
    public static final String Login = "login";
    public static final String MainMenu = "mainmenu";
    private JPanel contentPane;
    public Login login;
    public MainMenu mainMenu;
    private CardLayout c;

    public SnakeScreen() {
        setTitle("Snake City!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 500, 450);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        setVisible(true);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new CardLayout(0,0));

        login = new Login();
        contentPane.add(login, Login);

        mainMenu = new MainMenu();
        contentPane.add(mainMenu, MainMenu);

        c = (CardLayout) getContentPane().getLayout();
    }

    public Login getLogin(){
        return login;
    }

    public MainMenu getMainMenu(){
        return mainMenu;
    }

    public void show(String card){
        c.show(this.getContentPane(),card);
    }
}

