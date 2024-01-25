package splatnet.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import splatnet.Main;

import java.awt.*;
import java.io.IOException;

public class Controller {

    private final static String RELATIVE_FXML_PATH = "views/fxml/";


    public void loadNewFxml(String name) throws IOException {
        name = name.replace(".fxml", "");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(RELATIVE_FXML_PATH + name + ".fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.getPrimaryStage();
        if (stage == null) {
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            stage.getScene().setRoot(root);
        }

    }





}
