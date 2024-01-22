package splatnet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import splatnet.controllers.Controller;

import java.io.IOException;

public class MainController extends Controller {

    @FXML
    public void initialize() {
        System.out.println("MainController initialized");
    }

    public void startApplication() {
        System.out.println("Button clicked");
        try {
            loadNewFxml("homePage");
        } catch (IOException e) {
            System.out.println("Error while loading new fxml");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

    }

}
