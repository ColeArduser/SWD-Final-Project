import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Final_ProjectGUImain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlFile = getClass().getResource("gameFXML.fxml");

        Controller controller = new Controller();

        assert fxmlFile != null;
        Parent root = FXMLLoader.load(fxmlFile);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Word Game");
        primaryStage.setScene(scene);

        controller.setRoot(root);
        controller.setStage(primaryStage);
        //controller.setScene(scene);

        primaryStage.show();
    }
}
