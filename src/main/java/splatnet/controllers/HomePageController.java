package splatnet.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import splatnet.Main;
import splatnet.models.Storage;
import splatnet.s3s.S3SMain;
import splatnet.s3s.classes.Friend;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Player;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePageController extends Controller {

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
    public Pane bannerHolder;

    @FXML
    public VBox weaponHolder;

    @FXML
    public HBox gearHolder;


    @FXML
    public void initialize() {

        System.out.println("HomePageController initialized");


        // use a thread to fetch last game data and another to call fetchXPowers
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

        Storage storage = Storage.getInstance();

        storage.setPlayerData(myLastGamePlayer);
        storage.setxPowers(xPowers);


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

        displayWeapon();
        displayStuffs();

        System.out.println("Displaying last game infos");

        String bannerUrl = myLastGamePlayer.getBannerUrl();

        ImageView banner = new ImageView(new Image(bannerUrl));
        banner.setFitHeight(200);
        banner.setFitWidth(668);

        Label quoteLabel = new Label(myLastGamePlayer.getQuote());
        quoteLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the quote in the upper left corner of the banner
        quoteLabel.setLayoutX(15);
        quoteLabel.setLayoutY(0);
        quoteLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 20));

        Label nameLabel = new Label(myLastGamePlayer.getName());
        nameLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the name in center
        nameLabel.setLayoutX(bannerHolder.getWidth() / 2 - 55);
        nameLabel.setLayoutY(bannerHolder.getHeight() / 2 - 35);
        nameLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 30));


        Label idLabel = new Label("#" + myLastGamePlayer.getNameId());
        idLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the id in the lower left corner of the banner
        idLabel.setLayoutX(15);
        idLabel.setLayoutY(bannerHolder.getHeight() / 2 + 45);
        idLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 20));

        bannerHolder.getChildren().clear();
        bannerHolder.getChildren().add(banner);
        bannerHolder.getChildren().add(quoteLabel);
        bannerHolder.getChildren().add(nameLabel);
        bannerHolder.getChildren().add(idLabel);

    }

    private void displayStuffs() {

        JsonObject headGear = myLastGamePlayer.getHeadGear();
        JsonObject clothes = myLastGamePlayer.getClothingGear();
        JsonObject shoes = myLastGamePlayer.getShoesGear();

        VBox headGearVBox = new VBox();
        headGearVBox.setAlignment(Pos.CENTER);
        HBox headGearHBox = new HBox();
        headGearHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView headGearImage = new ImageView(new Image(headGear.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        headGearImage.setFitHeight(128);
        headGearImage.setFitWidth(128);

        ImageView headGearMain = new ImageView(new Image(headGear.get("primaryGearPower").getAsJsonObject()
                                                                          .get("image").getAsJsonObject()
                                                                          .get("url").getAsString()));
        headGearMain.setFitHeight(60);
        headGearMain.setFitWidth(60);
        headGearHBox.getChildren().add(headGearMain);

        for (JsonElement sub : headGear.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            headGearHBox.getChildren().add(subAbilityImage);
        }

        headGearVBox.getChildren().add(headGearImage);
        headGearVBox.getChildren().add(headGearHBox);


        VBox clothesVBox = new VBox();
        clothesVBox.setAlignment(Pos.CENTER);
        HBox clothesHBox = new HBox();
        clothesHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView clothesImage = new ImageView(new Image(clothes.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        clothesImage.setFitHeight(128);
        clothesImage.setFitWidth(128);

        ImageView clothesGearMain = new ImageView(new Image(clothes.get("primaryGearPower").getAsJsonObject()
                                                                           .get("image").getAsJsonObject()
                                                                           .get("url").getAsString()));
        clothesGearMain.setFitHeight(60);
        clothesGearMain.setFitWidth(60);
        clothesHBox.getChildren().add(clothesGearMain);

        for (JsonElement sub : clothes.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            clothesHBox.getChildren().add(subAbilityImage);
        }

        clothesVBox.getChildren().add(clothesImage);
        clothesVBox.getChildren().add(clothesHBox);


        VBox shoesVBox = new VBox();
        shoesVBox.setAlignment(Pos.CENTER);
        HBox shoesHBox = new HBox();
        shoesHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView shoesImage = new ImageView(new Image(shoes.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        shoesImage.setFitHeight(128);
        shoesImage.setFitWidth(128);

        ImageView shoesGearMain = new ImageView(new Image(shoes.get("primaryGearPower").getAsJsonObject()
                                                                           .get("image").getAsJsonObject()
                                                                           .get("url").getAsString()));
        shoesGearMain.setFitHeight(60);
        shoesGearMain.setFitWidth(60);
        shoesHBox.getChildren().add(shoesGearMain);

        for (JsonElement sub : shoes.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            shoesHBox.getChildren().add(subAbilityImage);
        }

        shoesVBox.getChildren().add(shoesImage);
        shoesVBox.getChildren().add(shoesHBox);

        gearHolder.getChildren().add(headGearVBox);
        gearHolder.getChildren().add(clothesVBox);
        gearHolder.getChildren().add(shoesVBox);


    }

    private void displayWeapon() {

        JsonObject weaponData = myLastGamePlayer.getWeapon();

        String mainWeaponUrl = weaponData.get("image").getAsJsonObject()
                                         .get("url").getAsString();

        String specialWeaponUrl = weaponData.get("specialWeapon").getAsJsonObject()
                                            .get("image").getAsJsonObject()
                                            .get("url").getAsString();

        String subWeaponUrl = weaponData.getAsJsonObject("subWeapon")
                                        .getAsJsonObject("image")
                                        .get("url").getAsString();

        ImageView weaponImage = new ImageView(new Image(mainWeaponUrl));
        weaponImage.setFitHeight(150);
        weaponImage.setFitWidth(150);

        ImageView specialWeaponImage = new ImageView(new Image(specialWeaponUrl));
        specialWeaponImage.setFitHeight(75);
        specialWeaponImage.setFitWidth(75);

        ImageView subWeaponImage = new ImageView(new Image(subWeaponUrl));
        subWeaponImage.setFitHeight(75);
        subWeaponImage.setFitWidth(75);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(subWeaponImage);
        hbox.getChildren().add(specialWeaponImage);

        weaponHolder.getChildren().add(weaponImage);
        weaponHolder.getChildren().add(hbox);

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