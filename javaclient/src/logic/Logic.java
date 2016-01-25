package logic;
import model.Game;
import model.Score;
import model.User;
import sdk.Sdk;
import gui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Logic {

    private SnakeScreen screen; //Selve Framet
    private Sdk sdk; //SDK
    private User currentUser; //Den aktuelle bruger

    public Logic(){
        sdk = new Sdk();
        currentUser = null;
    }

    public void run(){
        //Frame oprettes og ActionListeners injectes
        screen = new SnakeScreen();
        screen.getLogin().addActionListener(new LoginActionListener());
        screen.getMainMenu().addActionListener(new MainMenuActionListener());
    }

    /***
     * Logik til at fange tomme JTextField felter
     * @param obj Liste af enhver type java-objekter
     * @return Hvorvidt der findes et tomt JTextField-felt i listen
     */
    public boolean userInfoMissing(Object[] obj){

        for (Object field : obj){
            if(field instanceof JTextField)
                if(((JTextField) field).getText().equals("") || ((JTextField) field).getText().equals(null))
                return true;
        }
        return false;
    }
    /***
     * Logikken afvikles for den injectede actionlistener
     */
    public class LoginActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(e.getSource() == screen.getLogin().getBtnLogin()){

                String username = screen.getLogin().getTxtUsername().getText();
                String password = screen.getLogin().getTxtPassword().getText();

                if(!username.equals("") && !password.equals("")) {

                    currentUser = sdk.login(username, password);

                    if (currentUser != null) {

                        screen.getMainMenu().getTable().setModel(sdk.getGames());
                        screen.show(screen.MainMenu);
                    }

                }else{
                    JOptionPane.showMessageDialog(screen, "Please enter username and password!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getSource() == screen.getLogin().getBtnCreateUser()){

                    JTextField username = new JTextField();
                    JTextField password = new JPasswordField();
                    JTextField password2 = new JPasswordField();
                    JTextField firstName = new JTextField();
                    JTextField lastName = new JTextField();
                    JTextField email = new JTextField();

                    Object[] userInfo = {"Username:", username, "Password:", password, "Retype password:", password2,
                    "First name:", firstName, "Last name:", lastName, "Email:", email};

                    int option = JOptionPane.showConfirmDialog(screen, userInfo, "Create User", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION){
                            if (!userInfoMissing(userInfo)) {
                                if(password.getText().equals(password2.getText())) {
                                    User usr = new User();
                                    usr.setUsername(username.getText());
                                    usr.setPassword(password.getText());
                                    usr.setFirstName(firstName.getText());
                                    usr.setLastName(lastName.getText());
                                    usr.setEmail(email.getText());

                                    if(sdk.createUser(usr) != null){
                                        JOptionPane.showMessageDialog(screen, "User: " + usr.getUsername() + " created!",
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(screen, "Password fields must match",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }else {
                                JOptionPane.showMessageDialog(screen, "Please fill out all fields",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                    }
            }

        }

    }

    /***
     * Logikken afvikles for den injectede actionlistener
     */
    public class MainMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(e.getSource() == screen.getMainMenu().getBtnLogOut()){
                currentUser = null;
                screen.getLogin().clearLogin();
                screen.show(screen.Login);
            }
            else if(e.getSource() == screen.getMainMenu().getBtnUpdateTable()) {
                //screen.getMainMenu().getTable().clearSelection();
                screen.getMainMenu().getTable().setModel(sdk.getGames());
            }
            else if (e.getSource() == screen.getMainMenu().getBtnJoin()){
                int selectedGameId = Integer.parseInt(screen.getMainMenu().getTable().getValueAt(
                                screen.getMainMenu().getTable().getSelectedRow(), 0).toString());
                int selectedGameSize = Integer.parseInt(screen.getMainMenu().getTable().getValueAt(
                        screen.getMainMenu().getTable().getSelectedRow(), 2).toString());
                int joinGame = JOptionPane.showConfirmDialog(screen, "Do you want to join game " + selectedGameId,
                        "Join game?", JOptionPane.OK_CANCEL_OPTION);
                if (joinGame == JOptionPane.OK_OPTION){

                    JTextField moves = new JTextField();
                    Object[] userInfo = {"Your moves", moves};
                    int startGame = JOptionPane.showConfirmDialog(screen, userInfo,
                            "Enter moves under " + selectedGameSize + " characters!", JOptionPane.OK_CANCEL_OPTION);

                    String stringMoves = moves.getText();

                    if(startGame == JOptionPane.OK_OPTION && !stringMoves.equals("")){
                        if(!stringMoves.matches(".*\\d.*") && stringMoves.length() <= selectedGameSize){
                            if(sdk.joinGame(selectedGameId,currentUser).equals("Success")){
                                Game game = sdk.startGame(selectedGameId, stringMoves);
                                if (game != null){
                                    if(game.getOpponent().isWinner()){

                                        JOptionPane.showMessageDialog(screen, "You won with " + game.getOpponent().getScore() + " points!",
                                                "Game ended!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(screen, "You lost!",
                                                "Game ended!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                    screen.getMainMenu().getTable().setModel(sdk.getGames());
                                }else{
                                    JOptionPane.showMessageDialog(screen, "Something went wrong",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else {
                            JOptionPane.showMessageDialog(screen,
                                    "Restrict moves to map size.\n" +
                                            "Only WASD are valid moves.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
            else if (e.getSource() == screen.getMainMenu().getBtnDelete()){
                int selectedGameId = Integer.parseInt(screen.getMainMenu().getTable().getValueAt(
                        screen.getMainMenu().getTable().getSelectedRow(), 0).toString());
                int startGame = JOptionPane.showConfirmDialog(screen, "Delete game " + selectedGameId + "?",
                        "Delete Game", JOptionPane.OK_CANCEL_OPTION);
                if(startGame == JOptionPane.OK_OPTION){
                    String response = sdk.deleteGame(selectedGameId);
                    if(response != null) {
                        JOptionPane.showMessageDialog(screen, "Game " + selectedGameId + " deleted!",
                                "Game deleted", JOptionPane.INFORMATION_MESSAGE);
                        screen.getMainMenu().getTable().setModel(sdk.getGames());
                    }
                    else{
                        JOptionPane.showMessageDialog(screen, "Something went wrong",
                                "Game deletion", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else if (e.getSource() == screen.getMainMenu().getBtnCreate()){

                JTextField gameName = new JTextField();
                JTextField gameSize = new JTextField();
                JTextField gameControls = new JTextField();

                Object[] gameInfo = {"Game name: ", gameName, "Game size: ", gameSize, "Your controls: ", gameControls};

                int option = JOptionPane.showConfirmDialog(screen, gameInfo, "Create Game", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION){
                    if (!userInfoMissing(gameInfo)) {

                        try {
                            int mapSize = Integer.parseInt(gameSize.getText());

                            if (!gameControls.getText().matches(".*\\d.*") && gameControls.getText().length() <= mapSize) {
                                Game game = new Game();
                                game.setName(gameName.getText());
                                game.setMapSize(mapSize);

                                if (sdk.createGame(game, currentUser, gameControls.getText()) != null) {
                                    JOptionPane.showMessageDialog(screen, "Game: " + game.getName() + " was created!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                                    screen.getMainMenu().getTable().setModel(sdk.getGames());
                                }
                            } else {
                                JOptionPane.showMessageDialog(screen,
                                        "Restrict moves to map size.\n" +
                                                "Only WASD are valid moves.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ne) {
                            JOptionPane.showMessageDialog(screen, "Only WASD are valid moves.", "Error", JOptionPane.ERROR_MESSAGE);

                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(screen, "Please fill out all fields",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else if (e.getSource() == screen.getMainMenu().getBtnHighscore()){

                ArrayList<Score> highScores = sdk.getScores();
                StringBuilder scores = new StringBuilder();
                for (Score s : highScores){
                     scores.append(s.getGame().getWinner().getUsername() + " : " + s.getScore() + " points \n");
                }

                String builtScores = scores.toString();

                JOptionPane.showMessageDialog(screen, builtScores,
                        "High scores", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }

}
