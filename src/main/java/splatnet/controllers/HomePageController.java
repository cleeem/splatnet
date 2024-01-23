package splatnet.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import splatnet.Main;
import splatnet.s3s.S3SMain;
import splatnet.s3s.classes.Friend;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Player;


import java.util.ArrayList;
import java.util.HashMap;

public class HomePageController {

    private Player myLastGamePlayer;

    private HashMap<String, String> xPowers;

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
    public HBox powerHolder;

    @FXML
    public HBox bannerHolder;



    @FXML
    public void initialize() {

        System.out.println("HomePageController initialized");

//         use a thread to fetch last game data and another to call fetchXPowers
        Thread fetchLastGameData = new Thread(() -> {
            myLastGamePlayer = S3SMain.fetchMyLastGameData();
            Platform.runLater(this::displayLastGameInfos);
        });
        System.out.println("Starting fetchLastGameData thread");
        fetchLastGameData.start();

        Thread fetchXPowersData = new Thread(() -> {
            xPowers = S3SMain.fetchXPowers();
            Platform.runLater(this::displayXPowers);
        });
        System.out.println("Starting fetchXPowersData thread");
        fetchXPowersData.start();


    }

    private void displayXPowers() {

        VBox zonesVBox = new VBox();
        zonesVBox.setAlignment(Pos.CENTER);
        ImageView zonesImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Splat_Zones.png")));
        zonesImage.setFitHeight(128);
        zonesImage.setFitWidth(128);
        zonesVBox.getChildren().add(zonesImage);
        Label zonesLabel = new Label(xPowers.get("zones").substring(0, 7));
        zonesLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        zonesVBox.getChildren().add(zonesLabel);

        VBox towerVBox = new VBox();
        towerVBox.setAlignment(Pos.CENTER);
        ImageView towerImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Tower_Control.png")));
        towerImage.setFitHeight(128);
        towerImage.setFitWidth(128);
        towerVBox.getChildren().add(towerImage);
        Label towerLabel = new Label(xPowers.get("tower").substring(0, 7));
        towerLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        towerVBox.getChildren().add(towerLabel);

        VBox rainmakerVBox = new VBox();
        rainmakerVBox.setAlignment(Pos.CENTER);
        ImageView rainImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Rainmaker.png")));
        rainImage.setFitHeight(128);
        rainImage.setFitWidth(128);
        rainmakerVBox.getChildren().add(rainImage);
        Label rainmakerLabel = new Label(xPowers.get("rainmaker").substring(0, 7));
        rainmakerLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        rainmakerVBox.getChildren().add(rainmakerLabel);

        VBox clamVBox = new VBox();
        clamVBox.setAlignment(Pos.CENTER);
        ImageView clamImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Clam_Blitz.png")));
        clamImage.setFitHeight(128);
        clamImage.setFitWidth(128);
        clamVBox.getChildren().add(clamImage);
        Label clamLabel = new Label(xPowers.get("clam").substring(0, 7));
        clamLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        clamVBox.getChildren().add(clamLabel);

        powerHolder.getChildren().add(zonesVBox);
        powerHolder.getChildren().add(towerVBox);
        powerHolder.getChildren().add(rainmakerVBox);
        powerHolder.getChildren().add(clamVBox);


    }

    private void displayLastGameInfos() {

        System.out.println("Displaying last game infos");

        String bannerUrl = myLastGamePlayer.getBannerUrl();

        ImageView banner = new ImageView(new Image(bannerUrl));
        banner.setFitHeight(200);
        banner.setFitWidth(668);
        bannerHolder.getChildren().add(banner);

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