package splatnet.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController extends Controller {

    @FXML
    public void onHomeButtonClick(Event event) {
        System.out.println("Home button clicked");
        try {
            loadNewFxml("homePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onScheduleButtonClick(Event event) {
        System.out.println("Schedule button clicked");

        try {
            loadNewFxml("schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onHistoryButtonClick(Event event) {
        System.out.println("History button clicked");

        try {
            loadNewFxml("historyPage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onRandomWeaponButtonClick() {
        System.out.println("Random weapon button clicked");

        try {
            loadNewFxml("randomWeapon");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSettingButtonClick() {
        System.out.println("Setting button clicked");
    }
}
