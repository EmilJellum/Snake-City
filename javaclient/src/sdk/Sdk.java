package sdk;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import model.Game;
import model.Gamer;
import model.Score;
import model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;

public class Sdk {

    private String address; //Web service IP
    private int port; //Web service port
    private String serverRoot; //Web service roden

    public Sdk(){
        address = "localhost";
        port = 13867;
        serverRoot = "http://" + address + ":" + port + "/api/";
    }

    /***
     *  Returnerer en bruger fra serveren
     * @param userId Id på den forespurgte bruger
     * @return Returnerer instatieret User-objekt
     */
    //Returnerer en bruger fra serveren
    public User getUser(int userId){

        Client client = Client.create(); // Socket oprettes
        WebResource webResource = client.resource(serverRoot + "users/" + userId); //Requester resource
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class); //Behandler response

        if (response.getStatus() == 200) { //Hvis OK
            String entity = response.getEntity(String.class); //JSON udtrækkes fra response
            return new Gson().fromJson(entity, User.class); //JSON instantieres
        }
        return null;
    }

    /***
     * Opretter en bruger på serveren
     * @param user Bruger der skal oprettes på serveren
     * @return Response besked fra server
     */
    public String createUser(User user){

        String jsonUser = new Gson().toJson(user);

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "users/");
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonUser);

        if (response.getStatus() == 200) {
            String entity = response.getEntity(String.class);
            return entity;
        }
        return null;
    }

    /***
     * Logger en bruger ind
     * @param username Brugerens username
     * @param password Brugerens password
     * @return Et instantieret brugerobjekt fra serveren
     */
    public User login(String username, String password){

        String jsonLogin = "{'username': " + username + ", 'password': " + password + "}";

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "login/");
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonLogin);
        String jsonId = response.getEntity(String.class);

        if (response.getStatus() == 200) {
            try {
                JSONParser parser = new JSONParser();

                Object object = parser.parse(jsonId);
                JSONObject jsonObject = (JSONObject) object;

                return getUser((int)(long) jsonObject.get("userid"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * Henter et spil fra serveren
     * @param gameId Id på det forespurgte spil
     * @return Et instantieret Game-objekt
     */
    public Game getGame(int gameId){

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "game/" + gameId);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if (response.getStatus() == 200) {
            String entity = response.getEntity(String.class);
            return new Gson().fromJson(entity, Game.class);
        }
        return null;
    }

    /***
     * Henter alle åbne spil fra serveren
     * @return Et table med åbne spil (game-objekter) fra serveren
     */
    public DefaultTableModel getGames(){

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "games/open/");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if (response.getStatus() == 201) {
            String entity = response.getEntity(String.class);
            ArrayList<Game> games = new Gson().fromJson(entity, new TypeToken<ArrayList<Game>>(){}.getType());

            DefaultTableModel jtabledata = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            Vector<String> columns_name = new Vector<String>();
            Vector<String> data_rows;
            columns_name.addElement("Game Id");
            columns_name.addElement("Host Id");
            columns_name.addElement("Size");
            jtabledata.setColumnIdentifiers(columns_name);

            for(Game rows : games){
                 data_rows = new Vector<String>();

                data_rows.addElement(String.valueOf(rows.getGameId()));
                data_rows.addElement(String.valueOf(rows.getHost().getId()));
                data_rows.addElement(String.valueOf(rows.getMapSize()));

                jtabledata.addRow(data_rows);
            }

            return jtabledata;

        }
        return null;
    }

    /***
     * Henter high score fra serveren
     * @return En ArrayList af Score-objekter
     */
    public ArrayList<Score> getScores(){

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "scores/");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if (response.getStatus() == 200) {
            String entity = response.getEntity(String.class);
            return new Gson().fromJson(entity, new TypeToken<ArrayList<Score>>(){}.getType());
        }
        return null;
    }

    /***
     * Lader en bruger joine et åbent spil
     * @param gameId Id på det pågældende spil
     * @param user User-objektet for den indloggede bruger
     * @return Response besked fra server
     */
    public String joinGame(int gameId, User user){

        Game currentGame = getGame(gameId);

        Gamer gamer = new Gamer();
        gamer.setId(user.getId());

        currentGame.setOpponent(gamer);

        String jsonGame = new Gson().toJson(currentGame);

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "games/join/");
        ClientResponse response = webResource.type("application/json").put(ClientResponse.class, jsonGame);

        if (response.getStatus() == 201) {
            return "Success";
        }
        return null;
    }

    /***
     * Lader en bruger starte/afvikle et spil
     * @param gameId Det pågældende spils Id
     * @param controls Den indloggede brugers styringshandlinger
     * @return Et instantieret spilobjekt af det i kaldet afviklede spil
     */
    public Game startGame(int gameId, String controls){
        Game currentGame = getGame(gameId);
        currentGame.getOpponent().setControls(controls);

        String jsonGame = new Gson().toJson(currentGame);

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "games/start/");
        ClientResponse response = webResource.type("application/json").put(ClientResponse.class, jsonGame);

        if (response.getStatus() == 201) {
            String entity = response.getEntity(String.class);
            return new Gson().fromJson(entity, Game.class);
        }
        return null;
    }

    /***
     * Sletter et spil fra serveren
     * @param gameId Det pågældende spils Id
     * @return Response besked
     */
    public String deleteGame(int gameId){

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "games/" + gameId);
        ClientResponse response = webResource.type("application/json").delete(ClientResponse.class);

        if (response.getStatus() == 200) {
            return response.getEntity(String.class);
        }
        return null;
    }

    /***
     * Opretter et nyt spil på serveren
     * @param game Det allerede instantierede Game-objekt
     * @param user Den indloggede brugers User-objekt
     * @param controls Den indloggede brugers styringshandlinger
     * @return Response besked fra server
     */
    public String createGame(Game game, User user, String controls){

        Game newGame = game;
        Gamer host = new Gamer();
        host.setId(user.getId());
        host.setFirstName(user.getFirstName());
        host.setLastName(user.getLastName());
        host.setUsername(user.getUsername());
        host.setPassword(user.getPassword());
        host.setEmail(user.getEmail());
        host.setControls(controls);
        newGame.setHost(host);

        String jsonGame = new Gson().toJson(newGame);

        Client client = Client.create();
        WebResource webResource = client.resource(serverRoot + "games/");
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonGame);

        if (response.getStatus() == 201) {
            String entity = response.getEntity(String.class);
            return entity;
        }
        return null;
    }

}
