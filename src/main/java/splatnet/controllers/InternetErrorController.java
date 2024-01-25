package splatnet.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import splatnet.Main;

public class InternetErrorController extends Controller {


    @FXML
    public void exitAll() {

        // close the main stage
        Stage main = Main.getPrimaryStage();
        main.close();

    }

}
