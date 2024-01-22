package splatnet.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class HomePageController {

    @FXML
    public Button homeButton;

    @FXML
    public Button scheduleButton;

    @FXML
    public Button historyButton;

    @FXML
    public Button salmonButton;

    @FXML
    public Button settingButton;

    @FXML
    public ScrollPane friendList;
    
    @FXML
    public void initialize() {
        System.out.println("HomePageController initialized");
    }

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