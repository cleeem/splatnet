package splatnet.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;

public class MenuController extends Controller {

    @FXML
    public void onHomeButtonClick(Event event) {
        System.out.println("Home button clicked");
        System.out.println(event);

    }

    @FXML
    public void onScheduleButtonClick() {
        System.out.println("Schedule button clicked");
    }

    @FXML
    public void onHistoryButtonClick() {
        System.out.println("History button clicked");

        try {
            loadNewFxml("historyPage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onSalmonButtonClick() {
        System.out.println("Salmon button clicked");
    }

    @FXML
    public void onSettingButtonClick() {
        System.out.println("Setting button clicked");
    }
}
