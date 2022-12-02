import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Controller {
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private Client client;

    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public Parent getRoot() {
        return root;
    }
    public void setRoot(Parent root) {
        this.root = root;
    }

    private static Scene scene;
    private static Stage stage;
    private static Parent root;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Controller.username = username;
    }

    private static String username;

    public void switchScene(String fxmlUrl, String sceneTitle) throws IOException {

        Stage temp2 = stage;

        Stage tempStage = new Stage();

        setStage(tempStage);

        URL fxmlFile = getClass().getResource(fxmlUrl);
        assert fxmlFile != null;
        setRoot(FXMLLoader.load(fxmlFile));
        setScene(new Scene(getRoot()));
        getStage().setScene(getScene());

        stage.setTitle(sceneTitle);
        getStage().show();

        temp2.close();
    }
}
