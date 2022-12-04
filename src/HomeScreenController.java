import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class HomeScreenController extends Controller {
    @FXML
    private Pane statsPane;

    @FXML
    private Label totalWins;

    @FXML
    private Label tourneyWins;

    @FXML
    private Label brWins;

    @FXML
    private Label gamesPlayed;

    @FXML
    private Label h2hGames;

    @FXML
    private Label tourneysPlayed;

    @FXML
    private Label brPlayed;

    @FXML
    private Label usernameLabel;

    @FXML
    private JFXButton h2hMode;

    @FXML
    private JFXButton battleRoyale;

    @FXML
    private JFXButton readyUp;

    @FXML
    private Label gameModeFeedback;

    @FXML
    private Label gameStatus;

    @FXML
    private JFXButton tournamentMode;

    @FXML
    private Label h2hWins;

    @FXML
    private JFXButton logoutConfirmationYes;
    @FXML
    private JFXButton logoutConfirmationNo;

    private boolean readiedUp;
    private boolean logout;

    @FXML
    private JFXButton logOutButton;

    @FXML
    void readyUpListener() {

        if(!readiedUp) {
            if (battleRoyale.getBackground().getFills().get(0).getFill().equals(Color.GREEN)) {
                // send start game message to server with battle royale
                gameStatus.setText("Connecting to Game...");
                readyUp.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
                getClient().sendMessage(String.format("%s,%s\n", Server.sendMessage.MODE_SELECTION, Server.gameMode.BATTLE_ROYAL));
                readiedUp = true;
                readyUp.setText("Cancel");
            } else if (h2hMode.getBackground().getFills().get(0).getFill().equals(Color.GREEN)) {
                // send start game message to server with head to head
                gameStatus.setText("Connecting to Game...");
                readyUp.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
                getClient().sendMessage(String.format("%s,%s\n", Server.sendMessage.MODE_SELECTION, Server.gameMode.ONE_VS_ONE));
                readiedUp = true;
                readyUp.setText("Cancel");
            } else {
                gameModeFeedback.setText("Select a Game Mode");
                gameModeFeedback.setTextFill(Color.RED);
            }
        }
        else {
            // send cancel message to server
            getClient().sendMessage(String.format("%s\n", Server.sendMessage.CANCEL_MM));
            readyUp.setText("Ready Up!");
            readyUp.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            h2hMode.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            battleRoyale.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            gameStatus.setText("");
            readiedUp = false;
        }
    }

    @FXML
    void h2hListener() {
        if(!readiedUp) {
            gameModeFeedback.setText("");
            battleRoyale.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            h2hMode.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        }
    }

    @FXML
    void battleRoyaleListener() {
        if(!readiedUp) {
            gameModeFeedback.setText("");
            h2hMode.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            battleRoyale.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        }
    }

    @FXML
    void logOutListener() {
      logout = true;
      logOutButton.setText("Are You Sure?");
      logoutConfirmationNo.setText("No");
      logoutConfirmationYes.setText("Yes");
    }

    @FXML
    void logOutConfirmationYesListener() {
        if(logout) {
            try {
                getClient().sendMessage(String.format("%s\n", Server.sendMessage.LOGOUT_REQUEST));
                setPlayer(null);
                switchScene("LoginFXML.fxml", "Login");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void logOutConfirmationNoListener() {
        if(logout) {
            logOutButton.setText("Logout");
            logoutConfirmationNo.setText("");
            logoutConfirmationYes.setText("");
            logout = false;
        }
    }

    @FXML
    void tournamentModeListener() {

        //getClient().sendMessage(String.format("%s,%s,%s\n",Server.sendMessage.MODE_SELECTION ,"Tournament", ""));
        if(!readiedUp) {
            try {
                switchScene("StatsPage.fxml", "Tournament Home");
            } catch (IOException e) {
                gameModeFeedback.setText("Could not Open Tournament Mode");
            }
        }
    }

    public void initialize() {
        readiedUp = false;
        logout = false;
        getClient().setController(this);
        getClient().sendMessage(String.format("%s\n", Server.sendMessage.CLIENT_DATA_REQUEST));
        usernameLabel.setText(getPlayer().getUsername() + "'s Player Stats");
        statsPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        h2hMode.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        battleRoyale.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        tournamentMode.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
    }

    @Override
    public void updatePlayerStatsScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                totalWins.setText(getPlayer().getTotalWins());
                gamesPlayed.setText(getPlayer().getTotalGamesPlayed());
                h2hWins.setText(getPlayer().getOVOWins());
                h2hGames.setText(getPlayer().getOVOGamesPlayed());
                brWins.setText(getPlayer().getBRWins());
                brPlayed.setText(getPlayer().getBRGamesPlayed());
                tourneyWins.setText(getPlayer().getTournamentWins());
                tourneysPlayed.setText(getPlayer().getTournamentsPlayed());
            }
        });
    }

    @Override
    public void gameStart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    switchScene("gameFXML.fxml", "Game");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}