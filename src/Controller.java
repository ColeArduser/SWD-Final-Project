import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Controller class that all the controllers for each javaFX stage inherit from. Defines methods that are override in
 * the derived classes
 * @see LoginFXMLController
 * @see HomeScreenController
 * @see gameController
 * @see gameResultsController
 * @see SignUpPageController
 */
public class Controller {
    /**
     * Member variable that holds the user's IP
     */
    private static String ip;
    /**
     * Member variable that holds the user's port they want to use
     */
    private static int port;
    /**
     * Member variable that holds the Client class the controller uses to communicate with Server
     */
    private static Client client;
    /**
     * PlayerStats member variable that represents the current players stats
     */
    private static PlayerStats player;
    /**
     * TournamentStats member variable for holding the current players tourney stats
     */
    private static TournamentStats tournament;
    /**
     * Member variable for the Stage's X position
     */
    private static double xPos;
    /**
     * Member variable for the Stage's Y position
     */
    private static double yPos;
    /**
     * Member variable for the current Scene
     */
    private static Scene scene;
    /**
     * Member variable for the current Stage
     */
    private static Stage stage;
    /**
     * Member variable for the root of the Stage
     */
    private static Parent root;
    /**
     * Getter method for playerStats
     * @return the playerStats reference
     */
    public static PlayerStats getPlayer() {
        return player;
    }
    /**
     * Setter method for playerStats
     * @param player the playerStats reference
     */
    public static void setPlayer(PlayerStats player) {
        Controller.player = player;
    }
    /**
     * Getter method for tournamentStats
     * @return tournament reference
     */
    public static TournamentStats getTournament() {
        return tournament;
    }
    /**
     * Setter method for tournamentStats
     * @param tournament reference
     */
    public static void setTournament(TournamentStats tournament) {
        Controller.tournament = tournament;
    }
    /**
     * Getter method for the Stage's X position
     * @return the Stage's X position
     */
    public double getxPos() {
        return xPos;
    }
    /**
     * Setter method for the Stage's x position
     * @param xPos the X position
     */
    public void setxPos(double xPos) {
        Controller.xPos = xPos;
    }
    /**
     * Getter method for the Stage's Y position
     * @return the Stage's Y position
     */
    public double getyPos() {
        return yPos;
    }
    /**
     * Setter method for the Stage's Y position
     * @param yPos the Y position
     */
    public void setyPos(double yPos) {
        Controller.yPos = yPos;
    }
    /**
     * Getter method for the user's IP
     * @return the user's IP
     */
    public String getIp() {
        return ip;
    }
    /**
     * Setter method for the user's IP
     * @param ip the IP
     */
    public void setIp(String ip) {
        Controller.ip = ip;
    }
    /**
     * Getter method for the user's port
     * @return the user's port number
     */
    public int getPort() {
        return port;
    }
    /**
     * Setter method for the user's port
     * @param port the user port
     */
    public void setPort(int port) {
        Controller.port = port;
    }
    /**
     * Getter method for the Controller's client
     * @return the client reference
     */
    public Client getClient() {
        return client;
    }
    /**
     * Setter method for the Client
     * @param client the Client reference
     */
    public static void setClient(Client client) {
        Controller.client = client;
    }
    /**
     * Getter method for the Scene
     * @return the current scene
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Setter method for Scene
     * @param scene the Scene
     */
    public void setScene(Scene scene) {
        Controller.scene = scene;
    }
    /**
     * Getter method for the Stage
     * @return the current Stage
     */
    public Stage getStage() {
        return stage;
    }
    /**
     * Setter method for the Stage
     * @param stage the Stage
     */
    public void setStage(Stage stage) {
        Controller.stage = stage;
    }
    /**
     * Getter method for the Root of the Stage
     * @return the root of the Stage
     */
    public Parent getRoot() {
        return root;
    }
    /**
     * Setter method for the Root of the Stage
     * @param root the root of the Stage
     */
    public void setRoot(Parent root) {
        Controller.root = root;
    }
    /**
     * switchScene method for closing the current stage and opening a new one by loading the FXML file for at the
     * passed in path
     * @param path the path of the FXML file to load
     * @param sceneTitle the title for the new scene
     */
    public void switchScene(String path, String sceneTitle) throws IOException {

        Stage temp2 = stage;

        setyPos(stage.getY());
        setxPos(stage.getX());
        Stage tempStage = new Stage();
        setStage(tempStage);

        URL fxmlFile = getClass().getResource(path);
        assert fxmlFile != null;
        setRoot(FXMLLoader.load(fxmlFile));
        setScene(new Scene(getRoot()));
        getStage().setScene(getScene());

        stage.setTitle(sceneTitle);
        stage.setX(getxPos());
        stage.setY(getyPos());

        getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.out.println("Closing Client");
                if (!stage.getTitle().equals("Log In")) {
                    getClient().sendMessage(String.format("%s\n", Server.sendMessage.CLIENT_DISCONNECT));
                }
                System.exit(-1);
            }
        });

        getStage().show();
        temp2.close();
    }

    /**
     * updateTimer method that gameController overrides and uses to update the timer
     * @
     */
    public void updateTimer(int time) {
    }
    public void updateMatchCountDown(int time) {}
    public void updatePlayersConnected(int numPlayers){}

    public void loginInvalid() {
    }

    public void loginValid() {
    }

    public void signUpValid() {
    }

    public void signUpInvalid() {
    }

    public void gameStart() {
    }

    public void endGame() {
    }

    public void displayResults(String[] clientMessage) {
    }

    public void guessResult(int score) {
    }

    public void updatePlayerStatsScreen() {
    }

    public void updatePlayerStats(String username, String totalWins, String totalGamesPlayed, String OVOWins, String OVOGamesPlayed,
                                  String BRWins, String BRGamesPlayed, String tournamentWins, String tournamentsPlayed) {
        player.setUsername(username);
        player.setTotalWins(totalWins);
        player.setTotalGamesPlayed(totalGamesPlayed);
        player.setOVOWins(OVOWins);
        player.setOVOGamesPlayed(OVOGamesPlayed);
        player.setBRWins(BRWins);
        player.setBRGamesPlayed(BRGamesPlayed);
        player.setTournamentWins(tournamentWins);
        player.setTournamentsPlayed(tournamentsPlayed);
    }

    public void updateTSLeader(String rank, String username, String tournamentWins, String tournamentGamesLeft) {
        tournament.setUsername(username);
        tournament.setTournamentWins(Integer.parseInt(tournamentWins));
        tournament.setTournamentGamesLeft(Integer.parseInt(tournamentGamesLeft));
    }

    public void updateTSUser(String rank, String username, String tournamentWins, String tournamentGamesLeft) {
        tournament.setUsername(username);
        tournament.setTournamentWins(Integer.parseInt(tournamentWins));
        tournament.setTournamentGamesLeft(Integer.parseInt(tournamentGamesLeft));
    }


}
