package splatnet.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import splatnet.Main;

import java.io.IOException;

public class Controller {

    private final static String RELATIVE_FXML_PATH = "views/fxml/";

    public void loadNewFxml(String name) throws IOException {
        name = name.replace(".fxml", "");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(RELATIVE_FXML_PATH + name + ".fxml"));
        Stage stage = Main.getPrimaryStage();
        Parent root = fxmlLoader.load();
        stage.getScene().setRoot(root);

    }


}
