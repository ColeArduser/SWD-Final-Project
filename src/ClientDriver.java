import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Handles the creation of interfaces.
 * If GUI, then a controller is made and the JavaFX launch() is called
 * If text mode, the rest of the drivers main is used to handle user interaction
 */
@SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
public class ClientDriver extends Application {
    /**
     * Controller used to switch scenes
     */
    private static Controller controller;

    /**
     * Sets up controller or text mode depending on args
     * Also creates connection to server from given args
     *
     * @param args [0] text mode ("text"), [1]  server ip, [2] server port
     * @throws UnknownHostException If server cannot be found
     * @throws InterruptedException If thread is interrupted
     */
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Client textClient = null;
        switch (args.length) {
            case 3:
                if (args[0].equals("text")) {
                    textClient = new Client(args[1], Integer.parseInt(args[2]));
                    textClient.setTextMode(true);
                } else {
                    controller = new Controller();
                    controller.setIp(args[1]);
                    controller.setPort(Integer.parseInt(args[2]));
                    launch();
                }
                break;
            case 1:
                if (args[0].equals("text")) {
                    textClient = new Client(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt("23704"));
                    textClient.setTextMode(true);
                } else {
                    controller = new Controller();
                    controller.setIp(InetAddress.getLocalHost().getHostAddress());
                    controller.setPort(Integer.parseInt("23704"));
                    launch();
                }
                break;
            default:
                controller = new Controller();
                controller.setIp(InetAddress.getLocalHost().getHostAddress());
                controller.setPort(Integer.parseInt("23704"));
                launch();
        }

        // BEGIN TEXT MODE INTERFACE
        if (textClient != null) {
            Scanner scanner = new Scanner(System.in);
            char input;

            // log in / create account
            System.out.println("Please select to log in or Register");
            System.out.println("[1] Log In");
            System.out.println("[2] Register");
            do {
                input = scanner.nextLine().charAt(0);
                String username;
                switch (input) {

                    case '1': // login
                        System.out.println("Please enter username");
                        username = scanner.nextLine();
                        System.out.println("Please enter password");
                        textClient.sendMessage(String.format("%s,%s,%s\n", Server.sendMessage.LOGIN_REQUEST, username, scanner.nextLine()));
                        break;
                    case '2': // register
                        System.out.println("Please enter username");
                        username = scanner.nextLine();
                        System.out.println("Please enter password");
                        textClient.sendMessage(String.format("%s,%s,%s\n", Server.sendMessage.REGISTER_REQUEST, username, scanner.nextLine()));
                        break;
                }
                Thread.sleep(100);
                if (!textClient.isLoggedIn() && input == '1' || input == '2') {
                    System.out.println("login failed, please try again or register first\n");
                    System.out.println("Please select to log in or Register");
                    System.out.println("[1] Log In");
                    System.out.println("[2] Register");
                }
            } while (!textClient.isLoggedIn());

            // run text mode loop
            System.out.println("Please Select Mode");
            System.out.println("[1] 1 V 1");
            System.out.println("[2] BR");

            // select mode
            do {
                input = scanner.nextLine().charAt(0);
                switch (input) {
                    case '1': // 1v1
                        textClient.sendMessage(String.format("%s,%s\n", Server.sendMessage.MODE_SELECTION, Server.gameMode.ONE_VS_ONE));
                        break;
                    case '2': // BR
                        textClient.sendMessage(String.format("%s,%s\n", Server.sendMessage.MODE_SELECTION, Server.gameMode.BATTLE_ROYAL));
                        break;
                }
                Thread.sleep(100);
                System.out.println("waiting for match to start, press 3 to cancel");
                while (!textClient.isGameStart() && input == '1' || input == '2') {
                    if (scanner.hasNext()) {
                        if (scanner.nextLine().charAt(0) == '3') {
                            textClient.sendMessage(String.format("%s\n", Server.sendMessage.CANCEL_MM));
                            System.out.println("Please Select Mode");
                            System.out.println("[1] 1 V 1");
                            System.out.println("[2] BR");
                            break;
                        }
                    }
                }
            } while (!textClient.isGameStart());

            System.out.println("Enter Guesses!");
            // play game
            while (true) {
                System.out.printf("LETTERS: %s\n", textClient.getLetters());

                while (scanner.hasNextLine()) {
                    System.out.println("GUESS RECEIVED!");
                    String guess = scanner.nextLine();
                    textClient.sendMessage(String.format("%s,%s\n", Server.sendMessage.GUESS, guess));

                    Thread.sleep(100);

                    if (textClient.getGuessResult() != null) {
                        if (textClient.getGuessResult() > 0) {
                            System.out.printf("Scored %s\n", textClient.getGuessResult());
                        } else {
                            System.out.println("Not a word!");
                        }
                        System.out.printf("LETTERS: %s\n", textClient.getLetters());
                    }
                }
            }
        }
    }

    /**
     * Start method that launches the GUI and opens the login page
     *
     * @param primaryStage the Stage to open the scene on
     * @throws IOException if file can't be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlFile = getClass().getResource("LoginFXML.fxml");

        assert fxmlFile != null;
        Parent root = FXMLLoader.load(fxmlFile);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Log In");
        primaryStage.setScene(scene);

        controller.setRoot(root);
        controller.setStage(primaryStage);

        primaryStage.setOnCloseRequest(windowEvent -> {
            System.exit(-1);
        });

        primaryStage.show();
    }
}
