package splatnet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private final static int WIDTH = 1080;
    private final static int HEIGHT = 720;

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/fxml/welcome.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("views/fxml/apiError.fxml"));
            root = fxmlLoader.load();
        }
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Splatnet 3");
        stage.setScene(scene);

        primaryStage = stage;

        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}